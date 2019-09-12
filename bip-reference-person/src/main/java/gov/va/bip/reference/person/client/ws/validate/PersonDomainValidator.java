package gov.va.bip.reference.person.client.ws.validate;

import gov.va.bip.framework.validation.Defense;
import gov.va.bip.reference.person.model.PersonByPidDomainRequest;

/**
 * Validation class used to validate request and response
 * objects to/from client invocation
 *
 * @author Vaanapalliv
 */
public class PersonDomainValidator {

	/**
	 * Do not instantiate.
	 */
	private PersonDomainValidator() {
		throw new IllegalAccessError("PersonDomainValidator is a static class. Do not instantiate it.");
	}

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
