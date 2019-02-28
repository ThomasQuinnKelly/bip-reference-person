package gov.va.ocp.reference.person.api;

import gov.va.ocp.reference.person.model.person.v1.PersonInfoRequest;
import gov.va.ocp.reference.person.model.person.v1.PersonInfoResponse;

/**
 * The contract interface for the Person service
 *
 * @author aburkholder
 */
public interface ReferencePersonService {
	/**
	 * Searches for the person info by their Participant ID.
	 *
	 * @param personInfoRequest A PersonInfoRequest instance
	 * @return A PersonInfoResponse instance
	 */
	PersonInfoResponse findPersonByParticipantID(PersonInfoRequest personInfoRequest);
}
