package gov.va.ocp.reference.person.api;

import gov.va.ocp.reference.person.model.person.v1.PersonInfoRequest;
import gov.va.ocp.reference.person.model.person.v1.PersonInfoResponse;

public interface ReferencePersonService {
	/**
	 * Gets the person info.
	 *
	 * @param personInfoRequest A PersonInfoRequest instance
	 * @return A PersonInfoResponse instance
	 */
	PersonInfoResponse findPersonByParticipantID(PersonInfoRequest personInfoRequest);
}
