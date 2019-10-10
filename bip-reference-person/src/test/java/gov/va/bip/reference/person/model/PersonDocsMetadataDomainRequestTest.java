package gov.va.bip.reference.person.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PersonDocsMetadataDomainRequestTest {

    PersonDocsMetadataDomainRequest personDocsMetadataDomainRequest;
    Long pid = 9223372036854775807L;

    @Before
    public void setUp() {
        personDocsMetadataDomainRequest = new PersonDocsMetadataDomainRequest();
    }

    @Test
    public void testPersonDocsMetadataDomainRequest() {

        personDocsMetadataDomainRequest.setParticipantID(pid);

        assertEquals(pid, personDocsMetadataDomainRequest.getParticipantID());
    }

}
