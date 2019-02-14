package gov.va.os.reference.framework.exception;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import gov.va.os.reference.framework.exception.ReferenceRuntimeException;

public class ReferenceRuntimeExceptionTest {

	@BeforeClass
	public static void setUp() {
		System.setProperty("server.name", "Test Server");
	}

	@Test
	public void instantiateBaseAscentExceptions() throws Exception {
		ReferenceRuntimeException ascentRuntimeException = new ReferenceRuntimeException();

		Assert.assertEquals("Test Server", ascentRuntimeException.getServerName());
	}

	@Test
	public void getMessageTestServerName() throws Exception {
		ReferenceRuntimeException ascentRuntimeException = new ReferenceRuntimeException();

		Assert.assertEquals(null, ascentRuntimeException.getMessage());

	}

	@Test
	public void getMessageTestServerNameNull() throws Exception {
		// setup
		// do crazy reflection to make server name null
		Field field = ReferenceRuntimeException.class.getDeclaredField("SERVER_NAME");
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.isAccessible();
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
		field.isAccessible();
		field.setAccessible(true);
		field.set(null, null);

		ReferenceRuntimeException ascentRuntimeException = new ReferenceRuntimeException();

		Assert.assertNull(ascentRuntimeException.getMessage());

		// Reset server name to Test Server
		field.set(null, "Test Server");
	}

	@Test
	public void getMessageTestCategoryNull() throws Exception {
		ReferenceRuntimeException ascentRuntimeException = new ReferenceRuntimeException();
		Assert.assertEquals(null, ascentRuntimeException.getMessage());

	}

	@Test
	public void getSuperCauseTest() throws Exception {
		Throwable cause = new Throwable("test");
		ReferenceRuntimeException ascentRuntimeException = new ReferenceRuntimeException(cause);
		Assert.assertEquals("java.lang.Throwable: test", ascentRuntimeException.getMessage());

	}

	@Test
	public void getMessageCauseAndMessageTest() throws Exception {
		Throwable cause = new Throwable("test");
		ReferenceRuntimeException ascentRuntimeException = new ReferenceRuntimeException("Test Message", cause);
		Assert.assertEquals("Test Message", ascentRuntimeException.getMessage());

	}
}
