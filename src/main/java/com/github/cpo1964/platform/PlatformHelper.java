/*
 * Copyright (C) 2023 Christian Pöcksteiner (christian.poecksteiner@aon.at)
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *         https://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.cpo1964.platform;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.naming.ConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriverException;

import com.github.cpo1964.platform.selenium.SeleniumHelper;
import com.github.cpo1964.utils.ExcelHelper;

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

	private static boolean runStatus = true;

	// START - place to expand platform specific features - eg Selenium:

	/**
	 * Checks if is run status.
	 *
	 * @return true, if is run status
	 */
	public static boolean isRunStatus() {
		return runStatus;
	}

	/**
	 * Sets the run status.
	 *
	 * @param value the new run status
	 */
	public static void setRunStatus(boolean value) {
		runStatus = value;
	}

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
	public static final String PLATFORM_SELENIUM = "Selenium";

	@Override
	public void setTestPlatformProperties(String value) throws IOException {
		// TODO Auto-generated method stub

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
	 * Sets the driver implicitly wait timout seconds.
	 *
	 * @param value the new driver implicitly wait timout seconds
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
		try {
			if (isRunStatus()) {
				platform.click(locatorDelegate);
			}
		} catch (Exception e) {
			log.error("setRunStatus(false) at CLICK '" + locatorDelegate + "'");
			setRunStatus(false);
		}
	}

	/**
	 * Click by xpath.
	 *
	 * @param xpath the xpath
	 */
	public void clickByXpath(String xpath) {
		try {
			if (isRunStatus()) {
				platform.clickByXpath(xpath);
			}
		} catch (Exception e) {
			log.error("setRunStatus(false) at CLICK '" + xpath + "'");
			setRunStatus(false);
		}
	}

	/**
	 * Input.
	 *
	 * @param locatorDelegate the locator delegate
	 * @param value           the value
	 */
	public void input(String locatorDelegate, String value) {
		try {
			if (isRunStatus()) {
				platform.input(locatorDelegate, value);
			}
		} catch (Exception e) {
			log.error("setRunStatus(false) at INPUT '" + locatorDelegate + "'");
			setRunStatus(false);
		}
	}

	/**
	 * Input by xpath.
	 *
	 * @param xpath the xpath
	 * @param type  the class name
	 * @param value the value
	 */
	public void inputByXpath(String xpath, String type, String value) {
		try {
			if (isRunStatus()) {
				platform.inputByXpath(xpath, type, value);
			}
		} catch (Exception e) {
			log.error("setRunStatus(false) at INPUT '" + xpath + "'");
			setRunStatus(false);
		}
	}

	/**
	 * Output.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the string
	 */
	public String output(String locatorDelegate) {
		try {
			if (isRunStatus()) {
				return platform.output(locatorDelegate);
			}
		} catch (Exception e) {
			log.error("setRunStatus(false) at OUTPUT '" + locatorDelegate + "'");
			setRunStatus(false);
		}
		return "";
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
	 * Validate.
	 *
	 * @param condition   the condition
	 * @param description the description
	 * @return true, if successful
	 */
	public boolean validate(boolean condition, String description) {
		try {
			if (isRunStatus()) {
				return platform.validate(condition, description);
			}
		} catch (Exception e) {
			log.error("setRunStatus(false) at VALIDATE '" + description + "'");
			setRunStatus(false);
		}
		return false;
	}

	/**
	 * Exists.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return true, if successful
	 */
	public boolean exists(String locatorDelegate) {
		try {
			if (isRunStatus()) {
				return platform.exists(locatorDelegate);
			}
		} catch (Exception e) {
			setRunStatus(false);
		}
		return false;
	}

	/**
	 * Exists.
	 *
	 * @param locatorDelegate the locator delegate
	 * @param timeout         the timeout
	 * @return true, if successful
	 */
	public boolean exists(String locatorDelegate, int timeout) {
		try {
			if (isRunStatus()) {
				return platform.exists(locatorDelegate, timeout);
			}
		} catch (Exception e) {
			log.error("setRunStatus(false) at EXISTS '" + locatorDelegate + "'");
			setRunStatus(false);
		}
		return false;
	}

	/**
	 * Exists.
	 *
	 * @param locatorDelegate the locator delegate
	 * @param reportFailed    the report failed
	 * @param timeout         the timeout
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
	 * @param xpath        the xpath
	 * @param reportFailed the report failed
	 * @return true, if successful
	 */
	public boolean existsByXpath(String xpath, boolean reportFailed) {
		try {
			if (isRunStatus()) {
				return platform.existsByXpath(xpath, reportFailed);
			}
		} catch (Exception e) {
			log.error("setRunStatus(false) at EXISTS '" + xpath + "'");
			setRunStatus(false);
		}
		return false;
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
		try {
			if (isRunStatus()) {
				platform.reportTestFail(msg);
			}
		} catch (Exception e) {
			log.error("setRunStatus(false) at reportTestFail '" + msg + "'");
			setRunStatus(false);
		}
	}

	/**
	 * Test create step.
	 *
	 * @param msg the msg
	 */
	public void reportCreateStep(String msg) {
		try {
			if (isRunStatus()) {
				platform.reportCreateStep(msg);
			}
		} catch (Exception e) {
			log.error("setRunStatus(false) at reportCreateStep '" + msg + "'");
			setRunStatus(false);
		}
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
		try {
			platform.reportStepPassScreenshot(platform.screenshotFile());
		} catch (WebDriverException e) {
			log.warn("reportStepPassScreenshot throws WebDriverException");
//			log.warn(e.getMessage());
		}
	}

	/**
	 * Screenshot step fail.
	 */
	public void reportStepFailScreenshot() {
		try {
			platform.reportStepFailScreenshot(platform.screenshotFile());
		} catch (WebDriverException e) {
			log.warn("reportStepPassScreenshot throws WebDriverException");
//			log.warn(e.getMessage());
		}
	}

	// other stuff ============================================================

	/**
	 * Gets the testdata.
	 *
	 * @param testDataPath the test data path
	 * @param simpleName the simple name
	 * @return the testdata
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static List<Object[]> getTestdata(String testDataPath, String simpleName) throws IOException {
		File file = new File(testDataPath + File.separator + TESTDATA_XLS);
		ExcelHelper xl = new ExcelHelper(file, simpleName);
		return xl.getData();
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
		String[] values = { "true", "ok", "on" };
		return Arrays.stream(values).anyMatch(value.toLowerCase()::equals);
	}

	/**
	 * Click.
	 *
	 * @param locatorDelegate the locator delegate
	 * @param clickAction     the value
	 */
	@Override
	public void click(String locatorDelegate, String clickAction) {
		try {
			if (isRunStatus()) {
				platform.click(locatorDelegate, clickAction);
			}
		} catch (Exception e) {
			setRunStatus(false);
		}
	}

	/**
	 * Click.
	 *
	 * @param locatorDelegate the locator delegate
	 * @param timeout         the timeout
	 */
	@Override
	public void click(String locatorDelegate, long timeout) {
		try {
			if (isRunStatus()) {
				platform.click(locatorDelegate, timeout);
			}
		} catch (Exception e) {
			setRunStatus(false);
		}
	}

	/**
	 * Click.
	 *
	 * @param locatorDelegate the locator delegate
	 * @param clickAction     the action
	 * @param timeout         the timeout
	 */
	@Override
	public void click(String locatorDelegate, String clickAction, long timeout) {
		try {
			if (isRunStatus()) {
				platform.click(locatorDelegate, clickAction, timeout);
			}
		} catch (Exception e) {
			setRunStatus(false);
		}
	}

	/**
	 * Click by xpath.
	 *
	 * @param xpath       the xpath
	 * @param clickAction the value
	 */
	@Override
	public void clickByXpath(String xpath, String clickAction) {
		try {
			if (isRunStatus()) {
				platform.clickByXpath(xpath, clickAction);
			}
		} catch (Exception e) {
			setRunStatus(false);
		}
	}

	/**
	 * Click by xpath.
	 *
	 * @param xpath       the xpath
	 * @param clickAction the value
	 * @param timeout     the timeout
	 */
	@Override
	public void clickByXpath(String xpath, String clickAction, long timeout) {
		try {
			if (isRunStatus()) {
				platform.clickByXpath(xpath, clickAction, timeout);
			}
		} catch (Exception e) {
			setRunStatus(false);
		}
	}

	/**
	 * Drag and drop.
	 *
	 * @param locatorFrom the locator from
	 * @param locatorTo   the locator to
	 */
	@Override
	public void dragAndDrop(String locatorFrom, String locatorTo) {
		platform.dragAndDrop(locatorFrom, locatorTo);
	}

	/**
	 * Drag and drop by xpath.
	 *
	 * @param xpathFrom the xpath from
	 * @param xpathTo   the xpath to
	 */
	@Override
	public void dragAndDropByXpath(String xpathFrom, String xpathTo) {
		platform.dragAndDropByXpath(xpathFrom, xpathTo);
	}

	/**
	 * Common setup.
	 *
	 * @param platformDelegate the platform delegate
	 * @param testDataPath the test data path
	 * @return the platform interface
	 * @throws ConfigurationException the configuration exception
	 */
	@Override
	public PlatformInterface commonSetup(String platformDelegate, String testDataPath) throws ConfigurationException {
		if (PLATFORM_SELENIUM.equalsIgnoreCase(platformDelegate)) {
			platform = new SeleniumHelper();
		} else {
			throw new ConfigurationException();
		}
		return platform;
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
			Thread.currentThread().interrupt();
		}

	}

}
