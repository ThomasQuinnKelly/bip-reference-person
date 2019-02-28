package gov.va.ocp.reference.person.ws.client.validate;

import gov.va.ocp.reference.framework.util.Defense;
import gov.va.ocp.reference.person.model.person.v1.PersonInfoRequest;

/**
 * Validation class used to validate request and response
 * objects to/from client invocation
 *
 * @author Vaanapalliv
 */
public class PersonDomainValidator {

	/**
	 * Validates {@link PersonInfoRequest} and {@link PersonInfoRequest#getParticipantID()}
	 * for {@code null} and participantID is greater than zero.
	 *
	 * @param request
	 * @throws IllegalArgumentException if validation failed
	 */
	public static void validatePersonInfoRequest(PersonInfoRequest request) {
		Defense.notNull(request, "PersonInfoRequest cannot be null");
		Defense.notNull(request.getParticipantID(), "PersonInfoRequest.participantID cannot be null");
		Defense.isTrue(request.getParticipantID() > 0, "PersonInfoRequest.participantID cannot be zero");
	}
}
