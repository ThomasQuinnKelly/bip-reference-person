package gov.va.ocp.reference.partner.person.ws.client;

import org.springframework.http.HttpStatus;

import gov.va.ocp.framework.exception.OcpPartnerException;
import gov.va.ocp.framework.messages.MessageKey;
import gov.va.ocp.framework.messages.MessageSeverity;

/**
 * This class represents the unique <b>checked</b> exception that can be thrown
 * by the Person webservice client.
 */
public class PersonPartnerCheckedException extends OcpPartnerException {
	private static final long serialVersionUID = -190906250531003842L;

	/**
	 * Constructs a new PersonPartnerCheckedException with the specified detail key, message, severity, and status.
	 * The cause is not initialized, and may subsequently be initialized by a call to {@link #initCause}.
	 *
	 * @see OcpPartnerException#OcpPartnerException(String key, String message, MessageSeverity severity, HttpStatus status)
	 *
	 * @param key - the consumer-facing key that can uniquely identify the nature of the exception
	 * @param severity - the severity of the event: FATAL (500 series), ERROR (400 series), WARN (200 series), or INFO/DEBUG/TRACE
	 * @param status - the HTTP Status code that applies best to the encountered problem, see
	 *            <a href="https://tools.ietf.org/html/rfc7231">https://tools.ietf.org/html/rfc7231</a>
	 * @param params - arguments to fill in any params in the MessageKey message (e.g. value for {0})
	 */
	public PersonPartnerCheckedException(MessageKey key, MessageSeverity severity, HttpStatus status, Object... params) {
		super(key, severity, status, params);
	}

	/**
	 * Constructs a new PersonPartnerCheckedException with the specified detail key, message, severity, status, and cause.
	 *
	 * @see OcpPartnerException#OcpPartnerException(String key, String message, MessageSeverity severity, HttpStatus
	 *      status, Throwable cause)
	 *
	 * @param key - the consumer-facing key that can uniquely identify the nature of the exception
	 * @param severity - the severity of the event: FATAL (500 series), ERROR (400 series), WARN (200 series), or INFO/DEBUG/TRACE
	 * @param status - the HTTP Status code that applies best to the encountered problem, see
	 *            <a href="https://tools.ietf.org/html/rfc7231">https://tools.ietf.org/html/rfc7231</a>
	 * @param cause - the throwable that caused this throwable
	 * @param params - arguments to fill in any params in the MessageKey message (e.g. value for {0})
	 */
	public PersonPartnerCheckedException(MessageKey key, MessageSeverity severity, HttpStatus status, Throwable cause,
			Object... params) {
		super(key, severity, status, cause, params);
	}

}
