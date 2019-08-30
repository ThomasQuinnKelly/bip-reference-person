package gov.va.bip.reference.person.config;

//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories(
//		entityManagerFactoryRef = "persondocsEntityManagerFactory",
//		transactionManagerRef = "persondocsTransactionManager",
//		basePackages = { "gov.va.bip.reference.person.data.docs.repo" })
///* datasource properties selected by profile, not by property prefix */
//@Profile("db-docslocal")
public class ReferencePersonDocsDbConfig {
//
//	@Primary
//	@Bean(name = "persondocsDataSource")
//	public DataSource dataSource() {
//		return DataSourceBuilder.create().build();
//	}
//
//	@Primary
//	@Bean(name = "persondocsEntityManagerFactory")
//	public LocalContainerEntityManagerFactoryBean entityManagerFactory(
//			EntityManagerFactoryBuilder builder,
//			@Qualifier("dataSource") DataSource dataSource) {
//		return builder
//				.dataSource(dataSource)
//				.packages("gov.va.bip.reference.person.data.docs.entities")
//				.persistenceUnit("persondocs")
//				.build();
//	}
//
//	@Primary
//	@Bean(name = "persondocsTransactionManager")
//	public PlatformTransactionManager transactionManager(
//			@Qualifier("persondocsEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
//		return new JpaTransactionManager(entityManagerFactory);
//	}
}
