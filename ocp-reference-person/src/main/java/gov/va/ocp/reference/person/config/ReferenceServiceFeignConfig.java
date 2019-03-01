package gov.va.ocp.reference.person.config;

import org.springframework.context.annotation.Configuration;

import gov.va.ocp.reference.person.utils.HystrixCommandConstants;
import gov.va.ocp.reference.starter.feign.autoconfigure.OcpFeignAutoConfiguration;;

@Configuration
public class ReferenceServiceFeignConfig extends OcpFeignAutoConfiguration {

    public ReferenceServiceFeignConfig() {
    		super.setGroupKey(HystrixCommandConstants.REFERENCE_PERSON_SERVICE_GROUP_KEY); 
    }
}
