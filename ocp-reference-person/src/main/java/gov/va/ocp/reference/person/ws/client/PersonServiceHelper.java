package gov.va.ocp.reference.person.ws.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.va.ocp.reference.framework.log.ReferenceLogger;
import gov.va.ocp.reference.framework.log.ReferenceLoggerFactory;
import gov.va.ocp.reference.partner.person.ws.client.PersonWsClientImpl;
import gov.va.ocp.reference.partner.person.ws.transfer.FindPersonByPtcpntId;
import gov.va.ocp.reference.partner.person.ws.transfer.FindPersonByPtcpntIdResponse;
import gov.va.ocp.reference.person.exception.PersonServiceException;
import gov.va.ocp.reference.person.model.person.v1.PersonInfoRequest;
import gov.va.ocp.reference.person.model.person.v1.PersonInfoResponse;
import gov.va.ocp.reference.person.transform.impl.PersonByPid_DomainToPartner;
import gov.va.ocp.reference.person.transform.impl.PersonByPid_PartnerToDomain;
import gov.va.ocp.reference.person.ws.client.validate.PersonDomainValidator;

/**
 * Make external calls to the partner using the partner client.
 *
 * @author aburkholder
 */
@Component(PersonServiceHelper.BEAN_NAME)
public class PersonServiceHelper {
	public static final String BEAN_NAME = "personServiceHelper";
	/** Logger */
	private static final ReferenceLogger LOGGER = ReferenceLoggerFactory.getLogger(PersonServiceHelper.class);

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
	 * @param request the {@link PersonInfoRequest} from the domain
	 * @return PersonInfoResponse domain representation of the partner response
	 */
	public PersonInfoResponse findPersonByPid(PersonInfoRequest request) {
		// If validation fails, throws IllegalArgumentException back to ServiceExceptionHandlerAspect
		PersonDomainValidator.validatePersonInfoRequest(request);

		FindPersonByPtcpntId partnerRequest = personByPidD2P.transform(request);

		FindPersonByPtcpntIdResponse partnerResponse = null;
		try {
			partnerResponse = personWsClient.getPersonInfoByPtcpntId(partnerRequest);
		} catch (final Exception clientException) {
			String message = THROWSTR + clientException.getClass().getName() + ": " + clientException.getMessage();
			LOGGER.error(message, clientException);
			throw new PersonServiceException(message, clientException);
		}

		return partnerResponse == null ? null : personByPidP2D.transform(partnerResponse);
	}

}
