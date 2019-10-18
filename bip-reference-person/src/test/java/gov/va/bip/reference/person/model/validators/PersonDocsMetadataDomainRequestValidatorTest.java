package gov.va.bip.reference.person.model.validators;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import gov.va.bip.framework.messages.MessageKeys;
import gov.va.bip.framework.messages.MessageSeverity;
import gov.va.bip.framework.messages.ServiceMessage;
import gov.va.bip.reference.person.messages.PersonMessageKeys;
import gov.va.bip.reference.person.model.PersonDocsMetadataDomainRequest;

public class PersonDocsMetadataDomainRequestValidatorTest {

    PersonDocsMetadataDomainRequestValidator validator;
    PersonDocsMetadataDomainRequest toValidate;
    List<ServiceMessage> messages;

    Long pid = -9223372036854775807L;

    @Before
    public void setUp() {
        validator = new PersonDocsMetadataDomainRequestValidator();
        toValidate = new PersonDocsMetadataDomainRequest();
        toValidate.setParticipantID(pid);

        messages = new ArrayList<>();
    }

    @Test
    public void testPersonDocsMetadataDomainRequestValidatorNegative() {
        validator.validate(toValidate, messages);

        assertEquals(1, messages.size());
        assertEquals(MessageSeverity.ERROR, messages.get(0).getSeverity());
        assertEquals(Integer.valueOf(HttpStatus.BAD_REQUEST.value()), Integer.valueOf(messages.get(0).getStatus()));
        assertEquals(PersonMessageKeys.BIP_PERSON_DOCS_METADATA_REQUEST_PID_MIN.getKey(), messages.get(0).getKey());
    }

    @Test
    public void testPersonDocsMetadataDomainRequestValidatorNull() {
        toValidate.setParticipantID(null);
        validator.validate(toValidate, messages);

        assertEquals(1, messages.size());
        assertEquals(MessageSeverity.ERROR, messages.get(0).getSeverity());
        assertEquals(Integer.valueOf(HttpStatus.BAD_REQUEST.value()), Integer.valueOf(messages.get(0).getStatus()));
        assertEquals(MessageKeys.BIP_VALIDATOR_NOT_NULL.getKey(), messages.get(0).getKey());
    }

    @Test
    public void testPersonDocsMetadataDomainRequestValidator() {
        toValidate.setParticipantID(pid*-1);
        validator.validate(toValidate, messages);

        assertEquals(0, messages.size());
    }
}
