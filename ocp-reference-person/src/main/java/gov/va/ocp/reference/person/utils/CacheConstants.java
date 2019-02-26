package gov.va.ocp.reference.person.utils;

import gov.va.ocp.reference.service.utils.ApplicationInfo;

/**
 * <p>
 * Cache constants and utilities.
 * </p>
 * <p>
 * Note that service impl methods that are cached should use constants declared in this class for their "value" definitions. For
 * example:<br/>
 * {@code @CachePut(value = CacheConstants.CACHE_NAME_STATES, key = "#root.methodName", unless = ...}
 * </p>
 *
 * @author akulkarni
 */
public class CacheConstants {

	/**
	 * cache name separator
	 */
	private static final String CACHE_NAME_SEPARATOR = "_";

	/**
	 * cache name suffix
	 */
	private static final String CACHE_NAME_SUFFIX =
			CACHE_NAME_SEPARATOR + ApplicationInfo.PROJECT_NAME + CACHE_NAME_SEPARATOR + ApplicationInfo.VERSION;

	public static final String CACHENAME_REFERENCE_PERSON_SERVICE = "demoPersonService" + CACHE_NAME_SUFFIX;

	/**
	 * No instantiation
	 */
	private CacheConstants() {
		throw new UnsupportedOperationException("CacheConstants is a static class. Do not instantiate it.");
	}

}