package gov.va.bip.reference.person;

import java.io.IOException;

import gov.va.bip.framework.exception.BipException;
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
	 * @param domainRequest
	 * 
	 * @return the file as a byte array
	 * @throws IOException when file cannot be written into
	 * @throws BipException when there is problem with fetching docuement for a given pid
	 */
	PersonDocumentMetadataDomainResponse getMetadataForPid(PersonDocumentMetadataDomainRequest domainRequest);

	/**
	 * store the meta-data associated with the document for a given pid
	 * 
	 * @param pid the pid
	 * @param documentName the name of the document
	 * @param documentCreationDate the date of creation of the document
	 * 
	 */
	void storeMetadata(Long pid, String documentName, String documentCreationDate);

	byte[] getSampleReferenceDocument();
}
