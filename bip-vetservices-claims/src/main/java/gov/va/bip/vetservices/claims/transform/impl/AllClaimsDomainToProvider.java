package gov.va.bip.vetservices.claims.transform.impl;

import gov.va.bip.framework.messages.ServiceMessage;
import gov.va.bip.framework.transfer.transform.AbstractDomainToProvider;
import gov.va.bip.vetservices.claims.api.model.v1.ClaimsResponse;
import gov.va.bip.vetservices.claims.model.AllClaimsDomainResponse;

import org.springframework.stereotype.Component;
/**
 * Transform a service Domain {@link AllClaimsDomainResponse} into a REST Provider {@link ClaimsResponse} object.
 *
 * @author rajuthota
 */
@Component
public class AllClaimsDomainToProvider extends AbstractDomainToProvider<AllClaimsDomainResponse, ClaimsResponse> {

	/**
	 * Transform a service Domain {@link AllClaimsDomainResponse} into a REST Provider {@link ClaimsResponse} object.
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
