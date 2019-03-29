package gov.va.ocp.reference.person.transform.impl;

import gov.va.ocp.framework.transfer.transform.AbstractDomainToPartner;
import gov.va.ocp.reference.partner.person.ws.transfer.FindPersonByPtcpntId;
import gov.va.ocp.reference.person.model.PersonByPidDomainRequest;

/**
 * Transform a service Domain {@link PersonByPidDomainRequest} object to a Partner client {@link FindPersonByPtcpntId} requets object.
 *
 * @author aburkholder
 */
public class PersonByPid_DomainToPartner extends AbstractDomainToPartner<PersonByPidDomainRequest, FindPersonByPtcpntId> {

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
