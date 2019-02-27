package gov.va.ocp.reference.person.model.person.v1;

import gov.va.ocp.reference.framework.service.ServiceRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * A class to represent a request for PersonInfo from the Person Service.
 *
 */
@ApiModel(description = "Model for data to request PersonInfo from the Person Service")
public class PersonInfoRequest extends ServiceRequest {
	public static final String MODEL_NAME = PersonInfoRequest.class.getSimpleName();

	/** version id. */
	private static final long serialVersionUID = -1348027376496517555L;

	/** A String representing a social security number. */
	@ApiModelProperty(value = "The Participant ID of the person for whom to retrieve data", required = true,
			example = "13364995")
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
