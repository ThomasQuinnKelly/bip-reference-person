package gov.va.ocp.vetservices.claims;

import gov.va.ocp.vetservices.claims.model.ClaimDetailByIdDomainRequest;
import gov.va.ocp.vetservices.claims.model.ClaimDetailByIdDomainResponse;
import gov.va.ocp.vetservices.claims.model.ClaimsDomainResponse;

/**
 * The interface for the Claims service
 * @author rajuthota
 *
 */
public interface VetServicesClaimsService {
	/** 
	 * Returns the claim detail for a given claim id.
	 * @param id
	 * @return
	 */
	ClaimDetailByIdDomainResponse getClaimDetailById(ClaimDetailByIdDomainRequest getClaimDetailByIdDomainRequest);
	
	/** 
	 * Returns all claims
	 * @return
	 */
	ClaimsDomainResponse getClaims();
}
