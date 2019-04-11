package gov.va.bip.reference.partner.person.client.ws.remote;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

import gov.va.bip.framework.client.ws.remote.RemoteServiceCall;
import gov.va.bip.framework.config.BipCommonSpringProfiles;
import gov.va.bip.framework.log.BipLogger;
import gov.va.bip.framework.log.BipLoggerFactory;
import gov.va.bip.framework.transfer.PartnerTransferObjectMarker;

/**
 * Implements the {@link RemoteServiceCall} interface for the remote client impls spring profile
 */
@Profile(BipCommonSpringProfiles.PROFILE_REMOTE_CLIENT_IMPLS)
@Component(PersonRemoteServiceCallImpl.BEAN_NAME)
public class PersonRemoteServiceCallImpl implements RemoteServiceCall {
	private static final BipLogger LOGGER = BipLoggerFactory.getLogger(PersonRemoteServiceCallImpl.class);

	/** The spring bean name for implementation. MUST BE UNIQUE ACROSS ALL PARTNER JARS */
	public static final String BEAN_NAME = "personRemoteServiceCall";

	@Override
	public PartnerTransferObjectMarker callRemoteService(final WebServiceTemplate webserviceTemplate,
			final PartnerTransferObjectMarker request,
			final Class<? extends PartnerTransferObjectMarker> requestClass) {

		PartnerTransferObjectMarker response = null;

		LOGGER.info("Calling partner SOAP service with request " + ReflectionToStringBuilder.toString(request));
		response = (PartnerTransferObjectMarker) webserviceTemplate.marshalSendAndReceive(requestClass.cast(request));

		return response;
	}

}
