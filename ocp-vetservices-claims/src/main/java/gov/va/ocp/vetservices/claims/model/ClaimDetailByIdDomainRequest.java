package gov.va.ocp.vetservices.claims.model;

import javax.validation.constraints.NotNull;

public class ClaimDetailByIdDomainRequest  {

	/** The claim id. */
	// Suppressing warning for ShortVariable as id is a valid variable name
	@NotNull(message = "GetClaimDetailByIdDomainRequest.id.NotNull")
	@SuppressWarnings("PMD.ShortVariable")
	private String id;

	/**
	 * Gets the claim id.
	 * 
	 * @return the id
	 */
	public final String getId() {
		return id;
	}

	/**
	 * Sets the claim id.
	 * 
	 * @param id the new id
	 */
	public void setId(final String id) {
		this.id = id;
	}
}
