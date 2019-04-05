package gov.va.bip.vetservices.claims.model.validators;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.http.HttpStatus;

import gov.va.bip.framework.log.BipLogger;
import gov.va.bip.framework.log.BipLoggerFactory;
import gov.va.bip.framework.messages.MessageSeverity;
import gov.va.bip.framework.messages.ServiceMessage;
import gov.va.bip.framework.validation.AbstractStandardValidator;
import gov.va.bip.vetservices.claims.exception.ClaimsServiceException;
import gov.va.bip.vetservices.claims.messages.ClaimsMessageKeys;
import gov.va.bip.vetservices.claims.model.ClaimDetailByIdDomainResponse;

public class ClaimDetailByIdDomainResponseValidator extends AbstractStandardValidator<ClaimDetailByIdDomainResponse> {

	/** Class logger */
	private static final BipLogger LOGGER = BipLoggerFactory.getLogger(ClaimDetailByIdDomainResponseValidator.class);

	/** For the message when hystrix fallback method is manually invoked */
	private static final String INVOKE_FALLBACK_MESSAGE = "Could not get data from cache or database.";

	/** The method that caused this validator to be invoked */
	private Method callingMethod;
	
	@Override
	public void validate(ClaimDetailByIdDomainResponse toValidate, List<ServiceMessage> messages) {
		
		// if response has errors, fatals or warnings skip validations
		if (toValidate.hasErrors() || toValidate.hasFatals() || toValidate.hasWarnings()) {
			return;
		}
		// check if empty response, or errors / fatals
		if (toValidate == null || toValidate.getClaim() == null) {
			ClaimsMessageKeys key = ClaimsMessageKeys.BIP_CLAIM_DETAIL_INFO_REQUEST_NOTNULL;
			LOGGER.info(key.getKey() + " " + key.getMessage());
			throw new ClaimsServiceException(key, MessageSeverity.FATAL, HttpStatus.INTERNAL_SERVER_ERROR);
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
