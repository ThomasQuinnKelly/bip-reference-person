package gov.va.bip.reference.partner.person.ws.client.remote;

import java.text.MessageFormat;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

import gov.va.bip.framework.config.BipCommonSpringProfiles;
import gov.va.bip.framework.exception.BipPartnerRuntimeException;
import gov.va.bip.framework.log.BipLogger;
import gov.va.bip.framework.log.BipLoggerFactory;
import gov.va.bip.framework.messages.MessageKeys;
import gov.va.bip.framework.messages.MessageSeverity;
import gov.va.bip.framework.security.PersonTraits;
import gov.va.bip.framework.security.SecurityUtils;
import gov.va.bip.framework.transfer.PartnerTransferObjectMarker;
import gov.va.bip.framework.validation.Defense;
import gov.va.bip.framework.ws.client.remote.AbstractRemoteServiceCallMock;
import gov.va.bip.framework.ws.client.remote.RemoteServiceCall;
import gov.va.bip.reference.partner.person.ws.transfer.FindPersonByPtcpntId;

/**
 * Implements the {@link RemoteServiceCall} interface, and extends {@link AbstractRemoteServiceCallMock} for mocking the remote client
 * under the simulators spring profile.
 */
@Profile(BipCommonSpringProfiles.PROFILE_REMOTE_CLIENT_SIMULATORS)
@Component(PersonRemoteServiceCallImpl.BEAN_NAME) // intentionally using the IMPL name
public class PersonRemoteServiceCallMock extends AbstractRemoteServiceCallMock {
	/** Logger */
	private static final BipLogger LOGGER = BipLoggerFactory.getLogger(PersonRemoteServiceCallMock.class);

	/** error message if request is null */
	static final String ERROR_NULL_REQUEST = "getKeyForMockResponse request parameter cannot be null.";

	/*
	 * Below: BipConstants for mock XML file names
	 */

	/** The {@code src/main/resources/test/mocks/*} filename prefix for the "filename.PID.xml" mock file. */
	static final String MOCK_FINDPERSONBYPTCPNTID_RESPONSE = "person.getPersonInfoByPtcpntId.{0}";

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.va.bip.framework.ws.client.remote.RemoteServiceCall#callRemoteService( org.springframework.ws.client.core.
	 * WebServiceTemplate, gov.va.bip.framework.transfer.PartnerTransferObjectMarker, java.lang.Class)
	 */
	@Override
	public PartnerTransferObjectMarker callRemoteService(final WebServiceTemplate webserviceTemplate,
			final PartnerTransferObjectMarker request, final Class<? extends PartnerTransferObjectMarker> requestClass) {

		Defense.notNull(request, "Cannot callRemoteService with null request");
		Defense.notNull(requestClass, "Cannot callRemoteService with null requestClass");

		LOGGER.info("Calling MOCK service with request " + ReflectionToStringBuilder.reflectionToString(request));
		// super handles exceptions
		PartnerTransferObjectMarker response = super.callMockService(webserviceTemplate, request, requestClass);
		LOGGER.info("Called MOCK service with request '" + requestClass.getSimpleName() + "', got mocked response '"
				+ response.getClass() + "'");

		return response;
	}

	/**
	 * This implementation is entirely dependent on the naming conventions used for
	 * mock files in src/main/resources/test/mocks/**<br/>
	 * <b><i>Each implementation could differ.</i></b>
	 * {@inheritDoc}
	 *
	 * @see gov.va.bip.framework.ws.client.remote.AbstractRemoteServiceCallMock#
	 *      getKeyForMockResponse(gov.va.bip.framework.transfer. PartnerTransferObjectMarker)
	 */
	@Override
	protected String getKeyForMockResponse(final PartnerTransferObjectMarker request) {
		Defense.notNull(request, ERROR_NULL_REQUEST);

		String mockFilename = null;

		/*
		 * This block is dependent on the naming conventions used for
		 * mock files in src/main/resources/test/mocks/**
		 */
		if (request.getClass().isAssignableFrom(FindPersonByPtcpntId.class)) {
			final String paramPID = String.valueOf(((FindPersonByPtcpntId) request).getPtcpntId());
			mockFilename = getFileName(MOCK_FINDPERSONBYPTCPNTID_RESPONSE, paramPID);

		} else {
			// no recovery from this
			throw new BipPartnerRuntimeException(MessageKeys.BIP_REMOTE_MOCK_NOT_FOUND, MessageSeverity.WARN,
					HttpStatus.NOT_FOUND, this.getClass().getSimpleName(), request.getClass().getName());
		}

		/*
		 * Return value can never be null or empty,
		 * there is Defense against it in AbstractRemoteServiceCallMock
		 */
		return mockFilename;
	}

	/**
	 * Get filename from a pattern. It is assumed the pattern will always be in the form of <tt>servicename.operation[.{0}]</tt>.
	 * <p>
	 * Determining replaceable params looks for "{" in the fileNamePattern, and requires the security PersonTraits to be populated
	 * correctly.
	 * <p>
	 * If any of these checks fails, the fileNamePattern is returned with the trailing ".{0}" removed.
	 *
	 * @param fileNamePattern - the filename, or if param needs replacing, the pattern
	 * @param paramPID - null or the param replacement value
	 * @return String a filename
	 */
	static String getFileName(final String fileNamePattern, final String paramPID) {
		Defense.notNull(fileNamePattern, "fileNamePattern cannot be null");
		String fileName = fileNamePattern;
		final PersonTraits personTraits = SecurityUtils.getPersonTraits();

		if (StringUtils.isNotBlank(paramPID) && fileName.contains("{")) {
			fileName = MessageFormat.format(fileName, paramPID);
		} else if (personTraits != null && StringUtils.isNotBlank(personTraits.getPid()) && fileName.contains("{")) {
			fileName = MessageFormat.format(fileName, personTraits.getPid());
		} else {
			if (fileName.contains("{")) {
				fileName = fileName.replace(".{0}", "");
				LOGGER.warn("Could not retrieve PersonTraits from SecurityContext. Defaulting to MOCK response " + fileName);
			}
		}
		return fileName;
	}

}
