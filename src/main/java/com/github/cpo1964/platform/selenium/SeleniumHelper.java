/*
 * MIT License
 *
 * Copyright (c) 2023 Christian Pöcksteinera
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
package com.github.cpo1964.platform.selenium;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Properties;

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

import com.github.cpo1964.platform.PlatformInterface;
import com.github.cpo1964.report.extent.ExtentHelper;

/**
 * The Class SeleniumHelper.
 */
public class SeleniumHelper extends ExtentHelper implements PlatformInterface {

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
	 * @param webElement the new web element
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
		    setDriver(new FirefoxDriver(firefoxOptions));
		} else {
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
	public boolean existsByXpath(String xpath, long timeout) {
		setWebElement(null);
		long oldTimeout = getDriverImplicitlyWaitTimoutSeconds();
		setDriverImplicitlyWaitTimoutSeconds(3);
		getByXpath(xpath, timeout);
		setDriverImplicitlyWaitTimoutSeconds(oldTimeout);
		return getWebElement() != null;
	}

	private static void getByXpath(String xpath, long timeout) {
		List<WebElement> webEls = getDriver().findElements(By.xpath(xpath));
		if (!webEls.isEmpty()) {
			setWebElement(webEls.get(0));
			WebDriverWait wa = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
			setWebElement(wa.until(ExpectedConditions.elementToBeClickable(getWebElement())));
		}
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
	public boolean existsByXpath(String xpath, boolean reportFailed, long timeout) {
		setExistsCount(getExistsCount() + 1);
		boolean exists = existsByXpath(xpath, timeout);
		if (!exists)  {
			if (reportFailed) {
				reportStepFail("<b>EXISTS  </b>s by xpath $(\"" + xpath + "\") - false");
			} else {
				reportStepPass("<b>EXISTS  </b>s by xpath $(\"" + xpath + "\") - false");
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
		getByXpath(xpath, getDriverImplicitlyWaitTimoutSeconds());
		setWebElement(waitUntilClickable(getWebElement(), getDriverImplicitlyWaitTimoutSeconds()));
		if (getWebElement() != null && getWebElement().isEnabled()) {
			if (CLICKKEY.equals(clickAction)) {
				Actions actions = new Actions(getDriver());
				actions.moveToElement(getWebElement()).click().build().perform();
			} else {
				// The user-facing API for emulating complex user gestures. 
				// Use this class rather than using the Keyboard or Mouse directly
				Actions action = new Actions(getDriver());
				if (RIGHTCLICK.equals(clickAction)) {
					// context-click at middle of the given element
					action.contextClick(getWebElement()).perform();
				} else if (ALTCLICK.equals(clickAction)) {
					action
			        .keyDown(Keys.ALT)
			        .click(getWebElement())
			        .keyUp(Keys.ALT)
			        .perform();
				} else if (CONTROLCLICK.equals(clickAction)) {
					action
			        .keyDown(Keys.CONTROL)
			        .click(getWebElement())
			        .keyUp(Keys.CONTROL)
			        .perform();
				} else if (DOUBLECLICK.equals(clickAction)) {
					action.doubleClick(getWebElement()).perform();
				} else if (LONGCLICK.equals(clickAction)) {
					// Clicks (without releasing) in the middle of the given element
					action.clickAndHold(getWebElement()).perform();
					wait(2000);
					// Releases the depressed left mouse button, in the middle of the given element
					action.release(getWebElement()).perform();
				} else if (MOUSEOVER.equals(clickAction)) {
					action.moveToElement(getWebElement()).build().perform();
				} else if (SHIFTCLICK.equals(clickAction)) {
					action
			        .keyDown(Keys.SHIFT)
			        .click(getWebElement())
			        .keyUp(Keys.SHIFT)
			        .perform();
				}
			}
			reportStepPass("<b>CLICK   </b> by xpath $(\"" + xpath + "\")");
		} else {
			reportStepFail(getTest().addScreenCaptureFromPath(screenshotFile()) + "<b>CLICK   </b> by xpath $(\"" + xpath + "\")");
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
		clickByXpath(xpath, CLICKKEY);
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
	public void click(String locatorDelegate) {
		click(locatorDelegate, CLICKKEY);
	}

	/**
	 * Click.
	 *
	 * @param locatorDelegate the locator delegate
	 * @param action the action
	 * @param timeout the timeout
	 */
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
		click(locatorDelegate, CLICKKEY, timeout);
	}

	/**
	 * Input.
	 * 
	 * supported types:
	 *     <input type="text">
	 *     <input type="radio">
	 *     <input type="number">
	 *     <input type="file">
	 *     <input type="checkbox">
	 *     <input type="range"> // vulgo 'slider'
	 *
	 * @param xpath the xpath
	 * @param className the class name
	 * @param value           the value
	 * @param secret          the secret
	 */
	public void inputByXpath(String xpath, String className, String value, boolean secret) {
		setInputsCount(getInputsCount() + 1);
		setWebElement(null);
		try {
			if (className != null) {
				getByXpath(xpath, 30);
			} 
			if (getWebElement() == null) {
					logSecret(xpath + "(unknown) -> not done ", getSecretString(value, secret), secret);
					reportStepFail(getNode().addScreenCaptureFromPath(screenshotFile()) + "<b>input</b> ("
							+ xpath + ", '" + getSecretString(value, secret) + ")'");
					return;
			}
			if (EDITFIELD.equalsIgnoreCase(className)
					|| NUMERICFIELD.equalsIgnoreCase(className)
					|| SLIDER.equalsIgnoreCase(className) // type='range'
					|| FILEFIELD.equalsIgnoreCase(className)) {
				Actions actions = new Actions(getDriver());
				actions.moveToElement(getWebElement()).click().build().perform();
				getWebElement().clear();
				getWebElement().sendKeys(value);
				reportStepPass(BOLD_INPUT_BY_XPATH + xpath + VALUE2 + getSecretString(value, secret) + "'");
				logSecret(xpath, value, secret);
			} else  if (LISTBOX.equalsIgnoreCase(className)) {
				new Select(getWebElement()).selectByVisibleText(value);
				reportStepPass(BOLD_INPUT_BY_XPATH + xpath + VALUE2 + value + "'");
			} else if (CHECKBOX.equalsIgnoreCase(className)
					|| RADIOBUTTON.equalsIgnoreCase(className)) {
				if (getWebElement().isSelected() && "OFF".equalsIgnoreCase(value) ||
						!getWebElement().isSelected() && "ON".equalsIgnoreCase(value)) {
					getWebElement().click();
					reportStepPass(BOLD_INPUT_BY_XPATH + xpath + VALUE2 + value + "'");
				} else {
					throw new NotFoundException(BOLD_INPUT_BY_XPATH + xpath + "\"), value not found: '" + value + "'");
				}
			} else if (RADIOGROUP.equalsIgnoreCase(className)) {
				int option = Integer.parseInt(value);
				List<WebElement> radios = getDriver().findElements(By.xpath(xpath));
				if (option > 0 && option <= radios.size()) {
					radios.get(option - 1).click();
					reportStepPass(BOLD_INPUT_BY_XPATH + xpath + VALUE2 + value + "'");
				} else {
					throw new NotFoundException(BOLD_INPUT_BY_XPATH + xpath + "\"), value not found: '" + value + "'");
				}
			} else {
				throw new NotFoundException("type of webelement unknown: '" + className + "'");
			}
		} catch (IOException e) {
			reportStepFail(getNode().addScreenCaptureFromPath(screenshotFile()) + 
					"<b>INPUT   </b> (" + xpath + ", '" + getSecretString(value, secret) + ")'");
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
	public void inputByXpath(String xpath, String className, String value) {
		inputByXpath(xpath, className, value, false);
	}

	/**
	 * Input.
	 *
	 * @param locatorDelegate the locator delegate
	 * @param value the value
	 * @param b the b
	 */
	private void input(String locatorDelegate, String value, boolean b) {
		String xpath = getLocator(locatorDelegate); // expected: a xpath from the property file
		String className = "";
		String[] descParts = locatorDelegate.split("\\.");
		if (descParts.length == 3) {
			className = descParts[1];
		}
		inputByXpath(xpath, className, value, b);
	}
	
	/**
	 * Input.
	 *
	 * @param locatorDelegate the locator delegate
	 * @param value           the value
	 */
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
	 * @param locatorDelegate the locator delegate
	 * @param value  the value
	 * @param secret the secret
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void logSecret(String locatorDelegate, String value, boolean secret) throws IOException {
		String text = getSecretString(value, secret);
		logSelenium.debug(() -> "<b>input</b> by xpath $(\"" + locatorDelegate + "\"), value=" + text);
	}

	/**
	 * Gets the secret string.
	 *
	 * @param value  the value
	 * @param secret the secret
	 * @return the secret string
	 */
	public String getSecretString(String value, boolean secret) {
		StringBuilder bld = new StringBuilder();
		if (secret) {
			for (int i = 1; i < value.length(); i++) {
				bld.append("*");
			}
			return bld.toString();
		} else {
			return value;
		}
	}

	/**
	 * Wait until fully loaded.
	 *
	 * @param timeoutSeconds the timeout seconds
	 */
	public static void waitUntilFullyLoaded(int timeoutSeconds) {
		WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeoutSeconds));
		wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState")
				.equals("complete"));
	}

	/**
	 * Wait until webelement is clickable.
	 *
	 * @param webEl          the web el
	 * @param timeout the timeout seconds
	 * @return the web element
	 */
	public static WebElement waitUntilClickable(WebElement webEl, long timeout) {
		if (webEl != null) {
			WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
			return wait.until(ExpectedConditions.elementToBeClickable(webEl));
		}
		return null;
	}

	/**
	 * Wait.
	 *
	 * @param milliseconds the milliseconds
	 */
	public void wait(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Gets the locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the locator
	 */
	public String getLocator(String locatorDelegate) {
		String[] locatorDelegateSplit = locatorDelegate.split("\\.");
		if (locatorDelegateSplit.length != 3) {
			reportStepFail(locatorDelegate);
			throw new NotFoundException("locatorDelegate must match pattern 'classname.locatortype.locatorDelegate': '" + locatorDelegate + "'");
		}
		String cn = locatorDelegateSplit[0];
		String key = locatorDelegateSplit[2];
		Class<?> c = null;
		c = getClassByQualifiedName("at.cpo.selenium.common.pageobjects." + cn);
		return getResourcePropertyValueByKey(c, key);
	}

	/**
	 * Gets the resource property value by key.
	 *
	 * @param propHolder the prop holder
	 * @param key        the key
	 * @return the resource property value by key
	 */
	public String getResourcePropertyValueByKey(final Class<?> propHolder, String key) {
		String propertiesFileDestination = propHolder.getSimpleName() + ".properties";
		InputStream stream = propHolder.getResourceAsStream(propertiesFileDestination);
		if (stream == null) {
			URL url = propHolder.getClassLoader().getResource(propertiesFileDestination);
			if (url != null) {
				try (InputStream streamOpen = url.openStream()) {
					stream = streamOpen;
				} catch (IOException e1) {
					throw new UnsupportedOperationException("Can not find property file: " + propertiesFileDestination);
				}
			} else {
				throw new UnsupportedOperationException("Can not find property file: " + propertiesFileDestination);
			}
		}
		Properties prop = new Properties();
		try {
			prop.load(stream);
		} catch (IOException e) {
			throw new UnsupportedOperationException("Can not open property file: " + propertiesFileDestination);
		}

		String value = prop.getProperty(key);
		if (value == null || value.isEmpty()) {
			throw new IllegalArgumentException("Property \"" + key + "\" from file " + propertiesFileDestination
					+ " does not exists or is empty!");
		}
		logSelenium.debug(() -> "Found value '" + (!key.equals("password") ? value : "*****") + 
				"' by key '" + key + "' from file '" + propertiesFileDestination + "'");
		return value;
	}

	/**
	 * Gibt das jeweilige Testdaten Excel zurück.
	 *
	 * @return Das Testdaten Excel als File.
	 */
	public File getTestDataFile() {
		return new File(getTestDataPath() + File.separator + TESTDATA_XLS);
	}

	/**
	 * Sets the test platform properties.
	 *
	 * @param filePath the new test platform properties
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void setTestPlatformProperties(String filePath) throws IOException {
		try (Reader inStream = new InputStreamReader(new FileInputStream(new File(filePath)))) {
			testPlatformProperties.load(inStream);
		}
	}

	/**
	 * Gets the mandant.
	 *
	 * @return the mandant
	 */
	@Override
	public String getMandant() {
		return System.getProperty(MANDANTKEY, "");
	}

	/**
	 * Gets the test environment.
	 *
	 * @return the test environment
	 */
	@Override
	public String getTestEnvironment() {
		return System.getProperty(TEST_ENVIRONMENT, "");
	}

	/**
	 * Gets the produkt.
	 *
	 * @return the produkt
	 */
	@Override
	public String getProdukt() {
		return System.getProperty(PRODUKTKEY, "");
	}

	/**
	 * Common setup.
	 *
	 * @return the selenium helper
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public SeleniumHelper commonSetup() throws IOException {
		getProdukt();
		String mandantTestEnvironment = "";
		if (!getMandant().isEmpty()) {
			mandantTestEnvironment = File.separator + getMandant();
			if (!getTestEnvironment().isEmpty()) {
				mandantTestEnvironment = File.separator + getMandant() + "-" + getTestEnvironment();
			}
		}
		setTestDataPath(Paths.get("").toAbsolutePath().toString() + File.separator + TESTDATADIR
				+ mandantTestEnvironment);
		setTestPlatformProperties(getTestDataPath() + File.separator + TEST_PLATFORM_PROPERTIES);
		return new SeleniumHelper();
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
	 * Screenshot file to "RunResults" + File.separator + "Resources" +
	 * File.separator + "Snapshots"
	 *
	 * @return the string
	 */
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
	public static String screenshotBase64() {
		String scnShot = getDriver().getScreenshotAs(OutputType.BASE64);
		return "data:image/jpg;base64, " + scnShot;

	}
}
