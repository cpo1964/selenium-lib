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
package at.cpo.platform.selenium;

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
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import at.cpo.platform.PlatformInterface;
import at.cpo.report.extent.ExtentHelper;


/**
 * The Class SeleniumHelper.
 */
public class SeleniumHelper extends ExtentHelper implements PlatformInterface {

	/** The logger. */
	Logger LOGGER = LogManager.getLogger(this.getClass().getSimpleName());
	
	/** The name. */
	protected String name = "";

	/** The failed. */
	protected boolean failed;

	/** The browser. */
	protected static String browser;

	/** The web el. */
	protected WebElement webEl;

	/** The test environment. */
	private String testEnvironment = ""; // eg dev, prod

	/** The mandant. */
	private String mandant = ""; // eg dev, prod

	/** The produkt. */
	private String produkt = ""; // eg mtours

	/** The test data path. */
	private String testDataPath;

	private static boolean driverLoaded = false;
	
//	{
//		afterWithFailedInformation = RuleChain.outerRule(new ExternalResource() {
//			@Override
//			protected void after() {
//				logInfo("Test " + name + " " + (failed ? "failed" : "finished") + ".");
//			}
//
//		}).around(new TestWatcher() {
//			@Override
//			protected void finished(Description description) {
//				name = description.getDisplayName();
//			}
//
//			@Override
//			protected void failed(Throwable e, Description description) {
//				failed = true;
//			}
//		});
//	}

	/**
	 * Driver get.
	 *
	 * @param url the url
	 */
	@Override
	public void driverGet(String url) {
		driver.get(url);
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
		List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
		if (iframes.size() > 0) {
			driver.switchTo().frame(name);
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
		driver.switchTo().defaultContent();
	}

	/**
	 * Driver implicitly wait.
	 *
	 * @param value the value
	 */
	@Override
	public void driverImplicitlyWait(long value) {
		driver.manage().timeouts().implicitlyWait(Duration.ofMillis(value));
	}

	/**
	 * Setup driver.
	 */
	public Object setupDriver() {
		browser = System.getProperty("browser");
		if (browser == null || browser.isEmpty()) {
			browser = "firefox";
		}
		// see:
		// https://github.com/bharadwaj-pendyala/selenium-java-lean-test-achitecture
		// https://github.com/bharadwaj-pendyala/selenium-java-lean-test-achitecture/blob/master/src/main/java/driver/local/LocalDriverManager.java

		if ("chrome".equalsIgnoreCase(browser)) {
			setupChromeDriver();
			if (!driverLoaded) {
				LOGGER.info("using local chromedriver: " + System.getProperty("webdriver.chrome.driver") + System.lineSeparator());
				driverLoaded = true;
			}
		} else if ("firefox".equalsIgnoreCase(browser)) {
			setupFirefoxDriver();
			if (!driverLoaded) {
				LOGGER.info("using local geckodriver: " + System.getProperty("webdriver.gecko.driver") + System.lineSeparator());
				driverLoaded = true;
			}
		}
//		java.util.logging.Logger.getLogger("org.openqua.selenium.remote.RemoteWebDriver").setLevel(Level.OFF);
//		driver.setLogLevel(java.util.logging.Level.OFF);

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
//		driver.manage().window().maximize();
//        driver.manage().window().setSize(new Dimension(1900, 1000));

		return driver;
	}

	/**
	 * Setup chrome driver.
	 */
	private static void setupChromeDriver() {
		System.setProperty("webdriver.chrome.silentLogging", "true");
		System.setProperty("webdriver.chrome.verboseLogging", "false");
		System.setProperty("webdriver.chrome.silentOutput", "true");

		System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");

		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.setHeadless(true);

		driver = new ChromeDriver(chromeOptions);
	}

	/**
	 * Setup firefox driver.
	 */
	protected static void setupFirefoxDriver() {
		if (driver == null) {
			io.github.bonigarcia.wdm.WebDriverManager.firefoxdriver().setup();
		}
//		if (SystemUtils.IS_OS_LINUX) {
////			System.setProperty("webdriver.gecko.driver", "/usr/local/bin/geckodriver");
//			System.setProperty("webdriver.gecko.driver",
//					"src/test/resources/selenium_standalone_binaries/linux/marionette/64bit/geckodriver");
//		} else if (SystemUtils.IS_OS_WINDOWS) {
//			System.setProperty("webdriver.gecko.driver",
//					"src/test/resources/selenium_standalone_binaries/windows/marionette/64bit/geckodriver.exe");
//		}

		System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "FFLogs.txt");

//		DriverService serviceBuilder = new GeckoDriverService.Builder().build();
//		serviceBuilder.sendOutputTo(new OutputStream() {
//			@Override
//			public void write(int b) {
//			}
//		});

//		DesiredCapabilities capabilities = new DesiredCapabilities();
//		capabilities.setCapability("marionette", true);
//		FirefoxOptions options = new FirefoxOptions();
//		options.merge(capabilities);
//		options.setLogLevel(FirefoxDriverLogLevel.ERROR);
//		options.addPreference("browser.link.open_newwindow", 3);
//		options.addPreference("browser.link.open_newwindow.restriction", 0);
//      options.setHeadless(Boolean.getBoolean("headless"));

//		driver = new FirefoxDriver(options);
		driver = new FirefoxDriver();

		/*
		 * WebDriverManager.firefoxdriver().setup(); -> freischaltung fehlt !
		 * 
		 * io.github.bonigarcia.wdm.config.WebDriverManagerException:
		 * org.apache.hc.client5.http.HttpHostConnectException: Connect to
		 * https://api.github.com:443 [api.github.com/140.82.121.6] failed: Connection
		 * timed out
		 */

	}

	/**
	 * Close browser.
	 */
	public void closeBrowser() {
		try {
			if (driver != null) {
				driver.quit();
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
		webEl = null;
//		boolean enabled = true;
		driverImplicitlyWait(3000);
		List<WebElement> webEls = driver.findElements(By.xpath(xpath));
		if (webEls.size() > 0) {
			webEl = webEls.get(0);
			WebDriverWait wa = new WebDriverWait(driver, Duration.ofSeconds(timeout));
			webEl = wa.until(ExpectedConditions.elementToBeClickable(webEl));
//			webEl = wa.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
//			enabled = wa.until(ExpectedConditions.attributeToBe(webEl, "", ""));
		}
		return webEl != null;
	}

	/**
	 * Exists by xpath.
	 *
	 * @param xpath the xpath
	 * @return true, if successful
	 */
	@Override
	public boolean existsByXpath(String xpath) {
		return existsByXpath(xpath, driver.manage().timeouts().getImplicitWaitTimeout().getSeconds());
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
		return existsByXpath(xpath, reportFailed, driver.manage().timeouts().getImplicitWaitTimeout().getSeconds());
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
	 * @param value the value
	 */
	@Override
	public void clickByXpath(String xpath, String value) {
		webEl = driver.findElement(By.xpath(xpath));
		if (webEl.isEnabled()) {
			if (CLICK.equals(value)) {
				webEl.click();
			} else {
				// The user-facing API for emulating complex user gestures. 
				// Use this class rather than using the Keyboard or Mouse directly
				Actions action = new Actions(driver);
				if (RIGHTCLICK.equals(value)) {
					// context-click at middle of the given element
					action.contextClick(webEl).perform();
				} else if (ALTCLICK.equals(value)) {
					action
			        .keyDown(Keys.ALT)
			        .click(webEl)
			        .keyUp(Keys.ALT)
			        .perform();
				} else if (CONTROLCLICK.equals(value)) {
					action
			        .keyDown(Keys.CONTROL)
			        .click(webEl)
			        .keyUp(Keys.CONTROL)
			        .perform();
				} else if (DOUBLECLICK.equals(value)) {
					action.doubleClick(webEl).perform();
				} else if (LONGCLICK.equals(value)) {
					// Clicks (without releasing) in the middle of the given element
					action.clickAndHold(webEl).perform();
					wait(2000);
					// Releases the depressed left mouse button, in the middle of the given element
					action.release(webEl).perform();
				} else if (MOUSEOVER.equals(value)) {
					action.moveToElement(webEl).build().perform();
				} else if (SHIFTCLICK.equals(value)) {
					action
			        .keyDown(Keys.SHIFT)
			        .click(webEl)
			        .keyUp(Keys.SHIFT)
			        .perform();
				}
			}
			reportStepPass("<b>CLICK   </b> by xpath $(\"" + xpath + "\")");
		} else {
			reportStepFail(test.addScreenCaptureFromPath(screenshotFile(driver)) + "<b>CLICK   </b> by xpath $(\"" + xpath + "\")");
		}
	}

	/**
	 * Click by xpath.
	 *
	 * @param xpath the xpath
	 */
	@Override
	public void clickByXpath(String xpath) {
		clickByXpath(xpath, CLICK);
	}

	/**
	 * Click.
	 *
	 * @param locatorDelegate the locator delegate
	 * @param action the action
	 */
	@Override
	public void click(String locatorDelegate, String action) {
		String xpath = getLocator(locatorDelegate);
		clickByXpath(xpath, action);
	}

	/**
	 * Click.
	 *
	 * @param locatorDelegate the locator delegate
	 */
	public void click(String locatorDelegate) {
		click(locatorDelegate, CLICK);
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
		webEl = null;
		try {
			if (className != null) {
				webEl = driver.findElement(By.xpath(xpath));
				webEl = waitUntilClickable(webEl, 30);
			} 
			if (webEl == null) {
				try {
					logSecret(xpath + "(unknown) -> not done ", getSecretString(value, secret), secret);
					reportStepFail(node.addScreenCaptureFromPath(screenshotFile(driver)) + "<b>input</b> ("
							+ xpath + ", '" + getSecretString(value, secret) + ")'");
					throw new RuntimeException();
				} catch (IOException e) {
					throw new RuntimeException();
				}
			}
			if (EDITFIELD.equalsIgnoreCase(className)
					|| NUMERICFIELD.equalsIgnoreCase(className)
					|| SLIDER.equalsIgnoreCase(className) // type='range'
					|| FILEFIELD.equalsIgnoreCase(className)) {
				webEl.click();
				webEl.clear();
				webEl.sendKeys(value);
				reportStepPass("<b>INPUT   </b> by xpath $(\"" + xpath + "\"), value: '" + getSecretString(value, secret) + "'");
				try {
					logSecret(xpath, value, secret);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else  if (LISTBOX.equalsIgnoreCase(className)) {
				new Select(webEl).selectByVisibleText(value);
				reportStepPass("<b>INPUT   </b> by xpath $(\"" + xpath + "\"), value: '" + value + "'");
			} else if (CHECKBOX.equalsIgnoreCase(className)
					|| RADIOBUTTON.equalsIgnoreCase(className)) {
				if (webEl.isSelected() && "OFF".equalsIgnoreCase(value)) {
					webEl.click();
					reportStepPass("<b>INPUT   </b> by xpath $(\"" + xpath + "\"), value: '" + value + "'");
				} else if (!webEl.isSelected() && "ON".equalsIgnoreCase(value)) {
					webEl.click();
					reportStepPass("<b>INPUT   </b> by xpath $(\"" + xpath + "\"), value: '" + value + "'");
				} else {
					throw new NotFoundException("<b>INPUT   </b> by xpath $(\"" + xpath + "\"), value not found: '" + value + "'");
				}
			} else if (RADIOGROUP.equalsIgnoreCase(className)) {
				int option = Integer.valueOf(value);
				List<WebElement> radios = driver.findElements(By.xpath(xpath));
				if (option > 0 && option <= radios.size()) {
					radios.get(option - 1).click();
					reportStepPass("<b>INPUT   </b> by xpath $(\"" + xpath + "\"), value: '" + value + "'");
				} else {
					throw new NotFoundException("<b>INPUT   </b> by xpath $(\"" + xpath + "\"), value not found: '" + value + "'");
				}
			} else {
				throw new NotFoundException("type of webelement unknown: '" + className + "'");
			}
		} catch (RuntimeException e) {
			reportStepFail(node.addScreenCaptureFromPath(screenshotFile(driver)) + "<b>INPUT   </b> ("
					+ xpath + ", '" + getSecretString(value, secret) + ")'");
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
		webEl = driver.findElement(By.xpath(xpath));
		String output = webEl.getAttribute("textContent");
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
	 */
	@Override
	public void validate(boolean condition, String description) {
		if (condition) {
			reportStepPass("<b>VALIDATE</b> '" + description + "' - " + condition);
		} else {
			reportStepFail("<b>VALIDATE</b> '" + description + "' - " + condition);
			reportStepFailScreenshot(screenshotFile(driver));
		}
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
		WebElement webElFrom = driver.findElement(By.xpath(xpathFrom));	
		WebElement webElTo = driver.findElement(By.xpath(xpathTo));	
		// see: https://www.selenium.dev/documentation/webdriver/actions_api/mouse/
		new Actions(driver)
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
		LOGGER.debug("<b>input</b> by xpath $(\"" + locatorDelegate + "\"), value=" + text);
	}

	/**
	 * Gets the secret string.
	 *
	 * @param value  the value
	 * @param secret the secret
	 * @return the secret string
	 */
	public String getSecretString(String value, boolean secret) {
		String text = "";
		if (secret) {
			for (int i = 1; i < value.length(); i++) {
				text = text + "*";
			}
			return text;
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
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
		wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState")
				.equals("complete"));
	}

	/**
	 * Wait until webelement is clickable.
	 *
	 * @param webEl          the web el
	 * @param timeoutSeconds the timeout seconds
	 * @return the web element
	 */
	public static WebElement waitUntilClickable(WebElement webEl, int timeoutSeconds) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
		return wait.until(ExpectedConditions.elementToBeClickable(webEl));
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
//			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
//			InputStream stream = classloader.getResourceAsStream("test-data/myExcel.xlsx");
			URL url = propHolder.getClassLoader().getResource(propertiesFileDestination);
			if (url != null) {
				try {
					stream = url.openStream();
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
		LOGGER.debug("Found value '" + (!key.equals("password") ? value : "*****") + "' by key '" + key + "' from file '"
				+ propertiesFileDestination + "'");
		return value;
	}

	/**
	 * Gibt das jeweilige Testdaten Excel zurück.
	 *
	 * @return Das Testdaten Excel als File.
	 */
	public File getTestDataFile() {
		return new File(testDataPath + File.separator + TESTDATA_XLS);
	}

	/**
	 * Sets the test platform properties.
	 *
	 * @param filePath the new test platform properties
	 */
	private void setTestPlatformProperties(String filePath) {
		Reader inStream;
		try {
			inStream = new InputStreamReader(new FileInputStream(new File(filePath)));
			testPlatformProperties.load(inStream);
		} catch (IOException e) {
		}

//		Enumeration<?> e = testPlatforProperties.propertyNames();
//		while (e.hasMoreElements()) {
//			String key = (String) e.nextElement();
//			System.out.println(key + " -- " + testPlatforProperties.getProperty(key));
//		}
	}

	/**
	 * Gets the mandant.
	 *
	 * @return the mandant
	 */
	@Override
	public String getMandant() {
		mandant = System.getProperty(MANDANT, "");
		return mandant;
	}

	/**
	 * Gets the test environment.
	 *
	 * @return the test environment
	 */
	@Override
	public String getTestEnvironment() {
		testEnvironment = System.getProperty(TEST_ENVIRONMENT, "");
		return testEnvironment;
	}

	/**
	 * Gets the produkt.
	 *
	 * @return the produkt
	 */
	@Override
	public String getProdukt() {
		produkt = System.getProperty(PRODUKT, "");
		return produkt;
	}

	/**
	 * Common setup.
	 * @return 
	 */
	public SeleniumHelper commonSetup() {
		getProdukt();
		String mandantTestEnvironment = "";
		if (!getMandant().isEmpty()) {
			mandantTestEnvironment = File.separator + getMandant();
			if (!getTestEnvironment().isEmpty()) {
				mandantTestEnvironment = File.separator + getMandant() + "-" + getTestEnvironment();
			}
		}
		testDataPath = Paths.get("").toAbsolutePath().toString() + File.separator + TESTDATADIR
				+ mandantTestEnvironment;
		setTestPlatformProperties(testDataPath + File.separator + TEST_PLATFORM_PROPERTIES);
		return new SeleniumHelper();
	}

	/**
	 * Screenshot file to "RunResults" + File.separator + "Resources" +
	 * File.separator + "Snapshots"
	 *
	 * @param driver the driver
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public String screenshotFile(Object driver) {
		long time = new Date().getTime();
		File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String snapshotsDir = Paths.get("").toAbsolutePath().toString() + File.separator + "RunResults" + File.separator
				+ "Resources" + File.separator + "Snapshots";
		try {
			Files.createDirectories(Paths.get(snapshotsDir));
			String screenShotPath = snapshotsDir + File.separator + time + ".png";
			File destination = new File(screenShotPath);
			Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Resources" + File.separator + "Snapshots" + File.separator + time + ".png";
	}

	/**
	 * Screenshot base 64.
	 *
	 * @param driver the driver
	 * @return the string
	 * @throws Exception the exception
	 */
	public static String screenshotBase64(TakesScreenshot driver) throws Exception {
		TakesScreenshot newScreen = (TakesScreenshot) driver;
		String scnShot = newScreen.getScreenshotAs(OutputType.BASE64);
		return "data:image/jpg;base64, " + scnShot;

	}

}
