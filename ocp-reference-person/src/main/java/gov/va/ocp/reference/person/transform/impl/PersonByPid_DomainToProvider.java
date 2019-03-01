package gov.va.ocp.reference.person.transform.impl;

import gov.va.ocp.reference.partner.person.ws.transfer.FindPersonByPtcpntId;
import gov.va.ocp.reference.person.model.PersonByPidDomainRequest;
import gov.va.ocp.reference.person.transform.AbstractDomainToPartner;

/**
 * Transform a domain {@link PersonByPidDomainRequest} object to a provider {@link FindPersonByPtcpntId} object.
 *
 * @author aburkholder
 */
public class PersonByPid_DomainToProvider extends AbstractDomainToPartner<PersonByPidDomainRequest, FindPersonByPtcpntId> {

	/**
	 * Transform a domain {@libnk PersonByPidDomainRequest} into a provider {@link FindPersonByPtcpntId} request.
	 * <p>
	 * {@inheritDoc AbstractDomainToProvider}
	 */
	@Override
	public FindPersonByPtcpntId transform(PersonByPidDomainRequest domainObject) {
		FindPersonByPtcpntId providerObject = new FindPersonByPtcpntId();
		providerObject.setPtcpntId(domainObject.getParticipantID());
		return providerObject;
	}

}
