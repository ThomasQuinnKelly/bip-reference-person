package gov.va.bip.reference.person.api.provider;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import gov.va.bip.framework.log.BipLogger;
import gov.va.bip.framework.log.BipLoggerFactory;
import gov.va.bip.framework.validation.Defense;
import gov.va.bip.reference.person.ReferencePersonService;
import gov.va.bip.reference.person.api.model.v1.PersonInfoRequest;
import gov.va.bip.reference.person.api.model.v1.PersonInfoResponse;
import gov.va.bip.reference.person.model.PersonByPidDomainRequest;
import gov.va.bip.reference.person.model.PersonByPidDomainResponse;
import gov.va.bip.reference.person.transform.impl.PersonByPid_DomainToProvider;
import gov.va.bip.reference.person.transform.impl.PersonByPid_ProviderToDomain;

/**
 * An adapter between the provider layer api/model, and the services layer interface/model.
 *
 * @author aburkholder
 */
@Component
public class ServiceAdapter {
	/** Class logger */
	private static final BipLogger LOGGER = BipLoggerFactory.getLogger(ServiceAdapter.class);

	/** Transform Provider (REST) request to Domain (service) request */
	private PersonByPid_ProviderToDomain personByPidProvider2Domain = new PersonByPid_ProviderToDomain();
	/** Transform Domain (service) response to Provider (REST) response */
	private PersonByPid_DomainToProvider personByPidDomain2Provider = new PersonByPid_DomainToProvider();

	/** The service layer API contract for processing personByPid() requests */
	@Autowired
	@Qualifier("PERSON_SERVICE_IMPL")
	private ReferencePersonService refPersonService;

	/**
	 * Field defense validations.
	 */
	@PostConstruct
	public void postConstruct() {
		Defense.notNull(refPersonService);
		Defense.notNull(personByPidProvider2Domain);
		Defense.notNull(personByPidDomain2Provider);
	}

	/**
	 * Adapt the personByPid(..) request mapping method to the equivalent service layer method.
	 *
	 * @param personInfoRequest - the Provider layer request model object
	 * @return PersonInfoResponse - the Provider layer response model object
	 */
	PersonInfoResponse personByPid(final PersonInfoRequest personInfoRequest) {
		// transform provider request into domain request
		LOGGER.debug("Transforming from personInfoRequest to domainRequest");
		PersonByPidDomainRequest domainRequest = personByPidProvider2Domain.convert(personInfoRequest);

		// get domain response from the service (domain) layer
		LOGGER.debug("Calling refPersonService.findPersonByParticipantID");
		PersonByPidDomainResponse domainResponse = refPersonService.findPersonByParticipantID(domainRequest);

		// transform domain response into provider response
		LOGGER.debug("Transforming from domainResponse to providerResponse");
		PersonInfoResponse providerResponse = personByPidDomain2Provider.convert(domainResponse);

		return providerResponse;
	}
}
