package gov.va.ocp.vetservices.claims.api;

import org.springframework.boot.actuate.health.Health;
import org.springframework.web.bind.annotation.PathVariable;

import gov.va.ocp.vetservices.claims.model.ClaimDetailByIdDomainResponse;
import gov.va.ocp.vetservices.claims.model.ClaimsDomainResponse;

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
	public ClaimDetailByIdDomainResponse getClaimDetailById(@PathVariable("id") String id);
	
	/**
	 * 
	 * @return
	 */
	public ClaimsDomainResponse getAllclaims();

}
