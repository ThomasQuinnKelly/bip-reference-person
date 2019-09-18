package gov.va.bip.reference.person.model;

import gov.va.bip.framework.service.DomainRequest;

/**
 * This domain model represents a request for PersonDocsMetadataDomain by participant ID.
 * <p>
 * The domain service implementation uses this request to derive the appropriate response.
 */
public class PersonDocsMetadataDomainRequest extends DomainRequest {
	public static final String MODEL_NAME = PersonDocsMetadataDomainRequest.class.getSimpleName();

	/** version id. */
	private static final long serialVersionUID = 1593666859950183199L;

	/** A String representing a participant ID. */
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
