package gov.va.os.reference.partner.person.ws.client.remote;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

import gov.va.os.reference.framework.config.ReferenceCommonSpringProfiles;
import gov.va.os.reference.framework.log.ReferenceLogger;
import gov.va.os.reference.framework.log.ReferenceLoggerFactory;
import gov.va.os.reference.framework.transfer.PartnerTransferObjectMarker;
import gov.va.os.reference.framework.ws.client.remote.RemoteServiceCall;

/**
 * Implements the {@link RemoteServiceCall} interface for the remote client impls spring profile
 */
@Profile(ReferenceCommonSpringProfiles.PROFILE_REMOTE_CLIENT_IMPLS)
@Component(PersonRemoteServiceCallImpl.BEAN_NAME)
public class PersonRemoteServiceCallImpl implements RemoteServiceCall {
	private static final ReferenceLogger LOGGER = ReferenceLoggerFactory.getLogger(PersonRemoteServiceCallImpl.class);

	/** The spring bean name for implementation. MUST BE UNIQUE ACROSS ALL PARTNER JARS */
	public static final String BEAN_NAME = "intenttofileRemoteServiceCall";

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
