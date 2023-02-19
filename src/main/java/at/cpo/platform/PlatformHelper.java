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
import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import at.cpo.platform.selenium.SeleniumHelper;
import at.cpo.utils.ExcelHelper;


/**
 * The Class EnvironmentHelper.
 */
public class PlatformHelper implements PlatformInterface {

	/** The logger. */
	protected Logger LOGGER = LogManager.getLogger(this.getClass().getSimpleName());

	/** The platform. */
	static PlatformInterface platform;
	
//	static final PlatformHelper platformHelper = new PlatformHelper();
	
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
//			Class<?> c = ExtentHelper.getClassByQualifiedName("");
//			platform = platform.commonSetup(); // TODO
		} else {
			throw new RuntimeException();
		}
	}
	
	// END - place to expand platform specific features

	// web api stuff ==========================================================

	/**
	 * Setup driver.
	 */
	public void setupDriver() {
		platform.setupDriver();	
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
	 */
	public void validate(boolean condition, String description) {
		platform.validate(condition, description);
		Assert.assertTrue(condition);
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
		platform.reportStepPassScreenshot();
	}

	/**
	 * Screenshot step fail.
	 */
	public void reportStepFailScreenshot() {
		platform.reportStepFailScreenshot();
	}

	/**
	 * Tear down extent.
	 */
	public void reportTearDown() {
		platform.reportTearDown();	
	}

	/**
	 * Report teardown.
	 */
	public static void reportTeardown() {
		platform.reportTearDown();	
	}
	// logging stuff ==========================================================

//	/**
//	 * Log info.
//	 *
//	 * @param msg the msg
//	 */
//	public void logInfo(String msg) {
//		platform.logInfo(msg);
//	}
//
//	@Override
//	public void logDebug(String msg) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void logError(String msg) {
//		// TODO Auto-generated method stub
//	}
//
//	/**
//	 * Log all.
//	 */
//	public void logAll() {
//		platform.logAll();
//	}
//

	// other stuff ============================================================

	/**
	 * Gets the testdata.
	 *
	 * @param simpleName the simple name
	 * @return the testdata
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Collection<?> getTestdata(String simpleName) throws IOException {
		return new ExcelHelper(platform.getTestDataFile(), simpleName).getData();
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
		if (value == null || value.isEmpty() || value.isEmpty()) {
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
		// TODO Auto-generated method stub
		
	}

	/**
	 * Click by xpath.
	 *
	 * @param xpath the xpath
	 * @param value the value
	 */
	@Override
	public void clickByXpath(String xpath, String value) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Output by xpath.
	 *
	 * @param xpath the xpath
	 * @return the string
	 */
	@Override
	public String outputByXpath(String xpath) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Drag and drop.
	 *
	 * @param locatorFrom the locator from
	 * @param locatorTo the locator to
	 */
	@Override
	public void dragAndDrop(String locatorFrom, String locatorTo) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Drag and drop by xpath.
	 *
	 * @param xpathFrom the xpath from
	 * @param xpathTo the xpath to
	 */
	@Override
	public void dragAndDropByXpath(String xpathFrom, String xpathTo) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Gets the mandant.
	 *
	 * @return the mandant
	 */
	@Override
	public String getMandant() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Gets the test environment.
	 *
	 * @return the test environment
	 */
	@Override
	public String getTestEnvironment() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Gets the produkt.
	 *
	 * @return the produkt
	 */
	@Override
	public String getProdukt() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Common setup.
	 */
	@Override
	public SeleniumHelper commonSetup() {
		return null; // TODO
	}

}
