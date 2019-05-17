package gov.va.bip.reference.service.steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import gov.va.bip.framework.test.rest.BaseStepDefHandler;
import gov.va.bip.framework.test.util.JsonUtil;

public class PersonHealth {

	final static Logger LOGGER = LoggerFactory.getLogger(PersonHealth.class);
	private BaseStepDefHandler handler = null;

	public PersonHealth(BaseStepDefHandler handler) {
		this.handler = handler;
	}

	@Before({})
	public void setUpREST() {
		handler.initREST();
	}
	
	@Given("^the claimant is not a \"([^\"]*)\"$")
	public void setHeader(String users) throws Throwable {
		handler.setHeader(users);
	}
	@When("^client request health info \"([^\"]*)\"$")
	public void makingGetRequest(final String strURL) throws Throwable {
		String baseUrl = handler.getRestConfig().getProperty("baseURL", true);
		handler.invokeAPIUsingGet(baseUrl + strURL);
	}

	@And("verify person health service status is UP and details of Person Service REST Provider Up and Running")
	public void getPersonHealthMessageValidation() {
		String status = JsonUtil.getString(handler.getStrResponse(), "status");
		String details = JsonUtil.getString(handler.getStrResponse(),
				"['details'].['Reference Person Service REST Endpoint']");
		assertThat(status, equalTo("UP"));
		assertThat(details, equalTo("Person Service REST Provider Up and Running!"));
	}

	@After({})
	public void cleanUp(Scenario scenario) {
		handler.postProcess(scenario);

	}
}
