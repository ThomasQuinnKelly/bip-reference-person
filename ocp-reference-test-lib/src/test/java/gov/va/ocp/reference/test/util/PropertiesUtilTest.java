package gov.va.ocp.reference.test.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.net.URL;
import java.util.Properties;

import org.junit.Test;

public class PropertiesUtilTest {


	@Test
	public void testreadFile_Success() {
		
		final URL urlConfigFile = PropertiesUtilTest.class.getClassLoader().getResource("test.properties");
		Properties properties = PropertiesUtil.readFile(urlConfigFile);
		assertThat("reference", equalTo(properties.get("project")));
	}

	@Test (expected = NullPointerException.class)
	public void testreadFile_failure() {
		
		final URL urlConfigFile = PropertiesUtilTest.class.getClassLoader().getResource("test-not-exists.properties");
		PropertiesUtil.readFile(urlConfigFile);
	}
}
