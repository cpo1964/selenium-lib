package at.cpo.tests;

import java.io.File;

/**
 * The Interface EnvironmentInterface.
 */
public interface EnvironmentInterface {
	
	// platform stuff
	
	/**
	 * Setup driver.
	 */
	void setupDriver();
	
	/**
	 * Driver switch to default content.
	 */
	void driverSwitchToDefaultContent();
	
	/**
	 * Driver implicitly wait.
	 *
	 * @param value the value
	 */
	void driverImplicitlyWait(long value);

	/**
	 * Driver switch to I frame.
	 *
	 * @param name the name
	 * @return true, if successful
	 */
	boolean driverSwitchToIFrame(String name);

	/**
	 * Close browser.
	 */
	void closeBrowser();

	/**
	 * Driver get.
	 *
	 * @param url the url
	 */
	void driverGet(String url);

	/**
	 * Click.
	 *
	 * @param locatorDelegate the locator delegate
	 */
	void click(String locatorDelegate);

	/**
	 * Input.
	 *
	 * @param locatorDelegate the locator delegate
	 * @param value the value
	 */
	void input(String locatorDelegate, String value);

	/**
	 * Output.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the string
	 */
	String output(String locatorDelegate);

	/**
	 * Exists.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return true, if successful
	 */
	boolean exists(String locatorDelegate);

	// report stuff

	/**
	 * Report create test.
	 *
	 * @param msg the msg
	 */
	void reportCreateTest(String msg);

	/**
	 * Test log fail.
	 *
	 * @param msg the msg
	 */
	void reportTestLogFail(String msg);

	/**
	 * Test log pass.
	 *
	 * @param msg the msg
	 */
	void reportTestLogPass(String msg);

	/**
	 * Test create node.
	 step
	 * @param msg the msg
	 */
	void reportCreateStep(String msg);

	/**
	 * Screenshot step pass.
	 */
	void reportScreenshotStepPass();

	/**
	 * Screenshot step fail.
	 */
	void reportScreenshotStepFail();

	/**
	 * Tear down extent.
	 */
	void reportTearDown();

	// logging stuff

	/**
	 * Log info.
	 *
	 * @param msg the msg
	 */
	void logInfo(String msg);

	/**
	 * Log debug.
	 *
	 * @param msg the msg
	 */
	void logDebug(String msg);

	/**
	 * Log error.
	 *
	 * @param msg the msg
	 */
	void logError(String msg);

	/**
	 * Log all.
	 */
	void logAll();

	// other stuff

	/**
	 * Gets the test data file.
	 *
	 * @return the test data file
	 */
	File getTestDataFile();

}
