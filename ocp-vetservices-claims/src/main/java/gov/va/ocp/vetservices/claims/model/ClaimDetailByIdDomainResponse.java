package gov.va.ocp.vetservices.claims.model;

import gov.va.ocp.framework.service.DomainResponse;
import gov.va.ocp.vetservices.claims.orm.Claim;

/**
 * The class GetClaimsResponse is a response object which returns
 *  a list of claims
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
	 * Sets the claims for a user.
	 * 
	 * @return claim
	 */
	public void setClaim(Claim claim) {
		this.claim = claim;
	}
}
