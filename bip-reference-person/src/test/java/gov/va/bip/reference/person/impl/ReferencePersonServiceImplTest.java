package gov.va.bip.reference.person.impl;

import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import gov.va.bip.framework.exception.BipException;
import gov.va.bip.framework.security.PersonTraits;
import gov.va.bip.reference.person.client.ws.PersonPartnerHelper;
import gov.va.bip.reference.person.data.PersonDataHelper;
import gov.va.bip.reference.person.exception.PersonServiceException;
import gov.va.bip.reference.person.model.PersonByPidDomainRequest;
import gov.va.bip.reference.person.model.PersonByPidDomainResponse;

@RunWith(SpringRunner.class)
public class ReferencePersonServiceImplTest {

	@InjectMocks
	ReferencePersonServiceImpl refPersonService;
	
	@Mock
	PersonDataHelper personDataHelper;
	
	@Mock
	PersonPartnerHelper personPartnerHelper;

    @Mock
    CacheManager cacheManager;

    @Before
    public void setUp() {
		PersonTraits personTraits = new PersonTraits("user", "password",
				AuthorityUtils.createAuthorityList("ROLE_TEST"));
		Authentication auth = new UsernamePasswordAuthenticationToken(personTraits,
				personTraits.getPassword(), personTraits.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
    }
    
    @Test
    public void testGetPersonById() throws BipException {
    	PersonByPidDomainRequest personByIdDomainRequest = new PersonByPidDomainRequest();
    	PersonByPidDomainResponse personByIdDomainResponse = new PersonByPidDomainResponse();

        when(cacheManager.getCache(any())).thenReturn(null);
        when(personPartnerHelper.findPersonByPid(personByIdDomainRequest))
                .thenReturn(personByIdDomainResponse);
        PersonByPidDomainResponse result = refPersonService.findPersonByParticipantID(personByIdDomainRequest);
        assertEquals(0, result.getMessages().size());
    }
    
	@Test
	public void testStoreMetadataNegative() {
		try {
			refPersonService.storeMetadata(54321L, "docName", "NonsensicalDate");
			fail("An PersonServiceException was expected here.");
		} catch (PersonServiceException pse) {
			assertEquals("Date value given in the request is not valid.", pse.getMessage());
		}
	}

   
}

