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

import at.cpo.platform.selenium.SeleniumHelper;
import at.cpo.utils.ExcelHelper;

/**
 * The Class EnvironmentHelper.
 */
public class PlatformHelper implements PlatformInterface {

	protected Logger LOGGER = LogManager.getLogger(this.getClass().getSimpleName());

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

	public void inputByXpath(String xpath, String className, String value) {
		platform.inputByXpath(xpath, className, value);
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
	 */
	public void validate(boolean condition, String description) {
		platform.validate(condition, description);	
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
	
	public boolean exists(String locatorDelegate, int timeout) {
		return platform.exists(locatorDelegate, timeout);	
	}

	public boolean exists(String locatorDelegate, boolean reportFailed, long timeout) {
		return platform.exists(locatorDelegate, reportFailed, timeout);	
	}

	public boolean existsByXpath(String xpath) {
		return existsByXpath(xpath, false);	
	}
	
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

	public void reportEndTest() {
		platform.reportEndTest();
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

	public static void reportTeardown() {
		new PlatformHelper().reportTearDown();	
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

	public static Collection<?> getTestdata(String simpleName) throws IOException {
		return new ExcelHelper(new PlatformHelper().getTestDataFile(), simpleName).getData();
	}

	/**
	 * Gets the test data file.
	 *
	 * @return the test data file
	 */
	public File getTestDataFile() {
		return platform.getTestDataFile();	
	}
	
	protected static String testPlatformPropertiesGet(String key) {
		String url = (String) PlatformInterface.testPlatformProperties.get(key);
		new PlatformHelper().reportTestPass("Starting app: " + url);
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

	@Override
	public void click(String locatorDelegate, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clickByXpath(String xpath, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String outputByXpath(String xpath) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void dragAndDrop(String locatorFrom, String locatorTo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragAndDropByXpath(String xpathFrom, String xpathTo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getMandant() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTestEnvironment() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProdukt() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void commonSetup() {
		// TODO Auto-generated method stub
		
	}

}
