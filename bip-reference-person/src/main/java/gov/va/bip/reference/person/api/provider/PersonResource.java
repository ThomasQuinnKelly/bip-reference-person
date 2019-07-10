package gov.va.bip.reference.person.api.provider;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import gov.va.bip.framework.log.BipLogger;
import gov.va.bip.framework.log.BipLoggerFactory;
import gov.va.bip.framework.rest.provider.ProviderResponse;
import gov.va.bip.framework.swagger.SwaggerResponseMessages;
import gov.va.bip.reference.person.api.ReferencePersonApi;
import gov.va.bip.reference.person.api.model.v1.PersonInfoRequest;
import gov.va.bip.reference.person.api.model.v1.PersonInfoResponse;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

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

	// /**
	// * Returns the person data stored in database for a given person pid.
	// *
	// * @param pid the pid
	// * @return PersonStoredDataResponse
	// */
	// @RequestMapping(value = URL_PREFIX + "/storedDataByPid", produces = MediaType.APPLICATION_JSON_VALUE, method =
	// RequestMethod.GET)
	// @ApiOperation(value = "Retrieve person data stored from database by pid from person stored data Service",
	// notes = "Will return a person stored data Response based on search by pid.")
	// public gov.va.bip.reference.person.model.v1.PersonStoredDataResponse getPersonStoredDataByPid(
	// @Valid @RequestBody final gov.va.bip.reference.person.model.v1.PersonStoredDataRequest personInfoRequest) {
	// LOGGER.debug("getPersonStoredDataByPid() method invoked");
	//
	// gov.va.bip.reference.person.model.v1.PersonStoredDataResponse providerResponse =
	// serviceAdapter.personStoredDataByPid(personInfoRequest);
	//
	// LOGGER.debug("Returning PersonStoredDataResponse to consumer");
	// return providerResponse;
	// }

	/**
	 * accepts the document and stores in database for a given person pid.
	 *
	 * @param pid the pid
	 * @return ProviderResponse
	 */
	// @RequestMapping(path = URL_PREFIX + "/personDocument/uploadByPid/{pid}", method = RequestMethod.POST,
	// consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	// @ApiOperation(value = "Submit a Binary Document", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE,
	// produces = MediaType.APPLICATION_JSON_VALUE)
	@Override
	public ResponseEntity<ProviderResponse> submit(
			@ApiParam(value = "participant id", required = true) @PathVariable("pid") final String pid,
			@ApiParam(value = "byteFile", required = true) @Valid @RequestBody final Resource body) {
		byte[] b = null;
		try {
			body.getInputStream().read(b);
		} catch (IOException e) {
			LOGGER.error("Could not read body", e);
			new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		ProviderResponse docResponse = serviceAdapter.uploadDocumentForPid(Long.valueOf(pid), b);
		return new ResponseEntity<>(docResponse, HttpStatus.OK);
	}

	@RequestMapping(value = URL_PREFIX + "/personDocument/uploadByPid", method = RequestMethod.POST)
	@ApiOperation(value = "Will return a document doperson stored data Response based on search by pid.",
	notes = "Will perform a basic health check to see if the operation is running.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = MESSAGE_200) })
	public ProviderResponse upload(@RequestHeader final HttpHeaders headers,
			@ApiParam(value = "Document to upload", required = true) @RequestPart("file") final MultipartFile file,
			@ApiParam(value = "corresponding pid to upload to", required = true) @RequestPart("pid") final Long pid) {
		ProviderResponse response = new ProviderResponse();
		try {
			response = serviceAdapter.uploadDocumentForPid(pid, file.getBytes());
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return response;
	}

	// @RequestMapping(value = URL_PREFIX + "/person/modify/{claimId}", method = RequestMethod.POST)
	// @ApiOperation(value = "Modify claim-type value in claim attributes",
	// notes = "Will perform a basic health check to see if the operation is running.")
	// @ApiResponses(value = { @ApiResponse(code = 200, message = MESSAGE_200) })
	// public ProviderResponse modifyClaim(@PathVariable("claimId") final String claimId, @RequestHeader final HttpHeaders headers,
	// @ApiParam(value = "New claim type to change to", required = true) @RequestBody final String claimType) {
	// ProviderResponse response = serviceAdapter.modifyClaim(Long.valueOf(claimId), claimType);
	// return response;
	// }

	// @RequestMapping(value = URL_PREFIX + "/downloadFileForPid/{pid}", method = RequestMethod.GET)
	// @ApiOperation(value = "download a given file from database for the given pid",
	// notes = "Will perform a basic health check to see if the operation is running.")
	// @ApiResponses(value = { @ApiResponse(code = 200, message = MESSAGE_200) })
	// public ResponseEntity<Resource> downloadFile(@PathVariable("pid") final String pid, final HttpServletRequest request) {
	// // Load file as Resource
	// Resource resource = new ByteArrayResource(serviceAdapter.getDocumentForPid(Long.valueOf(pid)));
	//
	// // Try to determine file's content type
	// String contentType = null;
	// try {
	// contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
	// } catch (IOException ex) {
	// LOGGER.info("Could not determine file type.");
	// }
	//
	// // Fallback to the default content type if type could not be determined
	// if (contentType == null) {
	// contentType = "application/octet-stream";
	// }
	//
	// return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
	// .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
	// }

	// @RequestMapping(method = { RequestMethod.GET }, value = URL_PREFIX + "/downloadMultipartFile/{pid}",
	// produces = MediaType.MULTIPART_FORM_DATA_VALUE)
	// public ResponseEntity<MultiValueMap<String, Object>> getFileInMultipartData(@PathVariable("pid") final String pid) {
	//
	// JsonArray ja = new JsonArray();
	// ja.add(pid);
	// MultiValueMap<String, Object> mpr = new LinkedMultiValueMap<String, Object>();
	// HttpHeaders xHeader = new HttpHeaders();
	// xHeader.setContentType(MediaType.APPLICATION_JSON);
	// HttpEntity<String> xPart = new HttpEntity<String>(ja.toString(), xHeader);
	// mpr.add("response", xPart);
	// Resource resource = new ByteArrayResource(serviceAdapter.getDocumentForPid(Long.valueOf(pid)));
	// mpr.add("file", resource);
	// return new ResponseEntity<MultiValueMap<String, Object>>(mpr, HttpStatus.OK);
	// }

}
