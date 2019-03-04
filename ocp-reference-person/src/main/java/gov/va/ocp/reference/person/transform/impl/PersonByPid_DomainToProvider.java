package gov.va.ocp.reference.person.transform.impl;

import gov.va.ocp.reference.person.api.model.v1.PersonInfo;
import gov.va.ocp.reference.person.api.model.v1.PersonInfoResponse;
import gov.va.ocp.reference.person.model.PersonByPidDomainResponse;
import gov.va.ocp.reference.person.transform.AbstractDomainToProvider;

/**
 * Transform a service Domain {@link PersonByPidDomainResponse} into a REST Provider {@link PersonInfoResponse} object.
 *
 * @author aburkholder
 */
public class PersonByPid_DomainToProvider extends AbstractDomainToProvider<PersonByPidDomainResponse, PersonInfoResponse> {

	/**
	 * Transform a service Domain {@link PersonByPidDomainResponse} into a REST Provider {@link PersonInfoResponse} object.
	 * <p>
	 * {@inheritDoc AbstractDomainToProvider}
	 */
	@Override
	public PersonInfoResponse transform(PersonByPidDomainResponse domainObject) {
		PersonInfoResponse providerObject = new PersonInfoResponse();

		PersonInfo providerData = new PersonInfo();
		if (domainObject != null) {
			providerData.setFileNumber(domainObject.getPersonInfo().getFileNumber());
			providerData.setFirstName(domainObject.getPersonInfo().getFirstName());
			providerData.setLastName(domainObject.getPersonInfo().getLastName());
			providerData.setMiddleName(domainObject.getPersonInfo().getMiddleName());
			providerData.setParticipantId(domainObject.getPersonInfo().getParticipantId());
			providerData.setSocSecNo(domainObject.getPersonInfo().getSocSecNo());
		}

		providerObject.setPersonInfo(providerData);
		return providerObject;
	}

}
