package gov.va.bip.reference.person;

import gov.va.bip.reference.person.api.model.v1.JmsResponse;
import gov.va.bip.reference.person.api.model.v1.PublishResult;

/**
 * The contract interface for the AWS Person domain (service) layer.
 *
 */
public interface AwsPersonService {
	/**
	 * Send a message to a Queue
	 *
	 * @param message message for Queue
	 * @return A Integer representing the JMS Id
	 */
	JmsResponse sendMessage(String message);

	PublishResult publishMessage(String message);

}
