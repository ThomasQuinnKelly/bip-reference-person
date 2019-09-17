package gov.va.bip.reference.person.transform.impl;

import gov.va.bip.framework.transfer.transform.AbstractDomainToPartner;
import gov.va.bip.reference.person.model.PersonByPidDomainRequest;
import gov.va.bip.reference.partner.person.ws.transfer.FindPersonByPtcpntId;

/**
 * Transform a service Domain {@link PersonByPidDomainRequest} object to a Partner client {@link FindPersonByPtcpntId} request object.
 *
 * @author aburkholder
 */
public class PersonByPidDomainToPartner extends AbstractDomainToPartner<PersonByPidDomainRequest, FindPersonByPtcpntId> {

	/**
	 * Transform a service Domain {@link PersonByPidDomainRequest} into a Partner client {@link FindPersonByPtcpntId} request object.
	 * <p>
	 * {@inheritDoc AbstractDomainToPartner}
	 */
	@Override
	public FindPersonByPtcpntId convert(PersonByPidDomainRequest domainObject) {
		FindPersonByPtcpntId partnerObject = new FindPersonByPtcpntId();
		if (domainObject != null) {
			partnerObject.setPtcpntId(domainObject.getParticipantID());
		}
		return partnerObject;
	}

}
