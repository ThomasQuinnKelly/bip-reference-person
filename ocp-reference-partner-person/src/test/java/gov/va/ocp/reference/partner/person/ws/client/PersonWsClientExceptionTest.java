package gov.va.ocp.reference.partner.person.ws.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import gov.va.ocp.reference.partner.person.ws.client.PersonWsClientException;

public class PersonWsClientExceptionTest {

	private PersonWsClientException testException;

	private static final String TEST_MESSAGE = "This is a test error message";
	private static NullPointerException TEST_CAUSE = new NullPointerException();

	@Test
	public void testPersonWsClientException() {
		testException = new PersonWsClientException();
		assertNotNull(testException);
	}

	@Test
	public void testPersonWsClientExceptionStringThrowable() {
		testException = new PersonWsClientException(TEST_MESSAGE, TEST_CAUSE);
		assertEquals(TEST_MESSAGE, testException.getMessage());
		assertEquals(TEST_CAUSE, testException.getCause());
	}

	@Test
	public void testPersonWsClientExceptionString() {
		testException = new PersonWsClientException(TEST_MESSAGE);
		assertEquals(TEST_MESSAGE, testException.getMessage());
	}

	@Test
	public void testPersonWsClientExceptionThrowable() {
		testException = new PersonWsClientException(TEST_CAUSE);
		assertEquals(TEST_CAUSE, testException.getCause());
	}

}
