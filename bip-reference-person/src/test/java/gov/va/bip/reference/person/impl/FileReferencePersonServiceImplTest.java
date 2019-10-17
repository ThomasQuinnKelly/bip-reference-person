package gov.va.bip.reference.person.impl;

import gov.va.bip.framework.security.model.Person;
import gov.va.bip.reference.person.api.model.v1.*;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = { "spring.cloud.bus.enabled=false", "spring.cloud.discovery.enabled=false",
                "spring.cloud.consul.enabled=false", "spring.cloud.config.discovery.enabled=false", "spring.cloud.vault.enabled=false"})
public class FileReferencePersonServiceImplTest {

    @Autowired
    RestTemplateBuilder restTemplateBuilder;

    @Autowired
    private TestRestTemplate restTemplate;

    String token;

    HttpEntity<?> getRequestEntity;

    HttpEntity<MultiValueMap<String, Object>> postFileRequestEntity;

    String pid = "6666345";

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

    }

    @Test
    public void testPostDocumentForPID() throws IOException {
        HttpHeaders fileHeaders = new HttpHeaders();
        fileHeaders.set("Authorization", "Bearer " + token);
        fileHeaders.set("Content-Type", "multipart/form-data");

        File file = new File("./src/test/resources/file_upload_example.txt");

        FileSystemResource fileSystemResource = new FileSystemResource(file);

        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("docName", "Test");
        parts.add("docCreateDate", "2019-08-09");
        parts.add("file", fileSystemResource);

        postFileRequestEntity = new HttpEntity<>(parts, fileHeaders);

        ResponseEntity<PersonDocsMetadataUploadResponse> resourceResponseEntity = restTemplate.exchange("/api​/v1​/persons​/" + pid + "​/documents", HttpMethod.POST, postFileRequestEntity, PersonDocsMetadataUploadResponse.class);

        int statusCode = resourceResponseEntity.getStatusCodeValue();

        assertEquals(200, statusCode);

        PersonDocsMetadataUploadResponse returnedPersonDocsMetadataUpload = resourceResponseEntity.getBody();

        assertNotNull(returnedPersonDocsMetadataUpload);

        // TODO: add more

        ResponseEntity<PersonDocsMetadataResponse> resource2ResponseEntity = restTemplate.exchange("/api​/v1​/persons​/"+pid+"​/documents​/metadata", HttpMethod.GET, getRequestEntity, PersonDocsMetadataResponse.class);

        int statusCode2 = resource2ResponseEntity.getStatusCodeValue();

        assertEquals(200, statusCode2);

        PersonDocsMetadataResponse returnedPersonDocsMetadata = resource2ResponseEntity.getBody();

        assertNotNull(returnedPersonDocsMetadata);

        // TODO: add more
    }

}

