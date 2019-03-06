package gov.va.ocp.reference.person.exception;

import gov.va.ocp.framework.messages.MessageSeverity;

/**
 * Runtime exception for the Person service.
 *
 * @author aburkholder
 */
public class PersonServiceException extends RuntimeException {
	private static final long serialVersionUID = -5224277215368080694L;

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
	public PersonServiceException(final MessageSeverity severity, final String key, final String message) {
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
	public PersonServiceException(final MessageSeverity severity, final String message) {
		super(message);
		this.severity = severity;
		this.message = message;
	}

	/**
	 * Create an exception for the Person service, with default ERROR severity, and without a property key.
	 *
	 * @param message the message
	 */
	public PersonServiceException(String message) {
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
	public PersonServiceException(final MessageSeverity severity, final String key, final String message, Throwable cause) {
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
	public PersonServiceException(String message, Throwable cause) {
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
