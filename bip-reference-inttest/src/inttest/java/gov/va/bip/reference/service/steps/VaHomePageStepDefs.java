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
		// System.out.println("==============="+ServiceUrl);
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
	@And("^verify the other links in the navigation menu$")
	public void validateOtherLinks() throws Throwable {
	//	pagegObject.clickSearchButton();
		//boolean isnSearchDisplayed = pagegObject.isSearchMenuDisplayed();
		//assertEquals(true, isnSearchDisplayed); 
		boolean isContainerTitleDisplayed = pagegObject.isContainerTitleDisplayed();
		assertEquals(true, isContainerTitleDisplayed);
		boolean isRefillAndTrackDisplayed = pagegObject.isRefillAndTrackDisplayed();
		assertEquals(true, isRefillAndTrackDisplayed);

	}

	@After({})
	public void cleanUp(Scenario scenario) {
		browserDI.closeBrowser();

	}
}