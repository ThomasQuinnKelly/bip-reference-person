package gov.va.bip.vetservices.claims;

import gov.va.bip.vetservices.claims.model.AllClaimsDomainRequest;
import gov.va.bip.vetservices.claims.model.AllClaimsDomainResponse;
import gov.va.bip.vetservices.claims.model.ClaimDetailByIdDomainRequest;
import gov.va.bip.vetservices.claims.model.ClaimDetailByIdDomainResponse;

/**
 * The interface for the Claims service.
 *
 * @author rajuthota
 */
public interface VetServicesClaimsService {
	
	/**
	 *  
	 * Returns the claim detail for a given claim id.
	 *
	 * @param getClaimDetailByIdDomainRequest the get claim detail by id domain request
	 * @return the claim detail by id
	 */
	ClaimDetailByIdDomainResponse getClaimDetailById(ClaimDetailByIdDomainRequest getClaimDetailByIdDomainRequest);
	
	/**
	 *  
	 * Returns all claims.
	 *
	 * @param allClaimsDomainRequest the all claims domain request
	 * @return the claims
	 */
	AllClaimsDomainResponse getClaims(AllClaimsDomainRequest allClaimsDomainRequest);
}
