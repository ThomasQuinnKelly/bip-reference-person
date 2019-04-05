package gov.va.bip.reference.partner.person.ws.client;

import javax.annotation.PostConstruct;

import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;

import gov.va.bip.framework.log.BipLogger;
import gov.va.bip.framework.log.BipLoggerFactory;
import gov.va.bip.framework.log.PerformanceLogMethodInterceptor;
import gov.va.bip.framework.validation.Defense;
import gov.va.bip.framework.ws.client.BaseWsClientConfig;

/**
 * This class represents the Spring configuration for the Person Web Service Client.
 */
@Configuration
@ComponentScan(basePackages = {
		"gov.va.bip.framework.ws.client",
		"gov.va.bip.framework.audit",
		"gov.va.bip.reference.partner.person.ws.client" },
		excludeFilters = @Filter(Configuration.class))
public class PersonWsClientConfig extends BaseWsClientConfig {

	/** The logger for this class */
	public static final BipLogger LOGGER = BipLoggerFactory.getLogger(PersonWsClientConfig.class);

	/** The package name for data transfer objects. */
	private static final String TRANSFER_PACKAGE = "gov.va.bip.reference.partner.person.ws.transfer";

	/** The XSD for this web service */
	private static final String XSD = "xsd/PersonService/PersonWebService.xsd";

	// ####### for test, member values are from src/test/resource/application.yml ######

	/** Location of the truststore containing the cert */
	@Value("#{ baseWsClientUtil."
			+ "verifyAddFilePrefix('${bip-reference-partner-person.ws.client.ssl.keystore:classpath:ssl/dev/vaebnweb1Keystore.jks}') }")
	private Resource keystore;

	/** Password for the cert */
	@Value("${bip-reference-partner-person.ws.client.ssl.keystorePass:password}")
	private String keystorePass;
	
	/** Location of the truststore containing the cert */
	@Value("#{ baseWsClientUtil."
			+ "verifyAddFilePrefix('${bip-reference-partner-person.ws.client.ssl.truststore:classpath:ssl/dev/vaebnTruststore.jks}') }")
	private Resource truststore;

	/** Password for the cert */
	@Value("${bip-reference-partner-person.ws.client.ssl.truststorePass:password}")
	private String truststorePass;

	/** Decides if jaxb validation logs errors. */
	@Value("${bip-reference-partner-person.ws.client.logValidation:true}")
	private boolean logValidation;

	/** Username for WS Authentication. */
	@Value("${bip-reference-partner-person.ws.client.username}")
	private String username;

	/** Password for WS Authentication. */
	@Value("${bip-reference-partner-person.ws.client.password}")
	private String password;

	/** VA Application Name Header value. */
	@Value("${bip-reference-partner-person.ws.client.vaApplicationName}")
	private String vaApplicationName;

	/** The VA Station ID header value */
	@Value("${bip-reference-partner-person.ws.client.vaStationId:281}")
	private String vaStationId;

	/**
	 * Executed after dependency injection is done to validate initialization.
	 */
	@PostConstruct
	public final void postConstruct() {
		Defense.notNull(keystore, "Partner keystore cannot be empty.");
		Defense.hasText(keystorePass, "Partner keystorePass cannot be empty.");
		Defense.notNull(truststore, "Partner truststore cannot be empty.");
		Defense.hasText(truststorePass, "Partner truststorePass cannot be empty.");
		Defense.hasText(username, "Partner username cannot be empty.");
		Defense.hasText(password, "Partner password cannot be empty.");
		Defense.hasText(vaApplicationName, "Partner vaApplicationName cannot be empty.");
		Defense.hasText(vaStationId, "Station ID cannot be empty.");
	}

	/**
	 * WS Client object marshaller
	 *
	 * @return object marshaller
	 */
	@Bean
	Jaxb2Marshaller personMarshaller() {
		final Resource[] schemas = new Resource[] { new ClassPathResource(XSD) };
		return super.getMarshaller(TRANSFER_PACKAGE, schemas, logValidation);
	}

	/**
	 * Axiom based WebServiceTemplate for the Web Service Client.
	 *
	 * @param endpoint the endpoint
	 * @param readTimeout the read timeout
	 * @param connectionTimeout the connection timeout
	 * @return the web service template
	 */
	@Bean
	WebServiceTemplate personWsClientAxiomTemplate(
			@Value("${bip-reference-partner-person.ws.client.endpoint}") final String endpoint,
			@Value("${bip-reference-partner-person.ws.client.readTimeout:60000}") final int readTimeout,
			@Value("${bip-reference-partner-person.ws.client.connectionTimeout:60000}") final int connectionTimeout) {

		Defense.hasText(endpoint, "personWsClientAxiomTemplate endpoint cannot be empty.");

		return super.createSslWebServiceTemplate(endpoint, readTimeout, connectionTimeout, personMarshaller(), personMarshaller(),
				new ClientInterceptor[] { personSecurityInterceptor() },
				keystore, keystorePass, truststore, truststorePass);
	}

	/**
	 * Security interceptor to apply WSS security to WS calls.
	 *
	 * @return security interceptor
	 */
	@Bean
	Wss4jSecurityInterceptor personSecurityInterceptor() {
		return super.getVAServiceWss4jSecurityInterceptor(username, password, vaApplicationName, vaStationId);
	}

	/**
	 * Handles performance related logging of the web service client response times.
	 *
	 * @param methodWarningThreshhold the method warning threshold
	 * @return the performance log method interceptor
	 */
	@Bean
	PerformanceLogMethodInterceptor personWsClientPerformanceLogMethodInterceptor(
			@Value("${bip-reference-partner-person.ws.client.methodWarningThreshhold:2500}") final Integer methodWarningThreshhold) {
		return super.getPerformanceLogMethodInterceptor(methodWarningThreshhold);
	}

	@Bean
	BeanNameAutoProxyCreator personWsClientBeanProxy() {
		return super.getBeanNameAutoProxyCreator(new String[] { "personWsClient" },
				new String[] { "personWsClientPerformanceLogMethodInterceptor" });
	}
}
