package gov.va.bip.reference.person.transform.impl;

import gov.va.bip.framework.transfer.transform.AbstractProviderToDomain;
import gov.va.bip.reference.person.api.model.v1.PersonInfoRequest;
import gov.va.bip.reference.person.model.PersonByPidDomainRequest;

/**
 * Transform a REST Provider {@link PersonInfoRequest} into a service Domain {@link PersonByPidDomainRequest} object.
 *
 * @author aburkholder
 */
public class PersonByPidProviderToDomain extends AbstractProviderToDomain<PersonInfoRequest, PersonByPidDomainRequest> {

	/**
	 * Transform a REST Provider {@link PersonInfoRequest} into a service Domain {@link PersonByPidDomainRequest} object.
	 * <p>
	 * {@inheritDoc AbstractProviderToDomain}
	 */
	@Override
	public PersonByPidDomainRequest convert(PersonInfoRequest domainObject) {
		PersonByPidDomainRequest providerObject = new PersonByPidDomainRequest();
		if (domainObject != null) {
			providerObject.setParticipantID(domainObject.getParticipantID());
		}
		return providerObject;
	}

}
