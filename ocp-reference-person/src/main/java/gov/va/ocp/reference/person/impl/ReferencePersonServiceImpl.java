package gov.va.ocp.reference.person.impl;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import gov.va.ocp.framework.exception.OcpException;
import gov.va.ocp.framework.exception.OcpRuntimeException;
import gov.va.ocp.framework.exception.interceptor.ExceptionHandlingUtils;
import gov.va.ocp.framework.messages.MessageSeverity;
import gov.va.ocp.framework.security.PersonTraits;
import gov.va.ocp.framework.security.SecurityUtils;
import gov.va.ocp.framework.util.Defense;
import gov.va.ocp.framework.util.OcpCacheUtil;
import gov.va.ocp.reference.person.ReferencePersonService;
import gov.va.ocp.reference.person.exception.PersonServiceException;
import gov.va.ocp.reference.person.model.PersonByPidDomainRequest;
import gov.va.ocp.reference.person.model.PersonByPidDomainResponse;
import gov.va.ocp.reference.person.utils.CacheConstants;
import gov.va.ocp.reference.person.utils.HystrixCommandConstants;
import gov.va.ocp.reference.person.ws.client.PersonPartnerHelper;

/**
 * Implementation class for the Reference Person Service.
 * The class demonstrates the implementation of hystrix circuit breaker
 * pattern for read operations. When there is a failure the fallback method is invoked and the response is
 * returned from the cache
 *
 * @author
 *
 */
@Service(value = ReferencePersonServiceImpl.BEAN_NAME)
@Component
@Qualifier("PERSON_SERVICE_IMPL")
@RefreshScope
@DefaultProperties(groupKey = HystrixCommandConstants.REFERENCE_PERSON_SERVICE_GROUP_KEY)
public class ReferencePersonServiceImpl implements ReferencePersonService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReferencePersonServiceImpl.class);

	/** Bean name constant */
	public static final String BEAN_NAME = "personServiceImpl";

	private static final String WARN_MESSAGE =
			"In a real service, this condition should throw a service exception (in this case, PersonServiceException) with INVOKE_FALLBACK_MESSAGE.";

	/** The person web service client helper. */
	@Autowired
	private PersonPartnerHelper personPartnerHelper;

	@Autowired
	private CacheManager cacheManager;

	/** Constant for the message when hystrix fallback method is manually invoked */
	private static final String INVOKE_FALLBACK_MESSAGE = "Could not get data from cache or partner - invoking fallback.";

	/**
	 * Viability checks before the application is put into service.
	 */
	@PostConstruct
	void postConstruct() {
		// Check for WS Client reference. Note that cacheManager is allowed to be null.
		Defense.notNull(personPartnerHelper,
				"Unable to proceed with Person Service request. The personPartnerHelper must not be null.");
	}

	/**
	 * Implementation of the service (domain) layer API.
	 * <p>
	 * {@inheritDoc}
	 *
	 * @Cacheable Annotation indicating that the result of invoking a method (or all methods in a class) can be cached.
	 */
	@Override
	@CachePut(value = CacheConstants.CACHENAME_REFERENCE_PERSON_SERVICE,
			key = "#root.methodName + T(gov.va.ocp.framework.util.OcpCacheUtil).createKey(#personByPidDomainRequest.participantID)",
			unless = "T(gov.va.ocp.framework.util.OcpCacheUtil).checkResultConditions(#result)")
	@HystrixCommand(fallbackMethod = "findPersonByParticipantIDFallBack", commandKey = "GetPersonInfoByPIDCommand",
			ignoreExceptions = {IllegalArgumentException.class, OcpException.class})
	public PersonByPidDomainResponse findPersonByParticipantID(final PersonByPidDomainRequest personByPidDomainRequest) {

		String cacheKey = "findPersonByParticipantID" + OcpCacheUtil.createKey(personByPidDomainRequest.getParticipantID());

		// try from cache
		PersonByPidDomainResponse response = null;
		try {
			if (cacheManager != null && cacheManager.getCache(CacheConstants.CACHENAME_REFERENCE_PERSON_SERVICE) != null
					&& cacheManager.getCache(CacheConstants.CACHENAME_REFERENCE_PERSON_SERVICE).get(cacheKey) != null) {
				LOGGER.debug("findPersonByParticipantID returning cached data");
				response =
						cacheManager.getCache(CacheConstants.CACHENAME_REFERENCE_PERSON_SERVICE).get(cacheKey,
								PersonByPidDomainResponse.class);
				return response;
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		// try from partner
		if (response == null) {
			LOGGER.debug("findPersonByParticipantID no cached data found");
			try {
				response = personPartnerHelper.findPersonByPid(personByPidDomainRequest);
			} catch (OcpException ocpException) {
				PersonByPidDomainResponse domainResponse = new PersonByPidDomainResponse();
				// check exception..create domain model response
				domainResponse.addMessage(ocpException.getSeverity(), ocpException.getKey(), ocpException.getMessage(), ocpException.getStatus());
				return domainResponse;
			}
		}

		/* TODO below checks belong in business validation, not in this class */

		// check if empty response, or errors / fatals
		if (response == null || response.getPersonInfo() == null
				&& !response.hasErrors() && !response.hasFatals()) {
			LOGGER.info("findPersonByParticipantID empty response - throwing PersonServiceException: " + INVOKE_FALLBACK_MESSAGE);
			throw new PersonServiceException("", INVOKE_FALLBACK_MESSAGE, MessageSeverity.FATAL, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		/*
		 * In a real-world service, it is highly unlikely that a user would be allowed
		 * to query for someone else's data. In general, responses should *always*
		 * contain only data for the logged-in person.
		 * Therefore, the checks below would typically throw an exception,
		 * not just set a warning.
		 */
		LOGGER.debug("Request PID: " + personByPidDomainRequest.getParticipantID()
				+ "; Response PID: " + response.getPersonInfo().getParticipantId()
				+ "; PersonTraits PID: "
				+ (SecurityUtils.getPersonTraits() == null ? "null" : SecurityUtils.getPersonTraits().getPid()));

		// check requested pid = returned pid
		if (!response.getPersonInfo().getParticipantId().equals(personByPidDomainRequest.getParticipantID())) {
			LOGGER.info("findPersonByParticipantID response has different PID than the request - throwing PersonServiceException: "
					+ INVOKE_FALLBACK_MESSAGE);
			response.addMessage(MessageSeverity.WARN, HttpStatus.OK.name(),
					"A different Participant ID was retrieved than the one requested. " + WARN_MESSAGE, HttpStatus.OK);
		}
		// check logged in user's pid matches returned pid
		PersonTraits personTraits = SecurityUtils.getPersonTraits();
		if (personTraits != null && StringUtils.isNotBlank(personTraits.getPid())) {
			if (response.getPersonInfo() != null
					&& response.getPersonInfo().getParticipantId() != null
					&& !personTraits.getPid().equals(response.getPersonInfo().getParticipantId().toString())) {
				LOGGER.info(
						"findPersonByParticipantID response has different PID than the logged in user - throwing PersonServiceException: "
								+ INVOKE_FALLBACK_MESSAGE);
				response.addMessage(MessageSeverity.WARN, HttpStatus.OK.name(),
						"A different Participant ID was retrieved than that of the logged in user. " + WARN_MESSAGE, HttpStatus.OK);
			}
		}
		return response;
	}

	/**
	 * Hystrix Fallback Method Which is Triggered When there Is An Unexpected Exception
	 * in findPersonByParticipantID method.
	 *
	 * @param personByPidDomainRequest The request from the Java Service.
	 * @param throwable the throwable
	 * @return A JAXB element for the WS request
	 */
	@HystrixCommand(commandKey = "FindPersonByParticipantIDFallBackCommand")
	public PersonByPidDomainResponse findPersonByParticipantIDFallBack(final PersonByPidDomainRequest personByPidDomainRequest,
			final Throwable throwable) {
		LOGGER.info("findPersonByParticipantIDFallBack has been activated");
		if (throwable != null) {
			LOGGER.debug(ReflectionToStringBuilder.toString(throwable, null, true, true, Throwable.class));

			throw ExceptionHandlingUtils.resolveRuntimeException(throwable);

		} else {
			LOGGER.error(
					"findPersonByParticipantIDFallBack No Throwable Exception. Just Raise Runtime Exception {}",
					personByPidDomainRequest);
			throw new OcpRuntimeException("", "There was a problem processing your request.", MessageSeverity.FATAL,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}