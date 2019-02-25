package gov.va.ocp.reference.service;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Adds spring configuration for partner web service clients that are used by this application.
 * <p>
 * Example class level component scan annotation:<br/>
 * <tt>@ComponentScan(basePackages = { "gov.va.ocp.reference.partner.person.ws.client" })</tt>
 *
 * Regarding configuration of Feign clients, please see {@link ReferenceDocumentServiceFeignConfig}.
 */
@Configuration
@ComponentScan(basePackages = { "gov.va.ocp.reference.partner.person.ws.client" }
)
public class PartnerConfig {
	// nothing needed here
}
