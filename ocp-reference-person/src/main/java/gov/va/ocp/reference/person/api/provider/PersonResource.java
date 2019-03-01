package gov.va.ocp.reference.person.api.provider;

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
import gov.va.ocp.reference.person.model.PersonByPidDomainRequest;
import gov.va.ocp.reference.person.model.PersonByPidDomainResponse;
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

	private static final Logger LOGGER = LoggerFactory.getLogger(PersonResource.class);

	/** The service layer API contract for processing personByPid() requests */
	@Autowired
	@Qualifier("IMPL")
	ReferencePersonService refPersonService;

	/** The root path to this resource */
	public static final String URL_PREFIX = "/api/v1/persons";

	/**
	 * A REST call to test this endpoint is up and running.
	 *
	 * @see org.springframework.boot.actuate.health.HealthIndicator#health()
	 */
	@Override
	@RequestMapping(value = URL_PREFIX + "/health", method = RequestMethod.GET)
	@ApiOperation(value = "A health check of this endpoint",
			notes = "Will perform a basic health check to see if the operation is running.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = MESSAGE_200) })
	public Health health() {
		return Health.up().withDetail("Reference Person Service REST Endpoint", "Person Service REST Provider Up and Running!")
				.build();
	}

	/**
	 * Search for Person Information by their participant ID.
	 * <p>
	 * CODING PRACTICE FOR RETURN TYPES - Platform auditing aspects support two return types.
	 * <br/>
	 * 1) An object derived from DomainResponse. For Ex: PersonByPidDomainResponse as returned below.
	 * <br/>
	 * 2) An object derived from DomainResponse wrapped inside ResponseEntity.
	 * <br/>
	 * The auditing aspect won't be triggered if the return type in not one of the above.
	 *
	 * @param personByPidDomainRequest the person info request
	 * @return the person info response
	 */
	@RequestMapping(value = URL_PREFIX + "/pid",
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	@ApiOperation(value = "Retrieve person information by PID from Person Service .",
			notes = "Will return a person info object based on search by PID.")
	public PersonByPidDomainResponse personByPid(@RequestBody final PersonByPidDomainRequest personByPidDomainRequest) {
		LOGGER.debug("personByPid() method invoked");
		return refPersonService.findPersonByParticipantID(personByPidDomainRequest);
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
