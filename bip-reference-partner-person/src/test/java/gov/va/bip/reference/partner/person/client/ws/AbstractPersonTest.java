package gov.va.bip.reference.partner.person.client.ws;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import gov.va.bip.reference.partner.person.client.ws.PersonWsClient;
import gov.va.bip.reference.partner.person.ws.transfer.FindPersonByPtcpntId;
import gov.va.bip.reference.partner.person.ws.transfer.ObjectFactory;

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
