package gov.va.bip.reference.person;

import gov.va.bip.reference.person.api.model.v1.BipListQueuesResult;
import gov.va.bip.reference.person.api.model.v1.BipReceiveMessagesResult;
import gov.va.bip.reference.person.api.model.v1.BipSendMessageResult;

/**
 * The contract interface for the Reference S3 domain (service) layer.
 *
 */
public interface ReferenceSqsService {

	BipListQueuesResult listQueues(String queueNamePrefix);

	BipSendMessageResult sendMessage(String queueName, String messageBody);

	BipReceiveMessagesResult receiveMessages(String queueName);

}
