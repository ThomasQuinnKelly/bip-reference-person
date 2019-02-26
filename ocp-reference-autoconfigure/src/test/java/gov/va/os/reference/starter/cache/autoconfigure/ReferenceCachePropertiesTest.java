package gov.va.os.reference.starter.cache.autoconfigure;

import org.junit.Test;

import gov.va.ocp.reference.starter.cache.autoconfigure.ReferenceCacheProperties;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ReferenceCachePropertiesTest {

	@Test
	public void testGetters() {
		ReferenceCacheProperties ascentCacheProperties = new ReferenceCacheProperties();
		assertNull(ascentCacheProperties.getExpires());
		assertEquals(new Long(86400L), ascentCacheProperties.getDefaultExpires());
	}

	@Test
	public void testSetters() {
		ReferenceCacheProperties ascentCacheProperties = new ReferenceCacheProperties();
		List<ReferenceCacheProperties.RedisExpires> listRedisExpires = new ArrayList<>();
		ReferenceCacheProperties.RedisExpires redisExpires = new ReferenceCacheProperties.RedisExpires();
		redisExpires.setName("methodcachename_projectname_projectversion");
		redisExpires.setTtl(86400L);
		listRedisExpires.add(0, redisExpires);
		ascentCacheProperties.setExpires(listRedisExpires);
		ascentCacheProperties.setDefaultExpires(500L);
		assertTrue(!ascentCacheProperties.getExpires().isEmpty());
		assertTrue(Long.valueOf(86400L).equals(ascentCacheProperties.getExpires().get(0).getTtl()));
		assertEquals(new Long(500L), ascentCacheProperties.getDefaultExpires());
	}
}