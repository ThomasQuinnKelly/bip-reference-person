package gov.va.ocp.reference.person.ws.client;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.va.ocp.framework.exception.OcpException;
import gov.va.ocp.framework.exception.OcpRuntimeException;
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
	 * @throws OcpException 
	 */
	public PersonByPidDomainResponse findPersonByPid(PersonByPidDomainRequest request) throws OcpException {

		// transform from domain model request to partner model request
		FindPersonByPtcpntId partnerRequest = personByPidD2P.convert(request);

		FindPersonByPtcpntIdResponse partnerResponse = null;
		PersonByPidDomainResponse domainResponse = null;
		// call the partner
		try {
			partnerResponse = personWsClient.getPersonInfoByPtcpntId(partnerRequest);

			// transform from partner model response to domain model response
			domainResponse = personByPidP2D.convert(partnerResponse);

		} catch (final OcpException ocpException) {
			// checked exception to be handled separately
			String message = THROWSTR + ocpException.getClass().getName() + ": " + ocpException.getMessage();
			LOGGER.error(message, ocpException);
			throw ocpException;
		} catch (final OcpRuntimeException clientException) {
			// any other exception can be caught and thrown as PersonServiceException for the circuit not to be opened
			String message = THROWSTR + clientException.getClass().getName() + ": " + clientException.getMessage();
			LOGGER.error(message, clientException);
			throw new PersonServiceException(clientException.getKey(), clientException.getMessage(), 
					clientException.getSeverity(), clientException.getStatus());
		} catch (final RuntimeException runtimeException) {
			// RuntimeException can't be ignored as it's a candidate for circuit to be opened in Hystrix
			String message = THROWSTR + runtimeException.getClass().getName() + ": " + runtimeException.getMessage();
			LOGGER.error(message, runtimeException);
			throw runtimeException;
		}

		LOGGER.debug("Partner response: FindPersonByPtcpntIdResponse: {}",
				partnerResponse == null ? "null" : ToStringBuilder.reflectionToString(partnerResponse));
		LOGGER.debug("Domain response: PersonByPidDomainResponse: {}",
				domainResponse == null ? "null" : ToStringBuilder.reflectionToString(domainResponse));

		return domainResponse;
	}
}
