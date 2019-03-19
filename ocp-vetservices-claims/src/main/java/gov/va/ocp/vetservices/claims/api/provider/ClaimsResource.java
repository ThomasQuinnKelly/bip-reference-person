package gov.va.ocp.vetservices.claims.api.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.va.ocp.framework.swagger.SwaggerResponseMessages;
import gov.va.ocp.framework.transfer.ProviderTransferObjectMarker;
import gov.va.ocp.framework.util.Defense;
import gov.va.ocp.vetservices.claims.VetServicesClaimsService;
import gov.va.ocp.vetservices.claims.api.VetservicesClaimsApi;
import gov.va.ocp.vetservices.claims.api.model.v1.ClaimDetailRequest;
import gov.va.ocp.vetservices.claims.api.model.v1.ClaimDetailResponse;
import gov.va.ocp.vetservices.claims.api.model.v1.ClaimsResponse;
import gov.va.ocp.vetservices.claims.model.AllClaimsDomainRequest;
import gov.va.ocp.vetservices.claims.model.ClaimDetailByIdDomainRequest;
import gov.va.ocp.vetservices.claims.model.ClaimDetailByIdDomainResponse;
import gov.va.ocp.vetservices.claims.model.ClaimsDomainResponse;
import gov.va.ocp.vetservices.claims.transform.impl.ClaimsDomainToProviderConverter;
import gov.va.ocp.vetservices.claims.transform.impl.ClaimsProviderToDomainConverter;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class ClaimsResource implements VetservicesClaimsApi, HealthIndicator, SwaggerResponseMessages {

	/** The root path to this resource */
	public static final String URL_PREFIX = "/api/v1";

	/** The service layer API contract for processing claims requests */
	@Autowired
	@Qualifier("IMPL")
	VetServicesClaimsService vetServicesClaimsService;
	
	@Autowired
	ClaimsProviderToDomainConverter claimsProviderToDomainConverter;

	@Autowired
	ClaimsDomainToProviderConverter claimsDomainToProviderConverter;
	/**
	 * Returns the claim detail for a given claim id.
	 * @param getClaimDetailByIdDomainRequest
	 * @return ClaimDetailByIdDomainResponse
	 */
	@RequestMapping(value = URL_PREFIX
			+ "/claims/{claimId}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	@ApiOperation(value = "Retrieve Claim Detail information by id from Claims Service.",
	notes = "Will return a Claim object based on search by id.")
	public ClaimDetailResponse getClaimDetailById(@PathVariable("claimId") String claimId) {
		Defense.notNull(claimId, "Invalid request, id cannot be null.");
		
		ClaimDetailRequest claimDetailRequest = new ClaimDetailRequest();
		claimDetailRequest.setClaimId(claimId);
		ClaimDetailByIdDomainRequest claimDetailByIdDomainRequest = claimsProviderToDomainConverter.convert(claimDetailRequest);
		claimDetailByIdDomainRequest.setId(claimId);
		ClaimDetailByIdDomainResponse claimDetailByIdDomainResponse = vetServicesClaimsService
				.getClaimDetailById(claimDetailByIdDomainRequest);
		return claimsDomainToProviderConverter.convert(claimDetailByIdDomainResponse);
	}

	/** 
	 * Returns all claims
	 * @return ClaimsDomainResponse
	 */
	@RequestMapping(value = URL_PREFIX + "/claims", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	@ApiOperation(value = "Retrieves all Claims for a given user from Claims Service.",
	notes = "Will return all Claims based on search by pid of the user.")
	public ClaimsResponse getAllclaims() {
		AllClaimsDomainRequest allClaimsDomainRequest = claimsProviderToDomainConverter.convertAllClaims();
		ClaimsDomainResponse claimsDomainResponse = vetServicesClaimsService.getClaims(allClaimsDomainRequest);
		return claimsDomainToProviderConverter.convertAll(claimsDomainResponse);
	}

	/**
	 * A REST call to test this endpoint is up and running.
	 * <p>
	 * This endpoint is NOT intercepted by the standard publicServiceResponseRestMethod aspect
	 * because the return type does not implement {@link ProviderTransferObjectMarker}.
	 *
	 * @see org.springframework.boot.actuate.health.HealthIndicator#health()
	 */
	@Override
	@RequestMapping(value = URL_PREFIX + "/health", method = RequestMethod.GET)
	@ApiOperation(value = "A health check of this endpoint",
			notes = "Will perform a basic health check to see if the operation is running.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = MESSAGE_200) })
	public Health health() {
		return Health.up().withDetail("Claims Service REST Endpoint", "Claims Service REST Provider Up and Running!")
				.build();
	}
}
