package gov.va.ocp.reference.partner.person.ws.client.remote;

import java.text.MessageFormat;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

import gov.va.ocp.reference.framework.config.ReferenceCommonSpringProfiles;
import gov.va.ocp.reference.framework.log.ReferenceLogger;
import gov.va.ocp.reference.framework.log.ReferenceLoggerFactory;
import gov.va.ocp.reference.framework.security.PersonTraits;
import gov.va.ocp.reference.framework.security.SecurityUtils;
import gov.va.ocp.reference.framework.transfer.PartnerTransferObjectMarker;
import gov.va.ocp.reference.framework.util.Defense;
import gov.va.ocp.reference.framework.ws.client.remote.AbstractRemoteServiceCallMock;
import gov.va.ocp.reference.framework.ws.client.remote.RemoteServiceCall;
import gov.va.ocp.reference.partner.person.ws.client.PersonWsClientException;
import gov.va.ocp.reference.partner.person.ws.transfer.FindPersonByPtcpntId;

/**
 * Implements the {@link RemoteServiceCall} interface, and extends
 * {@link AbstractRemoteServiceCallMock} for mocking the remote client under the
 * simulators spring profile.
 */
@Profile(ReferenceCommonSpringProfiles.PROFILE_REMOTE_CLIENT_SIMULATORS)
@Component(PersonRemoteServiceCallImpl.BEAN_NAME) // intentionally using the IMPL name
public class PersonRemoteServiceCallMock extends AbstractRemoteServiceCallMock {
	private static final ReferenceLogger LOGGER = ReferenceLoggerFactory.getLogger(PersonRemoteServiceCallMock.class);

	/*
	 * Constants for mock XML file names
	 */
	/** error message if request is null */
	static final String ERROR_NULL_REQUEST = "getKeyForMockResponse request parameter cannot be null.";

	// TODO
	/** Error message prefix if request type is not handled in getKeyForMockResponse(..) */
	static final String ERROR_UNHANDLED_REQUEST_TYPE =
			PersonRemoteServiceCallMock.class.getSimpleName()
					+ ".getKeyForMockResponse(..) does not have a file naming block for requests of type ";

	/** The {@code src/main/resources/test/mocks/*} simple filename for the "filename.xml" mock file. */
	static final String MOCK_FINDPERSONBYPTCPNTID_RESPONSE = "person.getPersonInfoByPtcpntId";

	/** The {@code src/main/resources/test/mocks/*} filename prefix for the "filename.PID.xml" mock file. */
	static final String MOCK_FINDPERSONBYPTCPNTID_RESPONSE_WITH_ID = "person.getPersonInfoByPtcpntId.{0}";

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.va.ocp.reference.framework.ws.client.remote.RemoteServiceCall#callRemoteService( org.springframework.ws.client.core.
	 * WebServiceTemplate, gov.va.ocp.reference.framework.transfer.PartnerTransferObjectMarker, java.lang.Class)
	 */
	@Override
	public PartnerTransferObjectMarker callRemoteService(final WebServiceTemplate webserviceTemplate,
			final PartnerTransferObjectMarker request, final Class<? extends PartnerTransferObjectMarker> requestClass) {

		LOGGER.info("Calling MOCK service with request " + ReflectionToStringBuilder.toString(request));
		// super handles exceptions
		return super.callMockService(webserviceTemplate, request, requestClass);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.va.ocp.reference.framework.ws.client.remote.AbstractRemoteServiceCallMock#
	 * getKeyForMockResponse(gov.va.ocp.reference.framework.transfer. PartnerTransferObjectMarker)
	 */
	@Override
	protected String getKeyForMockResponse(final PartnerTransferObjectMarker request) {
		Defense.notNull(request, ERROR_NULL_REQUEST);

		String mockFilename = null;

		// TODO
		if (request.getClass().isAssignableFrom(FindPersonByPtcpntId.class)) {
			mockFilename = getFileName(MOCK_FINDPERSONBYPTCPNTID_RESPONSE, MOCK_FINDPERSONBYPTCPNTID_RESPONSE_WITH_ID);

		} else {
			throw new PersonWsClientException(
					this.getClass().getSimpleName() + ERROR_UNHANDLED_REQUEST_TYPE + request.getClass().getName());
		}

		// return value can never be null or empty, there is Defense against it
		return mockFilename;
	}

	/**
	 * @return The file name
	 */
	private String getFileName(final String fileNamePattern1, final String fileNamePattern2) {
		String fileName = fileNamePattern1;
		final PersonTraits personTraits = SecurityUtils.getPersonTraits();

		if (personTraits != null
				&& personTraits.getPid() != null
//	TODO is this necessary? >>			&& personTraits.getPid().startsWith("1967080")
		) {
			fileName = MessageFormat.format(fileNamePattern2, personTraits.getPid());
		}
		return fileName;
	}

}
