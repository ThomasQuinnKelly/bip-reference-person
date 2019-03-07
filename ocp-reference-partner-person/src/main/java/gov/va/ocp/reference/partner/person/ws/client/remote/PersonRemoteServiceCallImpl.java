package gov.va.ocp.reference.partner.person.ws.client.remote;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

import gov.va.ocp.framework.config.OcpCommonSpringProfiles;
import gov.va.ocp.framework.log.OcpLogger;
import gov.va.ocp.framework.log.OcpLoggerFactory;
import gov.va.ocp.framework.transfer.PartnerTransferObjectMarker;
import gov.va.ocp.framework.ws.client.remote.RemoteServiceCall;

/**
 * Implements the {@link RemoteServiceCall} interface for the remote client impls spring profile
 */
@Profile(OcpCommonSpringProfiles.PROFILE_REMOTE_CLIENT_IMPLS)
@Component(PersonRemoteServiceCallImpl.BEAN_NAME)
public class PersonRemoteServiceCallImpl implements RemoteServiceCall {
	private static final OcpLogger LOGGER = OcpLoggerFactory.getLogger(PersonRemoteServiceCallImpl.class);

	/** The spring bean name for implementation. MUST BE UNIQUE ACROSS ALL PARTNER JARS */
	public static final String BEAN_NAME = "personRemoteServiceCall";

	@Override
	public PartnerTransferObjectMarker callRemoteService(final WebServiceTemplate webserviceTemplate,
			final PartnerTransferObjectMarker request,
			final Class<? extends PartnerTransferObjectMarker> requestClass) {

		PartnerTransferObjectMarker response = null;

		try {
			LOGGER.info("Calling partner SOAP service with request " + ReflectionToStringBuilder.toString(request));
			response = (PartnerTransferObjectMarker) webserviceTemplate.marshalSendAndReceive(requestClass.cast(request));
		} catch (Exception e) {
			LOGGER.error("IMPL partner service call failed with requestClass "
					+ requestClass.getName() + " and request object " + ReflectionToStringBuilder.toString(request), e);
			throw e;
		}

		return response;
	}

}
