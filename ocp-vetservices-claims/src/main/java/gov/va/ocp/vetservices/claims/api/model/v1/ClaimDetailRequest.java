package gov.va.ocp.vetservices.claims.api.model.v1;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import gov.va.ocp.framework.transfer.ProviderTransferObjectMarker;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * A class to represent a request for PersonInfoDomain from the Person Service.
 *
 */
@ApiModel(description = "Model for data to request PersonInfoDomain from the Person Service")
@NotNull(message = "{ocp.reference.person.info.request.NotNull}")
public class ClaimDetailRequest implements ProviderTransferObjectMarker {
	public static final String MODEL_NAME = ClaimDetailRequest.class.getSimpleName();

	/** A String representing a social security number. */
	@ApiModelProperty(value = "The Participant ID of the person for whom to retrieve data", required = true,
			example = "6666345")
	@NotNull(message = "{ocp.reference.person.info.request.pid.NotNull}")
	@Min(value = 1, message = "{ocp.reference.person.info.request.pid.Min}")
	private String claimId;

	/**
	 * Gets the claimID.
	 *
	 * @return the claimID
	 */
	public String getClaimId() {
		return claimId;
	}

	/**
	 * Sets the claimID.
	 *
	 * @param claimID the claimID
	 */
	public void setClaimId(String claimId) {
		this.claimId = claimId;
	}
}
