package gov.va.os.reference.starter.cache.server;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import gov.va.ocp.reference.starter.cache.server.ReferenceEmbeddedRedisServer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 
 * @author rthota
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ReferenceEmbeddedRedisServerAutoConfiguration.class)
public class ReferenceEmbeddedRedisServerTest {

	@Autowired
	ReferenceEmbeddedRedisServer ascentEmbeddedServer;

	@Before
	public void setUp() {
		if (ascentEmbeddedServer.getRedisServer().isActive()) {
			ascentEmbeddedServer.stopRedis();
		}
	}

	@Test(timeout = 1500L)
	public void testSimpleRun() throws Exception {
		ascentEmbeddedServer.startRedis();
		ascentEmbeddedServer.stopRedis();
	}

	@Test
	public void shouldAllowSubsequentRuns() throws Exception {
		ascentEmbeddedServer.startRedis();
		ascentEmbeddedServer.stopRedis();

		ascentEmbeddedServer.startRedis();
		ascentEmbeddedServer.stopRedis();

		ascentEmbeddedServer.startRedis();
		ascentEmbeddedServer.stopRedis();
	}

	@Test
	public void testSimpleOperationsAfterRun() throws Exception {
		ascentEmbeddedServer.startRedis();

		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = new JedisPool("localhost", ascentEmbeddedServer.getRedisServer().ports().get(0));
			jedis = pool.getResource();
			jedis.mset("abc", "1", "def", "2");

			assertEquals("1", jedis.mget("abc").get(0));
			assertEquals("2", jedis.mget("def").get(0));
			assertEquals(null, jedis.mget("xyz").get(0));
		} finally {
			if (jedis != null)
				pool.close();
			ascentEmbeddedServer.stopRedis();
		}
	}

	@Test
	public void shouldIndicateInactiveBeforeStart() throws Exception {
		assertFalse(ascentEmbeddedServer.getRedisServer().isActive());
	}

	@Test
	public void shouldIndicateActiveAfterStart() throws Exception {
		ascentEmbeddedServer.startRedis();
		assertTrue(ascentEmbeddedServer.getRedisServer().isActive());
		ascentEmbeddedServer.stopRedis();
	}

	@Test
	public void shouldIndicateInactiveAfterStop() throws Exception {
		ascentEmbeddedServer.startRedis();
		ascentEmbeddedServer.stopRedis();
		assertFalse(ascentEmbeddedServer.getRedisServer().isActive());
	}

	@After
	public void teardown() {
		if (ascentEmbeddedServer.getRedisServer().isActive()) {
			ascentEmbeddedServer.stopRedis();
		}
	}
}