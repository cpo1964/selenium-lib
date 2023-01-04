package at.cpo.platform;

import java.io.File;

import at.cpo.report.ReportInterface;

/**
 * The Interface EnvironmentInterface.
 */
public interface EnvironmentInterface extends ReportInterface {
	
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

	// other stuff

	/**
	 * Gets the test data file.
	 *
	 * @return the test data file
	 */
	File getTestDataFile();

}
