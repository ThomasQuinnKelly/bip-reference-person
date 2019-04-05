package gov.va.bip.vetservices.claims.api.model.v1;

import gov.va.bip.framework.rest.provider.ProviderResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * A class to represent the data contained in the response
 * from the Claims Service.
 *
 */
@ApiModel(description = "Model for the response from the Claim Detail")
public class ClaimDetailResponse extends ProviderResponse {
	private static final long serialVersionUID = -7401776200109879041L;
	/** The claim object */
	@ApiModelProperty(value = "The object representing the claim information")
	private ClaimInfo claim;

	/**
	 * Gets the claims for a user.
	 * 
	 * @return claim
	 */
	public ClaimInfo getClaim() {
		return claim;
	}

	/**
	 * Sets the claims for a user.
	 *
	 * @param claim the new claim
	 */
	public void setClaim(ClaimInfo claim) {
		this.claim = claim;
	}
}
