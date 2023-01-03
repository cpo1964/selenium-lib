package at.cpo.tests;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.service.DriverService;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.Media;

public class SeleniumHelper {

	@Rule
	public TestRule afterWithFailedInformation;

	protected String name = "";
	protected boolean failed;

	public boolean ok = false;

	protected String value = "";

	protected static ExtentTest test;
	protected static ExtentTest node;

	protected static ExtentReports report;

	protected static RemoteWebDriver driver;

	protected static String browser;

	protected WebElement webEl;

	protected ArrayList<String> logBuffer = new ArrayList<String>();

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

	protected static void setupDriver() {
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

	private static void setupChromeDriver() {
		System.setProperty("webdriver.chrome.silentLogging", "true");
        System.setProperty("webdriver.chrome.verboseLogging", "false");
        System.setProperty("webdriver.chrome.silentOutput", "true");

        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");

		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.setHeadless(true);

		driver = new ChromeDriver(chromeOptions);

//		DriverService.Builder serviceBuilder = new ChromeDriverService.Builder().withSilent(true);
//		ChromeDriverService chromeDriverService = (ChromeDriverService) serviceBuilder.build();
//		chromeDriverService.sendOutputTo(new OutputStream() {
//			@Override
//			public void write(int b) {
//			}
//		});
//		driver = new ChromeDriver(chromeDriverService, chromeOptions);
	}

	@SuppressWarnings("deprecation")
	protected static void setupFirefoxDriver() {
//      System.setProperty("webdriver.gecko.driver", "/usr/local/bin/geckodriver");
//      System.setProperty("webdriver.gecko.driver", "src/test/resources/selenium_standalone_binaries/linux/marionette/64bit/geckodriver");
		System.setProperty("webdriver.gecko.driver",
				"src/test/resources/selenium_standalone_binaries/windows/marionette/64bit/geckodriver.exe");
		java.util.logging.Logger.getLogger("org.openqa.selenium.remote.RemoteWebDriver")
				.setLevel(java.util.logging.Level.SEVERE);
		System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "FFLogs.txt");

		DriverService serviceBuilder = new GeckoDriverService.Builder().build();
		serviceBuilder.sendOutputTo(new OutputStream() {
			@Override
			public void write(int b) {
			}
		});

		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("network.proxy.no_proxies_on", "localhost");
		profile.setPreference("javascript.enabled", true);
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("marionette", true);
		capabilities.setCapability(FirefoxDriver.PROFILE, profile);
		FirefoxOptions options = new FirefoxOptions();
		options.merge(capabilities);
		FirefoxDriverLogLevel x = FirefoxDriverLogLevel.FATAL;
		options.setLogLevel(x);
		options.addPreference("browser.link.open_newwindow", 3);
		options.addPreference("browser.link.open_newwindow.restriction", 0);
//      options.setHeadless(Boolean.getBoolean("headless"));

//		driver = // new FirefoxDriver(options);
//		(RemoteWebDriver) RemoteWebDriver.builder().withDriverService(serviceBuilder).build();

		driver = new FirefoxDriver(options);
//		driver.builder().withDriverService(serviceBuilder);

		/*
		 * WebDriverManager.firefoxdriver().setup(); freischaltung fehlt !
		 * 
		 * io.github.bonigarcia.wdm.config.WebDriverManagerException:
		 * org.apache.hc.client5.http.HttpHostConnectException: Connect to
		 * https://api.github.com:443 [api.github.com/140.82.121.6] failed: Connection
		 * timed out: connect
		 * 
		 * System.setProperty("webdriver.gecko.driver",
		 * "src/test/resources/geckodriver");
		 * 
		 * FirefoxOptions firefoxOptions = new FirefoxOptions(); //
		 * firefoxOptions.setHeadless(true);
		 * firefoxOptions.addArguments("--disable-gpu", "--window-size=1920,1200",
		 * "--ignore-certificate-errors"); // FirefoxBinary firefoxBinary = new
		 * FirefoxBinary(); // firefoxBinary.addCommandLineOptions("--headless"); //
		 * firefoxOptions.setBinary(firefoxBinary);
		 * 
		 * driver = new FirefoxDriver(firefoxOptions); // DevTools devTools =
		 * ((HasDevTools) driver).getDevTools(); // devTools.createSession(); //
		 * devTools.send(Network.enable(Optional.empty(), Optional.empty(),
		 * Optional.empty())); // devTools.addListener(Network.responseReceived(), //
		 * entry -> { // System.out.println("Request Id:"+entry.getRequestId() + "\t" +
		 * entry.getResponse().getUrl()); // });
		 * 
		 * // driver = new FirefoxDriver();
		 */

	}

	protected void closeBrowser() {
		try {
			driver.quit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean exists(String locatorDelegate) {
		return exists(locatorDelegate, false);
	}

	public boolean exists(String locatorDelegate, boolean secret) {
		String xpath = getLocator(locatorDelegate);
		logInfo("exists (" + xpath + ", " + secret + ")");
		webEl = driver.findElement(By.xpath(xpath));
		if (webEl == null) {
			node.log(Status.PASS, "exists (" + xpath + ", " + secret + ") - false");
			return false;
		}
		node.log(Status.PASS, "exists (" + xpath + ", " + secret + ") - true");
		return webEl.isDisplayed();
	}

	public void click(String locatorDelegate) {
		String xpath = getLocator(locatorDelegate);
		logInfo("click (" + xpath + ")");
		webEl = driver.findElement(By.xpath(xpath));
		if (webEl.isEnabled()) {
			// close messages popup
			webEl.click();
			node.log(Status.PASS, "click (" + xpath + ")");
		} else {
			try {
				node.log(Status.FAIL, test.addScreenCaptureFromPath(screenshotFile()) + "click (" + xpath + ")");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void input(String locatorDelegate, String value) {
		input(locatorDelegate, value, false);
//        webEl.submit();
	}

	public String output(String locatorDelegate) {
		String xpath = getLocator(locatorDelegate);
		logInfo("output (" + xpath + ")");
		node.log(Status.PASS, "output (" + xpath + ")");
		webEl = driver.findElement(By.xpath(xpath));
		return webEl.getAttribute("textContent");
	}

	public void input(String locatorDelegate, String value, boolean secret) {
		String desc = getLocator(locatorDelegate);
		String xpathDescription = "";
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
			xpathDescription = "//" + descParts[1];
			webEl = driver.findElement(By.xpath(xpathDescription));
		} else {
			try {
				logSecret(desc + "(unknown) -> not done ", getSecretString(value, secret), secret);
			} catch (IOException e) {
				try {
					logError("input (" + xpathDescription + ", '" + getSecretString(value, secret) + ")'");
					node.log(Status.FAIL, node.addScreenCaptureFromPath(screenshotFile()) + "input (" + xpathDescription
							+ ", '" + getSecretString(value, secret) + ")'");
				} catch (IOException e1) {
					e.printStackTrace();
				}
			}
			return;
		}
		if (xpathDescription.isEmpty()) {
			xpathDescription = locatorDelegate;
		}
		if ("ListBox".equals(className) || "RadioGroup".equals(className)) {
			// TODO select
		} else if ("CheckBox".equals(className)) {
			// TODO check
		} else {
			waitUntilWebelementIsClickable(30, webEl);
			wait(100);
			if (webEl.isEnabled()) {
				webEl.click();
				webEl.clear();
				webEl.sendKeys(value);
				logNode(Status.PASS, "input (" + xpathDescription + ", '" + getSecretString(value, secret) + ")'");
			} else { // webelement does not exist or is disabled
				try {
					logError("input (" + xpathDescription + ", '" + getSecretString(value, secret) + ")'");
					node.log(Status.FAIL, node.addScreenCaptureFromPath(screenshotFile()) + "input (" + xpathDescription
							+ ", '" + getSecretString(value, secret) + ")'");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				logSecret(desc, value, secret);
			} catch (IOException e) {
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
	 */
	private void logSecret(String desc, String value, boolean secret) throws IOException {
		String text = getSecretString(value, secret);
		logDebug("INPUT to " + desc + " value=" + text);
	}

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

	public static void waitUntilFullyLoaded(int timeoutSeconds) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
		wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState")
				.equals("complete"));
	}

	public static void waitUntilWebelementIsClickable(int timeoutSeconds, WebElement webEl) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
		wait.until(ExpectedConditions.elementToBeClickable(webEl));
	}

	public void wait(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Gibt das jeweilige Testdaten Excel zurück.
	 *
	 * @return Das Testdaten Excel als File.
	 */
	protected static File getTestDataFile() throws IOException {
		return new File(Paths.get("").toAbsolutePath().toString() + File.separator + "src" + File.separator + "test"
				+ File.separator + "data" + File.separator + "Testdata.xls");
	}

	public static String screenshotFile() throws IOException {
		long time = new Date().getTime();
		File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE); // clears old console output
		String snapshotsDir = Paths.get("").toAbsolutePath().toString() + File.separator + "RunResults" + File.separator
				+ "Resources" + File.separator + "Snapshots";
		Files.createDirectories(Paths.get(snapshotsDir));
		String screenShotPath = snapshotsDir + File.separator + time + ".png";
		File destination = new File(screenShotPath);
		Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
		return "Resources" + File.separator + "Snapshots" + File.separator + time + ".png";
	}

	public static void screenshotNode() throws IOException {
		Media media = node.addScreenCaptureFromPath(screenshotFile()).getModel().getMedia().get(0);
		node.getModel().getMedia().clear();
		node.log(Status.PASS, media);
//		node.log(Status.PASS, node.addScreenCaptureFromPath(base64conversion()).getModel().getMedia().get(0));
//		node.log(Status.PASS, "start MTours", node.addScreenCaptureFromPath(base64conversion()).getModel().getMedia().get(0));
//		node.log(Status.PASS, "start MTours", node.addScreenCaptureFromBase64String(base64conversion()).getModel().getMedia().get(0));
//		node.addScreenCaptureFromPath(base64conversion());
	}

	public static String screenshotBase64() throws Exception {
		TakesScreenshot newScreen = (TakesScreenshot) driver;
		String scnShot = newScreen.getScreenshotAs(OutputType.BASE64);
		return "data:image/jpg;base64, " + scnShot;

	}

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
	 * @param key the key
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

	protected void logInfo(String msg) {
		logBuffer.add("INFO#" + msg);
	}

	protected void logDebug(String msg) {
		logBuffer.add("DEBUG#" + msg);
	}

	protected void logError(String msg) {
		logBuffer.add("ERROR#" + msg);
	}

	protected void logNode(Status st, String msg) {
		logBuffer.add("INFO#" + msg);
		node.log(st, msg);
	}

	public final static void clearConsole() {
		for (int i = 0; i < 50; ++i) System.out.println("");

//		for (int i = 0; i < 50; ++i) System.out.println("\\r\\b");
//		
//		System.out.print("\033[H\033[2J");
//        System.out.flush();
//        
//        try {
//			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
//		} catch (InterruptedException | IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		System.out.print("\033\143");
//
//		String ESC = "\033[";
//		System.out.print(ESC + "2J");
//		
//		try {
//			final String os = System.getProperty("os.name");
//
//			if (os.contains("Windows")) {
//				Runtime.getRuntime().exec("cls");
//			} else {
//				Runtime.getRuntime().exec("clear");
//			}
//		} catch (final Exception e) {
//			// Handle any exceptions.
//		}
	}

	protected void logAll() {
		clearConsole();

		Logger LOGGER = LogManager.getLogger(getClass());
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
//		org.apache.logging.log4j.Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger(getClass());
//		org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");

		for (String el : logBuffer) {
			if (el.startsWith("INFO#")) {
				LOGGER.info(el.substring(5));
			} else if (el.startsWith("DEBUG#")) {
//				LOGGER.fine(el.substring(5));
				LOGGER.debug(el.substring(5));
			} else if (el.startsWith("ERROR#")) {
//				LOGGER.severe(el.substring(6));
				LOGGER.error(el.substring(6));
			}
		}

	}

}
