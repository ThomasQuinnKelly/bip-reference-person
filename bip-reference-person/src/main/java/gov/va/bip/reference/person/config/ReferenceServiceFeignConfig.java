package gov.va.bip.reference.person.config;

import org.springframework.context.annotation.Configuration;

import gov.va.bip.framework.feign.autoconfigure.BipFeignAutoConfiguration;
import gov.va.bip.reference.person.utils.HystrixCommandConstants;

@Configuration
public class ReferenceServiceFeignConfig extends BipFeignAutoConfiguration {

	public ReferenceServiceFeignConfig() {
		super.setGroupKey(HystrixCommandConstants.REFERENCE_PERSON_SERVICE_GROUP_KEY);
	}
}
