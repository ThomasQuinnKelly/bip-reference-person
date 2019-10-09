package gov.va.bip.reference.person.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import gov.va.bip.framework.messages.MessageKeys;
import gov.va.bip.framework.messages.MessageSeverity;

/**
 *
 * @author rthota
 */
public class PersonServiceExceptionTest {
	PersonServiceException instance;
	PersonServiceException instanceWithCause;

	private static final String NAME = "NO_KEY";
	private static final String MESSAGE = "NO_KEY";

	private static final String EXP_MESSAGE = "This is a big problem.";

	@Before
	public void setUp() {
		instance = new PersonServiceException(MessageKeys.NO_KEY, MessageSeverity.ERROR, null);
		instanceWithCause = new PersonServiceException(MessageKeys.NO_KEY, MessageSeverity.ERROR, null, new Exception(EXP_MESSAGE));
	}

	/**
	 * Test of getSeverity method, of class PersonServiceException.
	 */
	@Test
	public void testGetSeverity() {
		System.out.println("getSeverity");
		MessageSeverity expResult = MessageSeverity.ERROR;
		MessageSeverity result = instance.getExceptionData().getSeverity();
		assertEquals(expResult, result);
	}

	/**
	 * Test of getKey method, of class PersonServiceException.
	 */
	@Test
	public void testGetKey() {
		System.out.println("getKey");
		String expResult = NAME;
		String result = instance.getExceptionData().getKey();
		assertEquals(expResult, result);
	}

	/**
	 * Test of getMessage method, of class PersonServiceException.
	 */
	@Test
	public void testGetMessage() {
		System.out.println("getMessage");
		String expResult = MESSAGE;
		String result = instance.getMessage();
		assertEquals(expResult, result);

	}

	/**
	 * Test of PersonService Exception with a throwable
	 */
	@Test
	public void testGetSurpressed() {
		System.out.println("getCause");
		String expResult = EXP_MESSAGE;
		Throwable result = instanceWithCause.getCause();
		assertEquals(expResult, result.getMessage());

	}
}
