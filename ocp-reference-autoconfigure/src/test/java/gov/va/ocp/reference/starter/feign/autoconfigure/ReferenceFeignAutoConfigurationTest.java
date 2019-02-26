package gov.va.ocp.reference.starter.feign.autoconfigure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;

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

import com.netflix.hystrix.HystrixCommand;

import feign.Feign;
import feign.Target;
import feign.hystrix.SetterFactory;
import gov.va.ocp.reference.framework.rest.provider.RestProviderHttpResponseAspect;
import gov.va.ocp.reference.starter.audit.autoconfigure.ReferenceAuditAutoConfiguration;
import gov.va.ocp.reference.starter.feign.autoconfigure.ReferenceFeignAutoConfiguration;
import gov.va.ocp.reference.starter.feign.autoconfigure.TokenFeignRequestInterceptor;
import gov.va.ocp.reference.starter.security.autoconfigure.ReferenceSecurityAutoConfiguration;

/**
 * Created by rthota on 8/24/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class ReferenceFeignAutoConfigurationTest {

	private static String CONNECTION_TIMEOUT = "20000";

	private ReferenceFeignAutoConfiguration referenceFeignAutoConfiguration;

	private AnnotationConfigWebApplicationContext context;

	@Before
	public void setup() {
		context = new AnnotationConfigWebApplicationContext();
		TestPropertyValues.of("feign.hystrix.enabled=true").applyTo(context);
		TestPropertyValues.of("os.reference.rest.client.connection-timeout=" + CONNECTION_TIMEOUT).applyTo(context);
		context.register(JacksonAutoConfiguration.class, SecurityAutoConfiguration.class, EmbeddedWebServerFactoryCustomizerAutoConfiguration.class,
				ReferenceSecurityAutoConfiguration.class,
				ReferenceAuditAutoConfiguration.class, ReferenceFeignAutoConfiguration.class,
				RestProviderHttpResponseAspect.class);

		context.refresh();
		assertNotNull(context);

		referenceFeignAutoConfiguration = context.getBean(ReferenceFeignAutoConfiguration.class);
		assertNotNull(referenceFeignAutoConfiguration);
	}

	@After
	public void close() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void testWebConfiguration() throws Exception {
		final TokenFeignRequestInterceptor tokenFeignRequestInterceptor = this.context.getBean(TokenFeignRequestInterceptor.class);
		assertNotNull(tokenFeignRequestInterceptor);
	}

	@Test
	public void testWebConfiguration_BrokenProp() throws Exception {
		TestPropertyValues.of("os.reference.rest.client.connection-timeout=BLAHBLAH").applyTo(context);
		context.refresh();

		try {
			referenceFeignAutoConfiguration.feignBuilder();
			fail("referenceFeignAutoConfiguration.feignBuilder() should have thrown IllegalStateException");
		} catch (Exception e) {
			assertTrue(BeansException.class.isAssignableFrom(e.getClass()));
		} finally {
			TestPropertyValues.of("os.reference.rest.client.connection-timeout=" + CONNECTION_TIMEOUT).applyTo(context);
			context.refresh();
		}

	}

	@Test
	public void testGetterSettingReferenceFiegnConfig() throws Exception {
		final ReferenceFeignAutoConfiguration referenceFeignAutoConfiguration = new ReferenceFeignAutoConfiguration();
		assertEquals("defaultGroup", referenceFeignAutoConfiguration.getGroupKey());
		referenceFeignAutoConfiguration.setGroupKey("NewGroupKey");
		assertEquals("NewGroupKey", referenceFeignAutoConfiguration.getGroupKey());
	}

	/**
	 * Test of feignBuilder method, of class ReferenceFeignAutoConfiguration.
	 */
	@Test
	public void testFeignBuilder() {
		final Feign.Builder result = referenceFeignAutoConfiguration.feignBuilder();
		assertNotNull(result);

	}

	@Test
	public void testSetterFactory() {
		final Feign.Builder result = referenceFeignAutoConfiguration.feignBuilder();

		try {
			final Field setterFactoryField = result.getClass().getDeclaredField("setterFactory");
			setterFactoryField.setAccessible(true);
			final SetterFactory factory = (SetterFactory) setterFactoryField.get(result);
			final Target<?> target = new TestTarget(this.getClass(), "testFeignBuilder");
			final HystrixCommand.Setter setter = factory.create(target, this.getClass().getMethod("testFeignBuilder"));
			assertNotNull(setter);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| NoSuchMethodException e) {
			e.printStackTrace();
			fail("Should not throw exception here.");
		}
	}

	@SuppressWarnings("rawtypes")
	class TestTarget extends Target.HardCodedTarget {

		@SuppressWarnings("unchecked")
		public TestTarget(final Class type, final String url) {
			super(type, url);
		}

	}
}
