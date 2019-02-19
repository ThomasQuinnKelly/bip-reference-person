package gov.va.os.reference.partner.mock.framework;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;

import gov.va.os.reference.framework.config.ReferenceCommonSpringProfiles;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Spring configuration for the project.
 *
 * @author Jon Shrader
 */
@Configuration
@Profile({ ReferenceCommonSpringProfiles.PROFILE_REMOTE_CLIENT_SIMULATORS, PartnerMockFrameworkConfig.PROFILE_PARTNER_MOCK_FRAMEWORK })
@ComponentScan(basePackages = "gov.va.os.reference.partner.mock.framework", excludeFilters = @Filter(Configuration.class))
public class PartnerMockFrameworkConfig { // NOSONAR this is config, not a static class

	/** Activates the partner mock framework */
	public static final String PROFILE_PARTNER_MOCK_FRAMEWORK = "partner_mock_framework";

}
