package gov.va.os.reference.partner.mock.framework;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import gov.va.os.reference.framework.config.ReferenceCommonSpringProfiles;
import gov.va.os.reference.partner.mock.framework.PartnerMockFrameworkConfig;

/**
 * Base tests for all spring integrated unit tests in this project
 * 
 * @author jshrader
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(ReferenceCommonSpringProfiles.PROFILE_REMOTE_CLIENT_SIMULATORS)
@ContextConfiguration(inheritLocations = false, classes = { PartnerMockFrameworkTestPropertiesConfig.class, PartnerMockFrameworkConfig.class })
public abstract class AbstractPartnerMockFrameworkSpringIntegratedTest {

}