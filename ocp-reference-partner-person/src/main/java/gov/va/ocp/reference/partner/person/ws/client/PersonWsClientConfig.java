package gov.va.ocp.reference.partner.person.ws.client;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;

import gov.va.ocp.reference.framework.exception.InterceptingExceptionTranslator;
import gov.va.ocp.reference.framework.log.PerformanceLogMethodInterceptor;
import gov.va.ocp.reference.framework.log.ReferenceLogger;
import gov.va.ocp.reference.framework.log.ReferenceLoggerFactory;
import gov.va.ocp.reference.framework.util.Defense;
import gov.va.ocp.reference.framework.ws.client.BaseWsClientConfig;
import gov.va.ocp.reference.framework.ws.client.remote.RemoteServiceCallInterceptor;

/**
 * This class represents the Spring configuration for the Person Web Service Client.
 */
@Configuration
@ComponentScan(basePackages = { "gov.va.ocp.reference.partner",
		"gov.va.ocp.reference.framework.ws.client",
		"gov.va.ocp.reference.framework.audit" },
		excludeFilters = @Filter(Configuration.class))
public class PersonWsClientConfig extends BaseWsClientConfig {

	/** The logger for this class */
	public static final ReferenceLogger LOGGER = ReferenceLoggerFactory.getLogger(PersonWsClientConfig.class);

	/** The package name for data transfer objects. */
	private static final String TRANSFER_PACKAGE = "gov.va.ocp.reference.partner.person.ws.transfer";

	/** The XSD for this web service */
	private static final String XSD = "xsd/PersonService/PersonWebService.xsd";

	/** Exception class for exception interceptor */
	private static final String DEFAULT_EXCEPTION_CLASS = PersonWsClientException.class.getName();

	// ####### for test, member values are from src/test/resource/application.yml ######

	/** Location of the truststore containing the cert */
	@Value("${os-reference-partner-person.ws.client.ssl.keystore:src/test/resources/ssl/dev/vaebnweb1Keystore.jks}")
	private String keystore;

	/** Password for the cert */
	@Value("${os-reference-partner-person.ws.client.ssl.keystorePass:password}")
	private String keystorePass;

	/** Location of the truststore containing the cert */
	@Value("${os-reference-partner-person.ws.client.ssl.truststore:src/test/resources/ssl/dev/vaebnTruststore.jks}")
	private String truststore;

	/** Password for the cert */
	@Value("${os-reference-partner-person.ws.client.ssl.truststorePass:password}")
	private String truststorePass;

	/** Decides if jaxb validation logs errors. */
	@Value("${os-reference-partner-person.ws.client.logValidation:true}")
	private boolean logValidation;

	/** Username for WS Authentication. */
	@Value("${os-reference-partner-person.ws.client.username}")
	private String username;

	/** Password for WS Authentication. */
	@Value("${os-reference-partner-person.ws.client.password}")
	private String password;

	/** VA Application Name Header value. */
	@Value("${os-reference-partner-person.ws.client.vaApplicationName}")
	private String vaApplicationName;

	/** The VA Station ID header value */
	@Value("${os-reference-partner-person.ws.client.vaStationId:281}")
	private String vaStationId;

	/**
	 * Executed after dependency injection is done to validate initialization.
	 */
	@PostConstruct
	public final void postConstruct() {
		Defense.hasText(keystore, "Partner keystore cannot be empty.");
		Defense.hasText(keystorePass, "Partner keystorePass cannot be empty.");
		Defense.hasText(truststore, "Partner truststore cannot be empty.");
		Defense.hasText(truststorePass, "Partner truststorePass cannot be empty.");
		Defense.hasText(username, "Partner username cannot be empty.");
		Defense.hasText(password, "Partner password cannot be empty.");
		Defense.hasText(vaApplicationName, "Partner vaApplicationName cannot be empty.");
		Defense.hasText(vaStationId, "Station ID cannot be empty.");

		LOGGER.info("Station ID value : " + vaStationId);
		LOGGER.info("vaApplicationName : " + vaApplicationName);

	}

	/**
	 * WS Client object marshaller
	 *
	 * @return object marshaller
	 */
	@Bean
	Jaxb2Marshaller personMarshaller() {
		final Resource[] schemas = new Resource[] { new ClassPathResource(XSD) };
		return getMarshaller(TRANSFER_PACKAGE, schemas, logValidation);
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
			@Value("${os-reference-partner-person.ws.client.endpoint}") final String endpoint,
			@Value("${os-reference-partner-person.ws.client.readTimeout:60000}") final int readTimeout,
			@Value("${os-reference-partner-person.ws.client.connectionTimeout:60000}") final int connectionTimeout) {

		Defense.hasText(endpoint, "personWsClientAxiomTemplate endpoint cannot be empty.");

		return createSslWebServiceTemplate(endpoint, readTimeout, connectionTimeout, personMarshaller(), personMarshaller(),
				new ClientInterceptor[] { personSecurityInterceptor() },
				new FileSystemResource(keystore), keystorePass, new FileSystemResource(truststore), truststorePass);
	}

	/**
	 * Security interceptor to apply WSS security to WS calls.
	 *
	 * @return security interceptor
	 */
	@Bean
	Wss4jSecurityInterceptor personSecurityInterceptor() {
		LOGGER.debug("Station ID value : " + vaStationId);
		LOGGER.debug("vaApplicationName : " + vaApplicationName);
		return getVAServiceWss4jSecurityInterceptor(username, password, vaApplicationName, vaStationId);
	}

	/**
	 * PerformanceLogMethodInterceptor for the Web Service Client
	 * <p>
	 * Handles performance related logging of the web service client response times.
	 *
	 * @param methodWarningThreshhold the method warning threshold
	 * @return the performance log method interceptor
	 */
	@Bean
	PerformanceLogMethodInterceptor personWsClientPerformanceLogMethodInterceptor(
			@Value("${os-reference-partner-person.ws.client.methodWarningThreshhold:2500}") final Integer methodWarningThreshhold) {
		return getPerformanceLogMethodInterceptor(methodWarningThreshhold);
	}

	/**
	 * InterceptingExceptionTranslator for the Web Service Client
	 * <p>
	 * Handles runtime exceptions raised by the web service client through runtime operation and communication with the remote service.
	 *
	 * @return the intercepting exception translator
	 * @throws ClassNotFoundException the class not found exception
	 */
	@Bean
	InterceptingExceptionTranslator personWsClientExceptionInterceptor() throws ClassNotFoundException {
		return getInterceptingExceptionTranslator(DEFAULT_EXCEPTION_CLASS, PACKAGE_REFERENCE_FRAMEWORK_EXCEPTION);
	}

	/**
	 * RemoteServiceCallInterceptor for the Web Service Client
	 *
	 * Handles runtime exceptions raised by the web service client through runtime
	 * operation and communication with the remote service.
	 *
	 * @return the RemoteServiceCallInterceptor
	 * @throws ClassNotFoundException the class not found exception
	 */
	@Bean
	RemoteServiceCallInterceptor personWsClientRemoteServiceCallInterceptor() {
		return getRemoteServiceCallInterceptor();
	}
}
