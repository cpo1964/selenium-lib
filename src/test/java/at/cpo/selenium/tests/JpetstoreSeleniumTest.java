/*
 * MIT License
 *
 * Copyright (c) 2023 Christian Pöcksteinera
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

import javax.naming.ConfigurationException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import at.cpo.platform.PlatformHelper;


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
	public String remotehostUrl;
	/**
	 * The Skip.
	 */
	@Parameter(4)
	public String skip;
	/**
	 * The runlocal.
	 */
	@Parameter(5)
	public String runlocal;

	/**
	 * Gets the data.
	 *
	 * @return the data
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ConfigurationException 
	 */
	@Parameterized.Parameters // (name = "{index}: {0}")
	public static Collection<?> getData() throws IOException, ConfigurationException {
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
	}

	/**
	 * Sets the up.
	 *
	 * @throws IOException          Signals that an I/O exception has occurred.
	 * @throws InterruptedException the interrupted exception
	 */
	@Before
	public void setUp() throws IOException, InterruptedException {
		setIteration(getIteration() + 1);
		reportCreateTest("TestCase #" + getIteration() + " login to Jpetstore - runlocal: " + runlocal);
		if (isTrue(skip)) {
			reportTestInfo("setUp: Jpetstore test skipped ...");
			return;
		}

		reportTestInfo("Jpetstore started");
		reportTestInfo("<br>Testparameter:<br>" +
				"username: '" + username + "'<br>" + 
				"password: '" + password + "'<br>" + 
				"localhostUrl: '" + localhostUrl + "'<br>" +
				"remotehostUrl: '" + remotehostUrl + "'<br>" +
				"runlocal: '" + runlocal + "'<br>");

		reportCreateStep("setUp TestCase #" + getIteration() + " #");

		setupDriver();
	}

	/**
	 * Tear down.
	 */
	@After
	public void tearDown() {
		if (isTrue(skip)) {
			reportTestInfo("tearDown: Jpetstore test skipped ...<br>");
			return;
		}
		
		reportCreateStep("tearDown #");
		closeBrowser();

		reportEndTest("Jpetstore finished" + System.lineSeparator());
	}

	/**
	 * Smoke test.
	 *
	 * @throws InterruptedException the interrupted exception
	 */
	@Test
	public void doSeleniumTest() throws InterruptedException {
		if (isTrue(skip)) {
			reportTestInfo("doSeleniumTest: Jpetstore test skipped ...");
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
		testStep01("Step #1 - start Jpetstore");
		testStep02("Step #2 - Login to Jpetstore");
		testStep03("Step #3 - Logout of Jpetstore");
	}

	/**
	 * Test step start app.
	 *
	 * @param msg the msg
	 */
	private void testStep01(String msg) {
		reportCreateStep(msg);
		validate(navigateToStartJpetstorePage(), "Jpetstore app started");
		ok = existsByXpath("//a[contains(@href, '/account/signonForm')]", true);
		validate(ok, "signonForm is visible");
		reportStepPassScreenshot();
	}

	/**
	 * test Step 02.
	 *
	 * @param msg the msg
	 */
	private void testStep02(String msg) {
		reportCreateStep(msg);
		clickByXpath("//a[contains(@href, '/account/signonForm')]");
		inputByXpath("//input[@name='username']", EDITFIELD, username);
		inputByXpath("//input[@name='password']", EDITFIELD, password);
		clickByXpath("//div[@id='Signon']/form/div/div/button");
		ok = existsByXpath("//a[contains(@href, '/account/signoff')]", true);
		validate(ok, "signonOff link is visible");
		reportStepPassScreenshot();
	}

	/**
	 * Test step 03.
	 *
	 * @param msg the msg
	 */
	private void testStep03(String msg) {
		reportCreateStep(msg);
		clickByXpath("//a[contains(@href, '/account/signoff')]");
		ok = existsByXpath("//a[contains(text(),'Sign Up')]", true);
		validate(ok, "Sign Up link is visible");
		reportStepPassScreenshot();
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
				driverGet(testPlatformPropertiesGet(remotehostUrl));
			}
			reportTestPass("JPetstore started");
		} catch (Exception e1) {
			reportTestFail("JPetstore is down");
			return false;
		}
		return true;
	}

	/**
	 * Test signin test case.
	 *
	 * @throws Exception the exception
	 */
	public void testSigninTestCase() throws Exception {
		clickByXpath("//a[contains(@href, '/account/newAccountForm')]");
		inputByXpath("//input[@name='username']", EDITFIELD, "j2ee");
		inputByXpath("//input[@name='password']", EDITFIELD, "j2ee");
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
