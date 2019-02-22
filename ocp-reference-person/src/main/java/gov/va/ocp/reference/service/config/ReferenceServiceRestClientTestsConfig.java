package gov.va.ocp.reference.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import gov.va.ocp.reference.service.rest.client.discovery.PersonUsageDiscoveryClient;

@Configuration
public class ReferenceServiceRestClientTestsConfig {
	
	@Bean
    PersonUsageDiscoveryClient demoUsageDiscoveryClient() {
        return new PersonUsageDiscoveryClient();
    }
	
}