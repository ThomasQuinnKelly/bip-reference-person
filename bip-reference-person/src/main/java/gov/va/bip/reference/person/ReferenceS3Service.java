package gov.va.bip.reference.person;

import com.amazonaws.services.s3.model.PutObjectResult;

import java.io.File;

/**
 * The contract interface for the Reference S3 domain (service) layer.
 *
 */
public interface ReferenceS3Service {

	PutObjectResult putObject(String bucketName, String key, File file);

}
