package gov.va.ocp.reference.starter.cache.autoconfigure;

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
		ReferenceCacheProperties referenceCacheProperties = new ReferenceCacheProperties();
		assertNull(referenceCacheProperties.getExpires());
		assertEquals(new Long(86400L), referenceCacheProperties.getDefaultExpires());
	}

	@Test
	public void testSetters() {
		ReferenceCacheProperties referenceCacheProperties = new ReferenceCacheProperties();
		List<ReferenceCacheProperties.RedisExpires> listRedisExpires = new ArrayList<>();
		ReferenceCacheProperties.RedisExpires redisExpires = new ReferenceCacheProperties.RedisExpires();
		redisExpires.setName("methodcachename_projectname_projectversion");
		redisExpires.setTtl(86400L);
		listRedisExpires.add(0, redisExpires);
		referenceCacheProperties.setExpires(listRedisExpires);
		referenceCacheProperties.setDefaultExpires(500L);
		assertTrue(!referenceCacheProperties.getExpires().isEmpty());
		assertTrue(Long.valueOf(86400L).equals(referenceCacheProperties.getExpires().get(0).getTtl()));
		assertEquals(new Long(500L), referenceCacheProperties.getDefaultExpires());
	}
}