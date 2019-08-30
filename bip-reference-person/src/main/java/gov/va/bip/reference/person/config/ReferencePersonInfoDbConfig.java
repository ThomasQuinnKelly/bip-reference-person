package gov.va.bip.reference.person.config;

//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories(
//		entityManagerFactoryRef = "personinfoEntityManagerFactory",
//		basePackages = { "gov.va.bip.reference.person.data.info.repo" })
///* datasource properties selected by profile, not by property prefix */
//@Profile("db-persinfolocal")
public class ReferencePersonInfoDbConfig {

//	@Bean(name = "personinfoDataSource")
//	public DataSource dataSource() {
//		return DataSourceBuilder.create().build();
//	}
//
//	@Bean(name = "personinfoEntityManagerFactory")
//	public LocalContainerEntityManagerFactoryBean entityManagerFactory(
//			EntityManagerFactoryBuilder builder,
//			@Qualifier("dataSource") DataSource dataSource) {
//		return builder
//				.dataSource(dataSource)
//				.packages("gov.va.bip.reference.person.data.info.entities")
//				.persistenceUnit("personinfo")
//				.build();
//	}
}
