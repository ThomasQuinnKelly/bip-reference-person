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
	public ReferenceEmbeddedRedisServer referenceEmbeddedRedisServer() {
		return new ReferenceEmbeddedRedisServer();
	}

	@Bean
	@ConditionalOnMissingBean
	public ReferenceCacheProperties referenceCacheProperties() {
		ReferenceCacheProperties referenceCacheProperties = new ReferenceCacheProperties();
		referenceCacheProperties.setRedisConfig(new RedisConfig());
		referenceCacheProperties.getRedisConfig().setHost("localhost");
		referenceCacheProperties.setExpires(new ArrayList<>());
		referenceCacheProperties.setDefaultExpires(500L);
		return referenceCacheProperties;
	}
}