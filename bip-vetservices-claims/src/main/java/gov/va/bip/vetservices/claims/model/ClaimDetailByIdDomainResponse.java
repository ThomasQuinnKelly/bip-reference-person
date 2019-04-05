package gov.va.bip.vetservices.claims.model;

import gov.va.bip.framework.service.DomainResponse;
import gov.va.bip.vetservices.claims.orm.Claim;

/**
 * The class GetClaimsResponse is a response object which returns
 * claim details for a given claim id.
 * @author rajuthota
 *
 */
public class ClaimDetailByIdDomainResponse extends DomainResponse  {

	/** Id for serialization. */
	private static final long serialVersionUID = 7534345055983583386L;
	
	/** The claim object */
	private Claim claim;

	/**
	 * Gets the claims for a user.
	 * 
	 * @return claim
	 */
	public Claim getClaim() {
		return claim;
	}

	/**
	 * Sets the claim.
	 *
	 * @param claim the new claim
	 */
	public void setClaim(Claim claim) {
		this.claim = claim;
	}
}
