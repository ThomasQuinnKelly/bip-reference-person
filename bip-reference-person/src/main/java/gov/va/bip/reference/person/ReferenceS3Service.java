package gov.va.bip.reference.person;

import gov.va.bip.reference.person.api.model.v1.BipListObjectsResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * The contract interface for the Reference S3 domain (service) layer.
 *
 */
public interface ReferenceS3Service {

	Void putObject(String bucketName, String key, MultipartFile file);

	BipListObjectsResult listObjects(String bucketName);

}
