package gov.va.os.reference.service.rest.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.va.os.reference.framework.swagger.SwaggerResponseMessages;
import gov.va.os.reference.partner.person.ws.client.transfer.PersonInfoRequest;
import gov.va.os.reference.partner.person.ws.client.transfer.PersonInfoResponse;
import gov.va.os.reference.service.api.DemoPersonService;
import gov.va.os.reference.service.api.DemoService;
import gov.va.os.reference.service.api.v1.transfer.DemoServiceResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController

/**
 * REST Demo Service endpoint
 *
 * @author
 *
 */
public class DemoServiceEndpoint implements HealthIndicator, SwaggerResponseMessages {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(DemoServiceEndpoint.class);

	@Autowired
	@Qualifier("IMPL")
	DemoService demoService;

	@Autowired
	@Qualifier("IMPL")
	DemoPersonService demoPersonService;

	public static final String URL_PREFIX = "/service-1/v1";

	// NOSONAR TODO make this method a REST call to test this endpoint is up and running
	@Override
	@RequestMapping(value = URL_PREFIX + "/health", method = RequestMethod.GET)
	@ApiOperation(value = "A health check of this endpoint",
			notes = "Will perform a basic health check to see if the operation is running.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = MESSAGE_200) })
	public Health health() {
		return Health.up().withDetail("Demo Service REST Endpoint", "Demo Service REST Provider Up and Running!").build();
	}


	@RequestMapping(value = URL_PREFIX + "/read/{name}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	@ApiOperation(value = "Reads a DEMO.", notes = "Will retrieve and return a previously created DEMO entity.")
	public ResponseEntity<DemoServiceResponse> read(@PathVariable final String name) {
		return new ResponseEntity<>(demoService.read(name), HttpStatus.OK);
	}

	/**
	 * CODING PRACTICE FOR RETURN TYPES - Platform auditing aspects support two return types.
	 * 1) An object derived from ServiceResponse. For Ex: PersonInfoResponse as returned below.
	 * 2) An object derived from ServiceResponse wrapped inside ResponseEntity.
	 * The auditing aspect won't be triggered if the return type in not one of the above.
	 *
	 * @param personInfoRequest the person info request
	 * @return the person info response
	 */
	@RequestMapping(value = URL_PREFIX + "/person/pid",
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	@ApiOperation(value = "PID based Person Info from DEMO Partner Service.", notes = "Will return a person info based on PID.")
	public PersonInfoResponse personByPid(@RequestBody final PersonInfoRequest personInfoRequest) {
		return demoPersonService.findPersonByParticipantID(personInfoRequest);
	}

	/**
	 * Registers fields that should be allowed for data binding.
	 *
	 * @param binder
	 *            Spring-provided data binding context object.
	 */
	@InitBinder
	public void initBinder(final WebDataBinder binder) {
		binder.setAllowedFields(new String[] { "personInfo", "firstName", "lastName", "middleName", "fileNumber", "participantId",
				"ssn" });
	}

}
