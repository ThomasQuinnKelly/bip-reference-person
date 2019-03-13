package gov.va.ocp.reference.person.api.model.v1.validators;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import gov.va.ocp.framework.messages.MessageSeverity;
import gov.va.ocp.framework.messages.ServiceMessage;
import gov.va.ocp.framework.validation.Validator;
import gov.va.ocp.reference.person.model.PersonByPidDomainRequest;

/**
 * A service (domain) business {@link Validator} for validating {@link PersonByPidDomainRequest} objects.
 *
 * @author aburkholder
 */
public class PersonInfoRequestValidator implements Validator<PersonByPidDomainRequest> {

	/** The method that caused the validator to be executed */
	private Method callingMethod;

	/**
	 * Create a service (domain) business {@link Validator} for validating {@link PersonByPidDomainRequest} objects.
	 */
	public PersonInfoRequestValidator() {
	}

	@Override
	public Class<PersonByPidDomainRequest> getValidatedType() {
		return PersonByPidDomainRequest.class;
	}

	@Override
	public void validate(Object toValidate, List<ServiceMessage> messages) {
		if (messages == null) {
			messages = new ArrayList<>();
		}

		String callingMethodName = callingMethod == null ? ""
				: callingMethod.getDeclaringClass().getSimpleName()
						+ "." + callingMethod.getName() + ": ";

		// request-level null check
		if (toValidate == null) {
			messages.add(new ServiceMessage(MessageSeverity.ERROR, "", callingMethodName + "Request cannot be null.",
					HttpStatus.BAD_REQUEST));
			return;
		}

		// check class is correct
		if (!getValidatedType().isAssignableFrom(toValidate.getClass())) {
			messages.add(new ServiceMessage(MessageSeverity.ERROR, "",
					callingMethodName + "Request is not of type " + getValidatedType().getName(),
					HttpStatus.BAD_REQUEST));
			return;
		}

		// validate the request content (PID)
		Long pid = ((PersonByPidDomainRequest) toValidate).getParticipantID();
		if (pid == null) {
			messages.add(new ServiceMessage(MessageSeverity.ERROR, "", callingMethodName + "Participant ID cannot be null.",
					HttpStatus.BAD_REQUEST));
		} else if (pid <= 0) {
			messages.add(new ServiceMessage(MessageSeverity.ERROR, "", callingMethodName + "Participant ID must be greater than zero.",
					HttpStatus.BAD_REQUEST));
		}
	}

	@Override
	public void setCallingMethod(Method callingMethod) {
		this.callingMethod = callingMethod;
	}

	@Override
	public Method getCallingMethod() {
		return this.callingMethod;
	}
}
