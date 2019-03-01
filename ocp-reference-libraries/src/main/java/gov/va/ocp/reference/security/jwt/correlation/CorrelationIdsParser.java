package gov.va.ocp.reference.security.jwt.correlation;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import gov.va.ocp.reference.framework.exception.OcpRuntimeException;
import gov.va.ocp.reference.framework.log.OcpLogger;
import gov.va.ocp.reference.framework.log.OcpLoggerFactory;
import gov.va.ocp.reference.framework.security.PersonTraits;

/**
 * Parse values found in an array of Correlation IDs onto the PersonTraits object by way of a Map of Strings.
 */
public class CorrelationIdsParser {
	private static final OcpLogger LOGGER = OcpLoggerFactory.getLogger(CorrelationIdsParser.class);

	/** The count of elements in the CorrelationIds array that indicates it does not contain SS */
	private static final int ELEMENT_MAX_COUNT = 5;

	/** The count of elements in the CorrelationIds array that indicates it is only SS */
	private static final int ELEMENT_SS_COUNT = 2;

	/** The index into a single CorrelationId to get the identifier number */
	private static final int INDEX_ID = 0;
	/** The index into a single CorrelationId to get the Type */
	private static final int INDEX_TYPE = 1;
	/** The index into a single Correlation Id to get the assigning facility */
	private static final int INDEX_SOURCE = 2;
	/** The index into a single Correlation Id to get the assigning authority */
	private static final int INDEX_ISSUER = 3;
	/** The index into a single Correlation Id to get the user status */
	private static final int INDEX_STATUS = 4;

	/**
	 * This class is not meant to be instantiated since it all it has are utility methods for parsing correlation Ids
	 */
	private CorrelationIdsParser() {
		throw new IllegalStateException("Utility class for parsing correlation Ids");
	}

	/**
	 * Unpack a list of correlation IDs onto the provided personTraits.
	 *
	 * @param list - of correlation id srings
	 * @param personTraits - the object to update with IDs from the list of correlation ids
	 * @throws OcpRuntimeException if some problem with the correlation ids
	 */
	public static void parseCorrelationIds(final List<String> list, final PersonTraits personTraits) {
		if ((list != null) && !list.isEmpty()) {
			for (final String token : list) {
				processToken(token, personTraits);
			}
		}
	}

	/**
	 * Process the token and populate the map with values.
	 *
	 * @param tokenId
	 * @throws OcpRuntimeException if some problem with the correlation ids
	 */
	private static void processToken(final String token, final PersonTraits personTraits) {
		// split a single correlation id into its component parts
		if (StringUtils.isBlank(token)) {
			String msg = "Cannot process blank correlation id";
			LOGGER.error(msg);
			throw new OcpRuntimeException(msg);
		}
		final String[] tokens = token.split("\\^");

		if (tokens.length >= ELEMENT_MAX_COUNT) {
			final String elementId = tokens[INDEX_ID];
			final String type = tokens[INDEX_TYPE];
			final String assigningFacility = tokens[INDEX_SOURCE];
			final String assigningAuthority = tokens[INDEX_ISSUER];
			// this line confirms that the correlation id contains a valid user status
			UserStatus.fromValue(tokens[INDEX_STATUS]);

			determinePidAndFileNumber(personTraits, elementId, type, assigningAuthority, assigningFacility);
			determineEdipiAndIcn(personTraits, elementId, type, assigningAuthority, assigningFacility);
			determinePnIDAndPnIdType(personTraits, elementId, type, assigningAuthority);

		} else if (tokens.length == ELEMENT_SS_COUNT) {
			personTraits.setPnidType(IdTypes.SOCIAL.value());
			personTraits.setPnid(tokens[INDEX_ID]);

		} else {
			String msg = "Invalid number of elements {} in correlation id {}, should be " + ELEMENT_MAX_COUNT
					+ " or " + ELEMENT_SS_COUNT + tokens.length + ", " + token;
			LOGGER.error(msg);
			throw new OcpRuntimeException(msg);
		}
	}

	/**
	 *
	 * @param elementId
	 * @param type
	 * @param assigningAuthority
	 * @param assigningFacility
	 */
	private static void determinePidAndFileNumber(final PersonTraits personTraits, final String elementId, final String type,
			final String assigningAuthority, final String assigningFacility) {
		if (type.equals(IdTypes.PATIENT.value()) && assigningAuthority.equals(Issuers.USVBA.value())) {
			if (assigningFacility.equals(Sources.CORP.value())) {
				personTraits.setPid(elementId);
			} else if (assigningFacility.equals(Sources.BIRLS.value())) {
				personTraits.setFileNumber(elementId);
			}
		}
	}

	/**
	 *
	 * @param elementId
	 * @param type
	 * @param assigningAuthority
	 * @param assigningFacility
	 */
	private static void determineEdipiAndIcn(final PersonTraits personTraits, final String elementId, final String type,
			final String assigningAuthority, final String assigningFacility) {
		if (type.equals(IdTypes.NATIONAL.value())) {
			if (assigningFacility.equals(Sources.USDOD.value()) && assigningAuthority.equals(Issuers.USDOD.value())) {
				personTraits.setDodedipnid(elementId);
			}
			if (assigningFacility.equals(Sources.ICN.value()) && assigningAuthority.equals(Issuers.USVHA.value())) {
				personTraits.setIcn(elementId);
			}
		}
	}

	/**
	 *
	 * @param elementId
	 * @param type
	 * @param assigningAuthority
	 */
	private static void determinePnIDAndPnIdType(final PersonTraits personTraits, final String elementId, final String type,
			final String assigningAuthority) {
		if (type.equals(IdTypes.SOCIAL.value()) && assigningAuthority.equals(Issuers.USVBA.value())) {
			personTraits.setPnid(elementId);
			personTraits.setPnidType(type);
		}
	}
}