package gov.va.bip.reference.service.steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import gov.va.bip.framework.test.rest.BaseStepDefHandler;
import gov.va.bip.framework.test.service.BearerTokenService;

/**
 * Generic steps like validating the status code, setting header for all person service
 * features and scenarios Implementation for the API needs are specified here.
 * <p>
 * For more details please Read the <a
 * href="https://github.com/department-of-veterans-affairs/bip-reference-person/blob/master/docs/referenceperson-intest.md">Integration
 * Testing Guide </a>
 */
public class GenericSteps {
	private BaseStepDefHandler handler = null;

	final Logger LOGGER = LoggerFactory.getLogger(GenericSteps.class);

	public GenericSteps(BaseStepDefHandler handler) {
		this.handler = handler;
	}

	@Given("^the claimant is a \"([^\"]*)\"$")
	public void setHeader(String users) throws Throwable {
		handler.setHeader(users);
	}

	@And("^invoke token API by passing header from \"([^\"]*)\" and sets the authorization in the header$")
	public void setAuthorizationToken(String headerfilename) throws Throwable {
		String tokenvalue = BearerTokenService.getTokenByHeaderFile(headerfilename);
		handler.getHeaderMap().put("Authorization", "Bearer " + tokenvalue);
	}

	@Then("^the service returns status code = (\\d+)$")
	public void theServiceReturnsStatusCode(final int httpCode) throws Throwable {
		handler.validateStatusCode(httpCode);
	}

}
