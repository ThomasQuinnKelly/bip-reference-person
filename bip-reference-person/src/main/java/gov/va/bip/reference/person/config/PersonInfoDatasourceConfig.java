package gov.va.bip.reference.person.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

import liquibase.integration.spring.SpringLiquibase;

/**
 * Configuration for the Person Info datasource, entity manager factory, transaction manager, and liquibase beans.
 *
 * @author aburkholder
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "infoEntityManagerFactory",
		transactionManagerRef = "infoTransactionManager",
		// jpa repo base package
		basePackages = "gov.va.bip.reference.person.data.info")
public class PersonInfoDatasourceConfig extends PersonDatasourceBase {

	private static final String INFO_JPA_PREFIX = "db.jpa.info";
	private static final String INFO_DATASOURCE_PREFIX = "db.datasource.info";
	private static final String INFO_HIKARI_DATASOURCE_PREFIX = "db.datasource.info.hikari";
	private static final String INFO_LIQUIBASE_PROPERTY_PREFIX = "db.liquibase.info";
	private static final String INFO_PERSISTENCE_UNIT = "info";

	private static final String[] INFO_ENTITIES_PACKAGES = { "gov.va.bip.reference.person.data.info.entities" };

	/**
	 * Properties for the datasource and to populate liquibase config.
	 *
	 * @return DataSourceProperties
	 */
	@Bean
	@ConfigurationProperties(INFO_DATASOURCE_PREFIX)
	public DataSourceProperties infoDataSourceProperties() {
		return new DataSourceProperties();
	}

	/**
	 * Datasource for person "info" database, via hikari datasource pool.
	 * <p>
	 * Application yaml configures this datasource with db.datasource.info.** properties.
	 * <p>
	 * This datasource puts all values into the hikari config, but does not populate the
	 * "normal" datasource properties (url, user, pass). These values are manually added
	 * back so that liquibase can be initiated correctly.
	 *
	 * @return DataSource - the info datasource
	 */
	@Bean
	@ConfigurationProperties(prefix = INFO_HIKARI_DATASOURCE_PREFIX, ignoreInvalidFields = true)
	public HikariDataSource infoDataSource() {
		return infoDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
	}

	/**
	 * Entity Manager for person "info" entities.
	 *
	 * @param builder a builder for entity manager factory
	 * @param dataSource must be the "infoDataSource" bean
	 * @return LocalContainerEntityManagerFactoryBean entity manager for infoDataSource
	 */
	@Bean
	@ConfigurationProperties(prefix = INFO_JPA_PREFIX)
	@RefreshScope
	public LocalContainerEntityManagerFactoryBean infoEntityManagerFactory(
			EntityManagerFactoryBuilder builder,
			@Qualifier("infoDataSource") DataSource dataSource) {
		return builder
				.dataSource(dataSource)
				.packages(INFO_ENTITIES_PACKAGES)
				.persistenceUnit(INFO_PERSISTENCE_UNIT)
				.build();
	}

	/**
	 * Transaction Manager for person "info" entities.
	 *
	 * @param entityManagerFactory must be the "infoEntityManagerFactory" bean
	 * @return PlatformTransactionManager transaction manager for infoEntityManagerFactory
	 */
	@Bean
	public PlatformTransactionManager infoTransactionManager(
			@Qualifier("infoEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}

	/**
	 * Bean for liquibase properties.
	 *
	 * @return LiquibaseProperties properties for liquibase
	 */
	@Bean
	@ConfigurationProperties(prefix = INFO_LIQUIBASE_PROPERTY_PREFIX)
	public LiquibaseProperties infoLiquibaseProperties() {
		return new LiquibaseProperties();
	}

	/**
	 * The liquibase object configured for the datasource with the liquibase properties.
	 *
	 * @return SpringLiquibase
	 */
	@Bean
	public SpringLiquibase infoLiquibase() {
		return springLiquibase(infoDataSource(), infoLiquibaseProperties());
	}
}
