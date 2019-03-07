package gov.va.ocp.reference.person;

import gov.va.ocp.reference.person.model.PersonByPidDomainRequest;
import gov.va.ocp.reference.person.model.PersonByPidDomainResponse;

/**
 * The contract interface for the Person service
 *
 * @author aburkholder
 */
public interface ReferencePersonService {
	/**
	 * Searches for the person info by their Participant ID.
	 *
	 * @param personByPidDomainRequest A PersonByPidDomainRequest instance
	 * @return A PersonByPidDomainResponse instance
	 */
	PersonByPidDomainResponse findPersonByParticipantID(PersonByPidDomainRequest personByPidDomainRequest);
}
