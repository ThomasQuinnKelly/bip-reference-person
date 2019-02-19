package gov.va.os.reference.starter.audit.autoconfigure;


import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.scheduling.annotation.EnableAsync;

import gov.va.os.reference.framework.audit.RequestResponseLogSerializer;


/**
 * Created by rthota on 8/24/17.
 */

@Configuration
@EnableAsync
public class ReferenceAuditAutoConfiguration {
	
    @Bean
    @ConditionalOnMissingBean
    public RequestResponseLogSerializer requestResponseAsyncLogging() {
        return new RequestResponseLogSerializer();
    }
}


