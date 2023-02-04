/*
 * MIT License
 *
 * Copyright (c) 2018 Elias Nogueira
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package at.cpo.selenium.tests;

import java.io.IOException;
import java.util.Collection;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import at.cpo.platform.PlatformHelper;
import at.cpo.platform.PlatformInterface;
import at.cpo.selenium.common.pageobjects.MToursFlightsPage;
import at.cpo.selenium.common.pageobjects.MToursLoginPage;
import at.cpo.utils.ExcelHelper;

/**
 * Test Login by Selenium.
 */
@RunWith(Parameterized.class)
public class SmoketestSeleniumTest extends PlatformHelper {

	/**
	 * The Email.
	 */
	@Parameter()
	public String username;
	/**
	 * The Password.
	 */
	@Parameter(1)
	public String password;
	/**
	 * The localhostUrl.
	 */
	@Parameter(2)
	public String localhostUrl;
	/**
	 * The remoteUrl.
	 */
	@Parameter(3)
	public String remoteUrl;
	/**
	 * The Skip.
	 */
	@Parameter(4)
	public String skip;
	/**
	 * The snapshots.
	 */
	@Parameter(5)
	public String runlocal;

	/**
	 * Gets the data.
	 *
	 * @return the data
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Parameterized.Parameters // (name = "{index}: {0}")
	public static Collection<?> getData() throws IOException {
		// FIRST evaluate the file path THEN call getTestDataFile()
		commonSetup(PLATFORM_SELENIUM);
		return new ExcelHelper(getTestDataFile(), SmoketestSeleniumTest.class.getSimpleName()).getData();
	}

	/**
	 * Sets up before class.
	 *
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
	}

	/**
	 * Tear down after class.
	 *
	 */
	@AfterClass
	public static void tearDownAfterClass() {
		reportTearDown();
	}

	/**
	 * Sets the up.
	 *
	 * @throws IOException          Signals that an I/O exception has occurred.
	 * @throws InterruptedException the interrupted exception
	 */
	@Before
	public void setUp() throws IOException, InterruptedException {
		if (isTrue(skip)) {
			return;
		}
		logInfo("# setUp #");
		logInfo("# username: '" + username + "'");
		logInfo("# password: '" + password + "'");
		logInfo("# localhostUrl: '" + localhostUrl + "'");
		logInfo("# remoteUrl: '" + remoteUrl + "'");

		setupDriver();
	}

	/**
	 * Tear down.
	 */
	@After
	public void tearDown() {
		logInfo("# tearDown #");
		if (isTrue(skip)) {
			return;
		}

		logAll();
		closeBrowser();
	}

	/**
	 * Smoke test.
	 *
	 * @throws InterruptedException the interrupted exception
	 */
	@Test
	public void doSeleniumTest() throws InterruptedException {
		if (isTrue(skip)) {
			return;
		}

		if (localhostUrl.startsWith("mtours")) {
			doTestMtours();
		} else if (localhostUrl.startsWith("jpetstore")) {
			doTestJpetstore();
		}
	}

	private void doTestJpetstore() {
		logInfo("# login to Jpetstore ######################");
		reportCreateTest("login to Jpetstore - runlocal: " + runlocal); // level = 0

		// start Jpetstore
		reportCreateStep("Step #1 - start Jpetstore");
		if (!navigateToStartJpetstorePage()) {
			return;
		}
		reportStepPassScreenshot();

		reportCreateStep("Step #2 - Login to Jpetstore");
		testLogin("cpo1964", "Test");
		reportStepPassScreenshot();

		reportCreateStep("Step #3 - Logout of Jpetstore");
		testSignoffTestCase();
		reportStepPassScreenshot();

		reportTestPass("login to Jpetstore");
//		reportEndTest();
	}

	private boolean navigateToStartJpetstorePage() {
		String url;
		if (isTrue(runlocal)) {
			try {
				url = testPlatformPropertiesGet(localhostUrl);
				logInfo("localhostUrl: " + url);
				driverGet(url);
			} catch (Exception e1) {
				reportStepFailScreenshot();
				reportTestFail("JPetstore app is down");
				return false;
			}
		} else {
			try {
				url = testPlatformPropertiesGet(remoteUrl);
				logInfo("remoteUrl: " + url);
				driverGet(url);
			} catch (Exception e2) {
				reportStepFailScreenshot();
				reportTestFail("JPetstore app is down");
				return false;
			}
		}
		return true;
	}

	private void doTestMtours() {
		logInfo("# login to MTours ######################");
		reportCreateTest("login to MTours - runlocal: " + runlocal); // level = 0

		// start MTours
		reportCreateStep("Step #1 - start MTours");
		if (!navigateToStartMtoursPage()) {
//			reportEndTest();
			return;
		}
		reportStepPassScreenshot();

		// login
		reportCreateStep("Step #2 - login");
		input(MToursLoginPage.USERNAME, username);
		input(MToursLoginPage.PASSWORD, password);
		click(MToursLoginPage.LOGIN);
//		value = output(SeleniumLoginPage.LOGINOK);
//		String expectedText = "Login Successfully";
//		validate(normalizedValue().contains(expectedText),
//				"value of LoginOk'<br>" +
//				"expected: '" + expectedText + "'<br>" +
//				"found: '" + value + "'<br>'result");
		reportStepPassScreenshot();
//		click(SeleniumLoginPage.FLIGHTS);

		// flights page
		input(MToursFlightsPage.PASSENGERCOUNT, "2");
		click(MToursFlightsPage.SERVICECLASS_FIRST);

		// navigate to Home
		reportCreateStep("Step #3 - navigate to Home");
		click(MToursLoginPage.HOME);
		value = output(MToursLoginPage.SIGNININFO);
		String expectedText = "Registered users can sign-in here to find the lowest fare on participating airlines.";
		validate(normalizedValue().contains(expectedText), "value of SignInInfo'<br>" + "expected: '" + expectedText
				+ "'<br>" + "found: '" + value + "'<br>'result");
		reportStepPassScreenshot();

		reportTestPass("test #1");
//		reportEndTest();
	}

	private String normalizedValue() {
		while (value.contains("  ")) {
			value = value.replace("\n", "").replaceAll("  ", " ");
		}
		return value;
	}

	/**
	 * Navigate to start page.
	 *
	 * @return true, if successful
	 */
	private boolean navigateToStartMtoursPage() {
		if (isTrue(runlocal)) {
			try {
				logInfo("localhostUrl: " + testPlatformPropertiesGet(localhostUrl));
				driverGet(testPlatformPropertiesGet(localhostUrl));
			} catch (Exception e1) {
				reportStepFailScreenshot();
				reportTestFail("MTours app is down");
				return false;
			}
		} else {
			try {
				logInfo("remoteUrl: " + testPlatformPropertiesGet(remoteUrl));
				driverGet(testPlatformPropertiesGet(remoteUrl));
				driverImplicitlyWait(3000);
				// Send future commands to iFrame
				ok = driverSwitchToIFrame("gdpr-consent-notice");
				ok = ok && exists(MToursLoginPage.NOTICE);
				if (ok) {
					click(MToursLoginPage.NOTICE);
					// Send future commands to main document
					driverSwitchToDefaultContent();
				}
				driverImplicitlyWait(30000);
			} catch (Exception e2) {
				reportStepFailScreenshot();
				reportTestFail("MTours app is down");
				return false;
			}
		}
		return true;
	}

//	@Test
	public void testSigninTestCase() throws Exception {
		driverGet("https://jpetstore.aspectran.com/catalog/");
		clickByXpath("//a[contains(@href, '/account/newAccountForm')]");
		inputByXpath("//input[@name='username']", PlatformInterface.EDITFIELD, "cpo1964");
		inputByXpath("//input[@name='password']", PlatformInterface.EDITFIELD, "Test");
		inputByXpath("//input[@name='repeatedPassword']", PlatformInterface.EDITFIELD, "Test");
		inputByXpath("//input[@name='firstName']", PlatformInterface.EDITFIELD, "Cpo");
		inputByXpath("//input[@name='lastName']", PlatformInterface.EDITFIELD, "Cpo");
		inputByXpath("//input[@name='email']", PlatformInterface.EDITFIELD, "cpo1964@aon.at");
		inputByXpath("//input[@name='phone']", PlatformInterface.EDITFIELD, "12345");
		inputByXpath("//input[@name='address1']", PlatformInterface.EDITFIELD, "cpo 1");
		inputByXpath("//input[@name='address2']", PlatformInterface.EDITFIELD, "cpo 2");
		inputByXpath("//input[@name='city']", PlatformInterface.EDITFIELD, "Cpo");
		inputByXpath("//input[@name='state']", PlatformInterface.EDITFIELD, "Cpostate");
		inputByXpath("//input[@name='zip']", PlatformInterface.EDITFIELD, "1111");
		inputByXpath("//input[@name='country']", PlatformInterface.EDITFIELD, "Cpocountry");
		clickByXpath("//select[@name='languagePreference']");
		inputByXpath("//select[@name='languagePreference']", PlatformInterface.LISTBOX, "German");
		clickByXpath("//option[@value='german']");
		clickByXpath("//input[@name='listOption']");
		clickByXpath("//input[@name='bannerOption']");
		clickByXpath("//div[@id='CenterForm']/form/div/button");
	}

	private void testLogin(String user, String passwort) {
//		driverGet("https://jpetstore.aspectran.com/catalog/");
		ok = existsByXpath("//a[contains(@href, '/account/signonForm')]", true);
		validate(ok, "signonForm is visible");
		clickByXpath("//a[contains(@href, '/account/signonForm')]");
		inputByXpath("//input[@name='username']", PlatformInterface.EDITFIELD, user);
		inputByXpath("//input[@name='password']", PlatformInterface.EDITFIELD, passwort);
		clickByXpath("//div[@id='Signon']/form/div/div/button");
	}

	private void testSignoffTestCase() {
		driverGet("https://jpetstore.aspectran.com/catalog/");
		driverImplicitlyWait(3000);
		clickByXpath("//a[contains(@href, '/account/signoff')]");
		existsByXpath("//a[contains(text(),'Sign Up')]", true);
//		for (int second = 0;; second++) {
//			if (second >= 3)
//				fail("timeout");
//			try {
//				if (existsByXpath("//a[contains(text(),'Sign Up')]"))
//					break;
//			} catch (Exception e) {
//			}
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//			}
//		}
		driverImplicitlyWait(30000);
	}
}
