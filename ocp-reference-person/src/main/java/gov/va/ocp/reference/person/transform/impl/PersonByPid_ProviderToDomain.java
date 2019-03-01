package gov.va.ocp.reference.person.transform.impl;

import gov.va.ocp.reference.partner.person.ws.transfer.FindPersonByPtcpntIdResponse;
import gov.va.ocp.reference.partner.person.ws.transfer.ObjectFactory;
import gov.va.ocp.reference.person.model.PersonInfoDomain;
import gov.va.ocp.reference.person.model.PersonByPidDomainResponse;
import gov.va.ocp.reference.person.transform.AbstractPartnerToDomain;

/**
 * Transform a partner client {@link FindPersonByPtcpntIdResponse} object to a domain {@link PersonByPidDomainResponse} object.
 *
 * @author aburkholder
 */
public class PersonByPid_ProviderToDomain extends AbstractPartnerToDomain<FindPersonByPtcpntIdResponse, PersonByPidDomainResponse> {

	/** The Constant PERSON_OBJECT_FACTORY. */
	protected static final ObjectFactory PERSON_OBJECT_FACTORY = new ObjectFactory();

	/**
	 * Transform a partner {@link FindPersonByPtcpntIdResponse} into a domain {@link PersonByPidDomainResponse} object.
	 * <p>
	 * {@inheritDoc AbstractPartnerToDomain}
	 */
	@Override
	public PersonByPidDomainResponse transform(FindPersonByPtcpntIdResponse partnerObject) {
		PersonByPidDomainResponse domainObject = new PersonByPidDomainResponse();

		PersonInfoDomain domainData = new PersonInfoDomain();
		if (partnerObject != null) {
			domainData.setFileNumber(partnerObject.getPersonDTO().getFileNbr());
			domainData.setFirstName(partnerObject.getPersonDTO().getFirstNm());
			domainData.setLastName(partnerObject.getPersonDTO().getLastNm());
			domainData.setMiddleName(partnerObject.getPersonDTO().getMiddleNm());
			domainData.setParticipantId(partnerObject.getPersonDTO().getPtcpntId());
			domainData.setSocSecNo(partnerObject.getPersonDTO().getSsnNbr());
		}

		domainObject.setPersonInfo(domainData);
		return domainObject;
	}

}
