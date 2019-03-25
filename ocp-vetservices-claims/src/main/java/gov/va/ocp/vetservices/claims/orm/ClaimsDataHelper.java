package gov.va.ocp.vetservices.claims.orm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.va.ocp.framework.audit.AuditEvents;
import gov.va.ocp.framework.audit.Auditable;
import gov.va.ocp.vetservices.claims.model.ClaimDetailByIdDomainRequest;
import gov.va.ocp.vetservices.claims.model.ClaimDetailByIdDomainResponse;
import gov.va.ocp.vetservices.claims.model.AllClaimsDomainResponse;

@Component
public class ClaimsDataHelper {
	
	@Autowired
	ClaimsRepository claimsRepository;
	
	/**
	 * 
	 * @param claimDetailByIdDomainRequest
	 * @return ClaimDetailByIdDomainResponse
	 */
	@Auditable(event = AuditEvents.REST_REQUEST, activity = "getClaimDetailById")
	public ClaimDetailByIdDomainResponse getClaimDetailById(ClaimDetailByIdDomainRequest claimDetailByIdDomainRequest) {
		ClaimDetailByIdDomainResponse claimDetailByIdDomainResponse = new ClaimDetailByIdDomainResponse();
		claimDetailByIdDomainResponse
				.setClaim(claimsRepository.findById(Long.parseLong(claimDetailByIdDomainRequest.getId())).get());
		return claimDetailByIdDomainResponse;
	}
	
	/**
	 * 
	 * @return AllClaimsDomainResponse
	 */
	@Auditable(event = AuditEvents.REST_REQUEST, activity = "getClaims")
	public AllClaimsDomainResponse getClaims() {
		AllClaimsDomainResponse claimsDomainResponse = new AllClaimsDomainResponse();
		claimsDomainResponse.setClaims(claimsRepository.findAll());
		return claimsDomainResponse;
	}
}
