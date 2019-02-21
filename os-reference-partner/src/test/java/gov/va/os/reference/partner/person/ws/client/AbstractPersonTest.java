package gov.va.os.reference.partner.person.ws.client;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import gov.va.os.reference.partner.person.ws.transfer.FindPersonByPtcpntId;
import gov.va.os.reference.partner.person.ws.transfer.ObjectFactory;

public class AbstractPersonTest {

	/**
	 * Make a request object
	 *
	 * @return FindPersonByPtcpntId
	 */
	protected FindPersonByPtcpntId makeFindPersonByPtcpntIdRequest() {
		ObjectFactory of = new ObjectFactory();
		final FindPersonByPtcpntId request = of.createFindPersonByPtcpntId();
		return request;
	}

	@Test
	public void testInterface() {
		assertTrue(PersonWsClient.class.isInterface());
	}

}
