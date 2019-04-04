package gov.va.ocp.vetservices.claims.impl;

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

import  gov.va.ocp.vetservices.claims.utils.HystrixCommandConstants;
import gov.va.ocp.framework.exception.OcpRuntimeException;
import gov.va.ocp.framework.cache.OcpCacheUtil;
import gov.va.ocp.vetservices.claims.utils.CacheConstants;
import gov.va.ocp.vetservices.claims.VetServicesClaimsService;
import gov.va.ocp.vetservices.claims.model.AllClaimsDomainRequest;
import gov.va.ocp.vetservices.claims.model.ClaimDetailByIdDomainRequest;
import gov.va.ocp.vetservices.claims.model.ClaimDetailByIdDomainResponse;
import gov.va.ocp.vetservices.claims.model.AllClaimsDomainResponse;
import gov.va.ocp.vetservices.claims.orm.ClaimsDataHelper;

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
	@CachePut(value = gov.va.ocp.vetservices.claims.utils.CacheConstants.CACHENAME_VETSERVICES_CLAIMS_SERVICE,
			key = "#root.methodName + T(gov.va.ocp.framework.cache.OcpCacheUtil).createKey(#claimDetailByIdDomainRequest.id)",
			unless = "T(gov.va.ocp.framework.cache.OcpCacheUtil).checkResultConditions(#result)")
	@HystrixCommand(commandKey = "getClaimDetailById", ignoreExceptions = { IllegalArgumentException.class, OcpRuntimeException.class })
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

		try {
			claimDetailByIdDomainResponse = claimsDataHelper.getClaimDetailById(claimDetailByIdDomainRequest);
		} catch (OcpRuntimeException ocpRuntimeException) {
			ClaimDetailByIdDomainResponse response = new ClaimDetailByIdDomainResponse();
			// check exception..create domain model response
			response.addMessage(ocpRuntimeException.getSeverity(), ocpRuntimeException.getStatus(), ocpRuntimeException.getMessageKey(),
					ocpRuntimeException.getParams());
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
			key = "#root.methodName + T(gov.va.ocp.framework.cache.OcpCacheUtil).getUserBasedKey()", 
			unless = "T(gov.va.ocp.framework.cache.OcpCacheUtil).checkResultConditions(#result)")
	@HystrixCommand(commandKey = "getClaimsCommand", ignoreExceptions = { IllegalArgumentException.class })
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
}
