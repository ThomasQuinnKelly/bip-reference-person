package gov.va.os.reference.service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import gov.va.os.reference.partner.person.ws.client.PersonWsClientConfig;

@Configuration
@Import({ReferenceServiceRestClientTestsConfig.class, PersonWsClientConfig.class})
public class ReferenceServiceConfig {

}