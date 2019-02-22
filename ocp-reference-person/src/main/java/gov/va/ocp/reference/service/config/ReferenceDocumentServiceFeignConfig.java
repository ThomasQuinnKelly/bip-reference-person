package gov.va.ocp.reference.service.config;

import gov.va.ocp.reference.service.utils.HystrixCommandConstants;
import gov.va.ocp.reference.starter.feign.autoconfigure.ReferenceFeignAutoConfiguration;

/**
 * Configuration of Feign clients in this project.
 * <p>
 * Read <a href=
 * "https://cloud.spring.io/spring-cloud-netflix/multi/multi_spring-cloud-feign.html#spring-cloud-feign-overriding-defaults">https://cloud.spring.io/spring-cloud-netflix/multi/multi_spring-cloud-feign.html#spring-cloud-feign-overriding-defaults</a>
 * <p>
 * If this class is marked as @Configuration, then it should explicitly be excluded from @ComponentScan<br/>
 * <b>OR</b><br/>
 * Do not mark this class with @Configuration, and don't exclude it from @ComponentScan
 */
//@Configuration
public class ReferenceDocumentServiceFeignConfig extends ReferenceFeignAutoConfiguration {

	/**
	 * Additional configuration for Feign clients.
	 */
	public ReferenceDocumentServiceFeignConfig() {
		super.setGroupKey(HystrixCommandConstants.REFERENCE_DOCUMENT_SERVICE_GROUP_KEY);
	}

}
