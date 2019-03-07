package gov.va.ocp.vetservices.claims.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import gov.va.ocp.vetservices.claims.model.ClaimDetailByIdDomainRequest;
import gov.va.ocp.vetservices.claims.model.ClaimDetailByIdDomainResponse;
import gov.va.ocp.vetservices.claims.model.ClaimsDomainResponse;
import gov.va.ocp.vetservices.claims.orm.Claim;
import gov.va.ocp.vetservices.claims.orm.ClaimsRepository;
import gov.va.ocp.vetservices.claims.api.VetServicesClaimsService;

@Service(value = VetServicesClaimsServiceImpl.BEAN_NAME)
@Component
@Qualifier("IMPL")
public class VetServicesClaimsServiceImpl implements VetServicesClaimsService { 
	/** Bean name constant */
	public static final String BEAN_NAME = "vetServicesClaimsServiceImpl";

	@Autowired
	ClaimsRepository claimsRepository;
	
	/**
	 * Returns the claim detail for a given claim id.
	 *
	 * @param request the request
	 * @return the claim detail by id
	 */
    public ClaimDetailByIdDomainResponse getClaimDetailById(ClaimDetailByIdDomainRequest getClaimDetailByIdDomainRequest) {
    	ClaimDetailByIdDomainResponse claimDetailByIdDomainResponse = new ClaimDetailByIdDomainResponse();
    	claimDetailByIdDomainResponse.setClaim(claimsRepository.findById(Long.parseLong(getClaimDetailByIdDomainRequest.getId())));
		return claimDetailByIdDomainResponse;
    }
    
	/**
	 * Returns all claims
	 *
	 * @return the claims
	 */
    public ClaimsDomainResponse getClaims() {
    	ClaimsDomainResponse claimsDomainResponse = new ClaimsDomainResponse();
    	claimsDomainResponse.setClaims(claimsRepository.findAll());
		return claimsDomainResponse;
    }
}
