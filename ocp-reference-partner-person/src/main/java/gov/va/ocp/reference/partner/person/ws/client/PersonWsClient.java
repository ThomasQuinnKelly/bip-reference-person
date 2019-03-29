package gov.va.ocp.reference.partner.person.ws.client;

import gov.va.ocp.reference.partner.person.ws.transfer.FindPersonByPtcpntId;
import gov.va.ocp.reference.partner.person.ws.transfer.FindPersonByPtcpntIdResponse;

/**
 * The interface for the PersonWsClient Web Service Client.
 *
 */
@FunctionalInterface
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
