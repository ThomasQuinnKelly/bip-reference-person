package gov.va.bip.vetservices.claims.model.validators;

import java.util.List;

import org.springframework.http.HttpStatus;

import gov.va.bip.framework.log.BipLogger;
import gov.va.bip.framework.log.BipLoggerFactory;
import gov.va.bip.framework.messages.MessageSeverity;
import gov.va.bip.framework.messages.ServiceMessage;
import gov.va.bip.framework.validation.AbstractStandardValidator;
import gov.va.bip.vetservices.claims.messages.ClaimsMessageKeys;
import gov.va.bip.vetservices.claims.model.AllClaimsDomainRequest;

/**
 * Validates the PID input on the {@link AllClaimsDomainRequest}.
 *
 * @see AbstractStandardValidator
 * @author rajuthota
 */
public class AllClaimsDomainRequestValidator extends AbstractStandardValidator<AllClaimsDomainRequest> {
	/** Class logger */
	private static final BipLogger LOGGER = BipLoggerFactory.getLogger(AllClaimsDomainRequestValidator.class);

	@Override
	public void validate(AllClaimsDomainRequest toValidate, List<ServiceMessage> messages) {
		// validate the request content (claim id)
		if (toValidate.getPid() == null) {
			LOGGER.debug("PID is null");
			messages.add(new ServiceMessage(MessageSeverity.ERROR, HttpStatus.BAD_REQUEST,
					ClaimsMessageKeys.BIP_CLAIMS_INFO_REQUEST_PID_NOTNULL));
		} 
	}

}
