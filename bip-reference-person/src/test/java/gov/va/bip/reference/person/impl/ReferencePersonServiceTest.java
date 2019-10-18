package gov.va.bip.reference.person.impl;

import gov.va.bip.framework.security.model.Person;
import gov.va.bip.reference.person.api.model.v1.*;
import gov.va.bip.reference.person.exception.PersonServiceException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StreamUtils;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = { "spring.cloud.bus.enabled=false", "spring.cloud.discovery.enabled=false",
                "spring.cloud.consul.enabled=false", "spring.cloud.config.discovery.enabled=false", "spring.cloud.vault.enabled=false"})
public class ReferencePersonServiceTest {

    @Autowired
    RestTemplateBuilder restTemplateBuilder;

    @Autowired
    private TestRestTemplate restTemplate;

    String token;

    HttpEntity<?> getRequestEntity;

    HttpEntity<?> postRequestEntity;

    Long pidLong = 6666345L;

    @Before
    public void setup() throws URISyntaxException, IOException {
        Person person = new Person();
        person.setFirstName("JANE");
        person.setLastName("DOE");
        person.setGender("FEMALE");
        person.setPrefix("Ms");
        person.setAssuranceLevel(2);
        person.setMiddleName("M");
        person.setSuffix("S");
        person.setBirthDate("2000-02-23");
        person.setEmail("jane.doe@va.gov");

        List<String> listOfCorrelationIds = new ArrayList<>();

        listOfCorrelationIds.add("77779102^NI^200M^USVHA^P");
        listOfCorrelationIds.add("912444689^PI^200BRLS^USVBA^A");
        listOfCorrelationIds.add("6666345^PI^200CORP^USVBA^A");
        listOfCorrelationIds.add("1105051936^NI^200DOD^USDOD^A");
        listOfCorrelationIds.add("912444689^SS");

        person.setCorrelationIds(listOfCorrelationIds);

        URI uri = new URI("/api/v1/token");

        RequestEntity<Person> requestTokenEntity = new RequestEntity<Person>(person, HttpMethod.POST, uri);

        ResponseEntity<String> tokenResponseEntity = restTemplate.exchange("/api/v1/token", HttpMethod.POST, requestTokenEntity, String.class);

        token = tokenResponseEntity.getBody();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        getRequestEntity = new HttpEntity<>(headers);

        PersonInfoRequest body = new PersonInfoRequest();
        body.setParticipantID(pidLong);

        postRequestEntity = new HttpEntity<>(body, headers);

    }

    @Test
    public void testExampleDocument() throws IOException {
        ResponseEntity<Resource> resourceResponseEntity = restTemplate.exchange("/api/v1/persons/documents/sample", HttpMethod.GET, getRequestEntity, Resource.class);

        int statusCode = resourceResponseEntity.getStatusCodeValue();

        assertEquals(200, statusCode);

        Resource returnedSampleDoc = resourceResponseEntity.getBody();

        assertNotNull(returnedSampleDoc);
        assertEquals("Sample Reference data ...", StreamUtils.copyToString(returnedSampleDoc.getInputStream(), Charset.defaultCharset()));

    }

    @Test
    public void testPersonByPID() {
        ResponseEntity<PersonInfoResponse> resourceResponseEntity = restTemplate.exchange("/api/v1/persons/pid", HttpMethod.POST, postRequestEntity, PersonInfoResponse.class);

        int statusCode = resourceResponseEntity.getStatusCodeValue();

        assertEquals(200, statusCode);

        PersonInfoResponse returnedPersonInfo = resourceResponseEntity.getBody();

        assertNotNull(returnedPersonInfo);

        @Valid PersonInfo personInfo = returnedPersonInfo.getPersonInfo();

        assertNotNull(personInfo);

        assertEquals(pidLong, personInfo.getParticipantId());
        assertEquals("912444689", personInfo.getFileNumber());
        assertEquals("912444689", personInfo.getSocSecNo());
        assertEquals("JANE", personInfo.getFirstName());
        assertEquals("M", personInfo.getMiddleName());
        assertEquals("DOE", personInfo.getLastName());

    }

    @Test
    public void testPostDocumentForPID() throws IOException {

        ResponseEntity<PersonDocsMetadataResponse> resourceResponseEntity = restTemplate.exchange("/api/v1/persons/"+pidLong+"/documents/metadata", HttpMethod.GET, getRequestEntity, PersonDocsMetadataResponse.class);

        int statusCode = resourceResponseEntity.getStatusCodeValue();

        assertEquals(200, statusCode);

        PersonDocsMetadataResponse returnedPersonDocsMetadata = resourceResponseEntity.getBody();

        assertNotNull(returnedPersonDocsMetadata);

        PersonDocsMetadata personDocsMetadata = returnedPersonDocsMetadata.getPersonDocsMetadata();

        assertNotNull(personDocsMetadata);

        assertEquals("test1", personDocsMetadata.getDocName());
    }

    @Test
    public void testPersonByPIDRestNegativeTest() {
        ResponseEntity<PersonInfoResponse> resourceResponseEntity = restTemplate.exchange("/api/v1/persons/clientTests/callPersonByPidUsingRestTemplate", HttpMethod.POST, postRequestEntity, PersonInfoResponse.class);

        int statusCode = resourceResponseEntity.getStatusCodeValue();

        assertEquals(500, statusCode);

        PersonInfoResponse returnedPersonInfo = resourceResponseEntity.getBody();

        assertNull(returnedPersonInfo.getPersonInfo());

    }

    @Test
    public void testPersonByPIDFeignNegative() {
        ResponseEntity<PersonInfoResponse> resourceResponseEntity = restTemplate.exchange("/api/v1/persons/clientTests/callPersonByPidUsingFeignClient", HttpMethod.POST, postRequestEntity, PersonInfoResponse.class);

        int statusCode = resourceResponseEntity.getStatusCodeValue();

        assertEquals(500, statusCode);

        PersonInfoResponse returnedPersonInfo = resourceResponseEntity.getBody();

        assertNotNull(returnedPersonInfo);

        assertNull(returnedPersonInfo.getPersonInfo());

    }

    @Test
    public void testStoreMetadataNegative() {
        ReferencePersonServiceImpl referencePersonServiceImpl = new ReferencePersonServiceImpl();

        try {
            referencePersonServiceImpl.storeMetadata(54321L, "docName", "NonsensicalDate");
            fail("An PersonServiceException was expected here.");
        } catch (PersonServiceException pse) {
            assertEquals("Date value given in the request is not valid.", pse.getMessage());
        }
    }
}

