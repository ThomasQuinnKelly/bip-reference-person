package gov.va.bip.vetservices.claims.impl;

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

import gov.va.bip.framework.cache.BipCacheUtil;
import gov.va.bip.framework.exception.BipRuntimeException;
import gov.va.bip.vetservices.claims.VetServicesClaimsService;
import gov.va.bip.vetservices.claims.model.AllClaimsDomainRequest;
import gov.va.bip.vetservices.claims.model.AllClaimsDomainResponse;
import gov.va.bip.vetservices.claims.model.ClaimDetailByIdDomainRequest;
import gov.va.bip.vetservices.claims.model.ClaimDetailByIdDomainResponse;
import gov.va.bip.vetservices.claims.orm.ClaimsDataHelper;
import gov.va.bip.vetservices.claims.utils.CacheConstants;
import gov.va.bip.vetservices.claims.utils.HystrixCommandConstants;

/**
 * Claims Service implemnetation.
 * @author rajuthota
 *
 */
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
	 * @param claimDetailByIdDomainRequest the claim detail by id domain request
	 * @return the claim detail by id
	 */
	@CachePut(value = gov.va.bip.vetservices.claims.utils.CacheConstants.CACHENAME_VETSERVICES_CLAIMS_SERVICE,
			key = "#root.methodName + T(gov.va.bip.framework.cache.BipCacheUtil).createKey(#claimDetailByIdDomainRequest.id)",
			unless = "T(gov.va.bip.framework.cache.BipCacheUtil).checkResultConditions(#result)")
	@HystrixCommand(commandKey = "getClaimDetailById", ignoreExceptions = { IllegalArgumentException.class, BipRuntimeException.class })
	public ClaimDetailByIdDomainResponse getClaimDetailById(ClaimDetailByIdDomainRequest claimDetailByIdDomainRequest) {
		String cacheKey = "getClaimDetailById" + BipCacheUtil.createKey(claimDetailByIdDomainRequest.getId());

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

		try {
			claimDetailByIdDomainResponse = claimsDataHelper.getClaimDetailById(claimDetailByIdDomainRequest);
		} catch (BipRuntimeException bipRuntimeException) {
			ClaimDetailByIdDomainResponse response = new ClaimDetailByIdDomainResponse();
			// check exception..create domain model response
			response.addMessage(bipRuntimeException.getSeverity(), bipRuntimeException.getStatus(), bipRuntimeException.getMessageKey(),
					bipRuntimeException.getParams());
			return response;
		}
		return claimDetailByIdDomainResponse;
	}

	/**
	 * Returns all claims
	 *
	 * @return the claims
	 */
	@CachePut(value = CacheConstants.CACHENAME_VETSERVICES_CLAIMS_SERVICE, 
			key = "#root.methodName + T(gov.va.bip.framework.cache.BipCacheUtil).getUserBasedKey()", 
			unless = "T(gov.va.bip.framework.cache.BipCacheUtil).checkResultConditions(#result)")
	@HystrixCommand(commandKey = "getClaimsCommand", ignoreExceptions = { IllegalArgumentException.class })
	public AllClaimsDomainResponse getClaims(AllClaimsDomainRequest allClaimsDomainRequest) {
		String cacheKey = "getClaims" + BipCacheUtil.getUserBasedKey();

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
}
