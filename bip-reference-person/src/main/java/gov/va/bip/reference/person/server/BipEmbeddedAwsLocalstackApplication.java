//package gov.va.bip.reference.person.server;
//
//import cloud.localstack.TestUtils;
//import com.amazonaws.services.sns.AmazonSNS;
//import com.amazonaws.services.sns.model.*;
//import com.amazonaws.services.sqs.AmazonSQS;
//import com.amazonaws.services.sqs.model.CreateQueueRequest;
//import com.amazonaws.services.sqs.model.CreateQueueResult;
//import gov.va.bip.framework.aws.autoconfigure.BipSqsAutoConfiguration;
//import gov.va.bip.framework.localstack.autoconfigure.LocalstackAutoConfiguration;
//import gov.va.bip.framework.sqs.config.StandardSqsConfiguration;
//import gov.va.bip.framework.sqs.dto.SendMessageResponse;
//import gov.va.bip.framework.sqs.services.SqsService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.*;
//
//import javax.jms.ConnectionFactory;
//import javax.jms.TextMessage;
//
///**
// * This class will configure AWS localstack services.
// */
//@Configuration
//@ConditionalOnProperty(name = "bip.framework.localstack.enabled", havingValue = "true")
//@ComponentScan(basePackageClasses = {gov.va.bip.framework.aws.autoconfigure.BipSqsAutoConfiguration.class, })
//public class BipEmbeddedAwsLocalstackApplication extends LocalstackAutoConfiguration {
//
//	@Autowired
//	private gov.va.bip.reference.person.server.ApplicationSqsProperties applicationSqsProperties;
//
////	@Autowired
////	private gov.va.bip.framework.sqs.config.SqsProperties sqsFrameworkProperties;
//
//	@Autowired
//	private SnsProperties snsProperties;
//
////	@Autowired
////	private ConnectionFactory connectionFactory;
//
////	@Autowired
////	private StandardSqsConfiguration standardSqsConfiguration;
//
//	@Autowired
//	private BipSqsAutoConfiguration bipSqsAutoConfiguration;
//
//	public void configureAwsLocalStack() {
//		createQueueAndSendMessageThenRetrieveMessage();
//		//snsCreateTopicSubscribedToQueue();
//	}
//
//	private void createQueueAndSendMessageThenRetrieveMessage() {
//		SqsService sqsService = bipSqsAutoConfiguration.sqsService();
//
//		CreateQueueRequest createStandardQueueRequest = new CreateQueueRequest(applicationSqsProperties.getQueueName());
//		CreateQueueResult createQueueResult = sqsService.createQueue(createStandardQueueRequest);
//
//		String queueUrl = createQueueResult.getQueueUrl();
//
//		//print the SQS queues
//		System.out.println("SQS Queue: " + sqsService.listQueues());
//
//		TextMessage textMessage = sqsService.createTextMessage("This is the message.");
//		SendMessageResponse sendMessageResponse = sqsService.sendMessage(textMessage);
//
//		//print the SQS queues
//		System.out.println("Message Response Id: " + sendMessageResponse.getMessageId() + "; Status Code: " + sendMessageResponse.getStatusCode());
//
//		//Print the SNS published message from the subscribed queue
//		sqsService.receiveMessage(queueUrl);//..getMessages().forEach(System.out::println);
//	}
//
//	private void snsCreateTopicSubscribedToQueue() {
//		AmazonSNS snsClient = TestUtils.getClientSNS();
//		AmazonSQS sqsClient = TestUtils.getClientSQS();
//
//		// Create an Amazon SNS topic.
//		final CreateTopicRequest createTopicRequest = new CreateTopicRequest(snsProperties.getTopic());
//		final CreateTopicResult createTopicResult = snsClient.createTopic(createTopicRequest);
//
//		// Print the topic ARN.
//	 	String topicArn = createTopicResult.getTopicArn();
//	 	System.out.println("TopicArn:" + createTopicResult.getTopicArn());
//
//		// Print the request ID for the CreateTopicRequest action.
//		System.out.println("CreateTopicRequest: " + snsClient.getCachedResponseMetadata(createTopicRequest));
//
//		//create sqs queue
//		CreateQueueRequest createStandardQueueRequest = new CreateQueueRequest(applicationSqsProperties.getQueueName());
//		String queueURL = sqsClient.createQueue(createStandardQueueRequest).getQueueUrl();
//
//		//print the SQS queues
//		System.out.println("SQS Queue: " +sqsClient.listQueues());
//
//		// Subscribe an email endpoint to an Amazon SNS topic.
//		final SubscribeRequest subscribeRequest = new SubscribeRequest(topicArn, "sqs", queueURL);
//		snsClient.subscribe(subscribeRequest);
//
//		// Publish a message to an Amazon SNS topic.
//		final String msg = "If you receive this message, publishing a message to an Amazon SNS topic works.";
//		final PublishRequest publishRequest = new PublishRequest(topicArn, msg);
//		final PublishResult publishResponse = snsClient.publish(publishRequest);
//
//		// Print the MessageId of the message.
//		System.out.println("MessageId: " + publishResponse.getMessageId());
//
//		//Print the SNS published message from the subscribed queue
//		sqsClient.receiveMessage(queueURL).getMessages().forEach(System.out::println);
//
//	}
//
//}
//
//// Preserving base SNS subscribed to SQS example
////	private void snsCreateTopicSubscribedToQueue() {
////		AmazonSNS snsClient = TestUtils.getClientSNS();
////		AmazonSQS sqsClient = TestUtils.getClientSQS();
////
////		// Create an Amazon SNS topic.
////		final CreateTopicRequest createTopicRequest = new CreateTopicRequest(snsProperties.getTopic());
////		final CreateTopicResult createTopicResult = snsClient.createTopic(createTopicRequest);
////
////		// Print the topic ARN.
////		String topicArn = createTopicResult.getTopicArn();
////		System.out.println("TopicArn:" + createTopicResult.getTopicArn());
////
////		// Print the request ID for the CreateTopicRequest action.
////		System.out.println("CreateTopicRequest: " + snsClient.getCachedResponseMetadata(createTopicRequest));
////
////		//create sqs queue
////		CreateQueueRequest createStandardQueueRequest = new CreateQueueRequest(sqsProperties.getQueueName());
////		String queueURL = sqsClient.createQueue(createStandardQueueRequest).getQueueUrl();
////
////		//print the SQS queues
////		System.out.println("SQS Queue: " +sqsClient.listQueues());
////
////		// Subscribe an email endpoint to an Amazon SNS topic.
////		final SubscribeRequest subscribeRequest = new SubscribeRequest(topicArn, "sqs", queueURL);
////		snsClient.subscribe(subscribeRequest);
////
////		// Publish a message to an Amazon SNS topic.
////		final String msg = "If you receive this message, publishing a message to an Amazon SNS topic works.";
////		final PublishRequest publishRequest = new PublishRequest(topicArn, msg);
////		final PublishResult publishResponse = snsClient.publish(publishRequest);
////
////		// Print the MessageId of the message.
////		System.out.println("MessageId: " + publishResponse.getMessageId());
////
////		//Print the SNS published message from the subscribed queue
////		sqsClient.receiveMessage(queueURL).getMessages().forEach(System.out::println);
////
////	}