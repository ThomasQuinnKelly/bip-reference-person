package gov.va.bip.reference.service.steps;

import static org.junit.Assert.assertEquals;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gov.va.bip.framework.test.selenium.BrowserDI;
import gov.va.bip.reference.service.pages.VaHomePage;

/*
 * Step definition File for VA HomePage. Browser web driver will be injected thru constructor for navigating to any web page.
 * Each step in the feature file implementation is specified here.
 */
public class VaHomePageStepDefs {

	final static Logger LOGGER = LoggerFactory.getLogger(VaHomePageStepDefs.class);
	private BrowserDI browserDI;
	VaHomePage pagegObject = null;

	public VaHomePageStepDefs(BrowserDI browserDI) {
		this.browserDI = browserDI;
		pagegObject = new VaHomePage(this.browserDI.getDriver());
	}

	@Given("^the veteran navigates to VA homepage URL \"([^\"]*)\"$")
	public void ClientRequestPOSTWithJsondata(String ServiceUrl) throws Throwable {
		browserDI.getDriver().get(ServiceUrl);
	}

	@When("^the user is in the VA homepage$")
	public void thenCondition() throws Throwable {
		// do nothing
	}

	@Then("^verify the header in the VA home page$")
	public void validateHeader() throws Throwable {
		boolean isHeaderDisplayed = pagegObject.isHeaderDisplayed();
		assertEquals(true, isHeaderDisplayed);
	}

	@And("^verify the official USA website notice banner$")
	public void validateNoticeBanner() throws Throwable {
		boolean isnoticeBannerDisplayed = pagegObject.isnoticeBannerDisplayed();
		assertEquals(true, isnoticeBannerDisplayed);
	}

	@And("^verify the crisis line banner$")
	public void validateCrisisBanner() throws Throwable {
		boolean isBannerCrisisDisplayed = pagegObject.isBannerCrisisDisplayed();
		assertEquals(true, isBannerCrisisDisplayed);
	}

	@Then("^verify the title in the home page hub container$")
	public void validateContainerHubTitle() throws Throwable {
		boolean isContainerTitleDisplayed = pagegObject.isContainerTitleDisplayed();
		assertEquals(true, isContainerTitleDisplayed);

	}

	@And("^verify the other links inside the health care container$")
	public void validateHealthContainerLinks() throws Throwable {
		boolean isRefillAndTrackDisplayed = pagegObject.isRefillAndTrackDisplayed();
		assertEquals(true, isRefillAndTrackDisplayed);
		boolean isSendandSecureDisplayed = pagegObject.isSendandSecureDisplayed();
		assertEquals(true, isSendandSecureDisplayed);
		boolean isScheduleViewDisplayed = pagegObject.isScheduleViewDisplayed();
		assertEquals(true, isScheduleViewDisplayed);
		boolean isViewLabDisplayed = pagegObject.isViewLabDisplayed();
		assertEquals(true, isViewLabDisplayed);
		boolean isApplyNowDisplayed = pagegObject.isApplyNowDisplayed();
		assertEquals(true, isApplyNowDisplayed);
	}

	@And("^verify the other links inside the disablity container$")
	public void validateDisablityContainerLinks() throws Throwable {
		boolean isCheckYourClaimDisplayed = pagegObject.isCheckYourClaimDisplayed();
		assertEquals(true, isCheckYourClaimDisplayed);
		boolean isPaymentClaimDisplayed = pagegObject.isPaymentClaimDisplayed();
		assertEquals(true, isPaymentClaimDisplayed);
		boolean isUploadEvidenceDisplayed = pagegObject.isUploadEvidenceDisplayed();
		assertEquals(true, isUploadEvidenceDisplayed);
		boolean isFileVaDisablityDisplayed = pagegObject.isFileVaDisablityDisplayed();
		assertEquals(true, isFileVaDisablityDisplayed);
		boolean isClaimForCompDisplayed = pagegObject.isClaimForCompDisplayed();
		assertEquals(true, isClaimForCompDisplayed);

	}

	@After({})
	public void cleanUp(Scenario scenario) {
		browserDI.closeBrowser();

	}
}