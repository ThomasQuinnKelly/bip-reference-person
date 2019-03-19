package gov.va.ocp.reference.partner.person.ws.client;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.ws.WebServiceException;
import org.springframework.ws.client.core.WebServiceTemplate;

import gov.va.ocp.framework.audit.AuditEvents;
import gov.va.ocp.framework.audit.Auditable;
import gov.va.ocp.framework.log.OcpLogger;
import gov.va.ocp.framework.log.OcpLoggerFactory;
import gov.va.ocp.framework.messages.MessageSeverity;
import gov.va.ocp.framework.validation.Defense;
import gov.va.ocp.framework.ws.client.BaseWsClientImpl;
import gov.va.ocp.framework.ws.client.remote.RemoteServiceCall;
import gov.va.ocp.reference.partner.person.ws.client.remote.PersonRemoteServiceCallImpl;
import gov.va.ocp.reference.partner.person.ws.transfer.FindPersonByPtcpntId;
import gov.va.ocp.reference.partner.person.ws.transfer.FindPersonByPtcpntIdResponse;

/**
 * This class implements the Person WS Client interface. It encapsulates the details of interacting with the Person Web Service.
 *
 */
@Component(PersonWsClientImpl.BEAN_NAME)
public class PersonWsClientImpl extends BaseWsClientImpl implements PersonWsClient {
	/** Logger */
	private static final OcpLogger LOGGER = OcpLoggerFactory.getLogger(PersonWsClientImpl.class);

	/** A constant representing the Spring Bean name. */
	public static final String BEAN_NAME = "personWsClient";

	/** the switchable remote for service calls (impl or mock) */
	@Autowired
	@Qualifier(PersonRemoteServiceCallImpl.BEAN_NAME)
	private RemoteServiceCall remoteServiceCall;

	/** axiom web service template. */
	@Autowired
	@Qualifier("personWsClientAxiomTemplate")
	private WebServiceTemplate personWsTemplate;

	/**
	 * The WebServiceTemplate can't be null.
	 */
	@PostConstruct
	public final void postConstruct() {
		Defense.notNull(remoteServiceCall, "remoteServiceCall cannot be null.");
		Defense.notNull(personWsTemplate, "axiomWebServiceTemplate cannot be null in order for "
				+ this.getClass().getSimpleName() + " to work properly.");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * gov.va.ocp.reference.partner.person.ws.client.PersonWsClient#getPersonInfoByPtcpntId(gov.va.ocp.reference.partner.person.ws.
	 * transfer.FindPersonByPtcpntId)
	 */
	@Override
	@Auditable(event = AuditEvents.REQUEST_RESPONSE, activity = "partnerPersonInfoByPtcpntId")
	public FindPersonByPtcpntIdResponse getPersonInfoByPtcpntId(final FindPersonByPtcpntId findPersonByPtcpntIdRequest)
			throws PersonPartnerCheckedException {

		Defense.notNull(findPersonByPtcpntIdRequest, REQUEST_FOR_WEBSERVICE_CALL_NULL);

		LOGGER.debug("Calling partner client " + remoteServiceCall.getClass().getSimpleName()
				+ " with request " + findPersonByPtcpntIdRequest.getClass().getSimpleName());

		Object webServiceResponse = null;
		try {
			webServiceResponse = remoteServiceCall.callRemoteService(
					personWsTemplate, findPersonByPtcpntIdRequest, findPersonByPtcpntIdRequest.getClass());
			/*
			 * NOTE THAT PersonWsClientConfig configures an InterceptingExceptionTranslator
			 * that by default ignores any exceptions declared in the
			 * "gov.va.ocp.framework.exceptions" package.
			 *
			 * If some issue other than a SOAP Fault happens,
			 * we intentionally allow the exception to propagate as-is
			 * for the InterceptingExceptionTranslator to convert into
			 * an OcpPartnerRuntimeException.
			 */
		} catch (WebServiceException sf) { // <- usually thrown as SoapFaultClientException
			/*
			 * The called webservice threw a SOAP Fault back to us,
			 * so they have something to tell us.
			 * Re-throw the soap fault as a checked exception
			 * so the calling Helper has the opportunity to
			 * do something about it if necessary.
			 */
			throw new PersonPartnerCheckedException("", sf.getMessage(), MessageSeverity.ERROR, HttpStatus.BAD_REQUEST, sf);
		}

		Defense.notNull(webServiceResponse, RESPONSE_FROM_WEBSERVICE_CALL_NULL);
		// Below Defense is IMPORTANT - mocked responses are easy to mess up,
		// and partner responses that come back with unexpected type are hard to trace otherwise
		Defense.isInstanceOf(FindPersonByPtcpntIdResponse.class, webServiceResponse);

		return (FindPersonByPtcpntIdResponse) webServiceResponse;
	}

}
