package at.cpo.tests;

import java.io.IOException;
import java.nio.file.Paths;
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

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import at.cpo.selenium.common.pageobjects.SeleniumLoginPage;
import at.cpo.utils.ExcelHelper;

/**
 * Test Login by Selenium
 */
@RunWith(Parameterized.class)
public class SmoketestSelenium extends SeleniumHelper {

	/**
	 * The Constant LOGGER.
	 */
//	private static final Logger LOGGER = Logger.getLogger(SmoketestSelenium.class.getSimpleName());

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
		ExtentSparkReporter r = new ExtentSparkReporter(Paths.get("").toAbsolutePath().toString() + "\\RunResults\\runresults.html");
		report = new ExtentReports();
		report.attachReporter(r);

	}

	/**
	 * Tear down after class.
	 *
	 */
	@AfterClass
	public static void tearDownAfterClass() {
		report.flush();
	}

	@Before
	public void setUp() throws IOException {
		logInfo("# setUp ######################");
		logInfo("# username: '" + username + "'");
		logInfo("# password: '" + password + "'");

		setupDriver();
		wait(1);
	}

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
	 * 
	 * @throws Exception
	 */
	@Test
	public void doTest() throws Exception {
		logInfo("# check links xyz ######################");
		test = report.createTest("doTest"); // level = 0

		// start MTours
		node = test.createNode("Step #1 - start MTours");
//		driver.get("https://demo.guru99.com/test/newtours/index.php");
		driver.get("http://localhost:8881/servlets/com.mercurytours.servlet.WelcomeServlet");

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

		screenshotNode();

		// login
		node = test.createNode("Step #2 - login");
		input(SeleniumLoginPage.USERNAME, username);
		input(SeleniumLoginPage.PASSWORD, password);
		click(SeleniumLoginPage.LOGIN);
		screenshotNode();

		// navigate to Home
		node = test.createNode("Step #3 - navigate to Home").generateLog(Status.PASS, "navigate to Home");
		click(SeleniumLoginPage.HOME);
		value = output(SeleniumLoginPage.SIGNININFO);
		screenshotNode();

//		test.addScreenCaptureFromPath(base64conversion());
		test.log(Status.PASS, "test #1");

		wait(1);
	}

	public static void main(String[] args) {
	}
}
