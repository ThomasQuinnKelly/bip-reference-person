package gov.va.bip.reference.person.transform.impl;

import gov.va.bip.framework.transfer.transform.AbstractDomainToProvider;
import gov.va.bip.reference.person.api.model.v1.PersonDocumentMetadataResponse;
import gov.va.bip.reference.person.model.PersonDocumentMetadataDomainResponse;

/**
 * Transform a service Domain {@link PersonDocumentMetadataDomainResponse} into a REST Provider {@link PersonDocumentMetadataResponse}
 * object.
 *
 */
public class PersonDocumentMetadataByPid_DomainToProvider
		extends AbstractDomainToProvider<PersonDocumentMetadataDomainResponse, PersonDocumentMetadataResponse> {

	@Override
	public PersonDocumentMetadataResponse convert(final PersonDocumentMetadataDomainResponse domainObject) {
		PersonDocumentMetadataResponse providerObject = new PersonDocumentMetadataResponse();

		// add data
		if ((domainObject != null) && (domainObject.getPersonDocumentMetadataDomain() != null)) {
			providerObject.setDocumentName(domainObject.getPersonDocumentMetadataDomain().getDocumentName());
			providerObject.setDocumentCreationDate(domainObject.getPersonDocumentMetadataDomain().getDocumentCreationDate());
		}
		// add messages
		if ((domainObject != null) && (domainObject.getMessages() != null) && !domainObject.getMessages().isEmpty()) {
			for (gov.va.bip.framework.messages.ServiceMessage domainMsg : domainObject.getMessages()) {
				providerObject.addMessage(domainMsg.getSeverity(), domainMsg.getKey(), domainMsg.getText(), domainMsg.getHttpStatus());
			}
		}

		return providerObject;
	}

}
