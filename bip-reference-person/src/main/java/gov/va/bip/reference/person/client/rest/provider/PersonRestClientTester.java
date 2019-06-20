package gov.va.bip.reference.person.client.rest.provider;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import gov.va.bip.framework.client.rest.template.RestClientTemplate;
import gov.va.bip.framework.log.BipLogger;
import gov.va.bip.framework.log.BipLoggerFactory;
import gov.va.bip.framework.swagger.SwaggerResponseMessages;
import gov.va.bip.reference.person.api.PersonRestClientTesterApi;
import gov.va.bip.reference.person.api.model.v1.PersonInfoRequest;
import gov.va.bip.reference.person.api.model.v1.PersonInfoResponse;
import gov.va.bip.reference.person.api.provider.PersonResource;
import gov.va.bip.reference.person.client.rest.FeignPersonClient;

/**
 * The purpose of this class is to make REST client calls. These are REST clients to our own
 * services just to experiment with how to use REST clients through the various techniques.
 *
 * @author akulkarni
 */
@RestController
public class PersonRestClientTester implements PersonRestClientTesterApi, SwaggerResponseMessages {

	private static final BipLogger LOGGER = BipLoggerFactory.getLogger(PersonRestClientTester.class);

	@Autowired
	private RestClientTemplate personUsageRestTemplate;

	@Autowired
	private FeignPersonClient feignPersonClient;

	public static final String URL_PREFIX = PersonResource.URL_PREFIX + "/clientTests";

	@Override
	public ResponseEntity<PersonInfoResponse> callPersonByPidUsingFeignClientUsingPOST(
			@Valid PersonInfoRequest personInfoRequest) {
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

	@Override
	public ResponseEntity<PersonInfoResponse> callPersonByPidUsingRestTemplateUsingPOST(
			@Valid PersonInfoRequest personInfoRequest) {
		// invoke the service using classic REST Template from Spring, but load balanced through Consul
		HttpEntity<PersonInfoRequest> requestEntity = new HttpEntity<>(personInfoRequest);
		ResponseEntity<PersonInfoResponse> exchange = null;
		exchange =
				personUsageRestTemplate.executeURL("http://localhost:8080" + PersonResource.URL_PREFIX + "/pid",
						HttpMethod.POST, requestEntity, new ParameterizedTypeReference<PersonInfoResponse>() {
						});
		LOGGER.info("Invoked os-reference-person service using REST template: " + exchange);
		return exchange;
	}
}
