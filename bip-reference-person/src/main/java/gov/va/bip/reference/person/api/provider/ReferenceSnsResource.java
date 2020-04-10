package gov.va.bip.reference.person.api.provider;

import gov.va.bip.framework.swagger.SwaggerResponseMessages;
import gov.va.bip.reference.person.ReferenceSnsService;
import gov.va.bip.reference.person.api.ReferencePersonAwsSnsApi;
import gov.va.bip.reference.person.api.model.v1.BipPublishResult;
import gov.va.bip.reference.person.api.model.v1.BipSubscribeRequest;
import gov.va.bip.reference.person.api.model.v1.BipSubscribeResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@ConditionalOnProperty(name = "bip.framework.aws.sns.enabled", havingValue = "true")
public class ReferenceSnsResource implements ReferencePersonAwsSnsApi, SwaggerResponseMessages {

    /** The service layer API contract for processing SNS requests */
    @Autowired
    @Qualifier("REFERENCE_SNS_SERVICE_IMPL")
    private ReferenceSnsService refAwsPersonService;

    @Override
    public ResponseEntity<BipPublishResult> publishMessage(@Valid String message) {

        return new ResponseEntity<>(refAwsPersonService.publishMessage(message), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<BipSubscribeResult> subscribe(@Valid BipSubscribeRequest bipSubscribeRequest) {

        return new ResponseEntity<>(refAwsPersonService.subscribe(bipSubscribeRequest), HttpStatus.OK);

    }

}
