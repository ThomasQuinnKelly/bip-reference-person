package gov.va.bip.reference.partner.person.client.ws;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import gov.va.bip.framework.security.jks.KeystoreUtils;

/**
 * Configuration class for the Person Partner WebService Client
 * @author jluck
 *
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("bip-reference-partner-person.ws.client")
public class PersonWsClientProperties {
	
	static final String DEFAULT_ALIAS = "client";

	/** Private Key for client authentication in PEM format */
	private String privateKey;
	
	/** Private Key Password */
	private String privateKeyPass;
	
	/** Certificate for client authentication in PEM format */
	private String publicCert;
	
	/** Alias used for the Keystore */
	private String alias;
	
	/** Set of server certficates to trust. Maps alias to PEM cert value */
	private Map<String, String> trustedCerts;
	
	public final KeyStore getKeyStore() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, InvalidKeySpecException, IOException {
		return KeystoreUtils.createKeyStore(getPublicCert(), getPrivateKey(), getPrivateKeyPass(), alias);
	}
	
	public final KeyStore getTrustStore() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		KeyStore trustStore = null;
		for (String alias : getTrustedCerts().keySet()) {
			if (trustStore == null) {
				trustStore = KeystoreUtils.createTrustStore(alias, getTrustedCerts().get(alias));
			} else {
				KeystoreUtils.addTrustedCert(alias, getTrustedCerts().get(alias), trustStore);
			}
		}
		return trustStore;
	}

	/**
	 * @return the privateKey
	 */
	public final String getPrivateKey() {
		return privateKey;
	}

	/**
	 * @param privateKey the privateKey to set
	 */
	public final void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	/**
	 * @return the privateKeyPass
	 */
	public final String getPrivateKeyPass() {
		if (privateKeyPass == null) {
			privateKeyPass = "";
		}
		return privateKeyPass;
	}

	/**
	 * @param privateKeyPass the privateKeyPass to set
	 */
	public final void setPrivateKeyPass(String privateKeyPass) {
		this.privateKeyPass = privateKeyPass;
	}

	/**
	 * @return the publicCert
	 */
	public final String getPublicCert() {
		return publicCert;
	}

	/**
	 * @param publicCert the publicCert to set
	 */
	public final void setPublicCert(String publicCert) {
		this.publicCert = publicCert;
	}

	/**
	 * @return the trustedCerts
	 */
	public final Map<String, String> getTrustedCerts() {
		if (trustedCerts == null) {
			trustedCerts = new HashMap<String, String>();
		}
		return trustedCerts;
	}

	/**
	 * @param trustedCerts the trustedCerts to set
	 */
	public final void setTrustedCerts(Map<String, String> trustedCerts) {
		this.trustedCerts = trustedCerts;
	}

	/**
	 * @return the alias
	 */
	public final String getAlias() {
		if (alias == null) {
			alias = DEFAULT_ALIAS;
		}
		return alias;
	}

	/**
	 * @param alias the alias to set
	 */
	public final void setAlias(String alias) {
		this.alias = alias;
	}
	
	
}
