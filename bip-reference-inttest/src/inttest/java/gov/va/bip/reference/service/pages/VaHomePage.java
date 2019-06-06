package gov.va.bip.reference.service.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class VaHomePage {

	protected WebDriver driver;

	public VaHomePage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	// Page objects in VA home page

	// Header in the home page
	@FindBy(xpath = "//div[@class='row va-flex usa-grid usa-grid-full']")
	WebElement header;

	// crisis line banner in the header
	@FindBy(xpath = "//div[@class='va-crisis-line-inner']")
	WebElement crisislinebanner;

	// VA va notice banner
	@FindBy(xpath = "//DIV[@class='va-notice--banner']")
	WebElement noticebanner;

	// VA Logo in the header
	@FindBy(xpath = "//div[@class='va-header-logo-wrapper']")
	WebElement logo;

	// Search dropdown in the header
	@FindBy(xpath = "//div[@class='profile-nav']/div[1]/button")
	WebElement searchButton;

	// search menu in the nav menu in header
	@FindBy(xpath = "//div[@id='search-menu']")
	WebElement searchMenu;

	// Contactus dropdown in the header
	@FindBy(xpath = "//div[@class='profile-nav']/div[2]")
	WebElement contactus;

	// Signin button in the header
	@FindBy(xpath = "//BUTTON[@class='sign-in-link']")
	WebElement signin;

	// VA benefits and health care link in the nav menu in header
	@FindBy(xpath = "//ul[@id='vetnav-menu']/li[2]")
	WebElement vabenefitsandhealthcarelink;

	// About VA link in the nav menu in header
	@FindBy(xpath = "//ul[@id='vetnav-menu']/li[3]")
	WebElement aboutvalink;

	// Find VA location link in the nav menu in header
	@FindBy(xpath = "//ul[@id='vetnav-menu']/li[4]")
	WebElement valocationlink;

	// Container title
	@FindBy(xpath = "//H1[@class='heading-level-2 homepage-heading']")
	WebElement containertitle;

	// Health care container
	@FindBy(xpath = "(//DIV[@class='hub-links-container'])[1]")
	WebElement healthcarecontainer;

	// Health care container title
	@FindBy(xpath = "(//H2[@class='heading-level-3 hub-links-title'])[1]")
	WebElement healthcarecontainertitle;

	// Refill and track your prescriptions in health care in health care container
	// link
	@FindBy(xpath = "(//ul[@class='hub-links-list'])[1]/li[1]")
	WebElement refillandtracklink;

	// Send a secure message to your health care team in health care container link
	@FindBy(xpath = "(//ul[@class='hub-links-list'])[1]/li[2]")
	WebElement sendandsecuremessagge;

	// Schedule and view your appointments in health care container link
	@FindBy(xpath = "(//ul[@class='hub-links-list'])[1]/li[3]")
	WebElement scheduleviewappt;

	// View your lab and test results in health care container link
	@FindBy(xpath = "(//ul[@class='hub-links-list'])[1]/li[4]")
	WebElement viewlabtestandresult;

	// Apply now for VA health care in health care container link
	@FindBy(xpath = "(//ul[@class='hub-links-list'])[1]/li[5]")
	WebElement applynow;

	// Disablity container
	@FindBy(xpath = "(//DIV[@class='hub-links-container'])[2]")
	WebElement disablitycontainer;

	// Disablity container title
	@FindBy(xpath = "(//H2[@class='heading-level-3 hub-links-title'])[2]")
	WebElement disablitycontainertitle;

	// Check your claim or appeal status in diasblity container link
	@FindBy(xpath = "(//ul[@class='hub-links-list'])[2]/li[1]")
	WebElement checkyourclaim;

	// View your payment history in diasblity container link
	@FindBy(xpath = "(//ul[@class='hub-links-list'])[2]/li[2]")
	WebElement paymenthistory;

	// Upload evidence to support your claim in diasblity container link
	@FindBy(xpath = "(//ul[@class='hub-links-list'])[2]/li[3]")
	WebElement uploadevidence;

	// File for a VA disability increase in diasblity container link
	@FindBy(xpath = "(//ul[@class='hub-links-list'])[2]/li[4]")
	WebElement filevadisablity;

	// File a claim for compensation in diasblity container link
	@FindBy(xpath = "(//ul[@class='hub-links-list'])[2]/li[5]")
	WebElement claimforcomp;

	public boolean isHeaderDisplayed() {
		return header.isDisplayed();
	}

	public boolean isBannerCrisisDisplayed() {
		return crisislinebanner.isDisplayed();
	}

	public boolean isnoticeBannerDisplayed() {
		return noticebanner.isDisplayed();
	}

	public void clickSearchButton() {
		searchButton.click();
	}

	public boolean isSearchMenuDisplayed() {
		return searchMenu.isDisplayed();
	}

	public boolean isContactusDisplayed() {
		return contactus.isDisplayed();
	}

	public boolean isSignInDisplayed() {
		return signin.isDisplayed();
	}

	public boolean isBenefitsandHealthLinkDisplayed() {
		return vabenefitsandhealthcarelink.isDisplayed();
	}

	public boolean isAboutUsDisplayed() {
		return aboutvalink.isDisplayed();
	}
	public boolean isFindVALocationDisplayed() {
		return valocationlink.isDisplayed();
	}
	public boolean isContainerTitleDisplayed() {
		return containertitle.isDisplayed();
	}
	public boolean isHealthCareDisplayed() {
		return healthcarecontainer.isDisplayed();
	}
	public boolean isHealthCareContainerTitleDisplayed() {
		return healthcarecontainertitle.isDisplayed();
	}
	public boolean isRefillAndTrackDisplayed() {
		return refillandtracklink.isDisplayed();
	}
	public boolean isSendandSecureDisplayed() {
		return sendandsecuremessagge.isDisplayed();
	}
	public boolean isScheduleViewDisplayed() {
		return scheduleviewappt.isDisplayed();
	}
	public boolean isViewLabDisplayed() {
		return viewlabtestandresult.isDisplayed();
	}
	public boolean isApplyNowDisplayed() {
		return applynow.isDisplayed();
	}
	public boolean isCheckYourClaimDisplayed() {
		return checkyourclaim.isDisplayed();
	}
}
