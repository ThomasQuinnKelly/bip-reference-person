package gov.va.bip.reference.person.api.provider;

import gov.va.bip.framework.swagger.SwaggerResponseMessages;
import gov.va.bip.reference.person.api.ReferencePersonAwsApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AwsResource implements ReferencePersonAwsApi, SwaggerResponseMessages {

    @Override
    public ResponseEntity<Integer> sendMessage(@Valid String body) {
        return null;
    }

}
