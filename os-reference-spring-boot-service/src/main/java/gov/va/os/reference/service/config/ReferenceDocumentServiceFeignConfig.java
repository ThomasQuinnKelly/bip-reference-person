package gov.va.os.reference.service.config;

import org.springframework.context.annotation.Configuration;

import gov.va.os.reference.service.utils.HystrixCommandConstants;
import gov.va.os.reference.starter.feign.autoconfigure.ReferenceFeignAutoConfiguration;

@Configuration
public class ReferenceDocumentServiceFeignConfig  extends ReferenceFeignAutoConfiguration{
	
    public ReferenceDocumentServiceFeignConfig() {
        super.setGroupKey(HystrixCommandConstants.ASCENT_DOCUMENT_SERVICE_GROUP_KEY); 
    }
    
}

