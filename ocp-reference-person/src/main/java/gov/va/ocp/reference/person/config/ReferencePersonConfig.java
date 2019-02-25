package gov.va.ocp.reference.person.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import gov.va.ocp.reference.partner.person.ws.client.PersonWsClientConfig;

@Configuration
@Import({ReferenceServiceRestClientTestsConfig.class, PersonWsClientConfig.class})
public class ReferencePersonConfig {

}