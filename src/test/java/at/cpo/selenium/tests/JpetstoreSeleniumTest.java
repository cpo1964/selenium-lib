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

// TODO: Auto-generated Javadoc
/**
 * Test Login by Selenium.
 */
@RunWith(Parameterized.class)
public class JpetstoreSeleniumTest extends PlatformHelper {

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
		return getTestdata(JpetstoreSeleniumTest.class.getSimpleName());
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
		LOGGER.info("# setUp #");
		LOGGER.info("# username: '" + username + "'");
		LOGGER.info("# password: '" + password + "'");
		LOGGER.info("# localhostUrl: '" + localhostUrl + "'");
		LOGGER.info("# remoteUrl: '" + remoteUrl + "'");

		setupDriver();
	}

	/**
	 * Tear down.
	 */
	@After
	public void tearDown() {
		LOGGER.info("# tearDown #");
		if (isTrue(skip)) {
			return;
		}

//		logAll();
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

		if (localhostUrl.startsWith("jpetstore")) {
			doTestJpetstore();
		}
	}

	/**
	 * Do test jpetstore.
	 */
	private void doTestJpetstore() {
		LOGGER.info("# login to Jpetstore ######################");
		reportCreateTest("login to Jpetstore - runlocal: " + runlocal); // level = 0

		// start Jpetstore
		reportCreateStep("Step #1 - start Jpetstore");
		validate(navigateToStartJpetstorePage(), "Jpetstore started");
		reportStepPassScreenshot();

		reportCreateStep("Step #2 - Login to Jpetstore");
		testLogin("cpo1964", "Test");
		reportStepPassScreenshot();

		reportCreateStep("Step #3 - Logout of Jpetstore");
		testSignoffTestCase();
		reportStepPassScreenshot();

		reportTestPass("Jpetstore finished");
//		reportEndTest();
	}

	/**
	 * Navigate to start jpetstore page.
	 *
	 * @return true, if successful
	 */
	private boolean navigateToStartJpetstorePage() {
		try {
			if (isTrue(runlocal)) {
				driverGet(testPlatformPropertiesGet(localhostUrl));
			} else {
				driverGet(testPlatformPropertiesGet(remoteUrl));
			}
			reportTestPass("JPetstore started");
		} catch (Exception e1) {
			reportTestFail("JPetstore is down");
			return false;
		}
		return true;
	}

	/**
	 * Test login.
	 *
	 * @param user the user
	 * @param passwort the passwort
	 */
	private void testLogin(String user, String passwort) {
		ok = existsByXpath("//a[contains(@href, '/account/signonForm')]", true);
		validate(ok, "signonForm is visible");
		clickByXpath("//a[contains(@href, '/account/signonForm')]");
		inputByXpath("//input[@name='username']", EDITFIELD, user);
		inputByXpath("//input[@name='password']", EDITFIELD, passwort);
		clickByXpath("//div[@id='Signon']/form/div/div/button");
	}

	/**
	 * Test signoff test case.
	 */
	private void testSignoffTestCase() {
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

	/**
	 * Test signin test case.
	 *
	 * @throws Exception the exception
	 */
	public void testSigninTestCase() throws Exception {
		clickByXpath("//a[contains(@href, '/account/newAccountForm')]");
		inputByXpath("//input[@name='username']", EDITFIELD, "cpo1964");
		inputByXpath("//input[@name='password']", EDITFIELD, "Test");
		inputByXpath("//input[@name='repeatedPassword']", EDITFIELD, "Test");
		inputByXpath("//input[@name='firstName']", EDITFIELD, "Cpo");
		inputByXpath("//input[@name='lastName']", EDITFIELD, "Cpo");
		inputByXpath("//input[@name='email']", EDITFIELD, "cpo1964@aon.at");
		inputByXpath("//input[@name='phone']", EDITFIELD, "12345");
		inputByXpath("//input[@name='address1']", EDITFIELD, "cpo 1");
		inputByXpath("//input[@name='address2']", EDITFIELD, "cpo 2");
		inputByXpath("//input[@name='city']", EDITFIELD, "Cpo");
		inputByXpath("//input[@name='state']", EDITFIELD, "Cpostate");
		inputByXpath("//input[@name='zip']", EDITFIELD, "1111");
		inputByXpath("//input[@name='country']", EDITFIELD, "Cpocountry");
		clickByXpath("//select[@name='languagePreference']");
		inputByXpath("//select[@name='languagePreference']", LISTBOX, "German");
		clickByXpath("//option[@value='german']");
		clickByXpath("//input[@name='listOption']");
		clickByXpath("//input[@name='bannerOption']");
		clickByXpath("//div[@id='CenterForm']/form/div/button");
	}

}
