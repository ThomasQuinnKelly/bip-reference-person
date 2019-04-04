package gov.va.ocp.vetservices.claims.exception;

import org.springframework.http.HttpStatus;

import gov.va.ocp.framework.exception.OcpRuntimeException;
import gov.va.ocp.framework.messages.MessageKey;
import gov.va.ocp.framework.messages.MessageSeverity;

/**
 * Runtime exception for the Claims service.
 *
 * @author rajuthota
 */
public class ClaimsServiceException extends OcpRuntimeException {
	private static final long serialVersionUID = -7439237193647605148L;

	/**
	 * Constructs a new RuntimeException for the Claims service with the specified detail key, message, severity, and status.
	 * The cause is not initialized.
	 *
	 * @see OcpRuntimeException#OcpRuntimeException(String, String, MessageSeverity, HttpStatus)
	 *
	 * @param key - the consumer-facing key that can uniquely identify the nature of the exception
	 * @param severity - the severity of the event: FATAL (500 series), ERROR (400 series), WARN (200 series), or INFO/DEBUG/TRACE
	 * @param status - the HTTP Status code that applies best to the encountered problem, see
	 *            <a href="https://tools.ietf.org/html/rfc7231">https://tools.ietf.org/html/rfc7231</a>
	 * @param params - arguments to fill in any params in the MessageKey message (e.g. value for {0})
	 */
	public ClaimsServiceException(final MessageKey key, final MessageSeverity severity, HttpStatus status, Object... params) {
		super(key, severity, status, params);
	}

	/**
	 * Constructs a new RuntimeException with the specified detail key, message, severity, status, and cause.
	 *
	 * @see RuntimeException#RuntimeException(String, Throwable)
	 *
	 * @param key - the consumer-facing key that can uniquely identify the nature of the exception
	 * @param severity - the severity of the event: FATAL (500 series), ERROR (400 series), WARN (200 series), or INFO/DEBUG/TRACE
	 * @param status - the HTTP Status code that applies best to the encountered problem, see
	 *            <a href="https://tools.ietf.org/html/rfc7231">https://tools.ietf.org/html/rfc7231</a>
	 * @param cause - the throwable that caused this throwable
	 * @param params - arguments to fill in any params in the MessageKey message (e.g. value for {0})
	 */
	public ClaimsServiceException(MessageKey key, MessageSeverity severity, HttpStatus status, Throwable cause, Object... params) {
		super(key, severity, status, cause, params);
	}
}
