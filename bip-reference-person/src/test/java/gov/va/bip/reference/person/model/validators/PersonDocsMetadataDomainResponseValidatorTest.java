package gov.va.bip.reference.person.model.validators;

import gov.va.bip.framework.messages.ServiceMessage;
import gov.va.bip.reference.person.exception.PersonServiceException;
import gov.va.bip.reference.person.messages.PersonMessageKeys;
import gov.va.bip.reference.person.model.PersonDocsMetadataDomain;
import gov.va.bip.reference.person.model.PersonDocsMetadataDomainResponse;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PersonDocsMetadataDomainResponseValidatorTest {

    PersonDocsMetadataDomainResponseValidator validator;
    Method callingMethod;
    PersonDocsMetadataDomainResponse toValidate;
    PersonDocsMetadataDomain personDocsMetadataDomain;
    List<ServiceMessage> messages;

    @Before
    public void setUp() throws NoSuchMethodException {
        validator = new PersonDocsMetadataDomainResponseValidator();

        callingMethod = PersonDocsMetadataDomainResponseValidatorTest.class.getMethod("testMethod");

        toValidate = new PersonDocsMetadataDomainResponse();

        personDocsMetadataDomain = new PersonDocsMetadataDomain();
        personDocsMetadataDomain.setDocName("DocName");

        toValidate.setPersonDocsMetadataDomain(personDocsMetadataDomain);

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
        toValidate.setPersonDocsMetadataDomain(null);

        try {
            validator.validate(toValidate, messages);
            fail("PersonServiceException was expected");
        } catch (PersonServiceException pse) {
            assertEquals(PersonMessageKeys.BIP_PERSON_DOCS_METADATA_NOTNULL.getMessage(), pse.getMessage());
        }

        // PersonByPidDomainResponse null
        toValidate = null;

        try {
            validator.validate(toValidate, messages);
            fail("PersonServiceException was expected");
        } catch (PersonServiceException pse) {
            assertEquals(PersonMessageKeys.BIP_PERSON_DOCS_METADATA_NOTNULL.getMessage(), pse.getMessage());
        }
    }

    @Test
    public void testPersonDocsMetadataDomainResponseValidator() {
        validator.validate(toValidate, messages);

        assertEquals(0, messages.size());
    }

}
