package gov.va.ocp.vetservices.claims;

import gov.va.ocp.framework.messages.MessageSeverity;

/**
 * Runtime exception for the Claims service.
 *
 * @author aburkholder
 */
public class ClaimsServiceException extends RuntimeException {
	private static final long serialVersionUID = -7439237193647605148L;

	/** The enumeration for message severity */
	private MessageSeverity severity; // NOSONAR cannot be final
	/** The error message key */
	private String key; // NOSONAR cannot be final
	/** the error message */
	private String message; // NOSONAR cannot be final

	/**
	 * Create an exception for the Person service.
	 *
	 * @param severity the MessageSeverity
	 * @param key the property key for the message
	 * @param message the message
	 */
	public ClaimsServiceException(final MessageSeverity severity, final String key, final String message) {
		super(message);
		this.severity = severity;
		this.key = key;
		this.message = message;
	}

	/**
	 * Create an exception for the Person service, without a property key.
	 *
	 * @param severity the MessageSeverity
	 * @param message the message
	 */
	public ClaimsServiceException(final MessageSeverity severity, final String message) {
		super(message);
		this.severity = severity;
		this.message = message;
	}

	/**
	 * Create an exception for the Person service, with default ERROR severity, and without a property key.
	 *
	 * @param message the message
	 */
	public ClaimsServiceException(String message) {
		super(message);
		this.severity = MessageSeverity.ERROR;
		this.message = message;
	}

	/**
	 * Create an exception for the Person service, with cause.
	 *
	 * @param severity the MessageSeverity
	 * @param key the property key for the message
	 * @param message the message
	 * @param cause the exception that caused this one
	 */
	public ClaimsServiceException(final MessageSeverity severity, final String key, final String message, Throwable cause) {
		super(message, cause);
		this.severity = severity;
		this.key = key;
		this.message = message;
	}

	/**
	 * Create an exception for the Person service, with default ERROR severity, and without a property key.
	 *
	 * @param message the message
	 * @param cause the exception that caused this one
	 */
	public ClaimsServiceException(String message, Throwable cause) {
		super(message, cause);
		this.message = message;
	}

	/**
	 * The message severity enumeration.
	 *
	 * @return MessageSeverity
	 */
	public MessageSeverity getSeverity() {
		return severity;
	}

	/**
	 * The message key.
	 *
	 * @return String the key
	 */
	public String getKey() {
		return key;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
