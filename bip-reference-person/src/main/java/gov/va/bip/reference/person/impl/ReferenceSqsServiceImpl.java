package gov.va.bip.reference.person.impl;

import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazon.sqs.javamessaging.SQSSession;
import com.amazon.sqs.javamessaging.message.SQSTextMessage;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.*;
import gov.va.bip.framework.log.BipLogger;
import gov.va.bip.framework.log.BipLoggerFactory;
import gov.va.bip.framework.sqs.config.SqsProperties;
import gov.va.bip.reference.person.ReferenceSqsService;
import gov.va.bip.reference.person.api.model.v1.BipListQueuesResult;
import gov.va.bip.reference.person.api.model.v1.BipReceiveMessagesResult;
import gov.va.bip.reference.person.api.model.v1.BipSendMessageResult;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jms.Message;
import javax.jms.*;
import java.util.List;

/**
 * Implementation class for the Reference Person Service to demonstrate AWS SQS capabilities of the BLUE Framework.
 */
@Service(value = ReferenceSqsServiceImpl.BEAN_NAME)
@Component
@Qualifier("REFERENCE_SQS_SERVICE_IMPL")
@ConditionalOnProperty(name = "bip.framework.aws.sqs.enabled", havingValue = "true")
@RefreshScope
public class ReferenceSqsServiceImpl implements ReferenceSqsService {

	/**
	 * Bean name constant
	 */
	public static final String BEAN_NAME = "referenceSqsServiceImpl";

	private static final BipLogger LOGGER = BipLoggerFactory.getLogger(ReferenceSqsServiceImpl.class);

	Connection jmsConnection;
	Session session;
	MessageConsumer consumer;

	@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
	@Autowired
	@Qualifier("first-queue-id")
	AmazonSQS myQueue;

	@Autowired
	SqsProperties sqsProperties;

	ConnectionFactory jmsConnectionFactory;

	@PostConstruct
	public void init() throws JMSException {
		startJmsConnection();
		LOGGER.info("init() called. Started JMS Connection");
	}

	@PreDestroy
    public void cleanUp() throws JMSException {

        if (consumer != null) {
            consumer.close();
            LOGGER.info("Queue consumer closed");
        }

        if (session != null) {
            session.close();
			LOGGER.info("Session closed");
        }

        if (jmsConnection != null) {
			jmsConnection.close();
			LOGGER.info("JMS connection closed");
        }
    }

    /**
     * Creates a SQS Connection and listeners to the main and dead letter queues.
     */
    private void startJmsConnection() throws JMSException {

    	initializeJmsConnectionFactory();

        jmsConnection = jmsConnectionFactory.createConnection();

        try {
            // Create the session
            session = jmsConnection.createSession(false, SQSSession.UNORDERED_ACKNOWLEDGE);

            // Create the main queue
            consumer = session.createConsumer(session.createQueue("first-queue-name"));

            // Set the consumer listener
            FirstQueueListener listener = new FirstQueueListener();
            consumer.setMessageListener(listener);

        } catch (final JMSException e) {
            LOGGER.error("Error occurred while starting JMS connection and listeners. Error: {}", e);
        }

        // No messages are processed until this is called
        jmsConnection.start();
	}

	private void initializeJmsConnectionFactory() {
    	if(jmsConnectionFactory == null) {

    		ProviderConfiguration providerConfiguration = new ProviderConfiguration();
    		providerConfiguration.setNumberOfMessagesToPrefetch(5);

    		jmsConnectionFactory = new SQSConnectionFactory(providerConfiguration, myQueue);
		}
	}

	@Override
	@CircuitBreaker(name = "listQueues")
	public BipListQueuesResult listQueues(final String queueNamePrefix) {

		ListQueuesRequest listQueuesRequest = new ListQueuesRequest();
		if(queueNamePrefix != null) {
			listQueuesRequest.setQueueNamePrefix(queueNamePrefix);
		}

		ListQueuesResult awsResult = myQueue.listQueues(listQueuesRequest);

		List<String> queueUrls = awsResult.getQueueUrls();
		BipListQueuesResult result = new BipListQueuesResult();
		result.setQueueUrls(queueUrls);

		return result;
	}

	@Override
	@CircuitBreaker(name = "sendMessage")
	public BipSendMessageResult sendMessage(final String queueName, final String messageBody) {

		SendMessageRequest sendMessageRequest = new SendMessageRequest();
		sendMessageRequest.setQueueUrl(myQueue.getQueueUrl(queueName).getQueueUrl());
		sendMessageRequest.setMessageBody(messageBody);

		SendMessageResult awsResult = myQueue.sendMessage(sendMessageRequest);

		BipSendMessageResult result = new BipSendMessageResult();
		result.setMessageId(awsResult.getMessageId());

		return result;
	}

	@Override
	@CircuitBreaker(name = "receiveMessages")
	public BipReceiveMessagesResult receiveMessages(final String queueName) {

		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest();
		receiveMessageRequest.setQueueUrl(myQueue.getQueueUrl(queueName).getQueueUrl());

		ReceiveMessageResult awsResult = myQueue.receiveMessage(receiveMessageRequest);

		List<com.amazonaws.services.sqs.model.Message> messageList = awsResult.getMessages();
		BipReceiveMessagesResult result = new BipReceiveMessagesResult();

		for(com.amazonaws.services.sqs.model.Message message : messageList) {
			result.addMessagePayloadsItem(message.getBody());
		}

		return result;
	}

	/**
     * Message Listener for the Main Queue
     */
    public static class FirstQueueListener implements MessageListener {

        @Override
        public void onMessage(final Message message) {
            try {
				LOGGER.info("Consumer message processing started in Queue.");
                if (message instanceof SQSTextMessage) {
                    final SQSTextMessage sqsTextMessage = (SQSTextMessage) message;

					// Process the message
					LOGGER.info("Consumer received message with raw payload: "+sqsTextMessage.getText());

                    // Acknowledge deletes this instance of the message (The message has been processed)
                    message.acknowledge();
					LOGGER.info("Acknowledged message in Queue.");
                }
            } catch (JMSException e) {
				LOGGER.error("Error occurred while processing message. Error: {}", e);
            }
		}
    }
}