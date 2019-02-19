package gov.va.os.reference.partner.mock.framework;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import gov.va.os.reference.partner.mock.framework.PartnerMockFrameworkConfig;

/**
 * Fake Spring configuration used to test the partner mock framework classes
 * 
 * @author jshrader
 */
@Configuration
@Import({ PartnerMockFrameworkTestPropertiesConfig.class, PartnerMockFrameworkConfig.class })
public class PartnerMockFrameworkTestConfig {

}
