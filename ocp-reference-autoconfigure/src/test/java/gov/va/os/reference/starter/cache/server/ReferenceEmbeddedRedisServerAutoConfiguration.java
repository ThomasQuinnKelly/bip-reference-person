package gov.va.os.reference.starter.cache.server;

import java.util.ArrayList;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import gov.va.ocp.reference.starter.cache.autoconfigure.ReferenceCacheProperties;
import gov.va.ocp.reference.starter.cache.autoconfigure.ReferenceCacheProperties.RedisConfig;
import gov.va.ocp.reference.starter.cache.server.ReferenceEmbeddedRedisServer;

/**
 * 
 * @author rthota
 *
 */
@Configuration
public class ReferenceEmbeddedRedisServerAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public ReferenceEmbeddedRedisServer ascentEmbeddedRedisServer() {
		return new ReferenceEmbeddedRedisServer();
	}

	@Bean
	@ConditionalOnMissingBean
	public ReferenceCacheProperties ascentCacheProperties() {
		ReferenceCacheProperties ascentCacheProperties = new ReferenceCacheProperties();
		ascentCacheProperties.setRedisConfig(new RedisConfig());
		ascentCacheProperties.getRedisConfig().setHost("localhost");
		// ascentCacheProperties.getRedisConfig().setPort(6379);
		ascentCacheProperties.setExpires(new ArrayList<>());
		ascentCacheProperties.setDefaultExpires(500L);
		return ascentCacheProperties;
	}
}