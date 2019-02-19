package gov.va.os.reference.framework.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.Resource;

import gov.va.os.reference.framework.log.ReferenceLogger;
import gov.va.os.reference.framework.log.ReferenceLoggerFactory;

/**
 * Abstract base class for Spring configuration of the YAML files
 *
 * @author Abhijit Kulkarni
 */
public class BaseYamlConfig {

	/**
	 * AbstractPropertiesEnvironment, parent of all our properties environments.
	 */
	public static class BaseYamlEnvironment {

		/**
		 * Post construct called after spring initialization completes.
		 */
		@PostConstruct
		public final void postConstruct() {
			LOGGER.info("Loading environment: " + this.getClass().getName());
		}
	}

	/** logger for this class. */
	private static final ReferenceLogger LOGGER = ReferenceLoggerFactory.getLogger(BaseYamlConfig.class);

	/**
	 * protected utility class constructor.
	 */
	protected BaseYamlConfig() {
	}

	/**
	 * properties bean
	 *
	 * @return the property sources placeholder configurer
	 */
	@Bean(name = "properties")
	static PropertySourcesPlaceholderConfigurer properties(@Value("classpath:/application.yml") Resource config) {
		PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
		YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
		yaml.setResources(config);
		propertySourcesPlaceholderConfigurer.setProperties(yaml.getObject());
		return propertySourcesPlaceholderConfigurer;
	}

}
