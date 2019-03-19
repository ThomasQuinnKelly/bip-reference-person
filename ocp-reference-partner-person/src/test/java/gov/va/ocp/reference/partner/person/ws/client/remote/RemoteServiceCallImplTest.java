package gov.va.ocp.reference.partner.person.ws.client.remote;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.ws.test.client.RequestMatchers.payload;
import static org.springframework.ws.test.client.ResponseCreators.withPayload;

import java.io.IOException;
import java.text.MessageFormat;

import javax.xml.transform.Source;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
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
import org.springframework.ws.test.client.MockWebServiceServer;
import org.springframework.xml.transform.ResourceSource;
import org.springframework.xml.transform.StringResult;
import org.springframework.xml.transform.StringSource;

import gov.va.ocp.framework.config.OcpCommonSpringProfiles;
import gov.va.ocp.framework.exception.OcpPartnerRuntimeException;
import gov.va.ocp.framework.security.PersonTraits;
import gov.va.ocp.framework.transfer.PartnerTransferObjectMarker;
import gov.va.ocp.framework.validation.Defense;
import gov.va.ocp.framework.ws.client.remote.RemoteServiceCall;
import gov.va.ocp.reference.partner.person.ws.client.AbstractPersonTest;
import gov.va.ocp.reference.partner.person.ws.client.PartnerMockFrameworkTestConfig;
import gov.va.ocp.reference.partner.person.ws.client.PersonWsClientConfig;
import gov.va.ocp.reference.partner.person.ws.transfer.FindPersonByPtcpntId;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(inheritListeners = false, listeners = { DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
@ActiveProfiles({ OcpCommonSpringProfiles.PROFILE_REMOTE_CLIENT_IMPLS })
@ContextConfiguration(inheritLocations = false, classes = { PartnerMockFrameworkTestConfig.class, PersonWsClientConfig.class })
public class RemoteServiceCallImplTest extends AbstractPersonTest {

	private static final String MOCKS_PATH = "test/mocks/";

	private static final String PARTICIPANTID_FOR_MOCK_DATA = "13364995";

	// Autowire the IMPL class for the RemoteServiceCall interface declared by the current @ActiveProfiles
	@Autowired
	private RemoteServiceCall callPartnerService;

	private MockWebServiceServer mockWebServicesServer;

	@Autowired
	@Qualifier("personWsClientAxiomTemplate")
	private WebServiceTemplate axiomWebServiceTemplate;

	@Mock
	private WebServiceTemplate axiomWebServiceTemplateMock;

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Before
	public void setUp() {
		assertNotNull("FAIL axiomWebServiceTemplate cannot be null.", axiomWebServiceTemplate);
		assertNotNull("FAIL callPartnerService cannot be null.", callPartnerService);

		assertTrue(callPartnerService instanceof PersonRemoteServiceCallImpl);
		mockWebServicesServer = MockWebServiceServer.createServer(axiomWebServiceTemplate);
		assertNotNull("FAIL mockWebServicesServer cannot be null.", mockWebServicesServer);
	}

	@Test
	public void testCallRemoteService() {
		PersonTraits personTraits = new PersonTraits("user", "password", AuthorityUtils.createAuthorityList("ROLE_TEST"));
		personTraits.setPid(PARTICIPANTID_FOR_MOCK_DATA);
		Authentication auth =
				new UsernamePasswordAuthenticationToken(personTraits, personTraits.getPassword(), personTraits.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);

		final FindPersonByPtcpntId request = super.makeFindPersonByPtcpntIdRequest();
		final Source requestPayload =
				marshalMockRequest((Jaxb2Marshaller) axiomWebServiceTemplate.getMarshaller(), request, request.getClass());
		final Source responsePayload =
				readMockResponseByKey(PersonRemoteServiceCallMock.MOCK_FINDPERSONBYPTCPNTID_RESPONSE, PARTICIPANTID_FOR_MOCK_DATA);

		mockWebServicesServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));

		try {
			callPartnerService.callRemoteService(axiomWebServiceTemplate, request, FindPersonByPtcpntId.class);

			mockWebServicesServer.verify();

		} catch (final Throwable e) {
			e.printStackTrace();

			fail("FAIL mockWebServicesServer did not function as expected");
		}
	}

	@Test
	public void testCallRemoteServiceExceptionHandling() {
		final FindPersonByPtcpntId request = super.makeFindPersonByPtcpntIdRequest();

		when(axiomWebServiceTemplateMock.marshalSendAndReceive(any(PartnerTransferObjectMarker.class)))
				.thenThrow(new RuntimeException());

		try {
			callPartnerService.callRemoteService(axiomWebServiceTemplateMock, request, FindPersonByPtcpntId.class);

		} catch (Throwable e) {
			e.printStackTrace();
			assertNotNull(e);
		}
	}

	/**
	 * Exact copy of the request
	 *
	 * @param marshaller
	 * @param request
	 * @param requestClass
	 * @return
	 */
	private StringSource marshalMockRequest(final Jaxb2Marshaller marshaller, final PartnerTransferObjectMarker request,
			final Class<?> requestClass) {
		final StringResult result = new StringResult();
		marshaller.marshal(requestClass.cast(request), result);
		return new StringSource(result.toString());
	}

	/**
	 * Request payload creation specific to this endpoint operation test.
	 *
	 * @param keyPath
	 * @return
	 */
	private ResourceSource readMockResponseByKey(final String filenamePattern, final String replaceableParam) {
		Defense.hasText(filenamePattern);
		Defense.hasText(replaceableParam);

		// read the resource
		String filename = MOCKS_PATH + MessageFormat.format(filenamePattern, replaceableParam) + ".xml";
		ResourceSource resource = null;
		try {
			resource = new ResourceSource(new ClassPathResource(filename));
		} catch (final IOException e) {
			throw new OcpPartnerRuntimeException(null, "Could not read mock XML file '" + filename
					+ "'. Please make sure this response file exists in the main/resources directory.",
					null, null, e);
		}
		return resource;
	}
}
