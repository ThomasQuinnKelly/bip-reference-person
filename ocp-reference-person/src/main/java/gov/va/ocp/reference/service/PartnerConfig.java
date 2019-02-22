package gov.va.ocp.reference.service;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import gov.va.ocp.reference.service.config.ReferenceDocumentServiceFeignConfig;

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
//		, excludeFilters = {
//				@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = ReferenceDocumentServiceFeignConfig.class)
//		}
)
public class PartnerConfig {
	// nothing needed here
}
