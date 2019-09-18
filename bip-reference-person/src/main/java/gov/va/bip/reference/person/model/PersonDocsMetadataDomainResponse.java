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
	private PersonDocsMetadataDomain personDocsMetadataDomain;

	/**
	 * Gets the person metadata
	 *
	 * @return A personDocsMetadataDomain instance
	 */
	public final PersonDocsMetadataDomain getPersonDocsMetadataDomain() {
		return personDocsMetadataDomain;
	}

	/**
	 * Sets the person metadata object for the provider
	 *
	 * @param personDocsMetadataDomain A personDocsMetadataDomain instance
	 */
	public final void setPersonDocsMetadataDomain(final PersonDocsMetadataDomain personDocsMetadataDomain) {
		this.personDocsMetadataDomain = personDocsMetadataDomain;
	}
}
