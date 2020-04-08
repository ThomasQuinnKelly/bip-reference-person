package gov.va.bip.reference.person.impl;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.*;
import gov.va.bip.framework.sqs.config.SqsProperties;
import gov.va.bip.reference.person.api.model.v1.BipListQueuesResult;
import gov.va.bip.reference.person.api.model.v1.BipReceiveMessagesResult;
import gov.va.bip.reference.person.api.model.v1.BipSendMessageResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class ReferenceSqsServiceImplTest {

	private static final String QUEUE_PREFIX = "queueName";

	private static final String QUEUE_ID_1 = "queueId1";
	private static final String QUEUE_NAME_1 = "first-queue-name";
	private static final String QUEUE_ENDPOINT_1 = "http://localhost:1234/queue/queueName1";
	private static final String QUEUE_REGION_1 = "us-east-1";
	private static final String QUEUE_URL_1 = "queueUrl1";

	private static final String QUEUE_ID_2 = "queueId2";
	private static final String QUEUE_NAME_2 = "second-queue-name";
	private static final String QUEUE_ENDPOINT_2 = "http://localhost:1234/queue/queueName2";
	private static final String QUEUE_REGION_2 = "us-west-1";
	private static final String QUEUE_URL_2 = "queueUrl2";

	private static final String EXPECTED_MESSAGE_ID = "UUID";
	private static final String GENERIC_STRING = "Generic String";

	@InjectMocks
	ReferenceSqsServiceImpl instance;

	@Mock
	AmazonSQS sqsService;

	@Mock
	SqsProperties sqsProperties;

	@Mock
	GetQueueUrlResult mockQueueUrlResult1;

	@Mock
	Connection mockJmsConnection;
	@Mock
	Session mockSession;
	@Mock
	MessageConsumer mockConsumer;

	List<SqsProperties.SqsQueue> queueList;

	@Before
	public void before() {
		queueList = new ArrayList<>();
		SqsProperties.SqsQueue testQueue1 = new SqsProperties.SqsQueue();
		testQueue1.setId(QUEUE_ID_1);
		testQueue1.setName(QUEUE_NAME_1);
		testQueue1.setEndpoint(QUEUE_ENDPOINT_1);
		testQueue1.setRegion(QUEUE_REGION_1);
		SqsProperties.SqsQueue testQueue2 = new SqsProperties.SqsQueue();
		testQueue2.setId(QUEUE_ID_2);
		testQueue2.setName(QUEUE_NAME_2);
		testQueue2.setEndpoint(QUEUE_ENDPOINT_2);
		testQueue2.setRegion(QUEUE_REGION_2);
		queueList.add(testQueue1);
		queueList.add(testQueue2);

		when(sqsProperties.getQueues()).thenReturn(queueList);
		when(sqsService.getQueueUrl(QUEUE_NAME_1)).thenReturn(mockQueueUrlResult1);
		when(sqsService.getQueueUrl(any(GetQueueUrlRequest.class))).thenReturn(mockQueueUrlResult1);
		when(mockQueueUrlResult1.getQueueUrl()).thenReturn(QUEUE_URL_1);
	}

	@Test
	public void testInit() throws JMSException {

		instance.init();

		assertNotNull(instance.jmsConnectionFactory);
		assertNotNull(instance.jmsConnection);
		assertNotNull(instance.session);
		assertNotNull(instance.consumer);
	}

	@Test
	public void testCleanUp() throws JMSException {

		instance.jmsConnection = mockJmsConnection;
		instance.session = mockSession;
		instance.consumer = mockConsumer;

		instance.cleanUp();

		verify(mockJmsConnection, times(1)).close();
		verify(mockSession, times(1)).close();
		verify(mockConsumer, times(1)).close();
	}

	@Test
	public void testListQueues() {

		ListQueuesResult listQueuesResult = new ListQueuesResult();
		List<String> expectedQueueUrls = new ArrayList<>();
		for(SqsProperties.SqsQueue queue : queueList) {
			expectedQueueUrls.add(queue.getEndpoint());
		}
		listQueuesResult.setQueueUrls(expectedQueueUrls);

		when(sqsService.listQueues(any(ListQueuesRequest.class))).thenReturn(listQueuesResult);

		BipListQueuesResult result = instance.listQueues(QUEUE_PREFIX);

		assertNotNull(result);
		assertNotNull(result.getQueueUrls());
		assertEquals(2, result.getQueueUrls().size());
		for(SqsProperties.SqsQueue queue : queueList) {
			assertTrue(result.getQueueUrls().contains(queue.getEndpoint()));
		}
	}

	@Test
	public void testSendMessage() {

		SendMessageResult sendMessageResult = new SendMessageResult();
		sendMessageResult.setMessageId(EXPECTED_MESSAGE_ID);

		when(sqsService.sendMessage(any(SendMessageRequest.class))).thenReturn(sendMessageResult);

		BipSendMessageResult result = instance.sendMessage(QUEUE_NAME_1, GENERIC_STRING);

		assertNotNull(result);
		assertNotNull(result.getMessageId());
		assertEquals(EXPECTED_MESSAGE_ID, result.getMessageId());
	}

	@Test
	public void testReceiveMessages() {

		ReceiveMessageResult receiveMessageResult = new ReceiveMessageResult();
		List<Message> expectedMessages = new ArrayList<>();
		Message testMessage = new Message();
		testMessage.setBody(GENERIC_STRING);
		expectedMessages.add(testMessage);
		receiveMessageResult.setMessages(expectedMessages);

		when(sqsService.receiveMessage(any(ReceiveMessageRequest.class))).thenReturn(receiveMessageResult);

		BipReceiveMessagesResult result = instance.receiveMessages(QUEUE_NAME_1);

		assertNotNull(result);
		assertNotNull(result.getMessagePayloads());
		assertEquals(1, result.getMessagePayloads().size());
		assertEquals(GENERIC_STRING, result.getMessagePayloads().get(0));
	}
}
