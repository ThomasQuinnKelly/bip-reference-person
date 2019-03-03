package gov.va.ocp.reference.framework.exception;

import gov.va.ocp.reference.framework.service.DomainResponse;

/**
 * Custom extension of RuntimeException so that we can raise this for exceptions we have no intention
 * of handling and need to raise but for some reason cannot raise
 * java's RuntimeException or allow the original exception to simply propagate.
 *
 * @author jshrader
 */
public class OcpFeignRuntimeException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2598842813684506356L;

	/** Server name exception occurred on */
	public static final String SERVER_NAME = System.getProperty("server.name");
	
	private DomainResponse domainResponse;
	
	public DomainResponse getDomainResponse() {
		return domainResponse;
	}

	public void setDomainResponse(DomainResponse domainResponse) {
		this.domainResponse = domainResponse;
	}

	
	public OcpFeignRuntimeException(DomainResponse domainResponse) {
		super();
		this.domainResponse = domainResponse;
	}

	/**
	 * Instantiates a new exception.
	 */
	public OcpFeignRuntimeException() {
		super();
	}

	/**
	 * Instantiates a new service exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public OcpFeignRuntimeException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new service exception.
	 *
	 * @param message the message
	 */
	public OcpFeignRuntimeException(final String message) {
		super(message);
	}

	/**
	 * Instantiates a new service exception.
	 *
	 * @param cause the cause
	 */
	public OcpFeignRuntimeException(final Throwable cause) {
		super(cause);
	}

	/**
	 * Gets the server name.
	 *
	 * @return the server name
	 */
	public final String getServerName() {
		return SERVER_NAME;
	}
}
