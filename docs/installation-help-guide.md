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

## Running Sonar

A SonarQube docker image is added to your docker environment when you run a maven build without specifying to skip the docker build.

* Start the docker container for sonar individually with this command:
	```bash
	$ docker-compose -f [path]/bip-reference-person/local-dev/sonar/docker-compose.yml up --build -d
	```

* Wait for the container to spin up. It is accessible at http://localhost:9000 once it is is running.

* Run a sonar scan on your project(s)
	```bash
	$ mvn clean install # must build the project and run unit tests first
	$ mvn sonar:sonar
	```

* View the results at http://localhost:9000

* Stop SonarQube individually with this command:
	```bash
	$ docker-compose -f [path]/bip-reference-person/local-dev/sonar/docker-compose.yml down --rmi all -v
	```

## Install and Run Fortify

Of the Fortify suite of products, BIP service apps will typically use the `sca-maven-plugin` (which uses Fortify's Source Analyzer) to perform local scans, the `maven-ant-plugin` and Fortify's FPRUtility to merge FPR files, and Fortify's Auditors Workbench to view FPR files.

1. Follow the [instructions provided by SwA](https://wiki.mobilehealth.va.gov/display/OISSWA/How+to+download+the+VA-Licensed+Fortify+software) to get a download link and a license file. When running the installer, if options are provided to select / deselect software, ensure that all software is installed.

2. Make sure Fortify is added to your PATH

	* macOS: edit your bash profile, e.g. `open -a TextEdit ~/.bash_profile`, for example
	```bash
	# add fortify to path
	export PATH="/Applications/Fortify/Fortify_SCA_and_Apps_19.1.0/bin:$PATH"
	```

	* Windows: add the `\[APP-FOLDER]\Fortify_SCA_and_Apps_19.1.0\bin` folder to your "System Properties > Advanced > Environment Variables"

	* Test with the following command from some non-fortify directory: `sourceanalyzer -version`

3. Install the maven plugins into your `.m2` repo:

	* Open a terminal window and `$ cd [downloaded-fortify-folder]/plugins/maven`

	* Run `./install.sh` or `install.bat`

#### Running Fortify

There are many ways to run Fortify on your projects, however the easiest is likely to use a maven profile.

* Make sure all projects have `bip-framework-parentpom` _somewhere_ in their parent hierarchy, e.g. through the the `parentpom/pom.xml`.

* Ensure both the `fortify-sca` and `fortify-merge` profiles exist (exactly as shown in the collapsible section below) in your project's reactor POM. The configuration in each section can be copied and pasted without change.

<details><summary>Click to expand - Reactor POM configuration for fortify-sca profile</summary>

```xml
<profiles>
	<!--
		The fortify-sca profile runs the aggregate scan for all modules.
		If a project team believes that the fortify-sca profile requires ANY changes,
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

* Make sure the project has been fully built on the latest code. Then execute the maven profile from your project's root folder to create the FPR in the reactor's `target/fortify` directory, and merge it into the root FPR.

	```bash
	# --- Using the provided script ---
	# Example: build the project, and scan & merge 
	$ ./fortify -b
	# Example: only scan and merge code that has already been built
	$ ./fortify
	# --- Manual execution ---
	# build (if necessary)
	$ mvn clean install -U
	# scan the built project modules
	$ mvn initialize -Pfortify-sca
	# merge the new scan into the root FPR
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

## Creating Personal Access Token to connect to GitHub

Creating Personal Access Token is required to perform GIT operations using HTTPS for any private repositories

Execute the steps below only if you haven't already performed these steps.

* Developers using Windows, Mac and Linux:

  * Creating a Personal Token
    1. [Verify your email address](https://help.github.com/articles/verifying-your-email-address/), if it hasn't been verified yet on GitHub.
    2. In the upper-right corner of any page, click your profile photo, then click Settings.
    3. In the left sidebar, click Personal access tokens.
    4. Click Generate new token.
    5. Give your token a descriptive name.
    6. Select the scopes, or permissions, you'd like to grant this token. To use your token to access repositories from the command line, select repository.
    7. Click Generate token.
    8. Copy the token to your clipboard. For security reasons, after you navigate off the page, you will not be able to see the token again.

    **Warning**: Treat your tokens like passwords and keep them secret. When working with the API, use tokens as environment variables instead of hard-coding them into your programs.

  * Caching your GitHub password in Git
    1. If you're cloning GitHub repositories using HTTPS, you can use a credential helper to tell Git to remember your GitHub username and password every time it talks to GitHub.
    2. Follow the steps mentioned [here](https://help.github.com/en/articles/caching-your-github-password-in-git)

  * You could also refer to Instructions on GitHub to [Create Personal Token](https://help.github.com/articles/creating-a-personal-access-token-for-the-command-line/)
