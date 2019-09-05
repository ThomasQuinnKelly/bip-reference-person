# Databases in BIP

Spring offers a variety of ways to configure and use databases. The [Spring Boot features for working with databases](https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/boot-features-sql.html)  and [data access how-to](https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/htmlsingle/#howto-data-access) are useful pages.

The current focus is on relational databases. BIP Framework, the Reference Person application, and the BIP Archetype currently do not include support for NoSQL / flat-file databases.

BIP Framework provides Maven Dependency Management for common relational databases and JDBC drivers that are known to be in use in BIP projects:
- H2 (embedded)
- Postgres
- Oracle

### Recommended SQL Data Access Technologies

Recommended technology choices for relational database support:

* Use [Spring Boot 2.1.x support for SQL databases](https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/boot-features-sql.html).

* Connection Pool: datasource recommendation is to use [the default (and preferred) spring connection pool](https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/boot-features-sql.html#boot-features-connect-to-production-database), which is [HikariCP](https://github.com/brettwooldridge/HikariCP).

* Statement caching is the responsibility of the database (most JDBC drivers know how to access the db cache). Do not try to cache at the connection pool.

* For query logging, consider using the existing Spring/Hibernate logging mechanisms. If more sophisticated query is required, tools such as [P6Spy](https://github.com/p6spy/p6spy) may be worth investigating, but will complicate your project.

	<details><summary>Click here: Spring/Hibernate logging</summary>

	```properties
	# in app yaml - directly print to stdout ...
	spring.jpa.show-sql=true
	spring.jpa.properties.hibernate.format_sql=true
	# in app yaml - use more efficient logger
	logging.level.org.hibernate.SQL=DEBUG
	logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
	```

	</details>

* Entity Manager / ORM: default implementation of [Spring JPA](https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/boot-features-sql.html#boot-features-jpa-and-spring-data) with [hibernate](https://hibernate.org/orm/documentation/).

* Transaction Manager:

	* Applications with a single datasource, or multiple-but-independent datasources, the default transaction manager that is preconfigured with [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/boot-features-sql.html#boot-features-jpa-and-spring-data) is sufficient.

	* Applications with multiple datasources that would benefit from XA transactions should use an embedded transaction manager (recommend [Atomikos](https://www.atomikos.com/) or [Bitronix](https://github.com/bitronix/btm)). For more information about Spring Boot JTA, see [Distributed Transactions with JTA](https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/boot-features-jta.html) and [Configuring Spring and JTA without full Java EE](https://spring.io/blog/2011/08/15/configuring-spring-and-jta-without-full-java-ee/).

		**XA NOTE:** If XA transaction management is required, it will likely be necessary to add - at a minimum - a `JtaTransactionManager` bean to your application Configuration class.

* For database schema versioning and data management [Liquibase](http://www.liquibase.org/) is recommended, and is included in the Reference Person sample application.

* DB admin tools are plentiful, and usually quite interoperable. If you need (or just want) something that can handle multiple vendors, you might try [DBeaver](https://dbeaver.io/) (has an Eclipse plugin too), [OmniDB](https://omnidb.org/en/), or other similar tool.

### Supported Databases

Spring Boot supports a good variety of databases out of the box. Database availability will be different between the developer local environment, and servers.  

**Local Databases:**

* Recommendation for in-memory database is the spring default [H2 Database](https://www.h2database.com/html/main.html).

* If a local external database is required, teams can select which to use. Consider using a docker image of the desired local database (postgres, oracle, etc). The two bullets below show and example of acquiring and starting/stopping a postgres docker image:

	* acquire and start postgres docker instance: `docker run --rm --name bip-postgres -p 127.0.0.1:5432:5432/tcp -e POSTGRES_PASSWORD=password -d postgres:latest`

	* stop postgres docker instance: `docker stop bip-postgres`

**External Databases:**

* Developers _may_ be allowed to use any spring-supported database, however doing so can introduce some additional complexity:

	* if there is a possibility that there will be a need to bypass the ORM layer and write SQL directly to the JDBC driver, differences in SQL implementations may cause problems in higher environments.

	* Configurations for JDBC driver, any additional driver customizations, and potentially other tools / implementations will have to take into account the changes in database vendors for the environments.

For these reasons, it is advisable for developers to adopt the database vendor that is used in the upper environments, or that can easily be managed by your database tools (e.g. Liquibase).

Databases that are not directly supported by spring require additional configuration. Their use is discouraged, unless the database is used in higher environments.

### SQL Database Configuration

Configuration for a database takes place in the POM for build time dependencies, and in the application yaml file for runtime configuration.

Multiple datasources can be managed in the application yaml using spring profiles. In most cases, no java code would be required. An example of this approach can be found in the [bip-reference-pserson service POM](https://github.com/department-of-veterans-affairs/bip-reference-person/blob/master/bip-reference-person/pom.xml) (search for "database related" to quickly find the relevant entries). Comments are provided in this POM to explain how each stage of the configuration works.

To the extent possible, keep common schema and data setups for the service and its unit tests, integration tests, and performance tests.

### SQL Database Configuration

BIP Framework provides managed dependencies for H2, Postgres, and Oracle.

<details><summary>Click here: BIP Framework managed dependencies</summary>

	```xml
	<properties>
		...
		<h2.version>1.4.199</h2.version>
		<postgresql.version>42.2.6</postgresql.version>
		<ojdbc6.version>11.2.0.4</ojdbc6.version><!-- also 11.1.0.7 -->
		<ojdbc7.version>12.1.0.2</ojdbc7.version><!-- also 12.1.0.2 -->
		<ojdbc8.version>12.2.0.1</ojdbc8.version><!-- also 18.1.0, 19.3 -->
		<ojdbc10.version>19.3</ojdbc10.version>
		<!-- Liquibase -->
		<liquibase-core.version>${liquibase.version}</liquibase-core.version>
		<liquibase-hibernate5.version>3.8</liquibase-hibernate5.version>
		<!--
			For liquibase-maven-plugin: versions derived from spring dependencies.
			Required dependencies for various Liquibase Change operations, more may be needed.
			THESE NEED TO BE ADJUSTED IF SPRING IS UPGRADED
		-->
		<validation-api.version>2.0.1.Final</validation-api.version>
		<spring-core.version>5.1.8.RELEASE</spring-core.version>
		<spring-data-jpa.version>2.1.9.RELEASE</spring-data-jpa.version>
	</properties>
	<dependencyManagement>
		<dependencies>
			...
			<dependency>
				<groupId>com.h2database</groupId>
				<artifactId>h2</artifactId>
				<scope>runtime</scope>
				<version>${h2.version}</version> <!-- Need version >= 1.4.198 for row_number analytic -->
			</dependency>
			<dependency>
				<groupId>org.postgresql</groupId>
				<artifactId>postgresql</artifactId>
				<version>${postgresql.version}</version>
			</dependency>
			<dependency>
				<!-- TODO Oracle 11.1.x is on TRM Unapproved list -->
				<!-- TODO Oracle 11.2.x is on TRM Divest schedule -->
				<groupId>com.oracle</groupId>
				<artifactId>ojdbc6</artifactId>
				<version>${ojdbc6.version}</version>
			</dependency>
			<dependency>
				<!-- TODO Oracle 12.1.x is on TRM Divest schedule -->
				<groupId>com.oracle</groupId>
				<artifactId>ojdbc7</artifactId>
				<version>${ojdbc7.version}</version>
			</dependency>
			<dependency>
				<!-- TODO Oracle 12.2.x is on TRM Divest schedule -->
				<!-- Oracle 18.1.0 or 19.3 is TRM Approved -->
				<groupId>com.oracle</groupId>
				<artifactId>ojdbc8</artifactId>
				<version>${ojdbc8.version}</version>
			</dependency>
			<dependency>
				<!-- Oracle 19.3 is TRM Approved -->
				<groupId>com.oracle</groupId>
				<artifactId>ojdbc10</artifactId>
				<version>${ojdbc10.version}</version>
			</dependency>

			<dependency>
				<groupId>org.liquibase</groupId>
				<artifactId>liquibase-core</artifactId>
				<version>${liquibase-core.version}</version>
			</dependency>
			<dependency>
				<groupId>org.apache.logging.log4j</groupId>
				<artifactId>log4j-liquibase</artifactId>
				<exclusions>
					<exclusion>
						<groupId>org.liquibase</groupId>
						<artifactId>liquibase-core</artifactId>
					</exclusion>
				</exclusions>
			</dependency>
			<dependency>
				<groupId>org.liquibase.ext</groupId>
				<artifactId>liquibase-hibernate5</artifactId>
				<version>${liquibase-hibernate5.version}</version>
				<exclusions>
					<exclusion>
						<groupId>org.liquibase</groupId>
						<artifactId>liquibase-core</artifactId>
					</exclusion>
				</exclusions>
			</dependency>
		</dependencies>
	</dependencyManagement>
	```

</details>

#### Reactor POM Dependencies

Add dependencies in the reactor POM to provide them to any module in the project (particularly important for the "db" project that will be discussed later).

<details><summary>Click here: Reactor POM dependencies</summary>

	```xml
	<dependency>
		<groupId>org.liquibase</groupId>
		<artifactId>liquibase-core</artifactId>
	</dependency>
	<dependency>
		<!-- currently backward compatible to PostgreSQL 8.2 -->
		<groupId>org.postgresql</groupId>
		<artifactId>postgresql</artifactId>
	</dependency>
	<dependency>
		<groupId>com.h2database</groupId>
		<artifactId>h2</artifactId>
	</dependency>
	<dependency>
		<!-- TODO Oracle 11.1.x is on TRM Unapproved list -->
		<!-- TODO Oracle 11.2.x is on TRM Divest schedule -->
		<groupId>com.oracle</groupId>
		<artifactId>ojdbc6</artifactId>
	</dependency>
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-data-jpa</artifactId>
		<exclusions>
			<exclusion>
				<groupId>org.liquibase</groupId>
				<artifactId>liquibase-core</artifactId>
			</exclusion>
		</exclusions>
	</dependency>
	```

</details>

#### Service POM Dependencies

With the dependencies declared in the reactor POM, no dependencies are required in the service POM.

#### Application YAML Configuration

This is where datasource and related configuration properties are set. It is recommended to declare datasource configurations inside spring profiles. This allows multiple datasources to be declared without having to add any java beans or configurations. The exception to this is if an XA transaction manager is required, which will - at the least - require adding a `JtaTransactionManager` bean to your application Configuration class.

In the example below (in the collapsed section), multiple datasources are declared, and then applied to environments as needed. The comments describe each part of the configuration in detail.

<details><summary>Click here: application yaml - database configuration</summary>

```yaml
#########################################################################
#########################################################################
####
####    DATABASE RELATED CONFIGURATION
####    1. Generically define database types
####    2. Declaration of database instances
####    3. Apply database configs to execution environments
####
#### see https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-configure-a-datasource
#########################################################################
#########################################################################

#### >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
####    1. Generically define database types
####       - Declare the parts of spring jpa and datasource config that
####         are common to a specific type of database
####       - These generic definitions can be reused later when we
####         declare specific database instances
#### >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

---
### Generic H2 In-Memory database definition (no url defined in this profile)
### Ensure driver dependency is included in pom.xml
spring.profiles: dbtype-h2
##
## By default, Spring Boot configures the application to connect to an
## in-memory store with the username sa and an empty password. We can change
## those parameters by adding the following properties
  #spring.datasource.url=jdbc:h2:[mem|file]:[path/to/]dbinstancename
  #spring.datasource.driverClassName=org.h2.Driver
  #spring.datasource.username=sa
  #spring.datasource.password=
  #spring.jpa.database=h2
## "mem" dbs are ephemeral and do not survive spring restarts, "file" dbs do survive
spring.jpa:
    database: h2
    generate-ddl: true
    hibernate:
      ddl-auto: none
      connection:
        provider_class: org.hibernate.hikaricp.internal.HikariCPConnectionProvider
spring.datasource:
    type: com.zaxxer.hikari.HikariDataSource
    ### skip spring db init - using liquibase
    initialization-mode: never
---
### Generic Postgres database definition (no url defined in this profile)
### Ensure driver dependency is included in pom.xml
spring.profiles: dbtype-postgres
spring.jpa:
    database: postgresql
    generate-ddl: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQL95Dialect
    hibernate:
      ddl-auto: none
      connection:
        provider_class: org.hibernate.hikaricp.internal.HikariCPConnectionProvider
spring.datasource:
    type: com.zaxxer.hikari.HikariDataSource
    ### skip spring db init - using liquibase
    initialization-mode: never
---
### Generic Oracle 11.2 / ojdbc6 database definition (no url defined in this profile)
### Ensure driver dependency is included in pom.xml
spring.profiles: dbtype-oracle6
spring.jpa:
    database: oracle
    generate-ddl: false
    hibernate:
      ddl-auto: none
      connection:
        provider_class: org.hibernate.hikaricp.internal.HikariCPConnectionProvider
spring.datasource:
    type: com.zaxxer.hikari.HikariDataSource
    ### skip spring db init - using liquibase
    initialization-mode: never
    driver-class-name: oracle.jdbc.OracleDriver
---

#### >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
####    2. Declaration of database instances
####       - Use previously defined database types to declare profiles
####         for specific database instances and common liquibase params
####       - These instance declarations will later be used to configure
####         specific execution environments
#### >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

## Connection Pool settings can be refined on any below profile by adjusting
## hikari properties directly
## A list of all Hikari parameters with a good explanation is available on
## https://github.com/brettwooldridge/HikariCP#configuration-knobs-baby
#  hikari:
#    connection-timeout: 30000 # maximum number of milliseconds that a client will wait for a connection from connection pool
#    minimum-idle: 5 # minimum number of idle connections that is maintained by HikariCP in connection pool
#    maximum-pool-size: 12 # configures the maximum pool size
#    idle-timeout: 300000 # maximum amount of time in milliseconds that a connection is allowed to sit idle in connection pool
#    max-lifetime: 1200000 #  maximum life time in milliseconds of a connection in pool after it is closed
#    auto-commit: true # configures the default auto-commit behavior of connections returned from pool. Default value is true.

spring.profiles: db-docslocal
spring.profiles.include: dbtype-h2
#spring.profiles.include: dbtype-postgres
db.instance.name: docslocal
spring.datasource:
    url: jdbc:${spring.jpa.database}:mem:${db.instance.name}
#    url: jdbc:${spring.jpa.database}://localhost:5432/${db.instance.name}
    username: postgres
    password: password
spring.liquibase:
  parameters:
    db.instance.name: ${db.instance.name}
    db.type.name: ${spring.jpa.database}
  url: ${spring.datasource.url}
  user: ${spring.datasource.username}
  password: ${spring.datasource.password}
---
## >> just to show multiple instances for one environment can be declared
spring.profiles: db-persinfolocal
spring.profiles.include: dbtype-h2
#spring.profiles.include: dbtype-postgres
db.instance.name: persinfolocal
spring.datasource:
    url: jdbc:${spring.jpa.database}:file:/tmp/${db.instance.name}.db
#    url: jdbc:${spring.jpa.database}://localhost:5432/${db.instance.name}
    username: postgres
    password: password
spring.liquibase:
  parameters:
    db.instance.name: ${db.instance.name}
    db.type.name: ${spring.jpa.database}
  url: ${spring.datasource.url}
  user: ${spring.datasource.username}
  password: ${spring.datasource.password}
---
### EXAMPLE of a possible dev db instance
spring.profiles: db-corpdev
spring.profiles.include: dbtype-postgres
db.instance.name: corpdev
spring.datasource:
    url: jdbc:${spring.jpa.database}://postgres:5432/${db.instance.name}
    username: postgres
    password: password
spring.liquibase:
  parameters:
    db.instance.name: ${db.instance.name}
    db.type.name: ${spring.jpa.database}
  url: ${spring.datasource.url}
  user: ${spring.datasource.username}
  password: ${spring.datasource.password}
---
### EXAMPLE of a possible staging db instance
spring.profiles: db-corpstage
spring.profiles.include: dbtype-oracle
db.instance.name: corpstage
spring.datasource:
    url: jdbc:${spring.jpa.database}:thin:@oracle:1521/${db.instance.name}
    username: someuser
    password: somepassword
spring.liquibase:
  parameters:
    db.instance.name: ${db.instance.name}
    db.type.name: ${spring.jpa.database}
  url: ${spring.datasource.url}
  user: ${spring.datasource.username}
  password: ${spring.datasource.password}
---

#### >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
####    3. Apply database configs to execution environments
####       - Configure database instances for their execution
####         environment
####       - Set liquibase config and parameters for the environment
#### >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

spring.profiles: default
spring.profiles.include: db-docslocal, db-persinfolocal
### Default values for Liquibase
### https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/common-application-properties.html
spring.liquibase:
  enabled: true
  test-rollback-on-update: false
  # default liquibase operation at startup
  parameters.liquibase.operation: dbinit
---
### EXAMPLE of a possible local-int / ci db run
spring.profiles: local-int, ci
spring.profiles.include: db-docslocal, db-persinfolocal
### Local-Int & CI values for Liquibase
### https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/common-application-properties.html
spring.liquibase:
  enabled: true
  test-rollback-on-update: true
  # default liquibase operation at startup
  parameters.liquibase.operation: dbinit
---
### EXAMPLE of a possible dev db run
spring.profiles: dev
spring.profiles.include: db-corpdev
### Dev values for Liquibase
### https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/common-application-properties.html
spring.liquibase:
  enabled: false
  test-rollback-on-update: true
  # default liquibase operation at startup
  parameters.liquibase.operation: dbprep
---
### EXAMPLE of a possible staging db run
spring.profiles: stage
spring.profiles.include: db-corpstage
### Stage values for Liquibase
### https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/common-application-properties.html
spring.liquibase:
  enabled: false
  test-rollback-on-update: true
  # default liquibase operation at startup
  parameters.liquibase.operation: dbprep

```

</details>

Before you can use Liquibase, there are a couple more steps required. Read on ...


#### Using the Database Configurations

Use standard Spring JPA repos and entities. Examples are provided in [bip-reference-person](https://github.com/department-of-veterans-affairs/bip-reference-person/tree/master/bip-reference-person/src/main/java/gov/va/bip/reference/person/data).

* Ideally, references to repo and entities should be encapsulated in a helper class (for an example, see [PersonDataHelper](https://github.com/department-of-veterans-affairs/bip-reference-person/blob/master/bip-reference-person/src/main/java/gov/va/bip/reference/person/data/PersonDataHelper.java) in bip-reference-person).

* If multiple datasources are in use, it is recommended to package them under `**.data.[dbIdentifier].[repo|entities]`

# Schema and Data Management

Spring Boot has rudimentary database initialization capabilities. What spring does not provide out of the box is a means for managing schema and data changes over time. [Liquibase](http://www.liquibase.org/) is recommended for its database initialization, changeset and schema version management features.

The assumption is that the application is uses JPA in preference over raw JDBC. The spring boot dependency is `spring-boot-starter-data-jpa`.

### Database Management and Versioning With Liquibase

It is worth reviewing the [Liquibase documentation](https://www.liquibase.org/documentation/) to understand changesets, includes, preconditions, contexts and parameters.

Spring Boot will enable liquibase as the database manager if the spring initializer is disabled and it finds liquibase on the classpath. If you followed the configuration as described in the sections above, the reactor POM already adds liquibase. The configuration elements are:

<details><summary>Click here: Enable Liquibase as DB Initializer</summary>

	```yaml
	# turn off database initialization
	spring.jpa:
		hibernate:
			ddl-auto: none
	# turn on liquibase
	spring.liquibase:
		enabled: true
	```

</details>

By default, liquibase will search for a master changelog at `src/main/resources/db/changelog/db.changelog-master.yaml`. You must provide, at minimum, an "empty" changelog file at this location.

<details><summary>Click here: Empty db.changelog-master.yml File</summary>

```yaml
###############################################################################
###   MASTER CHANGELOG
###   This file controls which operations will be created for a given
###   Liquibase operation, on a database instance in a given environment.
###   - Only items common to ALL database instances, environments, operations
###     should appear in this root changelog file.
###   - Include other changelog files as needed.
###   - To use a variable it must be in the environment, or have been put in
###     the Liquibase Parameters list at the time of invocation, for example
###     in app yaml or commandline -D param: spring.liquibase.parameters.**
###############################################################################

databaseChangeLog:

```

</details>

If you look at the [db/changelog/db.changelog-master.yaml](https://github.com/department-of-veterans-affairs/bip-reference-person/blob/master/bip-reference-person/src/main/resources/db/changelog/db.changelog-master.yaml) file, you will see that it contains only an include statement. This statement uses the `db.instance.name` and `liquibase.operation` parameters to determine which changelog file it should read instructions from. These properties are configured in the application yaml, and are passed to Liquibase as parameters at runtime.

If you want your application to run Liquibase at startup, the master changelog must be in the resource classpath of the service that will be using it. The `db/changelog/**` directory cannot be moved to another module in the maven project.

### Using Liquibase as a Development Tool

Liquibase provides maven integration with the [liquibase-maven-plugin](https://www.liquibase.org/documentation/maven/index.html). The "change" operations and configuration options are a one-to-one match with the Liquibase [Changelogs and Commands](https://www.liquibase.org/documentation/index.html) available in the standalone product.

The `bip-reference-person` project has a [bip-reference-person-db](https://github.com/department-of-veterans-affairs/bip-reference-person/tree/master/bip-reference-person-db) module that provides guidance on how to set up a developer-only project. Operations in this project can be used for ongoing maintenance of local databases, for experimentation, and for one-shot operations.

* Property values to be passed into Liquibase _must_ be declared in the plugin's `<configuration>` section. Liquibase does not see properties in the maven config, spring context, or environment unless they have been explicitly passed as a parameter through the plugin configuration.

* The POM for this project should -in theory - only need to be modified to add new "change" operations. It configures Liquibase by:

 	* Using profiles to separate some different types of database operations, and to provide configurations that can be controlled via properties set in the POM or provided from the command line.

	* Altering the default changelog path to point to a specific changelog file that is related to a database-specific folder under `/db/changelogs/`

	* Configuring a properties file that can be used to override Liquibase default values. Note that the properties declared in these `*.properties` files can override any property that has been passed to Liquibase through the maven plugin's `<configuration>` or from the command line.

	* Sets some default behaviors for Liquibase.

### SQL Database Testing

Testing of data access should cover everything from the configuration of the access technologies is correct (drivers, pools, caches, etc), to ORM entities and operations.  Testing approaches differ depending on the purpose and environment.

#### Unit Tests

Use [Spring Boot Test](https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/boot-features-testing.html#boot-features-testing-spring-boot-applications-testing-autoconfigured-jpa-test). A good starting point for using these tools is in [this article](https://www.baeldung.com/spring-testing-separate-data-source).

* In-memory database should be used so that all ORM layers get coverage.

* When mocking is required, it should be done - generally speaking - as close to the connection pool (or JDBC driver) as possible.

#### Integration Tests

Integration tests are typically only executed as part of deployment into a server environment (e.g. dev).

* These tests should run against a live external database.

* If possible, use Liquibase to manage the state of data in the IT database.

#### Performance Tests

Performance tests should run in a dedicated Performance Test environment.

* These tests should run against a live external database.

* If possible, use Liquibase to manage the state of data in the perf database.
