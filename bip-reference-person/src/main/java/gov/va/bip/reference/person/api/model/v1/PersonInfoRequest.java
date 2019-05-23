package gov.va.bip.reference.person.api.model.v1;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import gov.va.bip.framework.transfer.ProviderTransferObjectMarker;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * A class to represent a request for PersonInfoDomain from the Person Service.
 *
 */
@ApiModel(description = "Model for data to request PersonInfoDomain from the Person Service")
@NotNull(message = "{bip.reference.person.info.request.NotNull}")
public class PersonInfoRequest implements ProviderTransferObjectMarker {
	public static final String MODEL_NAME = PersonInfoRequest.class.getSimpleName();

	/** A String representing a participant ID. */
	@ApiModelProperty(value = "The Participant ID of the person for whom to retrieve data", required = true,
			example = "6666345")
	@NotNull(message = "{bip.reference.person.info.request.pid.NotNull}")
	@Min(value = 1, message = "{bip.reference.person.info.request.pid.Min}")
	private Long participantID;

	/**
	 * Gets the participantId.
	 *
	 * @return the participantID
	 */
	public final Long getParticipantID() {
		return this.participantID;
	}

	/**
	 * Sets the participantId.
	 *
	 * @param participantID the participantID
	 */
	public final void setParticipantID(final Long participantID) {
		this.participantID = participantID;
	}
}
