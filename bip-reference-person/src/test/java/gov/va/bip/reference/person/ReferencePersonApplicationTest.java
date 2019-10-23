package gov.va.bip.reference.person;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = { "spring.cloud.bus.enabled=false",
		"spring.cloud.discovery.enabled=false", "spring.cloud.consul.enabled=false",
		"spring.cloud.config.discovery.enabled=false", "spring.cloud.vault.enabled=false" })
public class ReferencePersonApplicationTest {

	@Autowired
	private ReferencePersonProperties referencePersonProperties;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void contextLoads() {
	}

	@Test
	public void testRedirect() {
		// Testing out redirect
		ResponseEntity<Object> response = this.restTemplate.exchange("/", HttpMethod.GET, null, Object.class);

		int statusCode = response.getStatusCodeValue();

		assertEquals(302, statusCode);

		String location = response.getHeaders().getLocation() == null ? ""
				: response.getHeaders().getLocation().toString();

		assertThat(location).contains("/swagger-ui.html");
	}

	@Test
	public void testSwaggerUI() {
		String body = this.restTemplate.getForObject("/swagger-ui.html", String.class);
		assertThat(body).contains("Swagger UI");
	}

	@Test
	public void testReferencePersonProperties() {
		assertThat(referencePersonProperties.getEnv()).isEqualTo("default");
		assertThat(referencePersonProperties.getPassword()).isEqualTo("secret");
		assertThat(referencePersonProperties.getPropSource()).isEqualTo("internal");
		assertThat(referencePersonProperties.getSampleProperty()).isNotNull();
	}

}
