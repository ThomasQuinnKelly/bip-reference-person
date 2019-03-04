package gov.va.ocp.reference.partner.person.ws.client;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
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

import gov.va.ocp.reference.framework.config.OcpCommonSpringProfiles;
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
@ActiveProfiles({ OcpCommonSpringProfiles.PROFILE_REMOTE_CLIENT_SIMULATORS })
@ContextConfiguration(inheritLocations = false, classes = { PartnerMockFrameworkTestConfig.class,
		PersonWsClientConfig.class })
public class PersonWsClientImplTest extends AbstractPersonTest {

	private static final String PARTICIPANTID_FOR_MOCK_DATA = "13364995";

	@Autowired
	@Qualifier(PersonWsClientImpl.BEAN_NAME)
	PersonWsClientImpl personWsClientImpl;

	@Before
	public void setUp() {
		assertNotNull("FAIL personWsClientImpl cannot be null.", personWsClientImpl);
	}

	@After
	public void tearDown() {
		SecurityContextHolder.clearContext();
	}

	@Test
	public void testFindPersonByPtcpntId() {
		PersonTraits personTraits = new PersonTraits("user", "password", AuthorityUtils.createAuthorityList("ROLE_TEST"));
		personTraits.setPid(PARTICIPANTID_FOR_MOCK_DATA);
		Authentication auth =
				new UsernamePasswordAuthenticationToken(personTraits, personTraits.getPassword(), personTraits.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);

		final FindPersonByPtcpntId findPersonByPtcpntId = new FindPersonByPtcpntId();
		findPersonByPtcpntId.setPtcpntId(Long.valueOf(PARTICIPANTID_FOR_MOCK_DATA));
		FindPersonByPtcpntIdResponse response = personWsClientImpl.getPersonInfoByPtcpntId(findPersonByPtcpntId);
		assertNotNull(response);
		assertNotNull(response.getPersonDTO());
		assertTrue(response.getPersonDTO().getBrthdyDt().toString().equals("1961-09-02T00:00:00-05:00"));
		assertTrue(response.getPersonDTO().getEmailAddr().equals("russell@gmail.com"));
		assertTrue(response.getPersonDTO().getFileNbr().equals("796079018"));
		assertTrue(response.getPersonDTO().getFirstNm().equals("RUSSELL"));
		assertTrue(response.getPersonDTO().getFirstNmKey().equals(-2L));
		assertTrue(response.getPersonDTO().getGenderCd().equals("M"));
		assertTrue(response.getPersonDTO().getJrnDt().toString().equals("2014-03-19T11:14:13-05:00"));
		assertTrue(response.getPersonDTO().getJrnLctnId().equals("281"));
		assertTrue(response.getPersonDTO().getJrnObjId().equals("VBMS - CEST"));
		assertTrue(response.getPersonDTO().getJrnStatusTypeCd().equals("U"));
		assertTrue(response.getPersonDTO().getJrnUserId().equals("VBMSSYSACCT"));
		assertTrue(response.getPersonDTO().getLastNm().equals("WATSON"));
		assertTrue(response.getPersonDTO().getLastNmKey().equals(0L));
		assertTrue(response.getPersonDTO().getMiddleNm().equals("BILL"));
		assertTrue(response.getPersonDTO().getMiddleNmKey().equals(-1L));
		assertTrue(response.getPersonDTO().getMltyPersonInd().equals("Y"));
		assertTrue(response.getPersonDTO().getPtcpntId().toString().equals(PARTICIPANTID_FOR_MOCK_DATA));
		assertTrue(response.getPersonDTO().getSsnNbr().equals("796079018"));
		assertTrue(response.getPersonDTO().getSsnVrfctnStatusTypeCd().equals("0"));
		assertTrue(response.getPersonDTO().getStationOfJurisdiction().equals("317"));
		assertTrue(response.getPersonDTO().getTermnlDigitNbr().equals("18"));
		assertTrue(response.getPersonDTO().getVetInd().equals("Y"));
	}
}