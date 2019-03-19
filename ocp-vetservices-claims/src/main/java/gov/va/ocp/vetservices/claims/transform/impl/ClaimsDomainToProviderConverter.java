package gov.va.ocp.vetservices.claims.transform.impl;

import gov.va.ocp.framework.transfer.transform.AbstractDomainToProvider;
import gov.va.ocp.vetservices.claims.api.model.v1.AttributesInfo;
import gov.va.ocp.vetservices.claims.api.model.v1.ClaimDetailResponse;
import gov.va.ocp.vetservices.claims.api.model.v1.ClaimInfo;
import gov.va.ocp.vetservices.claims.api.model.v1.ClaimsResponse;
import gov.va.ocp.vetservices.claims.model.ClaimDetailByIdDomainResponse;
import gov.va.ocp.vetservices.claims.model.ClaimsDomainResponse;

import org.springframework.stereotype.Component;

import gov.va.ocp.framework.messages.ServiceMessage;
/**
 * Transform a service Domain {@link PersonByPidDomainResponse} into a REST Provider {@link ClaimDetailResponse} object.
 *
 * @author rajuthota
 */
@Component
public class ClaimsDomainToProviderConverter extends AbstractDomainToProvider<ClaimDetailByIdDomainResponse, ClaimDetailResponse> {

	/**
	 * Transform a service Domain {@link PersonByPidDomainResponse} into a REST Provider {@link ClaimDetailResponse} object.
	 * <br/>
	 * <b>Member objects inside the returned object may be {@code null}.</b>
	 * <p>
	 * {@inheritDoc AbstractDomainToProvider}
	 */
	@Override
	public ClaimDetailResponse convert(ClaimDetailByIdDomainResponse domainObject) {
		ClaimDetailResponse providerObject = new ClaimDetailResponse();

		// add data
		ClaimInfo providerData = new ClaimInfo();
		if (domainObject != null && domainObject.getClaim() != null) {
			providerData.setType(domainObject.getClaim().getType());
			providerData.setId(String.valueOf(domainObject.getClaim().getId()));
		}
		
		providerObject.setClaim(providerData);
		providerData.setAttributesInfo(populateAttributes(domainObject));
		
		// add messages
		if (domainObject.getMessages() != null && !domainObject.getMessages().isEmpty()) {
			for (ServiceMessage domainMsg : domainObject.getMessages()) {
				providerObject.addMessage(domainMsg.getSeverity(), domainMsg.getKey(), domainMsg.getText(),
						domainMsg.getHttpStatus());
			}
		}

		return providerObject;
	}
	
	private AttributesInfo populateAttributes(ClaimDetailByIdDomainResponse domainObject) {
		AttributesInfo attributesInfo = new AttributesInfo();
		attributesInfo.setClaim_type(domainObject.getClaim().getAttributes().getClaim_type());
		attributesInfo.setDate_filed(domainObject.getClaim().getAttributes().getDate_filed());
		attributesInfo.setDecision_letter_sent(domainObject.getClaim().getAttributes().getDecision_letter_sent());
		attributesInfo.setDevelopment_letter_sent(domainObject.getClaim().getAttributes().getDevelopment_letter_sent());
		attributesInfo.setDocuments_needed(domainObject.getClaim().getAttributes().getDocuments_needed());
		attributesInfo.setId(domainObject.getClaim().getAttributes().getId());
		attributesInfo.setMax_est_date(domainObject.getClaim().getAttributes().getMax_est_date());
		attributesInfo.setMin_est_date(domainObject.getClaim().getAttributes().getMin_est_date());
		attributesInfo.setOpen(domainObject.getClaim().getAttributes().getOpen());
		attributesInfo.setRequested_decision(domainObject.getClaim().getAttributes().getRequested_decision());
		attributesInfo.setWaiver_submitted(domainObject.getClaim().getAttributes().getWaiver_submitted());
		attributesInfo.setUpdated_at(domainObject.getClaim().getAttributes().getUpdated_at());
		attributesInfo.setStatus(domainObject.getClaim().getAttributes().getStatus());
		return attributesInfo;
	}
	
	/**
	 * Transform a service Domain {@link PersonByPidDomainResponse} into a REST Provider {@link ClaimDetailResponse} object.
	 * <br/>
	 * <b>Member objects inside the returned object may be {@code null}.</b>
	 * <p>
	 * {@inheritDoc AbstractDomainToProvider}
	 */
	public ClaimsResponse convertAll(ClaimsDomainResponse domainObject) {
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
