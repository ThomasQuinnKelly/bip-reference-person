package gov.va.ocp.vetservices.claims.api.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.va.ocp.vetservices.claims.api.VetServicesClaimsService;
import gov.va.ocp.vetservices.claims.model.ClaimDetailByIdDomainRequest;
import gov.va.ocp.vetservices.claims.model.ClaimDetailByIdDomainResponse;
import gov.va.ocp.vetservices.claims.model.ClaimsDomainResponse;

@RestController
public class ClaimsResource {

	/** The root path to this resource */
	public static final String URL_PREFIX = "/api/v1";

	/** The service layer API contract for processing claims requests */
	@Autowired
	@Qualifier("IMPL")
	VetServicesClaimsService vetServicesClaimsService;

	/**
	 * Returns the claim detail for a given claim id.
	 * @param getClaimDetailByIdDomainRequest
	 * @return ClaimDetailByIdDomainResponse
	 */
	@RequestMapping(value = URL_PREFIX
			+ "/claims/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ClaimDetailByIdDomainResponse getClaimDetailById(@PathVariable("id") String id) {
		ClaimDetailByIdDomainRequest claimDetailByIdDomainRequest = new ClaimDetailByIdDomainRequest();
		claimDetailByIdDomainRequest.setId(id);
		ClaimDetailByIdDomainResponse claimDetailByIdDomainResponse = vetServicesClaimsService
				.getClaimDetailById(claimDetailByIdDomainRequest);
		return claimDetailByIdDomainResponse;
	}

	/** 
	 * Returns all claims
	 * @return ClaimsDomainResponse
	 */
	@RequestMapping(value = URL_PREFIX + "/claims", method = RequestMethod.GET)
	public ClaimsDomainResponse getAllclaims() {
		return vetServicesClaimsService.getClaims();
	}
}
