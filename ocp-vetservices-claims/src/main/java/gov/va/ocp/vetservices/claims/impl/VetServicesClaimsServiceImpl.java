package gov.va.ocp.vetservices.claims.impl;

import java.util.ArrayList;
import java.util.List;

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

import  gov.va.ocp.vetservices.claims.utils.HystrixCommandConstants;
import gov.va.ocp.framework.exception.OcpRuntimeException;
import gov.va.ocp.framework.messages.MessageSeverity;
import gov.va.ocp.framework.messages.ServiceMessage;
import gov.va.ocp.framework.util.OcpCacheUtil;
import gov.va.ocp.vetservices.claims.utils.CacheConstants;
import gov.va.ocp.vetservices.claims.VetServicesClaimsService;
import gov.va.ocp.vetservices.claims.model.AllClaimsDomainRequest;
import gov.va.ocp.vetservices.claims.model.ClaimDetailByIdDomainRequest;
import gov.va.ocp.vetservices.claims.model.ClaimDetailByIdDomainResponse;
import gov.va.ocp.vetservices.claims.model.AllClaimsDomainResponse;
import gov.va.ocp.vetservices.claims.orm.ClaimsDataHelper;

@Service(value = VetServicesClaimsServiceImpl.BEAN_NAME)
@Component
@Qualifier("IMPL")
@RefreshScope
@DefaultProperties(groupKey = HystrixCommandConstants.VETSERVICES_CLAIMS_SERVICE_GROUP_KEY)
public class VetServicesClaimsServiceImpl implements VetServicesClaimsService { 
	
	private static final Logger LOGGER = LoggerFactory.getLogger(VetServicesClaimsServiceImpl.class);

	/** Bean name constant */
	public static final String BEAN_NAME = "vetServicesClaimsServiceImpl";

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	ClaimsDataHelper claimsDataHelper;
	
	/**
	 * Returns the claim detail for a given claim id.
	 *
	 * @param request the request
	 * @return the claim detail by id
	 */
	@CachePut(value = gov.va.ocp.vetservices.claims.utils.CacheConstants.CACHENAME_VETSERVICES_CLAIMS_SERVICE,
			key = "#root.methodName + T(gov.va.ocp.framework.util.OcpCacheUtil).createKey(#claimDetailByIdDomainRequest.id)",
			unless = "T(gov.va.ocp.framework.util.OcpCacheUtil).checkResultConditions(#result)")
	@HystrixCommand(fallbackMethod = "getClaimDetailByIdFallBack", commandKey = "getClaimDetailByIdCommand",
			ignoreExceptions = { IllegalArgumentException.class })
    public ClaimDetailByIdDomainResponse getClaimDetailById(ClaimDetailByIdDomainRequest claimDetailByIdDomainRequest) {
		String cacheKey = "getClaimDetailById" + OcpCacheUtil.createKey(claimDetailByIdDomainRequest.getId());
		
		ClaimDetailByIdDomainResponse claimDetailByIdDomainResponse = null;
    	
    	try {
			if (cacheManager != null && cacheManager.getCache(CacheConstants.CACHENAME_VETSERVICES_CLAIMS_SERVICE) != null
					&& cacheManager.getCache(CacheConstants.CACHENAME_VETSERVICES_CLAIMS_SERVICE).get(cacheKey) != null) {
				LOGGER.debug("getClaimDetailById returning cached data");
				claimDetailByIdDomainResponse =
						cacheManager.getCache(CacheConstants.CACHENAME_VETSERVICES_CLAIMS_SERVICE).get(cacheKey,
								ClaimDetailByIdDomainResponse.class);
				return claimDetailByIdDomainResponse;
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
    	
		claimDetailByIdDomainResponse = claimsDataHelper.getClaimDetailById(claimDetailByIdDomainRequest);
		return claimDetailByIdDomainResponse;
    }
	
	/**
	 * Hystrix Fallback Method Which is Triggered When there Is An Unexpected Exception
	 * in getClaimDetailById method.
	 *
	 * @param claimDetailByIdDomainRequest The request from the Java Service.
	 * @param throwable the throwable
	 * @return A JAXB element for the WS request
	 */
	@HystrixCommand(commandKey = "getClaimDetailByIdFallBackCommand")
	public ClaimDetailByIdDomainResponse getClaimDetailByIdFallBack(
			final ClaimDetailByIdDomainRequest claimDetailByIdDomainRequest, final Throwable throwable) {
		LOGGER.info("Hystrix getClaimDetailByIdFallBack has been activated");
		final ClaimDetailByIdDomainResponse response = new ClaimDetailByIdDomainResponse();
		if (throwable != null) {
			LOGGER.debug(ReflectionToStringBuilder.toString(throwable, null, true, true, Throwable.class));

			final String msg = throwable.getMessage();
			final List<ServiceMessage> serviceMessages = new ArrayList<>();
			serviceMessages.add(newMessage(MessageSeverity.FATAL, "FATAL", msg));
			response.setMessages(serviceMessages);

			if (response != null) {
				response.setDoNotCacheResponse(true);
			}
			return response;
		} else {
			LOGGER.error(
					"getClaimDetailByIdFallBack - No Throwable Exception and No Cached Data. Just Raise Runtime Exception {}",
					claimDetailByIdDomainRequest);
			throw new OcpRuntimeException("", "There was a problem processing your request.", MessageSeverity.FATAL,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}	
    
	/**
	 * Returns all claims
	 *
	 * @return the claims
	 */
	@CachePut(value = CacheConstants.CACHENAME_VETSERVICES_CLAIMS_SERVICE, key = "#root.methodName + T(gov.va.ocp.framework.util.OcpCacheUtil).getUserBasedKey()", unless = "T(gov.va.ocp.framework.util.OcpCacheUtil).checkResultConditions(#result)")
	@HystrixCommand(fallbackMethod = "getClaimsFallBack", commandKey = "getClaimsCommand", ignoreExceptions = {
			IllegalArgumentException.class })
	public AllClaimsDomainResponse getClaims(AllClaimsDomainRequest allClaimsDomainRequest) {
		String cacheKey = "getClaims" + OcpCacheUtil.getUserBasedKey();

		AllClaimsDomainResponse claimsDomainResponse = null;

		try {
			if (cacheManager != null
					&& cacheManager.getCache(CacheConstants.CACHENAME_VETSERVICES_CLAIMS_SERVICE) != null
					&& cacheManager.getCache(CacheConstants.CACHENAME_VETSERVICES_CLAIMS_SERVICE)
							.get(cacheKey) != null) {
				LOGGER.debug("getClaims returning cached data");
				claimsDomainResponse = cacheManager.getCache(CacheConstants.CACHENAME_VETSERVICES_CLAIMS_SERVICE)
						.get(cacheKey, AllClaimsDomainResponse.class);
				return claimsDomainResponse;
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		claimsDomainResponse = claimsDataHelper.getClaims();
		return claimsDomainResponse;
	}
	
	/**
	 * Hystrix Fallback Method Which is Triggered When there Is An Unexpected Exception
	 * in getClaims method.
	 *
	 * @param throwable the throwable
	 * @return A JAXB element for the WS request
	 */
	@HystrixCommand(commandKey = "getClaimsFallBackCommand")
	public AllClaimsDomainResponse getClaimsFallBack(final Throwable throwable) {
		LOGGER.info("Hystrix getClaimsFallBack has been activated");
		final AllClaimsDomainResponse claimsDomainResponse = new AllClaimsDomainResponse();
		if (throwable != null) {
			LOGGER.debug(ReflectionToStringBuilder.toString(throwable, null, true, true, Throwable.class));

			final String msg = throwable.getMessage();
			final List<ServiceMessage> serviceMessages = new ArrayList<>();
			serviceMessages.add(newMessage(MessageSeverity.FATAL, "FATAL", msg));
			claimsDomainResponse.setMessages(serviceMessages);

			if (claimsDomainResponse != null) {
				claimsDomainResponse.setDoNotCacheResponse(true);
			}
			return claimsDomainResponse;
		} else {
			LOGGER.error(
					"getClaimsFallBack - No Throwable Exception and No Cached Data. Just Raise Runtime Exception");
			throw new OcpRuntimeException("", "There was a problem processing your request.", MessageSeverity.FATAL,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
    
	/**
	 * Helper method to create a ServiceMessage object.
	 *
	 * @param severity the severity
	 * @param key the key
	 * @param text the text
	 * @return the message
	 */
	private final ServiceMessage newMessage(final MessageSeverity severity, final String key, final String text) {
		final ServiceMessage msg = new ServiceMessage();
		msg.setSeverity(severity);
		msg.setKey(key);
		msg.setText(text);
		return msg;
	}
}
