package gov.va.bip.reference.person.api.provider;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import gov.va.bip.framework.log.BipLogger;
import gov.va.bip.framework.log.BipLoggerFactory;
import gov.va.bip.framework.messages.MessageSeverity;
import gov.va.bip.framework.validation.Defense;
import gov.va.bip.reference.person.ReferencePersonService;
import gov.va.bip.reference.person.api.model.v1.PersonDocsMetadataResponse;
import gov.va.bip.reference.person.api.model.v1.PersonDocsMetadataUploadResponse;
import gov.va.bip.reference.person.api.model.v1.PersonInfoRequest;
import gov.va.bip.reference.person.api.model.v1.PersonInfoResponse;
import gov.va.bip.reference.person.exception.PersonServiceException;
import gov.va.bip.reference.person.model.PersonByPidDomainRequest;
import gov.va.bip.reference.person.model.PersonByPidDomainResponse;
import gov.va.bip.reference.person.model.PersonDocsMetadataDomainRequest;
import gov.va.bip.reference.person.model.PersonDocsMetadataDomainResponse;
import gov.va.bip.reference.person.transform.impl.PersonByPidDomainToProvider;
import gov.va.bip.reference.person.transform.impl.PersonByPidProviderToDomain;
import gov.va.bip.reference.person.transform.impl.PersonDocsMetadataDomainToProvider;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;

/**
 * An adapter between the provider layer api/model, and the services layer interface/model.
 *
 * @author aburkholder
 */
@Component
public class ServiceAdapter {
	/** Class logger */
	private static final BipLogger LOGGER = BipLoggerFactory.getLogger(ServiceAdapter.class);

	/** Transform Provider (REST) request to Domain (service) request */
	private PersonByPidProviderToDomain personByPidProvider2Domain = new PersonByPidProviderToDomain();
	/** Transform Domain (service) response to Provider (REST) response */
	private PersonByPidDomainToProvider personByPidDomain2Provider = new PersonByPidDomainToProvider();
	/** Transform Domain (service) response to Provider (REST) response */
	private PersonDocsMetadataDomainToProvider personDocsMetadataDomain2Provider =
			new PersonDocsMetadataDomainToProvider();

	/** The service layer API contract for processing personByPid() requests */
	@Autowired
	@Qualifier("PERSON_SERVICE_IMPL")
	private ReferencePersonService refPersonService;

	/**
	 * Field defense validations.
	 */
	@PostConstruct
	public void postConstruct() {
		Defense.notNull(refPersonService);
		Defense.notNull(personByPidProvider2Domain);
		Defense.notNull(personByPidDomain2Provider);
	}

	/**
	 * Adapt the personByPid(..) request mapping method to the equivalent service layer method.
	 *
	 * @param personInfoRequest - the Provider layer request model object
	 * @return PersonInfoResponse - the Provider layer response model object
	 */
	PersonInfoResponse personByPid(final PersonInfoRequest personInfoRequest) {
		// transform provider request into domain request
		LOGGER.debug("Transforming from personInfoRequest to domainRequest");
		PersonByPidDomainRequest domainRequest = personByPidProvider2Domain.convert(personInfoRequest);

		// get domain response from the service (domain) layer
		LOGGER.debug("Calling refPersonService.findPersonByParticipantID");
		PersonByPidDomainResponse domainResponse = refPersonService.findPersonByParticipantID(domainRequest);

		// transform domain response into provider response
		LOGGER.debug("Transforming from domainResponse to providerResponse");
		return personByPidDomain2Provider.convert(domainResponse);
	}

	/**
	 * Store meta data for a document for a given pid
	 *
	 * @param pid the pid
	 * @param docName the name of the document
	 * @param docCreateDate the date of creation of the document
	 * @param file
	 *
	 * @return a ProviderResponse
	 */
	PersonDocsMetadataUploadResponse storeMetaData(final Long pid, String docName, final String docCreateDate,
			@Valid final MultipartFile file) {

		PersonDocsMetadataUploadResponse response = new PersonDocsMetadataUploadResponse();

		try {
			if (StringUtils.isBlank(docName)) {
				docName = file.getResource().getFilename();
			}
			refPersonService.storeMetadata(pid, docName, docCreateDate);
		} catch (PersonServiceException e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			response.addMessage(MessageSeverity.ERROR, "Unexpected error", "failure message: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
			return response;
		}

		response.addMessage(MessageSeverity.INFO, "success", "file uploaded", HttpStatus.OK);
		return response;
	}

	/**
	 * Get the meta data associated with documents accepted for a pid
	 *
	 * @param pid the pid
	 * @return a PersonDocsMetadataResponse object with the required metadata
	 */
	PersonDocsMetadataResponse
			getMetadataDocumentForPid(final @Valid @Min(1) Long pid) {
		// transform provider request into domain request
		LOGGER.debug("Transforming from rest input data (only pid in this case) to domainRequest");
		PersonDocsMetadataDomainRequest domainRequest = new PersonDocsMetadataDomainRequest();
		domainRequest.setParticipantID(pid);

		// get domain response from the service (domain) layer
		LOGGER.debug("Calling refPersonService.findPersonByParticipantID");
		PersonDocsMetadataDomainResponse domainResponse = refPersonService.getMetadataForPid(domainRequest);

		// transform domain response into provider response
		LOGGER.debug("Transforming from domainResponse to providerResponse");
		return personDocsMetadataDomain2Provider.convert(domainResponse);
	}

	/**
	 * Get the static document representing a sample reference document
	 *
	 * @return a file as a resource
	 */
	Resource getSampleReferenceDocument() {
		try {
			return refPersonService.getSampleReferenceDocument();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		}
	}

}
