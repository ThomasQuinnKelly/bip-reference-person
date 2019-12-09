package gov.va.bip.reference.person.api.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;

import gov.va.bip.framework.security.model.Person;
import gov.va.bip.reference.person.api.model.v1.PersonDocsMetadata;
import gov.va.bip.reference.person.api.model.v1.PersonDocsMetadataResponse;
import gov.va.bip.reference.person.api.model.v1.PersonDocsMetadataUploadResponse;
import gov.va.bip.reference.person.api.model.v1.PersonInfo;
import gov.va.bip.reference.person.api.model.v1.PersonInfoRequest;
import gov.va.bip.reference.person.api.model.v1.PersonInfoResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
		"spring.cloud.bus.enabled=false", "spring.cloud.discovery.enabled=false", "spring.cloud.consul.enabled=false",
		"spring.cloud.config.discovery.enabled=false", "spring.cloud.vault.enabled=false" })
public class PersonResourceTest {

	@Autowired
	RestTemplateBuilder restTemplateBuilder;

	@Autowired
	private TestRestTemplate restTemplate;

	String token;
	
	HttpHeaders httpPostMultiPartHeaders;

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

		ResponseEntity<String> tokenResponseEntity = restTemplate.exchange("/api/v1/token", HttpMethod.POST,
				requestTokenEntity, String.class);

		token = tokenResponseEntity.getBody();

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + token);

		getRequestEntity = new HttpEntity<>(headers);

		PersonInfoRequest body = new PersonInfoRequest();
		body.setParticipantID(pidLong);

		postRequestEntity = new HttpEntity<>(body, headers);
		
		// MultiPart Form Header
		httpPostMultiPartHeaders = headers;
		httpPostMultiPartHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
		
	}

	@Test
	public void testExampleDocument() throws IOException {
		ResponseEntity<Resource> resourceResponseEntity = restTemplate.exchange("/api/v1/persons/documents/sample",
				HttpMethod.GET, getRequestEntity, Resource.class);

		int statusCode = resourceResponseEntity.getStatusCodeValue();

		assertEquals(200, statusCode);

		Resource returnedSampleDoc = resourceResponseEntity.getBody();

		assertNotNull(returnedSampleDoc);
		assertEquals("Sample Reference data ...",
				StreamUtils.copyToString(returnedSampleDoc.getInputStream(), Charset.defaultCharset()));

	}

	@Test
	public void testPersonByPID() {
		ResponseEntity<PersonInfoResponse> resourceResponseEntity = restTemplate.exchange("/api/v1/persons/pid",
				HttpMethod.POST, postRequestEntity, PersonInfoResponse.class);

		int statusCode = resourceResponseEntity.getStatusCodeValue();

		assertEquals(200, statusCode);

		PersonInfoResponse returnedPersonInfo = resourceResponseEntity.getBody();

		assertNotNull(returnedPersonInfo);

		@Valid
		PersonInfo personInfo = returnedPersonInfo.getPersonInfo();

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

		ResponseEntity<PersonDocsMetadataResponse> resourceResponseEntity = restTemplate.exchange(
				"/api/v1/persons/" + pidLong + "/documents/metadata", HttpMethod.GET, getRequestEntity,
				PersonDocsMetadataResponse.class);

		int statusCode = resourceResponseEntity.getStatusCodeValue();

		assertEquals(200, statusCode);

		PersonDocsMetadataResponse returnedPersonDocsMetadata = resourceResponseEntity.getBody();

		assertNotNull(returnedPersonDocsMetadata);

		PersonDocsMetadata personDocsMetadata = returnedPersonDocsMetadata.getPersonDocsMetadata();

		assertNotNull(personDocsMetadata);

		assertEquals("test1", personDocsMetadata.getDocName());
	}

	@Test
	public void testPersonDocUpload() {
		// MultiPart form body
		MultiValueMap<String, Object> multiPartBody = new LinkedMultiValueMap<>();
		multiPartBody.add("file", new ClassPathResource("document.txt"));
		multiPartBody.add("docName", "test");
		multiPartBody.add("docCreateDate", "2019-01-01");
		
		HttpEntity<?> postMultiPartRequestEntity = new HttpEntity<>(multiPartBody, httpPostMultiPartHeaders);
		
		ResponseEntity<PersonDocsMetadataUploadResponse> resourceResponseEntity = restTemplate.exchange(
				"/api/v1/persons/" + pidLong + "/documents", HttpMethod.POST, postMultiPartRequestEntity,
				PersonDocsMetadataUploadResponse.class);

		int statusCode = resourceResponseEntity.getStatusCodeValue();

		assertEquals(200, statusCode);

		PersonDocsMetadataUploadResponse returnedPersonDocsMetadataUpload = resourceResponseEntity.getBody();

		assertNotNull(returnedPersonDocsMetadataUpload);

	}
	
	@Test
	public void testPersonDocUploadInvalidDate() {
		// MultiPart form body invalid date
		MultiValueMap<String, Object> multiPartBodyInvalidDate = new LinkedMultiValueMap<>();
		multiPartBodyInvalidDate.add("file", new ClassPathResource("document.txt"));
		multiPartBodyInvalidDate.add("docName", "test");
		multiPartBodyInvalidDate.add("docCreateDate", "INVALID_DATE");
		
		HttpEntity<?> postMultiPartRequestEntityInvalidDate = new HttpEntity<>(multiPartBodyInvalidDate, httpPostMultiPartHeaders);

		ResponseEntity<PersonDocsMetadataUploadResponse> resourceResponseEntity = restTemplate.exchange(
				"/api/v1/persons/" + pidLong + "/documents", HttpMethod.POST, postMultiPartRequestEntityInvalidDate,
				PersonDocsMetadataUploadResponse.class);

		int statusCode = resourceResponseEntity.getStatusCodeValue();

		assertEquals(400, statusCode);

		PersonDocsMetadataUploadResponse returnedPersonDocsMetadataUpload = resourceResponseEntity.getBody();

		assertNotNull(returnedPersonDocsMetadataUpload);

	}
	
	@Test
	public void testPersonDocUploadBlankDocName() {
		// MultiPart form body invalid date
		MultiValueMap<String, Object> multiPartBodyBlankDocName = new LinkedMultiValueMap<>();
		multiPartBodyBlankDocName.add("file", new ClassPathResource("document.txt"));
		multiPartBodyBlankDocName.add("docName", "");
		multiPartBodyBlankDocName.add("docCreateDate", "2019-01-01");
		
		HttpEntity<?> postMultiPartRequestEntityBlankDocName = new HttpEntity<>(multiPartBodyBlankDocName, httpPostMultiPartHeaders);
		
		ResponseEntity<PersonDocsMetadataUploadResponse> resourceResponseEntity = restTemplate.exchange(
				"/api/v1/persons/" + pidLong + "/documents", HttpMethod.POST, postMultiPartRequestEntityBlankDocName,
				PersonDocsMetadataUploadResponse.class);

		int statusCode = resourceResponseEntity.getStatusCodeValue();

		assertEquals(200, statusCode);

		PersonDocsMetadataUploadResponse returnedPersonDocsMetadataUpload = resourceResponseEntity.getBody();

		assertNotNull(returnedPersonDocsMetadataUpload);

	}

	@Test
	public void testPersonByPIDRestNegativeTest() {
		ResponseEntity<PersonInfoResponse> resourceResponseEntity = restTemplate.exchange(
				"/api/v1/persons/clientTests/callPersonByPidUsingRestTemplate", HttpMethod.POST, postRequestEntity,
				PersonInfoResponse.class);

		int statusCode = resourceResponseEntity.getStatusCodeValue();

		assertNotEquals(200, statusCode);

		PersonInfoResponse returnedPersonInfo = resourceResponseEntity.getBody();

		assertNull(returnedPersonInfo.getPersonInfo());

	}

	@Test
	public void testPersonByPIDFeignNegative() {
		ResponseEntity<PersonInfoResponse> resourceResponseEntity = restTemplate.exchange(
				"/api/v1/persons/clientTests/callPersonByPidUsingFeignClient", HttpMethod.POST, postRequestEntity,
				PersonInfoResponse.class);

		int statusCode = resourceResponseEntity.getStatusCodeValue();

		assertNotEquals(200, statusCode);

		PersonInfoResponse returnedPersonInfo = resourceResponseEntity.getBody();

		assertNotNull(returnedPersonInfo);

		assertNull(returnedPersonInfo.getPersonInfo());

	}
}
