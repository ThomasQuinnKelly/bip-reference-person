---
** TODO **
	-- 448/453 revisit database settings based on changes made by Varun
	-- 448/453 describe current and future liquibase uses (current=dev, future=dba/devops)
	-- 448/453 after Varun is done, test and document multi-datasource configuration
	-- 455 - prove testing capabilities, flesh out testing documentation
	-- 454 - research if liquibase script execution can be safely done in clustered / distributed environment
---

# Databases in BIP

Spring offers a variety of ways to configure and use databases. The [Spring Boot features for working with databases](https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/boot-features-sql.html)  and [data access how-to](https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/htmlsingle/#howto-data-access) are useful pages.

Spring boot uses the excellent database support from the Spring Data module. Because BIP application datasource needs can vary widely, spring's database offering is about as granular as is possible. As a result, there is no real value-add that BIP Framework can currently offer for SQL database access.

However to encourage some level of consistency between applications, this document makes general recommendations and offers issue solutions. Working examples of the suggestions found on this page can be found as part of the bip-reference-person **[????](????)** endpoint.

# SQL/Relational Databases

### Recommended SQL Data Access Technologies

Recommended technology choices for relational database support:

* Use [Spring Boot 2.1.x support for SQL databases](https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/boot-features-sql.html).

* Connection Pool: datasource recommendation is to use [the default (and preferred) spring connection pool](https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/boot-features-sql.html#boot-features-connect-to-production-database), which is [HikariCP](https://github.com/brettwooldridge/HikariCP).

* Statement caching is the responsibility of the database (most JDBC drivers know how to access the db cache). Do not try to cache at the connection pool.

* For query logging, consider [P6Spy](https://github.com/p6spy/p6spy).

* Entity Manager / ORM: default implementation of [Spring JPA](https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/boot-features-sql.html#boot-features-jpa-and-spring-data) with [hibernate](https://hibernate.org/orm/documentation/).

* Transaction Manager:

	* Applications with a single datasource, or multiple-but-independent datasources, the default transaction manager that is preconfigured with [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/boot-features-sql.html#boot-features-jpa-and-spring-data) is sufficient.

	* Applications with multiple datasources that would benefit from XA transactions should use an embedded transaction manager (recommend [Atomikos](https://www.atomikos.com/) or [Bitronix](https://github.com/bitronix/btm)). For more information about Spring Boot JTA, see [Distributed Transactions with JTA](https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/boot-features-jta.html) and [Configuring Spring and JTA without full Java EE](https://spring.io/blog/2011/08/15/configuring-spring-and-jta-without-full-java-ee/). JTA is not nearly as scary as it once was.

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

### SQL Database Configuration: General Configuration

Projects should be arranged such that the same configurations can be used across all modules

* allows for common schema and data setups for the service and its unit tests, integration tests, and performance tests

* dependencies can be provided by `<dependencyManagement` in the reactor POM (or parent POM, if common dependencies have been split out)

* the same strategy should be considered for tools such as Liquibase.


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
		Oracle and other licensed drivers must be installed in .m2 manually.
		Pick from below (or add your own) and add to dependencies.
		Example
		<dependencies>
			...
			<dependency>
				<groupId>com.h2database</groupId>
				<artifactId>h2</artifactId>
			</dependency>
		</dependencies>
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

* If you are using the default connection pool (Hikari), do not use the standard `url` property. [HikariCP uses the `jdbc-url`](https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/htmlsingle/#howto-configure-a-datasource) property as shown in the example below.

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
			## see https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/htmlsingle/#howto-configure-a-datasource
			jdbc-url: jdbc:postgresql://localhost:5432/postgres
			## use "driver-class-name" only for drivers not supported by spring
			## Example for Oracle's ojdbc driver ...
#			driver-class-name: oracle.jdbc.driver.OracleDriver
			username: postgres
			password: password
			## Connection Pool settings can be refined by adjusting
			## hikari properties directly
#			hikari:
#				connectionTimeout: 30000
#				maximum-pool-size: 5
#				idleTimeout: 600000
#				maxLifetime: 1800000
	jpa:
		# the hibernate dialect to use
		database-platform: org.hibernate.dialect.PostgreSQL9Dialect
		hibernate:
			# always turn off hibernate DDL to avoid collision with spring or liquibase...
			ddl-auto: none
			# avoid exceptions with CLOBs...
			temp:
				use_jdbc_metadata_defaults: false
## In the logging section of the YAML file, you could add things like...
#logging.level.org.hibernate.SQL=debug
```

</details>

### SQL Database Configuration: Multiple Datasources

If your project requires multiple datasources to participate in XA transactions, there is a little more work involved. Remember that configurations will need to be set up in spring profiles so they are activated in the appropriate environment.

The Spring Boot docs have short sections on how to [Configure Two DataSources](https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/htmlsingle/#howto-two-datasources), which uses techniques from the prior section [Configure a Custom DataSource](https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/htmlsingle/#howto-configure-a-datasource).

The short version of what to do:

* Perform the steps in the above [SQL Database Configuration: Single Datasource](#sql-database-configuration-single-datasource) section.

	* Add an identifier to the property path to distinguish config for each datasource, e.g. `spring.datasource.ds1.*`, `spring.datasource.ds2.*`

		<details><summary>Click here: Multi-Datasource Configuration</summary>

		```yaml
		spring:
			datasource:
				ds1:
					jdbc-url: jdbc:postgresql://localhost:5432/postgres
					username: postgres
					password: password
				ds2:
					jdbc-url: jdbc:mysql://localhost/testing
					username: dbuser
					password: dbpass
		```

		</details>

* Create `@Configuration` classes for each database. These config classes are to declare beans for the datasource, entity manager, and transaction manager.

	* Set `@ConfigurationProperties` to the appropriate property paths

	* If you intend ever to use the spring boot database initializer, you must add the `@Primary` annotation to the datasource bean that will be updated from `schema.sql` and/or `data.sql`.

		<details><summary>Click here: Multi-Datasource Beans</summary>

		```java
		@Bean
		@Primary
		@ConfigurationProperties("spring.datasource.ds1")
		public DataSourceProperties ds1DataSourceProperties() {
			return new DataSourceProperties();
		}

		@Bean
		@Primary
		@ConfigurationProperties("spring.datasource.ds1.configuration")
		public HikariDataSource ds1DataSource() {
			return ds1DataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
		}

		@Bean
		@ConfigurationProperties("spring.datasource.ds2")
		public DataSourceProperties ds2DataSource() {
			return new DataSourceProperties();
		}

		@Bean
		@ConfigurationProperties("spring.datasource.ds2.configuration")
		public HikariDataSource ds1DataSource() {
			return ds2DataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
		}
		```

		</details>

For additional reference, these links are worth reviewing ...

* Spring docs has a short page on [Distributed Transactions with JTA](https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/boot-features-jta.html).

* Spring Blog has a detailed article on [Configuring Spring and JTA without full Java EE](https://spring.io/blog/2011/08/15/configuring-spring-and-jta-without-full-java-ee/).

* Baeldung provides a good example of how to set up [Spring JPA – Multiple Databases](https://www.baeldung.com/spring-data-jpa-multiple-databases). Classes for configuration are provided, and the boot autoconfiguration is shown in part _**6. Multiple Databases in Spring Boot**_. _Beware of implementation differences between the spring boot version used in their example and your code base._

# Schema and Data Management

Spring Boot has rudimentary database initialization capabilities. What spring does not provide out of the box is a means for managing schema and data changes over time. [Liquibase](http://www.liquibase.org/) is recommended for its database changeset and schema version management features.

The assumption is that the application is uses JPA in preference over raw JDBC. The spring boot dependency is `spring-boot-starter-data-jpa`.

### Database Management and Versioning With Liquibase

It is worth reviewing the [Liquibase documentation](https://www.liquibase.org/documentation/) to understand changesets, include, preconditions, contexts and parameters.

Some general suggestions:

* Use maven profiles to separate the types of operations performed (drop and recreate schema, update schema, populate data, etc). Each profile should have one or more corresponding properties files and changelogs that are named the same or similar as the profile id.

* Use liquibase properties files to specify the JDBC connection, and input / output file names.

* Use liquibase contexts to control which changesets in a changelog should be executed for an operation (clean and populate data, environment-specific operations, version-specific upgrade, etc).

Both the Liquibase and Spring documentation are very brief and somewhat vague about how to integrate Liquibase into a Spring Boot project. Furthermore, as Liqiobase matures and Spring follows with its integrations, handling of Liquibase behaviors and properties is a moving target - there are significant discrepancies between minor spring-boot point releases. As a result, **every source of information about Liquibase integration must be understood in the context of the version of Liquibase and Spring / Spring Boot** used in the discussion or article.

Below is a summarization of the key points, assuming the JDBC connections and other database-specific configuration is already done:

* Add Liquibase to the maven dependencies for liquibase to a root POM (spring-boot 2.1.6 uses Liquibase 3.6.3):

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

	```yml
	databaseChangeLog

	```

	</details>

* Spring Boot properties for Liquibase, as of spring-boot 2.1.6-RELEASE:

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

* By default, Liquibase stores all changelogs in one file. This is convenient, however it does not take long for the file to become large and unmanageable. If you want to split _changeset_ declarations into multiple changelog files, put them in their own directory, and set the databaseChangeLog `includeAll` directive:

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

	* Make sure profiles use the correct `${liquibase.changelog.path.*}` variables in their `<configuration>` properties.

	* Profiles:

		* `liquibase-generate-changelog` creates a changelog file derived from an existing datasource (including any JPA/Hibernate `@Entity` classes).

			* Configure each execution in `src/main/resources/db/${liquibase.changelog.path.*}/liquibase-generate-changelog.properties`

			* Execute: `mvn clean process-classes -Pliquibase-generate-changelog`

			* Produces output in `src/main/resources/db/${liquibase.changelog.path.*}/changelog/liquibase-generate-changelog.yml`

			* Contexts can be added by including `-Dliquibase.contexts=context1[,context2...]` to the maven command

		* `liquibase-create-db-from-changelog` drops any existing database by that name, and recreates if from a changelog file.

			* Configure each execution in `src/main/resources/db/${liquibase.changelog.path.*}/liquibase-create-db-from-changelog.properties`

			* Execute: `mvn clean process-classes -Pliquibase-create-db-from-changelog`

			* Produces output in `src/main/resources/db/${liquibase.changelog.path.*}/changelog/liquibase-create-db-from-changelog.yml`

			* Contexts can be added by including `-Dliquibase.contexts=context1[,context2...]` to the maven command


	<details><summary>Click here: Liquibase maven dependencies</summary>

	```xml
	<!-- TBD -->
	```

	</details>

### Logging Database Activity

[P6spy](https://p6spy.readthedocs.io) - specifically [its spring boot integration](https://github.com/gavlyukovskiy/spring-boot-data-source-decorator) is recommended due to its transparent integration in spring boot applications.  With the p6spy decorator, it is not necessary to pollute the Application class, or provide `@Configuration` beans.

<details><summary>Click here: p6spy configuration</summary>

```xml
	<dependency>
	    <groupId>com.github.gavlyukovskiy</groupId>
	    <artifactId>p6spy-spring-boot-starter</artifactId>
	    <version>1.5.6</version>
	</dependency>
```

</details>

### SQL Database Testing

Testing of data access should cover everything from the configuration of the access technologies is correct (drivers, pools, caches, etc), to ORM entities and operations.  Testing approaches differ depending on the purpose and environment.

#### Unit Tests

Use [Spring Boot Test](https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/boot-features-testing.html#boot-features-testing-spring-boot-applications-testing-autoconfigured-jpa-test). A good starting point for using these tools is in [this article](https://www.baeldung.com/spring-testing-separate-data-source).

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
