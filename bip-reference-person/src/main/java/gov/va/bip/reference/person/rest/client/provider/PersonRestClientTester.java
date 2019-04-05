package gov.va.bip.reference.person.rest.client.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.va.bip.framework.rest.client.resttemplate.RestClientTemplate;
import gov.va.bip.framework.swagger.SwaggerResponseMessages;
import gov.va.bip.reference.person.api.model.v1.PersonInfoRequest;
import gov.va.bip.reference.person.api.model.v1.PersonInfoResponse;
import gov.va.bip.reference.person.api.provider.PersonResource;
import gov.va.bip.reference.person.model.PersonByPidDomainRequest;
import gov.va.bip.reference.person.model.PersonByPidDomainResponse;
import gov.va.bip.reference.person.rest.client.FeignPersonClient;
import io.swagger.annotations.ApiOperation;

/**
 * The purpose of this class is to make REST client calls. These are REST clients to our own
 * services just to experiment with how to use REST clients through the various techniques.
 *
 * @author akulkarni
 */
@RestController
public class PersonRestClientTester implements SwaggerResponseMessages {

	private static final Logger LOGGER = LoggerFactory.getLogger(PersonRestClientTester.class);

	@Autowired
	private RestClientTemplate personUsageRestTemplate;

	@Autowired
	private FeignPersonClient feignPersonClient;

	public static final String URL_PREFIX = PersonResource.URL_PREFIX + "/clientTests";

	/**
	 * This method demonstrates the use of RestTemplate to make REST calls on the service endpoints.
	 *
	 * @param personByPidDomainRequest the person by pid domain request
	 * @return a ResponseEntity
	 */
	@ApiOperation(value = "An endpoint which uses a REST client using RestTemplate to call the remote echo operation.")
	@RequestMapping(value = URL_PREFIX + "/callPersonByPidUsingRestTemplate", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PersonByPidDomainResponse>
			callPersonByPidUsingRestTemplate(@RequestBody final PersonByPidDomainRequest personByPidDomainRequest) {
		// invoke the service using classic REST Template from Spring, but load balanced through Consul
		HttpEntity<PersonByPidDomainRequest> requestEntity = new HttpEntity<>(personByPidDomainRequest);
		ResponseEntity<PersonByPidDomainResponse> exchange = null;
		exchange =
				personUsageRestTemplate.executeURL("http://localhost:8080" + PersonResource.URL_PREFIX + "/pid",
						HttpMethod.POST, requestEntity, new ParameterizedTypeReference<PersonByPidDomainResponse>() {
						});
		LOGGER.info("Invoked os-reference-person service using REST template: " + exchange);
		return exchange;
	}

	/**
	 * This method demonstrates the use of Feign Client to make REST calls.
	 *
	 * @param personInfoRequest the person info request
	 * @return a ResponseEntity
	 */
	@ApiOperation(value = "An endpoint which uses a REST client using Feign to call the remote person by pid operation.")
	@RequestMapping(value = URL_PREFIX + "/callPersonByPidUsingFeignClient", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PersonInfoResponse>
			callPersonByPidUsingFeignClient(@RequestBody final PersonInfoRequest personInfoRequest) {

		// use this in case of feign hystrix to test fallback handler invocation
		// NOSONAR ConfigurationManager.getConfigInstance().setProperty("hystrix.command.default.circuitBreaker.forceOpen", "true");
		PersonInfoResponse personInfoResponse = null;

		personInfoResponse = feignPersonClient.personByPid(personInfoRequest); // NOSONAR cannot immediately return

		if (personInfoResponse != null) {
			if (personInfoResponse.hasErrors()) {
				return new ResponseEntity<>(personInfoResponse, HttpStatus.BAD_REQUEST);
			} else if (personInfoResponse.hasFatals()) {
				return new ResponseEntity<>(personInfoResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				// real-world might do more business processing here
				return new ResponseEntity<>(personInfoResponse, HttpStatus.OK);
			}
		} else {
			return new ResponseEntity<>(personInfoResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
