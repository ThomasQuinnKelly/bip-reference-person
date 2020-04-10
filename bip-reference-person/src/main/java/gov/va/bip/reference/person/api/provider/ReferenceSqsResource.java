package gov.va.bip.reference.person.api.provider;

import gov.va.bip.framework.swagger.SwaggerResponseMessages;
import gov.va.bip.reference.person.ReferenceSqsService;
import gov.va.bip.reference.person.api.ReferencePersonAwsSqsApi;
import gov.va.bip.reference.person.api.model.v1.BipListQueuesResult;
import gov.va.bip.reference.person.api.model.v1.BipReceiveMessagesResult;
import gov.va.bip.reference.person.api.model.v1.BipSendMessageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@ConditionalOnProperty(name = "bip.framework.aws.sqs.enabled", havingValue = "true")
public class ReferenceSqsResource implements ReferencePersonAwsSqsApi, SwaggerResponseMessages {

    /** The service layer API contract for processing SNS requests */
    @Autowired
    @Qualifier("REFERENCE_SQS_SERVICE_IMPL")
    private ReferenceSqsService refAwsPersonService;

    @Override
    public ResponseEntity<BipListQueuesResult> listQueues() {

        return new ResponseEntity<>(refAwsPersonService.listQueues(null), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<BipListQueuesResult> listQueuesByPrefix(@Valid String body) {

        return new ResponseEntity<>(refAwsPersonService.listQueues(body), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<BipSendMessageResult> sendMessage(String queueName, String messageBody) {

        return new ResponseEntity<>(refAwsPersonService.sendMessage(queueName, messageBody), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<BipReceiveMessagesResult> receiveMessages(@Valid String body) {

        return new ResponseEntity<>(refAwsPersonService.receiveMessages(body), HttpStatus.OK);

    }

}
