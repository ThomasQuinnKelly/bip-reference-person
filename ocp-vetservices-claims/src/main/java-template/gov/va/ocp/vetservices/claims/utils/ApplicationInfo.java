package gov.va.ocp.vetservices.claims.utils;

/**
 * Meta-information about the web application.<br/>
 * <br/>
 * 
 * Class created to have different <b>cache buckets based on version</b>. If data persists in the cache through an app upgrade, app may
 * no longer be able to parse the data in cache or instances that are yet to be upgraded during a rolling deployment. This might have
 * issues with new object formats getting stored in the cache.<br/>
 * <br/>
 * 
 * Class must be placed in the directory as configured via maven build plugin "templating-maven-plugin". See pom.xml file for usage
 * 
 *
 * @author Abhijit Kulkarni
 * @see pom.xml
 */
public final class ApplicationInfo {
	/**
	 * cache name separator
	 */
	String CACHE_NAME_SEPARATOR = "_";
	// project.* properties are set by goal of filter-sources
	// templating-maven-plugin.
	public static final String VERSION = "${project.version}";
	/** name of the application. */
	public static final String PROJECT_NAME = "${project.name}";

	/** Prevents outside instantiation. */
	private ApplicationInfo() {
		// Deliberately empty.
	}
}