package gov.va.bip.reference.person.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import gov.va.bip.framework.log.BipLogger;
import gov.va.bip.framework.log.BipLoggerFactory;
import gov.va.bip.framework.s3.config.S3Properties;
import gov.va.bip.reference.person.ReferenceS3Service;
import gov.va.bip.reference.person.api.model.v1.BipListObjectsResult;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Implementation class for the Reference Person Service to demonstrate AWS S3 capabilities of the BLUE Framework.
 */
@Service(value = ReferenceS3ServiceImpl.BEAN_NAME)
@Component
@Qualifier("REFERENCE_S3_SERVICE_IMPL")
@ConditionalOnProperty(name = "bip.framework.aws.s3.enabled", havingValue = "true")
@RefreshScope
public class ReferenceS3ServiceImpl implements ReferenceS3Service {

	/** Bean name constant */
	public static final String BEAN_NAME = "referenceS3ServiceImpl";

	private static final BipLogger LOGGER = BipLoggerFactory.getLogger(ReferenceS3ServiceImpl.class);

	@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
	@Autowired
	@Qualifier("first-bucket-id")
	AmazonS3 myBucket;

	@Autowired
	S3Properties s3Properties;

	@Override
	@CircuitBreaker(name = "putObject")
	public Void putObject(final String bucketName, final String key, final MultipartFile file) {
		try {
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType(file.getContentType());
			metadata.setContentLength(file.getSize());

			InputStream fileInputStream = file.getInputStream();

			PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, fileInputStream, metadata);

			myBucket.putObject(putObjectRequest);

		} catch (IOException e) {
			LOGGER.error("IO issue when attempting to upload a file to S3!", e);
		}

		return null;
	}

	@Override
	@CircuitBreaker(name = "listObjects")
	public BipListObjectsResult listObjects(final String bucketName) {

		ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request();
		listObjectsV2Request.setBucketName(bucketName);

		ListObjectsV2Result awsResult = myBucket.listObjectsV2(listObjectsV2Request);

		List<S3ObjectSummary> objectSummaries = awsResult.getObjectSummaries();
		BipListObjectsResult result = new BipListObjectsResult();

		for(S3ObjectSummary summary : objectSummaries) {
			result.addObjectKeysItem(summary.getKey());
		}

		return result;
	}
}