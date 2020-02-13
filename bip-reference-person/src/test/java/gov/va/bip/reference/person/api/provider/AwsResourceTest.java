package gov.va.bip.reference.person.api.provider;

import gov.va.bip.reference.person.api.model.v1.JmsResponse;
import gov.va.bip.reference.person.api.model.v1.PublishResult;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "spring.cloud.bus.enabled=false", "spring.cloud.discovery.enabled=false", "spring.cloud.consul.enabled=false",
        "spring.cloud.config.discovery.enabled=false", "spring.cloud.vault.enabled=false" })
public class AwsResourceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    String token;

    HttpHeaders httpPostMultiPartHeaders;

    HttpEntity<?> postRequestEntity;

    @Before
    public void setup() throws URISyntaxException, IOException {
        String message = "This is the test message";

        HttpHeaders headers = new HttpHeaders();

        postRequestEntity = new HttpEntity<>(message, headers);

        // MultiPart Form Header
        httpPostMultiPartHeaders = headers;
        httpPostMultiPartHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
    }

    @Test
    public void testSendMessage() throws IOException {
        ResponseEntity<JmsResponse> jmsResponseEntity = restTemplate.exchange("/api/v1/sqs/sendMessage",
                HttpMethod.POST, postRequestEntity, JmsResponse.class);

        int statusCode = jmsResponseEntity.getStatusCodeValue();

        assertEquals(200, statusCode);

        JmsResponse returnedJmsResponse = jmsResponseEntity.getBody();

        assertNotNull(returnedJmsResponse);
        assertEquals("ID:", returnedJmsResponse.getJmsId().substring(0,3));

    }


    @Test
    @Ignore
    public void testPublishMessage() throws IOException {
        ResponseEntity<PublishResult> publishResultResponseEntity = restTemplate.exchange("/api/v1/sns/publishMessage",
                HttpMethod.POST, postRequestEntity, PublishResult.class);

        int statusCode = publishResultResponseEntity.getStatusCodeValue();

        assertEquals(200, statusCode);

        PublishResult returnedPublishResult = publishResultResponseEntity.getBody();

        assertNotNull(returnedPublishResult);
        assertNotNull(returnedPublishResult.getMessageId());

    }

}
