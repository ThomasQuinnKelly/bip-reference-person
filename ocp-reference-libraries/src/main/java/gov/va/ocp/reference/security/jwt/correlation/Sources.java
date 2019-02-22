package gov.va.ocp.reference.security.jwt.correlation;

import gov.va.ocp.reference.framework.exception.ReferenceRuntimeException;
import gov.va.ocp.reference.framework.log.ReferenceLogger;
import gov.va.ocp.reference.framework.log.ReferenceLoggerFactory;

public enum Sources {
	/** The ICN assigning facility (VHA) */
	ICN("200M"),
	/** The DOD assigning facility */
	USDOD("200DOD"),
	/** The BiRLS assigning facility (VBA) */
	BIRLS("200BRLS"),
	/** The CORP facility */
	CORP("200CORP");

	private static final ReferenceLogger LOGGER = ReferenceLoggerFactory.getLogger(Sources.class);

	/** The arbitrary string value of the enumeration */
	private String source;

	/**
	 * Private constructor for enum initialization
	 *
	 * @param source String
	 */
	private Sources(String source) {
		this.source = source;
	}

	/**
	 * The arbitrary String value assigned to the enumeration.
	 *
	 * @return String
	 */
	public String value() {
		return this.source;
	}

	/**
	 * Get the enumeration for the associated arbitrary String value.
	 * Throws a runtime exception if the string value does not match one of the enumeration values.
	 *
	 * @param stringValue the string value
	 * @return Sources - the enumeration
	 * @throws ReferenceRuntimeException if no match of enumeration values
	 */
	public static Sources fromValue(final String stringValue) {
		for (Sources s : Sources.values()) {
			if (s.value().equals(stringValue)) {
				return s;
			}
		}
		String msg = "Source {} does not exist: " + stringValue;
		LOGGER.error(msg);
		throw new ReferenceRuntimeException(msg);
	}
}
