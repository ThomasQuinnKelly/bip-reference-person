package gov.va.bip.reference.service.steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import gov.va.bip.framework.test.rest.BaseStepDefHandler;

/**
 * The person info feature and scenario implementations for the API needs are specified here.
 * <p>
 * For more details please Read the <a
 * href="https://github.com/department-of-veterans-affairs/bip-reference-person/blob/master/docs/referenceperson-intest.md">Integration
 * Testing Guide</a>
 */
public class PersonDocsUpload {

	final static Logger LOGGER = LoggerFactory.getLogger(PersonDocsUpload.class);
	private BaseStepDefHandler handler = null;
	private String document;
	private String submitPayload;

	public PersonDocsUpload(BaseStepDefHandler handler) {
		this.handler = handler;
	}

	@Before({})
	public void setUpREST() {
		handler.initREST();
	}

	@When("^uploads a valid document for a person using \"([^\"]*)\"$")
	public void uploadsValidDocumentForPerson(String serviceURL) throws Throwable {
		String baseUrl = handler.getRestConfig().getProperty("baseURL", true);
		StringBuilder builder = new StringBuilder();
		builder.append(baseUrl).append(serviceURL);
		handler.invokeAPIUsingPostWithMultiPart(builder.toString(), document, submitPayload);
	}
	
	@And("^upload a document \"([^\"]*)\"$")
	public void document(String document) throws Throwable {
		this.document = document;
	}

	@And("^submit a payload \"([^\"]*)\"$")
	public void payLoad(String payLoad) throws Throwable {
		this.submitPayload = payLoad;
	}

	@After({})
	public void cleanUp(Scenario scenario) {
		handler.postProcess(scenario);

	}
}
