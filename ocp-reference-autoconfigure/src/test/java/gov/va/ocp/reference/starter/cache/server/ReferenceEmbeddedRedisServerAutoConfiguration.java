package gov.va.ocp.reference.starter.cache.server;

import java.util.ArrayList;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import gov.va.ocp.reference.starter.cache.autoconfigure.OcpCacheProperties;
import gov.va.ocp.reference.starter.cache.autoconfigure.OcpCacheProperties.RedisConfig;
import gov.va.ocp.reference.starter.cache.server.OcpEmbeddedRedisServer;

/**
 * 
 * @author rthota
 *
 */
@Configuration
public class ReferenceEmbeddedRedisServerAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public OcpEmbeddedRedisServer ocpEmbeddedRedisServer() {
		return new OcpEmbeddedRedisServer();
	}

	@Bean
	@ConditionalOnMissingBean
	public OcpCacheProperties ocpCacheProperties() {
		OcpCacheProperties ocpCacheProperties = new OcpCacheProperties();
		ocpCacheProperties.setRedisConfig(new RedisConfig());
		ocpCacheProperties.getRedisConfig().setHost("localhost");
		ocpCacheProperties.setExpires(new ArrayList<>());
		ocpCacheProperties.setDefaultExpires(500L);
		return ocpCacheProperties;
	}
}