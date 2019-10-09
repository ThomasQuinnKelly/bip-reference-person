package gov.va.bip.reference.person.transform.impl;

import gov.va.bip.reference.partner.person.ws.transfer.FindPersonByPtcpntId;
import gov.va.bip.reference.person.model.PersonByPidDomainRequest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class PersonByPidDomainToPartnerTest {

    PersonByPidDomainToPartner personByPidDomainToPartner;
    PersonByPidDomainRequest domainObject;

    @Before
    public void setUp() {
        personByPidDomainToPartner = new PersonByPidDomainToPartner();
        domainObject = new PersonByPidDomainRequest();
    }

    @Test
    public void testDomainRequestObjectToPartner() {
        FindPersonByPtcpntId find = personByPidDomainToPartner.convert(domainObject);

        assertNotNull(find);
        assertNull(find.getPtcpntId());

        domainObject.setParticipantID(45L);

        find = personByPidDomainToPartner.convert(domainObject);

        assertNotNull(find);
        assertNotNull(find.getPtcpntId());
        assertEquals(domainObject.getParticipantID(), find.getPtcpntId());

        domainObject = null;

        find = personByPidDomainToPartner.convert(domainObject);

        assertNotNull(find);
        assertNull(find.getPtcpntId());

    }

}
