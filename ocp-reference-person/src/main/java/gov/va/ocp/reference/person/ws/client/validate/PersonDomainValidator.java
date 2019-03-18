package gov.va.ocp.reference.person.ws.client.validate;

import gov.va.ocp.framework.validation.Defense;
import gov.va.ocp.reference.person.model.PersonByPidDomainRequest;

/**
 * Validation class used to validate request and response
 * objects to/from client invocation
 *
 * @author Vaanapalliv
 */
public class PersonDomainValidator {

	/**
	 * Validates {@link PersonByPidDomainRequest} and {@link PersonByPidDomainRequest#getParticipantID()}
	 * for {@code null} and participantID is greater than zero.
	 *
	 * @param request
	 * @throws IllegalArgumentException if validation failed
	 */
	public static void validatePersonInfoRequest(PersonByPidDomainRequest request) {
		Defense.notNull(request, "PersonByPidDomainRequest cannot be null");
		Defense.notNull(request.getParticipantID(), "PersonByPidDomainRequest.participantID cannot be null");
		Defense.isTrue(request.getParticipantID() > 0, "PersonByPidDomainRequest.participantID cannot be zero");
	}
}
