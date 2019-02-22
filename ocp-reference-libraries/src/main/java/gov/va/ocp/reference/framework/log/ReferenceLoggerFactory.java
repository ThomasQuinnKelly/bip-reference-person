package gov.va.ocp.reference.framework.log;

import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;

import gov.va.ocp.reference.framework.exception.ReferenceRuntimeException;

/**
 * This class wraps the SLF4J logger to add logging enhancements for the platform.
 * <p>
 * If a future upgrade of SLF4J changes the Logger interface, changes will be required in the ReferenceLogger class.
 *
 * @author aburkholder
 */
public final class ReferenceLoggerFactory {

	/**
	 * Do not instantiate.
	 */
	private ReferenceLoggerFactory() {
		throw new ReferenceRuntimeException("ReferenceLoggerFactory is a static class. Do not instantiate it.");
	}

	/**
	 * Gets a SLF4J-compliant logger, enhanced for applications, for the specified class.
	 *
	 * @param clazz the Class for which logging is desired
	 * @return ReferenceLogger
	 * @see org.slf4j.LoggerFactory#getLogger(Class)
	 */
	public static final ReferenceLogger getLogger(Class<?> clazz) {
		return ReferenceLogger.getLogger(LoggerFactory.getLogger(clazz));
	}

	/**
	 * Gets a SLF4J-compliant logger, enhanced for applications, for the specified name.
	 *
	 * @param name the name under which logging is desired
	 * @return ReferenceLogger
	 * @see org.slf4j.LoggerFactory#getLogger(String)
	 */
	public static final ReferenceLogger getLogger(String name) {
		return ReferenceLogger.getLogger(LoggerFactory.getLogger(name));
	}

	/**
	 * Get the implementation of the logger factory that is bound to SLF4J, that serves as the basis for ReferenceLoggerFactory.
	 *
	 * @return ILoggerFactory an instance of the bound factory implementation
	 * @see org.slf4j.LoggerFactory#getILoggerFactory()
	 */
	public static final ILoggerFactory getBoundFactory() {
		return LoggerFactory.getILoggerFactory();
	}

}
