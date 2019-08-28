package gov.va.bip.reference.person.model;

import gov.va.bip.framework.service.DomainResponse;

/**
 * This domain model represents a response from processing a request for getMetadataDocumentForPid.
 * <p>
 * The domain service implementation returns this response to the provider.
 */
public class PersonDocsMetadataDomainResponse extends DomainResponse {

	/** Id for serialization. */
	private static final long serialVersionUID = 1L;

	/** A PersonDocsMetadataDomain instance. */
	private PersonDocsMetadataDomain personDocumentMetadataDomain;

	/**
	 * Gets the person metadata
	 *
	 * @return A personDocumentMetadataDomain instance
	 */
	public final PersonDocsMetadataDomain getPersonDocumentMetadataDomain() {
		return personDocumentMetadataDomain;
	}

	/**
	 * Sets the person metadata object for the provider
	 *
	 * @param personDocumentMetadataDomain A personDocumentMetadataDomain instance
	 */
	public final void setPersonDocumentMetadataDomain(final PersonDocsMetadataDomain personDocumentMetadataDomain) {
		this.personDocumentMetadataDomain = personDocumentMetadataDomain;
	}
}
