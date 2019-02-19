package gov.va.os.reference.service.config;

import org.springframework.context.annotation.Configuration;

import gov.va.os.reference.service.utils.HystrixCommandConstants;
import gov.va.os.reference.starter.feign.autoconfigure.ReferenceFeignAutoConfiguration;;

@Configuration
public class ReferenceServiceFeignConfig extends ReferenceFeignAutoConfiguration{

    public ReferenceServiceFeignConfig() {
    		super.setGroupKey(HystrixCommandConstants.ASCENT_DEMO_SERVICE_GROUP_KEY); 
    }	
    
}
