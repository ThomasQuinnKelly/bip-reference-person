package gov.va.bip.reference.person.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import gov.va.bip.framework.s3.config.S3Properties;
import gov.va.bip.framework.sns.config.SnsProperties;
import gov.va.bip.reference.person.ReferenceS3Service;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Implementation class for the Reference Person Service to demonstrate AWS S3 capabilities of the BLUE Framework.
 */
@Service(value = ReferenceS3ServiceImpl.BEAN_NAME)
@Component
@Qualifier("REFERENCE_S3_SERVICE_IMPL")
@RefreshScope
public class ReferenceS3ServiceImpl implements ReferenceS3Service {

	/** Bean name constant */
	public static final String BEAN_NAME = "referenceS3ServiceImpl";

	@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
	@Autowired
	@Qualifier("firstS3Id")
	AmazonS3 myBucket;

	@Autowired
	S3Properties s3Properties;

	@Override
	@CircuitBreaker(name = "publishMessage")
	public PutObjectResult putObject(final String bucketName, final String key, final File file) {

		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);

		PutObjectResult result = myBucket.putObject(putObjectRequest);

		return result;
	}
}