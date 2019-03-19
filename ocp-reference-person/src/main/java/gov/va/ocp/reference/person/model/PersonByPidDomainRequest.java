package gov.va.ocp.reference.person.model;

import gov.va.ocp.framework.service.DomainRequest;
import io.swagger.annotations.ApiModelProperty;

/**
 * This domain model represents a request for PersonInfoDomain by participant ID.
 * <p>
 * The domain service implementation uses this request to derive the appropriate response.
 */
public class PersonByPidDomainRequest extends DomainRequest {
	public static final String MODEL_NAME = PersonByPidDomainRequest.class.getSimpleName();

	/** version id. */
	private static final long serialVersionUID = 1593666859950183199L;

	/** A String representing a social security number. */
	@ApiModelProperty(value = "The Participant ID of the person for whom to retrieve data", required = true,
			example = "6666345")
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
