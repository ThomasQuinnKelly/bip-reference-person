package gov.va.ocp.reference.framework.exception;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class ReferenceRuntimeExceptionTest {

	private static final String SERVER_NAME_PROPERTY = "server.name";

	@Before
	public void setUp() {
		System.setProperty(SERVER_NAME_PROPERTY, "Test Server");
	}

	@BeforeClass
	public static void setUpClass() {
		System.setProperty(SERVER_NAME_PROPERTY, "Test Server");
	}

	// TODO for some reason, System.getProperty(SERVER_NAME_PROPERTY)
	// in ReferenceRuntimeException comes back as null.
	// Not sure why it does that now, as in Ascent it always came back as "Test Server"
	@Ignore
	@Test
	public void instantiateBaseReferenceExceptions() throws Exception {
		ReferenceRuntimeException referenceRuntimeException = new ReferenceRuntimeException();

		Assert.assertEquals("Test Server", referenceRuntimeException.getServerName());
	}

	@Test
	public void getMessageTestServerName() throws Exception {
		ReferenceRuntimeException referenceRuntimeException = new ReferenceRuntimeException();

		Assert.assertEquals(null, referenceRuntimeException.getMessage());

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

		ReferenceRuntimeException referenceRuntimeException = new ReferenceRuntimeException();

		Assert.assertNull(referenceRuntimeException.getMessage());

		// Reset server name to Test Server
		field.set(null, "Test Server");
	}

	@Test
	public void getMessageTestCategoryNull() throws Exception {
		ReferenceRuntimeException referenceRuntimeException = new ReferenceRuntimeException();
		Assert.assertEquals(null, referenceRuntimeException.getMessage());

	}

	@Test
	public void getSuperCauseTest() throws Exception {
		Throwable cause = new Throwable("test");
		ReferenceRuntimeException referenceRuntimeException = new ReferenceRuntimeException(cause);
		Assert.assertEquals("java.lang.Throwable: test", referenceRuntimeException.getMessage());

	}

	@Test
	public void getMessageCauseAndMessageTest() throws Exception {
		Throwable cause = new Throwable("test");
		ReferenceRuntimeException referenceRuntimeException = new ReferenceRuntimeException("Test Message", cause);
		Assert.assertEquals("Test Message", referenceRuntimeException.getMessage());

	}
}
