package gov.va.bip.reference.person.client.rest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.netflix.hystrix.exception.HystrixRuntimeException.FailureType;
import com.netflix.hystrix.exception.HystrixTimeoutException;

import gov.va.bip.framework.exception.BipFeignRuntimeException;
import gov.va.bip.framework.messages.MessageKeys;
import gov.va.bip.framework.messages.MessageSeverity;
import gov.va.bip.framework.rest.provider.Message;
import gov.va.bip.reference.person.api.model.v1.PersonInfoRequest;
import gov.va.bip.reference.person.api.model.v1.PersonInfoResponse;

/**
 * Unit Test for FeignPersonClientFallbackFactory class
 * @author jluck
 *
 */
public class FeignPersonClientFallbackFactoryTest {
	
	private FeignPersonClientFallbackFactory factory = new FeignPersonClientFallbackFactory();
	

	/**
	 * Test that the create method returns a FeignPersonClient implementation that reports back that the service is unavailable
	 * at this time.
	 */
	@Test
	public void create_GenericException() {
		final PersonInfoRequest request = new PersonInfoRequest();
		
		FeignPersonClient result = factory.create(new Exception("Unit Testing"));
		final PersonInfoResponse response = result.personByPid(request);
		
		Assert.assertNull(response.getPersonInfo());
		Assert.assertFalse(response.getMessages().isEmpty());
		final Message msg = response.getMessages().get(0);
		Assert.assertEquals(Integer.toString(HttpStatus.SERVICE_UNAVAILABLE.value()), msg.getStatus());
		Assert.assertEquals(HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase(), msg.getKey());
		Assert.assertEquals(MessageSeverity.FATAL.name(), msg.getSeverity());
		Assert.assertEquals("Unit Testing", msg.getText());
	}
	
	/**
	 * Test that the create method returns a FeignPersonClient implementation that reports back that the service is unavailable
	 * at this time.
	 */
	@Test
	public void create_HystrixRuntimeException() {
		final PersonInfoRequest request = new PersonInfoRequest();
		
		FeignPersonClient result = factory.create(new HystrixRuntimeException(FailureType.TIMEOUT, null, "Unit Testing", null, null));
		final PersonInfoResponse response = result.personByPid(request);
		
		Assert.assertNull(response.getPersonInfo());
		Assert.assertFalse(response.getMessages().isEmpty());
		final Message msg = response.getMessages().get(0);
		Assert.assertEquals(Integer.toString(HttpStatus.SERVICE_UNAVAILABLE.value()), msg.getStatus());
		Assert.assertEquals(HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase(), msg.getKey());
		Assert.assertEquals(MessageSeverity.FATAL.name(), msg.getSeverity());
		Assert.assertEquals("HystrixRuntimeException: Unit Testing", msg.getText());
	}
	
	/**
	 * Test that the create method returns a FeignPersonClient implementation that reports back that the service is unavailable
	 * at this time.
	 */
	@Test
	public void create_HystrixTimeoutException() {
		final PersonInfoRequest request = new PersonInfoRequest();
		
		FeignPersonClient result = factory.create(new HystrixTimeoutException());
		final PersonInfoResponse response = result.personByPid(request);
		
		Assert.assertNull(response.getPersonInfo());
		Assert.assertFalse(response.getMessages().isEmpty());
		final Message msg = response.getMessages().get(0);
		Assert.assertEquals(Integer.toString(HttpStatus.SERVICE_UNAVAILABLE.value()), msg.getStatus());
		Assert.assertEquals(HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase(), msg.getKey());
		Assert.assertEquals(MessageSeverity.FATAL.name(), msg.getSeverity());
		Assert.assertEquals("HystrixTimeoutException: null", msg.getText());
	}
	
	/**
	 * Test that the create method returns a FeignPersonClient implementation that reports back that the service is unavailable
	 * at this time.
	 */
	@Test
	public void create_BipFeignRuntimeException() {
		final PersonInfoRequest request = new PersonInfoRequest();
		
		FeignPersonClient result = factory.create(new BipFeignRuntimeException(MessageKeys.BIP_SECURITY_TOKEN_INVALID, MessageSeverity.ERROR, HttpStatus.FORBIDDEN, "Unit Testing"));
		final PersonInfoResponse response = result.personByPid(request);
		
		Assert.assertNull(response.getPersonInfo());
		Assert.assertFalse(response.getMessages().isEmpty());
		final Message msg = response.getMessages().get(0);
		Assert.assertEquals(Integer.toString(HttpStatus.FORBIDDEN.value()), msg.getStatus());
		Assert.assertEquals(MessageKeys.BIP_SECURITY_TOKEN_INVALID.getKey(), msg.getKey());
		Assert.assertEquals(MessageSeverity.ERROR.name(), msg.getSeverity());
		Assert.assertEquals(MessageKeys.BIP_SECURITY_TOKEN_INVALID.getMessage("Unit Testing"), msg.getText());
	}
}
