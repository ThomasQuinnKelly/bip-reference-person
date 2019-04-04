package gov.va.ocp.vetservices.claims.transform.impl;

import gov.va.ocp.framework.transfer.transform.AbstractDomainToProvider;
import gov.va.ocp.vetservices.claims.api.model.v1.AttributesInfo;
import gov.va.ocp.vetservices.claims.api.model.v1.ClaimDetailResponse;
import gov.va.ocp.vetservices.claims.api.model.v1.ClaimInfo;
import gov.va.ocp.vetservices.claims.model.ClaimDetailByIdDomainResponse;
import gov.va.ocp.vetservices.claims.orm.Claim;

import org.springframework.stereotype.Component;

import gov.va.ocp.framework.messages.ServiceMessage;
/**
 * Transform a service Domain {@link ClaimDetailByIdDomainResponse} into a REST Provider {@link ClaimDetailResponse} object.
 *
 * @author rajuthota
 */
@Component
public class ClaimDetailDomainToProvider extends AbstractDomainToProvider<ClaimDetailByIdDomainResponse, ClaimDetailResponse> {

	/**
	 * Transform a service Domain {@link ClaimDetailByIdDomainResponse} into a REST Provider {@link ClaimDetailResponse} object.
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
			providerObject.setClaim(providerData);
			providerData.setAttributesInfo(populateAttributes(domainObject.getClaim()));
		}
		
		// add messages
		if (domainObject.getMessages() != null && !domainObject.getMessages().isEmpty()) {
			for (ServiceMessage domainMsg : domainObject.getMessages()) {
				providerObject.addMessage(domainMsg.getSeverity(), domainMsg.getKey(), domainMsg.getText(),
						domainMsg.getHttpStatus());
			}
		}

		return providerObject;
	}
	
	/**
	 * Populate Attributes
	 * @param domainObject
	 * @return
	 */
	private AttributesInfo populateAttributes(Claim claim) {
		AttributesInfo attributesInfo = new AttributesInfo();
		attributesInfo.setClaim_type(claim.getAttributes().getClaim_type());
		attributesInfo.setDate_filed(claim.getAttributes().getDate_filed());
		attributesInfo.setDecision_letter_sent(claim.getAttributes().getDecision_letter_sent());
		attributesInfo.setDevelopment_letter_sent(claim.getAttributes().getDevelopment_letter_sent());
		attributesInfo.setDocuments_needed(claim.getAttributes().getDocuments_needed());
		attributesInfo.setId(claim.getAttributes().getId());
		attributesInfo.setMax_est_date(claim.getAttributes().getMax_est_date());
		attributesInfo.setMin_est_date(claim.getAttributes().getMin_est_date());
		attributesInfo.setOpen(claim.getAttributes().getOpen());
		attributesInfo.setRequested_decision(claim.getAttributes().getRequested_decision());
		attributesInfo.setWaiver_submitted(claim.getAttributes().getWaiver_submitted());
		attributesInfo.setUpdated_at(claim.getAttributes().getUpdated_at());
		attributesInfo.setStatus(claim.getAttributes().getStatus());
		return attributesInfo;
	}
}
