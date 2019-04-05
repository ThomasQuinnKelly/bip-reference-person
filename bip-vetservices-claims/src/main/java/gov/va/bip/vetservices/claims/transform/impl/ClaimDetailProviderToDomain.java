package gov.va.bip.vetservices.claims.transform.impl;

import org.springframework.stereotype.Component;

import gov.va.bip.framework.transfer.transform.AbstractProviderToDomain;
import gov.va.bip.vetservices.claims.api.model.v1.ClaimDetailRequest;
import gov.va.bip.vetservices.claims.model.ClaimDetailByIdDomainRequest;

/**
 * Transform a REST Provider {@link ClaimDetailRequest} into a service Domain {@link ClaimDetailByIdDomainRequest} object.
 *
 * @author rajuthota
 */
@Component
public class ClaimDetailProviderToDomain extends AbstractProviderToDomain<ClaimDetailRequest, ClaimDetailByIdDomainRequest> {

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

}
