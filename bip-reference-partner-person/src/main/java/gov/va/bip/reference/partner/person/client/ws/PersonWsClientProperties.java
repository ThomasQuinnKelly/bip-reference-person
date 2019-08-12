package gov.va.bip.reference.partner.person.client.ws;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration class for the Person Partner WebService Client
 * @author jluck
 *
 */
@ConfigurationProperties("bip-reference-partner-person.ws.client")
public class PersonWsClientProperties {

	/** Private Key for client authentication in PEM format */
	private String privateKey;
	
	/** Private Key Password */
	private String privateKeyPass;
	
	/** Certificate for client authentication in PEM format */
	private String publicCert;
	
	/** Set of server certficates to trust. Maps alias to PEM cert value */
	private Map<String, String> trustedCerts;
}
