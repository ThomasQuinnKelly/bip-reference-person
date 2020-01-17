package gov.va.bip.reference.person.server;

import cloud.localstack.Localstack;
import cloud.localstack.TestUtils;
import cloud.localstack.docker.DockerExe;
import cloud.localstack.docker.annotation.LocalstackDockerConfiguration;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.*;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;
import com.amazonaws.services.sqs.model.QueueAttributeName;
import gov.va.bip.framework.localstack.autoconfigure.LocalstackAutoConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

/**
 * This class will configure AWS localstack services.
 */
@Configuration
@ConditionalOnProperty(name = "localstack.enabled", havingValue = "true")
//@Profile(BipCommonSpringProfiles.PROFILE_EMBEDDED_AWS)
//@EnableConfigurationProperties({ BipAwsLocalstackProperties.class, SqsProperties.class, SnsProperties.class })
public class BipEmbeddedAwsLocalstackApplication extends LocalstackAutoConfiguration {

	@Autowired
	private SqsProperties sqsProperties;

	@Autowired
	private SnsProperties snsProperties;

	@Override
	public void configureAwsLocalStack() {
		snsCreateTopicSubscribedToQueue();
	}

	private void snsCreateTopicSubscribedToQueue() {
		AmazonSNS snsClient = TestUtils.getClientSNS();
		AmazonSQS sqsClient = TestUtils.getClientSQS();

		// Create an Amazon SNS topic.
		final CreateTopicRequest createTopicRequest = new CreateTopicRequest(snsProperties.getTopic());
		final CreateTopicResult createTopicResult = snsClient.createTopic(createTopicRequest);

		// Print the topic ARN.
	 	String topicArn = createTopicResult.getTopicArn();
	 	System.out.println("TopicArn:" + createTopicResult.getTopicArn());

		// Print the request ID for the CreateTopicRequest action.
		System.out.println("CreateTopicRequest: " + snsClient.getCachedResponseMetadata(createTopicRequest));

		//create sqs que
		CreateQueueRequest createStandardQueueRequest = new CreateQueueRequest(sqsProperties.getQueueName());
		String queueURL = sqsClient.createQueue(createStandardQueueRequest).getQueueUrl();

		//print the SQS queues
		System.out.println("SQS Queue: " +sqsClient.listQueues());

		// Subscribe an email endpoint to an Amazon SNS topic.
		final SubscribeRequest subscribeRequest = new SubscribeRequest(topicArn, "sqs", queueURL);
		snsClient.subscribe(subscribeRequest);

		// Publish a message to an Amazon SNS topic.
		final String msg = "If you receive this message, publishing a message to an Amazon SNS topic works.";
		final PublishRequest publishRequest = new PublishRequest(topicArn, msg);
		final PublishResult publishResponse = snsClient.publish(publishRequest);

		// Print the MessageId of the message.
		System.out.println("MessageId: " + publishResponse.getMessageId());

		//Print the SNS published message from the subscribed que
		sqsClient.receiveMessage(queueURL).getMessages().forEach(System.out::println);

	}

}
