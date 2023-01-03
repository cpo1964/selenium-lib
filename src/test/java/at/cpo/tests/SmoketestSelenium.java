package at.cpo.tests;

import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.aventstack.extentreports.Status;

import at.cpo.selenium.common.pageobjects.SeleniumLoginPage;
import at.cpo.utils.ExcelHelper;

/**
 * Test Login by Selenium.
 */
@RunWith(Parameterized.class)
public class SmoketestSelenium extends SeleniumHelper {

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
	 */
	@Before
	public void setUp() throws IOException {
		logInfo("# setUp ######################");
		logInfo("# username: '" + username + "'");
		logInfo("# password: '" + password + "'");

		setupDriver();
		wait(1);
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

		closeBrowser();
		logAll();
	}

	/**
	 * Smoke test.
	 */
	@Test
	public void doTest() {
		logInfo("# check links xyz ######################");
		test = report.createTest("doTest"); // level = 0

		// start MTours
		node = test.createNode("Step #1 - start MTours");
		
		if (!navigateToStartPage()) {
			return;
		};

		screenshotNode(Status.PASS);

		// login
		node = test.createNode("Step #2 - login");
		input(SeleniumLoginPage.USERNAME, username);
		input(SeleniumLoginPage.PASSWORD, password);
		click(SeleniumLoginPage.LOGIN);
		screenshotNode(Status.PASS);

		// navigate to Home
		node = test.createNode("Step #3 - navigate to Home").generateLog(Status.PASS, "navigate to Home");
		click(SeleniumLoginPage.HOME);
		value = output(SeleniumLoginPage.SIGNININFO);
		screenshotNode(Status.PASS);

		test.log(Status.PASS, "test #1");

		wait(1);
	}

	/**
	 * Navigate to start page.
	 *
	 * @return true, if successful
	 */
	private boolean navigateToStartPage() {
		try {
			driver.get("http://localhost:8881/servlets/com.mercurytours.servlet.WelcomeServlet");
		} catch (Exception e1) {
			try {
				driver.get("https://demo.guru99.com/test/newtours/index.php");
				driver.manage().timeouts().implicitlyWait(Duration.ofMillis(3000));
				List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
				if (iframes.size() > 0) {
					driver.switchTo().frame("gdpr-consent-notice");
					ok = exists(SeleniumLoginPage.NOTICE);
					if (ok) {
						click(SeleniumLoginPage.NOTICE);
						driver.switchTo().defaultContent();
					}
				}
				driver.manage().timeouts().implicitlyWait(Duration.ofMillis(30000));
			} catch (Exception e2) {
				screenshotNode(Status.FAIL);
				test.log(Status.FAIL, "MTours app is down");
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
