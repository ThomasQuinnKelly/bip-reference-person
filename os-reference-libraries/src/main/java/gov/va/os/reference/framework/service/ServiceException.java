package gov.va.os.reference.framework.service;

import gov.va.os.reference.framework.exception.ReferenceRuntimeException;

/**
 * Root hierarchy of exceptions which indicates there was an exception/error in the Service tier
 *
 * @see gov.va.os.reference.framework.exception.ReferenceRuntimeException
 * @author jshrader
 */
public class ServiceException extends ReferenceRuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6590361959617339905L;

	/**
	 * Instantiates a new exception.
	 */
	public ServiceException() {
		super();
	}

	/**
	 * Instantiates a new exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public ServiceException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new exception.
	 *
	 * @param message the message
	 */
	public ServiceException(final String message) {
		super(message);
	}

	/**
	 * Instantiates a new exception.
	 *
	 * @param cause the cause
	 */
	public ServiceException(final Throwable cause) {
		super(cause);
	}
}
