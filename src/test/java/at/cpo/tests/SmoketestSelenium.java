package at.cpo.tests;

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
		tearDownExtent();
	}

	/**
	 * Sets the up.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws InterruptedException 
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
	 * @throws InterruptedException 
	 */
	@Test
	public void doTest() throws InterruptedException {
		logInfo("# do Test login to MTours ######################");
		reportCreateTest("doTest"); // level = 0

		// start MTours
		testCreateNode("Step #1 - start MTours");
		
		if (!navigateToStartPage()) {
			return;
		};

		screenshotNodePass();

		// login
		testCreateNode("Step #2 - login");
		input(SeleniumLoginPage.USERNAME, username);
		input(SeleniumLoginPage.PASSWORD, password);
		click(SeleniumLoginPage.LOGIN);
		screenshotNodePass();

		// navigate to Home
		testCreateNode("Step #3 - navigate to Home");
		click(SeleniumLoginPage.HOME);
		value = output(SeleniumLoginPage.SIGNININFO);
		screenshotNodePass();

		testLogPass("test #1");
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
				screenshotNodeFail();
				testLogFail("MTours app is down");
				return false;
			}
		}
		return true;
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
	}
}
