package gov.va.bip.reference.service.steps;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceRegionHttpMessageConverter;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import gov.va.bip.framework.test.rest.BaseStepDefHandler;

/**
 * The person documents upload feature and scenario implementations for the API
 * needs are specified here to demonstrate using MultiPart upload.
 * <p>
 * For more details please Read the following <br/>
 * <br/>
 * <a href=
 * "https://github.com/department-of-veterans-affairs/bip-reference-person/blob/master/docs/referenceperson-intest.md">DSVA:
 * Integration Testing Guide
 * <p>
 * <a href=
 * "https://github.ec.va.gov/EPMO//bip-reference-person/blob/master/docs/referenceperson-intest.md">EPMO:
 * Integration Testing Guide</a>
 */
public class PersonDocuments {

	/** The Constant LOGGER. */
	final static Logger LOGGER = LoggerFactory.getLogger(PersonDocuments.class);

	/** The handler. */
	private BaseStepDefHandler handler = null;

	/** The document. */
	private String document;

	/** The submit payload. */
	private String submitPayload;

	/**
	 * Instantiates a new person documents.
	 *
	 * @param handler
	 *            the handler
	 */
	public PersonDocuments(BaseStepDefHandler handler) {
		this.handler = handler;
	}

	/**
	 * Sets the up REST.
	 */
	@Before({})
	public void setUpREST() {
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(new ResourceRegionHttpMessageConverter());
		handler.initREST(messageConverters);
	}

	/**
	 * Uploads valid document for person.
	 *
	 * @param serviceURL
	 *            the service URL
	 * @throws Throwable
	 *             the throwable
	 */
	@When("^uploads a valid document for a person with PID using \"([^\"]*)\"$")
	public void uploadsValidDocumentForPerson(String serviceURL) throws Throwable {
		String baseUrl = handler.getRestConfig().getProperty("baseURL", true);
		StringBuilder builder = new StringBuilder();
		builder.append(baseUrl).append(serviceURL);
		handler.invokeAPIUsingPostWithMultiPart(builder.toString(), document, submitPayload);
	}

	/**
	 * Gets the valid document metadata for person.
	 *
	 * @param serviceURL
	 *            the service URL
	 * @return the valid document metadata for person
	 * @throws Throwable
	 *             the throwable
	 */
	@When("^gets a valid document metadata for a person with PID using \"([^\"]*)\"$")
	public void getValidDocumentMetadataForPerson(String serviceURL) throws Throwable {
		String baseUrl = handler.getRestConfig().getProperty("baseURL", true);
		StringBuilder builder = new StringBuilder();
		builder.append(baseUrl).append(serviceURL);
		handler.invokeAPIUsingGet(builder.toString());
	}

	/**
	 * Download sample document.
	 *
	 * @param serviceURL
	 *            the service URL
	 * @throws Throwable
	 *             the throwable
	 */
	@When("^download a sample document using \"([^\"]*)\"$")
	public void downloadSampleDocument(String serviceURL) throws Throwable {
		String baseUrl = handler.getRestConfig().getProperty("baseURL", true);
		StringBuilder builder = new StringBuilder();
		builder.append(baseUrl).append(serviceURL);
		handler.invokeAPIUsingGet(builder.toString(), Resource.class);
	}

	/**
	 * Document.
	 *
	 * @param document
	 *            the document
	 * @throws Throwable
	 *             the throwable
	 */
	@And("^upload a document \"([^\"]*)\"$")
	public void document(String document) throws Throwable {
		this.document = document;
	}

	/**
	 * Pay load.
	 *
	 * @param payLoad
	 *            the pay load
	 * @throws Throwable
	 *             the throwable
	 */
	@And("^submit a payload \"([^\"]*)\"$")
	public void payLoad(String payLoad) throws Throwable {
		this.submitPayload = payLoad;
	}

	/**
	 * Validate person documents download text message.
	 *
	 * @param text
	 *            the text
	 * @throws Throwable
	 *             the throwable
	 */
	@And("^validate sample document download returned content type \"([^\"]*)\"$")
	public void validatePersonDocumentsDownloadContentType(final String type) throws Throwable {
		String contentType = handler.getRestUtil().getResponseHttpHeaders().getContentType().toString();
		assertThat(contentType, equalTo(type));

		Object objResponse = handler.getObjResponse();
		LOGGER.debug("Download Object Response: {}", objResponse);
		if (objResponse instanceof Resource) {
			LOGGER.debug("Object Response of type Resource");
			Resource resource = (Resource) objResponse;
			long contentLength = resource.contentLength();
			assertTrue((contentLength > 0));
		}
	}

	@After({})
	public void cleanUp(Scenario scenario) {
		handler.postProcess(scenario);

	}
}
