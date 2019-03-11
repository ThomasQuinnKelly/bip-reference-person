package gov.va.ocp.vetservices.claims.model;

import java.util.List;

import gov.va.ocp.vetservices.claims.orm.Claim;

public class ClaimsDomainResponse {
	
	/** List of all claims */
	List<Claim> claims;

	/**
	 * Gets the claims for a user.
	 * 
	 * @return the claims
	 */
	public List<Claim> getClaims() {
		return claims;
	}

	/**
	 * Sets the claims for a user.
	 * @param claims
	 */
	public void setClaims(List<Claim> claims) {
		this.claims = claims;
	}
}
