# Software Assurance and Fortify

VA [OIS Software Assurance](https://wiki.mobilehealth.va.gov/display/OISSWA/) (SwA) performs design reviews, code security reviews, and code quality reviews. For code security reviews, SwA is interested in reviewing **release** versions of the software.

The tool used for the "[Secure & Quality Code Review](https://wiki.mobilehealth.va.gov/display/OISSWA/Frequently+Asked+Questions)" is Fortify.

## BIP Process

All BIP service projects are created as a collection of maven reactor modules. The process for managing Fortify scan results is:
1. Perform **scan** of the reactor modules - results of the scan are stored in the `target/fortify` folder. This step is intended for use on _local_ and on the _build pipeline_.
2. Perform **merge** of the new `target/fortify/*.fpr` into the `/*.fpr` root FPR. This step is intended for use on _local_. It allows a team to retain the accumulated suppressions, comments, etc. The file should be committed and pushed to the project GitHub repo.

## Install and Run Fortify

There are currently two maven profiles for running Fortify from maven (assuming installation prerequisites are met):

1. `fortify` profile: This profile is in early versions of bip-framework 1.x. It is _deprecated_ and will be removed in a future version. This profile does not help developers with FPR merge. To run the scan: `mvn clean install -DskipIts=true -P fortify`

2. `fortify-sca` and `fortify-merge` profiles: These new profiles are the recommended method of activating fortify scans and merges. There are various ways to run them (some outlined in [Install and Run Fortify](installation-help-guide.md#install-and-run-fortify)), but one common way is: `mvn clean install -Pfortify-sca -Dfortify.bind.phase=package` followed by `mvn antrun:run@fortify-merge -Pfortify-merge`

See [Install and Run Fortify](installation-help-guide.md#install-and-run-fortify) for more details.

## SwA Secure Code Review Submissions

If you used the `bip-archetype-service` pseudo archetype to create your project, the Origin project will have included a `local-dev/swa` directory with tools to automate and simplify preparation for SwA Secure Code Review.

For instructions on how to use the tools, see [SwA Preparation Script](../local-dev/swa/README.md).

## Technical Project Configuration Details for Fortify

This section describes technical details about the configuration and processes of Fortify on builds that are executed on local and in the pipeline.

#### Tools used

BIP projects run Fortify scans using the `sca-maven-plugin` and `sourceanalyzer` app. FPR merges use the `maven-antrun-plugin` and `FPRUtility` app. You should have installed this as part of the [Install and Run Fortify](installation-help-guide.md#install-and-run-fortify) instructions.

#### POM configurations

Fortify execution has been encapsulated in the `fortify-sca` and `fortify-merge` maven profiles. The BIP Framework provides the scanning profile for the service module projects through the `parentpom` projects. However, the service reactor project must declare its own scan aggregation and merge profiles in its own POM.

The Origin pseudo-archetype provides the necessary profiles to new service projects.

**Framework: Parent POM module scans**
The `bip-framework-parentpom/pom.xml` contains a `fortify-sca` profile that executes the Static Code Analyzer on an individual project. Any project that has this pom as its `<parent>` (e.g. the parentpom of your service app) will inherit this profile, so any module having the service app parentpom as its `<parent>` automatically can scan itself.

<details><summary>Click to expand - Framework parentpom profile</summary>

```xml
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
				<fortify.bind.phase>initialize</fortify.bind.phase>
				<fortify.inherit>false</fortify.inherit>
			</properties>
			<build>
				<pluginManagement>
					<plugins>
						<plugin>
							<groupId>com.fortify.sca.plugins.maven</groupId>
							<artifactId>sca-maven-plugin</artifactId>
							<version>${sca-maven-plugin.version}</version>
						</plugin>
					</plugins>
				</pluginManagement>
				<plugins>
					<plugin>
						<groupId>com.fortify.sca.plugins.maven</groupId>
						<artifactId>sca-maven-plugin</artifactId>
						<version>${sca-maven-plugin.version}</version>
						<inherited>${fortify.inherit}</inherited>
						<executions>
							<execution>
								<id>fortify-sca-clean</id>
								<phase>${fortify.bind.phase}</phase>
								<goals>
									<goal>clean</goal>
								</goals>
								<configuration>
									<aggregate>true</aggregate>
									<debug>true</debug>
									<verbose>true</verbose>
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
									<debug>true</debug>
									<verbose>true</verbose>
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
									<debug>true</debug>
									<verbose>true</verbose>
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

**Service: Reactor POM scan aggregation & merge**
The service reactor POM should contain `fortify-sca` and `fortify-merge` profiles. As previously discussed above, these profiles perform the scan and merge functions for BIP projects.

These profiles can be copied and pasted into any BIP service project's reactor POM without any changes. It will just work.

<details><summary>Click to expand - Service reactor profiles</summary>

```xml
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
```

</details>


