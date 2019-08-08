package gov.va.bip.reference.person;

import java.io.IOException;
import java.time.LocalDate;

import gov.va.bip.reference.person.model.PersonByPidDomainRequest;
import gov.va.bip.reference.person.model.PersonByPidDomainResponse;

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
	 * @param pid
	 * 
	 * @return the file as a byte array
	 * @throws IOException
	 */
	byte[] getMetadataDocumentForPid(Long pid) throws IOException;

	/**
	 * store the meta-data associated with the document for a given pid
	 * 
	 * @param pid the pid
	 * @param documentName the name of the document
	 * @param creationDate the date of creation of the document
	 * 
	 * @return the file as a byte array
	 */
	void storeMetadata(Long pid, String documentName, LocalDate creationDate);
}
