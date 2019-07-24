# Installation Help Guide

## Install JDK 8

To install the JDK package, you only have to install the binary files provided by Oracle.

* Download JDK 8 from [Oracle WebSite](https://www.oracle.com/technetwork/java/javase/downloads/index.html): [jdk-8u201 or newer] - you will probably have to search for "jdk-8u".
* Double click on the downloaded installer and follow the screen instructions.

#### On macOS

The JDK package will have been installed in /Library/Java/JavaVirtualMachines. You may have multiple versions of JDK package. To make sure it picks 1.8 version, update `~/.bash_profile` as shown below
```bash
	vi ~/.bash_profile
	# add these two lines to the file ...
	export JAVA_HOME="`/usr/libexec/java_home -v '1.8*'`"
	export JAVA_OPTS="-Xmx2048m -Djava.net.preferIPv4Stack=true"
```

#### On Windows

Run the installer.

Make sure your "System Properties > Advanced > Environment" `JAVA_HOME` points to the correct install of the JDK.

## Install Git

#### On macOS

Open a terminal window.

**Step 1**: Install Homebrew

Homebrew simplifies the installation of software on the Mac OS X operating system.

Copy & paste the following into the terminal window and hit Return.
```bash
		$ ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
		$ brew doctor
```

You will be offered to install the Command Line Developer Tools from Apple. Confirm by clicking Install. After the installation finished, continue installing Homebrew by hitting Return again.

**Step 2**:  Install Git

Copy & paste the following into the terminal window and hit Return.
```bash
	$ brew install git
	$ git --version
```

#### On Windows

Download and install [git for windows](https://gitforwindows.org/).

Take some time to get comfortable with Git BASH and the Git GUI.

## Install and Run Fortify

Of the Fortify suite of products, BIP service apps will typically use SCA (to translate and scan projects), Auditors Workbench (to view FPR files), and the maven plugins (to perform local scans).

1. Follow the [instructions provided by SwA](https://wiki.mobilehealth.va.gov/display/OISSWA/How+to+download+the+VA-Licensed+Fortify+software) to get a download link and a license file. When running the installer, if options are provided to select / deselect software, ensure that all software is installed.

2. Make sure Fortify is added to your PATH

	* macOS: edit your bash profile, e.g. `open -a TextEdit ~/.bash_profile`
		```bash
			# macOS
			export PATH="/Applications/Fortify/Fortify_SCA_and_Apps_18.20/bin:$PATH"
		```

	* Windows: add the `\[APP-FOLDER]\Fortify_SCA_and_Apps_18.20\bin` folder to your "System Properties > Advanced > Environment Variables"

	* Test with the following command from some non-fortify directory: `sourceanalyzer -version`

3. Install the maven plugins into your `.m2` repo:

	* Open a terminal window and `$ cd [downloaded-fortify-folder]/plugins/maven`

	* Run `./install.sh` or `install.bat`

#### Running Fortify

There are many ways to run Fortify on your projects, however the easiest is likely to use a maven profile.

* Make sure all projects have `bip-framework-parentpom` _somewhere_ in their parent hierarchy, e.g. through the reactor POM or the POM of a parentpom project.

* Ensure both the `fortify-sca` and `fortify-merge` profiles exist in your project's reactor POM. The configuration in each section can be copied and pasted without change.

	<details><summary>Click to expand - Reactor POM configuration for fortify-sca profile</summary>
	```xml
	<properties>
		<sca-maven-plugin.version>18.20</sca-maven-plugin.version>
		<!-- intentionally using old ant-contrib because newer version doesn't work with maven-antrun-plugin -->
		<ant-contrib.version>20020829</ant-contrib.version>
		<!-- the maven phase to bind fortify-sca -->
		<fortify.bind.phase>initialize</fortify.bind.phase>
	</properties>

	<profiles>
		<!--
			The fortify-sca profile runs the aggregate scan for all modules.
			If a project believes that the fortify-sca profile requires ANY changes,
			please consult with the BIP Framework development team.
			Base Fortify requirements for all project modules are declared in bip-framework-parentpom.
		-->
		<profile>
			<id>fortify-sca</id>
			<activation>
				<activeByDefault>false</activeByDefault>
			</activation>
			<properties>
				<!-- Don't run tests from SCA - profile should be run as: "mvn install -P fortify-sca" -->
				<skipTests>true</skipTests>
				<skipITs>true</skipITs>
				<skipPerfTests>true</skipPerfTests>
			</properties>
			<build>
				<plugins>
					<plugin>
						<groupId>com.fortify.sca.plugins.maven</groupId>
						<artifactId>sca-maven-plugin</artifactId>
						<version>${sca-maven-plugin.version}</version>
						<executions>
							<execution>
								<id>fortify-sca-clean</id>
								<phase>${fortify.bind.phase}</phase>
								<goals>
									<goal>clean</goal>
								</goals>
								<configuration>
									<aggregate>true</aggregate>
								</configuration>
							</execution>
							<execution>
								<id>fortify-sca-translate</id>
								<phase>${fortify.bind.phase}</phase>
								<goals>
									<goal>translate</goal>
								</goals>
								<configuration>
									<!-- run scans against all reactor projects -->
									<aggregate>true</aggregate>
									<!-- exclude inttest and perftest, as they don't go to prod -->
									<excludes>**/bip-*-inttest/*,**/bip-*-perftest/*</excludes>
								</configuration>
							</execution>
							<execution>
								<id>fortify-sca-scan</id>
								<phase>${fortify.bind.phase}</phase>
								<goals>
									<goal>scan</goal>
								</goals>
								<configuration>
									<!-- run scans against all reactor projects -->
									<aggregate>true</aggregate>
									<!-- exclude inttest and perftest, as they don't go to prod -->
									<excludes>**/bip-*-inttest/*,**/bip-*-perftest/*</excludes>
								</configuration>
							</execution>
						</executions>
					</plugin>
				</plugins>
			</build>
		</profile>
		<profile>
			<id>fortify-merge</id>
			<activation>
				<activeByDefault>false</activeByDefault>
			</activation>
			<properties>
				<!-- Don't run tests from SCA - profile should be run as: "mvn install -P fortify-sca" -->
				<skipTests>true</skipTests>
				<skipITs>true</skipITs>
				<skipPerfTests>true</skipPerfTests>
			</properties>
			<build>
				<plugins>
					<plugin>
						<groupId>org.apache.maven.plugins</groupId>
						<artifactId>maven-antrun-plugin</artifactId>
						<!-- do not run on child modules, just on reactor -->
						<inherited>false</inherited>
						<dependencies>
							<!-- provides ANT branch tags (if/then/else) -->
							<dependency>
								<groupId>ant-contrib</groupId>
								<artifactId>ant-contrib</artifactId>
								<version>${ant-contrib.version}</version>
							</dependency>
						</dependencies>
						<executions>
							<execution>
								<id>fortify-merge</id>
								<goals>
									<goal>run</goal>
								</goals>
								<configuration>
									<tasks>
										<!-- add the ant tasks from ant-contrib -->
										<taskdef resource="net/sf/antcontrib/antcontrib.properties">
											<classpath refid="maven.dependency.classpath" />
										</taskdef>
										<echo>+++ Executing ANT target for Fortify copy/merge</echo>
										<echo>+++ Checking file availability of ${project.basedir}/${project.artifactId}.fpr</echo>
										<if>
											<available file="${project.basedir}/${project.artifactId}.fpr" />
											<then>
												<echo>+++ Found file: ${project.basedir}/${project.artifactId}.fpr</echo>
												<echo>+++ Executing Fortify merge operation with:</echo>
												<echo>      FPRUtility -merge</echo>
												<echo>        -project ${project.build.directory}/fortify/${project.artifactId}-${project.version}.fpr</echo>
												<echo>        -source ${project.basedir}/${project.artifactId}.fpr</echo>
												<echo>        -f ${project.basedir}/${project.artifactId}.fpr</echo>
												<exec executable="FPRUtility">
													<arg
														line="-merge -project ${project.build.directory}/fortify/${project.artifactId}-${project.version}.fpr -source ${project.basedir}/${project.artifactId}.fpr -f ${project.basedir}/${project.artifactId}.fpr" />
												</exec>
											</then>
											<else>
												<echo>+++ Not-found file: ${project.basedir}/${project.artifactId}.fpr</echo>
												<echo>+++ Executing file copy with:</echo>
												<echo>      copy</echo>
												<echo>        ${project.build.directory}/fortify/${project.artifactId}-${project.version}.fpr</echo>
												<echo>        ${project.basedir}/${project.artifactId}.fpr</echo>
												<copy file="${project.build.directory}/fortify/${project.artifactId}-${project.version}.fpr"
													tofile="${project.basedir}/${project.artifactId}.fpr" />
											</else>
										</if>
									</tasks>
								</configuration>
							</execution>
						</executions>
					</plugin>
				</plugins>
			</build>
		</profile>
	</profiles>
	```

	</details>

* Execute the maven profile from your project's root folder to create the FPR in the reactor's `target/fortify` directory, and merge it into the root FPR.  There are two approaches, depending on whether the state of your build project. A simple script has been provided to simplify running the maven commands.
	* If your project has already been built, you can skip building again by using the maven initialize phase:
		 	```bash
			# --- EITHER ---
			# use the script to scan only
			$ ./fortify
			# --- OR ---
			# scan without first building
			$ mvn initialize -P fortify-sca
			# merge the new scan to the root FPR
			$ mvn antrun:run@fortify-merge -Pfortify-merge
			```

	* If your project has not been built, you can build and scan in one step by specifying the maven phase to bind fortify to:
			```bash
			# --- EITHER ---
			# use the script
			$ ./fortify -b
			# --- OR ---
			# build and scan after the build
			$ mvn clean install -Pfortify-sca -Dfortify.bind.phase=package
			# merge the new scan to the root FPR
			$ mvn antrun:run@fortify-merge -Pfortify-merge
			```

	A new `[project-name]-reactor.fpr` file will be created in the project's `target/fortify` folder, and optionally merged into the FPR in the root folder.

## How to connect Maven with Nexus using HTTPS

Maven can be set up to use Nexus repository. When the repository runs on https maven isn’t able to connect to it automatically. The solution to this is to add the server’s certificate to the default Java keystore. When connecting to your https-repository fails, Maven will show you an exception like
```text
[WARNING] Could not transfer metadata gov.va.bip.framework:bip-framework-parentpom:0.0.1-SNAPSHOT/maven-metadata.xml from/to nexus3 (https://nexus.dev.bip.va.gov/repository/maven-public): sun.security.validator
.ValidatorException: PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
```

To resolve this, download the server’s certificate and add it to the default Java keystore. The easiest way to download the certificate is with the Java provided keytool. The following command is an example to download the certificate to a `.pem` file

```bash
keytool -J-Djava.net.useSystemProxies=true -printcert -rfc --sslserver \nexus.dev.bip.va.gov\:443 > cert.pem
```

The proxy-part is optional. Now you downloaded the certificate, you can add it to the keystore with the following command

```bash
keytool -importcert -file cert.pem -alias nexus.dev.bip.va.gov -storepass changeit -keystore $JAVA_HOME/jre/lib/security/cacerts`
```

Note that $JAVA_HOME is the path to the JDK that is known by Maven
