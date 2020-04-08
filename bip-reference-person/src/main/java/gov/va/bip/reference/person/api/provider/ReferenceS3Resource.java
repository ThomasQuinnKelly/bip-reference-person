package gov.va.bip.reference.person.api.provider;

import gov.va.bip.framework.swagger.SwaggerResponseMessages;
import gov.va.bip.reference.person.ReferenceS3Service;
import gov.va.bip.reference.person.api.ReferencePersonAwsS3Api;
import gov.va.bip.reference.person.api.model.v1.BipListObjectsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@ConditionalOnProperty(name = "bip.framework.aws.s3.enabled", havingValue = "true")
public class ReferenceS3Resource implements ReferencePersonAwsS3Api, SwaggerResponseMessages {

    /** The service layer API contract for processing SNS requests */
    @Autowired
    @Qualifier("REFERENCE_S3_SERVICE_IMPL")
    private ReferenceS3Service refAwsPersonService;

    @Override
    public ResponseEntity<BipListObjectsResult> listObjects(@Valid String body) {

        return new ResponseEntity<>(refAwsPersonService.listObjects(body), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Void> putObject(String bucketName, String key, MultipartFile file) {

        return new ResponseEntity<>(refAwsPersonService.putObject(bucketName, key, file), HttpStatus.OK);

    }

}
