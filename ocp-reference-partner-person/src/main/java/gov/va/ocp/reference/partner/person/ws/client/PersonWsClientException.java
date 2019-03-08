package gov.va.ocp.reference.partner.person.ws.client;

import org.springframework.http.HttpStatus;

import gov.va.ocp.framework.exception.OcpRuntimeException;
import gov.va.ocp.framework.messages.MessageSeverity;

/**
 * This class represents the unique exception that can be thrown
 * by the PersonWsClient.
 *
 */
public class PersonWsClientException extends OcpRuntimeException {
	private static final long serialVersionUID = -6607671918333549316L;

	/**
	 * Constructs a new PersonWsClientException with the specified detail key, message, severity, and status.
	 * The cause is not initialized, and may subsequently be initialized by a call to {@link #initCause}.
	 *
	 * @see OcpRuntimeException#OcpRuntimeException(String key, String message, MessageSeverity severity, HttpStatus status)
	 *
	 * @param key - the consumer-facing key that can uniquely identify the nature of the exception
	 * @param message - the detail message
	 * @param severity - the severity of the event: FATAL (500 series), ERROR (400 series), WARN (200 series), or INFO/DEBUG/TRACE
	 * @param status - the HTTP Status code that applies best to the encountered problem, see
	 *            <a href="https://tools.ietf.org/html/rfc7231">https://tools.ietf.org/html/rfc7231</a>
	 */
	public PersonWsClientException(String key, String message, MessageSeverity severity, HttpStatus status) {
		super(key, message, severity, status);
	}

	/**
	 * Constructs a new PersonWsClientException with the specified detail key, message, severity, status, and cause.
	 *
	 * @see OcpRuntimeException#OcpRuntimeException(String key, String message, MessageSeverity severity, HttpStatus status, Throwable
	 *      cause)
	 *
	 * @param key - the consumer-facing key that can uniquely identify the nature of the exception
	 * @param message - the detail message
	 * @param severity - the severity of the event: FATAL (500 series), ERROR (400 series), WARN (200 series), or INFO/DEBUG/TRACE
	 * @param status - the HTTP Status code that applies best to the encountered problem, see
	 *            <a href="https://tools.ietf.org/html/rfc7231">https://tools.ietf.org/html/rfc7231</a>
	 * @param cause - the throwable that caused this throwable
	 */
	public PersonWsClientException(String key, String message, MessageSeverity severity, HttpStatus status, Throwable cause) {
		super(key, message, severity, status, cause);
	}
}
