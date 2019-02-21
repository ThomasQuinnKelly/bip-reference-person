package gov.va.os.reference.starter.vault.bootstrap;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

@Ignore
public class VaultForConsulBootstrapConfigurationTest {
	private AnnotationConfigWebApplicationContext context;

    @After
    public void close() {
        if (this.context != null) {
            this.context.close();
        }
    }

    @Test
    public void testWebConfiguration() throws Exception {
        context = new AnnotationConfigWebApplicationContext();
        context.register(VaultForConsulBootstrapConfiguration.class, EmbeddedWebServerFactoryCustomizerAutoConfiguration.class);
        context.refresh();
        assertNotNull(context);
        assertNotNull(this.context.getBean(VaultForConsulBootstrapConfiguration.class));

    }
}
