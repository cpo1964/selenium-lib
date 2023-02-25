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
package at.cpo.platform;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.naming.ConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;

import at.cpo.platform.selenium.SeleniumHelper;
import at.cpo.utils.ExcelHelper;

/**
 * The Class EnvironmentHelper.
 */
public class PlatformHelper implements PlatformInterface {

	/** The logger. */
	protected Logger log = LogManager.getLogger(this.getClass().getSimpleName());

	/** The platform. */
	static PlatformInterface platform;
	
	/** The driver. */
	static Object driver;
	
	/** The value. */
	protected static String value;

	/** The ok. */
	protected boolean ok;
	
	/** The iteration. */
	private static int iteration = 0;

	// START - place to expand platform specific features - eg Selenium:

	/**
	 * Gets the driver.
	 *
	 * @return the driver
	 */
	private static Object getDriver() {
		return driver;
	}

	/**
	 * Sets the driver.
	 *
	 * @param driver the new driver
	 */
	private static void setDriver(Object driver) {
		PlatformHelper.driver = driver;
	}

	/**
	 * Gets the iteration.
	 *
	 * @return the iteration
	 */
	public static int getIteration() {
		return iteration;
	}

	/**
	 * Sets the iteration.
	 *
	 * @param iteration the new iteration
	 */
	public static void setIteration(int iteration) {
		PlatformHelper.iteration = iteration;
	}

	/** The PLATFORM_SELENIUM. */
	protected static final String PLATFORM_SELENIUM = "Selenium";
	
	/**
	 * Sets the up platform.
	 *
	 * @param value the new up platform
	 * @throws ConfigurationException the configuration exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected static void commonSetup(String value) throws ConfigurationException, IOException {
		if (PLATFORM_SELENIUM.equalsIgnoreCase(value)) {
			platform = new SeleniumHelper();
			platform.commonSetup();
		} else {
			throw new ConfigurationException();
		}
	}
	
	// END - place to expand platform specific features

	// web api stuff ==========================================================

	/**
	 * Setup driver.
	 *
	 * @return the object
	 */
	public Object setupDriver() {
		setDriver(platform.setupDriver()); 
		return getDriver();
	}
	
	/**
	 * Driver switch to default content.
	 */
	public void driverSwitchToDefaultContent() {
		platform.driverSwitchToDefaultContent();
	}

	/**
	 * Driver switch to I frame.
	 *
	 * @param name the name
	 * @return true, if successful
	 */
	public boolean driverSwitchToIFrame(String name) {
		return platform.driverSwitchToIFrame(name);	
	}

	/**
	 * Gets the driver implicitly wait timout seconds.
	 *
	 * @return the driver implicitly wait timout seconds
	 */
	public long getDriverImplicitlyWaitTimoutSeconds() {
		return platform.getDriverImplicitlyWaitTimoutSeconds();
	}

	/**
	 * Driver implicitly wait.
	 *
	 * @param value the value
	 */
	public void setDriverImplicitlyWaitTimoutSeconds(long value) {
		platform.setDriverImplicitlyWaitTimoutSeconds(value);
	}

	/**
	 * Driver get.
	 *
	 * @param url the url
	 */
	public void driverGet(String url) {
		platform.driverGet(url);	
	}

	/**
	 * Close browser.
	 */
	public void closeBrowser() {
		platform.closeBrowser();
	}

	/**
	 * Click.
	 *
	 * @param locatorDelegate the locator delegate
	 */
	public void click(String locatorDelegate) {
		platform.click(locatorDelegate);	
	}

	/**
	 * Click by xpath.
	 *
	 * @param xpath the xpath
	 */
	public void clickByXpath(String xpath) {
		platform.clickByXpath(xpath);	
	}

	/**
	 * Input.
	 *
	 * @param locatorDelegate the locator delegate
	 * @param value the value
	 */
	public void input(String locatorDelegate, String value) {
		platform.input(locatorDelegate, value);
	}

	/**
	 * Input by xpath.
	 *
	 * @param xpath the xpath
	 * @param type the class name
	 * @param value the value
	 */
	public void inputByXpath(String xpath, String type, String value) {
		platform.inputByXpath(xpath, type, value);
	}

	/**
	 * Output.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the string
	 */
	public String output(String locatorDelegate) {
		return platform.output(locatorDelegate);	
	}

	/**
	 * Validate.
	 *
	 * @param condition the condition
	 * @param description the description
	 * @return true, if successful
	 */
	public boolean validate(boolean condition, String description) {
		return platform.validate(condition, description);
	}

	/**
	 * Exists.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return true, if successful
	 */
	public boolean exists(String locatorDelegate) {
		return platform.exists(locatorDelegate);	
	}
	
	/**
	 * Exists.
	 *
	 * @param locatorDelegate the locator delegate
	 * @param timeout the timeout
	 * @return true, if successful
	 */
	public boolean exists(String locatorDelegate, int timeout) {
		return platform.exists(locatorDelegate, timeout);	
	}

	/**
	 * Exists.
	 *
	 * @param locatorDelegate the locator delegate
	 * @param reportFailed the report failed
	 * @param timeout the timeout
	 * @return true, if successful
	 */
	public boolean exists(String locatorDelegate, boolean reportFailed, long timeout) {
		return platform.exists(locatorDelegate, reportFailed, timeout);	
	}

	/**
	 * Exists by xpath.
	 *
	 * @param xpath the xpath
	 * @return true, if successful
	 */
	public boolean existsByXpath(String xpath) {
		return existsByXpath(xpath, false);	
	}
	
	/**
	 * Exists by xpath.
	 *
	 * @param xpath the xpath
	 * @param reportFailed the report failed
	 * @return true, if successful
	 */
	public boolean existsByXpath(String xpath, boolean reportFailed) {
		return platform.existsByXpath(xpath, reportFailed);	
	}
	
	// report stuff ===========================================================

	/**
	 * Report create test.
	 *
	 * @param msg the msg
	 */
	public void reportCreateTest(String msg) {
		platform.reportCreateTest(msg);
	}

	/**
	 * Report end test.
	 *
	 * @param msg the msg
	 */
	@Override
	public void reportEndTest(String msg) {
		platform.reportEndTest(msg);
	}

	/**
	 * Test log info.
	 *
	 * @param msg the msg
	 */
	public void reportTestInfo(String msg) {
		platform.reportTestInfo(msg);
	}

	/**
	 * Test log pass.
	 *
	 * @param msg the msg
	 */
	public void reportTestPass(String msg) {
		platform.reportTestPass(msg);
	}

	/**
	 * Test log fail.
	 *
	 * @param msg the msg
	 */
	public void reportTestFail(String msg) {
		platform.reportTestFail(msg);
	}

	/**
	 * Test create step.
	 *
	 * @param msg the msg
	 */
	public void reportCreateStep(String msg) {
		platform.reportCreateStep(msg);
	}

	/**
	 * Report end step.
	 *
	 * @param msg the msg
	 */
	@Override
	public void reportEndStep(String msg) {
		platform.reportEndStep(msg);
	}

	/**
	 * Report step info.
	 *
	 * @param msg the msg
	 */
	@Override
	public void reportStepInfo(String msg) {
		platform.reportStepInfo(msg);
	}

	/**
	 * Step log pass.
	 *
	 * @param msg the msg
	 */
	public void reportStepPass(String msg) {
		platform.reportStepPass(msg);
	}

	/**
	 * Step log fail.
	 *
	 * @param msg the msg
	 */
	public void reportStepFail(String msg) {
		platform.reportStepFail(msg);
	}

	/**
	 * Screenshot step pass.
	 */
	public void reportStepPassScreenshot() {
		platform.reportStepPassScreenshot(platform.screenshotFile());
	}

	/**
	 * Screenshot step fail.
	 */
	public void reportStepFailScreenshot() {
		platform.reportStepFailScreenshot(platform.screenshotFile());
	}

	// other stuff ============================================================

	/**
	 * Gets the testdata.
	 *
	 * @param simpleName the simple name
	 * @return the testdata
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static List<Object[]> getTestdata(String simpleName) throws IOException {
		File file = platform.getTestDataFile();
		ExcelHelper xl = new ExcelHelper(file, simpleName);
		return xl.getData();
	}

	/**
	 * Gets the test data file.
	 *
	 * @return the test data file
	 */
	public File getTestDataFile() {
		return platform.getTestDataFile();	
	}
	
	/**
	 * Test platform properties get.
	 *
	 * @param key the key
	 * @return the string
	 */
	protected static String testPlatformPropertiesGet(String key) {
		String url = (String) PlatformInterface.testPlatformProperties.get(key);
		platform.reportTestInfo("Starting app: " + url);
		return url;	
	}
	
	/**
	 * Checks if is skipped.
	 *
	 * @param value the value
	 * @return true, if value.toLowerCase() is "true" or "ok" or "on"
	 */
	protected boolean isTrue(String value) {
		if (value == null || value.isEmpty()) {
			value = "false";
		}
		String[] values = {"true", "ok", "on"};
		return Arrays.stream(values).anyMatch(value.toLowerCase()::equals);
	}

	/**
	 * Click.
	 *
	 * @param locatorDelegate the locator delegate
	 * @param value the value
	 */
	@Override
	public void click(String locatorDelegate, String value) {
		platform.click(locatorDelegate, value);
	}

	/**
	 * Click.
	 *
	 * @param locatorDelegate the locator delegate
	 * @param timeout the timeout
	 */
	@Override
	public void click(String locatorDelegate, long timeout) {
		platform.click(locatorDelegate, timeout);
	}

	/**
	 * Click.
	 *
	 * @param locatorDelegate the locator delegate
	 * @param action the action
	 * @param timeout the timeout
	 */
	@Override
	public void click(String locatorDelegate, String action, long timeout) {
		platform.click(locatorDelegate, action, timeout);
	}

	/**
	 * Click by xpath.
	 *
	 * @param xpath the xpath
	 * @param value the value
	 */
	@Override
	public void clickByXpath(String xpath, String value) {
		platform.clickByXpath(xpath, value);
	}

	/**
	 * Click by xpath.
	 *
	 * @param xpath the xpath
	 * @param value the value
	 * @param timeout the timeout
	 */
	@Override
	public void clickByXpath(String xpath, String value, long timeout) {
		platform.clickByXpath(xpath, value, timeout);
	}

	/**
	 * Output by xpath.
	 *
	 * @param xpath the xpath
	 * @return the string
	 */
	@Override
	public String outputByXpath(String xpath) {
		return platform.outputByXpath(xpath);
	}

	/**
	 * Drag and drop.
	 *
	 * @param locatorFrom the locator from
	 * @param locatorTo the locator to
	 */
	@Override
	public void dragAndDrop(String locatorFrom, String locatorTo) {
		platform.dragAndDrop(locatorFrom, locatorTo);
	}

	/**
	 * Drag and drop by xpath.
	 *
	 * @param xpathFrom the xpath from
	 * @param xpathTo the xpath to
	 */
	@Override
	public void dragAndDropByXpath(String xpathFrom, String xpathTo) {
		platform.dragAndDropByXpath(xpathFrom, xpathTo);
	}

	/**
	 * Gets the mandant.
	 *
	 * @return the mandant
	 */
	@Override
	public String getMandant() {
		return platform.getMandant();
	}

	/**
	 * Gets the test environment.
	 *
	 * @return the test environment
	 */
	@Override
	public String getTestEnvironment() {
		return platform.getTestEnvironment();
	}

	/**
	 * Gets the produkt.
	 *
	 * @return the produkt
	 */
	@Override
	public String getProdukt() {
		return platform.getProdukt();
	}

	/**
	 * Common setup.
	 *
	 * @return the platform interface
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public PlatformInterface commonSetup() throws IOException {
		return platform.commonSetup();
	}

	/**
	 * Common teardown.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public void commonTeardown() throws IOException {
		platform.commonTeardown();
	}

	/**
	 * Report step pass screenshot.
	 *
	 * @param screenShot the screen shot
	 */
	@Override
	public void reportStepPassScreenshot(String screenShot) {
		platform.reportStepPassScreenshot(screenShot);
	}

	/**
	 * Report step fail screenshot.
	 *
	 * @param screenShot the screen shot
	 */
	@Override
	public void reportStepFailScreenshot(String screenShot) {
		platform.reportStepFailScreenshot(screenShot);
	}

	/**
	 * Screenshot file.
	 *
	 * @return the string
	 */
	@Override
	public String screenshotFile() {
		return platform.screenshotFile();
	}

	/**
	 * Wait until fully loaded.
	 *
	 * @param timeoutSeconds the timeout seconds
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public void waitUntilFullyLoaded(long timeoutSeconds) throws IOException {
		platform.waitUntilFullyLoaded(timeoutSeconds);
	}
	
	/**
	 * Scroll to bottom.
	 */
	public void scrollToBottom() {
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,document.body.scrollHeight)", "");
	}
	
	/**
	 * Wait.
	 *
	 * @param milliseconds the milliseconds
	 */
	public static void wait(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			Thread .currentThread().interrupt();
		}
		
	}

}
