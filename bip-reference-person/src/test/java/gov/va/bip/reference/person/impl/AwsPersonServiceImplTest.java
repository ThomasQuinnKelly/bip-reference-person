package gov.va.bip.reference.person.impl;

import com.amazon.sqs.javamessaging.message.SQSTextMessage;
import com.amazonaws.services.sns.model.PublishResult;
import gov.va.bip.framework.sns.config.SnsProperties;
import gov.va.bip.framework.sns.services.SnsService;
import gov.va.bip.framework.sqs.dto.SendMessageResponse;
import gov.va.bip.framework.sqs.services.SqsService;
import gov.va.bip.reference.person.api.model.v1.JmsResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jms.JMSException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class AwsPersonServiceImplTest {

	@InjectMocks
	AwsPersonServiceImpl refAwsPersonService;

	@Mock
	SqsService sqsService;

	@Mock
	SnsService snsService;

	@Mock
	SnsProperties snsProperties;

	String mockMessage = "This message.";

	@Test
	public void testSendMessage() throws JMSException {

		SQSTextMessage sqsTextMessage = new SQSTextMessage();
		SendMessageResponse sendMessageResponse = new SendMessageResponse();
		sendMessageResponse.setMessageId("ID:UUID");

		when(sqsService.createTextMessage(any())).thenReturn(sqsTextMessage);
		when(sqsService.sendMessage(sqsTextMessage)).thenReturn(sendMessageResponse);

		JmsResponse jmsResponse = refAwsPersonService.sendMessage(mockMessage);

		assertNotNull(jmsResponse);
		assertEquals("ID:", jmsResponse.getJmsId().substring(0,3));

	}

	@Test
	public void testPublishMessage() {

		PublishResult publishResult = new PublishResult();
		publishResult.setMessageId("UUID");

		when(snsProperties.getTopicArn()).thenReturn("arn:aws:sns:us-east-1:000000000000:test_my_topic");
		when(snsService.publish(any())).thenReturn(publishResult);

		gov.va.bip.reference.person.api.model.v1.PublishResult servicePublishResult = refAwsPersonService.publishMessage(mockMessage);

		assertNotNull(servicePublishResult);
		assertNotNull(servicePublishResult.getMessageId());
	}

}
