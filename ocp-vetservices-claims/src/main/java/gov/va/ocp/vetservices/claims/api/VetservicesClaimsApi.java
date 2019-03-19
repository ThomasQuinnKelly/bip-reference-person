package gov.va.ocp.vetservices.claims.api;

import org.springframework.boot.actuate.health.Health;
import org.springframework.web.bind.annotation.PathVariable;

import gov.va.ocp.vetservices.claims.api.model.v1.ClaimDetailResponse;
import gov.va.ocp.vetservices.claims.api.model.v1.ClaimsResponse;

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
	 * 
	 * @param id
	 * @return
	 */
	public ClaimDetailResponse getClaimDetailById(@PathVariable("id") String id);
	
	/**
	 * 
	 * @return
	 */
	public ClaimsResponse getAllclaims();

}
