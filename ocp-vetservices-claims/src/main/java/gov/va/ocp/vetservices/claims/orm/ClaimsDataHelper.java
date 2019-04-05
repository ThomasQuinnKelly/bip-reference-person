package gov.va.ocp.vetservices.claims.orm;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import gov.va.ocp.framework.audit.AuditEvents;
import gov.va.ocp.framework.audit.Auditable;
import gov.va.ocp.framework.log.OcpLogger;
import gov.va.ocp.framework.log.OcpLoggerFactory;
import gov.va.ocp.framework.messages.MessageSeverity;
import gov.va.ocp.vetservices.claims.model.ClaimDetailByIdDomainRequest;
import gov.va.ocp.vetservices.claims.model.ClaimDetailByIdDomainResponse;
import gov.va.ocp.vetservices.claims.exception.ClaimsServiceException;
import gov.va.ocp.vetservices.claims.messages.ClaimsMessageKeys;
import gov.va.ocp.vetservices.claims.model.AllClaimsDomainResponse;

@Component
public class ClaimsDataHelper {
	
	/** String to prepend messages for re-thrown exceptions */
	private static final String THROWSTR = "Rethrowing the following exception:  ";

	/** Logger */
	private static final OcpLogger LOGGER = OcpLoggerFactory.getLogger(ClaimsDataHelper.class);
	
	@Autowired
	ClaimsRepository claimsRepository;
	
	/**
	 * 
	 * @param claimDetailByIdDomainRequest
	 * @return ClaimDetailByIdDomainResponse
	 */
	@Auditable(event = AuditEvents.SERVICE_AUDIT, activity = "getClaimDetailById")
	public ClaimDetailByIdDomainResponse getClaimDetailById(ClaimDetailByIdDomainRequest claimDetailByIdDomainRequest) {
		ClaimDetailByIdDomainResponse claimDetailByIdDomainResponse = new ClaimDetailByIdDomainResponse();
		try {
			claimDetailByIdDomainResponse
					.setClaim(claimsRepository.findById(Long.parseLong(claimDetailByIdDomainRequest.getId())).get());
		} catch (final NoSuchElementException clientException) {
			// any other exception can be caught and thrown as ClaimsServiceException for
			// the circuit not to be opened
			String message = THROWSTR + clientException.getClass().getName() + ": " + clientException.getMessage();
			LOGGER.error(message, clientException);
			throw new ClaimsServiceException(ClaimsMessageKeys.OCP_CLAIMS_INFO_REQUEST_CLAIMID_NOTVALID, MessageSeverity.WARN, HttpStatus.BAD_REQUEST,
					clientException, ClaimsMessageKeys.OCP_CLAIMS_INFO_REQUEST_CLAIMID_NOTVALID);
		} catch (final RuntimeException runtimeException) {
			// RuntimeException can't be ignored as it's a candidate for circuit to be opened in Hystrix
			String message = THROWSTR + runtimeException.getClass().getName() + ": " + runtimeException.getMessage();
			LOGGER.error(message, runtimeException);
			throw runtimeException;
		}
		return claimDetailByIdDomainResponse;
	}
	
	/**
	 * 
	 * @return AllClaimsDomainResponse
	 */
	@Auditable(event = AuditEvents.SERVICE_AUDIT, activity = "getClaims")
	public AllClaimsDomainResponse getClaims() {
		AllClaimsDomainResponse claimsDomainResponse = new AllClaimsDomainResponse();
		try {
			claimsDomainResponse.setClaims(claimsRepository.findAll());
		} catch (final RuntimeException runtimeException) {
			// RuntimeException can't be ignored as it's a candidate for circuit to be opened in Hystrix
			String message = THROWSTR + runtimeException.getClass().getName() + ": " + runtimeException.getMessage();
			LOGGER.error(message, runtimeException);
			throw runtimeException;
		}
		return claimsDomainResponse;
	}
}
