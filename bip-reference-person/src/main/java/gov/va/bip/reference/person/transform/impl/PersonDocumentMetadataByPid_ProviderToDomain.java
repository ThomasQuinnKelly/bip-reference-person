package gov.va.bip.reference.person.transform.impl;

import gov.va.bip.framework.transfer.transform.AbstractProviderToDomain;
import gov.va.bip.reference.person.api.model.v1.PersonDocumentMetadataRequest;
import gov.va.bip.reference.person.model.PersonByPidDomainRequest;
import gov.va.bip.reference.person.model.PersonDocumentMetadataDomainRequest;

/**
 * Transform a REST Provider {@link PersonDocumentMetadata} into a service Domain {@link PersonByPidDomainRequest} object.
 *
 * @author aburkholder
 */
public class PersonDocumentMetadataByPid_ProviderToDomain extends AbstractProviderToDomain<PersonDocumentMetadataRequest, PersonDocumentMetadataDomainRequest> {

	/**
	 * Transform a REST Provider {@link personDocumentMetadataRequest} into a service Domain
	 * {@link PersonDocumentMetadataDomainRequest} object.
	 * <p>
	 * {@inheritDoc AbstractProviderToDomain}
	 */
	@Override
	public PersonDocumentMetadataDomainRequest convert(final PersonDocumentMetadataRequest providerObject) {
		PersonDocumentMetadataDomainRequest domainObject = new PersonDocumentMetadataDomainRequest();
		if (providerObject != null) {
			domainObject.setParticipantID(providerObject.getParticipantID());
		}
		return domainObject;
	}

}
