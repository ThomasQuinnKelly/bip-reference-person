package gov.va.bip.reference.person.api.provider;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import gov.va.bip.framework.log.BipLogger;
import gov.va.bip.framework.log.BipLoggerFactory;
import gov.va.bip.framework.swagger.SwaggerResponseMessages;
import gov.va.bip.reference.person.api.ReferencePersonApi;
import gov.va.bip.reference.person.api.model.v1.PersonInfoRequest;
import gov.va.bip.reference.person.api.model.v1.PersonInfoResponse;
import io.swagger.annotations.ApiParam;

/**
 * REST Person Service endpoint
 *
 * @author akulkarni
 *
 */
@RestController
public class PersonResource implements ReferencePersonApi, SwaggerResponseMessages {

	/** Logger instance */
	private static final BipLogger LOGGER = BipLoggerFactory.getLogger(PersonResource.class);

	/** The root path to this resource */
	public static final String URL_PREFIX = "/api/v1/persons";

	@Autowired
	ServiceAdapter serviceAdapter;

	@Autowired
	BuildProperties buildProperties;

	@PostConstruct
	public void postConstruct() {
		// Print build properties
		LOGGER.info(buildProperties.getName());
		LOGGER.info(buildProperties.getVersion());
		LOGGER.info(buildProperties.getArtifact());
		LOGGER.info(buildProperties.getGroup());
	}

	/**
	 * Registers fields that should be allowed for data binding.
	 *
	 * @param binder
	 *            Spring-provided data binding context object.
	 */
	@InitBinder
	public void initBinder(final WebDataBinder binder) {
		binder.setAllowedFields(new String[] { "personInfo", "firstName", "lastName", "middleName", "fileNumber",
				"participantId", "ssn" });
	}

	/**
	 * Search for Person Information by their participant ID.
	 * <p>
	 * CODING PRACTICE FOR RETURN TYPES - Platform auditing aspects support two
	 * return types.
	 * <ol>
	 * <li>An object that implements ProviderTransferObjectMarker, e.g.:
	 * PersonInfoResponse
	 * <li>An object of type ResponseEntity&lt;ProviderTransferObjectMarker&gt;,
	 * e.g. a ResponseEntity that wraps some class that implements
	 * ProviderTransferObjectMarker.
	 * </ol>
	 * The auditing aspect won't be triggered if the return type in not one of
	 * the above.
	 *
	 * @param personInfoRequest
	 *            the person info request
	 * @return the person info response
	 */
	@Override
	public ResponseEntity<PersonInfoResponse> personByPid(
			@ApiParam(value = "personInfoRequest", required = true) @Valid @RequestBody final PersonInfoRequest personInfoRequest) {
		LOGGER.debug("personByPid() method invoked");

		PersonInfoResponse providerResponse = serviceAdapter.personByPid(personInfoRequest);
		// send provider response back to consumer
		LOGGER.debug("Returning providerResponse to consumer");
		return new ResponseEntity<>(providerResponse, HttpStatus.OK);
	}

}
