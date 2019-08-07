# Databases in BIP

Spring offers a variety of ways to configure and use databases. The [Spring Boot features for working with databases](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-sql.html)  ... https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-data-access

Spring boot uses the excellent database support from the Spring Data module. Because BIP application datasource needs can vary widely, the spring's database offering is about as granular as is possible. As a result, there is no real value-add that BIP Framework can currently offer for SQL database access.

However to encourage some level of consistency between applications, there is a need for general recommendations. This document attempts to provide recommendations for common data access scenarios, and suggestions for potential outliers.

Working examples of the suggestions found on this page can be found as part of the bip-reference-person **[????](????)** endpoint.

# SQL/Relational Databases

### Recommended SQL Data Access Technologies

Recommended technology choices for relational database support:

* Use [spring boot support for SQL databases](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-sql.html).

* Connection Pool: datasource recommendation is to use [the default (and preferred) spring connection pool](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-sql.html#boot-features-connect-to-production-database), which is [HikariCP](https://github.com/brettwooldridge/HikariCP).

* Entity Manager / ORM: default implementation of [Spring JPA](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-sql.html#boot-features-jpa-and-spring-data) with [hibernate](https://hibernate.org/orm/documentation/).

* Transaction Manager: type of manager depends on needs

	* Applications with a single datasource, or multiple-but-independent datasources, the default transaction manager that is preconfigured with [Spring Data JPA](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-sql.html#boot-features-jpa-and-spring-data) is sufficient.

	* Applications with multiple datasources that require XA transactions should use the spring [Platform Transaction Manager](https://spring.io/blog/2011/08/15/configuring-spring-and-jta-without-full-java-ee/) abstraction of JTA, using an embedded transaction manager (e.g. [Atomikos](https://www.atomikos.com/) or [Bitronix](https://github.com/bitronix/btm)). For more information about JTA in spring boot, see [Distributed Transactions with JTA](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-jta.html) and [Configuring Spring and JTA without full Java EE](https://spring.io/blog/2011/08/15/configuring-spring-and-jta-without-full-java-ee/). JTA is no longer as scary as it once was.

* If the project can benefit from tooling for database schema and data management, [Liquibase](http://www.liquibase.org/) is recommended.

### Supported Databases

Spring Boot supports a good variety of databases out of the box. Database availability will be different between the developer local environment, and servers.  

Dependencies should be declared in the service `pom.xml`. The URL and other configuration properties that are configured for a given environment should be declared in the `[application-name].yml` file for the specific profile (the default profile, local-int, ci, dev, stage, prod).

Local Databases:

* Recommendation for in-memory database is the spring default of h2.

* If a local independent database is required, teams can select which to use. Consider using a docker image of the desired local database (postgres, oracle, etc). Examples of acquiring and starting/stopping a postgres docker image:

	* acquire and start postgres docker instance: `docker run --rm --name bip-postgres -p 127.0.0.1:5432:5432/tcp -e POSTGRES_PASSWORD=password -d postgres`

	* stop postgres docker intance: `docker stop bip-postgres`

* Managing local database schema and data can be challenging. Consider using [Liquibase](http://www.liquibase.org/) (see section below).

* Mocking (for unit tests) should ideally be done as close to the database driver as possible. [Spring Boot Test](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html#boot-features-testing-spring-boot-applications-testing-autoconfigured-jpa-test) is a great place to [start(https://www.baeldung.com/spring-testing-separate-data-source)].

External Databases:

* Developers _may_ be allowed to use any spring-supported database, however doing so can introduce some additional complexity:

	* if there is a possibility that there will be a need to bypass the ORM layer and write SQL directly to the JDBC driver, differences in SQL implementations may cause problems in higher environments.

	* Configurations for JDBC driver, any additional driver customizations, and potentially other tools / implementations will have to take into account the changes in database vendors for the environments.

For these reasons, it is advisable for developers to adopt the database vendor that is used in the upper environments.

Databases that are not directly supported by spring require additional configuration. Their use is discouraged, unless the database is used in higher environments.

### SQL Database Configuration: Single Datasource

As mentioned above, dependencies should be declared in the service `pom.xml`. The URL and other configuration properties that are configured for a given environment should be declared in the `[application-name].yml` file for the specific profile (the default profile, local-int, ci, dev, stage, prod).

#### POM Dependencies

Consider isolating dependency versions in the properties section

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

* If you are using the default connection pool (Hikari), do not use the standard `url` property. [HikariCP uses the `jdbc-url`](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-configure-a-datasource) as shown in the example below.

* If CLOBs will be used in your schema, hibernate and spring will generate an exception. To avoid this, add the following properties to your application YAML for all profiles:

	```yaml
	spring.jpa.database-platform: org.hibernate.dialect.PostgreSQL9Dialect # or the hibernate dialect for your chosen database
	spring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults: false
	```

<details><summary>Click here: Single Datasource Example</summary>

```yaml
---
### yaml document for default local database configuration
spring.profiles: default
spring.profiles.include: remote_client_sims, embedded-redis
spring:
	datasource:
		## hikariCP uses jdbc-url property, not "url"
		## see https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-configure-a-datasource
		jdbc-url: jdbc:postgresql://localhost:5432/
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
		properties:
			hibernate:
				# provide correct dialect to avoid exceptions with CLOBs...
				dialect: org.hibernate.dialect.PostgreSQLDialect
		hibernate:
			# do this in all cases to avoid collision with spring or liquibase...
			ddl-auto: none
			# avoid exceptions with CLOBs...
			temp:
				use_jdbc_metadata_defaults: false
## In the logging section of the YAML file, you could add things like...
#logging.level.org.hibernate.SQL=debug
---
```

</details>

### SQL Database Configuration: Multiple Datasources

If your project requires multiple datasources to participate in XA transactions, there is a little more work involved. Remember that configurations will need to be set up in profiles so they are activated in the appropriate environment.

The short version of what to do:

* For your **primary** datasource, perform the steps in [SQL Database Configuration: Single Datasource](#sql-database-configuration-single-datasource) above.

* Create configuration classes for each database to declare beans for the datasource, entity manager, and transaction manager.

	* Remember to add the `@Primary` annontation to your primary datasource bean.

* Create an autoconfiguration class for each datasource that will participate in the XA transactions, and define which property path applies to each datasource.

* Add configuration properties for each datasource "property path" in the application YAML file.

For reference, check out these links...

* Spring docs has a short page on [Distributed Transactions with JTA](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-jta.html).

* Spring Blog has a detailed article on [Configuring Spring and JTA without full Java EE](https://spring.io/blog/2011/08/15/configuring-spring-and-jta-without-full-java-ee/).

* Baeldung provides a must-read example of how to set up [Spring JPA – Multiple Databases](https://www.baeldung.com/spring-data-jpa-multiple-databases). Classes for configuration are provided, and the boot autoconfiguration is shown in part "6. Multiple Databases in Spring Boot".

# Schema and Data Management

Spring Boot has rudimentary database initialization capabilities. What spring does not provide out of the box is a means for managing schema and data changes over time. For that, Spring Boot provides support for Liquibase and FlyawayDB. [Liquibase](http://www.liquibase.org/) is recommended. Flyaway changelogs must be in SQL, whereas Liquibase changelogs can additionally use XML, YAML, and JSON.

Depending on project needs, Liquibase might be overkill, and it may be enough to simply use spring's built in database initializer. Both approaches are discussed below. The [spring database initialization how-to](https://docs.spring.io/spring-boot/docs/current/reference/html/howto-database-initialization.html) page is overly brief and incomplete.

The assumption is that the application uses `spring-boot-starter-data-jpa`.

### Native Spring DB Initialization

At runtime, spring will automatically process certain SQL files that it finds on the classpath. JPA applications can configure spring or hibernate to process the SQL files, **but not both**. It is recommended to disable hibernate processing with `spring.jpa.hibernate.ddl-auto: none`.

When a spring app starts up, an optional "platform" designator can be defined and used to specify, for example, vendor-specific versions of the SQL file(s). To enable the optional platform feature, define `spring.datasource.platform: [vendor-name]`, and add `-${platform}` to the file names:

* DDL (schema) file: `schema[-${platform}].sql`

* DML (data) file: `data[-${platform}].sql`

To tell spring what type of database it is allowed to initialize, set `spring.datasource.initialization-mode: [always|embedded|never]` (if you use Spring Batch, you must decide to use spring's datasource, or batch's `spring.batch.initialize-schema: [always|embedded|never]` ... you cannot use both). Control the application startup behavior when scripts cause errors with `spring.datasource.continue-on-error: [true|false]`.

### Liquibase DB Management

If ongoing schema and data management is required, Liquibase is a powerful tool that keeps changelogs of your database DDL and DML. It provides schema creation and updates/migrations, schema and data rollbacks, db documentation, diff, preconditions and contexts, replaceable parameters, and much more - it offers many capabilities, and is worth reviewing [the documentation](https://www.liquibase.org/documentation/). The [Maven Liquibase Plugin](https://www.liquibase.org/documentation/maven/index.html) provides a rich suite of goals to developers. Many schema and data management activities can be automated in maven profiles.

Both the Liquibase and Spring documentation are very brief and somewhat vague about how to integrate Liquibase into a Spring Boot project. Baeldung offers a good example of how to set up and [use Liquibase in a simple Spring Boot project](https://www.baeldung.com/liquibase-refactor-schema-of-java-app), but it does not address many details required for an enterprise project.

Below is a summarization of the key points, assuming the application uses `spring-boot-starter-data-jpa`:

* Add Liquibase to the maven dependencies (spring-boot 2.1.6 uses Liquibase 3.6.3):

	<details><summary>Click here: Liquibase maven dependency</summary>

	```xml
		<dependency>
				<groupId>org.liquibase</groupId>
				<artifactId>liquibase-core</artifactId>
				<!-- version is supplied by spring boot -->
		</dependency>
	```

	</details>

* Spring Boot properties for Liquibase: As Spring Boot matures in this integration, handling of Liquibase behaviors and properties seems to be a moving target, and there are significant discrepancies between minor point releases. As of spring-boot 2.1.6-RELEASE:

	* The most recent round of liquibase property deprecations and their replacement properties can be found in the [META-INF/additional-spring-configuration-metadata.json](https://github.com/spring-projects/spring-boot/blob/v2.1.6.RELEASE/spring-boot-project/spring-boot-autoconfigure/src/main/resources/META-INF/additional-spring-configuration-metadata.json) file (to see other releases, just change the version number in the URL). Search for `liquibase`.

	* Some useful properties, especially when considering spring profiles and mulit-datasource scenarios:

	<details><summary>Click here: Liquibase maven dependency</summary>

	```yaml
	spring:
		liquibase:
			enabled=true # Whether to enable Liquibase support.
			change-log=classpath:/db/changelog/db.changelog-master.yaml # Change log configuration path.
			check-change-log-location=true # Whether to check that the change log location exists.
			contexts= # Comma-separated list of runtime contexts to use.
			liquibase-tablespace= # Tablespace to use for Liquibase objects.
	spring.liquibase.database-change-log-lock-table=DATABASECHANGELOGLOCK # Name of table to use for tracking concurrent Liquibase usage.
	spring.liquibase.database-change-log-table=DATABASECHANGELOG # Name of table to use for tracking change history.
	spring.liquibase.default-schema= # Default database schema.
	spring.liquibase.drop-first=false # Whether to first drop the database schema.
	spring.liquibase.labels= # Comma-separated list of runtime labels to use.
	spring.liquibase.liquibase-schema= # Schema to use for Liquibase objects.
	spring.liquibase.parameters.*= # Change log parameters.
	spring.liquibase.password= # Login password of the database to migrate.
	spring.liquibase.rollback-file= # File to which rollback SQL is written when an update is performed.
	spring.liquibase.test-rollback-on-update=false # Whether rollback should be tested before update is performed.
	spring.liquibase.url= # JDBC URL of the database to migrate. If not set, the primary configured data source is used.
	spring.liquibase.user= # Login user of the database to migrate.		
	```

	</details>

* By default, Liquibase stores all changelogs in one file. This is convenient, however it does not take long for the file to become large and unmanageable. A couple options are presented below, your mileage may vary.

	* If you want multiple _changeset_ files, [this article](https://objectpartners.com/2018/05/09/liquibase-and-spring-boot/) explains one way to get Spring to process them. An example of what this would look like is:

		<details><summary>Click here: Changelog Location & Separate Changesets</summary>

		```yml
		---
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
			# the path -may- work without the "classpath*:" part
			path: classpath*:db/changelog/changes/
	### in the logging section of the YAML document, you can add...
	#logging.level.liquibase: DEBUG
		```

		</details>

	* If the above method does not work, [this article](https://medium.com/@harittweets/evolving-your-database-using-spring-boot-and-liquibase-844fcd7931da) implies that Spring will automatically process separate _changeset_ files by simply defining a custom master changelog (he uses a properties file instead of yaml).

* If you have multiple datasources, open the configuration class you created for the datasource in question, and mark it with **????**
