package gov.va.bip.reference.person.model.validators;

import gov.va.bip.framework.messages.ServiceMessage;
import gov.va.bip.reference.person.exception.PersonServiceException;
import gov.va.bip.reference.person.messages.PersonMessageKeys;
import gov.va.bip.reference.person.model.PersonByPidDomainResponse;
import gov.va.bip.reference.person.model.PersonInfoDomain;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PersonByPidDomainResponseValidatorTest {

    PersonByPidDomainResponseValidator validator;
    Method callingMethod;
    PersonByPidDomainResponse toValidate;
    PersonInfoDomain personInfo;
    List<ServiceMessage> messages;

    @Before
    public void setUp() throws NoSuchMethodException {
        validator = new PersonByPidDomainResponseValidator();
        callingMethod = PersonByPidDomainResponseValidatorTest.class.getMethod("testMethod");
        toValidate = new PersonByPidDomainResponse();

        personInfo = new PersonInfoDomain();
        personInfo.setParticipantId(1L);

        toValidate.setPersonInfo(personInfo);

        messages = new ArrayList<>();
    }

    @Test
    public void testMethod() {
        validator.setCallingMethod(callingMethod);
        assertEquals("testMethod", validator.getCallingMethod().getName());
    }

    @Test
    public void testPersonByPidDomainResponseValidatorNullExceptions() {
        //Person Info null
        toValidate.setPersonInfo(null);

        try {
            validator.validate(toValidate, messages);
            fail("PersonServiceException was expected");
        } catch (PersonServiceException pse) {
            assertEquals(PersonMessageKeys.BIP_PERSON_INFO_REQUEST_NOTNULL.getMessage(), pse.getMessage());
        }

        // PersonByPidDomainResponse null
        toValidate = null;

        try {
            validator.validate(toValidate, messages);
            fail("PersonServiceException was expected");
        } catch (PersonServiceException pse) {
            assertEquals(PersonMessageKeys.BIP_PERSON_INFO_REQUEST_NOTNULL.getMessage(), pse.getMessage());
        }
    }


    @Test
    public void testPersonByPidDomainResponseValidatorNoPersonTraits() {
        validator.validate(toValidate, messages);

        assertEquals(0, messages.size());

    }
}
