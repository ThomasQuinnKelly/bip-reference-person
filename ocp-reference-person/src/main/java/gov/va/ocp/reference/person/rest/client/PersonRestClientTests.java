package gov.va.ocp.reference.person.rest.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.va.ocp.reference.framework.rest.client.resttemplate.RestClientTemplate;
import gov.va.ocp.reference.framework.swagger.SwaggerResponseMessages;
import gov.va.ocp.reference.person.model.person.v1.PersonInfoRequest;
import gov.va.ocp.reference.person.model.person.v1.PersonInfoResponse;
import gov.va.ocp.reference.person.rest.client.feign.FeignPersonClient;
import gov.va.ocp.reference.person.rest.provider.PersonResource;
import io.swagger.annotations.ApiOperation;

/**
 * The purpose of this class is to make REST client calls. These are REST clients to our own
 * services just to experiment with how to use REST clients through the various techniques.
 *
 * @author akulkarni
 */
@RestController
public class PersonRestClientTests implements SwaggerResponseMessages {

	private static final Logger LOGGER = LoggerFactory.getLogger(PersonRestClientTests.class);

	@Autowired
	private RestClientTemplate personUsageRestTemplate;

	@Autowired
	private FeignPersonClient feignPersonClient;

	public static final String URL_PREFIX = PersonResource.URL_PREFIX + "/clientTests";

	/**
	 * This method demonstrates the use of RestTemplate to make REST calls on the service endpoints.
	 *
	 * @param request the request
	 * @return a ResponseEntity
	 */
	@ApiOperation(value = "An endpoint which uses a REST client using RestTemplate to call the remote echo operation.")
	@RequestMapping(value = URL_PREFIX + "/callPersonByPidUsingRestTemplate", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PersonInfoResponse> callPersonByPidUsingRestTemplate(@RequestBody final PersonInfoRequest personInfoRequest) {
		// invoke the service using classic REST Template from Spring, but load balanced through Consul
		HttpEntity<PersonInfoRequest> requestEntity = new HttpEntity<>(personInfoRequest);
		final ResponseEntity<PersonInfoResponse> exchange =
				personUsageRestTemplate.executeURL("http://localhost:8080" + PersonResource.URL_PREFIX + "/pid",
						HttpMethod.POST, requestEntity, new ParameterizedTypeReference<PersonInfoResponse>() {
						});
		LOGGER.info("Invoked os-reference-person service using REST template: " + exchange);
		return exchange;
	}

	/**
	 * This method demonstrates the use of Feign Client to make REST calls.
	 *
	 * @param request the request
	 * @return a ResponseEntity
	 */
	@ApiOperation(value = "An endpoint which uses a REST client using Feign to call the remote person by pid operation.")
	@RequestMapping(value = URL_PREFIX + "/callPersonByPidUsingFeignClient", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public PersonInfoResponse callPersonByPidUsingFeignClient(@RequestBody final PersonInfoRequest personInfoRequest) {

		// use this in case of feign hystrix to test fallback handler invocation
		// NOSONAR ConfigurationManager.getConfigInstance().setProperty("hystrix.command.default.circuitBreaker.forceOpen", "true");

		final PersonInfoResponse personInfoResponse = feignPersonClient.personByPid(personInfoRequest); // NOSONAR cannot immediately return

		return personInfoResponse;
	}
}