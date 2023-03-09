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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;
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

	/**
	 * The logger.
	 */
	static Logger logSelenium = LogManager.getLogger(SeleniumHelper.class.getSimpleName());

	/**
	 * The Constant WDM_CACHE_PATH.
	 */
	public static final String WDM_CACHE_PATH = "wdm.cachePath";

	/**
	 * The Constant VALUE2.
	 */
	private static final String XPATH_MSG_PART = "\"), value: '";

	/**
	 * The Constant BOLD_INPUT_BY_XPATH.
	 */
	private static final String BOLD_INPUT_BY_XPATH = "<b>INPUT   </b> by xpath $(\"";

	/**
	 * Displayed webelement state.
	 */
	public static final WebelementState Displayed = WebelementState.Displayed;

	/**
	 * Hidden webelement state.
	 */
	public static final WebelementState Hidden = WebelementState.Hidden;

	/**
	 * Selected webelement state.
	 */
	public static final WebelementState Selected = WebelementState.Selected;

	/**
	 * Un selected webelement state.
	 */
	public static final WebelementState UnSelected = WebelementState.UnSelected;

	/**
	 * Enabled webelement state.
	 */
	public static final WebelementState Enabled = WebelementState.Enabled;

	/**
	 * Disabled webelement state.
	 */
	public static final WebelementState Disabled = WebelementState.Disabled;

	/**
	 * Not found webelement state.
	 */
	public static final WebelementState NotFound = WebelementState.NotFound;

	/**
	 * The name.
	 */
	private String name = "";

	/**
	 * The value.
	 */
	protected static String value;

	/**
	 * The driver.
	 */
	private static RemoteWebDriver driver;

	/**
	 * The web element.
	 */
	private static WebElement webElement;

	/**
	 * The test data path.
	 */
	private String testDataPath;

	/**
	 * The proxy.
	 */
	private static String proxy;

	/**
	 * The proxy user.
	 */
	private static String proxyUser;

	/**
	 * The proxy pass.
	 */
	private static String proxyPass;

	/**
	 * The driver loaded.
	 */
	private static boolean driverLoaded = false;

	/**
	 * The ok.
	 */
	protected boolean ok;

	/**
	 * The global timeout
	 * 
	 * used for waitUntil methods
	 */
	private long timeout = 30;

	/**
	 * The test platform properties.
	 */
	public static final Properties testPlatformProperties = new Properties();

	/**
	 * The iteration.
	 */
	private static int iteration = 0;

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
		return !driverLoaded;
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
			browser = "firefox";
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
	 * <p>
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
	 * <p>
	 * Selects either the first frame on the page, or the main document when a page
	 * contains iframes.
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
		return timeout;
	}

	/**
	 * Driver implicitly wait.
	 *
	 * @param value the value
	 */
	@Override
	public void setDriverImplicitlyWaitTimoutSeconds(long value) {
		timeout = value;
	}

	/**
	 * Setup driver.
	 * <p>
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
		java.util.logging.Logger globalLogger = java.util.logging.Logger
				.getLogger(java.util.logging.Logger.GLOBAL_LOGGER_NAME);
		globalLogger.setLevel(java.util.logging.Level.OFF);

		// wdm.cachePath
		logSelenium.info(() -> "downloading driver to: " + System.getProperty(WDM_CACHE_PATH));
		
		setProxy(System.getProperty("proxy"));
		setProxyUser(System.getProperty("proxyUser"));
		setProxyPass(System.getProperty("proxyPass"));

		if ("chrome".equalsIgnoreCase(getBrowser())) {
			setupChromeDriver();
			if (isDriverLoaded()) {
				logSelenium.info(() -> "using local chromedriver: " + System.getProperty("webdriver.chrome.driver")
						+ System.lineSeparator());
				setDriverLoaded(true);
			}
		} else if ("firefox".equalsIgnoreCase(getBrowser())) {
			setupFirefoxDriver();
			if (isDriverLoaded()) {
				logSelenium.info(() -> "using local geckodriver: " + System.getProperty("webdriver.gecko.driver")
						+ System.lineSeparator());
				setDriverLoaded(true);
			}
		}
		getDriver().manage().window().maximize();
		return getDriver();
	}

	/**
	 * Setup chrome driver.
	 */
	private static void setupChromeDriver() {
		if (getDriver() == null) {
			if (getProxyUser() != null && !getProxyUser().isEmpty()) {
				io.github.bonigarcia.wdm.WebDriverManager.chromedriver().proxyUser(getProxyUser())
						.proxyPass(getProxyPass()).proxy(getProxy()).setup();
			} else {
				io.github.bonigarcia.wdm.WebDriverManager.chromedriver().setup();
			}
		}
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("--no-sandbox");
		chromeOptions.addArguments("--remote-allow-origins=*");
		chromeOptions.addArguments("--disable-gpu");
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
				io.github.bonigarcia.wdm.WebDriverManager.firefoxdriver().proxyUser(getProxyUser())
						.proxyPass(getProxyPass()).proxy(getProxy()).setup();
			} else {
				io.github.bonigarcia.wdm.WebDriverManager.firefoxdriver().setup();
			}
		}
		System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "FFLogs.txt");

		if ("true".equalsIgnoreCase(System.getProperty("headless"))) {
			FirefoxBinary firefoxBinary = new FirefoxBinary();
			firefoxBinary.addCommandLineOptions("--headless");

			FirefoxOptions firefoxOptions = new FirefoxOptions();
			firefoxOptions.setBinary(firefoxBinary);
			firefoxOptions.addArguments("--ignore-certificate-errors", "--ignore-ssl-errors");
			firefoxOptions.addArguments("--disable-web-security");
			firefoxOptions.addArguments("--allow-running-insecure-content");
			firefoxOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);

			FirefoxProfile profile = new FirefoxProfile();
			profile.setAcceptUntrustedCertificates(true);
			profile.setAssumeUntrustedCertificateIssuer(false);
			profile.setPreference("pageLoadStrategy", "normal");
			profile.setPreference("privacy.trackingprotection.enabled", false);
			firefoxOptions.setProfile(profile);
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
	 * Common setup.
	 *
	 * @param platformDelegate the platform delegate
	 * @param testDataPath     the test data path
	 * @return the selenium interface
	 */
	@Override
	public SeleniumInterface commonSetup(String platformDelegate, String testDataPath) {
		return null;
	}

	/**
	 * Gets a unique webelement by xpath.
	 *
	 * @param xpath   the xpath
	 * @param timeout the timeout
	 * @return the WebElement
	 */
	@SuppressWarnings("unused")
	private WebElement getWebelementByXpath(By xpath, long timeout) {
		WebElement webEl = null;
		long oldTimeout = getDriverImplicitlyWaitTimoutSeconds();
		/*
		 * Find all elements within the current page using the given mechanism. This
		 * method is affected by the 'implicit wait' times in force at the time of
		 * execution. When implicitly waiting, this method will return as soon as there
		 * are more than 0 items in the found collection, or will return an empty list
		 * if the timeout is reached.
		 */
		List<WebElement> webEls = getDriver().findElements(xpath);
		int i = 0;
		while (webEls.size() == 0 && i < timeout) {
			webEls = getDriver().findElements(xpath);
			wait(1000);
			i++;
		}
		if (i > 1) {
			logSelenium.info("waited " + i + " seconds for '" + xpath + "'");
		}
		// the searched webelement must be unique
		if (webEls.size() > 1) {
			
			throw new NonUniqueResultException("more then 1 webelement found with xpath: " + xpath);
		}
		// one Webelements found in ImplicitlyWaitTimout
		if (!webEls.isEmpty()) {
			webEl = webEls.get(0);
		}
		return webEl;
	}

	/**
	 * Click by xpath.
	 *
	 * @param xpath       the xpath
	 * @param clickAction the value
	 */
	@Override
	public void clickByXpath(String xpath, String clickAction) {
		if (isFailed()) {
			return;
		}
		setClicksCount(getClicksCount() + 1);
		setWebElement(null);
		if (waitUntilBy(By.xpath(xpath), WebelementState.Enabled, getDriverImplicitlyWaitTimoutSeconds())) {
			if (ClickActions.CLICKKEY.name().equals(clickAction)) {
				Actions actions = new Actions(getDriver());
				try {
					actions.moveToElement(getWebElement()).click().build().perform();
				} catch (MoveTargetOutOfBoundsException | StaleElementReferenceException e) {
					try {
						getWebElement().click();
					} catch (Exception e1) {
						reportStepFail("CLICK   by xpath $(" + xpath + ") with " + clickAction + " failed:"
								+ System.lineSeparator() + e1.getMessage());
						return;
					}
				}
			} else {
				// The user-facing API for emulating complex user gestures.
				// Use this class rather than using the Keyboard or Mouse directly
				Actions action = new Actions(getDriver());
				if (ClickActions.RIGHTCLICK.name().equals(clickAction)) {
					// context-click at middle of the given element
					action.contextClick(getWebElement()).perform();
				} else if (ClickActions.ALTCLICK.name().equals(clickAction)) {
					try {
						action.keyDown(Keys.ALT).click(getWebElement()).keyUp(Keys.ALT).perform();
					} catch (MoveTargetOutOfBoundsException e) {
						action.keyDown(Keys.ALT);
						getWebElement().click();
						action.keyUp(Keys.ALT);
					}
				} else if (ClickActions.CONTROLCLICK.name().equals(clickAction)) {
					try {
						action.keyDown(Keys.CONTROL).click(getWebElement()).keyUp(Keys.CONTROL).perform();
					} catch (MoveTargetOutOfBoundsException e) {
						action.keyDown(Keys.CONTROL);
						try {
							getWebElement().click();
						} catch (Exception e1) {
							reportStepFail("CLICK   by xpath $(" + xpath + ") with " + clickAction + " failed:"
									+ System.lineSeparator() + e1.getMessage());
							return;
						}
						action.keyUp(Keys.CONTROL);
					}
				} else if (ClickActions.DOUBLECLICK.name().equals(clickAction)) {
					action.doubleClick(getWebElement()).perform();
				} else if (ClickActions.LONGCLICK.name().equals(clickAction)) {
					// Clicks (without releasing) in the middle of the given element
					action.clickAndHold(getWebElement()).perform();
					wait(2000);
					// Releases the depressed left mouse button, in the middle of the given element
					action.release(getWebElement()).perform();
				} else if (ClickActions.MOUSEOVER.name().equals(clickAction)) {
					action.moveToElement(getWebElement()).build().perform();
				} else if (ClickActions.SHIFTCLICK.name().equals(clickAction)) {
					try {
						action.keyDown(Keys.SHIFT).click(getWebElement()).keyUp(Keys.SHIFT).perform();
					} catch (MoveTargetOutOfBoundsException e) {
						action.keyDown(Keys.SHIFT);
						getWebElement().click();
						action.keyUp(Keys.SHIFT);
					}
				}
			}
			reportStepPass("<b>CLICK   </b> by xpath $(\"" + xpath + "\")");
		} else {
			
			reportStepFail("CLICK   by xpath $(" + xpath + ") with " + clickAction + " failed");
			reportStepFailScreenshot(screenshotFile());
			throw new NoSuchElementException("CLICK   by xpath $(" + xpath + ") with " + clickAction + " failed");
		}
	}

	/**
	 * Click by xpath.
	 *
	 * @param xpath the xpath
	 */
	@Override
	public void clickByXpath(String xpath) {
		clickByXpath(xpath, ClickActions.CLICKKEY.name());
	}

	/**
	 * Click.
	 *
	 * @param locatorDelegate the locator delegate
	 * @param clickAction     the action
	 */
	@Override
	public void click(String locatorDelegate, String clickAction) {
		clickByXpath(getLocator(locatorDelegate), clickAction);
	}

	/**
	 * Click.
	 *
	 * @param locatorDelegate the locator delegate
	 */
	@Override
	public void click(String locatorDelegate) {
		clickByXpath(getLocator(locatorDelegate), ClickActions.CLICKKEY.name());
	}

	/**
	 * Input.
	 * <p>
	 * supported types: input type="text input type="radio input type="number input
	 * type="file input type="checkbox input type="range // vulgo 'slider'
	 *
	 * @param xpath  the xpath
	 * @param type   the class name
	 * @param value  the value
	 * @param secret the secret
	 */
	public void inputByXpath(String xpath, String type, String value, boolean secret) {
		if (isFailed()) {
			return;
		}
		setInputsCount(getInputsCount() + 1);
		try {
			ok = waitUntilBy(By.xpath(xpath), WebelementState.Enabled);
			if (!ok || getWebElement() == null || type == null) {
				
				logSecret(xpath + "(unknown) -> not done ", CommonHelper.getSecretString(value, secret), secret);
				reportStepFail("<b>INPUT   </b> (" + type + " - " + xpath + ", '"
						+ CommonHelper.getSecretString(value, secret) + ")'");
				reportStepFailScreenshot(screenshotFile());
				throw new NotFoundException("type of webelement unknown: '" + type + "'");
			}
			Actions actions = new Actions(getDriver());
			if (WebelementType.EDITFIELD.name().equalsIgnoreCase(type)
					|| WebelementType.NUMERICFIELD.name().equalsIgnoreCase(type)
					|| WebelementType.SLIDER.name().equalsIgnoreCase(type) // type='range'
					|| WebelementType.FILEFIELD.name().equalsIgnoreCase(type)) {
				try {
					actions.moveToElement(getWebElement()).click().build().perform();
				} catch (MoveTargetOutOfBoundsException | StaleElementReferenceException e) {
					try {
						getWebElement().click();
					} catch (Exception e1) {
						reportStepFail(type + " - " + "CLICK   by xpath $(" + xpath + ") with "
								+ ClickActions.CLICKKEY.name() + " failed:" + System.lineSeparator() + e1.getMessage());
						return;
					}
				}
				getWebElement().clear();
				getWebElement().sendKeys(value);
				reportStepPass(BOLD_INPUT_BY_XPATH + xpath + XPATH_MSG_PART
						+ CommonHelper.getSecretString(value, secret) + "'");
				logSecret(xpath, value, secret);
			} else if (WebelementType.LISTBOX.name().equalsIgnoreCase(type)) {
				new Select(getWebElement()).selectByVisibleText(value);
				reportStepPass(BOLD_INPUT_BY_XPATH + xpath + XPATH_MSG_PART + value + "'");
			} else if (WebelementType.CHECKBOX.name().equalsIgnoreCase(type)
					|| WebelementType.RADIOBUTTON.name().equalsIgnoreCase(type)) {
				// if checkbox is selected && value ==  on or true -> do nothing
				// if checkbox is not selected && value ==  off or false -> do nothing
				// checkbox is selected -> uncheck with value off or false
				if ((getWebElement().isSelected() && !CommonHelper.isTrue(value))
						// checkbox is not selected -> check with value on or true
						|| (!getWebElement().isSelected() && CommonHelper.isTrue(value))) {
					try {
						// see: https://stackoverflow.com/questions/34562061/webdriver-click-vs-javascript-click
						scrollIntoView(getWebElement());
						// FIRST approach
						getWebElement().click();
					} catch (Exception e1) {
						try {
							// SECOND approach
							actions
//							.moveToElement(getWebElement())
							.moveToElement(getWebElement(), 0, -250)
							.click()
							.build()
							.perform();
						} catch (MoveTargetOutOfBoundsException | StaleElementReferenceException e) {
							// THIRD approach
							clickByJS(getWebElement());
							// error if the checkbox is in NOT in the expected state
							if ((getWebElement().isSelected() && !CommonHelper.isTrue(value)) || 
									(!getWebElement().isSelected() && CommonHelper.isTrue(value))) {
								reportStepFail(type + " - " + "CLICK   by xpath $(" + xpath + ") with "
										+ ClickActions.CLICKKEY.name() + " failed:" + System.lineSeparator()
										+ e.getMessage());
								return;
							}
						}
					}
					reportStepPass(BOLD_INPUT_BY_XPATH + xpath + XPATH_MSG_PART + value + "'");
				} else {
					
					throw new NotFoundException(
							type + " - " + BOLD_INPUT_BY_XPATH + xpath + "\"), value not found: '" + value + "'");
				}
			} else if (WebelementType.RADIOGROUP.name().equalsIgnoreCase(type)) {
				int option = Integer.parseInt(value);
				List<WebElement> radios = getDriver().findElements(By.xpath(xpath));
				if (option > 0 && option <= radios.size()) {
					radios.get(option - 1).click();
					reportStepPass(BOLD_INPUT_BY_XPATH + xpath + XPATH_MSG_PART + value + "'");
				} else {
					reportStepFail("<b>INPUT   </b> (" + type + " - " + xpath + ", '"
							+ CommonHelper.getSecretString(value, secret) + ")'");
					throw new NotFoundException(
							type + " - " + BOLD_INPUT_BY_XPATH + xpath + "\"), value not found: '" + value + "'");
				}
			} else {
				throw new NotFoundException("type of webelement unknown: '" + type + "'");
			}
		} catch (Exception e) {
			reportStepFailScreenshot(screenshotFile());
			reportStepFail("<b>INPUT   </b> (" + type + " - " + xpath + ", '"
					+ CommonHelper.getSecretString(value, secret) + ")'");
		}
	}

	/**
	 * Input by xpath.
	 *
	 * @param xpath the xpath
	 * @param type  the type
	 * @param value the value
	 */
	@Override
	public void inputByXpath(String xpath, String type, String value) {
		inputByXpath(xpath, type, value, false);
	}

	/**
	 * Input.
	 * <p>
	 * If secret=true the input value will be masked in report like '*****'
	 *
	 * @param locatorDelegate the locator delegate
	 * @param value           the value
	 * @param secret          the secret
	 */
	@Override
	public void input(String locatorDelegate, String value, boolean secret) {
		String[] descParts = locatorDelegate.split(File.pathSeparator);
		inputByXpath(getLocator(locatorDelegate), descParts[1], value, secret);
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
	 * delivers even hidden text
	 *
	 * @param xpath the xpath
	 * @return the string
	 */
	@Override
	public String outputByXpath(String xpath) {
		String output = "";
		if (isFailed()) {
			return output;
		}
		setOutputsCount(getOutputsCount() + 1);
		WebDriverWait wa = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
		WebElement webEl = null;
		String errMsg = "";
		try {
			webEl = wa
					.until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath))));
			setWebElement(webEl);
		} catch (TimeoutException e1) {
			errMsg = e1.getRawMessage();
		}
		if (webEl != null) {
			output = getWebElement().getAttribute("textContent");
			reportStepPass("<b>OUTPUT   </b> by xpath $(\"" + xpath + "\")<br>text: '" + output + "'");
		} else {
			reportStepFailScreenshot(screenshotFile());
			reportStepFail(
					"<b>OUTPUT   </b> by xpath $(\"" + xpath + "\")<br>text: '" + output + "''" + webEl != null ? ""
							: (System.lineSeparator() + errMsg));
		}
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
		return outputByXpath(getLocator(locatorDelegate));
	}

	/**
	 * Validate.
	 *
	 * @param condition   the condition
	 * @param description the description
	 * @return true, if successful
	 */
	@Override
	public boolean validate(boolean condition, String description) {
		if (isPassed()) {
			if (condition) {
				reportStepPass("<b>VALIDATE</b> '" + description + "' - " + true);
			} else {
				reportStepFail("<b>VALIDATE</b> '" + description + "' - " + false);
				try {
					reportStepFailScreenshot(screenshotFile());
				} catch (WebDriverException e) {
					reportStepFail("screenshotFile failed");
				}
			}
		}
		return condition;
	}

	/**
	 * Drag and drop.
	 *
	 * @param locatorFrom the locator from
	 * @param locatorTo   the locator to
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
	 * @param xpathTo   the xpath to
	 */
	@Override
	public void dragAndDropByXpath(String xpathFrom, String xpathTo) {
		WebElement webElFrom = getDriver().findElement(By.xpath(xpathFrom));
		WebElement webElTo = getDriver().findElement(By.xpath(xpathTo));
		// see: https://www.selenium.dev/documentation/webdriver/actions_api/mouse/
		new Actions(getDriver()).dragAndDrop(webElFrom, webElTo).perform();
	}

	/**
	 * Log secret.
	 * <p>
	 * If secret==true the masked value will be logged
	 *
	 * @param locatorDelegate the locator delegate
	 * @param value           the value
	 * @param secret          the secret
	 */
	private void logSecret(String locatorDelegate, String value, boolean secret) {
		String text = value;
		if (secret) {
			text = CommonHelper.getSecretString(value, true);
		}
		logSelenium.debug("<b>input</b> by xpath $(\"" + locatorDelegate + "\"), value=" + text);
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
			String errMsg = "locatorDelegate must match pattern 'classname" + File.pathSeparator + "locatortype"
					+ File.pathSeparator + "locatorDelegate': '" + locatorDelegate + "'";
			reportStepFail(errMsg);
			throw new NotFoundException(errMsg);
		}
		String cn = locatorDelegateSplit[0];
		String key = locatorDelegateSplit[2];
		Class<?> c = getClassByQualifiedName(cn);
		if (c == null) {
			throw new NotFoundException("class not found: " + cn);
		}
		// expected: a xpath from the property file
		String locator = CommonHelper.getClassPropertyValueByKey(c, key);
		logSelenium.debug("Found value '" + (!key.equals("password") ? value : "*****") + "' by key '" + key
				+ "' from file '" + cn + ".properties'");
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
		if (SeleniumHelper.testPlatformProperties.isEmpty()) {
			try (Reader inStream = new InputStreamReader(new FileInputStream(filePath))) {
				SeleniumHelper.testPlatformProperties.load(inStream);
			} catch (IOException e) {
				logSelenium.debug(e.getMessage());
			}
		}
		return SeleniumHelper.testPlatformProperties;
	}

	/**
	 * Common teardown.
	 *
	 */
	@Override
	public void commonTeardown() {
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
		String snapshotsDir = Paths.get("").toAbsolutePath() + File.separator + "RunResults" + File.separator
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
	 * Wait.
	 * <p>
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
		return this.getClass().getName() + File.pathSeparator + WebelementType.EDITFIELD.name() + File.pathSeparator
				+ locatorDelegate;
	}

	/**
	 * Gets the button locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the button locator
	 */
	@Override
	public String getButtonLocator(String locatorDelegate) {
		return this.getClass().getName() + File.pathSeparator + WebelementType.BUTTON.name() + File.pathSeparator
				+ locatorDelegate;
	}

	/**
	 * Gets the text locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the text locator
	 */
	@Override
	public String getTextLocator(String locatorDelegate) {
		return this.getClass().getName() + File.pathSeparator + WebelementType.TEXT.name() + File.pathSeparator
				+ locatorDelegate;
	}

	/**
	 * Gets the listbox locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the listbox locator
	 */
	@Override
	public String getListboxLocator(String locatorDelegate) {
		return this.getClass().getName() + File.pathSeparator + WebelementType.LISTBOX.name() + File.pathSeparator
				+ locatorDelegate;
	}

	/**
	 * Gets the radiogroup locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the radiogroup locator
	 */
	@Override
	public String getRadiogroupLocator(String locatorDelegate) {
		return this.getClass().getName() + File.pathSeparator + WebelementType.RADIOGROUP.name() + File.pathSeparator
				+ locatorDelegate;
	}

	/**
	 * Gets the radiobutton locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the radiobutton locator
	 */
	@Override
	public String getRadiobuttonLocator(String locatorDelegate) {
		return this.getClass().getName() + File.pathSeparator + WebelementType.RADIOBUTTON.name() + File.pathSeparator
				+ locatorDelegate;
	}

	/**
	 * Gets the checkbox locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the checkbox locator
	 */
	@Override
	public String getCheckboxLocator(String locatorDelegate) {
		return this.getClass().getName() + File.pathSeparator + WebelementType.CHECKBOX.name() + File.pathSeparator
				+ locatorDelegate;
	}

	/**
	 * Gets the numericfield locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the numericfield locator
	 */
	@Override
	public String getNumericfieldLocator(String locatorDelegate) {
		return this.getClass().getName() + File.pathSeparator + WebelementType.NUMERICFIELD.name() + File.pathSeparator
				+ locatorDelegate;
	}

	/**
	 * Gets the filefield locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the filefield locator
	 */
	@Override
	public String getFilefieldLocator(String locatorDelegate) {
		return this.getClass().getName() + File.pathSeparator + WebelementType.FILEFIELD.name() + File.pathSeparator
				+ locatorDelegate;
	}

	/**
	 * Gets the slider locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the slider locator
	 */
	@Override
	public String getSliderLocator(String locatorDelegate) {
		return this.getClass().getName() + File.pathSeparator + WebelementType.SLIDER.name() + File.pathSeparator
				+ locatorDelegate;
	}

	/**
	 * Gets the link locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the link locator
	 */
	@Override
	public String getLinkLocator(String locatorDelegate) {
		return this.getClass().getName() + File.pathSeparator + WebelementType.LINK.name() + File.pathSeparator
				+ locatorDelegate;
	}

	/**
	 * Test platform properties get.
	 *
	 * @param key the key
	 * @return the string
	 */
	protected static String testPlatformPropertiesGet(String key) {
		return testPlatformProperties.getProperty(key);
	}

	/**
	 * Gets the testdata.
	 *
	 * @param testDataPath the test data path
	 * @param simpleName   the simple name
	 * @return the testdata
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static List<Object[]> getTestdata(String testDataPath, String simpleName) throws IOException {
		File file = new File(testDataPath + File.separator + SeleniumStrings.TESTDATA_XLS);
		ExcelHelper xl = new ExcelHelper(file, simpleName);
		return xl.getData();
	}

	/**
	 * Wait until locator reaches state.
	 *
	 * @param locator the locator
	 * @param state   the state
	 * @param timeout the timeout
	 * @param report  the report
	 * @return the boolean
	 */
	@Override
	public boolean waitUntilBy(By locator, WebelementState state, long timeout, boolean report) {
		setWaitCount(WaitCount() + 1);
		WebDriverWait wa = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
		WebElement webEl = null;
		String errMsg = "";
		try {
			webEl = wa.until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfElementLocated(locator)));
		} catch (TimeoutException e1) {
			if (WebelementState.NotFound.equals(state)) {
				return true;
			}
			errMsg = e1.getRawMessage();
		}
		if (webEl != null) {
			setWebElement(webEl);
			if (state.equals(WebelementState.Enabled)) {
				try {
					final WebElement el = webEl;
					ok = wa.until(ExpectedConditions.refreshed(driver -> el.isEnabled()));
				} catch (Exception e) {
					ok = false;
				}
			} else if (state.equals(WebelementState.Disabled)) {
				ok = wa.until(ExpectedConditions.refreshed(ExpectedConditions.invisibilityOf(webEl)));
			} else if (state.equals(WebelementState.Displayed)) {
				ok = wa.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(webEl))) != null;
			} else if (state.equals(WebelementState.Hidden)) {
				ok = wa.until(ExpectedConditions.refreshed(ExpectedConditions.invisibilityOf(webEl)));
			} else if (state.equals(WebelementState.Selected)) {
				ok = wa.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeSelected(webEl)));
			} else if (state.equals(WebelementState.UnSelected)) {
				try {
					final WebElement el = webEl;
					ok = wa.until(ExpectedConditions.refreshed(driver -> !el.isSelected()));
				} catch (Exception e) {
					ok = false;
				}
			}
		} else {
			if (WebelementState.NotFound.equals(state)) {
				return true;
			}
			ok = false;
		}

		if (report) {
			if (ok) {
				reportStepPass("<b>WAIT  </b> " + locator + " for " + state.name() + " - true");
			} else {
				reportStepFail("<b>WAIT  </b> " + locator + " for " + state.name() + " - false" + errMsg);
			}
		}
		return ok;
	}

	/**
	 * Wait until locator reaches state. Uses default timeout
	 *
	 * @param locator the locator
	 * @param state   the state
	 * @return the boolean
	 */
	@Override
	public boolean waitUntilBy(By locator, WebelementState state, long timeout) {
		return waitUntilBy(locator, state, getDriverImplicitlyWaitTimoutSeconds(), false);
	}

	/**
	 * Wait until locator reaches state. Uses default timeout
	 *
	 * @param locator the locator
	 * @param state   the state
	 * @param report  the report
	 * @return the boolean
	 */
	@Override
	public boolean waitUntilBy(By locator, WebelementState state, boolean report) {
		return waitUntilBy(locator, state, getDriverImplicitlyWaitTimoutSeconds(), report);
	}

	/**
	 * Wait until locator reaches state. Uses default timeout
	 *
	 * @param locator the locator
	 * @param state   the state
	 * @return the boolean
	 */
	@Override
	public boolean waitUntilBy(By locator, WebelementState state) {
		return waitUntilBy(locator, state, getDriverImplicitlyWaitTimoutSeconds(), false);
	}

	/**
	 * Wait until locator reaches state.
	 *
	 * @param locatorDelegate the locator delegate
	 * @param state           the state
	 * @param timeout         the timeout
	 * @param report          the report
	 * @return the boolean
	 */
	@Override
	public boolean waitUntil(String locatorDelegate, WebelementState state, long timeout, boolean report) {
		return waitUntilBy(By.xpath(getLocator(locatorDelegate)), state, timeout, report);
	}

	/**
	 * Wait until locator reaches state.
	 *
	 * @param locatorDelegate the locator delegate
	 * @param state           the state
	 * @param timeout         the timeout
	 * @return the boolean
	 */
	@Override
	public boolean waitUntil(String locatorDelegate, WebelementState state, long timeout) {
		return waitUntilBy(By.xpath(getLocator(locatorDelegate)), state, timeout, false);
	}

	/**
	 * Wait until locator reaches state. Uses default timeout
	 *
	 * @param locatorDelegate the locator delegate
	 * @param state           the state
	 * @param report          the report
	 * @return the boolean
	 */
	@Override
	public boolean waitUntil(String locatorDelegate, WebelementState state, boolean report) {
		return waitUntilBy(By.xpath(getLocator(locatorDelegate)), state, getDriverImplicitlyWaitTimoutSeconds(),
				report);
	}

	/**
	 * Wait until locator reaches state. Uses default timeout
	 *
	 * @param locatorDelegate the locator delegate
	 * @param state           the state
	 * @return the boolean
	 */
	@Override
	public boolean waitUntil(String locatorDelegate, WebelementState state) {
		return waitUntilBy(By.xpath(getLocator(locatorDelegate)), state, getDriverImplicitlyWaitTimoutSeconds(), false);
	}

//    public WebElement waitForElementToBeRefreshedAndClickable(By by) {
//        return new WebDriverWait(getDriver(), Duration.ofMillis(30000))
//                .until(ExpectedConditions.refreshed(
//                        ExpectedConditions.elementToBeClickable(by)));
//    }

	// javascript methods
	
	// mouseMove(getWebElement(), 0, -250).
	
	/**
	 * Scroll into view.
	 *
	 * @param webEl the WebElement
	 */
	public static void scrollIntoView(WebElement webEl) {
		((JavascriptExecutor) getDriver())
				.executeScript("arguments[0].scrollIntoView(true);", webEl);
	}

	/**
	 * Scroll to bottom.
	 */
	public static void scrollToBottom() {
		((JavascriptExecutor) getDriver())
			.executeScript("window.scrollBy(0,document.body.scrollHeight)", "");
	}

	/**
	 * Wait until fully loaded.
	 *
	 * @param timeoutSeconds the timeout seconds
	 */
	public static void waitUntilFullyLoaded(long timeoutSeconds) {
		WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeoutSeconds));
		wait.until(webDriver -> ((JavascriptExecutor) webDriver)
				.executeScript("return document.readyState"))
				.equals("complete");
	}
	
	/**
	 * Click the WebElement
	 *
	 * @param webEl the WebElement
	 */
	public static void clickByJS(WebElement webEl) {
		((JavascriptExecutor) getDriver()).
			executeScript("arguments[0].click();", webEl);

	}

}
