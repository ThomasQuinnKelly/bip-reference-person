# Databases in BIP

Spring offers a variety of ways to configure and use databases. The [Spring Boot features for working with databases](https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/boot-features-sql.html)  and [data access how-to](https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/htmlsingle/#howto-data-access) are useful pages.

This page is focused on relational databases. BIP Framework, the Reference Person application, and the BIP Archetype currently do not include support for NoSQL / flat-file databases.

BIP Framework provides Maven Dependency Management for common relational databases and JDBC drivers that are known to be in use in BIP projects:
- H2 (embedded)
- Postgres
- Oracle

A couple important notes before you get started

* JPA data access is recommended because - among other things - it decouples you from database vendor implementations. Use the JPA ORM.

* If you adopt Liquibase, use the liquibase `change` features. Using liquibase changes decouples your changelogs from vendor implementations. **Note that** changes between versions of Liquibase, the Liquibase Maven plugin, and the Spring / Spring Boot integrations is very much a moving target. Beware that internet searches will return information that is not applicable to your versions and may mislead you.

The point here is to do your best to avoid writing vendor-specific SQL statements, whether in code, config, or changelogs.

### Recommended SQL Data Access Technologies

Recommended technology choices for relational database support:

* Use [Spring Boot 2.1.x support for SQL databases](https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/boot-features-sql.html).

* Connection Pool: datasource recommendation is to use [the default (and preferred) spring connection pool](https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/boot-features-sql.html#boot-features-connect-to-production-database), which is [HikariCP](https://github.com/brettwooldridge/HikariCP).

* Statement caching is the responsibility of the database (most JDBC drivers know how to access the db cache). Do not try to cache at the connection pool.

* For query logging, consider using the existing Spring/Hibernate logging mechanisms - they will automatically route through fluentd to kibana. If more sophisticated logging is required, tools such as [P6Spy](https://github.com/p6spy/p6spy) may be worth investigating, but should be verified to ensure they still route to fluentd.

	<details><summary>Click here: Spring/Hibernate logging</summary>

	```yaml
	# in app yaml - directly print to stdout (inefficient) ...
	spring.jpa:
		hibernate.show-sql: true
		properties.hibernate.format_sql: true
	# OR, in app yaml - use more efficient logger
	logging.level:
		org.hibernate.SQL: DEBUG
		org.hibernate.type.descriptor.sql.BasicBinder: TRACE
	```

	</details>

* Entity Manager / ORM: default implementation of [Spring JPA](https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/boot-features-sql.html#boot-features-jpa-and-spring-data) with [hibernate](https://hibernate.org/orm/documentation/).

* Transaction Manager:

	* Applications with a single datasource, or multiple-but-independent datasources, the default transaction manager that is preconfigured with [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/boot-features-sql.html#boot-features-jpa-and-spring-data) is sufficient.

	* Applications with multiple datasources that would benefit from XA transactions should use an embedded transaction manager (recommend [Atomikos](https://www.atomikos.com/) or [Bitronix](https://github.com/bitronix/btm)). For more information about Spring Boot JTA, see [Distributed Transactions with JTA](https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/boot-features-jta.html) and [Configuring Spring and JTA without full Java EE](https://spring.io/blog/2011/08/15/configuring-spring-and-jta-without-full-java-ee/).

		**XA NOTE:** If XA (distributed) transaction management is required, it will likely be necessary to add - at a minimum - a `JtaTransactionManager` bean to your application Configuration class.

* For database schema versioning and data management [Liquibase](http://www.liquibase.org/) is recommended, and is included in the Reference Person sample application.

* DB admin tools are plentiful, and usually quite interoperable. If you need (or just want) something that can handle multiple vendors, you might try [DBeaver](https://dbeaver.io/) (has an Eclipse plugin too), [OmniDB](https://omnidb.org/en/), or other similar tool.

### Supported Databases

Spring Boot supports a good variety of databases out of the box. Database availability will be different between the developer local environment, and servers.  

**Local Databases:**

* Recommendation for in-memory database is the spring default [H2 Database](https://www.h2database.com/html/main.html).

* If a local external database is required, teams can select which to use. Consider using a docker image of the desired local database (postgres, oracle, etc). Docker images for products can be found on the [Docker Hub - Images](https://hub.docker.com/search/?image_filter=official&type=image) page. The two bullets below show an example of acquiring and starting/stopping a postgres docker image:

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

Spring provides non-intrusive support through autoconfiguration for applications that only need a single datasource.  However, if your project requires multiple datasources there are some specific requirements because datatasource autoconfiguration will implicitly be disabled.

See "Single Datasource Projects" and "Multiple Datasource Projects" sections below.

Examples for datasource profiles and environment activations can be found in the [bip-reference-person](https://github.ec.va.gov/EPMO/bip-reference-person/) project.

**Framework Managed Dependencies**

BIP Framework provides managed dependencies and plugins for supported databases.

<details><summary>Click here: BIP Framework Managed Dependencies/Plugins</summary>

```xml
<properties>
	<!-- ... -->
	<!-- DATABASE related versions -->
	<h2.version>1.4.199</h2.version>
	<postgresql.version>42.2.6</postgresql.version>
	<ojdbc6.version>11.2.0.4</ojdbc6.version><!-- also 11.1.0.7 -->
	<ojdbc7.version>12.1.0.2</ojdbc7.version>
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
<!-- ... -->
<dependencyManagement>
	<!-- ... -->
	<dependencies>
		<!-- DATABASE related dependencies -->
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
<!-- ... -->
<build>
	<!-- ... -->
	<pluginManagement>
		<plugins>
			<!-- DATABASE related plugins -->
			<plugin>
				<groupId>org.liquibase</groupId>
				<artifactId>liquibase-maven-plugin</artifactId>
				<version>${liquibase-core.version}</version>
				<!-- Required dependencies for various Change operations -->
				<dependencies>
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
					<dependency>
						<groupId>javax.validation</groupId>
						<artifactId>validation-api</artifactId>
						<version>${validation-api.version}</version>
					</dependency>
					<dependency>
						<groupId>org.springframework</groupId>
						<artifactId>spring-core</artifactId>
						<version>${spring-core.version}</version>
					</dependency>
					<dependency>
						<groupId>org.springframework</groupId>
						<artifactId>spring-context</artifactId>
						<version>${spring-core.version}</version>
					</dependency>
					<dependency>
						<groupId>org.springframework</groupId>
						<artifactId>spring-beans</artifactId>
						<version>${spring-core.version}</version>
					</dependency>
					<dependency>
						<groupId>org.springframework.data</groupId>
						<artifactId>spring-data-jpa</artifactId>
						<version>${spring-data-jpa.version}</version>
					</dependency>
					<dependency>
						<groupId>org.hibernate</groupId>
						<artifactId>hibernate-core</artifactId>
						<version>${hibernate.version}</version>
					</dependency>
				</dependencies>
			</plugin>
		</plugins>
	</pluginManagement>
</build>
<!-- ... -->
```

</details>

**Common Configuration**

It is recommended to provide dependencies in the projects root reactor POM to provide them transparently to the modules that need them (the service, integration testing, performance testing, local liquibase db changelogs).

<details><summary>Click here: Reactor POM Configuration</summary>

```xml
<!-- ASSUMES THAT THE PROPERTIES AND MANAGED DEPENDENCIES ARE AVAILABLE FROM bip-framework-parentpom -->
<dependencies>
	<!--
		DATABASE related dependencies, configured in app yaml.
		Available managed dependencies from bip-framework-parentpom/pom.xml:
		com.h2database:h2:${h2.version}
		org.postgresql:postgresql:${postgresql.version}
		com.oracle:ojdbc6:${ojdbc6.version}
		com.oracle:ojdbc7:${ojdbc7.version}
		com.oracle:ojdbc8:${ojdbc8.version}
		com.oracle:ojdbc10:${ojdbc8.version}
		org.liquibase:liquibase-core:${liquibase-core.version}
		org.liquibase.ext:liquibase-hibernate5:${liquibase-hibernate5.version}
	-->
	<dependency>
		<groupId>org.liquibase</groupId>
		<artifactId>liquibase-core</artifactId>
	</dependency>
	<dependency>
		<groupId>org.liquibase.ext</groupId>
		<artifactId>liquibase-hibernate5</artifactId>
	</dependency>
	<!-- postgresql: currently backward compatible to PostgreSQL 8.2 -->
	<dependency>
		<groupId>org.postgresql</groupId>
		<artifactId>postgresql</artifactId>
	</dependency>
	<dependency>
		<groupId>com.h2database</groupId>
		<artifactId>h2</artifactId>
	</dependency>
	<!-- TODO Oracle 11.1.x is on TRM Unapproved list -->
	<!-- TODO Oracle 11.2.x and 12.x.x are on TRM Divest schedule -->
	<!--
	<dependency>
		<groupId>com.oracle</groupId>
		<artifactId>ojdbc6</artifactId>
	</dependency>
	-->
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
</dependencies>
```

</details>

To the extent possible, keep common schema and data setups in your liquibase `db/changelog/**` for the service and its unit tests, integration tests, and performance tests.

BIP Framework provides managed dependencies for H2, Postgres, and Oracle.

Note that for non-embedded database types (e.g. postgres, oracle), the database schema must already exist. Preparation can be very simple, for example:

* In pgadmin right-click on _Databases > Create > Database..._ and in the dialog specify the database name and click _Save_. This creates the schema.

* You can manually add tables, or you can make table creation a changeset in your master changelog. An example of this can be found in the bip-reference-person [db.changelog-master.yaml](https://github.ec.va.gov/EPMO/bip-reference-person/blob/master/bip-reference-person/src/main/resources/db/changelog/db.changelog-master.yaml) file.

**Single Datasource Projects**

Projects using only one datasource can leverage Spring Boot's autoconfiguration. No java code is required.

* In the application YAML

	* Use the standard datasource property path as described in the Spring documentation, e.g. `spring.jpa.**`, `spring.datasource.**`, etc.

	* Create the database configuration. It is recommended to use profiles to separate the configurations for the Datasources and the Environments. Working examples for this profile separation are provided with documentation in [bip-reference-person.yml](https://github.ec.va.gov/EPMO/bip-reference-person/blob/master/bip-reference-person/src/main/resources/bip-reference-person.yml) (search for "DATABASE RELATED CONFIGURATION").

	<details><summary>Click here: Example Single Datasource Configuration</summary>

	```yaml
	---
	### EXAMPLE of a possible single-datasource dev db instance
	spring.profiles: db-single-ds-mydb
	spring.h2.console.enabled: true
	spring.jpa:
	    database: h2
	    generate-ddl: false
	    properties:
	      hibernate:
	        dialect: org.hibernate.dialect.H2Dialect
	        hibernate.format_sql: true
	    hibernate:
	      show-sql: true
	      naming.physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
	      ddl-auto: none
	      connection:
	        provider_class: org.hibernate.hikaricp.internal.HikariCPConnectionProvider
	spring.datasource:
	    instance.name: mydb
	    type: com.zaxxer.hikari.HikariDataSource
	    ### skip spring db init - using liquibase
	    initialization-mode: never
	    driver-class-name: org.h2.Driver
	    url: jdbc:${spring.jpa.database}:mem:${spring.datasource.instance.name}
	    username:
	    password:
	    hikari:
	      connection-timeout: 6000
	      maxLifetime: 6000
	      minimum-idle: 6
	      maximum-pool-size: 16
	      idle-timeout: 60000
	### Liquibase values. https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/common-application-properties.html
	### The spring DatasourceConfig automatically passes the datasource, so no need to specify url, user, or pass
	spring.liquibase:
	  parameters:
	    db.instance.name: ${spring.datasource.instance.name}
	    db.type.name: ${spring.jpa.database}
	    # default liquibase changelog operation at startup
	    liquibase.operation: dbinit
	---
	# Add the "db-single-ds-mydb" datasource profile to the desired environments
	spring.profiles: default,local-int,ci,dev
	spring.profiles.include: db-single-ds-mydb
	spring.liquibase:
	  enabled: false
	  test-rollback-on-update: false
	---
	```

	</details>

	* If you intend to use liquibase to run changelogs on server startup, make sure you turn off the spring/hibernate equivalent. The specific elements from the above example are ...

	<details><summary>Click here: Turning Off Spring/Hibernate Startup Scripting</summary>

	```yaml
	spring.profiles: some-db-profile-identifier
	spring.jpa:
		generate-ddl: false
		hibernate:
			ddl-auto: none
	# ... etc ...
	```

	</details>

* Test and adjust as necessary

**Multiple Datasource Projects**

Projects that use multiple datasources will automatically cause Spring Boot's autoconfiguration to be disabled. The application must provide unique property paths for each datasource, and must provide beans for the datasources, the transaction managers, the entity managers, hikari connection pools, and liquibase instances.

* In the application YAML

	* Begin by deciding which property paths will be used, for example `db.datasource.one` and `db.datasource.two`.

	* Create the initial database configuration. It is recommended to use profiles to separate the configurations for JPA database types, the Datasources, and the Environments. Working examples for this profile separation are provided with documentation in [bip-reference-person.yml](https://github.ec.va.gov/EPMO/bip-reference-person/blob/master/bip-reference-person/src/main/resources/bip-reference-person.yml) (search for "DATABASE RELATED CONFIGURATION").

	<details><summary>Click here: Mutli-Datasource YAML Configuration</summary>

	```yaml
	---
	### Datasource for database named "one"
	spring.profiles: db-ds-one
	spring.h2.console.enabled: true
	db.jpa.one:
	    database: h2
	    generate-ddl: false
	    properties:
	      hibernate:
	        dialect: org.hibernate.dialect.H2Dialect
	        hibernate.format_sql: true
	    hibernate:
	      show-sql: true
	      naming.physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
	      ddl-auto: none
	      connection:
	        provider_class: org.hibernate.hikaricp.internal.HikariCPConnectionProvider
	db.datasource.one:
	    instance.name: one
	    type: com.zaxxer.hikari.HikariDataSource
	    ### skip spring db init - using liquibase
	    initialization-mode: never
	    driver-class-name: org.h2.Driver
	    url: jdbc:${db.jpa.one.database}:mem:${db.datasource.one.instance.name}
	    ### user/pass must be set up in Vault, e.g. ${bip.database.[local|ci|dev|stage|prod].one.[username|password]}
	    username:
	    password:
	    hikari:
	      connection-timeout: 4000
	      maxLifetime: 4000
	      minimum-idle: 4
	      maximum-pool-size: 14
	      idle-timeout: 40000
	### Liquibase values. https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/common-application-properties.html
	### The OneDatasourceConfig class automatically passes the datasource, so no need to specify url, user, or pass
	db.liquibase.one:
	  parameters:
	    db.instance.name: ${db.datasource.one.instance.name}
	    db.type.name: ${db.jpa.one.database}
	    # default liquibase changelog operation at startup
	    liquibase.operation: dbinit
	---
	### Datasource for database named "two"
	spring.profiles: db-ds-two
	db.jpa.two:
	    database: h2
	    generate-ddl: false
	    properties:
	      hibernate:
	        dialect: org.hibernate.dialect.H2Dialect
	        hibernate.format_sql: true
	    hibernate:
	      show-sql: true
	      naming.physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
	      ddl-auto: none
	      connection:
	        provider_class: org.hibernate.hikaricp.internal.HikariCPConnectionProvider
	db.datasource.two:
	    instance.name: two
	    type: com.zaxxer.hikari.HikariDataSource
	    ### skip spring db init - using liquibase
	    initialization-mode: never
	    driver-class-name: org.h2.Driver
	    url: jdbc:${db.jpa.two.database}:mem:${db.datasource.two.instance.name}
	    ### user/pass must be set up in Vault, e.g. ${bip.database.[local|ci|dev|stage|prod].persondocs.[username|password]}
	    username:
	    password:
	    hibernate:
	      naming.physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
	    hikari:
	      connection-timeout: 5000
	      maxLifetime: 50000
	      minimum-idle: 5
	      maximum-pool-size: 15
	      idle-timeout: 50000
	### Liquibase values. https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/common-application-properties.html
	### The TwoDatasourceConfig class automatically passes the datasource, so no need to specify url, user, or pass
	db.liquibase.two:
	  parameters:
	    db.instance.name: ${db.datasource.two.instance.name}
	    db.type.name: ${db.jpa.two.database}
	    # default liquibase changelog operation at startup
	    liquibase.operation: dbinit
	---
	# Add the "db-ds-one" and "db-ds-two" datasource profiles to the desired environments
	spring.profiles: default,local-int,ci,dev
	spring.profiles.include: db-ds-one, db-ds-two
	spring.liquibase:
	  enabled: false
	  test-rollback-on-update: false
	---
	```

	</details>

	* If you intend to use liquibase to run changelogs on server startup, make sure you turn off the spring/hibernate equivalent. The specific elements from the above example are ...

	<details><summary>Click here: Turning Off Sring/Hibernate Startup Scripting</summary>

	```yaml
	---
	spring.profiles: db.datasource.one
	spring.jpa:
		generate-ddl: false
		hibernate:
			ddl-auto: none
	# ... etc ...
	---
	spring.profiles: db.datasource.two
	spring.jpa:
		generate-ddl: false
		hibernate:
			ddl-auto: none
	# ... etc ...
	---
	```

	</details>

* Make sure that the java packages related to each datasource are separated from each other, for example `gov.va.bip.bip-myproject.data.db1` and `gov.va.bip.bip-myproject.data.db2`.

* For each datasource, add a new `@Configuration` class in your `gov.va.bip.bip-myproject.config` package (this package should already being scanned by Spring). Each of these classes must provide beans that read from the related property path you decided in the first step. **Note that** the beans in one datasource _must_ be assigned `@Primary`, and any other datasources _not_ assigned as primary. An example of a primary datasource class is in the collapsible section ...

<details><summary>Click here: PRIMARY Configuration Class for a DataSource "one"</summary>

```java
package gov.va.bip.reference.person.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

import liquibase.integration.spring.SpringLiquibase;

/**
 * Configuration for the "One" datasource, entity manager factory, transaction manager, and liquibase beans.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "oneEntityManagerFactory",
		transactionManagerRef = "oneTransactionManager",
		// jpa repo base package
		basePackages = "gov.va.bip.reference.person.data.one")
public class PersonOneDatasourceConfig extends PersonDatasourceBase {

	private static final String ONE_JPA_PREFIX = "db.jpa.one";
	private static final String ONE_DATASOURCE_PREFIX = "db.datasource.one";
	private static final String ONE_HIKARI_DATASOURCE_PREFIX = "db.datasource.one.hikari";
	private static final String ONE_LIQUIBASE_PROPERTY_PREFIX = "db.liquibase.one";
	private static final String ONE_PERSISTENCE_UNIT = "one";

	private static final String[] ONE_ENTITIES_PACKAGES = { "gov.va.bip.reference.person.data.one.entities" };

	/**
	 * Properties for the datasource and to populate liquibase config.
	 *
	 * @return DataSourceProperties
	 */
	@Bean
	@Primary
	@ConfigurationProperties(ONE_DATASOURCE_PREFIX)
	public DataSourceProperties oneDataSourceProperties() {
		return new DataSourceProperties();
	}

	/**
	 * Datasource for person "one" database, via hikari datasource pool.
	 * <p>
	 * Application yaml configures this datasource with db.datasource.one.** properties.
	 * <p>
	 * This datasource puts all values into the hikari config, but does not populate the
	 * "normal" datasource properties (url, user, pass). These values are manually added
	 * back so that liquibase can be initiated correctly.
	 *
	 * @return DataSource - the one datasource
	 */
	@Primary
	@Bean
	@ConfigurationProperties(prefix = ONE_HIKARI_DATASOURCE_PREFIX)
	public HikariDataSource oneDataSource() {
		return oneDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
	}

	/**
	 * Entity Manager for person "one" entities.
	 *
	 * @param builder a builder for entity manager factory
	 * @param dataSource must be the "oneDataSource" bean
	 * @return LocalContainerEntityManagerFactoryBean entity manager for oneDataSource
	 */
	@Primary
	@Bean
	@ConfigurationProperties(prefix = ONE_JPA_PREFIX)
	public LocalContainerEntityManagerFactoryBean oneEntityManagerFactory(
			EntityManagerFactoryBuilder builder,
			@Qualifier("oneDataSource") DataSource dataSource) {
		return builder
				.dataSource(dataSource)
				.packages(ONE_ENTITIES_PACKAGES)
				.persistenceUnit(ONE_PERSISTENCE_UNIT)
				.build();
	}

	/**
	 * Transaction Manager for person "one" entities.
	 *
	 * @param entityManagerFactory must be the "oneEntityManagerFactory" bean
	 * @return PlatformTransactionManager transaction manager for oneEntityManagerFactory
	 */
	@Primary
	@Bean
	public PlatformTransactionManager oneTransactionManager(
			@Qualifier("oneEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}

	/**
	 * Bean for liquibase properties.
	 *
	 * @return LiquibaseProperties properties for liquibase
	 */
	@Bean
	@ConfigurationProperties(prefix = ONE_LIQUIBASE_PROPERTY_PREFIX)
	public LiquibaseProperties oneLiquibaseProperties() {
		return new LiquibaseProperties();
	}

	/**
	 * The liquibase object configured for the datasource with the liquibase properties.
	 *
	 * @return SpringLiquibase
	 */
	@Bean
	public SpringLiquibase oneLiquibase() {
		return springLiquibase(oneDataSource(), oneLiquibaseProperties());
	}
}
```

</details>

# Using Liquibase

Spring Boot has rudimentary database initialization capabilities. What spring does not provide out of the box is a means for managing schema and data changes over time. [Liquibase](http://www.liquibase.org/) is recommended for its database initialization, changeset and schema version management features.

Version changes in Liquibase, the Liquibase Maven plugin, and the Spring integrations can be significant and subtle - so beware of the information you find in any internet searches.

Version-specific documentation links:

* [Spring Boot 2.1.6 - executing liquibase at startup](https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/htmlsingle/#howto-execute-liquibase-database-migrations-on-startup)

* [Spring Boot 2.1.6 - common application properties - search for "liquibase"](https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/htmlsingle/#common-application-properties)

Unfortunately, Liquibase does not maintain version-specific documentation. You will need to discover any changes since the 3.6.x releases and subsequent releases to understand what is not available in the release used by Spring Boot.

* [Liquibase 3.6.3 Release Notes](https://liquibase.jira.com/secure/ReleaseNote.jspa?projectId=10020&version=13504)

* [Liquibase 3.7 Release Notes](http://www.liquibase.org/2019/07/liquibase-3-7-0-released.html)

* [Liquibase 3.8 Release Notes](https://www.liquibase.org/2019/08/liquibase-3-8-0-released.html)

* [Liquibase Documentation](https://www.liquibase.org/documentation/index.html)

* [Liquibase Maven plugin](https://www.liquibase.org/documentation/maven/index.html)

### Liquibase At Server Startup

Spring Boot will enable liquibase as the database manager if the spring/hibernate initializer is disabled, AND if it finds liquibase on the classpath. If you followed the configuration as described in the sections above, the reactor POM already adds liquibase.

By default, liquibase will search for a master changelog in your _service_ project at `src/main/resources/db/changelog/db.changelog-master.yaml`. You must provide, at minimum, an "empty" changelog file at this location.

<details><summary>Click here: Empty db.changelog-master.yaml File</summary>

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
# empty line below

```

</details>

If you look at the [db/changelog/db.changelog-master.yaml](https://github.ec.va.gov/EPMO/bip-reference-person/blob/master/bip-reference-person/src/main/resources/db/changelog/db.changelog-master.yaml) file, you will see that it contains only an include statement. This statement uses the `db.instance.name` and `liquibase.operation` parameters to determine which changelog file it should read instructions from. These properties are configured in the application yaml, and are passed to Liquibase as parameters at runtime.

### The `${project.artifactId}-db` Liquibase Project

The `${project.artifactId}-db` project provides a place where developers can create, test, and prepare database changes that are (or may eventually be) promoted to higher environments.

The [liquibase-maven-plugin](https://www.liquibase.org/documentation/maven/index.html). The "change" operations and configuration options are a one-to-one match with the Liquibase [Changelogs and Commands](https://www.liquibase.org/documentation/index.html) available in the standalone product. With this plugin, you can configure maven profiles to perform specific operations. And by using liquibase Parameters, you can make add flexibility as to exactly how and with what changelogs or resources the operation is performed. Examples of this are provided in the project POM already, or can be viewed in [bip-reference-person-db POM](https://github.ec.va.gov/EPMO/bip-reference-person/blob/master/bip-reference-person-db/pom.xml).

### Using Liquibase as a Development Tool

The `bip-reference-person` project has a [bip-reference-person-db](https://github.ec.va.gov/EPMO/bip-reference-person/tree/master/bip-reference-person-db) module that provides guidance on how to configure and use this developer-only project. Operations in this project can be used for ongoing maintenance of local databases, for experimentation, and for one-shot operations.

* Property values to be passed into Liquibase _must_ be declared in the plugin's `<configuration>` section. Liquibase does not see properties in the maven config, spring context, or environment unless they have been explicitly passed as a parameter through the plugin configuration.

* The POM for this project should - in theory - only need to be modified to add new "change" operations. It configures Liquibase by:

 	* Using profiles to separate some different types of database operations, and to provide configurations that can be controlled via properties set in the POM or provided from the command line.

	* Altering the default changelog path to point to a specific changelog file that is related to a database-specific folder under `/db/changelogs/`

	* Configuring a properties file that can be used to override Liquibase default values. Note that the properties declared in these `*.properties` files can override any property that has been passed to Liquibase through the maven plugin's `<configuration>` or from the command line.

	* Sets some default behaviors for Liquibase.

# SQL Database Testing

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
