package gov.va.ocp.reference.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import gov.va.ocp.reference.framework.exception.ReferenceRuntimeException;
import gov.va.ocp.reference.framework.messages.Message;
import gov.va.ocp.reference.framework.messages.MessageSeverity;
import gov.va.ocp.reference.framework.util.Defense;
import gov.va.ocp.reference.partner.person.ws.client.PersonWsClient;
import gov.va.ocp.reference.service.api.ReferencePersonService;
import gov.va.ocp.reference.service.exception.DemoServiceException;
import gov.va.ocp.reference.service.model.person.v1.PersonInfo;
import gov.va.ocp.reference.service.model.person.v1.PersonInfoRequest;
import gov.va.ocp.reference.service.model.person.v1.PersonInfoResponse;
import gov.va.ocp.reference.service.utils.HystrixCommandConstants;
import gov.va.ocp.reference.service.utils.StringUtil;
import gov.va.ocp.reference.partner.person.ws.transfer.FindPersonByPtcpntId;
import gov.va.ocp.reference.partner.person.ws.transfer.FindPersonByPtcpntIdResponse;
import gov.va.ocp.reference.partner.person.ws.transfer.ObjectFactory;
import gov.va.ocp.reference.partner.person.ws.transfer.PersonDTO;

@Service(value = ReferencePersonServiceImpl.BEAN_NAME)
@Component
@Qualifier("IMPL")
@RefreshScope
@DefaultProperties(groupKey = HystrixCommandConstants.REFERENCE_PERSON_SERVICE_GROUP_KEY)

/**
 * Implementation class for the Demo Person Service.
 * The class demonstrates the implementation of hystrix circuit breaker
 * pattern for read operations. When there is a failure the fallback method is invoked and the response is
 * returned from the cache
 *
 * @author
 *
 */
public class ReferencePersonServiceImpl implements ReferencePersonService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReferencePersonServiceImpl.class);

	private static final String CACHENAME_DEMO_PERSON_SERVICE = "demoPersonService";

	/** Bean name constant */
	public static final String BEAN_NAME = "personServiceImpl";

	/** The person web service client reference. */
	@Autowired
	private PersonWsClient personWsClient;

	@Autowired
	private CacheManager cacheManager;

	/** String Constant NOPERSONFORPTCTID */
	private static final String NOPERSONFORPTCTID = "NOPERSONFORPTCTID";

	/** String Constant NO_PERSON_FOUND_FOR_PARTICIPANT_ID */
	private static final String NO_PERSON_FOUND_FOR_PARTICIPANT_ID = "No person found for participantID ";

	/** The Constant PERSON_OBJECT_FACTORY. */
	protected static final ObjectFactory PERSON_OBJECT_FACTORY = new ObjectFactory();

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.va.ocp.reference.service.api.ReferencePersonService#findPersonByParticipantID
	 * (gov.va.ocp.reference.partner.person.ws.client.transfer.PersonInfoRequest)
	 *
	 * @Cacheable Annotation indicating that the result of invoking a method (or all methods in a class) can be cached.
	 */
	@Override
	@CachePut(value = "demoPersonService", key = "#personInfoRequest",
			unless = "#result == null || #result.personInfo == null || #result.hasErrors() || #result.hasFatals()")
	@HystrixCommand(
			fallbackMethod = "findPersonByParticipantIDFallBack",
			commandKey = "GetPersonInfoByPIDCommand",
			ignoreExceptions = { IllegalArgumentException.class })
	public PersonInfoResponse findPersonByParticipantID(final PersonInfoRequest personInfoRequest) {

		// Check for valid input arguments and WS Client reference.
		Defense.notNull(personWsClient, "Unable to proceed with Person Service request. The personWsClient must not be null.");
		Defense.notNull(personInfoRequest.getParticipantID(), "Invalid argument, pid must not be null.");

		if (cacheManager.getCache(CACHENAME_DEMO_PERSON_SERVICE) != null
				&& cacheManager.getCache(CACHENAME_DEMO_PERSON_SERVICE).get(personInfoRequest) != null) {
			return cacheManager.getCache(CACHENAME_DEMO_PERSON_SERVICE).get(personInfoRequest, PersonInfoResponse.class);
		} else {
			// Prepare the WS request
			final FindPersonByPtcpntId findPersonByPtcpntIdRequestElement = createFindPersonByPidRequest(personInfoRequest);
// TODO
//			// Invoke the Person Web Service via the WS Client
//			final JAXBElement<FindPersonByPtcpntIdResponse> findPersonByPtcpntIdResponseElement =
//					personWsClient.getPersonInfoByPtcpntId(findPersonByPtcpntIdRequestElement);
//
//			// Prepare the service response
//			return createPersonInfoResponse(findPersonByPtcpntIdResponseElement, personInfoRequest.getParticipantID());
			return null;
		}
	}

	/**
	 * Hystrix Fallback Method Which is Triggered When there Is An Unexpected Exception
	 * in getPersonInfo.
	 *
	 * @param personInfoRequest The request from the Java Service.
	 * @param throwable the throwable
	 * @return A JAXB element for the WS request
	 */
	@HystrixCommand(commandKey = "GetPersonInfoFallBackCommand")
	public PersonInfoResponse getPersonInfoFallBack(final PersonInfoRequest personInfoRequest, final Throwable throwable) {
		final PersonInfoResponse response = new PersonInfoResponse();
		if (throwable != null) {
			LOGGER.error("Exception occurred in getPersonInfoFallBack {}", throwable);
			throw new DemoServiceException("Error: " + throwable.toString());
		} else {
			return response;
		}
	}

	/**
	 * Hystrix Fallback Method Which is Triggered When there Is An Unexpected Exception
	 * in findPersonByParticipantID method.
	 *
	 * @param personInfoRequest The request from the Java Service.
	 * @param throwable the throwable
	 * @return A JAXB element for the WS request
	 */
	@HystrixCommand(commandKey = "FindPersonByParticipantIDFallBackCommand")
	public PersonInfoResponse findPersonByParticipantIDFallBack(final PersonInfoRequest personInfoRequest, final Throwable throwable) {
		final PersonInfoResponse response = new PersonInfoResponse();
		if (throwable != null) {
			final String msg = throwable.getMessage();
			final List<Message> messages = new ArrayList<>();
			messages.add(newMessage(MessageSeverity.FATAL, "FATAL", msg));
			response.setMessages(messages);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(msg);
			}
			return response;
		} else {
			LOGGER.error(
					"findPersonByParticipantIDFallBack No Throwable Exception and No Cached Data. Just Raise Runtime Exception {}",
					personInfoRequest);
			throw new ReferenceRuntimeException("There was a problem processing your request.");
		}
	}

	/**
	 * Helper method to create a Message object.
	 *
	 * @param severity the severity
	 * @param key the key
	 * @param text the text
	 * @return the message
	 */
	private final Message newMessage(final MessageSeverity severity, final String key, final String text) {
		final Message msg = new Message();
		msg.setSeverity(severity);
		msg.setKey(key);
		msg.setText(text);
		return msg;
	}

	/**
	 * @param personInfoRequest The request from the Java Service.
	 * @return A JAXB element for the WS request
	 */
	private FindPersonByPtcpntId createFindPersonByPidRequest(final PersonInfoRequest personInfoRequest) {

		final FindPersonByPtcpntId findPersonByPtcpntId = new FindPersonByPtcpntId();
		findPersonByPtcpntId.setPtcpntId(personInfoRequest.getParticipantID());

//		TODO
//		return PERSON_OBJECT_FACTORY.createFindPersonByPtcpntId(findPersonByPtcpntId);
		return null;
	}

	/**
	 * @param findPersonByPtcpntIdResponseElement The response JAXB element
	 * @param participantID The person's participantID
	 * @return the person info response
	 */
	private PersonInfoResponse createPersonInfoResponse(
			final FindPersonByPtcpntIdResponse findPersonByPtcpntIdResponseElement, final Long participantID) {

		final PersonInfoResponse personInfoResponse = new PersonInfoResponse();
		final String maskedInfo = StringUtil.getMask4(participantID.toString());
		// Check for null xml element
		if (findPersonByPtcpntIdResponseElement == null) {

			personInfoResponse.addMessage(MessageSeverity.ERROR, NOPERSONFORPTCTID,
					NO_PERSON_FOUND_FOR_PARTICIPANT_ID + maskedInfo);

		} else {
			final FindPersonByPtcpntIdResponse findPersonByPtcpntIdResponse = findPersonByPtcpntIdResponseElement;
			// Check for null response object
			if (findPersonByPtcpntIdResponse == null) {

				personInfoResponse.addMessage(MessageSeverity.ERROR, NOPERSONFORPTCTID,
						NO_PERSON_FOUND_FOR_PARTICIPANT_ID + maskedInfo);

			} else {
				final PersonDTO personDto = findPersonByPtcpntIdResponse.getPersonDTO();
				// If no DTO was returned, the PID did not match a person.
				if (personDto == null) {

					personInfoResponse.addMessage(MessageSeverity.ERROR, NOPERSONFORPTCTID,
							NO_PERSON_FOUND_FOR_PARTICIPANT_ID + maskedInfo);

				} else {
					// Copy the data of interest to the person info and add it to the service response.
					final PersonInfo personInfo = createPersonInfo(personDto);
					personInfoResponse.setPersonInfo(personInfo);
				}
			}
		}
		return personInfoResponse;
	}

	/**
	 * @param personDto The person DTO
	 * @return A PersonInfo object containing data from the DTO
	 */
	private PersonInfo createPersonInfo(final PersonDTO personDto) {
		final PersonInfo personInfo = new PersonInfo();
		personInfo.setFileNumber(personDto.getFileNbr());
		personInfo.setFirstName(personDto.getFirstNm());
		personInfo.setMiddleName(personDto.getMiddleNm());
		personInfo.setLastName(personDto.getLastNm());
		personInfo.setParticipantId(personDto.getPtcpntId());
		personInfo.setSocSecNo(personDto.getSsnNbr());
		return personInfo;
	}

}