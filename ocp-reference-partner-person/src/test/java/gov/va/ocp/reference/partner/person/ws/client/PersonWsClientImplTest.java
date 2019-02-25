package gov.va.ocp.reference.partner.person.ws.client;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import gov.va.ocp.reference.framework.config.ReferenceCommonSpringProfiles;
import gov.va.ocp.reference.framework.security.PersonTraits;
import gov.va.ocp.reference.partner.person.ws.client.PersonWsClientConfig;
import gov.va.ocp.reference.partner.person.ws.client.PersonWsClientImpl;
import gov.va.ocp.reference.partner.person.ws.transfer.FindPersonByPtcpntId;
import gov.va.ocp.reference.partner.person.ws.transfer.FindPersonByPtcpntIdResponse;

/**
 * <p>
 * Tests the webservice implementation. Note specifically the @ActiveProfiles
 * and @ContextConfiguration.
 * </p>
 * <p>
 * To engage mocking capabilities, @ActiveProfiles must specify the simulator
 * profile. {@link IntentToFileRemoteServiceCallMock} declares itself as the
 * mocking implementation for the simulator profile.
 * </p>
 * <p>
 * MockitoJUnitRunner class cannot be used to @RunWith because the application
 * context must Autowire the WebServiceTemplate from the client implementation.
 * </p>
 *
 * @author aburkholder
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(inheritListeners = false, listeners = { DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
@ActiveProfiles({ ReferenceCommonSpringProfiles.PROFILE_REMOTE_CLIENT_SIMULATORS })
@ContextConfiguration(inheritLocations = false, classes = { PartnerMockFrameworkTestConfig.class,
		PersonWsClientConfig.class })
public class PersonWsClientImplTest extends AbstractPersonTest {

	@Autowired
	@Qualifier(PersonWsClientImpl.BEAN_NAME)
	PersonWsClientImpl personWsClientImpl;

	@Before
	public void setUp() {
		assertNotNull("FAIL intenttofileWsClientImpl cannot be null.", personWsClientImpl);
	}

	@After
	public void tearDown() {
		SecurityContextHolder.clearContext();
	}

	@Test
	@Ignore
	public void testFindPersonByPtcpntId() {
		PersonTraits personTraits = new PersonTraits("user", "password",
				AuthorityUtils.createAuthorityList("ROLE_TEST"));
		personTraits.setPid("196708051");
		Authentication auth = new UsernamePasswordAuthenticationToken(personTraits, personTraits.getPassword(),
				personTraits.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);

		final FindPersonByPtcpntId findPersonByPtcpntId = new FindPersonByPtcpntId();
		FindPersonByPtcpntIdResponse response = personWsClientImpl.getPersonInfoByPtcpntId(findPersonByPtcpntId);
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getPersonDTO());
//		TODO need to set up the mock and test data, then devise assertions for it
//		Assert.assertEquals(ONE_TWO_THEREE, response.getPersonDTO().get());
//		assertTrue("Duplicate".equals(response.getPersonDTO().get()));
	}
}