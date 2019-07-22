# Software Assurance and Fortify

VA [OIS Software Assurance](https://wiki.mobilehealth.va.gov/display/OISSWA/) (SwA) performs design reviews, code security reviews, and code quality reviews. For code security reviews, SwA is interested in reviewing **release** versions of the software.

The tool used for the "[Secure & Quality Code Review](https://wiki.mobilehealth.va.gov/display/OISSWA/Frequently+Asked+Questions)" is Fortify.

## Install and Run Fortify

Follow the [Install and Run Fortify](installation-help-guide.md#install-and-run-fortify) instructions.

## SwA Secure Code Review Submissions

If you used the `bip-archetype-service` pseudo archetype to create your project, the Origin project will have included a `local-dev/swa` directory with tools to automate and simplify preparation for SwA Secure Code Review.

For instructions on how to use the tools, see [SwA Preparation Script](../local-dev/swa/README.md).

## Technical Project Configuration Details for Fortify

This section describes technical details about the configuration and processes of Fortify on builds that are executed on local and in the pipeline.

#### Tools used

BIP projects run Fortify scans using the `sca-maven-plugin`. You should have installed this as part of the [Install and Run Fortify](installation-help-guide.md#install-and-run-fortify) instructions. This plugin is (as one might expect) quite strict in its implementation, and (unsurprisingly) is a little quirky. However, it is a pretty good alternative to the burden of maintaining large ANT scripts in a dedicated repo and convoluted maven integration.

#### Tooling impositions

This plugin imposes a couple requirements on reactor projects:
1. The reactor POM **may not** have a `<parent>` tag. Fortify enforces that reactor POMs remain stand-alone, and only coordinate activities for the modules.
2. A `parentpom` module project **must** be used if there is more than one module project.
3. The `parentpom/pom.xml` **must** specify its `<parent>` as `bip-framework-parentpom`.

#### POM configurations

Fortify execution has been encapsulated in the `fortify-sca` maven profile.

From a Fortify perspective, when dealing with maven reactor projects, there are two distinct and separate functions: 1) scanning the modules, and 2) managing the aggregation of scans into one FPR file. This separation of responsibilities results in having to declare configuration in two locations: the reactor, and the parentpom.

**Parent POM = module scans**
The `bip-framework-parentpom/pom.xml` contains a `fortify-sca` profile that executes the Static Code Analyzer on that individual project.

Any project that has this pom as its `<parent>` (e.g. the parentpom of your service app) will inherit this profile, so any module having the service app parentpom as its `<parent>` automatically can scan itself.

<details><summary>Click to expand - parentpom profile</summary>

```xml
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
		</plugins>
	</build>
</profile>
```

</details>

**Reactor = aggregation**
The reactor POM also contains a `fortify-sca` profile, which is structurally independent from the profile of the same name in the parentpom. This POM **cannot** have a `<parent>` (a requirement imposed by Fortify), so it must declare all of its own configuration. During the build, however, maven does merge this profile with the parentpom profile of the same name.

This profile contains two plugins / executions:
* The `sca-maven-plugin` (id `fortify-clean-translate-scan`). The reactor POM **must** declare the _exact same_ `sca-maven-plugin` configuration as in the parentpom. This also provides the reactor with the information needed to _coordinate_ scans within each of the modules.
* The `maven-antrun-plugin` (id `fortify-copy-or-merge`). This plugin merges new scan results into the root `[project-name]-reactor.fpr` file (or copies the scan FPR to the root). If a project contains multiple modules that require scans, additional configuration can be added to ensure all necessary copy/merge operations occur.

This profile can be copied and pasted into any BIP service project's reactor POM, usually without any changes. It will just work.

<details><summary>Click to expand - reactor profile</summary>

```xml
<properties>
	<sca-maven-plugin.version>18.20</sca-maven-plugin.version>
	<!-- intentionally using old ant-contrib because newer version doesn't work with maven-antrun-plugin -->
	<ant-contrib.version>20020829</ant-contrib.version>
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
							<!-- MUST run AFTER fortify-clean-translate-scan execution, "verify" is the first available -->
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
											<echo>+++ Found file: ${project.basedir}/${project.artifactId}.fpr</echo>
											<echo>+++ Executing Fortify merge operation with: FPRUtility -merge -project
												${project.build.directory}/fortify/${project.build.finalName}.fpr -source
												${project.basedir}/${project.artifactId}.fpr -f ${project.basedir}/${project.artifactId}.fpr</echo>
											<exec executable="FPRUtility">
												<arg
													line="-merge -project ${project.build.directory}/fortify/${project.build.finalName}.fpr -source ${project.basedir}/${project.artifactId}.fpr -f ${project.basedir}/${project.artifactId}.fpr" />
											</exec>
										</then>
										<else>
											<echo>+++ Not-found file: ${project.basedir}/${project.artifactId}.fpr</echo>
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

...
