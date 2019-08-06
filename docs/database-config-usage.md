# Databases in BIP

Spring offers a variety of ways to configure and use databases. The [Spring Boot features for working with databases](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-sql.html)  ... https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-data-access

Spring boot uses the excellent database support from the Spring Data module. Because BIP application datasource needs can vary widely, the spring's database offering is about as granular as is possible. As a result, there is no real value-add that BIP Framework can currently offer for SQL database access.

However to encourage some level of consistency between applications, there is a need for general recommendations. This document attempts to provide recommendations for common data access scenarios, and suggestions for potential outliers.

Working examples of the suggestions found on this page can be found as part of the bip-reference-person [????](????) endpoint.

# SQL/Relational Databases

### Recommended SQL Data Access Technologies

Recommended technology choices for relational database support:

* Use [spring boot support for SQL databases](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-sql.html).

* Connection Pool: datasource recommendation is to use [the default (and preferred) spring connection pool](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-sql.html#boot-features-connect-to-production-database), which is [HikariCP](https://github.com/brettwooldridge/HikariCP).

* Entity Manager / ORM: default implementation of [Spring JPA](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-sql.html#boot-features-jpa-and-spring-data) with [hibernate](https://hibernate.org/orm/documentation/).

* Transaction Manager: type of manager depends on needs

	* Applications with a single datasource, or multiple-but-independent datasources, the default transaction manager that is preconfigured with [Spring Data JPA](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-sql.html#boot-features-jpa-and-spring-data) is sufficient.

	* Applications with multiple datasources that require XA transactions should use the spring [Platform Transaction Manager](https://spring.io/blog/2011/08/15/configuring-spring-and-jta-without-full-java-ee/) abstraction of JTA, using an embedded transaction manager (e.g. [Atomikos](https://www.atomikos.com/) or [Bitronix](https://github.com/bitronix/btm)). For more information about JTA in spring boot, see [Distributed Transactions with JTA](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-jta.html) and [Configuring Spring and JTA without full Java EE](https://spring.io/blog/2011/08/15/configuring-spring-and-jta-without-full-java-ee/). JTA is no longer as scary as it once was.

### Supported Databases

Spring Boot supports a good variety of databases out of the box. Database availability will be different between the developer local environment, and servers.  

Dependencies should be declared in the service `pom.xml`. The URL and other configuration properties that are configured for a given environment should be declared in the `[application-name].yml` file for the specific profile (the default profile, local-int, ci, dev, stage, prod).

Local Databases:

* Recommendation for in-memory database is the spring default of h2.
* If a local independent database is required, teams can select which to use. Consider using a docker image of the desired local database (postgres, oracle, etc). Examples of acquiring and starting/stopping a postgres docker image:
	* acquire and start postgres docker instance: `docker run --rm --name bip-postgres -p 127.0.0.1:5432:5432/tcp -e POSTGRES_PASSWORD=password -d postgres`
	* stop postgres docker intance: `docker stop bip-postgres`
* Managing local database schema and data can be challenging. Consider using [LiquiBase](http://www.liquibase.org/) or [FlyAway](https://flywaydb.org/).
* Mocking (for unit tests) should ideally be done as close to the database driver as possible. [Spring Boot Test](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html#boot-features-testing-spring-boot-applications-testing-autoconfigured-jpa-test) is a great place to [start(https://www.baeldung.com/spring-testing-separate-data-source)].

### SQL Database Configuration

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

If you are using the default connection pool (Hikari), do not use the standard `url` property. [HikariCP uses the `jdbc-url`](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-configure-a-datasource) as shown in the example below.

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
		properties:
			hibernate:
				dialect: org.hibernate.dialect.PostgreSQLDialect
		hibernate:
			ddl-auto: create
## In the logging section of the YAML file, you could add things like...
#logging.level.org.hibernate.SQL=debug
---
```

</details>

If your project requires multiple datasources to participate in XA transactions, there is a little more work involved. Remember that configurations will need to be set up in profiles so they are activated in the appropriate environment.

The short version of what to do:

* Create configuration classes for each database to declare beans for the datasource, entity manager, and transaction manager
* Create an autoconfiguration class for each datasource that will participate in the XA transactions, and define which property path applies to each datasource.
* Add configuration properties for each datasource "property path" in the application YAML file.

For reference, check out these links...

* Spring docs has a short page on [Distributed Transactions with JTA](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-jta.html).
* Spring Blog has a detailed article on [Configuring Spring and JTA without full Java EE](https://spring.io/blog/2011/08/15/configuring-spring-and-jta-without-full-java-ee/).
* Baeldung provides a must-read example of how to set up [Spring JPA – Multiple Databases](https://www.baeldung.com/spring-data-jpa-multiple-databases). Classes for configuration are provided, and the boot autoconfiguration is shown in part "6. Multiple Databases in Spring Boot".
