package gov.va.bip.reference.person.transform.impl;

import gov.va.bip.framework.messages.MessageSeverity;
import gov.va.bip.framework.rest.provider.Message;
import gov.va.bip.reference.person.api.model.v1.PersonInfoResponse;
import gov.va.bip.reference.person.messages.PersonMessageKeys;
import gov.va.bip.reference.person.model.PersonByPidDomainResponse;
import gov.va.bip.reference.person.model.PersonInfoDomain;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.Assert.*;

public class PersonByPidDomainToProviderTest {

    PersonByPidDomainToProvider personByPidDomainToProvider;
    PersonByPidDomainResponse domainObject;
    PersonInfoDomain personInfoDomain;

    @Before
    public void setUp() {
        personByPidDomainToProvider = new PersonByPidDomainToProvider();
        domainObject = new PersonByPidDomainResponse();
        personInfoDomain = new PersonInfoDomain();

        personInfoDomain.setFileNumber("555555555");
        personInfoDomain.setSocSecNo("555555555");
        personInfoDomain.setParticipantId(99000000108392392L);
        personInfoDomain.setFirstName("Abraham");
        personInfoDomain.setMiddleName("Hanks");
        personInfoDomain.setLastName("Lincoln");
    }

    @Test
    public void testDomainResponseObjectToProvider() {
        // Person Info Domain Assertions
        PersonInfoResponse providerResponse = personByPidDomainToProvider.convert(domainObject);

        assertNotNull(providerResponse);
        assertNotNull(providerResponse.getPersonInfo());
        assertNull(providerResponse.getPersonInfo().getFileNumber());
        assertEquals(0, providerResponse.getMessages().size());

        domainObject.setPersonInfo(personInfoDomain);

        providerResponse = personByPidDomainToProvider.convert(domainObject);

        assertNotNull(providerResponse);
        assertNotNull(providerResponse.getPersonInfo());
        assertEquals("555555555", providerResponse.getPersonInfo().getFileNumber());
        assertEquals("555555555", providerResponse.getPersonInfo().getSocSecNo());
        assertEquals(Long.valueOf(99000000108392392L), providerResponse.getPersonInfo().getParticipantId());
        assertEquals("Abraham", providerResponse.getPersonInfo().getFirstName());
        assertEquals("Hanks", providerResponse.getPersonInfo().getMiddleName());
        assertEquals("Lincoln", providerResponse.getPersonInfo().getLastName());
        assertEquals(0, providerResponse.getMessages().size());

        // Messages Assertions
        domainObject.addMessage(MessageSeverity.DEBUG, HttpStatus.ACCEPTED, PersonMessageKeys.BIP_PERSON_DOCS_METADATA_NOTNULL);

        providerResponse = personByPidDomainToProvider.convert(domainObject);

        assertNotNull(providerResponse);

        List<Message> messages = providerResponse.getMessages();

        assertNotNull(providerResponse.getPersonInfo());
        assertEquals(1, messages.size());
        assertEquals(MessageSeverity.DEBUG.value(), messages.get(0).getSeverity());
        assertEquals(Integer.valueOf(HttpStatus.ACCEPTED.value()), Integer.valueOf(messages.get(0).getStatus()));
        assertEquals(PersonMessageKeys.BIP_PERSON_DOCS_METADATA_NOTNULL.getKey(), messages.get(0).getKey());

        // Domain Object Null Assertions
        domainObject = null;

        providerResponse = personByPidDomainToProvider.convert(domainObject);

        assertNotNull(providerResponse);
        assertNotNull(providerResponse.getPersonInfo());
        assertNull(providerResponse.getPersonInfo().getFileNumber());
        assertEquals(0, providerResponse.getMessages().size());
    }

}
