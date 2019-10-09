package gov.va.bip.reference.person.transform.impl;

import gov.va.bip.framework.messages.MessageSeverity;
import gov.va.bip.framework.rest.provider.Message;
import gov.va.bip.reference.person.api.model.v1.PersonDocsMetadataResponse;
import gov.va.bip.reference.person.messages.PersonMessageKeys;
import gov.va.bip.reference.person.model.PersonDocsMetadataDomain;
import gov.va.bip.reference.person.model.PersonDocsMetadataDomainResponse;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class PersonDocsMetadataDomainToProviderTest {

    PersonDocsMetadataDomainToProvider personDocsMetadataDomainToProvider;
    PersonDocsMetadataDomainResponse domainObject;
    PersonDocsMetadataDomain personDocsMetadataDomain;

    @Before
    public void setUp() {
        personDocsMetadataDomainToProvider = new PersonDocsMetadataDomainToProvider();
        domainObject = new PersonDocsMetadataDomainResponse();
        personDocsMetadataDomain = new PersonDocsMetadataDomain();

        personDocsMetadataDomain.setDocName("Special Doc");
        personDocsMetadataDomain.setDocCreateDate("10/09/2019");
    }

    @Test
    public void testDomainResponseObjectToProvider() {
        // Person Info Domain Assertions
        PersonDocsMetadataResponse personDocsMetadataResponse = personDocsMetadataDomainToProvider.convert(domainObject);

        assertNotNull(personDocsMetadataResponse);
        assertNotNull(personDocsMetadataResponse.getPersonDocsMetadata());
        assertNull(personDocsMetadataResponse.getPersonDocsMetadata().getDocName());
        assertEquals(0, personDocsMetadataResponse.getMessages().size());

        domainObject.setPersonDocsMetadataDomain(personDocsMetadataDomain);

        personDocsMetadataResponse = personDocsMetadataDomainToProvider.convert(domainObject);

        assertNotNull(personDocsMetadataResponse);
        assertNotNull(personDocsMetadataResponse.getPersonDocsMetadata());
        assertEquals("Special Doc", personDocsMetadataResponse.getPersonDocsMetadata().getDocName());
        assertEquals("10/09/2019", personDocsMetadataResponse.getPersonDocsMetadata().getDocCreateDate());
        assertEquals(0, personDocsMetadataResponse.getMessages().size());

        // Messages Assertions
        domainObject.addMessage(MessageSeverity.DEBUG, HttpStatus.ACCEPTED, PersonMessageKeys.BIP_PERSON_DOCS_METADATA_NOTNULL);

        personDocsMetadataResponse = personDocsMetadataDomainToProvider.convert(domainObject);

        assertNotNull(personDocsMetadataResponse);

        List<Message> messages = personDocsMetadataResponse.getMessages();

        assertNotNull(personDocsMetadataResponse.getPersonDocsMetadata());
        assertEquals(1, messages.size());
        assertEquals(MessageSeverity.DEBUG.value(), messages.get(0).getSeverity());
        assertEquals(Integer.valueOf(HttpStatus.ACCEPTED.value()), Integer.valueOf(messages.get(0).getStatus()));
        assertEquals(PersonMessageKeys.BIP_PERSON_DOCS_METADATA_NOTNULL.getKey(), messages.get(0).getKey());

        // Domain Object Null Assertions
        domainObject = null;

        personDocsMetadataResponse = personDocsMetadataDomainToProvider.convert(domainObject);

        assertNotNull(personDocsMetadataResponse);
        assertNotNull(personDocsMetadataResponse.getPersonDocsMetadata());
        assertNull(personDocsMetadataResponse.getPersonDocsMetadata().getDocName());
        assertEquals(0, personDocsMetadataResponse.getMessages().size());
    }

}
