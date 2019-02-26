package gov.va.ocp.reference.service.steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.When;
import gov.va.ocp.reference.test.restassured.BaseStepDefHandler;

public class PersonInfo {

	final static Logger LOGGER = LoggerFactory.getLogger(PersonInfo.class);
	private BaseStepDefHandler handler = null;

	public PersonInfo(BaseStepDefHandler handler) {
		this.handler = handler;
	}

	@Before({})
	public void setUpREST() {
		handler.initREST();
	}

	@When("^client request person info \"([^\"]*)\" with PID data \"([^\"]*)\"$")
	public void ClientRequestPOSTWithJsondata(String strURL, String requestFile) throws Throwable {
		String baseUrl = handler.getRestConfig().getProperty("baseURL", true);
		handler.getRestUtil().setUpRequest(requestFile, handler.getHeaderMap());
		handler.invokeAPIUsingPost(baseUrl + strURL);
	}

	@After({})
	public void cleanUp(Scenario scenario) {
		handler.postProcess(scenario);

	}
}
