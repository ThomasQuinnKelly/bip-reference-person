package gov.va.bip.reference.person.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import gov.va.bip.framework.exception.BipException;
import gov.va.bip.framework.messages.MessageKeys;
import gov.va.bip.framework.messages.MessageSeverity;
import gov.va.bip.framework.security.PersonTraits;
import gov.va.bip.reference.person.client.ws.PersonPartnerHelper;
import gov.va.bip.reference.person.data.PersonDataHelper;
import gov.va.bip.reference.person.exception.PersonServiceException;
import gov.va.bip.reference.person.model.PersonByPidDomainRequest;
import gov.va.bip.reference.person.model.PersonByPidDomainResponse;
import gov.va.bip.reference.person.utils.CacheConstants;

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
		Authentication auth = new UsernamePasswordAuthenticationToken(personTraits, personTraits.getPassword(),
				personTraits.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	@Test
	public void testGetPersonById() throws BipException {
		PersonByPidDomainRequest personByIdDomainRequest = new PersonByPidDomainRequest();
		PersonByPidDomainResponse personByIdDomainResponse = new PersonByPidDomainResponse();

		when(cacheManager.getCache(any())).thenReturn(null);
		when(personPartnerHelper.findPersonByPid(personByIdDomainRequest)).thenReturn(personByIdDomainResponse);
		PersonByPidDomainResponse result = refPersonService.findPersonByParticipantID(personByIdDomainRequest);
		assertEquals(0, result.getMessages().size());
	}

	@Test
	public void testGetPersonByIdBipException() {
		PersonByPidDomainRequest personByIdDomainRequest = new PersonByPidDomainRequest();

		when(cacheManager.getCache(any())).thenReturn(null);
		try {
			when(personPartnerHelper.findPersonByPid(personByIdDomainRequest)).thenThrow(
					new BipException(MessageKeys.NO_KEY, MessageSeverity.ERROR, HttpStatus.INTERNAL_SERVER_ERROR, ""));
		} catch (BipException e) {
			fail("BipException not expected" + e);
		}
		PersonByPidDomainResponse result = refPersonService.findPersonByParticipantID(personByIdDomainRequest);
		assertEquals(1, result.getMessages().size());
		assertEquals(MessageKeys.NO_KEY.toString(), result.getMessages().get(0).getKey());
	}

	@Test
	public void testGetPersonByIdCache() {
		Cache cache = mock(Cache.class);
		Cache.ValueWrapper valWrapper = mock(Cache.ValueWrapper.class);
		PersonByPidDomainResponse personByIdDomainResponse = new PersonByPidDomainResponse();
		when(cacheManager.getCache(CacheConstants.CACHENAME_REFERENCE_PERSON_SERVICE)).thenReturn(cache);
		when(cache.get(any())).thenReturn(valWrapper);
		when(cache.get(any(), eq(PersonByPidDomainResponse.class))).thenReturn(personByIdDomainResponse);

		PersonByPidDomainRequest personByIdDomainRequest = new PersonByPidDomainRequest();

		PersonByPidDomainResponse result = refPersonService.findPersonByParticipantID(personByIdDomainRequest);
		assertSame(personByIdDomainResponse, result);

	}

	@Test
	public void testGetInfoForIcnException() {
		PersonByPidDomainRequest personByIdDomainRequest = new PersonByPidDomainRequest();

		when(cacheManager.getCache(any())).thenReturn(null);
		when(personDataHelper.getInfoForIcn(54321L))
				.thenThrow(new RuntimeException("Test Exception thrown for getInfoForIcn(54321L) call"));
		PersonByPidDomainResponse result = refPersonService.findPersonByParticipantID(personByIdDomainRequest);
		assertEquals(1, result.getMessages().size());
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

	@Test
	public void testFindPersonByParticipantIDFallBack() {
		PersonByPidDomainRequest personByIdDomainRequest = new PersonByPidDomainRequest();
		RuntimeException runtimeException = new RuntimeException("Test Exception");

		PersonByPidDomainResponse result = refPersonService.findPersonByParticipantIDFallBack(personByIdDomainRequest,
				runtimeException);
		assertEquals(1, result.getMessages().size());
	}

}
