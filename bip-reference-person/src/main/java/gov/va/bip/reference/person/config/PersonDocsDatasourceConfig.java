package gov.va.bip.reference.person.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
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
 * Configuration for the Person docs datasource, entity manager factory, transaction manager, and liquibase beans.
 *
 * @author aburkholder
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "docsEntityManagerFactory",
		transactionManagerRef = "docsTransactionManager",
		// jpa repo base package
		basePackages = "gov.va.bip.reference.person.data.docs")
public class PersonDocsDatasourceConfig extends PersonDatasourceBase {

	private static final String DOCS_JPA_PREFIX = "db.jpa.docs";
	private static final String DOCS_DATASOURCE_PREFIX = "db.datasource.docs";
	private static final String DOCS_HIKARI_DATASOURCE_PREFIX = "db.datasource.docs.hikari";
	private static final String DOCS_LIQUIBASE_PROPERTY_PREFIX = "db.liquibase.docs";
	private static final String DOCS_PERSISTENCE_UNIT = "docs";

	private static final String[] DOCS_ENTITIES_PACKAGES = { "gov.va.bip.reference.person.data.docs.entities" };
	

	/**
	 * Properties for the datasource and to populate liquibase config.
	 *
	 * @return DataSourceProperties
	 */
	@Bean
	@Primary
	@ConfigurationProperties(DOCS_DATASOURCE_PREFIX)
	public DataSourceProperties docsDataSourceProperties() {
		return new DataSourceProperties();
	}

	/**
	 * Datasource for person "docs" database, via hikari datasource pool.
	 * <p>
	 * Application yaml configures this datasource with db.datasource.docs.** properties.
	 * <p>
	 * This datasource puts all values into the hikari config, but does not populate the
	 * "normal" datasource properties (url, user, pass). These values are manually added
	 * back so that liquibase can be initiated correctly.
	 *
	 * @return DataSource - the docs datasource
	 */
	@Primary
	@Bean
	@ConfigurationProperties(prefix = DOCS_HIKARI_DATASOURCE_PREFIX)
	public DataSource docsDataSource() {
		return docsDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
	}
	
	/**
	 * JpaProperties for person "docs" entities.
	 *
	 * @return the JPA properties
	 */
	@Primary
	@Bean
	@ConfigurationProperties(prefix = DOCS_JPA_PREFIX)
	public JpaProperties docsJpaProperties() {
	    return new JpaProperties();
	}

	/**
	 * Entity Manager for person "docs" entities.
	 *
	 * @param builder a builder for entity manager factory
	 * @param dataSource must be the "docsDataSource" bean
	 * @return LocalContainerEntityManagerFactoryBean entity manager for docsDataSource
	 */
	@Primary
	@Bean
	public LocalContainerEntityManagerFactoryBean docsEntityManagerFactory(
			EntityManagerFactoryBuilder builder,
			@Qualifier("docsDataSource") DataSource dataSource) {
		return builder
				.dataSource(dataSource)
				.packages(DOCS_ENTITIES_PACKAGES)
				.persistenceUnit(DOCS_PERSISTENCE_UNIT)
				.properties(docsJpaProperties().getProperties())
				.build();
	}

	/**
	 * Transaction Manager for person "docs" entities.
	 *
	 * @param entityManagerFactory must be the "docsEntityManagerFactory" bean
	 * @return PlatformTransactionManager transaction manager for docsEntityManagerFactory
	 */
	@Primary
	@Bean
	public PlatformTransactionManager docsTransactionManager(
			@Qualifier("docsEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}

	/**
	 * Bean for liquibase properties.
	 *
	 * @return LiquibaseProperties properties for liquibase
	 */
	@Bean
	@ConfigurationProperties(prefix = DOCS_LIQUIBASE_PROPERTY_PREFIX)
	public LiquibaseProperties docsLiquibaseProperties() {
		return new LiquibaseProperties();
	}

	/**
	 * The liquibase object configured for the datasource with the liquibase properties.
	 *
	 * @return SpringLiquibase
	 */
	@Bean
	public SpringLiquibase docsLiquibase() {
		return springLiquibase(docsDataSource(), docsLiquibaseProperties());
	}
}
