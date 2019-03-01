package gov.va.ocp.reference.starter.audit.autoconfigure;


import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.scheduling.annotation.EnableAsync;

import gov.va.ocp.reference.framework.audit.RequestResponseLogSerializer;


/**
 * Created by rthota on 8/24/17.
 */

@Configuration
@EnableAsync
public class OcpAuditAutoConfiguration {
	
    @Bean
    @ConditionalOnMissingBean
    public RequestResponseLogSerializer requestResponseAsyncLogging() {
        return new RequestResponseLogSerializer();
    }
}


