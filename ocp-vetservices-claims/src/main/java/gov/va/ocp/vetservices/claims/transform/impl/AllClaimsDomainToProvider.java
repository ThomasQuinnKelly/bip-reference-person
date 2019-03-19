package gov.va.ocp.vetservices.claims.transform.impl;

import gov.va.ocp.framework.transfer.transform.AbstractDomainToProvider;
import gov.va.ocp.vetservices.claims.api.model.v1.ClaimDetailResponse;
import gov.va.ocp.vetservices.claims.api.model.v1.ClaimsResponse;
import gov.va.ocp.vetservices.claims.model.AllClaimsDomainResponse;

import org.springframework.stereotype.Component;

import gov.va.ocp.framework.messages.ServiceMessage;
/**
 * Transform a service Domain {@link PersonByPidDomainResponse} into a REST Provider {@link ClaimDetailResponse} object.
 *
 * @author rajuthota
 */
@Component
public class AllClaimsDomainToProvider extends AbstractDomainToProvider<AllClaimsDomainResponse, ClaimsResponse> {

	/**
	 * Transform a service Domain {@link PersonByPidDomainResponse} into a REST Provider {@link ClaimDetailResponse} object.
	 * <br/>
	 * <b>Member objects inside the returned object may be {@code null}.</b>
	 * <p>
	 * {@inheritDoc AbstractDomainToProvider}
	 */
	@Override
	public ClaimsResponse convert(AllClaimsDomainResponse domainObject) {
		ClaimsResponse providerObject = new ClaimsResponse();

		// add data
		providerObject.setClaims(domainObject.getClaims());
		// add messages
		if (domainObject.getMessages() != null && !domainObject.getMessages().isEmpty()) {
			for (ServiceMessage domainMsg : domainObject.getMessages()) {
				providerObject.addMessage(domainMsg.getSeverity(), domainMsg.getKey(), domainMsg.getText(),
						domainMsg.getHttpStatus());
			}
		}

		return providerObject;
	}

}
