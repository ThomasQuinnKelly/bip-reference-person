package gov.va.bip.reference.person.impl;

import com.amazon.sqs.javamessaging.message.SQSTextMessage;
import com.amazonaws.services.sns.model.PublishRequest;
import gov.va.bip.framework.aws.autoconfigure.BipSnsAutoConfiguration;
import gov.va.bip.framework.aws.autoconfigure.BipSqsAutoConfiguration;
import gov.va.bip.framework.log.BipLogger;
import gov.va.bip.framework.log.BipLoggerFactory;
import gov.va.bip.framework.sns.config.SnsProperties;
import gov.va.bip.framework.sns.services.SnsService;
import gov.va.bip.framework.sqs.dto.SendMessageResponse;
import gov.va.bip.framework.sqs.services.SqsService;
import gov.va.bip.reference.person.AwsPersonService;
import gov.va.bip.reference.person.api.model.v1.JmsResponse;
import gov.va.bip.reference.person.api.model.v1.PublishResult;
import gov.va.bip.reference.person.sqs.service.MessageAttributes;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Implementation class for the Reference Person Service. The class demonstrates
 * the implementation of resilience4j circuit breaker pattern for read
 * operations. When there is a failure the fallback method is invoked and the
 * response is returned from the cache
 */
@Service(value = AwsPersonServiceImpl.BEAN_NAME)
@Component
@Qualifier("AWS_PERSON_SERVICE_IMPL")
@RefreshScope
public class AwsPersonServiceImpl implements AwsPersonService {

	@Autowired
	BipSqsAutoConfiguration bipSqsAutoConfiguration;

	/** Bean name constant */
	public static final String BEAN_NAME = "awsPersonServiceImpl";

	@Autowired
	SqsService sqsService;

	@Autowired
	BipSnsAutoConfiguration bipSnsAutoConfiguration;

	@Autowired
	SnsService snsService;

	@Autowired
	SnsProperties snsProperties;

	/**
	 * Send a message to the queue
	 *
	 * @param message
	 *            message to be sent to the queue
	 * @return A Integer representing the JMS Id
	 */
	@Override
	@CircuitBreaker(name = "sendMessage")
	public JmsResponse sendMessage(final String message) {

		SQSTextMessage textMessage = sqsService.createTextMessage(new MessageAttributes(message).toJson());

		SendMessageResponse s = sqsService.sendMessage(textMessage);

		JmsResponse result = new JmsResponse();

		result.setJmsId(s.getMessageId());

		return result;
	}

	@Override
	@CircuitBreaker(name = "publishMessage")
	public PublishResult publishMessage(final String message) {

		PublishRequest publishRequest = new PublishRequest();
		publishRequest.setMessage(new MessageAttributes(message).toJson());
		publishRequest.setTopicArn(snsProperties.getTopicArn());

		com.amazonaws.services.sns.model.PublishResult result2 = snsService.publish(publishRequest);

		PublishResult result = new PublishResult();
		result.setMessageId(result2.getMessageId());

		return result;
	}

}