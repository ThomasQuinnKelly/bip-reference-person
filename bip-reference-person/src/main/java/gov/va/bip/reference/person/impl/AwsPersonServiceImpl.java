package gov.va.bip.reference.person.impl;

import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import gov.va.bip.framework.log.BipLogger;
import gov.va.bip.framework.log.BipLoggerFactory;
import gov.va.bip.framework.sqs.dto.SendMessageResponse;
import gov.va.bip.framework.sqs.services.SqsService;
import gov.va.bip.framework.sqs.services.impl.SqsServiceImpl;
import gov.va.bip.reference.person.AwsPersonService;
import gov.va.bip.reference.person.api.model.v1.JmsResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Import;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.jms.ConnectionFactory;
import javax.jms.TextMessage;

/**
 * Implementation class for the Reference Person Service. The class demonstrates
 * the implementation of resilience4j circuit breaker pattern for read
 * operations. When there is a failure the fallback method is invoked and the
 * response is returned from the cache
 *
 * @author akulkarni
 */
@Service(value = AwsPersonServiceImpl.BEAN_NAME)
@Component
@Qualifier("AWS_PERSON_SERVICE_IMPL")
//@Import({SqsServiceImpl.class})//, SQSConnectionFactory.class})//, JmsTemplate.class, SQSConnectionFactory.class})
@RefreshScope
public class AwsPersonServiceImpl implements AwsPersonService {
	private static final BipLogger LOGGER = BipLoggerFactory.getLogger(AwsPersonServiceImpl.class);

	/** Bean name constant */
	public static final String BEAN_NAME = "awsPersonServiceImpl";

//	@Autowired
//	SQSConnectionFactory connectionFactory;
//
//	@Autowired
//	SqsService sqsService;

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

		LOGGER.info("Info: " + message);
		LOGGER.debug("Debug: " + message);
		LOGGER.warn("Warn: " + message);
		LOGGER.error("Error: " + message);

		System.out.println(message);

//		TextMessage textMessage = sqsService.createTextMessage(message);

//		SendMessageResponse s = sqsService.sendMessage(textMessage);

		JmsResponse result = new JmsResponse();

		result.setJmsId(Integer.valueOf(5));//s.getMessageId()

		return result;
	}

}