package gov.va.ocp.vetservices.claims.model.validators;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.http.HttpStatus;

import gov.va.ocp.framework.log.OcpLogger;
import gov.va.ocp.framework.log.OcpLoggerFactory;
import gov.va.ocp.framework.messages.MessageSeverity;
import gov.va.ocp.framework.messages.ServiceMessage;
import gov.va.ocp.framework.validation.AbstractStandardValidator;
import gov.va.ocp.vetservices.claims.exception.ClaimsServiceException;
import gov.va.ocp.vetservices.claims.messages.ClaimsMessageKeys;
import gov.va.ocp.vetservices.claims.model.AllClaimsDomainResponse;;

/**
 * 
 * @author rajuthota
 *
 */
public class AllClaimsDomainResponseValidator extends AbstractStandardValidator<AllClaimsDomainResponse> {

	/** Class logger */
	private static final OcpLogger LOGGER = OcpLoggerFactory.getLogger(AllClaimsDomainResponseValidator.class);

	/** For the message when hystrix fallback method is manually invoked */
	//private static final String INVOKE_FALLBACK_MESSAGE = "Could not get data from cache or database.";

	/** The method that caused this validator to be invoked */
	private Method callingMethod;
	
	@Override
	public void validate(AllClaimsDomainResponse toValidate, List<ServiceMessage> messages) {

		// if response has errors, fatals or warnings skip validations
		if (toValidate.hasErrors() || toValidate.hasFatals() || toValidate.hasWarnings()) {
			return;
		}
		// check if empty response, or errors / fatals
		if (toValidate == null || toValidate.getClaims() == null) {
			ClaimsMessageKeys key = ClaimsMessageKeys.OCP_CLAIMS_INFO_REQUEST_NOTNULL;
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
