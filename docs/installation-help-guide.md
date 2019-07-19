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

Follow the [instructions provided by SwA](https://wiki.mobilehealth.va.gov/display/OISSWA/How+to+download+the+VA-Licensed+Fortify+software) to get a download link and a license file. When running the installer, if options are provided to select / deselect software, ensure that all software is installed.

Make sure Fortify is added to your PATH
* macOS: edit `~/.bash_profile`
	```bash
		# macOS
		export PATH="/Applications/Fortify/Fortify_SCA_and_Apps_18.20/bin:$PATH"
	```

* Windows: add the fortify `\\bin` folder to your "System Properties > Advanced > Environment Variables"

#### Running Fortify

There are many ways to run Fortify on your projects, however the easiest is likely to use a maven profile.
* Make sure all projects have `bip-framework-parentpom` _somewhere_ in their parent hierarchy. An easy, centralized way to do this is to set it as the parent in your project's `./parentpom/pom.xml` file
	```xml
		<!-- ./parentpom/pom.xml -->
		<parent>
			<groupId>gov.va.bip.framework</groupId>
			<artifactId>bip-framework-parentpom</artifactId>
			<version>VERSION</version>
			<relativePath />
		</parent>
	```

	For fortify, the reason for including the framework parent pom is to inherit the base `fortify-sca` profile so that fortify scans can be run on the individual projects.

* Ensure the `fortify-sca` profile (and plugins) is in your project's reactor POM. The configuration in each section can be copied and pasted without change. _**Note that**_ your reactor POM _cannot_ have a `<parent>` tag, or the fortify build will fail (a quirk of the sca-maven-plugin).

	<details><summary>Click to expand - Reactor POM configuration for fortify-sca profile</summary>
	```xml
	<properties>
		<site-maven-plugin.version>0.12</site-maven-plugin.version>
		<sca-maven-plugin.version>18.20</sca-maven-plugin.version>
		<!-- intentionally using old ant-contrib because newer version doesn't work with maven-antrun-plugin -->
		<ant-contrib.version>20020829</ant-contrib.version>
	</properties>

	<!-- TEMPORARY PLUGINS SKIP TO BE REMOVED ONCE NEXUS REPO IS AVAILABLE -->
	<build>
		<pluginManagement>
			<plugins>
				<plugin>
					<groupId>com.fortify.sca.plugins.maven</groupId>
					<artifactId>sca-maven-plugin</artifactId>
					<version>${sca-maven-plugin.version}</version>
				</plugin>
				<plugin>
					<groupId>ant-contrib</groupId>
					<artifactId>ant-contrib</artifactId>
					<version>${ant-contrib.version}</version>
				</plugin>
			</plugins>
		</pluginManagement>
		</plugins>
	</build>

	<profiles>
		<!--
			!!! Fortify profile specifically for this project (not inheritable). !!!
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
					<!--
					This plugin runs the aggregate scan on the reactor project only.
					The parent POM contains the sca-maven-plugin needs for modules.
					-->
					<plugin>
						<groupId>com.fortify.sca.plugins.maven</groupId>
						<artifactId>sca-maven-plugin</artifactId>
						<version>${sca-maven-plugin.version}</version>
						<executions>
							<execution>
								<id>fortify-clean-translate-scan</id>
								<goals>
									<!-- clean: binds to prepare-package phase -->
									<goal>clean</goal>
									<!-- translate: binds to package phase -->
									<goal>translate</goal>
									<!-- scan: binds to integration-test phase -->
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
					<plugin>
						<groupId>org.apache.maven.plugins</groupId>
						<artifactId>maven-antrun-plugin</artifactId>
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
								<id>fortify-copy-or-merge</id>
								<!-- MUST run AFTER fortify-clean-translate-scan execution -->
								<phase>verify</phase>
								<goals>
									<goal>run</goal>
								</goals>
								<configuration>
									<tasks>
										<!-- add the ant tasks from ant-contrib -->
										<taskdef resource="net/sf/antcontrib/antcontrib.properties">
											<classpath refid="maven.dependency.classpath"/>
										</taskdef>
										<echo>+++ Executing ANT target for Fortify copy/merge</echo>
										<echo>+++ Checking file availability of ${project.basedir}/${project.artifactId}.fpr</echo>
										<if>
											<available file="${project.basedir}/${project.artifactId}.fpr" />
											<then>
												<echo>+++ Found file ${project.basedir}/${project.artifactId}.fpr</echo>
												<echo>+++ Executing Fortify merge operation with: FPRUtility -merge -project
													${project.build.directory}/fortify/${project.build.finalName}.fpr -source
													${project.basedir}/${project.artifactId}.fpr -f ${project.basedir}/${project.artifactId}.fpr</echo>
												<exec executable="FPRUtility">
													<arg
														line="-merge -project ${project.build.directory}/fortify/${project.build.finalName}.fpr -source ${project.basedir}/${project.artifactId}.fpr -f ${project.basedir}/${project.artifactId}.fpr" />
												</exec>
											</then>
											<else>
												<echo>+++ Not-found file ${project.basedir}/${project.artifactId}.fpr</echo>
												<echo>+++ Executing file copy with: copy ${project.build.directory}/fortify/${project.build.finalName}.fpr
													${project.basedir}/${project.artifactId}.fpr</echo>
												<copy file="${project.build.directory}/fortify/${project.build.finalName}.fpr"
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

* Execute the maven profile from your project's root folder
 	```bash
	$ mvn clean install -P fortify-sca
	```

	A new `[project-name]-reactor.fpr` file will be created in the project's root folder.

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
