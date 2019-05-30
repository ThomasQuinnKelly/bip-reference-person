package gov.va.bip.reference.service.steps;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import gov.va.bip.framework.test.rest.BaseStepDefHandler;
import gov.va.bip.framework.test.util.JsonUtil;

/**
 * The person info feature and scenario implementations for the API needs are specified here.
 * <p>
 * For more details please Read the <a
 * href="https://github.com/department-of-veterans-affairs/bip-reference-person/blob/master/docs/referenceperson-intest.md">Integration
 * Testing Guide</a>
 */
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

	@And("^the service returns ParticipantID PID based on participantId (\\d+)$")
	public void validatepartcipantId(final int participantId) throws Throwable {
		Integer partcipantValue = JsonUtil.getInt(handler.getStrResponse(), "personInfo.participantId");
		assertThat(partcipantValue, equalTo(participantId));
	}

	@And("^the service returns message \"([^\"]*)\" and \"([^\"]*)\"$")
	public void validateSeverityTextMessage(final String severity, String text) throws Throwable {
		String severityMessage = JsonUtil.getString(handler.getStrResponse(), "messages[0].severity");
		String textMessage = JsonUtil.getString(handler.getStrResponse(), "messages[0].text");
		assertThat(severityMessage, equalTo(severity));
		assertThat(textMessage, equalTo(text));
	}

	@And("^the service returns message \"([^\"]*)\"$")
	public void validateTextMessage(final String text) throws Throwable {
		String textMessage = JsonUtil.getString(handler.getStrResponse(), "messages[0].text");
		assertThat(textMessage, equalTo(text));
	}
	@And("^the service returns content type \"([^\"]*)\"$")
	public void validateContentType(final String type) throws Throwable {
		String contentType = handler.getRestUtil().getResponseHttpHeaders().getContentType().toString();
		assertThat(contentType, equalTo(type));
	}

	@After({})
	public void cleanUp(Scenario scenario) {
		handler.postProcess(scenario);

	}
}
