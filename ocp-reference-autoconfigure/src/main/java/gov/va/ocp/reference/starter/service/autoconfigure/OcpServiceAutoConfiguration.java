package gov.va.ocp.reference.starter.service.autoconfigure;


import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import gov.va.ocp.reference.framework.service.aspect.ServiceExceptionHandlerAspect;
import gov.va.ocp.reference.framework.service.aspect.ServiceTimerAspect;
import gov.va.ocp.reference.framework.service.aspect.ServiceValidationToMessageAspect;


/**
 * Created by rthota on 8/24/17.
 */

@Configuration
public class OcpServiceAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ServiceExceptionHandlerAspect serviceExceptionHandlerAspect(){
        return new ServiceExceptionHandlerAspect();
    }
    
    @Bean
    @ConditionalOnMissingBean
    public ServiceTimerAspect serviceTimerAspect(){
        return new ServiceTimerAspect();
    }    
    
    @Bean
    @ConditionalOnMissingBean
    public ServiceValidationToMessageAspect serviceValidationToMessageAspect(){
        return new ServiceValidationToMessageAspect();
    }   

}


