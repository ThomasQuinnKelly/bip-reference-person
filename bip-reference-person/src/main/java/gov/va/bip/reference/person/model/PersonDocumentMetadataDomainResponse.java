package gov.va.bip.reference.person.model;

import gov.va.bip.framework.service.DomainResponse;

/**
 * This domain model represents a response from processing a request for getMetadataDocumentForPid.
 * <p>
 * The domain service implementation returns this response to the provider.
 */
public class PersonDocumentMetadataDomainResponse extends DomainResponse {

	/** Id for serialization. */
	private static final long serialVersionUID = 1L;

	/** A PersonDocumentMetadataDomain instance. */
	private PersonDocumentMetadataDomain personDocumentMetadataDomain;

	/**
	 * Gets the person metadata
	 *
	 * @return A personDocumentMetadataDomain instance
	 */
	public final PersonDocumentMetadataDomain getPersonDocumentMetadataDomain() {
		return personDocumentMetadataDomain;
	}

	/**
	 * Sets the person metadata object for the provider
	 *
	 * @param personDocumentMetadataDomain A personDocumentMetadataDomain instance
	 */
	public final void setPersonDocumentMetadataDomain(final PersonDocumentMetadataDomain personDocumentMetadataDomain) {
		this.personDocumentMetadataDomain = personDocumentMetadataDomain;
	}
}
