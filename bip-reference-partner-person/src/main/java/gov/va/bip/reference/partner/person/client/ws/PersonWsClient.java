package gov.va.bip.reference.partner.person.client.ws;

import gov.va.bip.reference.partner.person.ws.transfer.FindPersonByPtcpntId;
import gov.va.bip.reference.partner.person.ws.transfer.FindPersonByPtcpntIdResponse;

/**
 * The interface for the PersonWsClient Web Service Client.
 *
 */
@FunctionalInterface
/**
 * Note: This interface is tagged as a @FunctionalInterface to allow for Java lambda integration. This also forces
 * the WsClient to have exactly one method. BLUE Framework does not enforce this pattern on your WsClient interfaces,
 * so if your implementation will have more than one method, you can remove this tag.
 */
public interface PersonWsClient {

	/**
	 * Find the Person by their PID.
	 *
	 * @param findPersonByPtcpntIdRequest The Person Web Service request entity
	 * @return findPersonByPtcpntIdResponse The Person Web Service response entity
	 * @throws Exception
	 */
	FindPersonByPtcpntIdResponse getPersonInfoByPtcpntId(final FindPersonByPtcpntId findPersonByPtcpntIdRequest)
			throws PersonPartnerCheckedException;

}
