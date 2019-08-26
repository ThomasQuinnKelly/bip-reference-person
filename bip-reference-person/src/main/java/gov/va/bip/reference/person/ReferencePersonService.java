package gov.va.bip.reference.person;

import org.springframework.core.io.Resource;

import gov.va.bip.reference.person.model.PersonByPidDomainRequest;
import gov.va.bip.reference.person.model.PersonByPidDomainResponse;
import gov.va.bip.reference.person.model.PersonDocumentMetadataDomainRequest;
import gov.va.bip.reference.person.model.PersonDocumentMetadataDomainResponse;

/**
 * The contract interface for the Person domain (service) layer.
 *
 * @author aburkholder
 */
public interface ReferencePersonService {
	/**
	 * Search for the person info by their Participant ID.
	 *
	 * @param personByPidDomainRequest A PersonByPidDomainRequest instance
	 * @return A PersonByPidDomainResponse instance
	 */
	PersonByPidDomainResponse findPersonByParticipantID(PersonByPidDomainRequest personByPidDomainRequest);

	/**
	 * Get the meta data associated with documents accepted for a pid
	 * 
	 * @param domainRequest the object containing information required for fetching metadata
	 * 
	 * @return the PersonDocumentMetadataDomainResponse object containing data associated with a pid
	 */
	PersonDocumentMetadataDomainResponse getMetadataForPid(PersonDocumentMetadataDomainRequest domainRequest);

	/**
	 * Store the meta-data associated with the document for a given pid
	 * 
	 * @param pid the pid
	 * @param documentName the name of the document
	 * @param documentCreationDate the date of creation of the document
	 */
	void storeMetadata(Long pid, String documentName, String documentCreationDate);

	/**
	 * Get a static document as a byte array containing some sample reference data
	 * 
	 * @return the file as a resource
	 */
	Resource getSampleReferenceDocument();
}
