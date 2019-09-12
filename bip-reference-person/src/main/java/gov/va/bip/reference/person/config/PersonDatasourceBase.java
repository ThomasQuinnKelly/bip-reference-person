package gov.va.bip.reference.person.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;

import liquibase.integration.spring.SpringLiquibase;

/**
 * Base class for PersonDatasource* classes
 *
 * @author aburkholder
 */
public abstract class PersonDatasourceBase {

	/**
	 * Set Liquibase object properties using the provided datasource and properties.
	 * <p>
	 * Does <b>NOT</b> use or set the following properties:
	 * <ul>
	 * <li> CheckChangeLogLocation
	 * <li> IgnoreClasspathPrefix
	 * <li> Tag
	 * </ul>
	 *
	 * @param dataSource the datasource to use
	 * @param properties the properties to use
	 * @return SpringLiquibase the liquibase object
	 */
	static SpringLiquibase springLiquibase(DataSource dataSource, LiquibaseProperties properties) {
		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setDataSource(dataSource);
		liquibase.setChangeLog(properties.getChangeLog());
		liquibase.setContexts(properties.getContexts());
		liquibase.setDefaultSchema(properties.getDefaultSchema());
		liquibase.setDropFirst(properties.isDropFirst());
		liquibase.setShouldRun(properties.isEnabled());
		liquibase.setLabels(properties.getLabels());
		liquibase.setChangeLogParameters(properties.getParameters());
		liquibase.setRollbackFile(properties.getRollbackFile());
		liquibase.setDatabaseChangeLogLockTable(properties.getDatabaseChangeLogLockTable());
		liquibase.setDatabaseChangeLogTable(properties.getDatabaseChangeLogTable());
		liquibase.setLiquibaseSchema(properties.getLiquibaseSchema());
		liquibase.setLiquibaseTablespace(properties.getLiquibaseTablespace());
		liquibase.setTestRollbackOnUpdate(properties.isTestRollbackOnUpdate());
		return liquibase;
	}
}
