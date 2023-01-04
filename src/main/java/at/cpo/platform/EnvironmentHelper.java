package at.cpo.platform;

import java.io.File;

import at.cpo.platform.selenium.SeleniumHelper;

/**
 * The Class EnvironmentHelper.
 */
public class EnvironmentHelper {

	/** The platform. */
	protected static String PLATFORM_SELENIUM = "Selenium";
	
	/** The environment. */
	static EnvironmentInterface environment;
	
	/** The value. */
	protected static String value;

	/** The ok. */
	protected boolean ok;

	/**
	 * Sets the up platform.
	 *
	 * @param value the new up platform
	 */
	protected static void setupPlatform(String value) {
		if (PLATFORM_SELENIUM.equalsIgnoreCase(value)) {
			environment = new SeleniumHelper();
		} else {
			throw new RuntimeException();
		}
	}
	
	/**
	 * Setup driver.
	 */
	protected void setupDriver() {
		environment.setupDriver();	
	}
	
	/**
	 * Driver switch to default content.
	 */
	protected void driverSwitchToDefaultContent() {
		environment.driverSwitchToDefaultContent();
	}

	/**
	 * Driver implicitly wait.
	 *
	 * @param value the value
	 */
	public void driverImplicitlyWait(long value) {
		environment.driverImplicitlyWait(value);
	}

	/**
	 * Driver switch to I frame.
	 *
	 * @param name the name
	 * @return true, if successful
	 */
	protected boolean driverSwitchToIFrame(String name) {
		return environment.driverSwitchToIFrame(name);	
	}

	/**
	 * Driver get.
	 *
	 * @param url the url
	 */
	protected void driverGet(String url) {
		environment.driverGet(url);	
	}

	/**
	 * Close browser.
	 */
	protected void closeBrowser() {
		environment.closeBrowser();
	}

	/**
	 * Click.
	 *
	 * @param locatorDelegate the locator delegate
	 */
	protected void click(String locatorDelegate) {
		environment.click(locatorDelegate);	
	}

	/**
	 * Input.
	 *
	 * @param locatorDelegate the locator delegate
	 * @param value the value
	 */
	protected void input(String locatorDelegate, String value) {
		environment.input(locatorDelegate, value);
	}

	/**
	 * Output.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the string
	 */
	protected String output(String locatorDelegate) {
		return environment.output(locatorDelegate);	
	}

	/**
	 * Exists.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return true, if successful
	 */
	protected boolean exists(String locatorDelegate) {
		return environment.exists(locatorDelegate);	
	}

	/**
	 * Test log fail.
	 *
	 * @param msg the msg
	 */
	protected void reportTestLogFail(String msg) {
		environment.reportTestLogFail(msg);
	}

	/**
	 * Screenshot step fail.
	 */
	protected void reportScreenshotStepFail() {
		environment.reportScreenshotStepFail();
	}

	/**
	 * Test log pass.
	 *
	 * @param msg the msg
	 */
	protected void reportTestLogPass(String msg) {
		environment.reportTestLogPass(msg);
	}

	/**
	 * Screenshot step pass.
	 */
	protected void reportScreenshotStepPass() {
		environment.reportScreenshotStepPass();
	}

	/**
	 * Test create step.
	 *
	 * @param msg the msg
	 */
	protected void reportCreateStep(String msg) {
		environment.reportCreateStep(msg);
	}

	/**
	 * Report create test.
	 *
	 * @param msg the msg
	 */
	protected void reportCreateTest(String msg) {
		environment.reportCreateTest(msg);
	}

	/**
	 * Tear down extent.
	 */
	protected static void reportTearDown() {
		environment.reportTearDown();	
	}

	/**
	 * Gets the test data file.
	 *
	 * @return the test data file
	 */
	protected static File getTestDataFile() {
		return environment.getTestDataFile();	
	}

	/**
	 * Log info.
	 *
	 * @param msg the msg
	 */
	protected void logInfo(String msg) {
		environment.logInfo(msg);
	}

	/**
	 * Log all.
	 */
	protected void logAll() {
		environment.logAll();
	}

}
