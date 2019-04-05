package gov.va.bip.vetservices.claims.transform.impl;

import org.springframework.stereotype.Component;

import gov.va.bip.framework.security.PersonTraits;
import gov.va.bip.framework.security.SecurityUtils;
import gov.va.bip.vetservices.claims.model.AllClaimsDomainRequest;

/**
 * Transform a REST Provider into a service Domain {@link AllClaimsDomainRequest} object.
 *
 * @author rajuthota
 */
@Component
public class AllClaimsProviderToDomain {

	/**
	 * Transform a REST Provider into a service Domain {@link AllClaimsDomainRequest} object.
	 * <p>
	 */
	public AllClaimsDomainRequest convert() {
		PersonTraits personTraits = SecurityUtils.getPersonTraits();

		AllClaimsDomainRequest domainRequest = new AllClaimsDomainRequest();
		if (personTraits != null) {
			domainRequest.setPid(personTraits.getPid());
		} else {
			//TODO: clean up this code later
			domainRequest.setPid("1234567689");
		}
		return domainRequest;
	}

}
