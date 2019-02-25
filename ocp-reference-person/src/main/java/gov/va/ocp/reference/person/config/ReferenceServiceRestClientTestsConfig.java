package gov.va.ocp.reference.person.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import gov.va.ocp.reference.person.rest.client.discovery.PersonUsageDiscoveryClient;

@Configuration
public class ReferenceServiceRestClientTestsConfig {
	
	@Bean
    PersonUsageDiscoveryClient personUsageDiscoveryClient() {
        return new PersonUsageDiscoveryClient();
    }
	
}