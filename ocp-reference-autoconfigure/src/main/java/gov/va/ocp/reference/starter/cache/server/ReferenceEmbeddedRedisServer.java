package gov.va.ocp.reference.starter.cache.server;

import java.io.IOException;
import java.net.ServerSocket;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.net.ServerSocketFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;

import gov.va.ocp.reference.framework.config.ReferenceCommonSpringProfiles;
import gov.va.ocp.reference.framework.log.ReferenceLogger;
import gov.va.ocp.reference.framework.log.ReferenceLoggerFactory;
import gov.va.ocp.reference.framework.util.Defense;
import gov.va.ocp.reference.starter.cache.autoconfigure.ReferenceCacheProperties;
import redis.embedded.RedisServer;

/**
 * this class will start embedded redis, to be used for local envs. The profile embedded-redis needs to be added in order for this bean
 * to be created
 *
 * @author akulkarni
 */
@Profile(ReferenceCommonSpringProfiles.PROFILE_EMBEDDED_REDIS)
public class ReferenceEmbeddedRedisServer {

	/** The Constant LOGGER. */
	private static final ReferenceLogger LOGGER = ReferenceLoggerFactory.getLogger(ReferenceEmbeddedRedisServer.class);

	/**
	 * Cache Properties Bean
	 */
	@Autowired
	private ReferenceCacheProperties properties;

	/**
	 * Embedded redis server object
	 */
	private RedisServer redisServer;

	public RedisServer getRedisServer() {
		return redisServer;
	}

	/**
	 * Start embedded redis server on context load
	 *
	 * @throws IOException
	 */
	@PostConstruct
	public void startRedis() throws IOException {
		Defense.notNull(properties.getRedisConfig(), "properties.getRedisConfig() is required to run/use Redis!");

		if (properties.getRedisConfig().getPort() == null) {
			ServerSocket ss = null;
			try {
				ss = ServerSocketFactory.getDefault().createServerSocket(0);
				properties.getRedisConfig().setPort(ss.getLocalPort());
			} finally {
				if (ss!=null) {
					ss.close();
				}
			}
		}
		LOGGER.info("Starting Embedded Redis. This embedded redis is only to be used in local enviroments");
		LOGGER.info("Embedded redis starting on port {}", properties.getRedisConfig().getPort());
		try {
			redisServer = RedisServer.builder().port(properties.getRedisConfig().getPort())
					// .redisExecProvider(customRedisExec) //com.github.kstyrc (not com.orange.redis-embedded)
					.setting("maxmemory 128M") // maxheap 128M
					.setting("bind localhost") // force bind to localhost to avoid firewall pop-ups
					.build();
			redisServer.start();
		} catch (final Exception exc) {
			LOGGER.warn("Not able to start embedded redis, most likely it's already running on the given port on this host!", exc);
		}
	}

	/**
	 * stop embedded redis server on context destroy
	 */
	@PreDestroy
	public void stopRedis() {
		LOGGER.info("Shutting Down Embedded Redis", "This embedded redis is only to be used in local enviroments");
		redisServer.stop();
	}

}