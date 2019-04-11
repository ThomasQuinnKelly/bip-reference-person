package gov.va.bip.reference.person.impl;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import gov.va.bip.framework.cache.BipCacheUtil;
import gov.va.bip.framework.exception.BipException;
import gov.va.bip.framework.exception.BipRuntimeException;
import gov.va.bip.framework.messages.MessageKeys;
import gov.va.bip.framework.messages.MessageSeverity;
import gov.va.bip.framework.validation.Defense;
import gov.va.bip.reference.person.ReferencePersonService;
import gov.va.bip.reference.person.client.ws.PersonPartnerHelper;
import gov.va.bip.reference.person.model.PersonByPidDomainRequest;
import gov.va.bip.reference.person.model.PersonByPidDomainResponse;
import gov.va.bip.reference.person.utils.CacheConstants;
import gov.va.bip.reference.person.utils.HystrixCommandConstants;

/**
 * Implementation class for the Reference Person Service.
 * The class demonstrates the implementation of hystrix circuit breaker
 * pattern for read operations. When there is a failure the fallback method is invoked and the response is
 * returned from the cache
 *
 * @author akulkarni
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

	/** The person web service client helper. */
	@Autowired
	private PersonPartnerHelper personPartnerHelper;

	@Autowired
	private CacheManager cacheManager;

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
	 */
	@Override
	@CachePut(value = CacheConstants.CACHENAME_REFERENCE_PERSON_SERVICE,
			key = "#root.methodName + T(gov.va.bip.framework.cache.BipCacheUtil).createKey(#personByPidDomainRequest.participantID)",
			unless = "T(gov.va.bip.framework.cache.BipCacheUtil).checkResultConditions(#result)")
	@HystrixCommand(commandKey = "GetPersonInfoByPIDCommand",
			ignoreExceptions = { IllegalArgumentException.class, BipException.class, BipRuntimeException.class })
	public PersonByPidDomainResponse findPersonByParticipantID(final PersonByPidDomainRequest personByPidDomainRequest) {

		String cacheKey = "findPersonByParticipantID" + BipCacheUtil.createKey(personByPidDomainRequest.getParticipantID());

		// try from cache
		PersonByPidDomainResponse response = null;
		try {
			Cache cache = null;
			if (cacheManager != null && (cache = cacheManager.getCache(CacheConstants.CACHENAME_REFERENCE_PERSON_SERVICE)) != null
					&& cache.get(cacheKey) != null) {
				LOGGER.debug("findPersonByParticipantID returning cached data");
				response = cache.get(cacheKey, PersonByPidDomainResponse.class);
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
			} catch (BipException | BipRuntimeException bipException) {
				PersonByPidDomainResponse domainResponse = new PersonByPidDomainResponse();
				// check exception..create domain model response
				domainResponse.addMessage(bipException.getSeverity(), bipException.getStatus(), bipException.getMessageKey(),
						bipException.getParams());
				return domainResponse;
			}
		}

		return response;
	}

	/**
	 * Support graceful degradation in a Hystrix command by adding a fallback method that Hystrix will call to obtain a
	 * default value or values in case the main command fails for findPersonByParticipantID <br/>
	 * <br/>
	 *
	 * See {https://github.com/Netflix/Hystrix/wiki/How-To-Use#fallback} for Hystrix Fallback usage <br/>
	 * <br/>
	 *
	 * Hystrix doesn't REQUIRE you to set this method. Unless you want to return a default data or add business logic for that case,
	 * .* If you throw an exception you'll "confuse" Hystrix and it will throw an HystrixRuntimeException.
	 *
	 * @param personByPidDomainRequest The request from the Java Service.
	 * @param throwable the throwable
	 * @return A JAXB element for the WS request
	 */
	@HystrixCommand(commandKey = "FindPersonByParticipantIDFallBackCommand")
	public PersonByPidDomainResponse findPersonByParticipantIDFallBack(final PersonByPidDomainRequest personByPidDomainRequest,
			final Throwable throwable) {
		LOGGER.info("findPersonByParticipantIDFallBack has been activated");

		/**
		 * Fallback Method for Demonstration Purpose. In this use case, there is no static / mock data
		 * that can be sent back to the consumers. Hence the method isn't configured as fallback.
		 *
		 * If needed to be configured, add annotation to the implementation method "findPersonByParticipantID" as below
		 *
		 * @HystrixCommand(fallbackMethod = "findPersonByParticipantIDFallBack")
		 */
		final PersonByPidDomainResponse response = new PersonByPidDomainResponse();
		response.setDoNotCacheResponse(true);

		if (throwable != null) {
			LOGGER.debug(ReflectionToStringBuilder.toString(throwable, null, true, true, Throwable.class));
			response.addMessage(MessageSeverity.WARN, HttpStatus.OK, MessageKeys.BIP_GLOBAL_GENERAL_EXCEPTION,
					throwable.getClass().getSimpleName(), throwable.getLocalizedMessage());
		} else {
			LOGGER.error(
					"findPersonByParticipantIDFallBack No Throwable Exception. Just Raise Runtime Exception {}",
					personByPidDomainRequest);
			response.addMessage(MessageSeverity.WARN, HttpStatus.OK, MessageKeys.WARN_KEY,
					"There was a problem processing your request.");
		}
		return response;
	}
}