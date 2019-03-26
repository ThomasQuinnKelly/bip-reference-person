package gov.va.ocp.vetservices.claims.exception;

import org.springframework.http.HttpStatus;

import gov.va.ocp.framework.exception.OcpRuntimeException;
import gov.va.ocp.framework.messages.MessageSeverity;

/**
 * Runtime exception for the Claims service.
 *
 * @author rajuthota
 */
public class ClaimsServiceException extends OcpRuntimeException {
	private static final long serialVersionUID = -7439237193647605148L;

	/**
	 * Constructs a new RuntimeException for the Claimns service with the specified detail key, message, severity, and status.
	 * The cause is not initialized.
	 *
	 * @see OcpRuntimeException#OcpRuntimeException(String, String, MessageSeverity, HttpStatus)
	 *
	 * @param key - the consumer-facing key that can uniquely identify the nature of the exception
	 * @param message - the detail message
	 * @param severity - the severity of the event: FATAL (500 series), ERROR (400 series), WARN (200 series), or INFO/DEBUG/TRACE
	 * @param status - the HTTP Status code that applies best to the encountered problem, see
	 *            <a href="https://tools.ietf.org/html/rfc7231">https://tools.ietf.org/html/rfc7231</a>
	 */
	public ClaimsServiceException(final String key, final String message, final MessageSeverity severity, HttpStatus status) {
		super(key, message, severity, status);
	}

	/**
	 * Constructs a new RuntimeException with the specified detail key, message, severity, status, and cause.
	 *
	 * @see RuntimeException#RuntimeException(String, Throwable)
	 *
	 * @param key - the consumer-facing key that can uniquely identify the nature of the exception
	 * @param message - the detail message
	 * @param severity - the severity of the event: FATAL (500 series), ERROR (400 series), WARN (200 series), or INFO/DEBUG/TRACE
	 * @param status - the HTTP Status code that applies best to the encountered problem, see
	 *            <a href="https://tools.ietf.org/html/rfc7231">https://tools.ietf.org/html/rfc7231</a>
	 * @param cause - the throwable that caused this throwable
	 */
	public ClaimsServiceException(String key, String message, MessageSeverity severity, HttpStatus status, Throwable cause) {
		super(key, message, severity, status, cause);
	}
}
