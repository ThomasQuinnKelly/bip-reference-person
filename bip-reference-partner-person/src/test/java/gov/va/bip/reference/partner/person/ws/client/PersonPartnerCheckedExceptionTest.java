package gov.va.bip.reference.partner.person.ws.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import gov.va.bip.framework.messages.MessageKeys;
import gov.va.bip.reference.partner.person.ws.client.PersonPartnerCheckedException;

@RunWith(JUnit4.class)
public class PersonPartnerCheckedExceptionTest {

	private PersonPartnerCheckedException testException;

	private static final String TEST_MESSAGE = "NO_KEY";
	private static NullPointerException TEST_CAUSE = new NullPointerException();

	@Test
	public void testPersonWsClientException() {
		testException = new PersonPartnerCheckedException(null, null, null, TEST_CAUSE);
		assertNotNull(testException);
	}

	@Test
	public void testPersonWsClientExceptionStringThrowable() {
		testException = new PersonPartnerCheckedException(MessageKeys.NO_KEY, null, null, TEST_CAUSE);
		assertNotNull(testException);
		assertEquals(MessageKeys.NO_KEY.getMessage(), testException.getMessage());
		assertEquals(TEST_CAUSE, testException.getCause());
	}

	@Test
	public void testPersonWsClientExceptionString() {
		testException = new PersonPartnerCheckedException(MessageKeys.NO_KEY, null, null, null);
		assertNotNull(testException);
		assertEquals(TEST_MESSAGE, testException.getMessage());
	}

	@Test
	public void testPersonWsClientExceptionThrowable() {
		testException = new PersonPartnerCheckedException(null, null, null, TEST_CAUSE);
		assertNotNull(testException);
		assertEquals(TEST_CAUSE, testException.getCause());
	}

}
