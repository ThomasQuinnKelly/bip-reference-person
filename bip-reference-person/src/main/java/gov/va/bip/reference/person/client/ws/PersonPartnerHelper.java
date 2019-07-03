package gov.va.bip.reference.person.client.ws;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.va.bip.framework.exception.BipException;
import gov.va.bip.framework.exception.BipRuntimeException;
import gov.va.bip.framework.log.BipLogger;
import gov.va.bip.framework.log.BipLoggerFactory;
import gov.va.bip.reference.person.exception.PersonServiceException;
import gov.va.bip.reference.person.model.PersonByPidDomainRequest;
import gov.va.bip.reference.person.model.PersonByPidDomainResponse;
import gov.va.bip.reference.person.transform.impl.PersonByPid_DomainToPartner;
import gov.va.bip.reference.person.transform.impl.PersonByPid_PartnerToDomain;
import gov.va.bip.reference.partner.person.client.ws.PersonWsClient;
import gov.va.bip.reference.partner.person.ws.transfer.FindPersonByPtcpntId;
import gov.va.bip.reference.partner.person.ws.transfer.FindPersonByPtcpntIdResponse;

/**
 * Make external calls to the partner using the partner client.
 * <p>
 * This Helper isolates references to partner clients. There should not be
 * references to partner client classes outside of this class.
 *
 * @author aburkholder
 */
@Component(PersonPartnerHelper.BEAN_NAME)
public class PersonPartnerHelper {
	public static final String BEAN_NAME = "personServiceHelper";

	/** Logger */
	private static final BipLogger LOGGER = BipLoggerFactory.getLogger(PersonPartnerHelper.class);

	/** String to prepend messages for re-thrown exceptions */
	private static final String THROWSTR = "Rethrowing the following exception:  ";

	/** WS client to run all intent to file operations via SOAP */
	@Autowired
	private PersonWsClient personWsClient;

	/** Transformer for domain-to-partner model transformation */
	private PersonByPid_DomainToPartner personByPidD2P = new PersonByPid_DomainToPartner();

	/** Transformer for partner-to-domain model transformation */
	private PersonByPid_PartnerToDomain personByPidP2D = new PersonByPid_PartnerToDomain();

	/**
	 * Make the partner call to find person information by participant id.
	 *
	 * @param request the {@link PersonByPidDomainRequest} from the domain
	 * @return PersonByPidDomainResponse domain representation of the partner response
	 * @throws BipException
	 */
	public PersonByPidDomainResponse findPersonByPid(final PersonByPidDomainRequest request) throws BipException {

		// transform from domain model request to partner model request
		FindPersonByPtcpntId partnerRequest = personByPidD2P.convert(request);

		FindPersonByPtcpntIdResponse partnerResponse = null;
		PersonByPidDomainResponse domainResponse = null;
		// call the partner
		try {
			partnerResponse = personWsClient.getPersonInfoByPtcpntId(partnerRequest);

			// transform from partner model response to domain model response
			domainResponse = personByPidP2D.convert(partnerResponse);

		} catch (final BipException bipException) {
			/*
			 * For this service, no useful work could be done without a successful call
			 * to the partner web service.
			 * So in this case, we throw a RuntimeException to abort execution and
			 * handle from BipRestGlobalExceptionHandler
			 */
			// checked exception to be handled separately
			String message = THROWSTR + bipException.getClass().getName() + ": " + bipException.getMessage();
			LOGGER.error(message, bipException);
			throw bipException;
		} catch (final BipRuntimeException clientException) {
			// any other exception can be caught and thrown as PersonServiceException for the circuit not to be opened
			String message = THROWSTR + clientException.getClass().getName() + ": " + clientException.getMessage();
			LOGGER.error(message, clientException);
			throw new PersonServiceException(clientException.getExceptionData().getMessageKey(),
					clientException.getExceptionData().getSeverity(), clientException.getExceptionData().getStatus(),
					clientException.getExceptionData().getParams());
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
