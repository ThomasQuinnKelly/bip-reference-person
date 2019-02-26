package gov.va.ocp.reference.person.model.person.v1;

import gov.va.ocp.reference.framework.service.ServiceRequest;

/**
 * A class to represent a request for PersonInfo from the Person Service.
 *
 */
public class PersonInfoRequest extends ServiceRequest {

	/** version id. */
	private static final long serialVersionUID = -1348027376496517555L;

	/** A String representing a social security number. */
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
