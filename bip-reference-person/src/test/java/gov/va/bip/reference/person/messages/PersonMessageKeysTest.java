package gov.va.bip.reference.person.messages;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PersonMessageKeysTest {

    PersonMessageKeys bipPersonDocsMetadataNotNull;
    PersonMessageKeys bipPersonDocsMetadataRequestPidMin;
    PersonMessageKeys bipPersonInfoRequestNotNull;
    PersonMessageKeys bipPersonInfoRequestPidInconsistent;
    PersonMessageKeys bipPersonInfoRequestPidMin;
    PersonMessageKeys bipPersonInfoRequestPidNotFound;
    PersonMessageKeys bipPersonInvalidDate;

    @Before
    public void setUp() {
        bipPersonDocsMetadataNotNull = PersonMessageKeys.BIP_PERSON_DOCS_METADATA_NOTNULL;
        bipPersonDocsMetadataRequestPidMin = PersonMessageKeys.BIP_PERSON_DOCS_METADATA_REQUEST_PID_MIN;
        bipPersonInfoRequestNotNull = PersonMessageKeys.BIP_PERSON_INFO_REQUEST_NOTNULL;
        bipPersonInfoRequestPidInconsistent = PersonMessageKeys.BIP_PERSON_INFO_REQUEST_PID_INCONSISTENT;
        bipPersonInfoRequestPidMin = PersonMessageKeys.BIP_PERSON_INFO_REQUEST_PID_MIN;
        bipPersonInfoRequestPidNotFound = PersonMessageKeys.BIP_PERSON_INFO_REQUEST_PID_NOT_FOUND;
        bipPersonInvalidDate = PersonMessageKeys.BIP_PERSON_INVALID_DATE;
    }

    /**
     * Test of BIP_PERSON_DOCS_METADATA_NOTNULL enum, of class PersonMessageKeys.
     */
    @Test
    public void testBipPersonDocsMetadataNotNull() {

        assertEquals("NotNull.personDocsMetadata", bipPersonDocsMetadataNotNull.getKey());
        assertEquals("PersonDocsMetadata Payload cannot be null", bipPersonDocsMetadataNotNull.getMessage());

    }

    /**
     * Test of BIP_PERSON_DOCS_METADATA_REQUEST_PID_MIN enum, of class PersonMessageKeys.
     */
    @Test
    public void testBipPersonDocsMetadataRequestPidMin() {

        assertEquals("Min.personDocsMetadataRequest.participantID", bipPersonDocsMetadataRequestPidMin.getKey());
        assertEquals("personDocsMetadataRequest.participantID cannot be zero", bipPersonDocsMetadataRequestPidMin.getMessage());

    }

    /**
     * Test of BIP_PERSON_INFO_REQUEST_NOTNULL enum, of class PersonMessageKeys.
     */
    @Test
    public void testBipPersonInfoRequestNotNull() {

        assertEquals("NotNull.personInfoRequest", bipPersonInfoRequestNotNull.getKey());
        assertEquals("PersonInfoRequest Payload cannot be null.", bipPersonInfoRequestNotNull.getMessage());

    }

    /**
     * Test of BIP_PERSON_INFO_REQUEST_PID_INCONSISTENT enum, of class PersonMessageKeys.
     */
    @Test
    public void testBipPersonInfoRequestPidInconsistent() {

        assertEquals("bip.reference.person.info.request.pid.inconsistent", bipPersonInfoRequestPidInconsistent.getKey());
        assertEquals("Response has different PID than the request.", bipPersonInfoRequestPidInconsistent.getMessage());

    }

    /**
     * Test of BIP_PERSON_INFO_REQUEST_PID_MIN enum, of class PersonMessageKeys.
     */
    @Test
    public void testBipPersonInfoRequestPidMin() {

        assertEquals("Min.personInfoRequest.participantID", bipPersonInfoRequestPidMin.getKey());
        assertEquals("Participant ID must be greater than zero.", bipPersonInfoRequestPidMin.getMessage());

    }

    /**
     * Test of BIP_PERSON_INFO_REQUEST_PID_NOT_FOUND enum, of class PersonMessageKeys.
     */
    @Test
    public void testBipPersonInfoRequestPidNotFound() {

        assertEquals("bip.reference.person.pid.not.found", bipPersonInfoRequestPidNotFound.getKey());
        assertEquals("Pid value could not be found.", bipPersonInfoRequestPidNotFound.getMessage());

    }

    /**
     * Test of BIP_PERSON_INVALID_DATE enum, of class PersonMessageKeys.
     */
    @Test
    public void testBipPersonInvalidDate() {

        assertEquals("bip.reference.person.invalid.date", bipPersonInvalidDate.getKey());
        assertEquals("Date value given in the request is not valid.", bipPersonInvalidDate.getMessage());

    }

}
