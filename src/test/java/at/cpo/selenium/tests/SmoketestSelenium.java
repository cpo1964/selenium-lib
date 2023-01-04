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

import at.cpo.platform.EnvironmentHelper;
import at.cpo.selenium.common.pageobjects.SeleniumLoginPage;
import at.cpo.utils.ExcelHelper;

/**
 * Test Login by Selenium.
 */
@RunWith(Parameterized.class)
public class SmoketestSelenium extends EnvironmentHelper {

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
	 * The Skip.
	 */
	@Parameter(2)
	public String skip;
	/**
	 * The snapshots.
	 */
	@Parameter(3)
	public String snapshots;
	
	/**
	 * Gets the data.
	 *
	 * @return the data
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Parameterized.Parameters // (name = "{index}: {0}")
	public static Collection<?> getData() throws IOException {
		setupPlatform(PLATFORM_SELENIUM);
		return new ExcelHelper(getTestDataFile(), SmoketestSelenium.class.getSimpleName()).getData();
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
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws InterruptedException the interrupted exception
	 */
	@Before
	public void setUp() throws IOException, InterruptedException {
		logInfo("# setUp ######################");
		logInfo("# username: '" + username + "'");
		logInfo("# password: '" + password + "'");

		setupDriver();
	}

	/**
	 * Tear down.
	 */
	@After
	public void tearDown() {
		logInfo("# tearDown ######################");
		if ("true".equals(skip)) {
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
	public void doTest() throws InterruptedException {
		logInfo("# do Test login to MTours ######################");
		reportCreateTest("doTest"); // level = 0

		// start MTours
		reportCreateStep("Step #1 - start MTours");
		if (!navigateToStartPage()) {
			return;
		};
		reportStepPassScreenshot();

		// login
		reportCreateStep("Step #2 - login");
		input(SeleniumLoginPage.USERNAME, username);
		input(SeleniumLoginPage.PASSWORD, password);
		click(SeleniumLoginPage.LOGIN);
		reportStepPassScreenshot();

		// navigate to Home
		reportCreateStep("Step #3 - navigate to Home");
		click(SeleniumLoginPage.HOME);
		value = output(SeleniumLoginPage.SIGNININFO);
		reportStepPass("output:\n" + value);
		reportStepPassScreenshot();

		reportTestPass("test #1");
	}

	/**
	 * Navigate to start page.
	 *
	 * @return true, if successful
	 */
	private boolean navigateToStartPage() {
		try {
			driverGet("http://localhost:8881/servlets/com.mercurytours.servlet.WelcomeServlet");
		} catch (Exception e1) {
			try {
				driverGet("https://demo.guru99.com/test/newtours/index.php");
				driverImplicitlyWait(3000);
				ok = driverSwitchToIFrame("gdpr-consent-notice");
				ok = ok && exists(SeleniumLoginPage.NOTICE);
				if (ok) {
					click(SeleniumLoginPage.NOTICE);
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
}
