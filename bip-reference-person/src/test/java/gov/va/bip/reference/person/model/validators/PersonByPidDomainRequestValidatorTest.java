package gov.va.bip.reference.person.model.validators;

import gov.va.bip.framework.messages.MessageKeys;
import gov.va.bip.framework.messages.MessageSeverity;
import gov.va.bip.framework.messages.ServiceMessage;
import gov.va.bip.reference.person.messages.PersonMessageKeys;
import gov.va.bip.reference.person.model.PersonByPidDomainRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PersonByPidDomainRequestValidatorTest {

    PersonByPidDomainRequestValidator validator;
    PersonByPidDomainRequest toValidate;
    List<ServiceMessage> messages;

    Long pid = -9223372036854775807L;

    @Before
    public void setUp() {
        validator = new PersonByPidDomainRequestValidator();
        toValidate = new PersonByPidDomainRequest();
        toValidate.setParticipantID(pid);

        messages = new ArrayList<>();
    }

    @Test
    public void testPersonByPidDomainRequestValidatorNegative() {
        validator.validate(toValidate, messages);

        assertEquals(1, messages.size());
        assertEquals(MessageSeverity.ERROR, messages.get(0).getSeverity());
        assertEquals(Integer.valueOf(HttpStatus.BAD_REQUEST.value()), Integer.valueOf(messages.get(0).getStatus()));
        assertEquals(PersonMessageKeys.BIP_PERSON_INFO_REQUEST_PID_MIN.getKey(), messages.get(0).getKey());
    }

    @Test
    public void testPersonByPidDomainRequestValidatorNull() {
        toValidate.setParticipantID(null);
        validator.validate(toValidate, messages);

        assertEquals(1, messages.size());
        assertEquals(MessageSeverity.ERROR, messages.get(0).getSeverity());
        assertEquals(Integer.valueOf(HttpStatus.BAD_REQUEST.value()), Integer.valueOf(messages.get(0).getStatus()));
        assertEquals(MessageKeys.BIP_VALIDATOR_NOT_NULL.getKey(), messages.get(0).getKey());
    }

    @Test
    public void testPersonByPidDomainRequestValidator() {
        toValidate.setParticipantID(pid*-1);
        validator.validate(toValidate, messages);

        assertEquals(0, messages.size());
    }
}
