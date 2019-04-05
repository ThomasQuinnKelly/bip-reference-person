package gov.va.bip.reference.partner.person.ws.client.remote;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
import org.springframework.ws.client.core.WebServiceTemplate;

import gov.va.bip.framework.config.BipCommonSpringProfiles;
import gov.va.bip.framework.exception.BipValidationRuntimeException;
import gov.va.bip.framework.security.PersonTraits;
import gov.va.bip.reference.partner.person.ws.client.AbstractPersonTest;
import gov.va.bip.reference.partner.person.ws.client.PartnerMockFrameworkTestConfig;
import gov.va.bip.reference.partner.person.ws.client.PersonWsClientConfig;
import gov.va.bip.reference.partner.person.ws.client.remote.PersonRemoteServiceCallMock;
import gov.va.bip.reference.partner.person.ws.transfer.FindPersonByPtcpntId;
import gov.va.bip.reference.partner.person.ws.transfer.FindPersonByPtcpntIdResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(inheritListeners = false, listeners = { DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
@ActiveProfiles({ BipCommonSpringProfiles.PROFILE_REMOTE_CLIENT_SIMULATORS })
@ContextConfiguration(inheritLocations = false, classes = { PartnerMockFrameworkTestConfig.class, PersonWsClientConfig.class })
public class RemoteServiceCallMockTest extends AbstractPersonTest {

	private static final String PARTICIPANTID_FOR_MOCK_DATA = "13364995";

	static final String MOCK_FINDPERSONBYPTCPNTID_RESPONSE = "person.getPersonInfoByPtcpntId.{0}";

	static final String MOCK_FINDPERSONBYPTCPNTID_RESPONSE_WITHOUT_BRACES = "person.getPersonInfoByPtcpntId";

	@Autowired
	@Qualifier("personWsClientAxiomTemplate")
	private WebServiceTemplate axiomWebServiceTemplate;

	@Before
	public void setUp() {
		PersonTraits personTraits = new PersonTraits("user", "password", AuthorityUtils.createAuthorityList("ROLE_TEST"));
		personTraits.setPid(PARTICIPANTID_FOR_MOCK_DATA);
		Authentication auth =
				new UsernamePasswordAuthenticationToken(personTraits, personTraits.getPassword(), personTraits.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);

		assertNotNull("FAIL axiomWebServiceTemplate cannot be null.", axiomWebServiceTemplate);
	}

	@After
	public void tearDown() {
		SecurityContextHolder.clearContext();
	}

	@Test
	public void callRemoteServiceTest() {
		final FindPersonByPtcpntId request = super.makeFindPersonByPtcpntIdRequest();
		request.setPtcpntId(Long.valueOf(PARTICIPANTID_FOR_MOCK_DATA));
		PersonRemoteServiceCallMock mockServiceCall = new PersonRemoteServiceCallMock();

		try {
			FindPersonByPtcpntIdResponse response = (FindPersonByPtcpntIdResponse) mockServiceCall
					.callRemoteService(axiomWebServiceTemplate, request, FindPersonByPtcpntId.class);
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
		} catch (final Throwable e) {
			e.printStackTrace();

			fail("FAIL mockWebServicesServer did not function as expected");
		}
	}

	@Test
	public void testGetKeyForMockResponseListAll() {
		PersonRemoteServiceCallMock mock = new PersonRemoteServiceCallMock();
		FindPersonByPtcpntId request = super.makeFindPersonByPtcpntIdRequest();
		String keyForMockResponse = mock.getKeyForMockResponse(request);

		assertNotNull(keyForMockResponse);
		assertTrue(keyForMockResponse.startsWith("person.getPersonInfoByPtcpntId"));
	}

	@Test
	public void testGetKeyForMockResponse_NullRequest() {
		PersonRemoteServiceCallMock mock = new PersonRemoteServiceCallMock();
		FindPersonByPtcpntId request = null;

		String keyForMockResponse = null;

		try {
			keyForMockResponse = mock.getKeyForMockResponse(request);
		} catch (Throwable e) {
			assertTrue("Invalid exception was thrown.", BipValidationRuntimeException.class.equals(e.getClass()));
			assertTrue("Exception message contains wrong string.",
					e.getMessage().equals(PersonRemoteServiceCallMock.ERROR_NULL_REQUEST));
		}

		assertNull("Null request should have thrown exception.", keyForMockResponse);
	}

	@Test
	public void TestGetFileName() {
		String filename = PersonRemoteServiceCallMock.getFileName(MOCK_FINDPERSONBYPTCPNTID_RESPONSE_WITHOUT_BRACES, "");
		assertTrue(MOCK_FINDPERSONBYPTCPNTID_RESPONSE_WITHOUT_BRACES.equals(filename));

		PersonTraits personTraits = new PersonTraits("user", "password", AuthorityUtils.createAuthorityList("ROLE_TEST"));
		personTraits.setPid(PARTICIPANTID_FOR_MOCK_DATA);
		Authentication auth =
				new UsernamePasswordAuthenticationToken(personTraits, personTraits.getPassword(), personTraits.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);

		filename = PersonRemoteServiceCallMock.getFileName(MOCK_FINDPERSONBYPTCPNTID_RESPONSE_WITHOUT_BRACES, "");
		assertTrue(MOCK_FINDPERSONBYPTCPNTID_RESPONSE_WITHOUT_BRACES.equals(filename));

	}

}
