package gov.va.bip.reference.person.utils;

/**
 * The Class HystrixCommandConstants.
 */
public final class HystrixCommandConstants {

	/** Reference Person Service Thread Pool Group. */
	public static final String REFERENCE_PERSON_SERVICE_GROUP_KEY = "ReferencePersonServiceGroup";

	/**
	 * Do not instantiate
	 */
	private HystrixCommandConstants() {
		throw new UnsupportedOperationException("HystrixCommandConstants is a static class. Do not instantiate it.");
	}
}