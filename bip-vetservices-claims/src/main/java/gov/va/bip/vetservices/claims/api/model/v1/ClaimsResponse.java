package gov.va.bip.vetservices.claims.api.model.v1;

import java.util.List;

import gov.va.bip.framework.rest.provider.ProviderResponse;
import gov.va.bip.vetservices.claims.orm.Claim;

public class ClaimsResponse extends ProviderResponse{
	/** Id for serialization. */
	private static final long serialVersionUID = -3525380268185832510L;
	
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
