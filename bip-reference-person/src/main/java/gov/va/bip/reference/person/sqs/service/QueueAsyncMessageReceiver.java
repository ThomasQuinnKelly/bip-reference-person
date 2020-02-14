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

    private static final String MESSAGE_TIME_ELAPSED = "Message time elapsed: %1$s";

    private Connection connection;
    private Session session;
    private MessageConsumer consumer;
    private MessageConsumer dlqconsumer;

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
    public void init() throws JMSException {
        startJmsConnection();
        logger.info("init() called. Started JMS Connection");
    }

    @PreDestroy
    public void cleanUp() throws JMSException {

        if (consumer != null) {
            consumer.close();
            logger.info("Queue consumer closed");
        }


        if (dlqconsumer != null) {
            dlqconsumer.close();
            logger.info("DLQ Queue consumer closed");
        }


        if (session != null) {
            session.close();
            logger.info("Session closed");
        }

        if (connection != null) {
            connection.close();
            logger.info("JMS connection closed");
        }
    }

    /**
     * Creates a SQS Connection and listeners to the main and dead letter queues.
     */
    private void startJmsConnection() throws JMSException {

        connection = connectionFactory.createConnection();

        try
        {
            // Create the session
            session = connection.createSession(false, SQSSession.UNORDERED_ACKNOWLEDGE);

            // Create the Main Queue
            consumer = session.createConsumer(session.createQueue(sqsProperties.getQueueName()));

            // Create the Dead Letter Queue
            dlqconsumer = session.createConsumer(session.createQueue(sqsProperties.getDLQQueueName()));

            //set consumer listener
            QReceiverCallback callback = new QReceiverCallback();
            consumer.setMessageListener(callback);

            //set dlq consumer listener
            DLQReceiverCallback dlqcallback = new DLQReceiverCallback();
            dlqconsumer.setMessageListener(dlqcallback);

        } catch (final JMSException e) {
            logger.error("Error occurred while starting JMS connection and listeners. Error: {}", e);
        }

        // No messages are processed until this is called
        connection.start();
    }

    /**
     * Listener for the Main Queue
     */
    public class QReceiverCallback implements MessageListener {
        @Override
        public void onMessage(final Message message) {
            try {
                if(logger.isInfoEnabled()) logger.info(String.format("Consumer message processing started for Normal Queue. JMS Message %1$s", message.getJMSMessageID()));
                if (message instanceof TextMessage) {
                    final TextMessage messageText = (TextMessage) message;

                    final MessageAttributes messageAttributes = getMessageAttributesFromJson(messageText.getText());
                    findJMSElapsedTime(messageAttributes.getCreateTimestamp());
                    final String messageAttributesText = messageAttributes.getMessage();

                    // a mock of a lack of ability to process for any number of reasons
                    if (messageAttributesText.contains("donotprocess")) {
                        if(logger.isErrorEnabled()) logger.error(String.format("Message is not processed. JMS Message %1$s", message.getJMSMessageID()));
                        return;
                    }

                    // acknowledge deletes this instance of the message (The message has been processed)
                    message.acknowledge();
                }
                if(logger.isInfoEnabled()) logger.info(String.format("Acknowledged message. JMS Message %1$s", message.getJMSMessageID()));
            } catch (final JMSException e) {
                logger.error("Error occurred while processing message. Error: {}", e);
            } catch (final Exception e) {
                logger.error("Error occurred while copying the object. Error: {}", e);
            }
        }

        /**
         * @param createTimeStamp
         */
        public long findJMSElapsedTime(final long createTimeStamp) {
            final long currentTime = System.currentTimeMillis();
            long differenceTime = currentTime - createTimeStamp;

            long resultDifferenceTime = differenceTime;

            String timeMessageElapsed = TimeUnit.MILLISECONDS.toHours(differenceTime) + " hrs, ";
            differenceTime = differenceTime - TimeUnit.MILLISECONDS.toHours(differenceTime) * 3600000;

            timeMessageElapsed += TimeUnit.MILLISECONDS.toMinutes(differenceTime) + " mins, ";
            differenceTime = differenceTime - TimeUnit.MILLISECONDS.toMinutes(differenceTime) * 60000;

            timeMessageElapsed += TimeUnit.MILLISECONDS.toSeconds(differenceTime) + " secs, and ";
            differenceTime = differenceTime - TimeUnit.MILLISECONDS.toSeconds(differenceTime) * 1000;

            timeMessageElapsed += differenceTime + " ms.";

            if(logger.isInfoEnabled()) logger.info(String.format(MESSAGE_TIME_ELAPSED, timeMessageElapsed));

            return resultDifferenceTime;
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
                if(logger.isInfoEnabled()) logger.info(String.format(
                        "Consumer message processing started for DLQ. JMS Message %1$s", message.getJMSMessageID()));

                if (message instanceof SQSTextMessage) {
                    final SQSTextMessage messageText = (SQSTextMessage) message;
                    final MessageAttributes messageAttributes = getMessageAttributesFromJson(messageText.getText());

                    if (messageAttributes == null) {
                        return;
                    }

                    // If the number of current tries in the message attributes is greater than or equal to the retries detailed in the sqsProperties
                    if (messageAttributes.getNumberOfRetries() >= sqsProperties.getRetries()) {
                        // archive the message here in some way
                        if(logger.isInfoEnabled()) logger.info("Deleting the message from DLQ after {} attempts. JMS Message {}",
                                sqsProperties.getRetries(), message.getJMSMessageID());
                    } else {
                        final SQSTextMessage txtMessage = moveMessageToQueue(messageAttributes);
                        sqsServices.sendMessage(txtMessage);
                    }

                    // acknowledge deletes this instance of the message (The message has had an attempted processing)
                    message.acknowledge();
                }
                if(logger.isInfoEnabled()) logger.info(String.format("Acknowledged message from DLQ. JMS Message %1$s", message.getJMSMessageID()));

            } catch (final JMSException e) {
                logger.error("Error occurred while processing message. Error: {}", e);
            }
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



}
