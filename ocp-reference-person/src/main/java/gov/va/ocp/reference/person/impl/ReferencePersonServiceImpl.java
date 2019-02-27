package gov.va.ocp.reference.person.impl;

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
import gov.va.ocp.reference.partner.person.ws.transfer.ObjectFactory;
import gov.va.ocp.reference.person.api.ReferencePersonService;
import gov.va.ocp.reference.person.model.person.v1.PersonInfoRequest;
import gov.va.ocp.reference.person.model.person.v1.PersonInfoResponse;
import gov.va.ocp.reference.person.utils.HystrixCommandConstants;
import gov.va.ocp.reference.person.ws.client.PersonServiceHelper;

@Service(value = ReferencePersonServiceImpl.BEAN_NAME)
@Component
@Qualifier("IMPL")
@RefreshScope
@DefaultProperties(groupKey = HystrixCommandConstants.REFERENCE_PERSON_SERVICE_GROUP_KEY)
/**
 * Implementation class for the Reference Person Service.
 * The class demonstrates the implementation of hystrix circuit breaker
 * pattern for read operations. When there is a failure the fallback method is invoked and the response is
 * returned from the cache
 *
 * @author
 *
 */
public class ReferencePersonServiceImpl implements ReferencePersonService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReferencePersonServiceImpl.class);

	private static final String CACHENAME_REFERENCE_PERSON_SERVICE = "refPersonService";

	/** Bean name constant */
	public static final String BEAN_NAME = "personServiceImpl";

	/** The person web service client helper. */
	@Autowired
	private PersonServiceHelper personServiceHelper;

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
	 * @see gov.va.ocp.reference.person.api.ReferencePersonService#findPersonByParticipantID
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
		Defense.notNull(personServiceHelper,
				"Unable to proceed with Person Service request. The personServiceHelper must not be null.");
		Defense.notNull(personInfoRequest.getParticipantID(), "Invalid argument, pid must not be null.");

		PersonInfoResponse response = null;
		if (cacheManager.getCache(CACHENAME_REFERENCE_PERSON_SERVICE) != null
				&& cacheManager.getCache(CACHENAME_REFERENCE_PERSON_SERVICE).get(personInfoRequest) != null) {
			response = cacheManager.getCache(CACHENAME_REFERENCE_PERSON_SERVICE).get(personInfoRequest, PersonInfoResponse.class);
		} else {
			response = personServiceHelper.findPersonByPid(personInfoRequest);
		}
		return response;
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
		LOGGER.info("Hystrix findPersonByParticipantIDFallBack has been activated");
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
}