package gov.va.ocp.reference.service.steps;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import gov.va.ocp.framework.test.restassured.BaseStepDefHandler;
import gov.va.ocp.framework.test.service.BearerTokenService;

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

	/*
	 * @And("^the response should be same as \"(.*?)\"$") public void
	 * responseShouldBe(final String responseFile) throws Throwable {
	 * LOGGER.info("responseFile {}", responseFile);
	 * assertThat(handler.compareExpectedResponseWithActual(responseFile),
	 * is(true)); }
	 */

}
