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

import org.junit.Assert;
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
/* 
 * ein JUnit Test
 * Aufrufreihenfolge:
 * 1) die Methoden annotiert mit @Parameterized.Parameters - zb getData()
 * 2) die Methode annotiert mit @BeforeClass - zb setUpBeforeClass()
 * 3) die Methoden annotiert mit @Before - zb setUp()
 * 4) die Methoden annotiert mit @Test - zb doSeleniumTest()
 * 5) die Methoden annotiert mit @After - zb tearDown()
 * 6) die Methode annotiert mit @AfterClass - zb tearDownAfterClass()
 */
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
	public String remotehostUrl;
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
	 * @throws ConfigurationException 
	 */
	@Parameterized.Parameters // (name = "{index}: {0}")
	public static Collection<?> getData() throws IOException, ConfigurationException {
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
	}

	/**
	 * Sets the up.
	 *
	 * @throws IOException          Signals that an I/O exception has occurred.
	 * @throws InterruptedException the interrupted exception
	 */
	@Before
	public void setUp() throws IOException, InterruptedException {
		setRunStatus(true);
		setIteration(getIteration() + 1);
		reportCreateTest("TestCase #" + getIteration() + " login to MTours - runlocal: " + runlocal);
		if (isTrue(skip)) {
			reportTestInfo("setUp: MTours test skipped");
			setRunStatus(true);
			return;
		}
		reportTestInfo("MTours started");
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
			reportTestInfo("tearDown: MTours test skipped");
			setRunStatus(true);
			return;
		}
		
		reportCreateStep("tearDown #");
		closeBrowser();

		reportEndTest("MTours finished" + System.lineSeparator());
		Assert.assertTrue(isRunStatus());
	}

	/**
	 * Smoke test.
	 *
	 * @throws InterruptedException the interrupted exception
	 */
	@Test
	public void doSeleniumTest() throws InterruptedException {
		Assert.assertTrue(isRunStatus());
		if (isTrue(skip)) {
			reportTestInfo("doSeleniumTest: MTours test skipped");
			setRunStatus(true);
			return;
		}

		if (localhostUrl.startsWith("mtours")) {
			doTestMtours();
		}
	}

		/**
		 * Do test mtours.
		 */
		private void doTestMtours() {

		// start MTours
		reportCreateStep("Step #1 - start MTours");
		validate(navigateToStartMtoursPage(), "MTours started");
		reportStepPassScreenshot();

		// login
		reportCreateStep("Step #2 - login");
		exists(MToursLoginPage.USERNAME, 10);
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
			ok = exists(MToursLoginPage.FLIGHTS, 3);
			if (ok) {
				click(MToursLoginPage.FLIGHTS);
			}
		}

		// flights page
//		input(MToursFlightsPage.PASSENGERCOUNT, "2");
//		click(MToursFlightsPage.SERVICECLASS_FIRST);

		// navigate to Home
		reportCreateStep("Step #3 - navigate to Home");
		ok = exists(MToursLoginPage.HOME, 3);
		if (ok || "false".equalsIgnoreCase(runlocal)) {
			click(MToursLoginPage.HOME);
			value = output(MToursLoginPage.SIGNININFO);
			String expectedText = "Registered users can sign-in here to find the lowest fare on participating airlines.";
			// TODO
//			validate(normalizedValue().contains(expectedText), "value of SignInInfo'" + "expected: '" + expectedText
//					+ "' - found: '" + value + "'");
		}
		reportStepPassScreenshot();
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
				log.info("localhostUrl: " + url);
				driverGet(url);
			} catch (Exception e1) {
				reportTestFail("MTours is down");
				return false;
			}
		} else {
			try {
				url = testPlatformPropertiesGet(remotehostUrl);
				log.info("remoteUrl: " + url);
				driverGet(url);
//				setDriverImplicitlyWaitTimoutSeconds(10);
				// Send future commands to iFrame
//				wait(5000);
				ok = driverSwitchToIFrame("gdpr-consent-notice");
//				wait(5000);
				ok = ok && exists(MToursLoginPage.NOTICE, 10);
				wait(5000);
				if (ok) {
					wait(5000);
					reportStepInfo("iframe mit notiz schliessen ...");
					click(MToursLoginPage.NOTICE, 10);
					// Send future commands to main document
					driverSwitchToDefaultContent();
					wait(2000);
				}
//				setDriverImplicitlyWaitTimoutSeconds(30);
			} catch (Exception e2) {
				reportStepFailScreenshot();
				reportTestFail("MTours is down");
				return false;
			}
		}
		return true;
	}

	/**
	 * Normalized value.
	 *
	 * @return the string
	 */
	private String normalizedValue() {
		value = value.replaceAll("<br>", "");
		while (value.contains("  ")) {
			value = value.replace("\n", "").replaceAll("  ", " ");
		}
		return value;
	}

}
