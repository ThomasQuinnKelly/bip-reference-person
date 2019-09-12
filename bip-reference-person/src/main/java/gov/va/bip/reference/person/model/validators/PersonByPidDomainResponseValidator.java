package gov.va.bip.reference.person.model.validators;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import gov.va.bip.framework.log.BipLogger;
import gov.va.bip.framework.log.BipLoggerFactory;
import gov.va.bip.framework.messages.MessageSeverity;
import gov.va.bip.framework.messages.ServiceMessage;
import gov.va.bip.framework.security.PersonTraits;
import gov.va.bip.framework.security.SecurityUtils;
import gov.va.bip.framework.validation.AbstractStandardValidator;
import gov.va.bip.reference.person.exception.PersonServiceException;
import gov.va.bip.reference.person.messages.PersonMessageKeys;
import gov.va.bip.reference.person.model.PersonByPidDomainRequest;
import gov.va.bip.reference.person.model.PersonByPidDomainResponse;

/**
 * Validates the PID input on the {@link PersonByPidDomainResponse}.
 *
 * @see AbstractStandardValidator
 * @author aburkholder
 */
public class PersonByPidDomainResponseValidator extends AbstractStandardValidator<PersonByPidDomainResponse> {

	/** Class logger */
	private static final BipLogger LOGGER = BipLoggerFactory.getLogger(PersonByPidDomainResponseValidator.class);

	/** The method that caused this validator to be invoked */
	private Method callingMethod;

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.va.bip.framework.validation.AbstractStandardValidator#validate(java.lang.Object, java.util.List)
	 */
	@Override
	public void validate(PersonByPidDomainResponse toValidate, List<ServiceMessage> messages) {
		Object supplemental = getSupplemental(PersonByPidDomainRequest.class);
		PersonByPidDomainRequest request =
				supplemental == null ? new PersonByPidDomainRequest() : (PersonByPidDomainRequest) supplemental;

		// if response has errors, fatals or warnings skip validations
		if (toValidate != null
				&& (toValidate.hasErrors() || toValidate.hasFatals() || toValidate.hasWarnings())) {
			return;
		}
		// check if empty response, or errors / fatals
		if (toValidate.getPersonInfo() == null) {
			PersonMessageKeys key = PersonMessageKeys.BIP_PERSON_INFO_REQUEST_NOTNULL;
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
			PersonMessageKeys key = PersonMessageKeys.BIP_PERSON_INFO_REQUEST_PID_INCONSISTENT;

			LOGGER.info(key.getKey() + " " + key.getMessage());
			toValidate.addMessage(MessageSeverity.WARN, HttpStatus.OK, key);
		}
		// check logged in user's pid matches returned pid
		PersonTraits personTraits = SecurityUtils.getPersonTraits();
		boolean hasTraits = personTraits != null
				&& StringUtils.isNotBlank(personTraits.getPid());
		boolean canValidate = toValidate.getPersonInfo() != null
				&& toValidate.getPersonInfo().getParticipantId() != null;

		if (hasTraits && canValidate
				&& !personTraits.getPid().equals(toValidate.getPersonInfo().getParticipantId().toString())) {

			PersonMessageKeys key = PersonMessageKeys.BIP_PERSON_INFO_REQUEST_PID_INVALID;
			LOGGER.info(key.getKey() + " " + key.getMessage());
			toValidate.addMessage(MessageSeverity.WARN, HttpStatus.OK, key);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.va.bip.framework.validation.AbstractStandardValidator#setCallingMethod(java.lang.reflect.Method)
	 */
	@Override
	public void setCallingMethod(Method callingMethod) {
		this.callingMethod = callingMethod;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.va.bip.framework.validation.AbstractStandardValidator#getCallingMethod()
	 */
	@Override
	public Method getCallingMethod() {
		return this.callingMethod;
	}
}
