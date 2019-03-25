package gov.va.ocp.reference.person.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { "gov.va.ocp.framework.audit" },
		excludeFilters = @Filter(Configuration.class))
public class ReferencePersonConfig {

}