package gov.va.os.reference.starter.service.autoconfigure;


import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import gov.va.os.reference.framework.service.ServiceExceptionHandlerAspect;
import gov.va.os.reference.framework.service.ServiceTimerAspect;
import gov.va.os.reference.framework.service.ServiceValidationToMessageAspect;


/**
 * Created by rthota on 8/24/17.
 */

@Configuration
public class ReferenceServiceAutoConfiguration {

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


