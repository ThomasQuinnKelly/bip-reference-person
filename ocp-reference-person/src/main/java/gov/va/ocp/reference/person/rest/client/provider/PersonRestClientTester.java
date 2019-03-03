package gov.va.ocp.reference.person.rest.client.provider;

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

import gov.va.ocp.reference.framework.messages.MessageSeverity;
import gov.va.ocp.reference.framework.rest.client.exception.ResponseEntityErrorException;
import gov.va.ocp.reference.framework.rest.client.resttemplate.RestClientTemplate;
import gov.va.ocp.reference.framework.service.DomainResponse;
import gov.va.ocp.reference.framework.swagger.SwaggerResponseMessages;
import gov.va.ocp.reference.person.api.provider.PersonResource;
import gov.va.ocp.reference.person.model.PersonByPidDomainRequest;
import gov.va.ocp.reference.person.model.PersonByPidDomainResponse;
import gov.va.ocp.reference.person.rest.client.FeignPersonClient;
import io.swagger.annotations.ApiOperation;

@RestController

/**
 * The purpose of this class is to make REST client calls. These are REST clients to our own
 * services just to experiment with how to use REST clients through the various techniques.
 *
 * @author akulkarni
 */
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
	 * @param request the request
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
		try {
			exchange =
					personUsageRestTemplate.executeURL("http://localhost:8080" + PersonResource.URL_PREFIX + "/pid",
							HttpMethod.POST, requestEntity, new ParameterizedTypeReference<PersonByPidDomainResponse>() {
							});
		} catch (ResponseEntityErrorException e) {
			LOGGER.error("ResponseEntityErrorHandler: {}", e);
			DomainResponse serviceResponse = e.getErrorResponse() == null ? new DomainResponse() : e.getErrorResponse().getBody();
			LOGGER.error("ServiceResponse: {}", serviceResponse == null ? "" : serviceResponse.toString());
			PersonByPidDomainResponse personInfoResponse = new PersonByPidDomainResponse();
			personInfoResponse.addMessages(serviceResponse.getMessages());
			LOGGER.error("PersonInfoResponse: {}", personInfoResponse.toString());
			return new ResponseEntity<>(personInfoResponse, e.getErrorResponse().getStatusCode());
		}
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
	public ResponseEntity<PersonByPidDomainResponse>
			callPersonByPidUsingFeignClient(@RequestBody final PersonByPidDomainRequest personByPidDomainRequest) {

		// use this in case of feign hystrix to test fallback handler invocation
		// NOSONAR ConfigurationManager.getConfigInstance().setProperty("hystrix.command.default.circuitBreaker.forceOpen", "true");
		PersonByPidDomainResponse personByPidDomainResponse = null;
		try {
			personByPidDomainResponse = feignPersonClient.personByPid(personByPidDomainRequest); // NOSONAR cannot immediately return
		} catch (Exception e) {
			LOGGER.error("Exception: {}", e);
			personByPidDomainResponse = new PersonByPidDomainResponse();
			personByPidDomainResponse.addMessage(MessageSeverity.FATAL, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
					e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			LOGGER.error("PersonInfoResponse: {}", personByPidDomainResponse.toString());
			return new ResponseEntity<>(personByPidDomainResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(personByPidDomainResponse, HttpStatus.OK);
	}
}
