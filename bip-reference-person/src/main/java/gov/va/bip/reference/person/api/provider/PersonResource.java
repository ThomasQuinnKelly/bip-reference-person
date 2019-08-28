package gov.va.bip.reference.person.api.provider;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import gov.va.bip.framework.log.BipLogger;
import gov.va.bip.framework.log.BipLoggerFactory;
import gov.va.bip.framework.messages.MessageKeys;
import gov.va.bip.framework.messages.MessageSeverity;
import gov.va.bip.framework.swagger.SwaggerResponseMessages;
import gov.va.bip.reference.person.api.ReferencePersonApi;
import gov.va.bip.reference.person.api.model.v1.PersonDocsMetadataResponse;
import gov.va.bip.reference.person.api.model.v1.PersonDocsMetadataUploadResponse;
import gov.va.bip.reference.person.api.model.v1.PersonInfoRequest;
import gov.va.bip.reference.person.api.model.v1.PersonInfoResponse;
import gov.va.bip.reference.person.exception.PersonServiceException;
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

	/**
	 * Get the statically stored sample reference document
	 *
	 * <p>
	 * CODING PRACTICE FOR RETURN TYPES - Platform auditing aspects support two return types.
	 * <ol>
	 * <li>An object that implements ProviderTransferObjectMarker, e.g.: PersonInfoResponse
	 * <li>An object of type ResponseEntity&lt;ProviderTransferObjectMarker&gt;, e.g. a ResponseEntity that wraps some class that
	 * implements ProviderTransferObjectMarker.
	 * </ol>
	 * The auditing aspect won't be triggered if the return type in not one of the above.
	 *
	 * @return the document resource to be downloaded
	 */
	@Override
	public ResponseEntity<Resource> downloadSampleDocument() {
		LOGGER.debug("downloadSampleDocument() method invoked");

		try {
			Resource resource = serviceAdapter.getSampleReferenceDocument();

			// send provider response back to consumer
			LOGGER.debug("Returning providerResponse to consumer");
			return ResponseEntity.ok()
					.contentType(MediaType.parseMediaType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
					.header(HttpHeaders.CONTENT_DISPOSITION,
							"attachment; filename=\"" + resource.getFilename() + "\"")
					.body(resource);
		} catch (PersonServiceException e) {
			throw e;
		} catch (Exception e) {
			LOGGER.error("Download failed due to unexpected exception", e);
			throw new PersonServiceException(MessageKeys.NO_KEY, MessageSeverity.ERROR,
					HttpStatus.INTERNAL_SERVER_ERROR, e);
		}
	}

	/**
	 * Get metadata sent along with uploaded document by their participant ID.
	 * <p>
	 * CODING PRACTICE FOR RETURN TYPES - Platform auditing aspects support two return types.
	 * <ol>
	 * <li>An object that implements ProviderTransferObjectMarker, e.g.: PersonInfoResponse
	 * <li>An object of type ResponseEntity&lt;ProviderTransferObjectMarker&gt;, e.g. a ResponseEntity that wraps some class that
	 * implements ProviderTransferObjectMarker.
	 * </ol>
	 * The auditing aspect won't be triggered if the return type in not one of the above.
	 *
	 * @param personDocumentMetadataRequest the PersonDocumentMetadataRequest object containing  information required for requesting metadata information
	 * 
	 * @return the PersonDocumentMetadataResponse wrapped by a response entity
	 */
	@Override
	public ResponseEntity<PersonDocsMetadataResponse> getDocumentMetadataForPerson(@Min(1) final Long pid) {
		LOGGER.debug("getDocumentMetadata() method invoked");

		PersonDocsMetadataResponse providerResponse = serviceAdapter.getMetadataDocumentForPid(pid);

		// send provider response back to consumer
		LOGGER.debug("Returning providerResponse to consumer");
		return new ResponseEntity<>(providerResponse, HttpStatus.OK);
	}

	/**
	 * Accept the document and store the metadata supplied in the multi-part request in database for a given person pid. The document
	 * is currently discarded. Storage of the document is not demonstrated.
	 *
	 * <p>
	 * CODING PRACTICE FOR RETURN TYPES - Platform auditing aspects support two return types.
	 * <ol>
	 * <li>An object that implements ProviderTransferObjectMarker, e.g.: PersonInfoResponse
	 * <li>An object of type ResponseEntity&lt;ProviderTransferObjectMarker&gt;, e.g. a ResponseEntity that wraps some class that
	 * implements ProviderTransferObjectMarker.
	 * </ol>
	 * The auditing aspect won't be triggered if the return type in not one of the above.
	 *
	 * @param pid the pid
	 * @param documentName the name of the document
	 * @param file the file uploaded for the pid
	 * @param documentCreationDate the date of creation of the document
	 * 
	 * @return PersonDocumentMetadataResponse
	 */
	@Override
	public ResponseEntity<PersonDocsMetadataUploadResponse> upload(@Min(1) final Long pid, final String documentName,
			@Valid final MultipartFile file,
			final String documentCreationDate) {
		LOGGER.debug("upload() method invoked");
		PersonDocsMetadataUploadResponse providerResponse = new PersonDocsMetadataUploadResponse();
		try {
			providerResponse =
					serviceAdapter.storeMetaData(Long.valueOf(pid), documentName, documentCreationDate, file);
			// send provider response back to consumer
			LOGGER.debug("Returning providerResponse to consumer");
			return new ResponseEntity<>(providerResponse, HttpStatus.OK);
		} catch (PersonServiceException e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("Upload failed due to unexpected exception", e);
			// send provider response back to consumer
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
