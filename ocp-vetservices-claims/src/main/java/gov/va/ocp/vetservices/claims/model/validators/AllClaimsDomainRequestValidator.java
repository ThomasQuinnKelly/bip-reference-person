package gov.va.ocp.vetservices.claims.model.validators;

import java.util.List;

import org.springframework.http.HttpStatus;

import gov.va.ocp.framework.log.OcpLogger;
import gov.va.ocp.framework.log.OcpLoggerFactory;
import gov.va.ocp.framework.messages.MessageSeverity;
import gov.va.ocp.framework.messages.ServiceMessage;
import gov.va.ocp.framework.validation.AbstractStandardValidator;
import gov.va.ocp.vetservices.claims.model.AllClaimsDomainRequest;

/**
 * Validates the PID input on the {@link PersonByPidDomainRequest}.
 *
 * @see AbstractStandardValidator
 * @author rajuthota
 */
public class AllClaimsDomainRequestValidator extends AbstractStandardValidator<AllClaimsDomainRequest> {
	/** Class logger */
	private static final OcpLogger LOGGER = OcpLoggerFactory.getLogger(AllClaimsDomainRequestValidator.class);

	@Override
	public void validate(AllClaimsDomainRequest toValidate, List<ServiceMessage> messages) {
		// validate the request content (claim id)
		if (toValidate.getPid() == null) {
			LOGGER.debug("PID is null");
			messages.add(new ServiceMessage(MessageSeverity.ERROR, "",
					super.getCallingMethodName() + "Participant ID cannot be null.",
					HttpStatus.BAD_REQUEST));
		} 
	}

}
