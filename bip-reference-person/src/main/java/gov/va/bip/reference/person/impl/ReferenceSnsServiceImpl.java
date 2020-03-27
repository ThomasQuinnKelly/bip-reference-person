package gov.va.bip.reference.person.impl;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import gov.va.bip.framework.sns.config.SnsProperties;
import gov.va.bip.reference.person.ReferenceSnsService;
import gov.va.bip.reference.person.api.model.v1.BipPublishResult;
import gov.va.bip.reference.person.sqs.service.MessageAttributes;
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
	@Qualifier("firstSnsId")
	AmazonSNS myTopic;

	@Autowired
	SnsProperties snsProperties;

	@Override
	@CircuitBreaker(name = "publishMessage")
	public BipPublishResult publishMessage(final String message) {

		PublishRequest publishRequest = new PublishRequest();
		publishRequest.setMessage(new MessageAttributes(message).toJson());
		publishRequest.setTopicArn(snsProperties.getTopics().get(0).getTopicArn());

		PublishResult awsResult = myTopic.publish(publishRequest);

		BipPublishResult bipResult = new BipPublishResult();
		bipResult.setMessageId(awsResult.getMessageId());

		return bipResult;
	}
}