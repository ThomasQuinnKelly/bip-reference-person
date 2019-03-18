package gov.va.ocp.reference.partner.person.ws.client;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;

import gov.va.ocp.framework.config.OcpCommonSpringProfiles;
import gov.va.ocp.framework.exception.interceptor.InterceptingExceptionTranslator;
import gov.va.ocp.framework.log.PerformanceLogMethodInterceptor;
import gov.va.ocp.framework.ws.client.remote.AuditAroundRemoteServiceCallInterceptor;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(inheritListeners = false, listeners = { DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
@ActiveProfiles({ OcpCommonSpringProfiles.PROFILE_REMOTE_CLIENT_SIMULATORS })
@ContextConfiguration(inheritLocations = false, classes = { PartnerMockFrameworkTestConfig.class, PersonWsClientConfig.class })
public class PersonWsClientConfigTest {

	@Autowired
	@Qualifier("personMarshaller")
	Jaxb2Marshaller mashaller;

	@Autowired
	@Qualifier("personWsClientAxiomTemplate")
	WebServiceTemplate personWsClientAxiomTemplate;

	@Autowired
	@Qualifier("personSecurityInterceptor")
	Wss4jSecurityInterceptor personSecurityInterceptor;

	@Autowired
	@Qualifier("personWsClientPerformanceLogMethodInterceptor")
	PerformanceLogMethodInterceptor personWsClientPerformanceLogMethodInterceptor;

	@Autowired
	@Qualifier("personWsClientExceptionInterceptor")
	InterceptingExceptionTranslator personWsClientExceptionInterceptor;

	@Autowired
	@Qualifier("personWsClientRemoteServiceCallInterceptor")
	AuditAroundRemoteServiceCallInterceptor personWsClientRemoteServiceCallInterceptor;

	@Test
	public void personMarshallerTest() {
		assertNotNull(mashaller);
	}

	@Test
	public void testPersonWsClientAxiomTemplateTest() {
		assertNotNull(personWsClientAxiomTemplate);
	}

	@Test
	public void personSecurityInterceptorTest() {
		assertNotNull(personSecurityInterceptor);
	}

	@Test
	public void personWsClientPerformanceLogMethodInterceptorTest() {
		assertNotNull(personWsClientPerformanceLogMethodInterceptor);
	}

	@Test
	public void personWsClientExceptionInterceptorTest() {
		assertNotNull(personWsClientExceptionInterceptor);
	}

	@Test
	public void personWsClientRemoteServiceCallInterceptorTest() {
		assertNotNull(personWsClientRemoteServiceCallInterceptor);
	}
}
