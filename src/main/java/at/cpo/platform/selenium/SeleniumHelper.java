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
import java.io.OutputStream;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.SystemUtils;
//import org.junit.Rule;
//import org.junit.rules.TestRule;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.service.DriverService;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import at.cpo.platform.PlatformInterface;
import at.cpo.report.extent.ExtentHelper;

/**
 * The Class SeleniumHelper.
 */
public class SeleniumHelper extends ExtentHelper implements PlatformInterface {

	/** The name. */
	protected String name = "";

	/** The failed. */
	protected boolean failed;

	/** The ok. */
	public boolean ok = false;

	/** The value. */
	protected String value = "";

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

	private String testDataPath;

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
	public void setupDriver() {
		browser = System.getProperty("browser");
		if (browser == null || browser.isEmpty()) {
			browser = "firefox";
		}
		// see:
		// https://github.com/bharadwaj-pendyala/selenium-java-lean-test-achitecture
		// https://github.com/bharadwaj-pendyala/selenium-java-lean-test-achitecture/blob/master/src/main/java/driver/local/LocalDriverManager.java

		if ("chrome".equalsIgnoreCase(browser)) {
			setupChromeDriver();
		} else if ("firefox".equalsIgnoreCase(browser)) {
			setupFirefoxDriver();
		}
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
//		driver.manage().window().maximize();
//        driver.manage().window().setSize(new Dimension(1900, 1000));

		driver.setLogLevel(java.util.logging.Level.SEVERE);
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
		if (SystemUtils.IS_OS_LINUX) {
//			System.setProperty("webdriver.gecko.driver", "/usr/local/bin/geckodriver");
			System.setProperty("webdriver.gecko.driver",
					"src/test/resources/selenium_standalone_binaries/linux/marionette/64bit/geckodriver");
		} else if (SystemUtils.IS_OS_WINDOWS) {
			System.setProperty("webdriver.gecko.driver",
					"src/test/resources/selenium_standalone_binaries/windows/marionette/64bit/geckodriver.exe");
		}

		java.util.logging.Logger.getLogger("org.openqa.selenium.remote.RemoteWebDriver")
				.setLevel(java.util.logging.Level.SEVERE);
		System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "FFLogs.txt");

		DriverService serviceBuilder = new GeckoDriverService.Builder().build();
		serviceBuilder.sendOutputTo(new OutputStream() {
			@Override
			public void write(int b) {
			}
		});

		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("marionette", true);
		FirefoxOptions options = new FirefoxOptions();
		options.merge(capabilities);
		options.setLogLevel(FirefoxDriverLogLevel.FATAL);
		options.addPreference("browser.link.open_newwindow", 3);
		options.addPreference("browser.link.open_newwindow.restriction", 0);
//      options.setHeadless(Boolean.getBoolean("headless"));

		driver = new FirefoxDriver(options);

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
			driver.quit();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	 * @param secret          the secret
	 * @return true, if successful
	 */
	public boolean exists(String locatorDelegate, boolean secret) {
		String xpath = getLocator(locatorDelegate);
		webEl = driver.findElement(By.xpath(xpath));
		if (webEl == null) {
			reportStepPass("exists by xpath $(\"" + xpath + "\") - false");
			return false;
		}
		reportStepPass("exists by xpath $(\"" + xpath + "\"), " + secret + " - true");
		return webEl.isDisplayed();
	}

	/**
	 * Click.
	 *
	 * @param locatorDelegate the locator delegate
	 */
	public void click(String locatorDelegate) {
		String xpath = getLocator(locatorDelegate);
		webEl = driver.findElement(By.xpath(xpath));
		if (webEl.isEnabled()) {
			webEl.click();
			reportStepPass("click by xpath $(\"" + xpath + "\")");
		} else {
			try {
				reportStepFail(test.addScreenCaptureFromPath(screenshotFile(driver)) + "click by xpath $(\"" + xpath + "\")");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
	 * Output.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the string
	 */
	public String output(String locatorDelegate) {
		String xpath = getLocator(locatorDelegate);
		webEl = driver.findElement(By.xpath(xpath));
		String output = webEl.getAttribute("textContent");
		reportStepPass("output by xpath $(\"" + xpath + "\")<br>text: " + output);
		return output;
	}

	/**
	 * Input.
	 *
	 * @param locatorDelegate the locator delegate
	 * @param value           the value
	 * @param secret          the secret
	 */
	public void input(String locatorDelegate, String value, boolean secret) {
		String xpath = "";
		try {
			String desc = getLocator(locatorDelegate);
			String className = "";
			String[] descParts = {};
			if (desc.contains("://")) {
				descParts = desc.split("://");
			}
			if (desc.contains(":(//")) {
				descParts = desc.split(":\\(//");
			}
			if (descParts.length == 2) {
				className = descParts[0];
				xpath = "//" + descParts[1];
				webEl = driver.findElement(By.xpath(xpath));
			} else {
				try {
					logSecret(desc + "(unknown) -> not done ", getSecretString(value, secret), secret);
				} catch (IOException e) {
					try {
						logError("input by xpath $(\"" + xpath + "\"), value: '" + getSecretString(value, secret) + "'");
						reportStepFail(node.addScreenCaptureFromPath(ExtentHelper.screenshotFile(driver)) + "input ("
								+ xpath + ", '" + getSecretString(value, secret) + ")'");
					} catch (IOException e1) {
						e.printStackTrace();
					}
				}
				return;
			}
			if (xpath.isEmpty()) {
				xpath = locatorDelegate;
			}
			waitUntilWebelementIsClickable(30, webEl);
			wait(100);
			if (!webEl.isEnabled()) {
				throw new RuntimeException();
			}
			if ("ListBox".equalsIgnoreCase(className) || "RadioGroup".equals(className)) {
				waitUntilWebelementIsClickable(30, webEl);
				wait(100);
				Select lb = (Select) webEl;
				lb.selectByValue(value);
				reportStepPass("input by xpath $(\"" + xpath + "\"), value: '" + value + "'");
			} else if ("RadioGroup".equalsIgnoreCase(className)) {
				int option = Integer.valueOf(value);
				List<WebElement> radios = driver.findElements(By.xpath(xpath));
				if (option > 0 && option <= radios.size()) {
					radios.get(option - 1).click();
					reportStepPass("input by xpath $(\"" + xpath + "\"), value: '" + value + "'");
				} else {
					throw new NotFoundException("input by xpath $(\"" + xpath + "\"), value not found: '" + value + "'");
				}
			} else if ("CheckBox".equalsIgnoreCase(className)) {
				if (webEl.isSelected() && "OFF".equalsIgnoreCase(value)) {
					webEl.click();
					reportStepPass("input by xpath $(\"" + xpath + "\"), value: '" + value + "'");
				} else if (!webEl.isSelected() && "ON".equalsIgnoreCase(value)) {
					webEl.click();
					reportStepPass("input by xpath $(\"" + xpath + "\"), value: '" + value + "'");
				} else {
					throw new NotFoundException("input by xpath $(\"" + xpath + "\"), value not found: '" + value + "'");
				}
			} else if ("NumericField".equalsIgnoreCase(className)) {
				webEl.sendKeys(value);
				reportStepPass("input by xpath $(\"" + xpath + "\"), value: '" + value + "'");
			} else if ("FileField".equalsIgnoreCase(className)) {
				webEl.sendKeys(value);
				reportStepPass("input by xpath $(\"" + xpath + "\"), value: '" + value + "'");
			} else if ("Slider".equalsIgnoreCase(className)) {
				webEl.sendKeys(value);
				reportStepPass("input by xpath $(\"" + xpath + "\"), value: '" + value + "'");
			} else {
				webEl.click();
				webEl.clear();
				webEl.sendKeys(value);
				reportStepPass("input by xpath $(\"" + xpath + "\"), value: '" + getSecretString(value, secret) + "'");
				try {
					logSecret(desc, value, secret);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (RuntimeException e) {
			// webelement does not exist or is disabled
			try {
				logError("input by xpath $(\"" + xpath + "\"), value: '" + getSecretString(value, secret) + "'");
				reportStepFail(node.addScreenCaptureFromPath(ExtentHelper.screenshotFile(driver)) + "input ("
						+ xpath + ", '" + getSecretString(value, secret) + ")'");
			} catch (IOException e1) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Log secret.
	 *
	 * @param desc   the desc
	 * @param value  the value
	 * @param secret the secret
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void logSecret(String desc, String value, boolean secret) throws IOException {
		String text = getSecretString(value, secret);
		logDebug("INPUT to " + desc + " value=" + text);
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
	 * @param timeoutSeconds the timeout seconds
	 * @param webEl          the web el
	 */
	public static void waitUntilWebelementIsClickable(int timeoutSeconds, WebElement webEl) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
		wait.until(ExpectedConditions.elementToBeClickable(webEl));
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
		String cn = locatorDelegate.split("\\.")[0];
		String key = locatorDelegate.replace(cn + ".", "");
		Class<?> c = null;
		try {
			c = Class.forName("at.cpo.selenium.common.pageobjects." + cn);
		} catch (ClassNotFoundException e) {
			return null;
		}
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
			logError("No value found by key '" + key + "' from '" + propertiesFileDestination + "'");
			throw new IllegalArgumentException("Property \"" + key + "\" from file " + propertiesFileDestination
					+ " does not exists or is empty!");
		}
		logDebug("Found value '" + (!key.equals("password") ? value : "*****") + "' by key '" + key + "' from file '"
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
	 * Common setup.
	 */
	public void commonSetup() {
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

}
