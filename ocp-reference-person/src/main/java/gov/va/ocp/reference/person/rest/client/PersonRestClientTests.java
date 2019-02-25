package gov.va.ocp.reference.person.rest.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import gov.va.ocp.reference.framework.rest.client.resttemplate.RestClientTemplate;
import gov.va.ocp.reference.framework.swagger.SwaggerResponseMessages;
import gov.va.ocp.reference.person.rest.client.discovery.PersonUsageDiscoveryClient;
import gov.va.ocp.reference.person.rest.client.feign.FeignEchoClient;
import gov.va.ocp.reference.person.rest.provider.PersonResource;

/**
 * The purpose of this class is to make REST client calls. These are REST clients to our own
 * services just to experiment with how to use REST clients through the various techniques.
 *
 * @author jshrader
 */
@RestController
public class PersonRestClientTests implements SwaggerResponseMessages {

	private static final Logger LOGGER = LoggerFactory.getLogger(PersonRestClientTests.class);

	@Autowired
	private PersonUsageDiscoveryClient personUsageDiscoveryClient;

	@Autowired
	private RestClientTemplate personUsageRestTemplate;

	@Autowired
	private FeignEchoClient feignEchoClient;

	public static final String URL_PREFIX = PersonResource.URL_PREFIX + "/clientTests";

//	TODO
//	/**
//	 * This method demonstrates the use of DiscoveryClient to iterate through each of service instance endpoint to make REST calls.
//	 *
//	 * @param request the request
//	 * @return a ResponseEntity
//	 */
//	@ApiOperation(value = "An endpoint demo's using the DiscoveryClient to interrogate services.")
//	@RequestMapping(value = URL_PREFIX + "/demoDiscoveryClientUsage", method = RequestMethod.GET,
//			produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<ServiceInstancesServiceResponse> demoDiscoveryClientUsage(final HttpServletRequest request) {
//		final ServiceInstancesServiceResponse response = personUsageDiscoveryClient.invokeServiceUsingDiscoveryClient();
//		return new ResponseEntity<>(response, HttpStatus.OK);
//	}
//
//	/**
//	 * This method demonstrates the use of RestTemplate to make REST calls on the service endpoints.
//	 *
//	 * @param request the request
//	 * @return a ResponseEntity
//	 */
//	@ApiOperation(value = "An endpoint which uses a REST client using RestTemplate to call the remote echo operation.")
//	@RequestMapping(value = URL_PREFIX + "/personCallEchoUsingRestTemplate", method = RequestMethod.GET,
//			produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<EchoHostServiceResponse> personCallEchoUsingRestTemplate(final HttpServletRequest request) {
//		// invoke the service using classic REST Template from Spring, but load balanced through Eureka/Zuul
//		final ResponseEntity<EchoHostServiceResponse> exchange =
//				personUsageRestTemplate.executeURL("http://os-reference-service-1/service-1/v1/echo",
//						new ParameterizedTypeReference<EchoHostServiceResponse>() {
//						});
//		LOGGER.info("INVOKED A REFERENCE-DEMO-SERVICE USING REST TEMPLATE: " + exchange.getBody());
//		return new ResponseEntity<>(exchange.getBody(), exchange.getStatusCode());
//	}
//
//	/**
//	 * This method demonstrates the use of Feign Client to make REST calls.
//	 *
//	 * @param request the request
//	 * @return a ResponseEntity
//	 */
//	@ApiOperation(value = "An endpoint which uses a REST client using Feign to call the remote echo operation.")
//	@RequestMapping(value = URL_PREFIX + "/demoCallEchoUsingFeignClient", method = RequestMethod.GET,
//			produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<EchoHostServiceResponse> demoCallEchoUsingFeignClient(final HttpServletRequest request) {
//
//		// use this in case of feign hystrix to test fallback handler invocation
//		// NOSONAR ConfigurationManager.getConfigInstance().setProperty("hystrix.command.default.circuitBreaker.forceOpen", "true");
//
//		final ResponseEntity<EchoHostServiceResponse> echoResponse = feignEchoClient.echo(); // NOSONAR connot immediately return
//
//		// use this in case of feign hystrix
//		// NOSONAR ConfigurationManager.getConfigInstance().setProperty("hystrix.command.default.circuitBreaker.forceOpen", "false");
//
//		return echoResponse;
//	}
//
//	@ApiOperation(value = "An endpoint which uses a REST client using Feign to call the remote document service operation.")
//	@RequestMapping(value = URL_PREFIX + "/demoCallDocumentServiceFeignClient", method = RequestMethod.GET,
//			produces = MediaType.APPLICATION_JSON_VALUE)
//	public GetDocumentTypesResponse demoCallDocumentServiceUsingFeignClient(final HttpServletRequest request) {
//
//		// use this in case of feign hystrix to test fallback handler invocation
//		// NOSONAR ConfigurationManager.getConfigInstance().setProperty("hystrix.command.default.circuitBreaker.forceOpen", "true");
//
//		final ResponseEntity<GetDocumentTypesResponse> docResponse = feignDocumentClient.getDocumentTypes(); // NOSONAR
//
//		// use this in case of feign hystrix
//		// NOSONAR ConfigurationManager.getConfigInstance().setProperty("hystrix.command.default.circuitBreaker.forceOpen", "false");
//
//		return docResponse;
//	}
//
}
