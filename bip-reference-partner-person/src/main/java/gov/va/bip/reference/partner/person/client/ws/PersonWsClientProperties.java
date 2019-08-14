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
 * 
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

	/** Set of server certificates to trust. Maps alias to PEM cert value */
	private Map<String, String> trustedCerts;

	/**
	 * Gets the key store.
	 *
	 * @return the key store
	 * @throws KeyStoreException the key store exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws CertificateException the certificate exception
	 * @throws InvalidKeySpecException the invalid key spec exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public final KeyStore getKeyStore() throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
			InvalidKeySpecException, IOException {
		if (getPublicCert() != null && getPrivateKey() != null) {
			return KeystoreUtils.createKeyStore(getPublicCert(), getPrivateKey(), getPrivateKeyPass(), getAlias());
		} else {
			return null;
		}
	}

	/**
	 * Gets the trust store.
	 *
	 * @return the trust store
	 * @throws KeyStoreException the key store exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws CertificateException the certificate exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public final KeyStore getTrustStore()
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		KeyStore trustStore = null;
		for (String certAlias : getTrustedCerts().keySet()) {
			if (trustStore == null) {
				trustStore = KeystoreUtils.createTrustStore(certAlias, getTrustedCerts().get(certAlias));
			} else {
				KeystoreUtils.addTrustedCert(certAlias, getTrustedCerts().get(certAlias), trustStore);
			}
		}
		return trustStore;
	}

	/**
	 * Gets the private key.
	 *
	 * @return the privateKey
	 */
	public final String getPrivateKey() {
		return privateKey;
	}

	/**
	 * Sets the private key.
	 *
	 * @param privateKey  the privateKey to set
	 */
	public final void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	/**
	 * Gets the private key pass.
	 *
	 * @return the privateKeyPass
	 */
	public final String getPrivateKeyPass() {
		if (privateKeyPass == null) {
			privateKeyPass = "";
		}
		return privateKeyPass;
	}

	/**
	 * Sets the private key pass.
	 *
	 * @param privateKeyPass   the privateKeyPass to set
	 */
	public final void setPrivateKeyPass(String privateKeyPass) {
		this.privateKeyPass = privateKeyPass;
	}

	/**
	 * Gets the public cert.
	 *
	 * @return the publicCert
	 */
	public final String getPublicCert() {
		return publicCert;
	}

	/**
	 * @param publicCert
	 *            the publicCert to set
	 */
	public final void setPublicCert(String publicCert) {
		this.publicCert = publicCert;
	}

	/**
	 * @return the trustedCerts
	 */
	public final Map<String, String> getTrustedCerts() {
		if (trustedCerts == null) {
			trustedCerts = new HashMap<>();
		}
		return trustedCerts;
	}

	/**
	 * Sets the trusted certs.
	 *
	 * @param trustedCerts   the trustedCerts to set
	 */
	public final void setTrustedCerts(Map<String, String> trustedCerts) {
		this.trustedCerts = trustedCerts;
	}

	/**
	 * Gets the alias.
	 *
	 * @return the alias
	 */
	public final String getAlias() {
		if (alias == null) {
			alias = DEFAULT_ALIAS;
		}
		return alias;
	}

	/**
	 * Sets the alias.
	 *
	 * @param alias   the alias to set
	 */
	public final void setAlias(String alias) {
		this.alias = alias;
	}

}
