package gov.va.bip.reference.person.model.validators;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.http.HttpStatus;

import gov.va.bip.framework.log.BipLogger;
import gov.va.bip.framework.log.BipLoggerFactory;
import gov.va.bip.framework.messages.MessageSeverity;
import gov.va.bip.framework.messages.ServiceMessage;
import gov.va.bip.framework.security.SecurityUtils;
import gov.va.bip.framework.validation.AbstractStandardValidator;
import gov.va.bip.reference.person.exception.PersonServiceException;
import gov.va.bip.reference.person.messages.PersonMessageKeys;
import gov.va.bip.reference.person.model.PersonDocumentMetadataDomainRequest;
import gov.va.bip.reference.person.model.PersonDocumentMetadataDomainResponse;

/**
 * Validates the PID input on the {@link PersonDocumentMetadataDomainResponse}.
 */
public class PersonDocumentMetadataDomainResponseValidator extends AbstractStandardValidator<PersonDocumentMetadataDomainResponse> {

	/** Class logger */
	private static final BipLogger LOGGER = BipLoggerFactory.getLogger(PersonDocumentMetadataDomainResponseValidator.class);

	/** The method that caused this validator to be invoked */
	private Method callingMethod;

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.va.bip.framework.validation.AbstractStandardValidator#validate(java.lang.Object, java.util.List)
	 */
	@Override
	public void validate(final PersonDocumentMetadataDomainResponse toValidate, final List<ServiceMessage> messages) {
		Object supplemental = getSupplemental(PersonDocumentMetadataDomainRequest.class);
		PersonDocumentMetadataDomainRequest request =
				supplemental == null ? new PersonDocumentMetadataDomainRequest() : (PersonDocumentMetadataDomainRequest) supplemental;

				// if response has errors, fatals or warnings skip validations
				if (toValidate.hasErrors() || toValidate.hasFatals() || toValidate.hasWarnings()) {
					return;
				}
				// check if empty response, or errors / fatals
				if ((toValidate == null) || (toValidate.getPersonDocumentMetadataDomain() == null)) {
					PersonMessageKeys key = PersonMessageKeys.BIP_PERSON_DOCUMENT_METADATA_REQUEST_NOTNULL;
					LOGGER.info(key.getKey() + " " + key.getMessage());
					throw new PersonServiceException(key, MessageSeverity.FATAL, HttpStatus.INTERNAL_SERVER_ERROR);
				}

				/*
				 * In a real-world service, it is highly unlikely that a user would be allowed to query for someone else's data. In general,
				 * responses should *always* contain only data for the logged-in person. Therefore, the checks below would typically throw an
				 * exception, not just set a warning.
				 */
				LOGGER.debug("Request PID: " + request.getParticipantID() + "; Response document name: "
						+ toValidate.getPersonDocumentMetadataDomain().getDocumentName() + "; Response document creation date: "
						+ toValidate.getPersonDocumentMetadataDomain().getDocumentCreationDate() + "; PersonTraits PID: "
						+ (SecurityUtils.getPersonTraits() == null ? "null" : SecurityUtils.getPersonTraits().getPid()));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.va.bip.framework.validation.AbstractStandardValidator#setCallingMethod(java.lang.reflect.Method)
	 */
	@Override
	public void setCallingMethod(final Method callingMethod) {
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
