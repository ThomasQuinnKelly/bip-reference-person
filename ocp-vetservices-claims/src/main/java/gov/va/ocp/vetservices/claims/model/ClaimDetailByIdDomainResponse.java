package gov.va.ocp.vetservices.claims.model;

import java.util.Optional;

import gov.va.ocp.vetservices.claims.orm.Claim;

/**
 * The class GetClaimsResponse is a response object which returns
 *  a list of claims
 * 
 */
public class ClaimDetailByIdDomainResponse {

	/** The claim object */
	private Optional<Claim> claim;

	/**
	 * Gets the claims for a user.
	 * 
	 * @return claim
	 */
	public Optional<Claim> getClaim() {
		return claim;
	}

	/**
	 * Sets the claims for a user.
	 * 
	 * @return claim
	 */
	public void setClaim(Optional<Claim> claim) {
		this.claim = claim;
	}
}
