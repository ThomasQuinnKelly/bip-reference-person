package gov.va.bip.reference.person.sqs.service;

import com.amazon.sqs.javamessaging.SQSSession;
import com.amazon.sqs.javamessaging.message.SQSTextMessage;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.va.bip.framework.sns.dto.SNSMessage;
import gov.va.bip.framework.sqs.config.SqsProperties;
import gov.va.bip.framework.sqs.services.SqsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jms.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class QueueAsyncMessageReceiver {

    private final Logger logger = LoggerFactory.getLogger(QueueAsyncMessageReceiver.class);

    @Autowired
    ObjectMapper mapper;

    private static final String MESSAGE_TIME_ELAPSED = "Message time elapsed: ";

    private Connection connection;

    @Autowired
    ConnectionFactory connectionFactory;

    @Autowired
    SqsService sqsServices;

    private SqsProperties sqsProperties;


    @Autowired
    public void setApp(final SqsProperties sqsProperties) {
        this.sqsProperties = sqsProperties;
    }

    @PostConstruct
    public void init() {
        startJmsConnection();
        logger.info("init() called. Started JMS Connection");
    }

    @PreDestroy
    public void cleanUp() throws JMSException {
        if (connection != null) {
            connection.close();
            logger.info("JMS connection closed");
        }
    }

    /**
     * Creates a SQS Connection and listeners to the main and dead letter queues.
     */
    private void startJmsConnection() {
        try {
            connection = connectionFactory.createConnection();

            // Create the session
            final Session session = connection.createSession(false, SQSSession.UNORDERED_ACKNOWLEDGE);

            // Create the Main Queue
            final MessageConsumer consumer = session.createConsumer(session.createQueue(sqsProperties.getQueueName()));
            final QReceiverCallback callback = new QReceiverCallback();
            consumer.setMessageListener(callback);

            // Create the Dead Letter Queue
            final MessageConsumer dlqconsumer = session.createConsumer(session.createQueue(sqsProperties.getDLQQueueName()));
            final DLQReceiverCallback dlqcallback = new DLQReceiverCallback();
            dlqconsumer.setMessageListener(dlqcallback);

            // No messages are processed until this is called
            connection.start();

        } catch (final JMSException e) {
            logger.error("Error occurred while starting JMS connection and listeners. Error: {}", e);
        }
    }

    /**
     * Listener for the Main Queue
     */
    private class QReceiverCallback implements MessageListener {
        @Override
        public void onMessage(final Message message) {
            try {
                logger.info("Consumer message processing started for Normal Queue. JMS Message " + message.getJMSMessageID());
                if (message instanceof TextMessage) {
                    final TextMessage messageText = (TextMessage) message;

                    final MessageAttributes messageAttributes = getMessageAttributesFromJson(messageText.getText());
                    findJMSElapsedTime(messageAttributes.getCreateTimestamp());
                    final String messageAttributesText = messageAttributes.getMessage();

                    // a mock of a lack of ability to process for any number of reasons
                    if (messageAttributesText.contains("donotprocess")) {
                        logger.error("Message is not processed. JMS Message " + message.getJMSMessageID());
                        return;
                    }

                    // acknowledge deletes this instance of the message (The message has been processed)
                    message.acknowledge();
                }
                logger.info("Acknowledged message. JMS Message " + message.getJMSMessageID());
            } catch (final JMSException e) {
                logger.error("Error occurred while processing message. Error: {}", e);
            } catch (final Exception e) {
                logger.error("Error occurred while copying the object. Error: {}", e);
                return;
            }
        }

        /**
         * @param createTimeStamp
         */
        private long findJMSElapsedTime(final long createTimeStamp) {
            final long currentTime = System.currentTimeMillis();
            final long differenceTime = currentTime - createTimeStamp;
            logger.info(MESSAGE_TIME_ELAPSED + differenceTime + " ms");
            logger.info(MESSAGE_TIME_ELAPSED + TimeUnit.MILLISECONDS.toSeconds(differenceTime) + " secs");
            logger.info(MESSAGE_TIME_ELAPSED + TimeUnit.MILLISECONDS.toMinutes(differenceTime) + " mins");
            logger.info(MESSAGE_TIME_ELAPSED + TimeUnit.MILLISECONDS.toHours(differenceTime) + " hrs");
            return differenceTime;
        }
    }

    /**
     *         Listener for the Dead Letter Queue. The message is psuhed back into main queue.
     *         After three attempts, the message is deleted.
     */
    private class DLQReceiverCallback implements MessageListener {
        @Override
        public void onMessage(final Message message) {
            try {
                logger.info(
                        "Consumer message processing started for DLQ. JMS Message " + message.getJMSMessageID());
                if (message instanceof SQSTextMessage) {
                    final SQSTextMessage messageText = (SQSTextMessage) message;
                    final MessageAttributes messageAttributes = getMessageAttributesFromJson(messageText.getText());

                    if (messageAttributes == null) {
                        return;
                    }

                    // If the number of current tries in the message attributes is greater than or equal to the retries detailed in the sqsProperties
                    if (messageAttributes.getNumberOfRetries() >= sqsProperties.getRetries()) {
                        // archive the message here in some way
                        logger.info("Deleting the message from DLQ after {} attempts. JMS Message {}",
                                sqsProperties.getRetries(), message.getJMSMessageID());
                    } else {
                        final SQSTextMessage txtMessage = moveMessageToQueue(messageAttributes);
                        sqsServices.sendMessage(txtMessage);
                    }

                    // acknowledge deletes this instance of the message (The message has had an attempted processing)
                    message.acknowledge();
                }
                logger.info("Acknowledged message from DLQ. JMS Message " + message.getJMSMessageID());

            } catch (final JMSException e) {
                logger.error("Error occurred while processing message. Error: {}", e);
            }
        }
    }

    public MessageAttributes getMessageAttributesFromJson(String message) {
        try {
            MessageAttributes messageAttribute = mapper.readValue(message, MessageAttributes.class);

            // resolve SNS topic message sent to SQS
            if (messageAttribute.getMessage() == null) {

                SNSMessage snsMessageAttempt = mapper.readValue(message, SNSMessage.class);

                messageAttribute = mapper.readValue(snsMessageAttempt.getMessage(), MessageAttributes.class);

            }

            return messageAttribute;
        } catch (final JsonParseException e) {
            logger.error("JsonParseException {}", e);
        } catch (final JsonMappingException e) {
            logger.error("JsonMappingException {}", e);
        } catch (final IOException e) {
            logger.error("IOException {}", e);
        }
        return null;
    }

    private SQSTextMessage moveMessageToQueue(final MessageAttributes messageAttributes) {
        // move the message to normal queue for processing
        messageAttributes.setNumberOfRetries(messageAttributes.getNumberOfRetries() + 1);
        SQSTextMessage txtMessage = null;
        try {
            txtMessage = sqsServices.createTextMessage(mapper.writeValueAsString(messageAttributes));
        } catch (final JsonProcessingException e) {
            logger.error("Error occurred while creating text message. Error: {}", e);
        }
        return txtMessage;
    }

}
