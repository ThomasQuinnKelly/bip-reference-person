package gov.va.ocp.vetservices.claims.config;

import org.springframework.context.annotation.Configuration;

import gov.va.ocp.framework.feign.autoconfigure.OcpFeignAutoConfiguration;
import gov.va.ocp.vetservices.claims.utils.HystrixCommandConstants;;

@Configuration
public class VetservicesClaimsFeignConfig extends OcpFeignAutoConfiguration {

    public VetservicesClaimsFeignConfig() {
    		super.setGroupKey(HystrixCommandConstants.VETSERVICES_CLAIMS_SERVICE_GROUP_KEY); 
    }
}
