package gov.va.bip.reference.person.transform.impl;

import gov.va.bip.framework.transfer.transform.AbstractDomainToProvider;
import gov.va.bip.reference.person.api.model.v1.PersonDocsMetadata;
import gov.va.bip.reference.person.api.model.v1.PersonDocsMetadataResponse;
import gov.va.bip.reference.person.model.PersonDocsMetadataDomainResponse;

/**
 * Transform a service Domain {@link PersonDocsMetadataDomainResponse} into a REST Provider {@link PersonDocumentMetadataResponse}
 * object.
 *
 */
public class PersonDocumentMetadata_DomainToProvider
		extends AbstractDomainToProvider<PersonDocsMetadataDomainResponse, PersonDocsMetadataResponse> {

	@Override
	public PersonDocsMetadataResponse convert(final PersonDocsMetadataDomainResponse domainObject) {
		PersonDocsMetadataResponse providerObject = new PersonDocsMetadataResponse();

		// add data
		PersonDocsMetadata providerData = new PersonDocsMetadata();
		if ((domainObject != null) && (domainObject.getPersonDocumentMetadataDomain() != null)) {
			providerData.setDocName(domainObject.getPersonDocumentMetadataDomain().getDocumentName());
			providerData.setDocCreateDate(domainObject.getPersonDocumentMetadataDomain().getDocumentCreationDate());
		}
		providerObject.setPersonDocsMetadata(providerData);

		// add messages
		if ((domainObject != null) && (domainObject.getMessages() != null) && !domainObject.getMessages().isEmpty()) {
			for (gov.va.bip.framework.messages.ServiceMessage domainMsg : domainObject.getMessages()) {
				providerObject.addMessage(domainMsg.getSeverity(), domainMsg.getKey(), domainMsg.getText(), domainMsg.getHttpStatus());
			}
		}

		return providerObject;
	}

}
