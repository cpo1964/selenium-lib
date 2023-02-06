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
import at.cpo.selenium.common.pageobjects.MToursFlightsPage;
import at.cpo.selenium.common.pageobjects.MToursLoginPage;

/**
 * Test Login by Selenium.
 */
@RunWith(Parameterized.class)
public class MtoursSeleniumTest extends PlatformHelper {

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
		return getTestdata(MtoursSeleniumTest.class.getSimpleName());
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
		reportTeardown();
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
		}
	}

		private void doTestMtours() {
		logInfo("# login to MTours ######################");
		reportCreateTest("Starting MTours - runlocal: " + runlocal); // level = 0

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
		if ("false".equalsIgnoreCase(runlocal)) {
			click(MToursLoginPage.FLIGHTS);
		}

		// flights page
		input(MToursFlightsPage.PASSENGERCOUNT, "2");
		click(MToursFlightsPage.SERVICECLASS_FIRST);

		// navigate to Home
		reportCreateStep("Step #3 - navigate to Home");
		if ("false".equalsIgnoreCase(runlocal)) {
			click(MToursLoginPage.HOME);
			value = output(MToursLoginPage.SIGNININFO);
			String expectedText = "Registered users can sign-in here to find the lowest fare on participating airlines.";
			validate(normalizedValue().contains(expectedText), "value of SignInInfo'<br>" + "expected: '" + expectedText
					+ "'<br>" + "found: '" + value + "'<br>'result");
		}
		reportStepPassScreenshot();

		reportTestPass("Mtours finished");
//		reportEndTest();
	}

	/**
	 * Navigate to start page.
	 *
	 * @return true, if successful
	 */
	private boolean navigateToStartMtoursPage() {
		String url;
		if (isTrue(runlocal)) {
			try {
				url = testPlatformPropertiesGet(localhostUrl);
				logInfo("localhostUrl: " + url);
				driverGet(url);
			} catch (Exception e1) {
				reportStepFailScreenshot();
				reportTestFail("MTours is down");
				return false;
			}
		} else {
			try {
				url = testPlatformPropertiesGet(remoteUrl);
				logInfo("remoteUrl: " + url);
				driverGet(url);
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
				reportTestFail("MTours is down");
				return false;
			}
		}
		return true;
	}

	private String normalizedValue() {
		while (value.contains("  ")) {
			value = value.replace("\n", "").replaceAll("  ", " ");
		}
		return value;
	}

}
