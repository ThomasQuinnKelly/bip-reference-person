package gov.va.ocp.reference.person.transform.impl;

import gov.va.ocp.reference.partner.person.ws.transfer.FindPersonByPtcpntIdResponse;
import gov.va.ocp.reference.partner.person.ws.transfer.ObjectFactory;
import gov.va.ocp.reference.person.model.person.v1.PersonInfo;
import gov.va.ocp.reference.person.model.person.v1.PersonInfoResponse;
import gov.va.ocp.reference.person.transform.AbstractPartnerToDomain;

/**
 * Transform a partner client {@link FindPersonByPtcpntIdResponse} object to a domain {@link PersonInfoResponse} object.
 *
 * @author aburkholder
 */
public class PersonByPid_PartnerToDomain extends AbstractPartnerToDomain<FindPersonByPtcpntIdResponse, PersonInfoResponse> {

	/** String Constant NOPERSONFORPTCTID */
	private static final String NOPERSONFORPTCTID = "NOPERSONFORPTCTID";

	/** String Constant NO_PERSON_FOUND_FOR_PARTICIPANT_ID */
	private static final String NO_PERSON_FOUND_FOR_PARTICIPANT_ID = "No person found for participantID ";

	/** The Constant PERSON_OBJECT_FACTORY. */
	protected static final ObjectFactory PERSON_OBJECT_FACTORY = new ObjectFactory();

	/**
	 * Transform a partner {@link FindPersonByPtcpntIdResponse} into a domain {@link PersonInfoResponse} object.
	 * <p>
	 * {@inheritDoc AbstractPartnerToDomain}
	 */
	@Override
	public PersonInfoResponse transform(FindPersonByPtcpntIdResponse partnerObject) {
		PersonInfoResponse domainObject = new PersonInfoResponse();

		PersonInfo domainData = new PersonInfo();
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
