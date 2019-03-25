package gov.va.ocp.vetservices.claims.model.validators;

import java.util.List;

import org.springframework.http.HttpStatus;

import gov.va.ocp.framework.log.OcpLogger;
import gov.va.ocp.framework.log.OcpLoggerFactory;
import gov.va.ocp.framework.messages.MessageSeverity;
import gov.va.ocp.framework.messages.ServiceMessage;
import gov.va.ocp.framework.validation.AbstractStandardValidator;
import gov.va.ocp.vetservices.claims.model.ClaimDetailByIdDomainRequest;

/**
 * Validates the PID input on the {@link ClaimDetailByIdDomainRequest}.
 *
 * @see AbstractStandardValidator
 * @author rajuthota
 */
public class ClaimDetailByIdDomainRequestValidator extends AbstractStandardValidator<ClaimDetailByIdDomainRequest> {
	/** Class logger */
	private static final OcpLogger LOGGER = OcpLoggerFactory.getLogger(ClaimDetailByIdDomainRequestValidator.class);

	@Override
	public void validate(ClaimDetailByIdDomainRequest toValidate, List<ServiceMessage> messages) {
		// validate the request content (claim id)
		if (toValidate.getId() == null) {
			LOGGER.error("claim id is null");
			messages.add(new ServiceMessage(MessageSeverity.ERROR, "",
					super.getCallingMethodName() + "Claims ID cannot be null.",
					HttpStatus.BAD_REQUEST));
		} 
	}
}
