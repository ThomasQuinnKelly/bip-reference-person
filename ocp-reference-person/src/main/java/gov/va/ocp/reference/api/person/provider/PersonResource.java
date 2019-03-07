package gov.va.ocp.reference.api.person.provider;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

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

import gov.va.ocp.framework.swagger.SwaggerResponseMessages;
import gov.va.ocp.framework.transfer.ProviderTransferObjectMarker;
import gov.va.ocp.framework.util.Defense;
import gov.va.ocp.reference.api.person.ReferencePersonApi;
import gov.va.ocp.reference.api.person.model.v1.PersonInfoRequest;
import gov.va.ocp.reference.api.person.model.v1.PersonInfoResponse;
import gov.va.ocp.reference.person.ReferencePersonService;
import gov.va.ocp.reference.person.model.PersonByPidDomainRequest;
import gov.va.ocp.reference.person.model.PersonByPidDomainResponse;
import gov.va.ocp.reference.person.transform.impl.PersonByPid_DomainToProvider;
import gov.va.ocp.reference.person.transform.impl.PersonByPid_ProviderToDomain;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * REST Person Service endpoint
 *
 * @author
 *
 */
@RestController
public class PersonResource implements ReferencePersonApi, HealthIndicator, SwaggerResponseMessages {

	/** Logger instance */
	private static final Logger LOGGER = LoggerFactory.getLogger(PersonResource.class);

	/** The root path to this resource */
	public static final String URL_PREFIX = "/api/v1/persons";

	/** The service layer API contract for processing personByPid() requests */
	@Autowired
	@Qualifier("PERSON_SERVICE_IMPL")
	private ReferencePersonService refPersonService;

	/** Transform Provider (REST) request to Domain (service) request */
	private PersonByPid_ProviderToDomain personByPidProvider2Domain = new PersonByPid_ProviderToDomain();
	/** Transform Domain (service) response to Provider (REST) response */
	private PersonByPid_DomainToProvider personByPidDomain2Provider = new PersonByPid_DomainToProvider();

	@PostConstruct
	public void postConstruct() {
		Defense.notNull(refPersonService);
		Defense.notNull(personByPidProvider2Domain);
		Defense.notNull(personByPidDomain2Provider);
	}

	/**
	 * A REST call to test this endpoint is up and running.
	 * <p>
	 * This endpoint is NOT intercepted by the standard publicServiceResponseRestMethod aspect
	 * because the return type does not implement {@link ProviderTransferObjectMarker}.
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
	 * <ol>
	 * <li>An object that implements ProviderTransferObjectMarker, e.g.: PersonInfoResponse
	 * <li>An object of type ResponseEntity&lt;ProviderTransferObjectMarker&gt;,
	 * e.g. a ResponseEntity that wraps some class that implements ProviderTransferObjectMarker.
	 * </ol>
	 * The auditing aspect won't be triggered if the return type in not one of the above.
	 *
	 * @param personByPidDomainRequest the person info request
	 * @return the person info response
	 */
	@Override
	@RequestMapping(value = URL_PREFIX + "/pid",
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	@ApiOperation(value = "Retrieve person information by PID from Person Service .",
			notes = "Will return a person info object based on search by PID.")
	public PersonInfoResponse personByPid(@Valid @RequestBody final PersonInfoRequest personInfoRequest) {
		LOGGER.debug("personByPid() method invoked");

		/** TODO move transforms into an aspect */

		// transform provider request into domain request
		LOGGER.debug("Transforming from personInfoRequest to domainRequest");
		PersonByPidDomainRequest domainRequest = personByPidProvider2Domain.convert(personInfoRequest);

		// get domain response from the service (domain) layer
		LOGGER.debug("Calling refPersonService.findPersonByParticipantID");
		PersonByPidDomainResponse domainResponse = refPersonService.findPersonByParticipantID(domainRequest);

		// transform domain response into provider response
		LOGGER.debug("Transforming from domainResponse to providerResponse");
		PersonInfoResponse providerResponse = personByPidDomain2Provider.convert(domainResponse);

		// send provider response back to consumer
		LOGGER.debug("Returning providerResponse to consumer");
		return providerResponse;
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
