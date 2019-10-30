package gov.va.bip.reference.person.client.ws;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import javax.validation.Valid;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import gov.va.bip.framework.exception.BipException;
import gov.va.bip.framework.exception.BipRuntimeException;
import gov.va.bip.framework.messages.MessageKeys;
import gov.va.bip.framework.messages.MessageSeverity;
import gov.va.bip.reference.partner.person.client.ws.PersonWsClient;
import gov.va.bip.reference.person.exception.PersonServiceException;
import gov.va.bip.reference.person.model.PersonByPidDomainRequest;
import gov.va.bip.reference.person.model.PersonByPidDomainResponse;
import gov.va.bip.reference.person.model.PersonInfoDomain;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = { "spring.cloud.bus.enabled=false", "spring.cloud.discovery.enabled=false",
                "spring.cloud.consul.enabled=false", "spring.cloud.config.discovery.enabled=false", "spring.cloud.vault.enabled=false"})
public class PersonPartnerHelperTest {
	
	@Autowired
	private PersonPartnerHelper personPartnerHelper;

	@Test
	public void findPersonByPid() throws BipException {
		final PersonByPidDomainRequest request = new PersonByPidDomainRequest();
		request.setParticipantID(6666345L);
		
		final PersonByPidDomainResponse result = personPartnerHelper.findPersonByPid(request);
		Assert.assertTrue(result.getMessages().isEmpty());
		@Valid PersonInfoDomain personInfo = result.getPersonInfo();

        assertNotNull(personInfo);

        assertEquals(new Long(6666345), personInfo.getParticipantId());
        assertEquals("912444689", personInfo.getFileNumber());
        assertEquals("912444689", personInfo.getSocSecNo());
        assertEquals("JANE", personInfo.getFirstName());
        assertEquals("M", personInfo.getMiddleName());
        assertEquals("DOE", personInfo.getLastName());
		
		
	}
	
	@Test
	public void findPersonByPid_BipException() throws BipException {
		final PersonByPidDomainRequest request = new PersonByPidDomainRequest();
		request.setParticipantID(1L);
		
		final PersonWsClient mock = Mockito.mock(PersonWsClient.class);
		when(mock.getPersonInfoByPtcpntId(Mockito.any())).thenAnswer(invocation -> {
			   throw new BipException(MessageKeys.BIP_SECURITY_TOKEN_INVALID, MessageSeverity.ERROR, HttpStatus.FORBIDDEN, "Unit Testing");
		});
		
		final PersonPartnerHelper classToTest = new PersonPartnerHelper();
		classToTest.setPersonWsClient(mock);
		
		try {
			classToTest.findPersonByPid(request);
			Assert.fail("BipException should have been thrown");
		} catch (final BipException ex) {
			Assert.assertNotNull(ex);
		}
	}
	
	@Test
	public void findPersonByPid_BipRuntimeException() throws BipException {
		final PersonByPidDomainRequest request = new PersonByPidDomainRequest();
		request.setParticipantID(1L);
		
		final PersonWsClient mock = Mockito.mock(PersonWsClient.class);
		when(mock.getPersonInfoByPtcpntId(Mockito.any())).thenAnswer(invocation -> {
			   throw new BipRuntimeException(MessageKeys.BIP_SECURITY_TOKEN_INVALID, MessageSeverity.ERROR, HttpStatus.FORBIDDEN, "Unit Testing");
		});
		
		final PersonPartnerHelper classToTest = new PersonPartnerHelper();
		classToTest.setPersonWsClient(mock);
		
		try {
			classToTest.findPersonByPid(request);
			Assert.fail("PersonServiceException should have been thrown");
		} catch (final PersonServiceException ex) {
			Assert.assertNotNull(ex);
			Assert.assertEquals(MessageKeys.BIP_SECURITY_TOKEN_INVALID.getKey(), ex.getExceptionData().getMessageKey().getKey());
		}
	}
	
	@Test
	public void findPersonByPid_RuntimeException() throws BipException {
		final PersonByPidDomainRequest request = new PersonByPidDomainRequest();
		request.setParticipantID(1L);
		
		final PersonWsClient mock = Mockito.mock(PersonWsClient.class);
		when(mock.getPersonInfoByPtcpntId(Mockito.any())).thenAnswer(invocation -> {
			   throw new RuntimeException("Unit Testing");
		});
		
		final PersonPartnerHelper classToTest = new PersonPartnerHelper();
		classToTest.setPersonWsClient(mock);
		
		try {
			classToTest.findPersonByPid(request);
			Assert.fail("RuntimeException should have been thrown");
		} catch (final RuntimeException ex) {
			Assert.assertEquals("Unit Testing", ex.getMessage());
		}
	}
}
