package gov.va.ocp.reference.person.ws.client;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.va.ocp.framework.log.OcpLogger;
import gov.va.ocp.framework.log.OcpLoggerFactory;
import gov.va.ocp.reference.partner.person.ws.client.PersonWsClientImpl;
import gov.va.ocp.reference.partner.person.ws.transfer.FindPersonByPtcpntId;
import gov.va.ocp.reference.partner.person.ws.transfer.FindPersonByPtcpntIdResponse;
import gov.va.ocp.reference.person.exception.PersonServiceException;
import gov.va.ocp.reference.person.model.PersonByPidDomainRequest;
import gov.va.ocp.reference.person.model.PersonByPidDomainResponse;
import gov.va.ocp.reference.person.transform.impl.PersonByPid_DomainToPartner;
import gov.va.ocp.reference.person.transform.impl.PersonByPid_PartnerToDomain;

/**
 * Make external calls to the partner using the partner client.
 *
 * @author aburkholder
 */
@Component(PersonPartnerHelper.BEAN_NAME)
public class PersonPartnerHelper {
	public static final String BEAN_NAME = "personServiceHelper";

	/** Logger */
	private static final OcpLogger LOGGER = OcpLoggerFactory.getLogger(PersonPartnerHelper.class);

	/** String to prepend messages for re-thrown exceptions */
	private static final String THROWSTR = "Rethrowing the following exception:  ";

	/** WS client to run all intent to file operations via SOAP */
	@Autowired
	private PersonWsClientImpl personWsClient;

	/** Transformer for domain-to-partner model transformation */
	private PersonByPid_DomainToPartner personByPidD2P = new PersonByPid_DomainToPartner();

	/** Transformer for partner-to-domain model transformation */
	private PersonByPid_PartnerToDomain personByPidP2D = new PersonByPid_PartnerToDomain();

	/**
	 * Make the partner call to find person information by participant id.
	 *
	 * @param request the {@link PersonByPidDomainRequest} from the domain
	 * @return PersonByPidDomainResponse domain representation of the partner response
	 */
	public PersonByPidDomainResponse findPersonByPid(PersonByPidDomainRequest request) {

		// transform from domain model request to partner model request
		FindPersonByPtcpntId partnerRequest = personByPidD2P.transform(request);

		FindPersonByPtcpntIdResponse partnerResponse = null;
		// call the partner
		try {
			partnerResponse = personWsClient.getPersonInfoByPtcpntId(partnerRequest);
		} catch (final Exception clientException) {
			String message = THROWSTR + clientException.getClass().getName() + ": " + clientException.getMessage();
			LOGGER.error(message, clientException);
			throw new PersonServiceException(message, clientException);
		}

		// transform from partner model response to domain model response
		PersonByPidDomainResponse domainResponse = personByPidP2D.transform(partnerResponse);

		LOGGER.debug("Partner response: FindPersonByPtcpntIdResponse: {}",
				partnerResponse == null ? "null" : ToStringBuilder.reflectionToString(partnerResponse));
		LOGGER.debug("Domain response: PersonByPidDomainResponse: {}",
				domainResponse == null ? "null" : ToStringBuilder.reflectionToString(domainResponse));

		return domainResponse;
	}
}
