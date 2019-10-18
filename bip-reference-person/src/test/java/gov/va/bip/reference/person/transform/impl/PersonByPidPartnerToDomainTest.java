package gov.va.bip.reference.person.transform.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import gov.va.bip.reference.partner.person.ws.transfer.FindPersonByPtcpntIdResponse;
import gov.va.bip.reference.partner.person.ws.transfer.PersonDTO;
import gov.va.bip.reference.person.model.PersonByPidDomainResponse;

public class PersonByPidPartnerToDomainTest {

    PersonByPidPartnerToDomain personByPidPartnerToDomain;
    FindPersonByPtcpntIdResponse domainObject;
    PersonDTO personDTO;

    @Before
    public void setUp() {
        personByPidPartnerToDomain = new PersonByPidPartnerToDomain();
        domainObject = new FindPersonByPtcpntIdResponse();
        personDTO = new PersonDTO();

        personDTO.setFileNbr("555555555");
        personDTO.setSsnNbr("555555555");
        personDTO.setPtcpntId(99000000108392392L);
        personDTO.setFirstNm("Abraham");
        personDTO.setMiddleNm("Hanks");
        personDTO.setLastNm("Lincoln");
    }

    @Test
    public void testPartnerToDomainResponseObject() {
        // Person DTO Domain Assertions
        PersonByPidDomainResponse providerResponse = personByPidPartnerToDomain.convert(domainObject);

        assertNotNull(providerResponse);
        assertNull(providerResponse.getPersonInfo());

        domainObject.setPersonDTO(personDTO);

        providerResponse = personByPidPartnerToDomain.convert(domainObject);

        assertNotNull(providerResponse);
        assertNotNull(providerResponse.getPersonInfo());
        assertEquals("555555555", providerResponse.getPersonInfo().getFileNumber());
        assertEquals("555555555", providerResponse.getPersonInfo().getSocSecNo());
        assertEquals(Long.valueOf(99000000108392392L), providerResponse.getPersonInfo().getParticipantId());
        assertEquals("Abraham", providerResponse.getPersonInfo().getFirstName());
        assertEquals("Hanks", providerResponse.getPersonInfo().getMiddleName());
        assertEquals("Lincoln", providerResponse.getPersonInfo().getLastName());

        // Domain Object Null Assertions
        domainObject = null;

        providerResponse = personByPidPartnerToDomain.convert(domainObject);

        assertNotNull(providerResponse);
        assertNull(providerResponse.getPersonInfo());
    }

}
