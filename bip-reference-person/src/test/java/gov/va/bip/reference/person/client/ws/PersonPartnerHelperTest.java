package gov.va.bip.reference.person.client.ws;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.validation.Valid;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import gov.va.bip.framework.exception.BipException;
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
}
