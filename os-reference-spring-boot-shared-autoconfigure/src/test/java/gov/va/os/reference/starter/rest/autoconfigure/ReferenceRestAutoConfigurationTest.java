package gov.va.os.reference.starter.rest.autoconfigure;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import gov.va.os.reference.framework.rest.provider.RestProviderHttpResponseCodeAspect;
import gov.va.os.reference.starter.audit.autoconfigure.ReferenceAuditAutoConfiguration;
import gov.va.os.reference.starter.rest.autoconfigure.ReferenceRestAutoConfiguration;
import gov.va.os.reference.starter.security.autoconfigure.ReferenceSecurityAutoConfiguration;

/**
 * Created by rthota on 8/24/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class ReferenceRestAutoConfigurationTest {

	private static final String CONNECTION_TIMEOUT = "20000";

	private ReferenceRestAutoConfiguration ascentRestAutoConfiguration;

	private AnnotationConfigWebApplicationContext context;

	@Before
	public void setup() {
		context = new AnnotationConfigWebApplicationContext();
		TestPropertyValues.of("feign.hystrix.enabled=true").applyTo(context);;
		TestPropertyValues.of("ascent.rest.client.connection-timeout=" + CONNECTION_TIMEOUT).applyTo(context);;
		context.register(JacksonAutoConfiguration.class, SecurityAutoConfiguration.class, EmbeddedWebServerFactoryCustomizerAutoConfiguration.class,
				ReferenceSecurityAutoConfiguration.class,
				ReferenceAuditAutoConfiguration.class, ReferenceRestAutoConfiguration.class,
				RestProviderHttpResponseCodeAspect.class);

		context.refresh();
		assertNotNull(context);

		// test configuration and give ascentRestAutoConfiguration a value for other tests
		ascentRestAutoConfiguration = context.getBean(ReferenceRestAutoConfiguration.class);
		assertNotNull(ascentRestAutoConfiguration);
	}

	@After
	public void close() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void testConfiguration_Broken() {
		TestPropertyValues.of("ascent.rest.client.connection-timeout=BLAHBLAH").applyTo(context);;

		try {
			context.refresh();
			ascentRestAutoConfiguration.restClientTemplate();
			fail("ReferenceRestAutoConfiguration should have thrown IllegalStateException or BeansException");
		} catch (Exception e) {
			assertTrue(BeansException.class.isAssignableFrom(e.getClass()));
		} finally {
			TestPropertyValues.of("ascent.rest.client.connection-timeout=" + CONNECTION_TIMEOUT).applyTo(context);;
			context.refresh();
			ascentRestAutoConfiguration = context.getBean(ReferenceRestAutoConfiguration.class);
			assertNotNull(ascentRestAutoConfiguration);
		}
	}

	@Test
	public void testWebConfiguration() throws Exception {
		assertNotNull(ascentRestAutoConfiguration.restProviderHttpResponseCodeAspect());
		assertNotNull(ascentRestAutoConfiguration.restProviderTimerAspect());
		assertNotNull(ascentRestAutoConfiguration.restClientTemplate());
		assertNotNull(ascentRestAutoConfiguration.tokenClientHttpRequestInterceptor());
	}

}
