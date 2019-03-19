package gov.va.ocp.vetservices.claims.model;

import javax.validation.constraints.NotNull;

import gov.va.ocp.framework.service.DomainRequest;

public class ClaimDetailByIdDomainRequest extends DomainRequest {
	/** Id for serialization. */
	private static final long serialVersionUID = 1721412087987262336L;

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
	public String getId() {
		return id;
	}

	/**
	 * Sets the claim id.
	 * 
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
	}
}
