package gov.va.bip.reference.person.impl;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishResult;
import gov.va.bip.framework.sns.config.SnsProperties;
import gov.va.bip.reference.person.api.model.v1.BipPublishResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ReferenceSnsServiceImplTest {

	@InjectMocks
	ReferenceSnsServiceImpl instance;

	@Mock
	AmazonSNS snsService;

	@Mock
	SnsProperties snsProperties;

	String EXPECTED_MESSAGE = "This message.";
	String EXPECTED_MESSAGE_ID = "UUID";

	@Test
	public void testPublishMessage() {

		List<SnsProperties.SnsTopic> testList = new ArrayList<>();
		SnsProperties.SnsTopic testTopic = new SnsProperties.SnsTopic();
		testTopic.setTopicArn("arn:aws:sns:us-east-1:000000000000:test_my_topic");
		testList.add(testTopic);

		PublishResult publishResult = new PublishResult();
		publishResult.setMessageId(EXPECTED_MESSAGE_ID);

		when(snsProperties.getTopics()).thenReturn(testList);
		when(snsService.publish(any())).thenReturn(publishResult);

		BipPublishResult result = instance.publishMessage(EXPECTED_MESSAGE);

		assertNotNull(result);
		assertNotNull(result.getMessageId());
		assertEquals(EXPECTED_MESSAGE_ID, result.getMessageId());
	}

}
