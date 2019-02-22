package gov.va.ocp.reference.service.config;

import org.springframework.context.annotation.Configuration;

import gov.va.ocp.reference.service.utils.HystrixCommandConstants;
import gov.va.ocp.reference.starter.feign.autoconfigure.ReferenceFeignAutoConfiguration;;

@Configuration
public class ReferenceServiceFeignConfig extends ReferenceFeignAutoConfiguration{

    public ReferenceServiceFeignConfig() {
    		super.setGroupKey(HystrixCommandConstants.REFERENCE_DEMO_SERVICE_GROUP_KEY); 
    }	
    
}
