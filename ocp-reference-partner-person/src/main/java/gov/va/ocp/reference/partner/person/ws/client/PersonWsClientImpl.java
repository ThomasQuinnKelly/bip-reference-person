package gov.va.ocp.reference.partner.person.ws.client;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

import gov.va.ocp.reference.framework.audit.AuditEvents;
import gov.va.ocp.reference.framework.audit.Auditable;
import gov.va.ocp.reference.framework.log.OcpLogger;
import gov.va.ocp.reference.framework.log.OcpLoggerFactory;
import gov.va.ocp.reference.framework.util.Defense;
import gov.va.ocp.reference.framework.ws.client.BaseWsClientImpl;
import gov.va.ocp.reference.framework.ws.client.remote.RemoteServiceCall;
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
	public FindPersonByPtcpntIdResponse getPersonInfoByPtcpntId(final FindPersonByPtcpntId findPersonByPtcpntIdRequest) {
		Defense.notNull(findPersonByPtcpntIdRequest, REQUEST_FOR_WEBSERVICE_CALL_NULL);
		LOGGER.debug("Calling partner client " + remoteServiceCall.getClass().getSimpleName()
				+ " with request " + findPersonByPtcpntIdRequest.getClass().getSimpleName());
		final Object webServiceResponse = remoteServiceCall.callRemoteService(
				personWsTemplate, findPersonByPtcpntIdRequest, findPersonByPtcpntIdRequest.getClass());
		Defense.notNull(webServiceResponse, RESPONSE_FROM_WEBSERVICE_CALL_NULL);
		// Below Defense is IMPORTANT - mocked responses are easy to mess up,
		// and partner responses that come back with unexpected type are hard to trace otherwise
		Defense.isInstanceOf(FindPersonByPtcpntIdResponse.class, webServiceResponse);

		return (FindPersonByPtcpntIdResponse) webServiceResponse;
	}

}
