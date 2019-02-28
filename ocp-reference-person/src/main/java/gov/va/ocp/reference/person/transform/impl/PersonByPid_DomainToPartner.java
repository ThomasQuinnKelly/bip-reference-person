package gov.va.ocp.reference.person.transform.impl;

import gov.va.ocp.reference.partner.person.ws.transfer.FindPersonByPtcpntId;
import gov.va.ocp.reference.person.model.person.v1.PersonInfoRequest;
import gov.va.ocp.reference.person.transform.AbstractDomainToPartner;

/**
 * Transform a domain {@link PersonInfoRequest} object to a partner {@link FindPersonByPtcpntId} object.
 *
 * @author aburkholder
 */
public class PersonByPid_DomainToPartner extends AbstractDomainToPartner<PersonInfoRequest, FindPersonByPtcpntId> {

	/**
	 * Transform a domain {@libnk PersonInfoRequest} into a partner {@link FindPersonByPtcpntId} request.
	 * <p>
	 * {@inheritDoc AbstractDomainToPartner}
	 */
	@Override
	public FindPersonByPtcpntId transform(PersonInfoRequest domainObject) {
		FindPersonByPtcpntId partnerObject = new FindPersonByPtcpntId();
		partnerObject.setPtcpntId(domainObject.getParticipantID());
		return partnerObject;
	}

}
