package gov.va.bip.reference.person;

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
	 * Upload given document to the database in the same record as the pid
	 * 
	 * @param pid the Person id number
	 * @param file the file to be uploaded
	 */
	void uploadDocument(long pid, byte[] file);

	/**
	 * get the document associated with the pid
	 * 
	 * @param pid
	 * 
	 * @return the file as a byte array
	 */
	byte[] getDocument(Long pid);
}
