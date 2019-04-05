package gov.va.bip.reference.partner.person.ws.client;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import gov.va.bip.framework.config.BaseYamlConfig;

/**
 * Fake Spring configuration used to test the partner mock framework classes
 *
 * @author jshrader
 */
@Configuration
public class PartnerMockFrameworkTestConfig extends BaseYamlConfig {

	/** The Constant DEFAULT_PROPERTIES. */
	private static final String DEFAULT_PROPERTIES = "classpath:/application.yml";

	/**
	 * The local environment configuration.
	 */
	@Configuration
	@PropertySource(DEFAULT_PROPERTIES)
	static class DefaultEnvironment extends BaseYamlEnvironment {
	}

}
