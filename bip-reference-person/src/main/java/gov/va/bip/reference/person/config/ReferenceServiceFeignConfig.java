package gov.va.bip.reference.person.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import gov.va.bip.framework.feign.autoconfigure.BipFeignAutoConfiguration;
import gov.va.bip.framework.feign.autoconfigure.TokenFeignRequestInterceptor;

@Configuration
public class ReferenceServiceFeignConfig extends BipFeignAutoConfiguration {

	public ReferenceServiceFeignConfig() {
	}

	/**
	 * A bean for internal purposes, the standard (non-feign) REST request
	 * intercepter
	 * 
	 * Adds token header from the originating request to the outgoing request for
	 * internal service calls.
	 *	
	 * @return TokenFeignRequestInterceptor
	 */
	@Bean
	@ConditionalOnMissingBean
	public TokenFeignRequestInterceptor tokenFeignRequestInterceptor() {
		return new TokenFeignRequestInterceptor();
	}
}
