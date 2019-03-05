package gov.va.ocp.reference.person.config;

import org.springframework.context.annotation.Configuration;

import gov.va.ocp.framework.feign.autoconfigure.OcpFeignAutoConfiguration;
import gov.va.ocp.reference.person.utils.HystrixCommandConstants;;

@Configuration
public class ReferenceServiceFeignConfig extends OcpFeignAutoConfiguration {

    public ReferenceServiceFeignConfig() {
    		super.setGroupKey(HystrixCommandConstants.REFERENCE_PERSON_SERVICE_GROUP_KEY); 
    }
}
