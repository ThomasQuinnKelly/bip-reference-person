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
import gov.va.bip.framework.rest.provider.ProviderResponse;
import gov.va.bip.framework.validation.Defense;
import gov.va.bip.reference.person.ReferencePersonService;
import gov.va.bip.reference.person.api.model.v1.PersonDocumentMetadataResponse;
import gov.va.bip.reference.person.api.model.v1.PersonInfoRequest;
import gov.va.bip.reference.person.api.model.v1.PersonInfoResponse;
import gov.va.bip.reference.person.exception.PersonServiceException;
import gov.va.bip.reference.person.model.PersonByPidDomainRequest;
import gov.va.bip.reference.person.model.PersonByPidDomainResponse;
import gov.va.bip.reference.person.model.PersonDocumentMetadataDomainRequest;
import gov.va.bip.reference.person.model.PersonDocumentMetadataDomainResponse;
import gov.va.bip.reference.person.transform.impl.PersonByPid_DomainToProvider;
import gov.va.bip.reference.person.transform.impl.PersonByPid_ProviderToDomain;
import gov.va.bip.reference.person.transform.impl.PersonDocumentMetadata_DomainToProvider;
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
	private PersonByPid_ProviderToDomain personByPidProvider2Domain = new PersonByPid_ProviderToDomain();
	/** Transform Domain (service) response to Provider (REST) response */
	private PersonByPid_DomainToProvider personByPidDomain2Provider = new PersonByPid_DomainToProvider();
	/** Transform Domain (service) response to Provider (REST) response */
	private PersonDocumentMetadata_DomainToProvider personDocumentMetadataDomain2Provider =
			new PersonDocumentMetadata_DomainToProvider();

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
		PersonInfoResponse providerResponse = personByPidDomain2Provider.convert(domainResponse);

		return providerResponse;
	}

	/**
	 * Store meta data for a document for a given pid
	 * 
	 * @param pid the pid
	 * @param documentName the name of the document
	 * @param documentCreationDate the date of creation of the document
	 * @param file
	 * 
	 * @return a ProviderResponse
	 */
	ProviderResponse storeMetaData(final Long pid, String documentName, final String documentCreationDate, @Valid final MultipartFile file) {

		ProviderResponse response = new ProviderResponse();

		try {
			if (StringUtils.isBlank(documentName)) {
				documentName = file.getResource().getFilename();
			}
			refPersonService.storeMetadata(pid, documentName, documentCreationDate);
		} catch (PersonServiceException e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			response.addMessage(MessageSeverity.ERROR, "Unexpected error", "failure message: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
			return response;
		}

		response.addMessage(MessageSeverity.INFO, "success", "file uploaded", HttpStatus.ACCEPTED);
		return response;
	}

	/**
	 * Get the meta data associated with documents accepted for a pid
	 * 
	 * @param pid the pid
	 * @return a PersonDocumentMetadataResponse object with the required metadata
	 */
	PersonDocumentMetadataResponse
	getMetadataDocumentForPid(final @Valid @Min(1) Long pid) {
		// transform provider request into domain request
		LOGGER.debug("Transforming from personDocumentMetadataRequest to domainRequest");
		PersonDocumentMetadataDomainRequest domainRequest = new PersonDocumentMetadataDomainRequest();
		domainRequest.setParticipantID(pid);

		// get domain response from the service (domain) layer
		LOGGER.debug("Calling refPersonService.findPersonByParticipantID");
		PersonDocumentMetadataDomainResponse domainResponse = refPersonService.getMetadataForPid(domainRequest);

		// transform domain response into provider response
		LOGGER.debug("Transforming from domainResponse to providerResponse");
		PersonDocumentMetadataResponse providerResponse = personDocumentMetadataDomain2Provider.convert(domainResponse);

		return providerResponse;
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
