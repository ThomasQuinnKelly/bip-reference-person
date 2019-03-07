package gov.va.ocp.reference.person.transform.impl;

import gov.va.ocp.framework.transfer.transform.AbstractPartnerToDomain;
import gov.va.ocp.reference.partner.person.ws.transfer.FindPersonByPtcpntIdResponse;
import gov.va.ocp.reference.partner.person.ws.transfer.ObjectFactory;
import gov.va.ocp.reference.person.model.PersonByPidDomainResponse;
import gov.va.ocp.reference.person.model.PersonInfoDomain;

/**
 * Transform a Partner client {@link FindPersonByPtcpntIdResponse} object to a service Domain {@link PersonByPidDomainResponse} object.
 *
 * @author aburkholder
 */
public class PersonByPid_PartnerToDomain extends AbstractPartnerToDomain<FindPersonByPtcpntIdResponse, PersonByPidDomainResponse> {

	/** The Constant PERSON_OBJECT_FACTORY. */
	protected static final ObjectFactory PERSON_OBJECT_FACTORY = new ObjectFactory();

	/**
	 * Transform a Partner {@link FindPersonByPtcpntIdResponse} into a service Domain {@link PersonByPidDomainResponse} object.
	 * <br/>
	 * <b>Member objects inside the returned object may be {@code null}.</b>
	 * <p>
	 * {@inheritDoc AbstractPartnerToDomain}
	 */
	@Override
	public PersonByPidDomainResponse convert(FindPersonByPtcpntIdResponse partnerObject) {
		PersonByPidDomainResponse domainObject = new PersonByPidDomainResponse();

		if (partnerObject != null && partnerObject.getPersonDTO() != null) {
			PersonInfoDomain domainData = new PersonInfoDomain();

			domainData.setFileNumber(partnerObject.getPersonDTO().getFileNbr());
			domainData.setFirstName(partnerObject.getPersonDTO().getFirstNm());
			domainData.setLastName(partnerObject.getPersonDTO().getLastNm());
			domainData.setMiddleName(partnerObject.getPersonDTO().getMiddleNm());
			domainData.setParticipantId(partnerObject.getPersonDTO().getPtcpntId());
			domainData.setSocSecNo(partnerObject.getPersonDTO().getSsnNbr());

			domainObject.setPersonInfo(domainData);
		}

		return domainObject;
	}

}
