package gov.va.bip.reference.person.api.provider;

import gov.va.bip.framework.swagger.SwaggerResponseMessages;
import gov.va.bip.reference.person.AwsPersonService;
import gov.va.bip.reference.person.api.ReferencePersonAwsApi;
import gov.va.bip.reference.person.api.model.v1.JmsResponse;
import gov.va.bip.reference.person.api.model.v1.PublishResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AwsResource implements ReferencePersonAwsApi, SwaggerResponseMessages {

    /** The service layer API contract for processing personByPid() requests */
    @Autowired
    @Qualifier("AWS_PERSON_SERVICE_IMPL")
    private AwsPersonService refAwsPersonService;

    @Override
    public ResponseEntity<JmsResponse> sendMessage(@Valid String body) {

        ResponseEntity<JmsResponse> jmsId = new ResponseEntity<JmsResponse>(refAwsPersonService.sendMessage(body), HttpStatus.OK);

        return jmsId;
    }

    @Override
    public ResponseEntity<PublishResult> publishMessage(@Valid String body) {

        ResponseEntity<PublishResult> messageId = new ResponseEntity<PublishResult>(refAwsPersonService.publishMessage(body), HttpStatus.OK);

        return messageId;
    }

}
