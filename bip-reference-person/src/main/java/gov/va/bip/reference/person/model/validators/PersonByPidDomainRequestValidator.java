package gov.va.bip.reference.person.model.validators;

import java.util.List;

import org.springframework.http.HttpStatus;

import gov.va.bip.framework.log.BipLogger;
import gov.va.bip.framework.log.BipLoggerFactory;
import gov.va.bip.framework.messages.MessageKeys;
import gov.va.bip.framework.messages.MessageSeverity;
import gov.va.bip.framework.messages.ServiceMessage;
import gov.va.bip.framework.validation.AbstractStandardValidator;
import gov.va.bip.reference.person.messages.PersonMessageKeys;
import gov.va.bip.reference.person.model.PersonByPidDomainRequest;

/**
 * Validates the PID input on the {@link PersonByPidDomainRequest}.
 *
 * @see AbstractStandardValidator
 * @author aburkholder
 */
public class PersonByPidDomainRequestValidator extends AbstractStandardValidator<PersonByPidDomainRequest> {
	/** Class logger */
	private static final BipLogger LOGGER = BipLoggerFactory.getLogger(PersonByPidDomainRequestValidator.class);

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.va.bip.framework.validation.AbstractStandardValidator#validate(java.lang.Object, java.util.List)
	 */
	@Override
	public void validate(PersonByPidDomainRequest toValidate, List<ServiceMessage> messages) {
		// validate the request content (PID)
		Long pid = toValidate.getParticipantID();
		if (pid == null) {
			LOGGER.debug("PID is null");
			messages.add(new ServiceMessage(MessageSeverity.ERROR, HttpStatus.BAD_REQUEST,
					MessageKeys.BIP_VALIDATOR_NOT_NULL, super.getCallingMethodName() + "Participant ID"));
		} else if (pid <= 0) {
			LOGGER.debug("PID is <= 0");
			messages.add(new ServiceMessage(MessageSeverity.ERROR, HttpStatus.BAD_REQUEST,
					PersonMessageKeys.BIP_PERSON_INFO_REQUEST_PID_MIN));
		}
	}
}
