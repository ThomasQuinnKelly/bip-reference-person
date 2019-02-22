package gov.va.ocp.reference.service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import gov.va.ocp.reference.partner.person.ws.client.PersonWsClientConfig;

@Configuration
@Import({ReferenceServiceRestClientTestsConfig.class, PersonWsClientConfig.class})
public class ReferenceServiceConfig {

}