package gov.va.ocp.reference.partner.person.ws.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class PersonPartnerCheckedExceptionTest {

	private PersonPartnerCheckedException testException;

	private static final String TEST_MESSAGE = "This is a test error message";
	private static NullPointerException TEST_CAUSE = new NullPointerException();

	@Test
	public void testPersonWsClientException() {
		testException = new PersonPartnerCheckedException(null, null, null, null);
		assertNotNull(testException);
	}

	@Test
	public void testPersonWsClientExceptionStringThrowable() {
		testException = new PersonPartnerCheckedException(null, TEST_MESSAGE, null, null, TEST_CAUSE);
		assertNotNull(testException);
		assertEquals(TEST_MESSAGE, testException.getMessage());
		assertEquals(TEST_CAUSE, testException.getCause());
	}

	@Test
	public void testPersonWsClientExceptionString() {
		testException = new PersonPartnerCheckedException(null, TEST_MESSAGE, null, null);
		assertNotNull(testException);
		assertEquals(TEST_MESSAGE, testException.getMessage());
	}

	@Test
	public void testPersonWsClientExceptionThrowable() {
		testException = new PersonPartnerCheckedException(null, null, null, null, TEST_CAUSE);
		assertNotNull(testException);
		assertEquals(TEST_CAUSE, testException.getCause());
	}

}
