package gov.va.bip.reference.person.transform.impl;

import gov.va.bip.reference.partner.person.ws.transfer.FindPersonByPtcpntId;
import gov.va.bip.reference.person.api.model.v1.PersonInfoRequest;
import gov.va.bip.reference.person.model.PersonByPidDomainRequest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PersonByPidProviderToDomainTest {

    PersonByPidProviderToDomain personByPidProviderToDomain;
    PersonInfoRequest domainObject;

    @Before
    public void setUp() {
        personByPidProviderToDomain = new PersonByPidProviderToDomain();
        domainObject = new PersonInfoRequest();
    }

    @Test
    public void testProviderToDomainRequestObject() {
        PersonByPidDomainRequest find = personByPidProviderToDomain.convert(domainObject);

        assertNotNull(find);
        assertNull(find.getParticipantID());

        domainObject.setParticipantID(45L);

        find = personByPidProviderToDomain.convert(domainObject);

        assertNotNull(find);
        assertNotNull(find.getParticipantID());
        assertEquals(domainObject.getParticipantID(), find.getParticipantID());

        domainObject = null;

        find = personByPidProviderToDomain.convert(domainObject);

        assertNotNull(find);
        assertNull(find.getParticipantID());

    }
}
