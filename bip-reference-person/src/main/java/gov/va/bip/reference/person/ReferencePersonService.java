package gov.va.bip.reference.person;

import java.io.IOException;

import gov.va.bip.reference.person.api.model.v1.PersonDocumentMetadata;
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
	 * get the document associated with the pid
	 * 
	 * @param pid
	 * 
	 * @return the file as a byte array
	 * @throws IOException
	 */
	byte[] getDocument(Long pid) throws IOException;

	/**
	 * store the meta-data associated with the document for a given pid
	 * 
	 * @param pid
	 * 
	 * @return the file as a byte array
	 */
	void storeMetadata(Long pid, PersonDocumentMetadata personDocumentMetadata);
}
