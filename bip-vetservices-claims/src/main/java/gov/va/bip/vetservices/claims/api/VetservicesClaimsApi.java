package gov.va.bip.vetservices.claims.api;

import org.springframework.boot.actuate.health.Health;
import org.springframework.web.bind.annotation.PathVariable;

import gov.va.bip.vetservices.claims.api.model.v1.ClaimDetailResponse;
import gov.va.bip.vetservices.claims.api.model.v1.ClaimsResponse;

/**
 * The contract for the Vetservices Claims endpoint.
 *
 * @author rajutgota
 */
public interface VetservicesClaimsApi {

	/**
	 * Contract for the {@link Health} operation.
	 * 
	 * @return Health
	 */
	public Health health();

	/**
	 * Gets the claim detail by id.
	 *
	 * @param id the id
	 * @return the claim detail by id
	 */
	public ClaimDetailResponse getClaimDetailById(@PathVariable("id") String id);
	
	/**
	 * Gets the all claims.
	 *
	 * @return the all claims
	 */
	public ClaimsResponse getAllclaims();

}
