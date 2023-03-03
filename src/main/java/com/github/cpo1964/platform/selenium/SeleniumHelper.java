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
package com.github.cpo1964.platform.selenium;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.naming.ConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.github.cpo1964.report.extent.ExtentHelper;
import com.github.cpo1964.utils.CommonHelper;
import com.github.cpo1964.utils.ExcelHelper;

/**
 * The Class SeleniumHelper.
 */
public class SeleniumHelper extends ExtentHelper implements SeleniumInterface {

	/** The logger. */
	static Logger logSelenium = LogManager.getLogger(SeleniumHelper.class.getSimpleName());
	
	/** The Constant WDM_CACHE_PATH. */
	private static final String WDM_CACHE_PATH = "wdm.cachePath";

	/** The Constant VALUE2. */
	private static final String VALUE2 = "\"), value: '";

	/** The Constant BOLD_INPUT_BY_XPATH. */
	private static final String BOLD_INPUT_BY_XPATH = "<b>INPUT   </b> by xpath $(\"";

	/** The name. */
	private String name = "";

	/** The failed. */
	private boolean failed;

	/** The value. */
	protected static String value;

	/** The driver. */
	private static RemoteWebDriver driver;

	/** The web element. */
	private static WebElement webElement;

	/** The test data path. */
	private String testDataPath;

	/** The proxy. */
	private static String proxy;

	/** The proxy user. */
	private static String proxyUser;

	/** The proxy pass. */
	private static String proxyPass;

	/** The driver loaded. */
	private static boolean driverLoaded = false;
	
	/** The ok. */
	protected boolean ok;

	/** The test platform properties. */
	public static final Properties testPlatformProperties = new Properties();

	/** The iteration. */
	private static int iteration = 0;

	/** The run status. */
	private static boolean runStatus = true;

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
	 * @param value the new iteration
	 */
	public static void setIteration(int value) {
		iteration = value;
	}

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
		logSelenium.info("setRunStatus: " + value);
		runStatus = value;
	}

	/**
	 * Checks if is failed.
	 *
	 * @return true, if is failed
	 */
	public boolean isFailed() {
		return failed;
	}

	/**
	 * Sets the failed.
	 *
	 * @param failed the new failed
	 */
	public void setFailed(boolean failed) {
		this.failed = failed;
	}

	/**
	 * Gets the driver.
	 *
	 * @return the driver
	 */
	public static RemoteWebDriver getDriver() {
		return driver;
	}

	/**
	 * Sets the driver.
	 *
	 * @param driver the new driver
	 */
	public static void setDriver(RemoteWebDriver driver) {
		SeleniumHelper.driver = driver;
	}

	/**
	 * Gets the web element.
	 *
	 * @return the web element
	 */
	public static WebElement getWebElement() {
		return webElement;
	}

	/**
	 * Sets the web element.
	 *
	 * @param webEl the new web element
	 */
	public static void setWebElement(WebElement webEl) {
		webElement = webEl;
	}

	/**
	 * Gets the test data path.
	 *
	 * @return the test data path
	 */
	public String getTestDataPath() {
		return testDataPath;
	}

	/**
	 * Sets the test data path.
	 *
	 * @param testDataPath the new test data path
	 */
	public void setTestDataPath(String testDataPath) {
		this.testDataPath = testDataPath;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Checks if is driver loaded.
	 *
	 * @return true, if is driver loaded
	 */
	public static boolean isDriverLoaded() {
		return driverLoaded;
	}

	/**
	 * Sets the driver loaded.
	 *
	 * @param driverLoaded the new driver loaded
	 */
	public static void setDriverLoaded(boolean driverLoaded) {
		SeleniumHelper.driverLoaded = driverLoaded;
	}

	/**
	 * Gets the browser.
	 *
	 * @return the browser
	 */
	public static String getBrowser() {
		String browser = System.getProperty("browser");
		if (browser == null || browser.isEmpty()) {
			browser ="firefox";
		}
		return browser;
	}

	/**
	 * Driver get.
	 *
	 * @param url the url
	 */
	@Override
	public void driverGet(String url) {
		getDriver().get(url);
	}

	/**
	 * Gets the proxy.
	 *
	 * @return the proxy
	 */
	public static String getProxy() {
		return proxy;
	}

	/**
	 * Sets the proxy.
	 *
	 * @param proxy the new proxy
	 */
	public static void setProxy(String proxy) {
		SeleniumHelper.proxy = proxy;
	}

	/**
	 * Gets the proxy user.
	 *
	 * @return the proxy user
	 */
	public static String getProxyUser() {
		return proxyUser;
	}

	/**
	 * Sets the proxy user.
	 *
	 * @param proxyUser the new proxy user
	 */
	public static void setProxyUser(String proxyUser) {
		SeleniumHelper.proxyUser = proxyUser;
	}

	/**
	 * Gets the proxy pass.
	 *
	 * @return the proxy pass
	 */
	public static String getProxyPass() {
		return proxyPass;
	}

	/**
	 * Sets the proxy pass.
	 *
	 * @param proxyPass the new proxy pass
	 */
	public static void setProxyPass(String proxyPass) {
		SeleniumHelper.proxyPass = proxyPass;
	}

	/**
	 * Driver switch to I frame.
	 * 
	 * Send future commands to iFrame
	 *
	 * @param name the name
	 * @return true, if successful
	 */
	@Override
	public boolean driverSwitchToIFrame(String name) {
		List<WebElement> iframes = getDriver().findElements(By.tagName("iframe"));
		if (!iframes.isEmpty()) {
			getDriver().switchTo().frame(name);
			return true;
		}
		return false;
	}

	/**
	 * Driver switch to default content.
	 * 
	 * Selects either the first frame on the page, or the main document when a page contains iframes.
	 */
	@Override
	public void driverSwitchToDefaultContent() {
		getDriver().switchTo().defaultContent();
	}

	/**
	 * Gets the driver implicitly wait timout seconds.
	 *
	 * @return the driver implicitly wait timout seconds
	 */
	@Override
	public long getDriverImplicitlyWaitTimoutSeconds() {
		return getDriver().manage().timeouts().getImplicitWaitTimeout().toMillis();
	}

	/**
	 * Driver implicitly wait.
	 *
	 * @param value the value
	 */
	@Override
	public void setDriverImplicitlyWaitTimoutSeconds(long value) {
		getDriver().manage().timeouts().implicitlyWait(Duration.ofMillis(value));
	}

	/**
	 * Setup driver.
	 * 
	 * see:
	 * https://github.com/bharadwaj-pendyala/selenium-java-lean-test-achitecture
	 * https://github.com/bharadwaj-pendyala/selenium-java-lean-test-achitecture/blob/master/src/main/java/driver/local/LocalDriverManager.java
	 *
	 * @return the object
	 */
	@Override
	public Object setupDriver() {
		// supress all java.util.logging messages
		java.util.logging.LogManager.getLogManager().reset();
		java.util.logging.Logger globalLogger = java.util.logging.Logger.getLogger(java.util.logging.Logger.GLOBAL_LOGGER_NAME);
		globalLogger.setLevel(java.util.logging.Level.OFF);

		System.setProperty(WDM_CACHE_PATH, "src/test/resources");
		String wdmCachePath = System.getProperty(WDM_CACHE_PATH);
		if (wdmCachePath != null && !wdmCachePath.isEmpty()) {
			System.setProperty(WDM_CACHE_PATH, wdmCachePath);
		}
		setProxy(System.getProperty("proxy"));
		setProxyUser(System.getProperty("proxyUser"));
		setProxyPass(System.getProperty("proxyPass"));

		if ("chrome".equalsIgnoreCase(getBrowser())) {
			setupChromeDriver();
			if (!isDriverLoaded()) {
				logSelenium.info(() -> "using local chromedriver: " + System.getProperty("webdriver.chrome.driver") + System.lineSeparator());
				setDriverLoaded(true);
			}
		} else if ("firefox".equalsIgnoreCase(getBrowser())) {
			setupFirefoxDriver();
			if (!isDriverLoaded()) {
				logSelenium.info(() -> "using local geckodriver: " + System.getProperty("webdriver.gecko.driver") + System.lineSeparator());
				setDriverLoaded(true);
			}
		}
		setDriverImplicitlyWaitTimoutSeconds(30);
		return getDriver();
	}

	/**
	 * Setup chrome driver.
	 */
	private static void setupChromeDriver() {
		
//		System.setProperty("webdriver.chrome.silentLogging", "true");
//		System.setProperty("webdriver.chrome.verboseLogging", "false");
//		System.setProperty("webdriver.chrome.silentOutput", "true");

		if (getDriver() == null) {
			if (getProxyUser() != null && !getProxyUser().isEmpty()) {
				io.github.bonigarcia.wdm.WebDriverManager.chromedriver().proxyUser(getProxyUser()).proxyPass(getProxyPass()).proxy(getProxy()).setup();
			} else {
				io.github.bonigarcia.wdm.WebDriverManager.chromedriver().setup();
			}
		}

		ChromeOptions chromeOptions = new ChromeOptions();
		if ("true".equalsIgnoreCase(System.getProperty("headless"))) {
			chromeOptions.setHeadless(true);
		}

		// start chrome with empty tab
		setDriver(new ChromeDriver(chromeOptions));
	}

	/**
	 * Setup firefox driver.
	 */
	protected static void setupFirefoxDriver() {
		if (getDriver() == null) {
			if (getProxyUser() != null && !getProxyUser().isEmpty()) {
				io.github.bonigarcia.wdm.WebDriverManager.firefoxdriver().proxyUser(getProxyUser()).proxyPass(getProxyPass()).proxy(getProxy()).setup();
			} else {
				io.github.bonigarcia.wdm.WebDriverManager.firefoxdriver().setup();
			}
		}
		System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "FFLogs.txt");

		if ("true".equalsIgnoreCase(System.getProperty("headless"))) {
			FirefoxBinary firefoxBinary = new FirefoxBinary();
		    firefoxBinary.addCommandLineOptions("--headless");
		    FirefoxOptions firefoxOptions = new FirefoxOptions();
		    FirefoxProfile profile = new FirefoxProfile();
			firefoxOptions.setProfile(profile );
		    profile.setPreference("privacy.trackingprotection.enabled", false);
		    firefoxOptions.setBinary(firefoxBinary);
			// start firefox with empty tab
		    setDriver(new FirefoxDriver(firefoxOptions));
		} else {
			// start firefox with epmty tab
			setDriver(new FirefoxDriver());
		}
	}

	/**
	 * Close browser.
	 */
	public void closeBrowser() {
		try {
			if (getDriver() != null) {
				getDriver().quit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Exists by xpath.
	 *
	 * @param xpath the xpath
	 * @param timeout the timeout
	 * @return true, if successful
	 */
	@Override
	public boolean existsByXpath(String xpath, long timeout) {
		setWebElement(null);
		long oldTimeout = getDriverImplicitlyWaitTimoutSeconds();
		setDriverImplicitlyWaitTimoutSeconds(timeout);
		setWebElement(getByXpath(xpath, timeout));
		setDriverImplicitlyWaitTimoutSeconds(oldTimeout);
		return getWebElement() != null;
	}

	private static WebElement getByXpath(String xpath) {
		return getByXpath(xpath, getDriver().manage().timeouts().getImplicitWaitTimeout().toMillis());
	}
	/**
	 * Gets the by xpath.
	 *
	 * @param xpath the xpath
	 * @param timeout the timeout
	 * @return the WebElement
	 */
	private static WebElement getByXpath(String xpath, long timeout) {
		WebElement webEl = null;
		List<WebElement> webEls = getDriver().findElements(By.xpath(xpath));
		// one or more Webelements found in ImplicitlyWaitTimout
		if (!webEls.isEmpty()) {
			webEl = webEls.get(0);
		}
		return webEl;
	}

	/**
	 * Checks if is clickable.
	 * 
	 * An expectation for checking an element is visible and enabled such that you can click it.
	 *
	 * @param xpath the xpath
	 * @param timeout the timeout
	 * @return true, if is clickable
	 */
	@Override
	public boolean isClickableByXpath(String xpath, long timeout) {
		WebElement webEl = getByXpath(xpath, timeout);
		if (webEl == null) {
			return false;
		}
		// wait timeout for first Webelement to be clickable
		WebDriverWait wa = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
		wa.until(ExpectedConditions.elementToBeClickable(webEl));
		return webEl.isEnabled();
	}

	/**
	 * Checks if is clickable by xpath.
	 *
	 * An expectation for checking an element is visible and enabled such that you can click it.
	 * Uses default timeout
	 *
	 * @param xpath the xpath
	 * @return true, if is clickable by xpath
	 */
	@Override
	public boolean isClickableByXpath(String xpath) {
		return isClickableByXpath(xpath, getDriver().manage().timeouts().getImplicitWaitTimeout().getSeconds());
	}

	/**
	 * Checks if is clickable.
	 * 
	 * An expectation for checking an element is visible and enabled such that you can click it.
	 *
	 * @param xpath the xpath
	 * @return true, if is clickable
	 */
	@Override
	public boolean isClickable(String locatorDelegate, long timeout) {
		String xpath = getLocator(locatorDelegate);
		return isClickable(xpath, getDriver().manage().timeouts().getImplicitWaitTimeout().getSeconds());
	}

	/**
	 * Checks if is clickable.
	 *
	 * An expectation for checking an element is visible and enabled such that you can click it.
	 * Uses default timeout
	 *
	 * @param locatorDelegate the locator delegate
	 * @return true, if is clickable
	 */
	@Override
	public boolean isClickable(String locatorDelegate) {
		return isClickable(locatorDelegate, getDriver().manage().timeouts().getImplicitWaitTimeout().getSeconds());
	}

	/**
	 * Checks if is displayed.
	 * 
	 * An expectation for checking that an element is present on the DOM of a page and visible.
	 * Visibility means that the element is not only displayed but also has a height and width that isgreater than 0.
	 *
	 * @param xpath the xpath
	 * @param timeout the timeout
	 * @return true, if is displayed
	 */
	@Override
	public boolean isDisplayed(String xpath, long timeout) {
		// wait timeout for first Webelement to be clickable
		WebDriverWait wa = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
		WebElement webEl = wa.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
		if (webEl == null) {
			return false;
		}
		return webEl.isDisplayed();
	}

	/**
	 * Checks if is selected.
	 * 
	 * An expectation for checking if the given element is selected.
	 *
	 * @param xpath the xpath
	 * @param timeout the timeout
	 * @return true, if is selected
	 */
	@Override
	public boolean isSelected(String xpath, long timeout) {
		// wait timeout for first Webelement to be clickable
		WebDriverWait wa = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
		return wa.until(ExpectedConditions.elementToBeSelected(By.xpath(xpath)));
	}

	/**
	 * Exists by xpath.
	 *
	 * @param xpath the xpath
	 * @return true, if successful
	 */
	@Override
	public boolean existsByXpath(String xpath) {
		return existsByXpath(xpath, getDriver().manage().timeouts().getImplicitWaitTimeout().getSeconds());
	}

	/**
	 * Exists.
	 *
	 * @param locatorDelegate the locator delegate
	 * @param reportFailed    the reportFailed
	 * @return true, if successful
	 */
	@Override
	public boolean exists(String locatorDelegate, boolean reportFailed) {
		String xpath = getLocator(locatorDelegate);
		return existsByXpath(xpath, reportFailed);
	}

	/**
	 * Exists.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return true, if successful
	 */
	@Override
	public boolean exists(String locatorDelegate) {
		return exists(locatorDelegate, false);
	}

	/**
	 * Exists.
	 *
	 * @param locatorDelegate the locator delegate
	 * @param reportFailed the report failed
	 * @param timeout the timeout
	 * @return true, if successful
	 */
	@Override
	public boolean exists(String locatorDelegate, boolean reportFailed, long timeout) {
		String xpath = getLocator(locatorDelegate);
		return existsByXpath(xpath, reportFailed, timeout);
	}

	/**
	 * Exists.
	 *
	 * @param locatorDelegate the locator delegate
	 * @param timeout the timeout
	 * @return true, if successful
	 */
	@Override
	public boolean exists(String locatorDelegate, int timeout) {
		return exists(locatorDelegate, false, timeout);
	}


	/**
	 * Exists.
	 *
	 * @param xpath the xpath
	 * @param reportFailed    the reportFailed
	 * @return true, if successful
	 */
	@Override
	public boolean existsByXpath(String xpath, boolean reportFailed) {
		return existsByXpath(xpath, reportFailed, getDriver().manage().timeouts().getImplicitWaitTimeout().getSeconds());
	}
	
	/**
	 * Exists by xpath.
	 *
	 * @param xpath the xpath
	 * @param reportFailed the report failed
	 * @param timeout the timeout
	 * @return true, if successful
	 */
	@Override
	public boolean existsByXpath(String xpath, boolean reportFailed, long timeout) {
		setExistsCount(getExistsCount() + 1);
		boolean exists = existsByXpath(xpath, timeout);
		if (!exists)  {
			if (reportFailed) {
				reportStepFail("<b>EXISTS  </b> by xpath $(\"" + xpath + "\") - false");
			} else {
				reportStepPass("<b>EXISTS  </b> by xpath $(\"" + xpath + "\") - false");
			}
			return false;
		}
		reportStepPass("<b>EXISTS  </b> by xpath $(\"" + xpath + "\") - true");
		return exists;
	}

	/**
	 * Click by xpath.
	 *
	 * @param xpath the xpath
	 * @param clickAction the value
	 * @param timeout the timeout
	 */
	@Override
	public void clickByXpath(String xpath, String clickAction, long timeout) {
		existsByXpath(xpath, timeout);
		clickByXpath(xpath, clickAction);
	}

	/**
	 * Click by xpath.
	 *
	 * @param xpath the xpath
	 * @param clickAction the value
	 */
	@Override
	public void clickByXpath(String xpath, String clickAction) {
		setClicksCount(getClicksCount() + 1);
		setWebElement(getByXpath(xpath));
		if (isClickableByXpath(xpath)) {
			if (SeleniumStrings.CLICKKEY.equals(clickAction)) {
				Actions actions = new Actions(getDriver());
				actions.moveToElement(getWebElement()).click().build().perform();
			} else {
				// The user-facing API for emulating complex user gestures. 
				// Use this class rather than using the Keyboard or Mouse directly
				Actions action = new Actions(getDriver());
				if (SeleniumStrings.RIGHTCLICK.equals(clickAction)) {
					// context-click at middle of the given element
					action.contextClick(getWebElement()).perform();
				} else if (SeleniumStrings.ALTCLICK.equals(clickAction)) {
					action
			        .keyDown(Keys.ALT)
			        .click(getWebElement())
			        .keyUp(Keys.ALT)
			        .perform();
				} else if (SeleniumStrings.CONTROLCLICK.equals(clickAction)) {
					action
			        .keyDown(Keys.CONTROL)
			        .click(getWebElement())
			        .keyUp(Keys.CONTROL)
			        .perform();
				} else if (SeleniumStrings.DOUBLECLICK.equals(clickAction)) {
					action.doubleClick(getWebElement()).perform();
				} else if (SeleniumStrings.LONGCLICK.equals(clickAction)) {
					// Clicks (without releasing) in the middle of the given element
					action.clickAndHold(getWebElement()).perform();
					wait(2000);
					// Releases the depressed left mouse button, in the middle of the given element
					action.release(getWebElement()).perform();
				} else if (SeleniumStrings.MOUSEOVER.equals(clickAction)) {
					action.moveToElement(getWebElement()).build().perform();
				} else if (SeleniumStrings.SHIFTCLICK.equals(clickAction)) {
					action
			        .keyDown(Keys.SHIFT)
			        .click(getWebElement())
			        .keyUp(Keys.SHIFT)
			        .perform();
				}
			}
			reportStepPass("<b>CLICK   </b> by xpath $(\"" + xpath + "\")");
		} else {
			reportStepFailScreenshot(screenshotFile());
			throw new NoSuchElementException("CLICK   by xpath $(" + xpath + ") failed");
		}
	}

	/**
	 * Click by xpath.
	 *
	 * @param xpath the xpath
	 */
	@Override
	public void clickByXpath(String xpath) {
		clickByXpath(xpath, SeleniumStrings.CLICKKEY);
	}

	/**
	 * Click.
	 *
	 * @param locatorDelegate the locator delegate
	 * @param clickAction the action
	 */
	@Override
	public void click(String locatorDelegate, String clickAction) {
		String xpath = getLocator(locatorDelegate);
		clickByXpath(xpath, clickAction);
	}

	/**
	 * Click.
	 *
	 * @param locatorDelegate the locator delegate
	 */
	@Override
	public void click(String locatorDelegate) {
		click(locatorDelegate, SeleniumStrings.CLICKKEY);
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
		String xpath = getLocator(locatorDelegate);
		clickByXpath(xpath, action, timeout);
	}

	/**
	 * Click.
	 *
	 * @param locatorDelegate the locator delegate
	 * @param timeout the timeout
	 */
	@Override
	public void click(String locatorDelegate, long timeout) {
		click(locatorDelegate, SeleniumStrings.CLICKKEY, timeout);
	}

	/**
	 * Input.
	 * 
	 * supported types:
	 *     input type="text
	 *     input type="radio
	 *     input type="number
	 *     input type="file
	 *     input type="checkbox
	 *     input type="range // vulgo 'slider'
	 *
	 * @param xpath the xpath
	 * @param type the class name
	 * @param value           the value
	 * @param secret          the secret
	 */
	public void inputByXpath(String xpath, String type, String value, boolean secret) {
		setInputsCount(getInputsCount() + 1);
		setWebElement(null);
		try {
			if (type != null) {
				setWebElement(getByXpath(xpath, 30));
			} else {
					logSecret(xpath + "(unknown) -> not done ", CommonHelper.getSecretString(value), secret);
					try {
						reportStepFailScreenshot(screenshotFile());
					} catch (WebDriverException ew) {
						reportStepFail("type of webelement unknown: '" + type + "'");
						throw new NotFoundException("type of webelement unknown: '" + type + "'");
					}
					return;
			}
			if (SeleniumStrings.EDITFIELD.equalsIgnoreCase(type)
					|| SeleniumStrings.NUMERICFIELD.equalsIgnoreCase(type)
					|| SeleniumStrings.SLIDER.equalsIgnoreCase(type) // type='range'
					|| SeleniumStrings.FILEFIELD.equalsIgnoreCase(type)) {
				Actions actions = new Actions(getDriver());
				actions.moveToElement(getWebElement()).click().build().perform();
				getWebElement().clear();
				getWebElement().sendKeys(value);
				reportStepPass(BOLD_INPUT_BY_XPATH + xpath + VALUE2 + CommonHelper.getSecretString(value) + "'");
				logSecret(xpath, value, secret);
			} else  if (SeleniumStrings.LISTBOX.equalsIgnoreCase(type)) {
				new Select(getWebElement()).selectByVisibleText(value);
				reportStepPass(BOLD_INPUT_BY_XPATH + xpath + VALUE2 + value + "'");
			} else if (SeleniumStrings.CHECKBOX.equalsIgnoreCase(type)
					|| SeleniumStrings.RADIOBUTTON.equalsIgnoreCase(type)) {
				if (getWebElement().isSelected() && "OFF".equalsIgnoreCase(value) ||
						!getWebElement().isSelected() && "ON".equalsIgnoreCase(value)) {
					getWebElement().click();
					reportStepPass(BOLD_INPUT_BY_XPATH + xpath + VALUE2 + value + "'");
				} else {
					throw new NotFoundException(BOLD_INPUT_BY_XPATH + xpath + "\"), value not found: '" + value + "'");
				}
			} else if (SeleniumStrings.RADIOGROUP.equalsIgnoreCase(type)) {
				int option = Integer.parseInt(value);
				List<WebElement> radios = getDriver().findElements(By.xpath(xpath));
				if (option > 0 && option <= radios.size()) {
					radios.get(option - 1).click();
					reportStepPass(BOLD_INPUT_BY_XPATH + xpath + VALUE2 + value + "'");
				} else {
					throw new NotFoundException(BOLD_INPUT_BY_XPATH + xpath + "\"), value not found: '" + value + "'");
				}
			} else {
				throw new NotFoundException("type of webelement unknown: '" + type + "'");
			}
		} catch (IOException e) {
			reportStepFailScreenshot(screenshotFile());
			reportStepFail("<b>INPUT   </b> (" + xpath + ", '" + CommonHelper.getSecretString(value) + ")'");
		}
	}

	/**
	 * Input by xpath.
	 *
	 * @param xpath the xpath
	 * @param className the class name
	 * @param value the value
	 */
	@Override
	public void inputByXpath(String xpath, String type, String value) {
		inputByXpath(xpath, type, value, false);
	}

	/**
	 * Input.
	 * 
	 * If secret=true the input value will be masked in report like '*****'
	 *
	 * @param locatorDelegate the locator delegate
	 * @param value the value
	 * @param secret the secret
	 */
	@Override
	public void input(String locatorDelegate, String value, boolean secret) {
		String xpath = getLocator(locatorDelegate); // expected: a xpath from the property file
		String type = "";
		String[] descParts = locatorDelegate.split(File.pathSeparator);
		type = descParts[1];
		inputByXpath(xpath, type, value, secret);
	}
	
	/**
	 * Input.
	 *
	 * @param locatorDelegate the locator delegate
	 * @param value           the value
	 */
	@Override
	public void input(String locatorDelegate, String value) {
		input(locatorDelegate, value, false);
	}

	/**
	 * Output by xpath.
	 *
	 * @param xpath the xpath
	 * @return the string
	 */
	@Override
	public String outputByXpath(String xpath) {
		setOutputsCount(getOutputsCount() + 1);
		setWebElement(getDriver().findElement(By.xpath(xpath)));
		String output = getWebElement().getAttribute("textContent");
		reportStepPass("<b>OUTPUT   </b> by xpath $(\"" + xpath + "\")<br>text: '" + output + "'");
		return output;
	}

	/**
	 * Output.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the string
	 */
	@Override
	public String output(String locatorDelegate) {
		String xpath = getLocator(locatorDelegate);
		return outputByXpath(xpath);
	}

	/**
	 * Validate.
	 *
	 * @param condition the condition
	 * @param description the description
	 * @return true, if successful
	 */
	@Override
	public boolean validate(boolean condition, String description) {
		if (condition) {
			reportStepPass("<b>VALIDATE</b> '" + description + "' - " + condition);
		} else {
			reportStepFail("<b>VALIDATE</b> '" + description + "' - " + condition);
			try {
				reportStepFailScreenshot(screenshotFile());
			} catch (WebDriverException e) {
				reportStepFail("<b>VALIDATE</b> '" + description + "' - " + condition);
			}
		}
		return condition;
	}

	/**
	 * Drag and drop.
	 *
	 * @param locatorFrom the locator from
	 * @param locatorTo the locator to
	 */
	@Override
	public void dragAndDrop(String locatorFrom, String locatorTo) {
		 // expected: xpath from the property file
		String xpathFrom = getLocator(locatorFrom);
		String xpathTo = getLocator(locatorTo);
		dragAndDropByXpath(xpathFrom, xpathTo);
	}

	/**
	 * Drag and drop by xpath.
	 *
	 * @param xpathFrom the xpath from
	 * @param xpathTo the xpath to
	 */
	@Override
	public void dragAndDropByXpath(String xpathFrom, String xpathTo) {
		WebElement webElFrom = getDriver().findElement(By.xpath(xpathFrom));	
		WebElement webElTo = getDriver().findElement(By.xpath(xpathTo));	
		// see: https://www.selenium.dev/documentation/webdriver/actions_api/mouse/
		new Actions(getDriver())
        .dragAndDrop(webElFrom, webElTo)
        .perform();	
	}

	/**
	 * Log secret.
	 * 
	 * If secret==true the masked value will be logged
	 *
	 * @param locatorDelegate the locator delegate
	 * @param value  the value
	 * @param secret the secret
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void logSecret(String locatorDelegate, String value, boolean secret) throws IOException {
		String text = value;
		if (secret) {
			text = CommonHelper.getSecretString(value);
		}
		logSelenium.debug("<b>input</b> by xpath $(\"" + locatorDelegate + "\"), value=" + text);
	}

	/**
	 * Wait until fully loaded.
	 *
	 * @param timeoutSeconds the timeout seconds
	 */
	@Override
	public void waitUntilFullyLoaded(int timeoutSeconds) {
		WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeoutSeconds));
		wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState")
				.equals("complete"));
	}

	/**
	 * Gets the locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the locator
	 */
	private String getLocator(String locatorDelegate) {
		String[] locatorDelegateSplit = locatorDelegate.split(File.pathSeparator);
		if (locatorDelegateSplit.length != 3) {
			String errMsg = "locatorDelegate must match pattern 'classname" + 
					File.pathSeparator + "locatortype" + File.pathSeparator + "locatorDelegate': '" + locatorDelegate + "'";
			reportStepFail(errMsg);
			throw new NotFoundException(errMsg );
		}
		String cn = locatorDelegateSplit[0];
		String key = locatorDelegateSplit[2];
		Class<?> c = null;
		c = getClassByQualifiedName(cn);
		if (c == null) {
			throw new NotFoundException("class not found: " + cn);
		}
		String locator = CommonHelper.getClassPropertyValueByKey(c, key);
		logSelenium.debug("Found value '" + (!key.equals("password") ? value : "*****") + 
				"' by key '" + key + "' from file '" + cn + ".properties'");

		return locator;
	}

	/**
	 * Gets the test platform properties.
	 *
	 * @param filePath the file path
	 * @return the test platform properties
	 */
	@Override
	public Properties getTestPlatformProperties(String filePath) {
		if (SeleniumHelper.testPlatformProperties == null || SeleniumHelper.testPlatformProperties.isEmpty()) {
			try (Reader inStream = new InputStreamReader(new FileInputStream(new File(filePath)))) {
				SeleniumHelper.testPlatformProperties.load(inStream);
			} catch (IOException e) {
			}
		}
		return SeleniumHelper.testPlatformProperties;
	}

	/**
	 * Wait until fully loaded.
	 *
	 * @param timeoutSeconds the timeout seconds
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public void waitUntilFullyLoaded(long timeoutSeconds) throws IOException {
		WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeoutSeconds));
		wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState")).equals("complete");
	}

	/**
	 * Common teardown.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public void commonTeardown() throws IOException {
		throw new UnsupportedOperationException("Not implemented, yet");
	}

	/**
	 * Report step pass screenshot.
	 */
	@Override
	public void reportStepPassScreenshot() {
		reportStepPassScreenshot(screenshotFile());
	}

	/**
	 * Report step fail screenshot.
	 */
	@Override
	public void reportStepFailScreenshot() {
		reportStepFailScreenshot(screenshotFile());
	}

	/**
	 * Screenshot file to "RunResults" + File.separator + "Resources" +
	 * File.separator + "Snapshots"
	 *
	 * @return the string
	 */
	@Override
	public String screenshotFile() {
		long time = new Date().getTime();
		File source = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
		String snapshotsDir = Paths.get("").toAbsolutePath().toString() + File.separator + "RunResults" + File.separator
				+ "Resources" + File.separator + "Snapshots";
		try {
			Files.createDirectories(Paths.get(snapshotsDir));
			String screenShotPath = snapshotsDir + File.separator + time + ".png";
			File destination = new File(screenShotPath);
			Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Resources" + File.separator + "Snapshots" + File.separator + time + ".png";
	}

	/**
	 * Screenshot base 64.
	 *
	 * @return the string
	 */
	@Override
	public String screenshotBase64() {
		String scnShot = getDriver().getScreenshotAs(OutputType.BASE64);
		return "data:image/jpg;base64, " + scnShot;

	}

	/**
	 * Scroll to bottom.
	 */
	@Override
	public void scrollToBottom() {
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,document.body.scrollHeight)", "");
	}

	/**
	 * Wait.
	 * 
	 * Waits the given milliseconds
	 *
	 * @param milliseconds the milliseconds
	 */
	@Override
	public void wait(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Gets the edits the field locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the edits the field locator
	 */
	@Override
	public String getEditFieldLocator(String locatorDelegate) {
		return this.getClass().getName() + File.pathSeparator + SeleniumStrings.EDITFIELD + File.pathSeparator + locatorDelegate;
	}

	/**
	 * Gets the button locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the button locator
	 */
	@Override
	public String getButtonLocator(String locatorDelegate) {
		return this.getClass().getName() + File.pathSeparator + SeleniumStrings.BUTTON + File.pathSeparator + locatorDelegate;
	}

	/**
	 * Gets the text locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the text locator
	 */
	@Override
	public String getTextLocator(String locatorDelegate) {
		return this.getClass().getName() + File.pathSeparator + SeleniumStrings.TEXT + File.pathSeparator + locatorDelegate;
	}

	/**
	 * Gets the listbox locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the listbox locator
	 */
	@Override
	public String getListboxLocator(String locatorDelegate) {
		return this.getClass().getName() + File.pathSeparator + SeleniumStrings.LISTBOX + File.pathSeparator + locatorDelegate;
	}

	/**
	 * Gets the radiogroup locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the radiogroup locator
	 */
	@Override
	public String getRadiogroupLocator(String locatorDelegate) {
		return this.getClass().getName() + File.pathSeparator + SeleniumStrings.RADIOGROUP + File.pathSeparator + locatorDelegate;
	}

	/**
	 * Gets the radiobutton locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the radiobutton locator
	 */
	@Override
	public String getRadiobuttonLocator(String locatorDelegate) {
		return this.getClass().getName() + File.pathSeparator + SeleniumStrings.RADIOBUTTON + File.pathSeparator + locatorDelegate;
	}

	/**
	 * Gets the checkbox locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the checkbox locator
	 */
	@Override
	public String getCheckboxLocator(String locatorDelegate) {
		return this.getClass().getName() + File.pathSeparator + SeleniumStrings.CHECKBOX + File.pathSeparator + locatorDelegate;
	}

	/**
	 * Gets the numericfield locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the numericfield locator
	 */
	@Override
	public String getNumericfieldLocator(String locatorDelegate) {
		return this.getClass().getName() + File.pathSeparator + SeleniumStrings.NUMERICFIELD + File.pathSeparator + locatorDelegate;
	}

	/**
	 * Gets the filefield locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the filefield locator
	 */
	@Override
	public String getFilefieldLocator(String locatorDelegate) {
		return this.getClass().getName() + File.pathSeparator + SeleniumStrings.FILEFIELD + File.pathSeparator + locatorDelegate;
	}

	/**
	 * Gets the slider locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the slider locator
	 */
	@Override
	public String getSliderLocator(String locatorDelegate) {
		return this.getClass().getName() + File.pathSeparator + SeleniumStrings.SLIDER + File.pathSeparator + locatorDelegate;
	}

	/**
	 * Gets the link locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the link locator
	 */
	@Override
	public String getLinkLocator(String locatorDelegate) {
		return this.getClass().getName() + File.pathSeparator + SeleniumStrings.LINK + File.pathSeparator + locatorDelegate;
	}

	/**
	 * Test platform properties get.
	 *
	 * @param key the key
	 * @return the string
	 */
	protected static String testPlatformPropertiesGet(String key) {
		String value = testPlatformProperties.getProperty(key); 
		return value;
	}

	/**
	 * Gets the testdata.
	 *
	 * @param testDataPath the test data path
	 * @param simpleName the simple name
	 * @return the testdata
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static List<Object[]> getTestdata(String testDataPath, String simpleName) throws IOException {
		File file = new File(testDataPath + File.separator + SeleniumStrings.TESTDATA_XLS);
		ExcelHelper xl = new ExcelHelper(file, simpleName);
		return xl.getData();
	}

	/**
	 * Common setup.
	 *
	 * @param platformDelegate the platform delegate
	 * @param testDataPath the test data path
	 * @return the selenium interface
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ConfigurationException the configuration exception
	 */
	@Override
	public SeleniumInterface commonSetup(String platformDelegate, String testDataPath)
			throws IOException, ConfigurationException {
		return null;
	}
}
