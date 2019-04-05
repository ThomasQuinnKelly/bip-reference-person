package gov.va.bip.vetservices.claims.config;

import org.springframework.context.annotation.Configuration;

import gov.va.bip.vetservices.claims.utils.HystrixCommandConstants;
import gov.va.bip.framework.feign.autoconfigure.BipFeignAutoConfiguration;;

@Configuration
public class VetservicesClaimsFeignConfig extends BipFeignAutoConfiguration {

    public VetservicesClaimsFeignConfig() {
    		super.setGroupKey(HystrixCommandConstants.VETSERVICES_CLAIMS_SERVICE_GROUP_KEY); 
    }
}
