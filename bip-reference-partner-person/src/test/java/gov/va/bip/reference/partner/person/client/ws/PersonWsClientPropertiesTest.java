package gov.va.bip.reference.partner.person.client.ws;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.security.Key;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.Base64;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import gov.va.bip.framework.config.BipCommonSpringProfiles;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(inheritListeners = false, listeners = { DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
@ActiveProfiles({ BipCommonSpringProfiles.PROFILE_REMOTE_CLIENT_SIMULATORS })
@ContextConfiguration(inheritLocations = false, classes = { PartnerMockFrameworkTestConfig.class, PersonWsClientProperties.class })
public class PersonWsClientPropertiesTest {
	
	@Autowired
	PersonWsClientProperties properties;
	
	@Value("${bip-reference-partner-person.ws.client.privateKey}")
	String privateKey;
	
	@Value("${bip-reference-partner-person.ws.client.privateKeyPass:}")
	String privateKeyPass;
	
	@Value("${bip-reference-partner-person.ws.client.publicCert}")
	String publicCert;
	
	@Value("${bip-reference-partner-person.ws.client.alias}")
	String alias;
	
	@Value("${bip-reference-partner-person.ws.client.trustedCerts.ca}")
	String trustedCert;
	
	@Test
	public void getPrivateKey() {
		String result = properties.getPrivateKey();
		assertEquals(privateKey, result);
	}
	
	@Test
	public void getPrivateKeyPass() {
		String result = properties.getPrivateKeyPass();
		assertEquals(privateKeyPass, result);
	}
	
	@Test
	public void getPrivateKeyPass_Null() {
		properties.setPrivateKeyPass(null);
		String result = properties.getPrivateKeyPass();
		assertEquals("", result);
	}
	
	@Test
	public void getPublicCert() {
		String result = properties.getPublicCert();
		assertEquals(publicCert, result);
	}
	
	@Test
	public void getAlias() {
		String result = properties.getAlias();
		assertEquals(alias, result);
	
		properties.setAlias(null);
		result = properties.getAlias();
		assertEquals(PersonWsClientProperties.DEFAULT_ALIAS, result);
	}
	

	@Test
	public void getKeyStore() throws Exception {
		
		KeyStore result = properties.getKeyStore();
		assertNotNull(result);
		
		assertTrue(result.containsAlias(properties.getAlias()));
		
		//Verify the Private key matches what we provided
		Key resultPK = result.getKey(properties.getAlias(), properties.getPrivateKeyPass().toCharArray());
		String encodedPK = "-----BEGIN PRIVATE KEY-----" + Base64.getEncoder().encodeToString(resultPK.getEncoded()) + "-----END PRIVATE KEY-----";
		assertEquals(properties.getPrivateKey(), encodedPK);
		
		//Verify our public certificate
		Certificate resultCert = result.getCertificate(properties.getAlias());
		String encodedCert = "-----BEGIN CERTIFICATE-----" + Base64.getEncoder().encodeToString(resultCert.getEncoded()) + "-----END CERTIFICATE-----";
		assertEquals(properties.getPublicCert(), encodedCert);
	}
	
	@Test
	public void getTrustedCerts() {
		Map<String, String> result = properties.getTrustedCerts();
		assertTrue(result.containsKey("ca"));
		assertEquals(trustedCert, result.get("ca"));
	}
	
	@Test
	public void getTrustStore() throws Exception {
		
		KeyStore result = properties.getTrustStore();
		assertNotNull(result);
		
		//Verify our public certificate
		for (String alias : properties.getTrustedCerts().keySet()) {
			Certificate resultCert = result.getCertificate(alias);
			String encodedCert = "-----BEGIN CERTIFICATE-----" + Base64.getEncoder().encodeToString(resultCert.getEncoded()) + "-----END CERTIFICATE-----";
			assertEquals(properties.getTrustedCerts().get(alias), encodedCert);
		}
	}
}
