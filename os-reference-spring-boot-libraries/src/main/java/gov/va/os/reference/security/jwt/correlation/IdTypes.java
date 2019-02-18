package gov.va.os.reference.security.jwt.correlation;

import gov.va.os.reference.framework.exception.ReferenceRuntimeException;
import gov.va.os.reference.framework.log.ReferenceLogger;
import gov.va.os.reference.framework.log.ReferenceLoggerFactory;

public enum IdTypes {
	/** The type of correlation id for national authorities */
	NATIONAL("NI"),
	/** The type of correlation id for patient authorities */
	PATIENT("PI"),
	/** The pnidType (currently the only one) associated with the PNID field value */
	SOCIAL("SS");

	private static final ReferenceLogger LOGGER = ReferenceLoggerFactory.getLogger(IdTypes.class);

	/** The arbitrary string value of the enumeration */
	private String type;

	/**
	 * Private constructor for enum initialization
	 *
	 * @param type String
	 */
	private IdTypes(String type) {
		this.type = type;
	}

	/**
	 * The arbitrary String value assigned to the enumeration.
	 *
	 * @return String
	 */
	public String value() {
		return this.type;
	}

	/**
	 * Get the enumeration for the associated arbitrary String value.
	 * Throws a runtime exception if the string value does not match one of the enumeration values.
	 *
	 * @param stringValue the string value
	 * @return IdTypes - the enumeration
	 * @throws ReferenceRuntimeException if no match of enumeration values
	 */
	public static IdTypes fromValue(final String stringValue) {
		for (IdTypes s : IdTypes.values()) {
			if (s.value().equals(stringValue)) {
				return s;
			}
		}
		String msg = "IdType {} does not exist: " + stringValue;
		LOGGER.error(msg);
		throw new ReferenceRuntimeException(msg);
	}
}
