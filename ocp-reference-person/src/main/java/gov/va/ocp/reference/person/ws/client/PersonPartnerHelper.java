package gov.va.ocp.reference.person.ws.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import gov.va.ocp.reference.framework.log.OcpLogger;
import gov.va.ocp.reference.framework.log.OcpLoggerFactory;
import gov.va.ocp.reference.framework.messages.Message;
import gov.va.ocp.reference.framework.messages.MessageSeverity;
import gov.va.ocp.reference.partner.person.ws.client.PersonWsClientImpl;
import gov.va.ocp.reference.partner.person.ws.transfer.FindPersonByPtcpntId;
import gov.va.ocp.reference.partner.person.ws.transfer.FindPersonByPtcpntIdResponse;
import gov.va.ocp.reference.partner.person.ws.transfer.ObjectFactory;
import gov.va.ocp.reference.person.exception.PersonServiceException;
import gov.va.ocp.reference.person.model.PersonByPidDomainRequest;
import gov.va.ocp.reference.person.model.PersonByPidDomainResponse;
import gov.va.ocp.reference.person.transform.impl.PersonByPid_DomainToPartner;
import gov.va.ocp.reference.person.transform.impl.PersonByPid_PartnerToDomain;
import gov.va.ocp.reference.person.utils.StringUtil;

/**
 * Make external calls to the partner using the partner client.
 *
 * @author aburkholder
 */
@Component(PersonPartnerHelper.BEAN_NAME)
public class PersonPartnerHelper {
	public static final String BEAN_NAME = "personServiceHelper";
	/** Logger */
	private static final OcpLogger LOGGER = OcpLoggerFactory.getLogger(PersonPartnerHelper.class);

	/** String to prepend messages for re-thrown exceptions */
	private static final String THROWSTR = "Rethrowing the following exception:  ";

	/** String Constant NOPERSONFORPTCTID */
	private static final String NOPERSONFORPTCTID = "NOPERSONFORPTCTID";

	/** String Constant NO_PERSON_FOUND_FOR_PARTICIPANT_ID */
	private static final String NO_PERSON_FOUND_FOR_PARTICIPANT_ID = "No person found for participantID ";

	/** The Constant PERSON_OBJECT_FACTORY. */
	protected static final ObjectFactory PERSON_OBJECT_FACTORY = new ObjectFactory();

	/** WS client to run all intent to file operations via SOAP */
	@Autowired
	private PersonWsClientImpl personWsClient;

	/** Transformer for domain-to-partner model transformation */
	private PersonByPid_DomainToPartner personByPidD2P = new PersonByPid_DomainToPartner();

	/** Transformer for partner-to-domain model transformation */
	private PersonByPid_PartnerToDomain personByPidP2D = new PersonByPid_PartnerToDomain();

	/**
	 * Make the partner call to find person information by participant id.
	 *
	 * @param request the {@link PersonByPidDomainRequest} from the domain
	 * @return PersonByPidDomainResponse domain representation of the partner response
	 */
	public PersonByPidDomainResponse findPersonByPid(PersonByPidDomainRequest request) {

		FindPersonByPtcpntId partnerRequest = personByPidD2P.transform(request);

		FindPersonByPtcpntIdResponse partnerResponse = null;
		PersonByPidDomainResponse domainResponse = null;
		try {
			partnerResponse = personWsClient.getPersonInfoByPtcpntId(partnerRequest);
		} catch (final Exception clientException) {
			String message = THROWSTR + clientException.getClass().getName() + ": " + clientException.getMessage();
			LOGGER.error(message, clientException);
			throw new PersonServiceException(message, clientException);
		}

		domainResponse = personByPidP2D.transform(partnerResponse);

		LOGGER.debug("PersonByPidDomainResponse: {}",
				domainResponse == null ? "" : ToStringBuilder.reflectionToString(domainResponse));
		LOGGER.debug("FindPersonByPtcpntIdResponse: {}",
				partnerResponse == null ? "" : ToStringBuilder.reflectionToString(partnerResponse));

		List<Message> messages = checkPartnerResponse(request.getParticipantID(), partnerResponse);
		if (messages != null && !messages.isEmpty()) {
			domainResponse.addMessages(messages);
		}
		LOGGER.debug("PersonByPidDomainResponse after addMessages: {}",
				domainResponse == null ? "" : ToStringBuilder.reflectionToString(domainResponse));
		return domainResponse;
	}

	/**
	 * Check to make sure that a response was received, and that a participant ID exists on the partner response.
	 *
	 * @param participantID the participant ID
	 * @param partnerResponse
	 * @return
	 */
	private List<Message> checkPartnerResponse(Long participantID, FindPersonByPtcpntIdResponse partnerResponse) {
		List<Message> messages = null;

		final String maskedInfo = StringUtil.getMask4(participantID.toString());
		// Check for null response objects or incorrect PID
		if (partnerResponse == null || partnerResponse.getPersonDTO() == null
				|| partnerResponse.getPersonDTO().getPtcpntId() < 1
				|| !participantID.equals(partnerResponse.getPersonDTO().getPtcpntId())) {

			messages = new ArrayList<>();
			messages.add(new Message(MessageSeverity.ERROR, NOPERSONFORPTCTID,
					NO_PERSON_FOUND_FOR_PARTICIPANT_ID + maskedInfo, HttpStatus.BAD_REQUEST));
		}
		return messages;
	}
}
