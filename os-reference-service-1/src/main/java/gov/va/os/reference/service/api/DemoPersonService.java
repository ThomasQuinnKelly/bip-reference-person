package gov.va.os.reference.service.api;

import gov.va.os.reference.service.model.person.v1.PersonInfoRequest;
import gov.va.os.reference.service.model.person.v1.PersonInfoResponse;

public interface DemoPersonService {
	/**
	 * Gets the person info.
	 *
	 * @param personInfoRequest A PersonInfoRequest instance
	 * @return A PersonInfoResponse instance
	 */
	PersonInfoResponse findPersonByParticipantID(PersonInfoRequest personInfoRequest);
}
