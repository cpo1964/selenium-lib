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
import java.util.Arrays;

import at.cpo.platform.selenium.SeleniumHelper;

/**
 * The Class EnvironmentHelper.
 */
public class PlatformHelper {

	/** The platform. */
	static PlatformInterface platform;
	
	/** The value. */
	protected static String value;

	/** The ok. */
	protected boolean ok;
	
	// START - place to expand platform specific features - eg Selenium:

	/** The PLATFORM_SELENIUM. */
	protected static String PLATFORM_SELENIUM = "Selenium";
	
	/**
	 * Sets the up platform.
	 *
	 * @param value the new up platform
	 */
	protected static void commonSetup(String value) {
		if (PLATFORM_SELENIUM.equalsIgnoreCase(value)) {
			platform = new SeleniumHelper();
			platform.commonSetup();
		} else {
			throw new RuntimeException();
		}
	}
	
	// END - place to expand platform specific features

	// web api stuff ==========================================================

	/**
	 * Setup driver.
	 */
	protected void setupDriver() {
		platform.setupDriver();	
	}
	
	/**
	 * Driver switch to default content.
	 */
	protected void driverSwitchToDefaultContent() {
		platform.driverSwitchToDefaultContent();
	}

	/**
	 * Driver switch to I frame.
	 *
	 * @param name the name
	 * @return true, if successful
	 */
	protected boolean driverSwitchToIFrame(String name) {
		return platform.driverSwitchToIFrame(name);	
	}

	/**
	 * Driver implicitly wait.
	 *
	 * @param value the value
	 */
	public void driverImplicitlyWait(long value) {
		platform.driverImplicitlyWait(value);
	}

	/**
	 * Driver get.
	 *
	 * @param url the url
	 */
	protected void driverGet(String url) {
		platform.driverGet(url);	
	}

	/**
	 * Close browser.
	 */
	protected void closeBrowser() {
		platform.closeBrowser();
	}

	/**
	 * Click.
	 *
	 * @param locatorDelegate the locator delegate
	 */
	protected void click(String locatorDelegate) {
		platform.click(locatorDelegate);	
	}

	/**
	 * Input.
	 *
	 * @param locatorDelegate the locator delegate
	 * @param value the value
	 */
	protected void input(String locatorDelegate, String value) {
		platform.input(locatorDelegate, value);
	}

	/**
	 * Output.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the string
	 */
	protected String output(String locatorDelegate) {
		return platform.output(locatorDelegate);	
	}

	/**
	 * Validate.
	 */
	protected void validate(boolean condition, String description) {
		platform.validate(condition, description);	
	}

	/**
	 * Exists.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return true, if successful
	 */
	protected boolean exists(String locatorDelegate) {
		return platform.exists(locatorDelegate);	
	}
	
	// report stuff ===========================================================

	/**
	 * Report create test.
	 *
	 * @param msg the msg
	 */
	protected void reportCreateTest(String msg) {
		platform.reportCreateTest(msg);
	}

	/**
	 * Test log pass.
	 *
	 * @param msg the msg
	 */
	protected void reportTestPass(String msg) {
		platform.reportTestPass(msg);
	}

	/**
	 * Test log fail.
	 *
	 * @param msg the msg
	 */
	protected void reportTestFail(String msg) {
		platform.reportTestFail(msg);
	}

	/**
	 * Test create step.
	 *
	 * @param msg the msg
	 */
	protected void reportCreateStep(String msg) {
		platform.reportCreateStep(msg);
	}

	/**
	 * Step log pass.
	 *
	 * @param msg the msg
	 */
	protected void reportStepPass(String msg) {
		platform.reportStepPass(msg);
	}

	/**
	 * Step log fail.
	 *
	 * @param msg the msg
	 */
	protected void reportStepFail(String msg) {
		platform.reportStepFail(msg);
	}

	/**
	 * Screenshot step pass.
	 */
	protected void reportStepPassScreenshot() {
		platform.reportStepPassScreenshot();
	}

	/**
	 * Screenshot step fail.
	 */
	protected void reportStepFailScreenshot() {
		platform.reportStepFailScreenshot();
	}

	/**
	 * Tear down extent.
	 */
	protected static void reportTearDown() {
		platform.reportTearDown();	
	}

	// logging stuff ==========================================================

	/**
	 * Log info.
	 *
	 * @param msg the msg
	 */
	protected void logInfo(String msg) {
		platform.logInfo(msg);
	}

	/**
	 * Log all.
	 */
	protected void logAll() {
		platform.logAll();
	}

	// other stuff ============================================================

	/**
	 * Gets the test data file.
	 *
	 * @return the test data file
	 */
	protected static File getTestDataFile() {
		return platform.getTestDataFile();	
	}
	
	protected static String testPlatformPropertiesGet(String key) {
		return (String) PlatformInterface.testPlatformProperties.get(key);	
	}
	
	/**
	 * Checks if is skipped.
	 *
	 * @param value the value
	 * @return true, if value.toLowerCase() is "true" or "ok" or "on"
	 */
	protected boolean isSkipped(String value) {
		String[] skips = {"true", "ok", "on"};
		return Arrays.stream(skips).anyMatch(value.toLowerCase()::equals);
	}

}
