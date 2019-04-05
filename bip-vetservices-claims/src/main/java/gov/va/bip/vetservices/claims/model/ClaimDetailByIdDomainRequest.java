package gov.va.bip.vetservices.claims.model;

import javax.validation.constraints.NotNull;

import gov.va.bip.framework.service.DomainRequest;
import io.swagger.annotations.ApiModel;

/**
 * A class to represent a request for AllClaimsDomain from the Claims Service.
 * @author rajuthota
 *
 */
@ApiModel(description = "Model for data to request ClaimDetailByIdDomain from the Claims Service")
@NotNull(message = "{bip.vetservices.claims.claimdetail.request.NotNull}")
public class ClaimDetailByIdDomainRequest extends DomainRequest {
	/** Id for serialization. */
	private static final long serialVersionUID = 1721412087987262336L;

	/** The claim id. */
	// Suppressing warning for ShortVariable as id is a valid variable name
	@NotNull(message = "{bip.vetservices.claims.claimdetail.request.id.NotNull}")
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
