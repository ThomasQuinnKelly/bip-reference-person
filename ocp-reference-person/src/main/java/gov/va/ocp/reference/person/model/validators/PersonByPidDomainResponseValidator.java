package gov.va.ocp.reference.person.model.validators;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import gov.va.ocp.framework.log.OcpLogger;
import gov.va.ocp.framework.log.OcpLoggerFactory;
import gov.va.ocp.framework.messages.MessageSeverity;
import gov.va.ocp.framework.messages.ServiceMessage;
import gov.va.ocp.framework.security.PersonTraits;
import gov.va.ocp.framework.security.SecurityUtils;
import gov.va.ocp.framework.validation.AbstractStandardValidator;
import gov.va.ocp.reference.person.exception.PersonServiceException;
import gov.va.ocp.reference.person.messages.PersonMessageKeys;
import gov.va.ocp.reference.person.model.PersonByPidDomainRequest;
import gov.va.ocp.reference.person.model.PersonByPidDomainResponse;

public class PersonByPidDomainResponseValidator extends AbstractStandardValidator<PersonByPidDomainResponse> {

	/** Class logger */
	private static final OcpLogger LOGGER = OcpLoggerFactory.getLogger(PersonByPidDomainResponseValidator.class);

	/** For the message when hystrix fallback method is manually invoked */
//	private static final String INVOKE_FALLBACK_MESSAGE = "Could not get data from cache or partner.";

	/** Reference-specific warning that a real service would handle PID requests better */
	private static final String WARN_MESSAGE =
			"In a real service, this condition should throw a service exception (in this case, PersonServiceException) with INVOKE_FALLBACK_MESSAGE.";

	/** The method that caused this validator to be invoked */
	private Method callingMethod;

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.va.ocp.framework.validation.AbstractStandardValidator#validate(java.lang.Object, java.util.List)
	 */
	@Override
	public void validate(PersonByPidDomainResponse toValidate, List<ServiceMessage> messages) {
		Object supplemental = getSupplemental(PersonByPidDomainRequest.class);
		PersonByPidDomainRequest request =
				supplemental == null ? new PersonByPidDomainRequest() : (PersonByPidDomainRequest) supplemental;

		// if response has errors, fatals or warnings skip validations
		if (toValidate.hasErrors()
				|| toValidate.hasFatals()
				|| toValidate.hasWarnings()) {
			return;
		}
		// check if empty response, or errors / fatals
		if (toValidate == null || toValidate.getPersonInfo() == null) {
			PersonMessageKeys key = PersonMessageKeys.OCP_PERSON_INFO_REQUEST_NOTNULL;
			LOGGER.info(key.getKey() + " " + key.getMessage());
			throw new PersonServiceException(key, MessageSeverity.FATAL, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		/*
		 * In a real-world service, it is highly unlikely that a user would be allowed
		 * to query for someone else's data. In general, responses should *always*
		 * contain only data for the logged-in person.
		 * Therefore, the checks below would typically throw an exception,
		 * not just set a warning.
		 */
		LOGGER.debug("Request PID: " + request.getParticipantID()
				+ "; Response PID: " + toValidate.getPersonInfo().getParticipantId()
				+ "; PersonTraits PID: "
				+ (SecurityUtils.getPersonTraits() == null ? "null" : SecurityUtils.getPersonTraits().getPid()));

		// check requested pid = returned pid
		if (!toValidate.getPersonInfo().getParticipantId().equals(request.getParticipantID())) {
			PersonMessageKeys key = PersonMessageKeys.OCP_PERSON_INFO_REQUEST_PID_INCONSISTENT;

			LOGGER.info(key.getKey() + " " + key.getMessage());
			toValidate.addMessage(MessageSeverity.WARN, HttpStatus.OK, key);
		}
		// check logged in user's pid matches returned pid
		PersonTraits personTraits = SecurityUtils.getPersonTraits();
		if (personTraits != null && StringUtils.isNotBlank(personTraits.getPid())) {
			if (toValidate.getPersonInfo() != null
					&& toValidate.getPersonInfo().getParticipantId() != null
					&& !personTraits.getPid().equals(toValidate.getPersonInfo().getParticipantId().toString())) {

				PersonMessageKeys key = PersonMessageKeys.OCP_PERSON_INFO_REQUEST_PID_INVALID;
				LOGGER.info(key.getKey() + " " + key.getMessage());
				toValidate.addMessage(MessageSeverity.WARN, HttpStatus.OK, key);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.va.ocp.framework.validation.AbstractStandardValidator#setCallingMethod(java.lang.reflect.Method)
	 */
	@Override
	public void setCallingMethod(Method callingMethod) {
		this.callingMethod = callingMethod;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.va.ocp.framework.validation.AbstractStandardValidator#getCallingMethod()
	 */
	@Override
	public Method getCallingMethod() {
		return this.callingMethod;
	}
}
