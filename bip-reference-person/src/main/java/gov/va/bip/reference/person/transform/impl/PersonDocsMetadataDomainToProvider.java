package gov.va.bip.reference.person.transform.impl;

import gov.va.bip.framework.transfer.transform.AbstractDomainToProvider;
import gov.va.bip.framework.transfer.transform.TransformerUtils;
import gov.va.bip.reference.person.api.model.v1.PersonDocsMetadata;
import gov.va.bip.reference.person.api.model.v1.PersonDocsMetadataResponse;
import gov.va.bip.reference.person.model.PersonDocsMetadataDomainResponse;

/**
 * Transform a service Domain {@link PersonDocsMetadataDomainResponse} into a REST Provider {@link PersonDocsMetadataResponse} object.
 *
 */
public class PersonDocsMetadataDomainToProvider
extends AbstractDomainToProvider<PersonDocsMetadataDomainResponse, PersonDocsMetadataResponse> {

	@Override
	public PersonDocsMetadataResponse convert(final PersonDocsMetadataDomainResponse domainObject) {
		PersonDocsMetadataResponse providerObject = new PersonDocsMetadataResponse();

		// add data
		PersonDocsMetadata providerData = new PersonDocsMetadata();
		if ((domainObject != null) && (domainObject.getPersonDocsMetadataDomain() != null)) {
			providerData.setDocName(domainObject.getPersonDocsMetadataDomain().getDocName());
			providerData.setDocCreateDate(domainObject.getPersonDocsMetadataDomain().getDocCreateDate());
		}
		providerObject.setPersonDocsMetadata(providerData);

		// add messages
		if ((domainObject != null) && (domainObject.getMessages() != null) && !domainObject.getMessages().isEmpty()) {
			TransformerUtils.transferMessages(providerObject, domainObject);
		}

		return providerObject;
	}

}
