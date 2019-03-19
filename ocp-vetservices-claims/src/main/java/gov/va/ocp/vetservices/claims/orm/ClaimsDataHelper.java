package gov.va.ocp.vetservices.claims.orm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.va.ocp.vetservices.claims.model.ClaimDetailByIdDomainRequest;
import gov.va.ocp.vetservices.claims.model.ClaimDetailByIdDomainResponse;
import gov.va.ocp.vetservices.claims.model.ClaimsDomainResponse;

@Component
public class ClaimsDataHelper {
	
	@Autowired
	ClaimsRepository claimsRepository;
	
	public ClaimDetailByIdDomainResponse getClaimDetailById(ClaimDetailByIdDomainRequest claimDetailByIdDomainRequest) {
		ClaimDetailByIdDomainResponse claimDetailByIdDomainResponse = new ClaimDetailByIdDomainResponse();
		claimDetailByIdDomainResponse
				.setClaim(claimsRepository.findById(Long.parseLong(claimDetailByIdDomainRequest.getId())).get());
		return claimDetailByIdDomainResponse;
	}
	
	public ClaimsDomainResponse getClaims() {
		ClaimsDomainResponse claimsDomainResponse = new ClaimsDomainResponse();
		claimsDomainResponse.setClaims(claimsRepository.findAll());
		return claimsDomainResponse;
	}
}
