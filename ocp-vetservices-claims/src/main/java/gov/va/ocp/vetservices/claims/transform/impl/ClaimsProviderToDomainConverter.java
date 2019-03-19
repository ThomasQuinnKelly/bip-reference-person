package gov.va.ocp.vetservices.claims.transform.impl;

import org.springframework.stereotype.Component;

import gov.va.ocp.framework.security.PersonTraits;
import gov.va.ocp.framework.security.SecurityUtils;
import gov.va.ocp.framework.transfer.transform.AbstractProviderToDomain;
import gov.va.ocp.vetservices.claims.api.model.v1.ClaimDetailRequest;
import gov.va.ocp.vetservices.claims.model.AllClaimsDomainRequest;
import gov.va.ocp.vetservices.claims.model.ClaimDetailByIdDomainRequest;

/**
 * Transform a REST Provider {@link ClaimDetailRequest} into a service Domain {@link ClaimDetailByIdDomainRequest} object.
 *
 * @author rajuthota
 */
@Component
public class ClaimsProviderToDomainConverter extends AbstractProviderToDomain<ClaimDetailRequest, ClaimDetailByIdDomainRequest> {

	/**
	 * Transform a REST Provider {@link ClaimDetailRequest} into a service Domain {@link ClaimDetailByIdDomainRequest} object.
	 * <p>
	 * {@inheritDoc AbstractProviderToDomain}
	 */
	@Override
	public ClaimDetailByIdDomainRequest convert(ClaimDetailRequest providerRequest) {
		ClaimDetailByIdDomainRequest domainRequest = new ClaimDetailByIdDomainRequest();
		domainRequest.setId(providerRequest.getClaimId());
		return domainRequest;
	}
	
	public AllClaimsDomainRequest convertAllClaims() {
		PersonTraits personTraits = SecurityUtils.getPersonTraits();

		AllClaimsDomainRequest domainRequest = new AllClaimsDomainRequest();
		domainRequest.setId(personTraits.getPid());
		return domainRequest;
	}

}
