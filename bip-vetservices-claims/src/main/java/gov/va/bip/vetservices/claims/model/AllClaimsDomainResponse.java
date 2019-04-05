package gov.va.bip.vetservices.claims.model;

import java.util.List;

import gov.va.bip.framework.service.DomainResponse;
import gov.va.bip.vetservices.claims.orm.Claim;

/**
 * AllClaimsDomainResponse class returns list of claims for a given user.
 * @author rajuthota
 *
 */
public class AllClaimsDomainResponse extends DomainResponse{
	/** Id for serialization. */
	private static final long serialVersionUID = 1022369564490801042L;

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
