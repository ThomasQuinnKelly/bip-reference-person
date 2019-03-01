package gov.va.ocp.reference.person.transform.impl;

import gov.va.ocp.reference.partner.person.ws.transfer.FindPersonByPtcpntId;
import gov.va.ocp.reference.person.model.PersonByPidDomainRequest;
import gov.va.ocp.reference.person.transform.AbstractDomainToPartner;

/**
 * Transform a domain {@link PersonByPidDomainRequest} object to a partner {@link FindPersonByPtcpntId} object.
 *
 * @author aburkholder
 */
public class PersonByPid_DomainToPartner extends AbstractDomainToPartner<PersonByPidDomainRequest, FindPersonByPtcpntId> {

	/**
	 * Transform a domain {@libnk PersonByPidDomainRequest} into a partner {@link FindPersonByPtcpntId} request.
	 * <p>
	 * {@inheritDoc AbstractDomainToPartner}
	 */
	@Override
	public FindPersonByPtcpntId transform(PersonByPidDomainRequest domainObject) {
		FindPersonByPtcpntId partnerObject = new FindPersonByPtcpntId();
		partnerObject.setPtcpntId(domainObject.getParticipantID());
		return partnerObject;
	}

}
