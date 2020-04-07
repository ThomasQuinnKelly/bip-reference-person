package gov.va.bip.reference.person.impl;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.SubscribeResult;
import gov.va.bip.framework.sns.config.SnsProperties;
import gov.va.bip.reference.person.ReferenceSnsService;
import gov.va.bip.reference.person.api.model.v1.BipPublishResult;
import gov.va.bip.reference.person.api.model.v1.BipSubscribeRequest;
import gov.va.bip.reference.person.api.model.v1.BipSubscribeResult;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Implementation class for the Reference Person Service to demonstrate AWS SNS capabilities of the BLUE Framework.
 */
@Service(value = ReferenceSnsServiceImpl.BEAN_NAME)
@Component
@Qualifier("REFERENCE_SNS_SERVICE_IMPL")
@RefreshScope
public class ReferenceSnsServiceImpl implements ReferenceSnsService {

	/** Bean name constant */
	public static final String BEAN_NAME = "referenceSnsServiceImpl";

	@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
	@Autowired
	@Qualifier("first-topic-id")
	AmazonSNS myTopic;

	@Autowired
	SnsProperties snsProperties;

	@Override
	@CircuitBreaker(name = "publishMessage")
	public BipPublishResult publishMessage(final String message) {

		PublishRequest publishRequest = new PublishRequest();
		publishRequest.setMessage(message);
		publishRequest.setTopicArn(snsProperties.getTopics().get(0).getTopicArn());

		PublishResult awsResult = myTopic.publish(publishRequest);

		BipPublishResult bipResult = new BipPublishResult();
		bipResult.setMessageId(awsResult.getMessageId());

		return bipResult;
	}

	@Override
	@CircuitBreaker(name = "subscribe")
	public BipSubscribeResult subscribe(final BipSubscribeRequest bipSubscribeRequest) {

		SubscribeRequest subscribeRequest = new SubscribeRequest();
		subscribeRequest.setTopicArn(snsProperties.getTopics().get(0).getTopicArn());
		subscribeRequest.setProtocol(bipSubscribeRequest.getProtocol());
		subscribeRequest.setEndpoint(bipSubscribeRequest.getEndpoint());

		SubscribeResult awsResult = myTopic.subscribe(subscribeRequest);

		BipSubscribeResult bipResult = new BipSubscribeResult();
		bipResult.setSubscriptionArn(awsResult.getSubscriptionArn());

		return bipResult;
	}
}