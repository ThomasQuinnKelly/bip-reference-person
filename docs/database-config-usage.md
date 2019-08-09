---
** TODO **
	-- 448/453 revisit database settings based on changes made by Varun
	-- 448/453 describe current and future liquibase uses (current=dev, future=dba/devops)
	-- 448/453 after Varun is done, test and document multi-datasource configuration
	-- 455 - prove testing capabilities, flesh out testing documentation
	-- 454 - research if liquibase script execution can be safely done in clustered / distributed environment
---

# Databases in BIP

Spring offers a variety of ways to configure and use databases. The [Spring Boot features for working with databases](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-sql.html)  and [data access how-to](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-data-access) are useful pages.

Spring boot uses the excellent database support from the Spring Data module. Because BIP application datasource needs can vary widely, spring's database offering is about as granular as is possible. As a result, there is no real value-add that BIP Framework can currently offer for SQL database access.

However to encourage some level of consistency between applications, this document makes general recommendations and offers issue solutions. Working examples of the suggestions found on this page can be found as part of the bip-reference-person **[????](????)** endpoint.

# SQL/Relational Databases

### Recommended SQL Data Access Technologies

Recommended technology choices for relational database support:

* Use [Spring Boot 2.x support for SQL databases](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-sql.html).

* Connection Pool: datasource recommendation is to use [the default (and preferred) spring connection pool](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-sql.html#boot-features-connect-to-production-database), which is [HikariCP](https://github.com/brettwooldridge/HikariCP).

* Statement caching is the responsibility of the database (most JDBC drivers know how to access the db cache). Do not try to cache at the connection pool.

* For query logging, consider [P6Spy](https://github.com/p6spy/p6spy).

* Entity Manager / ORM: default implementation of [Spring JPA](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-sql.html#boot-features-jpa-and-spring-data) with [hibernate](https://hibernate.org/orm/documentation/).

* Transaction Manager:

	* Applications with a single datasource, or multiple-but-independent datasources, the default transaction manager that is preconfigured with [Spring Data JPA](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-sql.html#boot-features-jpa-and-spring-data) is sufficient.

	* Applications with multiple datasources that would benefit from XA transactions should use an embedded transaction manager (recommend [Atomikos](https://www.atomikos.com/) or [Bitronix](https://github.com/bitronix/btm)). For more information about Spring Boot JTA, see [Distributed Transactions with JTA](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-jta.html) and [Configuring Spring and JTA without full Java EE](https://spring.io/blog/2011/08/15/configuring-spring-and-jta-without-full-java-ee/). JTA is not nearly as scary as it once was.

* For database schema versioning and data management [Liquibase](http://www.liquibase.org/) is recommended.

### Supported Databases

Spring Boot supports a good variety of databases out of the box. Database availability will be different between the developer local environment, and servers.  

**Local Databases:**

* Recommendation for in-memory database is the spring default [H2 Database](https://www.h2database.com/html/main.html).

* If a local external database is required, teams can select which to use. Consider using a docker image of the desired local database (postgres, oracle, etc). Examples of acquiring and starting/stopping a postgres docker image:

	* acquire and start postgres docker instance: `docker run --rm --name bip-postgres -p 127.0.0.1:5432:5432/tcp -e POSTGRES_PASSWORD=password -d postgres`

	* stop postgres docker intance: `docker stop bip-postgres`

**External Databases:**

* Developers _may_ be allowed to use any spring-supported database, however doing so can introduce some additional complexity:

	* if there is a possibility that there will be a need to bypass the ORM layer and write SQL directly to the JDBC driver, differences in SQL implementations may cause problems in higher environments.

	* Configurations for JDBC driver, any additional driver customizations, and potentially other tools / implementations will have to take into account the changes in database vendors for the environments.

For these reasons, it is advisable for developers to adopt the database vendor that is used in the upper environments.

Databases that are not directly supported by spring require additional configuration. Their use is discouraged, unless the database is used in higher environments.

### SQL Database Configuration: Single Datasource

As mentioned above, dependencies should be declared in the service `pom.xml`. The URL and other configuration properties for a given environment should be declared in the `[application-name].yml` file for the specific profile (the _default_ profile, _local-int_, _ci_, _dev_, _stage_, _prod_).

#### POM Dependencies

Consider isolating dependency versions in the properties section.

For single-datasource projects, add only the **one** desired driver dependency (in the example below, would copy `groupId` and `artifactId` from the `dependencyManagement` section). If more than one driver appears in the dependencies, experience suggests that _which_ driver is selected by spring is undetermined due to uncertainty around the order classpath lookups in Java. For the same reason, with multiple-datasource projects, add only the necessary drivers to the dependencies.

<details><summary>Click here: POM Dependencies</summary>

```xml
<properties>
	<h2.version>1.4.199</h2.version>
	<hsqldb.version>2.5.0</hsqldb.version>
	<derby.version>10.15.1.3</derby.version>
	<postgres.version>42.2.6</postgres.version>
	<mysql.version>8.0.17</mysql.version>
	<oracledb.version>11.2.0.4</oracledb.version>
</properties>


<dependencies>
	<!-- activate spring boot JPA -->
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-data-jpa</artifactId>
	</dependency>

</dependencies>

<dependencyManagement>
	<!--
		Pick from below (or add your own) and add to dependencies.
		For Oracle and other licensed drivers must be installed in .m2 manually.
		Example
		<dependency>
			<groupId>com.h2database</groupId>
			<artifactId>h2</artifactId>
		</dependency>
	-->

	<!-- in-memory dbs -->
	<dependency>
		<groupId>com.h2database</groupId>
		<artifactId>h2</artifactId>
		<version>${h2.version}</version>
		<scope>runtime</scope>
		<optional>true</optional>
	</dependency>
	<dependency>
		<groupId>org.hsqldb</groupId>
		<artifactId>hsqldb</artifactId>
		<version>${hsqldb.version}</version>
		<scope>runtime</scope>
		<optional>true</optional>
	</dependency>
	<dependency>
		<groupId>org.apache.derby</groupId>
		<artifactId>derby</artifactId>
		<version>${derby.version}</version>
		<scope>runtime</scope>
		<optional>true</optional>
	</dependency>
	<!-- standalone db servers -->
	<dependency>
			<groupId>org.postgresql</groupId>
			<artifactId>postgresql</artifactId>
			<version>${postgres.version}</version>
			<scope>runtime</scope>
		<optional>true</optional>
	</dependency>
	<dependency>
		<groupId>mysql</groupId>
		<artifactId>mysql-connector-java</artifactId>
		<version>${mysql.version}</version>
			<scope>runtime</scope>
		<optional>true</optional>
	</dependency>
	<dependency>
		<groupId>com.oracle</groupId>
		<artifactId>ojdbc8</artifactId>
		<version>${oracledb.version}</version>
			<scope>runtime</scope>
		<optional>true</optional>
	</dependency>
	<dependency>
		<groupId>com.oracle</groupId>
		<artifactId>ojdbc7</artifactId>
		<version>${oracledb.version}</version>
			<scope>runtime</scope>
		<optional>true</optional>
	</dependency>
	<dependency>
		<groupId>com.oracle</groupId>
		<artifactId>ojdbc6</artifactId>
		<version>${oracledb.version}</version>
			<scope>runtime</scope>
		<optional>true</optional>
	</dependency>
</dependencyManagement>
```

</details>

#### Application YAML Configuration

This is where datasource and related configuration properties are set.

**Note:** If a spring supported in-memory database is used (that is, it is included in the dependencies) for local development and testing, _no configuration is required_ for that profile. Spring Boot will automatically configure the URL and default credentials for it.

Some important configuration points:

* If you are using the default connection pool (Hikari), do not use the standard `url` property. [HikariCP uses the `jdbc-url`](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-configure-a-datasource) property as shown in the example below.

* If CLOBs will be used in your schema, hibernate and spring will generate an exception. To avoid this, add the following properties to your application YAML for all profiles:

	```yaml
	# for available hibernate dialects, see https://docs.jboss.org/hibernate/orm/5.0/javadocs/org/hibernate/dialect/package-summary.html
	spring.jpa.database-platform: org.hibernate.dialect.[database]Dialect
	spring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults: false
	```

<details><summary>Click here: Single Datasource Example</summary>

```yaml
### yaml document for default local database configuration
	spring.profiles: default
	spring.profiles.include: remote_client_sims, embedded-redis
	spring:
	datasource:
		## hikariCP uses jdbc-url property, not "url"
		## see https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-configure-a-datasource
		jdbc-url: jdbc:postgresql://localhost:5432/postgres
		## use "driver-class-name" only for drivers not supported by spring
		## Example for Oracle's ojdbc driver ...
#		driver-class-name: oracle.jdbc.driver.OracleDriver
		username: postgres
		password: password
		## Connection Pool settings can be refined by adjusting
		## hikari properties directly
#		hikari:
#			connectionTimeout: 30000
#			maximum-pool-size: 5
#			idleTimeout: 600000
#			maxLifetime: 1800000
	jpa:
		database-platform: org.hibernate.dialect.PostgreSQL9Dialect
		hibernate:
			# do this in all cases to avoid collision with spring or liquibase...
			ddl-auto: none
			# avoid exceptions with CLOBs...
			temp:
				use_jdbc_metadata_defaults: false
## In the logging section of the YAML file, you could add things like...
#logging.level.org.hibernate.SQL=debug
```

</details>

### SQL Database Configuration: Multiple Datasources

If your project requires multiple datasources to participate in XA transactions, there is a little more work involved. Remember that configurations will need to be set up in profiles so they are activated in the appropriate environment.

The short version of what to do:

* For your **primary** datasource, perform the steps in [SQL Database Configuration: Single Datasource](#sql-database-configuration-single-datasource) above.

* Create configuration classes for each database to declare beans for the datasource, entity manager, and transaction manager.

	* Remember to add the `@Primary` annotation to your primary datasource bean.

* Create an autoconfiguration class for each datasource that will participate in the XA transactions, and define which property path applies to each datasource.

* Add configuration properties for each datasource "property path" in the application YAML file.

For reference, check out these links...

* Spring docs has a short page on [Distributed Transactions with JTA](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-jta.html).

* Spring Blog has a detailed article on [Configuring Spring and JTA without full Java EE](https://spring.io/blog/2011/08/15/configuring-spring-and-jta-without-full-java-ee/).

* Baeldung provides a must-read example of how to set up [Spring JPA – Multiple Databases](https://www.baeldung.com/spring-data-jpa-multiple-databases). Classes for configuration are provided, and the boot autoconfiguration is shown in part _**6. Multiple Databases in Spring Boot**_.

# Schema and Data Management

Spring Boot has rudimentary database initialization capabilities. What spring does not provide out of the box is a means for managing schema and data changes over time. [Liquibase](http://www.liquibase.org/) is recommended for its database changeset and schema version management features.

The assumption is that the application uses `spring-boot-starter-data-jpa`.

### Database Management and Versioning

It is worth reviewing the [Liquibase documentation](https://www.liquibase.org/documentation/) to understand changesets, include, preconditions, contexts and parameters.

Both the Liquibase and Spring documentation are very brief and somewhat vague about how to integrate Liquibase into a Spring Boot project. Furthermore, as Spring Boot matures in this integration, handling of Liquibase behaviors and properties is a moving target - there are significant discrepancies between minor spring-boot point releases. As a result, **every source of information about Liquibase integration must be understood in the context of the version of Spring / Spring Boot** used in the discussion or article.

The current recommendation is to create an `[application-name]-db-liquibase` project (maven module) to isolate Liquibase references and configuration.

Below is a summarization of the key points, assuming the application uses `spring-boot-starter-data-jpa`:

* Add Liquibase to the maven dependencies for liquibase (spring-boot 2.1.6 uses Liquibase 3.6.3), and configure the plugin that comes with liquibase-core:

	<details><summary>Click here: Liquibase Maven Dependencies</summary>

	```xml
	<dependency>
			<groupId>org.liquibase</groupId>
			<artifactId>liquibase-core</artifactId>
			<!-- version is supplied by spring boot -->
	</dependency>
	```

	</details>

* Add an empty master changelog as a starting point. Default location is `src/main/resources/db/db.changelog.xml`

	<details><summary>Click here: Empty Master Changelog</summary>

	```xml
	<databaseChangeLog
		xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xmlns:ext="http://www.liquibase.org/xml/ns/dbchangelog-ext"
		xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
			http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.6.xsd
			http://www.liquibase.org/xml/ns/dbchangelog-ext
			http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-ext.xsd">
	</databaseChangeLog>
	```

	</details>

* Spring Boot properties for Liquibase:  As of spring-boot 2.1.6-RELEASE:

	* The most recent round of liquibase property deprecations and their replacement properties can be found in the [META-INF/additional-spring-configuration-metadata.json](https://github.com/spring-projects/spring-boot/blob/v2.1.6.RELEASE/spring-boot-project/spring-boot-autoconfigure/src/main/resources/META-INF/additional-spring-configuration-metadata.json) file (to see what was deprecated in other releases, just change the version number in the URL). Search for `liquibase`.

	* Some useful properties, especially when considering spring profiles and multi-datasource scenarios:

		<details><summary>Click here: Liquibase maven dependency</summary>

		```yaml
		spring.profiles: default
		spring.profiles.include: remote_client_sims, embedded-redis
		spring:
			liquibase:
				enabled: true # Whether to enable Liquibase support.
				change-log: classpath:/db/changelog/db.changelog-master.yaml # Master changelog location.
				check-change-log-location: true # Whether to check that the changelog location exists.
				contexts: [value] # Comma-separated list of runtime contexts to use.
				liquibase-tablespace: [value] # Tablespace to use for Liquibase objects.
				parameters.[*]: [value] # Change replaceable parameters.
				labels: [value] # Comma-separated list of runtime labels to use.
				### For multiple datasources, Spring Boot will tell Liquibase the URL and credentials for the
				### @Primary datasource on which to operate. However, you can manually set the values here...
				url: [value] # JDBC URL of the database to migrate. If not set, the primary configured data source is used.
				user: [value] # Login user of the database to migrate.		
				password: [value] # Login password of the database to migrate.
				#
				### some additional properties that may be useful...
				#
				database-change-log-lock-table: DATABASECHANGELOGLOCK # Name of table to use for tracking concurrent Liquibase usage.
				database-change-log-table: DATABASECHANGELOG # Name of table to use for tracking change history.
				default-schema: [value] # Default database schema.
				drop-first: false # Whether to first drop the database schema.
				liquibase-schema: [value] # Schema to use for Liquibase objects.
				rollback-file: [value] # File to which rollback SQL is written when an update is performed.
				test-rollback-on-update: false # Whether rollback should be tested before update is performed.
		```

		</details>

* By default, Liquibase stores all changelogs in one file. This is convenient, however it does not take long for the file to become large and unmanageable. Configure for separate  _changeset_ files with the databaseChangeLog `includeAll` directive to your configuration:

		<details><summary>Click here: Changelog Location & Separate Changesets</summary>

		```yml
			### yaml document for default local database configuration
			spring.profiles: default
			spring.profiles.include: remote_client_sims, embedded-redis
			spring:
				# ... other data configuration here ...
				liquibase:
					## run liquibase at startup?
					enabled: true
					## change where the master changelog is stored...
					change-log: classpath:/db/changelog/changelog-master.xml
			## direct liquibase to store changesets separately...
		databaseChangeLog:
			- includeAll:
				## this example reads -all- changelog files under the specified path
				path: classpath*:db/changelog/changes/
		### in the logging section of the YAML document, you can add...
		#logging.level.liquibase: DEBUG
		```

		</details>

* If you have multiple datasources, open the configuration class you created for the datasource in question, and mark it with **????**

* Liquibase extensions are available to add missing support for specific databases. If the functionality is needed by a project, the extension plugins can be useful.  See the [Available Plugins](https://liquibase.jira.com/wiki/spaces/CONTRIB/overview) list (right side of the page). The Oracle Extensions plugin may be of interest.

### The maven-liquibase-plugin

The [Maven Liquibase Plugin](https://www.liquibase.org/documentation/maven/index.html) provides a rich suite of goals to developers. Many schema and data management activities can be automated (or at least simplify liquibase executions) in maven profiles.

* Add the liquibase-maven-plugin to your POM (must use spring-boot's `${liquibase.version}`) from liquibase-core. Note that the example below is simple and does not fully configure the plugin, does not configure any goals and executions, or leverage `pluginManagement` (e.g. used in parent pom). Note that JDBC driver dependencies are also declared within the plugin. Refer to the [liquibase-maven-plugin documentation](https://www.liquibase.org/documentation/maven/index.html) for more information.

	<details><summary>Click here: Liquibase maven dependencies</summary>

	```xml
	<dependencies>
			<!-- ... -->
		<dependency>
		    <groupId>org.liquibase</groupId>
		    <artifactId>liquibase-maven-plugin</artifactId>
				<!-- same version as liquibase-core -->
		    <version>${liquibase.version}</version>
		</dependency>
		<!-- ... -->
	</dependencies>
	<!-- ... -->
	<plugins>
	    <plugin>
	        <groupId>org.liquibase</groupId>
	        <artifactId>liquibase-maven-plugin</artifactId>
	        <version>${liquibase.version}</version>
					<dependencies>
							<dependency>
								<groupId>org.postgresql</groupId>
								<artifactId>postgresql</artifactId>
								<version>${postgres.version}</version>
							</dependency>
							<!-- additional dependencies as necessary -->
					</dependencies>
	        <configuration>                  
	            <propertyFile>src/main/resources/liquibase.properties</propertyFile>
							<!-- additional configurations 	as necessary -->
	        </configuration>                
	    </plugin>
	</plugins>
	```

	</details>

* Configure Maven Profiles for expected activities

	<details><summary>Click here: Liquibase maven dependencies</summary>

	```xml
	<properties>
			<!-- Liquibase Properties -->
			<updatesOnly>false</updatesOnly>

			<master-localdb.username>postgres</master-localdb.username>
			<master-localdb.password>password</master-localdb.password>

			<master-liquibase.username>liquibase</master-liquibase.username>
			<master-liquibase.password>liquibase</master-liquibase.password>

			<proddb.username>proddb</proddb.username>
			<proddb.password>Niagraraproboscispoultice</proddb.password>

			<liquibase.url>jdbc:postgresql://localhost:5432/postgres</liquibase.url>
	</properties>
	<profiles>
			<profile>
					<id>db-local-initial-setup</id>
					<build>
							<plugins>
									<plugin>
											<groupId>org.liquibase</groupId>
											<artifactId>liquibase-maven-plugin</artifactId>
											<executions>

													<!-- drop database and liquibase user -->
													<execution>
															<id>drop-local-database</id>
															<goals>
																	<goal>update</goal>
															</goals>
															<phase>clean</phase>
															<configuration>
																	<!-- only if not provided by spring boot, e.g.: <driver>oracle.jdbc.OracleDriver</driver> -->
																	<url>${liquibase.url}</url>
																	<changeLogFile>${project.basedir}/src/main/resources/local/clean/clean.yml</changeLogFile>
																	<promptOnNonLocalDatabase>false</promptOnNonLocalDatabase>
																	<username>${master-localdb.username}</username>
																	<password>${master-localdb.password}</password>
															</configuration>
													</execution>

													<!-- create database -->
													<execution>
															<id>create-local-database</id>
															<goals>
																	<goal>update</goal>
															</goals>
															<phase>process-resources</phase>
															<configuration>
																	<!-- only if not provided by spring boot, e.g.: <driver>oracle.jdbc.OracleDriver</driver> -->
																	<url>${liquibase.url}</url>
																	<changeLogFile>${project.basedir}/src/main/resources/local/install/install.yml</changeLogFile>
																	<promptOnNonLocalDatabase>false</promptOnNonLocalDatabase>
																	<username>${master-localdb.username}</username>
																	<password>${master-localdb.password}</password>
															</configuration>
													</execution>

											</executions>
									</plugin>
							</plugins>
					</build>
			</profile>

			<profile>
					<id>db-local-setup</id>
					<build>
							<plugins>
									<plugin>
											<groupId>org.liquibase</groupId>
											<artifactId>liquibase-maven-plugin</artifactId>
											<executions>

													<!-- Clean -->
													<execution>
															<id>clean-schemas</id>
															<goals>
																	<goal>update</goal>
															</goals>
															<phase>clean</phase>
															<configuration>
																	<!-- only if not provided by spring boot, e.g.: <driver>oracle.jdbc.OracleDriver</driver> -->
																	<url>${liquibase.url}</url>
																	<changeLogFile>${project.basedir}/src/main/resources/liquibase/clean/clean.yml</changeLogFile>
																	<promptOnNonLocalDatabase>false</promptOnNonLocalDatabase>
																	<username>${master-liquibase.username}</username>
																	<password>${master-liquibase.password}</password>
															</configuration>
													</execution>

													<!-- Users -->
													<execution>
															<id>users</id>
															<goals>
																	<goal>update</goal>
															</goals>
															<phase>process-resources</phase>
															<configuration>
																	<!-- only if not provided by spring boot, e.g.: <driver>oracle.jdbc.OracleDriver</driver> -->
																	<url>${liquibase.url}</url>
																	<changeLogFile>liquibase/users/users-all.yml</changeLogFile>
																	<promptOnNonLocalDatabase>false</promptOnNonLocalDatabase>
																	<username>${master-liquibase.username}</username>
																	<password>${master-liquibase.password}</password>
															</configuration>
													</execution>

											</executions>
									</plugin>
							</plugins>
					</build>
			</profile>

			<profile>
					<id>db-install</id>
					<build>
							<plugins>
									<plugin>
											<groupId>org.liquibase</groupId>
											<artifactId>liquibase-maven-plugin</artifactId>
											<dependencies>
													<dependency>
														<groupId>org.postgresql</groupId>
														<artifactId>postgresql</artifactId>
														<version>${postgres.version}</version>
													</dependency>
											</dependencies>
											<executions>

													<!--Install coporate -->
													<execution>
															<id>install-coprorate</id>
															<goals>
																	<goal>update</goal>
															</goals>
															<phase>process-resources</phase>
															<configuration>
																	<!-- only if not provided by spring boot, e.g.: <driver>oracle.jdbc.OracleDriver</driver> -->
																	<url>${liquibase.url}</url>
																	<changeLogFile>liquibase/proddb/install.yml</changeLogFile>
																	<promptOnNonLocalDatabase>false</promptOnNonLocalDatabase>
																	<username>${proddb.username}</username>
																	<password>${proddb.password}</password>
															</configuration>
													</execution>

													<!--Update proddb -->
													<execution>
															<id>update-proddb</id>
															<goals>
																	<goal>update</goal>
															</goals>
															<phase>process-resources</phase>
															<configuration>
																	<!-- only if not provided by spring boot, e.g.: <driver>oracle.jdbc.OracleDriver</driver> -->
																	<url>${liquibase.url}</url>
																	<changeLogFile>liquibase/proddb/update.yml</changeLogFile>
																	<promptOnNonLocalDatabase>false</promptOnNonLocalDatabase>
																	<username>${proddb.username}</username>
																	<password>${proddb.password}</password>
															</configuration>
													</execution>

											</executions>
									</plugin>
							</plugins>
					</build>
			</profile>

			<profile>
					<id>db-update</id>
					<build>
							<plugins>
									<plugin>
											<groupId>org.liquibase</groupId>
											<artifactId>liquibase-maven-plugin</artifactId>
											<dependencies>
													<dependency>
														<groupId>org.postgresql</groupId>
														<artifactId>postgresql</artifactId>
														<version>${postgres.version}</version>
													</dependency>
											</dependencies>
											<executions>

													<!--Update proddb -->
													<execution>
															<id>update-proddb</id>
															<goals>
																	<goal>update</goal>
															</goals>
															<phase>process-resources</phase>
															<configuration>
																	<!-- only if not provided by spring boot, e.g.: <driver>oracle.jdbc.OracleDriver</driver> -->
																	<url>${liquibase.url}</url>
																	<changeLogFile>liquibase/proddb/update.yml</changeLogFile>
																	<promptOnNonLocalDatabase>false</promptOnNonLocalDatabase>
																	<username>${proddb.username}</username>
																	<password>${proddb.password}</password>
															</configuration>
													</execution>
											</executions>
									</plugin>
							</plugins>
					</build>
			</profile>

			<profile>
					<id>db-major-upgrade</id>
					<build>
							<plugins>
									<plugin>
											<groupId>org.liquibase</groupId>
											<artifactId>liquibase-maven-plugin</artifactId>
											<dependencies>
													<dependency>
														<groupId>org.postgresql</groupId>
														<artifactId>postgresql</artifactId>
														<version>${postgres.version}</version>
													</dependency>
											</dependencies>
											<executions>

													<!--Upgrade proddb-->
													<execution>
															<id>upgrade-proddb</id>
															<goals>
																	<goal>update</goal>
															</goals>
															<phase>process-resources</phase>
															<configuration>
																	<!-- only if not provided by spring boot, e.g.: <driver>oracle.jdbc.OracleDriver</driver> -->
																	<url>${liquibase.url}</url>
																	<changeLogFile>liquibase/proddb/upgrade_to_major.yml</changeLogFile>
																	<promptOnNonLocalDatabase>false</promptOnNonLocalDatabase>
																	<username>${proddb.username}</username>
																	<password>${proddb.password}</password>
															</configuration>
													</execution>

													<!--Update proddb -->
													<execution>
															<id>update-proddb</id>
															<goals>
																	<goal>update</goal>
															</goals>
															<phase>process-resources</phase>
															<configuration>
																	<!-- only if not provided by spring boot, e.g.: <driver>oracle.jdbc.OracleDriver</driver> -->
																	<url>${liquibase.url}</url>
																	<changeLogFile>liquibase/proddb/update.yml</changeLogFile>
																	<promptOnNonLocalDatabase>false</promptOnNonLocalDatabase>
																	<username>${proddb.username}</username>
																	<password>${proddb.password}</password>
															</configuration>
													</execution>

											</executions>
									</plugin>
							</plugins>
					</build>
			</profile>

			<profile>
					<id>db-generate-changelog</id>
					<build>
							<plugins>
									<plugin>
											<groupId>org.liquibase</groupId>
											<artifactId>liquibase-maven-plugin</artifactId>
											<dependencies>
													<dependency>
														<groupId>org.postgresql</groupId>
														<artifactId>postgresql</artifactId>
														<version>${postgres.version}</version>
													</dependency>
											</dependencies>
											<executions>
													<execution>
															<id>generate proddb</id>
															<goals>
																	<goal>generateChangeLog</goal>
															</goals>
															<phase>process-resources</phase>
															<configuration>
																	<defaultSchemaName>proddb</defaultSchemaName>
																	<outputDefaultSchema>true</outputDefaultSchema>
																	<outputChangeLogFile>${project.build.directory}/proddb.changelog.yml</outputChangeLogFile>
																	<!-- only if not provided by spring boot, e.g.: <driver>oracle.jdbc.OracleDriver</driver> -->
																	<url>${liquibase.url}</url>
																	<promptOnNonLocalDatabase>false</promptOnNonLocalDatabase>
																	<username>${master-liquibase.username}</username>
																	<password>${master-liquibase.password}</password>
																	<verbose>true</verbose>
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

### SQL Database Testing

Testing of data access should cover everything from the configuration of the access technologies is correct (drivers, pools, caches, etc), to ORM entities and operations.  Testing approaches differ depending on the purpose and environment.

#### Unit Tests

Use [Spring Boot Test](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html#boot-features-testing-spring-boot-applications-testing-autoconfigured-jpa-test). A good starting point for using these tools is in [this article](https://www.baeldung.com/spring-testing-separate-data-source).

* In-memory database should be used so that all ORM layers get coverage.

* Use spring profile(s) to manage datasource configuration.

* When mocking is required, it should be done - generally speaking - as close to the connection pool (or JDBC driver) as possible.

#### Integration Tests

Integration tests are typically only executed as part of deployment into a server environment (e.g. dev).

* These tests should run against a live external database.

* Use spring profile(s) to manage datasource configuration.

* If possible, use Liquibase to manage the state of data in the database.

#### Performance Tests

Performance tests should run in a dedicated Performance Test environment.

* These tests should run against a live external database.

* Use spring profile(s) to manage datasource configuration.

* If possible, use Liquibase to manage the state of data in the database.
