package gov.va.bip.reference.person;

import gov.va.bip.reference.person.api.model.v1.BipPublishResult;
import gov.va.bip.reference.person.api.model.v1.BipSubscribeRequest;
import gov.va.bip.reference.person.api.model.v1.BipSubscribeResult;

/**
 * The contract interface for the Reference SNS domain (service) layer.
 *
 */
public interface ReferenceSnsService {

	BipPublishResult publishMessage(String message);

	BipSubscribeResult subscribe(BipSubscribeRequest bipSubscribeRequest);

}
