package gov.va.bip.reference.person.api.provider;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import gov.va.bip.framework.log.BipLogger;
import gov.va.bip.framework.log.BipLoggerFactory;
import gov.va.bip.framework.messages.MessageKeys;
import gov.va.bip.framework.messages.MessageSeverity;
import gov.va.bip.framework.rest.provider.ProviderResponse;
import gov.va.bip.framework.swagger.SwaggerResponseMessages;
import gov.va.bip.reference.person.api.ReferencePersonApi;
import gov.va.bip.reference.person.api.model.v1.PersonDocumentMetadataResponse;
import gov.va.bip.reference.person.api.model.v1.PersonInfoRequest;
import gov.va.bip.reference.person.api.model.v1.PersonInfoResponse;
import gov.va.bip.reference.person.exception.PersonServiceException;
import gov.va.bip.reference.person.messages.PersonMessageKeys;
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
	 * @return ProviderResponse
	 */
	@Override
	public ResponseEntity<gov.va.bip.framework.rest.provider.ProviderResponse> upload(
			@Min(1L) @ApiParam(value = "participant id", required = true) @PathVariable("pid") final Long pid,
			@ApiParam(value = "The name of the document", required = true) @RequestParam(value = "documentName",
			required = true) final String documentName,
			@ApiParam(value = "file detail") @Valid @RequestPart("file") final MultipartFile file,
			@ApiParam(value = "The date of creation of the document in YYYY-MM-DD format") @RequestParam(
					value = "documentCreationDate", required = false) final String documentCreationDate) {
		LOGGER.debug("upload() method invoked");
		ProviderResponse response = new ProviderResponse();
		try {
			response = serviceAdapter.storeMetaData(Long.valueOf(pid), documentName, documentCreationDate);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("Upload failed due to unexpected exception", e);
		}
		LOGGER.debug("Returning providerResponse to consumer");
		return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
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
		// Load file as Resource
		LOGGER.debug("downloadSampleDocument() method invoked");

		byte[] docBytes;
		try {
			docBytes = serviceAdapter.getSampleReferenceDocument();
			if ((docBytes == null) || (docBytes.length == 0)) {
				throw new PersonServiceException(PersonMessageKeys.BIP_PERSON_NO_DOCUMENT_DOWNLOAD,
						MessageSeverity.WARN, HttpStatus.NO_CONTENT);
			} else {
				Resource resource = new ByteArrayResource(docBytes);
				return ResponseEntity.ok()
						.contentType(MediaType.parseMediaType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
						.header(HttpHeaders.CONTENT_DISPOSITION,
								"attachment; filename=\"" + resource.getFilename() + "\"")
						.body(resource);
			}
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
	 * @param pid the pid
	 * 
	 * @return the person info response
	 */
	public ResponseEntity<PersonDocumentMetadataResponse>
	getDocumentMetadata(@Min(1L) @ApiParam(value = "participant id", required = true) @PathVariable("pid") final Long pid) {
		LOGGER.debug("getDocumentMetadata() method invoked");

		PersonDocumentMetadataResponse providerResponse = serviceAdapter.getMetadataDocumentForPid(pid);

		// send provider response back to consumer
		LOGGER.debug("Returning providerResponse to consumer");
		return new ResponseEntity<>(providerResponse, HttpStatus.OK);
	}

}
