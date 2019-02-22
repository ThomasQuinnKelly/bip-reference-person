package gov.va.os.reference.partner.person.ws.client.remote;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Test;
import org.springframework.security.core.context.SecurityContextHolder;

import gov.va.os.reference.partner.person.ws.client.AbstractPersonTest;
import gov.va.os.reference.partner.person.ws.transfer.FindPersonByPtcpntId;

public class RemoteServiceCallMockTest extends AbstractPersonTest {

	@After
	public void tearDown() {
		SecurityContextHolder.clearContext();
	}

	@Test
	public void testGetKeyForMockResponseListAll() {
		PersonRemoteServiceCallMock mock = new PersonRemoteServiceCallMock();
		FindPersonByPtcpntId request = super.makeFindPersonByPtcpntIdRequest();
		String keyForMockResponse = mock.getKeyForMockResponse(request);

		assertNotNull(keyForMockResponse);
		assertTrue(keyForMockResponse.equals(PersonRemoteServiceCallMock.MOCK_FINDPERSONBYPTCPNTID_RESPONSE));
	}

	@Test
	public void testGetKeyForMockResponse_NullRequest() {
		PersonRemoteServiceCallMock mock = new PersonRemoteServiceCallMock();
		FindPersonByPtcpntId request = null;

		String keyForMockResponse = null;

		try {
			keyForMockResponse = mock.getKeyForMockResponse(request);
		} catch (Throwable e) {
			assertTrue("Invalid exception was thrown.", IllegalArgumentException.class.equals(e.getClass()));
			assertTrue("Exception message contains wrong string.",
					e.getMessage().equals(PersonRemoteServiceCallMock.ERROR_NULL_REQUEST));
		}

		assertNull("Null request should have thrown exception.", keyForMockResponse);
	}

}
