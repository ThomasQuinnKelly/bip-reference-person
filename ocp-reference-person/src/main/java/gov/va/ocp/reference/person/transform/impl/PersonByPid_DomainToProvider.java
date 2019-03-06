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
	 * <br/>
	 * <b>Member objects inside the returned object may be {@code null}.</b>
	 * <p>
	 * {@inheritDoc AbstractDomainToProvider}
	 */
	@Override
	public PersonInfoResponse transform(PersonByPidDomainResponse domainObject) {
		PersonInfoResponse providerObject = new PersonInfoResponse();

		// add data
		PersonInfo providerData = new PersonInfo();
		if (domainObject != null && domainObject.getPersonInfo() != null) {
			providerData.setFileNumber(domainObject.getPersonInfo().getFileNumber());
			providerData.setFirstName(domainObject.getPersonInfo().getFirstName());
			providerData.setLastName(domainObject.getPersonInfo().getLastName());
			providerData.setMiddleName(domainObject.getPersonInfo().getMiddleName());
			providerData.setParticipantId(domainObject.getPersonInfo().getParticipantId());
			providerData.setSocSecNo(domainObject.getPersonInfo().getSocSecNo());
		}
		providerObject.setPersonInfo(providerData);
		// add messages
		if (domainObject.getMessages() != null && !domainObject.getMessages().isEmpty()) {
			for (gov.va.ocp.framework.messages.Message domainMsg : domainObject.getMessages()) {
				providerObject.add(domainMsg.getSeverity(), domainMsg.getKey(), domainMsg.getText(),
						domainMsg.getHttpStatus());
			}
		}

		return providerObject;
	}
}
