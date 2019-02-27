package gov.va.ocp.reference.partner.person.ws.client;

import gov.va.ocp.reference.framework.exception.ReferenceRuntimeException;

/**
 * This class represents the unique exception that can be thrown
 * by the PersonWsClient.
 *
 */
public class PersonWsClientException extends ReferenceRuntimeException {

	/** The serialVersionUID */
	private static final long serialVersionUID = 406968015972994683L;

	/**
	 * Instantiates a new exception.
	 */
	public PersonWsClientException() {
		super();
	}

	/**
	 * Instantiates a new exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public PersonWsClientException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new exception.
	 *
	 * @param message the message
	 */
	public PersonWsClientException(final String message) {
		super(message);
	}

	/**
	 * Instantiates a new exception.
	 *
	 * @param cause the cause
	 */
	public PersonWsClientException(final Throwable cause) {
		super(cause);
	}
}