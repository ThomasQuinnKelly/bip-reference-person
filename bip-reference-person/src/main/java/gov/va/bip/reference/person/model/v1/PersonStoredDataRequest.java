package gov.va.bip.reference.person.model.v1;

import javax.validation.constraints.NotNull;

import gov.va.bip.framework.transfer.ProviderTransferObjectMarker;
import io.swagger.annotations.ApiModel;

/**
 * A class to represent a request for claimDetailByIdDomain from the Claims Service.
 *
 */
@ApiModel(description = "Model for data to request ClaimDetailByIdDomain from the Claims Service")
@NotNull(message = "{bip.vetservices.claims.claimdetail.request.NotNull}")
public class PersonStoredDataRequest implements ProviderTransferObjectMarker {
	// public static final String MODEL_NAME = ClaimDetailRequest.class.getSimpleName();
	//
	// /** A String representing a social security number. */
	// @ApiModelProperty(value = "The Claim Id of the claim ", required = true,
	// example = "123456")
	// @NotNull(message = "{bip.vetservices.claims.claimdetail.request.id.NotNull}")
	// private Long participantId;
	//
	// /**
	// * Gets the claimID.
	// *
	// * @return the claimID
	// */
	// public String getClaimId() {
	// return claimId;
	// }
	//
	// /**
	// * Sets the claimID.
	// *
	// * @param claimId the new claim id
	// */
	// public void setClaimId(final String claimId) {
	// this.claimId = claimId;
	// }
	//
	// public Long getParticipantID() {
	// // TODO Auto-generated method stub
	// return participantId;
	// }
}
