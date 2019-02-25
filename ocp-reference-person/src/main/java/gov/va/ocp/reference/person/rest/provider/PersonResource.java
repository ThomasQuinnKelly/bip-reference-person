package gov.va.ocp.reference.person.rest.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.MediaType;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.va.ocp.reference.framework.swagger.SwaggerResponseMessages;
import gov.va.ocp.reference.person.api.ReferencePersonService;
import gov.va.ocp.reference.person.model.person.v1.PersonInfoRequest;
import gov.va.ocp.reference.person.model.person.v1.PersonInfoResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController

/**
 * REST Person Service endpoint
 *
 * @author
 *
 */
public class PersonResource implements HealthIndicator, SwaggerResponseMessages {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(PersonResource.class);

	@Autowired
	@Qualifier("IMPL")
	ReferencePersonService refPersonService;

	public static final String URL_PREFIX = "/api/v1/persons";

	// NOSONAR TODO make this method a REST call to test this endpoint is up and running
	@Override
	@RequestMapping(value = URL_PREFIX + "/health", method = RequestMethod.GET)
	@ApiOperation(value = "A health check of this endpoint",
			notes = "Will perform a basic health check to see if the operation is running.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = MESSAGE_200) })
	public Health health() {
		return Health.up().withDetail("Person Service REST Endpoint", "Person Service REST Provider Up and Running!").build();
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
	@RequestMapping(value = URL_PREFIX + "/pid",
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	@ApiOperation(value = "PID based Person Info from Person Partner Service.", notes = "Will return a person info based on PID.")
	public PersonInfoResponse personByPid(@RequestBody final PersonInfoRequest personInfoRequest) {
		return refPersonService.findPersonByParticipantID(personInfoRequest);
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
