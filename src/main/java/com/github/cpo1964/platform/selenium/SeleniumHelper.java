/*
 * Copyright (C) 2023 Christian Pöcksteiner (christian.poecksteiner@aon.at)
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.cpo1964.platform.selenium;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.github.cpo1964.report.ReportInterface;
import com.github.cpo1964.report.extent.ExtentHelper;
import com.github.cpo1964.utils.BrowserHelper;
import com.github.cpo1964.utils.CommonHelper;
import com.github.cpo1964.utils.ExcelHelper;
import com.github.cpo1964.utils.MaxlevelStreamHandler;

/**
 * The Class SeleniumHelper provides concrete implementations for browser
 * automation. It manages the driver lifecycle, element interactions, and
 * reporting integration.
 */
public class SeleniumHelper extends ExtentHelper implements SeleniumInterface, ReportInterface {

    /** Properties for test configuration. */
    private static Properties testProperties = null;

    /** Logger instance. */
    public static Logger log = Logger.getLogger(SeleniumHelper.class.getSimpleName());

    /** Configuration key for WebDriverManager cache (legacy support). */
    public static final String WDM_CACHE_PATH = "wdm.cachePath";

    /** Reporting string constants. */
    private static final String XPATH_MSG_PART = "\"), value: '";
    private static final String BOLD_INPUT_BY_XPATH = "<b>INPUT   </b> by xpath $(\"";

    /** Public state constants for easier access. */
    public static final WebelementState Displayed = WebelementState.Displayed;
    public static final WebelementState Hidden = WebelementState.Hidden;
    public static final WebelementState Selected = WebelementState.Selected;
    public static final WebelementState UnSelected = WebelementState.UnSelected;
    public static final WebelementState Enabled = WebelementState.Enabled;
    public static final WebelementState Disabled = WebelementState.Disabled;
    public static final WebelementState NotFound = WebelementState.NotFound;
    
    public static final WebelementType EDITFIELD = WebelementType.EDITFIELD; 
    public static final WebelementType BUTTON = WebelementType.BUTTON;
    public static final WebelementType CHECKBOX = WebelementType.CHECKBOX;
    public static final WebelementType FILEFIELD = WebelementType.FILEFIELD;
    public static final WebelementType LINK = WebelementType.LINK;
    public static final WebelementType LISTBOX = WebelementType.LISTBOX;
    public static final WebelementType NUMERICFIELD = WebelementType.NUMERICFIELD;
    public static final WebelementType RADIOBUTTON = WebelementType.RADIOBUTTON;
    public static final WebelementType RADIOGROUP = WebelementType.RADIOGROUP;
    public static final WebelementType SLIDER = WebelementType.SLIDER;
    public static final WebelementType TEXT = WebelementType.TEXT;

    /** Internal state variables. */
    protected static String value;
    /** The active RemoteWebDriver instance. */
    private static RemoteWebDriver driver;
    /** The current or last interacted WebElement. */
    private static WebElement webElement;
    /** Path to the test data directory. */
    private String testDataPath;
    /** Proxy server address. */
    public static String proxy;
    /** Username for proxy authentication. */
    private static String proxyUser;
    /** Password for proxy authentication. */
    private static String proxyPass;
    /** Flag indicating if the driver has been successfully initialized. */
    private static boolean driverLoaded = false;
    /** Success status flag for operations. */
    protected boolean ok;
    /** Default timeout for implicit waits in seconds. */
    private long timeout = 30;
    /** Counter for current test iteration. */
    private static int iteration = 0;

    /**
     * Gets the current iteration.
     * @return the iteration
     */
    public static int getIteration() {
        return iteration;
    }

    /**
     * Sets the iteration.
     * @param value the iteration value
     */
    public static void setIteration(int value) {
        iteration = value;
    }

    /**
     * Gets the active WebDriver.
     * @return the driver
     */
    public static RemoteWebDriver getDriver() {
        return driver;
    }

    /**
     * Initializes the driver based on the browser system property.
     */
    public static void setDriver() {
        String browser = getBrowser();
        if (SeleniumStrings.CHROME.equalsIgnoreCase(browser)) {
            driver = BrowserHelper.getChromeDriver();
        } else if (SeleniumStrings.FIREFOX.equalsIgnoreCase(browser)) {
            driver = BrowserHelper.getFirefoxDriver();
        }
        if (driver != null) {
            log.info(() -> "Driver initialized for browser: " + browser);
            setDriverLoaded(true);
        }
    }

    /**
     * Gets the last located WebElement.
     * @return the web element
     */
    public static WebElement getWebElement() {
        return webElement;
    }

    /**
     * Sets the last located WebElement.
     * @param webEl the web element
     */
    public static void setWebElement(WebElement webEl) {
        webElement = webEl;
    }

    /**
     * Gets the test data path.
     * @return the path
     */
    public String getTestDataPath() {
        return testDataPath;
    }

    /**
     * Sets the test data path.
     * @param testDataPath the path
     */
    public void setTestDataPath(String testDataPath) {
        this.testDataPath = testDataPath;
    }

    /**
     * Checks if the driver is loaded.
     * @return true if NOT loaded
     */
    public static boolean isDriverLoaded() {
        return !driverLoaded;
    }

    /**
     * Sets the driver loaded state.
     * @param driverLoaded the state
     */
    public static void setDriverLoaded(boolean driverLoaded) {
        SeleniumHelper.driverLoaded = driverLoaded;
    }

    /**
     * Detects the browser from system properties.
     * @return the browser name
     */
    public static String getBrowser() {
        String browser = System.getProperty("browser");
        return (browser == null || browser.isEmpty()) ? SeleniumStrings.FIREFOX : browser;
    }

    /**
     * Navigates the browser to the specified URL.
     * @param url the destination URL
     */
    @Override
    public void navigateTo(String url) {
        getDriver().get(url);
    }

    /**
     * Gets the current proxy address.
     * @return the proxy
     */
    public static String getProxy() {
        return proxy;
    }

    /**
     * Sets the proxy address.
     * @param proxy the proxy address to set
     */
    public static void setProxy(String proxy) {
        SeleniumHelper.proxy = proxy;
    }

    /**
     * Gets the proxy username.
     * @return the proxy username
     */
    public static String getProxyUser() {
        return proxyUser;
    }

    /**
     * Sets the proxy username.
     * @param proxyUserValue the username to set
     */
    public static void setProxyUser(String proxyUserValue) {
        proxyUser = proxyUserValue;
    }

    /**
     * Gets the proxy password.
     * @return the proxy password
     */
    public static String getProxyPass() {
        return proxyPass;
    }

    /**
     * Sets the proxy password.
     * @param proxyPass the password to set
     */
    public static void setProxyPass(String proxyPass) {
        SeleniumHelper.proxyPass = proxyPass;
    }

    /**
     * Switches the driver context to an iframe by its name or ID.
     * @param name the name or ID of the iframe
     * @return true if the switch was successful
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
     * Switches the driver context back to the default content (main page).
     */
    @Override
    public void driverSwitchToDefaultContent() {
        getDriver().switchTo().defaultContent();
    }

    /**
     * Gets the current implicit wait timeout in seconds.
     * @return the timeout in seconds
     */
    @Override
    public long getDriverImplicitlyWaitTimoutSeconds() {
        return timeout;
    }

    /**
     * Sets the implicit wait timeout in seconds.
     * @param value the timeout in seconds
     */
    @Override
    public void setDriverImplicitlyWaitTimoutSeconds(long value) {
        timeout = value;
    }

    /**
     * Launches the browser environment.
     * Performs cleanup, configures logs, sets proxy settings, and maximizes the window.
     */
    @Override
    public void launch() {
        BrowserHelper.cleanDriver();
        MaxlevelStreamHandler.setupMaxLevelStreamHandler(log);
        String cachePath = Paths.get("").toAbsolutePath().toString() + File.separator + "src" + File.separator + "test"
                + File.separator + "resources";
        System.setProperty(WDM_CACHE_PATH, cachePath);
        if (System.getProperty("proxy") != null) {
            setProxy(System.getProperty("proxy"));
            setProxyUser(System.getProperty("proxyUser"));
            setProxyPass(System.getProperty("proxyPass"));
        }
        setDriver();
        if (getDriver() != null)
            getDriver().manage().window().maximize();
    }

    /**
     * Closes the browser and quits the WebDriver session.
     */
    @Override
    public void closeBrowser() {
        try {
            if (getDriver() != null)
                getDriver().quit();
        } catch (Exception e) {
            log.warning(e.getMessage());
        }
    }

    /**
     * Performs click by XPath and handles different click actions.
     * @param xpath       the locator
     * @param clickAction the action type
     */
    private void clickByXpath(String xpath, String clickAction) {
        if (isFailed())
            return;
        Actions action = new Actions(getDriver());
        setClicksCount(getClicksCount() + 1);
        if (waitOnBy(By.xpath(xpath), WebelementState.Enabled, getDriverImplicitlyWaitTimoutSeconds())) {
            WebElement el = getWebElement();
            try {
                if (ClickActions.CLICKKEY.name().equals(clickAction)) {
                    try {
                        el.click();
                    } catch (Exception e) {
                        action.moveToElement(el).click().perform();
                    }
                } else if (ClickActions.RIGHTCLICK.name().equals(clickAction)) {
                    action.contextClick(el).perform();
                } else if (ClickActions.DOUBLECLICK.name().equals(clickAction)) {
                    action.doubleClick(el).perform();
                } else if (ClickActions.MOUSEOVER.name().equals(clickAction)) {
                    action.moveToElement(el).perform();
                }
                reportStepPass("<b>CLICK   </b> by xpath $(\"" + xpath + "\")");
            } catch (Exception e) {
                reportStepFail("CLICK failed: " + e.getMessage());
                throw e;
            }
        } else {
            reportStepFailScreenshot();
            throw new NoSuchElementException("Element not found or not enabled: " + xpath);
        }
    }

    /**
     * Clicks an element identified by the locator delegate with a specific action.
     * @param locatorDelegate the locator string
     * @param clickAction the type of click action
     */
    @Override
    public void click(String locatorDelegate, String clickAction) {
        try {
            clickByXpath(LocatorHelper.getLocator(locatorDelegate), clickAction);
        } catch (NotFoundException e) {
            reportStepFail(e.getMessage());
        }
    }

    /**
     * Clicks an element identified by the locator delegate using a standard click.
     * @param locatorDelegate the locator string
     */
    @Override
    public void click(String locatorDelegate) {
        click(locatorDelegate, ClickActions.CLICKKEY.name());
    }

    /**
     * Core input logic for various element types.
     * @param xpath    the locator
     * @param type     the element type
     * @param value    the value to enter
     * @param isSecret masking flag
     */
    private void inputByXpath(String xpath, WebelementType type, String value, boolean isSecret) {
        if (isFailed())
            return;
        setInputsCount(getInputsCount() + 1);
        if (!waitOnBy(By.xpath(xpath), WebelementState.Enabled) || getWebElement() == null) {
            reportStepFailScreenshot();
            throw new NotFoundException("Element not found: " + xpath);
        }
        WebElement el = getWebElement();
        if (WebelementType.EDITFIELD.equals(type) || WebelementType.NUMERICFIELD.equals(type)) {
            el.clear();
            el.sendKeys(value);
            reportStepPass(
                    BOLD_INPUT_BY_XPATH + xpath + XPATH_MSG_PART + CommonHelper.getSecretString(value, isSecret) + "'");
        } else if (WebelementType.LISTBOX.equals(type)) {
            new Select(el).selectByVisibleText(value);
            reportStepPass(BOLD_INPUT_BY_XPATH + xpath + XPATH_MSG_PART + value + "'");
        } else if (WebelementType.CHECKBOX.equals(type) || WebelementType.RADIOBUTTON.equals(type)) {
            boolean shouldBeSelected = CommonHelper.isTrue(value);
            if (el.isSelected() != shouldBeSelected) {
                el.click();
            }
            reportStepPass(BOLD_INPUT_BY_XPATH + xpath + XPATH_MSG_PART + value + "'");
        }
    }

    /**
     * Enters a value into an element, supporting secret masking.
     * @param locatorDelegate the locator string
     * @param value the value to enter
     * @param secret whether to mask the value in reports
     */
    @Override
    public void input(String locatorDelegate, String value, boolean secret) {
        String[] descParts = locatorDelegate.split(File.pathSeparator);
        try {
            inputByXpath(LocatorHelper.getLocator(locatorDelegate), WebelementType.valueOf(descParts[1]), value,
                    secret);
        } catch (Exception e) {
            reportStepFail(e.getMessage());
        }
    }

    /**
     * Enters a value into an element.
     * @param locatorDelegate the locator string
     * @param value the value to enter
     */
    @Override
    public void input(String locatorDelegate, String value) {
        input(locatorDelegate, value, false);
    }

    /**
     * Enters a value into an element with a specified type.
     * @param locatorDelegate the locator string
     * @param type the type of web element
     * @param value the value to enter
     */
    public void input(String locatorDelegate, WebelementType type, String value) {
        input(locatorDelegate, type, value, false);
    }

    /**
     * Enters a value into an element with a specified type and secret masking.
     * @param locatorDelegate the locator string
     * @param type the type of web element
     * @param value the value to enter
     * @param secret whether to mask the value in reports
     */
    public void input(String locatorDelegate, WebelementType type, String value, boolean secret) {
        try {
            inputByXpath(LocatorHelper.getLocator(locatorDelegate), type, value, secret);
        } catch (Exception e) {
            reportStepFail(e.getMessage());
        }
    }

    /**
     * Retrieves the text content of an element.
     * @param locatorDelegate the locator string
     * @return the text content or empty string if failed
     */
    @Override
    public String output(String locatorDelegate) {
        if (isFailed())
            return "";
        String xpath = LocatorHelper.getLocator(locatorDelegate);
        setOutputsCount(getOutputsCount() + 1);
        try {
            WebElement el = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout))
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
            setWebElement(el);
            String text = el.getAttribute("textContent");
            reportStepPass("<b>OUTPUT   </b> by xpath $(\"" + xpath + "\")<br>text: '" + text + "'");
            return text;
        } catch (Exception e) {
            reportStepFailScreenshot();
            return "";
        }
    }

    /**
     * Validates a condition and reports the result.
     * @param condition the condition to check
     * @param description the description of the validation step
     * @return the result of the condition
     */
    @Override
    public boolean validate(boolean condition, String description) {
        if (condition)
            reportStepPass("<b>VALIDATE</b> '" + description + "' - SUCCESS");
        else {
            reportStepFail("<b>VALIDATE</b> '" + description + "' - FAILED");
            reportStepFailScreenshot();
        }
        return condition;
    }

    /**
     * Performs a drag and drop operation between two elements.
     * @param locatorFrom the source element locator
     * @param locatorTo the target element locator
     */
    @Override
    public void dragAndDrop(String locatorFrom, String locatorTo) {
        try {
            WebElement from = getDriver().findElement(By.xpath(LocatorHelper.getLocator(locatorFrom)));
            WebElement to = getDriver().findElement(By.xpath(LocatorHelper.getLocator(locatorTo)));
            new Actions(getDriver()).dragAndDrop(from, to).perform();
        } catch (Exception e) {
            reportStepFail("DragAndDrop failed");
        }
    }

    /**
     * Reports a successful step including a screenshot.
     */
    @Override
    public void reportStepPassScreenshot() {
        reportStepPassScreenshot(screenshotFile());
    }

    /**
     * Reports a failed step including a screenshot.
     */
    @Override
    public void reportStepFailScreenshot() {
        reportStepFailScreenshot(screenshotFile());
    }

    /**
     * Captures a screenshot and returns the file path.
     * @return the relative path to the snapshot
     */
    public String screenshotFile() {
        long time = new Date().getTime();
        try {
            File source = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
            String snapshotsDir = Paths.get("").toAbsolutePath() + File.separator + "RunResults" + File.separator
                    + "Resources" + File.separator + "Snapshots";
            Files.createDirectories(Paths.get(snapshotsDir));
            String fileName = time + ".png";
            File destination = new File(snapshotsDir, fileName);
            Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return "Resources" + File.separator + "Snapshots" + File.separator + fileName;
        } catch (IOException | WebDriverException e) {
            return "";
        }
    }

    /**
     * Internal wait helper with 4 arguments.
     * @param locator the element locator
     * @param state the expected state of the element
     * @param timeout timeout in seconds
     * @param report whether to report the result
     * @return true if the condition was met within timeout
     */
    private boolean waitOnBy(By locator, WebelementState state, long timeout, boolean report) {
        setWaitCount(WaitCount() + 1);
        WebDriverWait wa = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
        try {
            WebElement el = wa
                    .until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfElementLocated(locator)));
            setWebElement(el);
            boolean result = false;
            switch (state) {
            case Enabled:
                result = wa.until(d -> el.isEnabled());
                break;
            case Displayed:
                result = wa.until(ExpectedConditions.visibilityOf(el)) != null;
                break;
            case Hidden:
                result = wa.until(ExpectedConditions.invisibilityOf(el));
                break;
            case Selected:
                result = wa.until(ExpectedConditions.elementToBeSelected(el));
                break;
            case UnSelected:
                result = wa.until(d -> !el.isSelected());
                break;
            default:
                break;
            }
            if (report) {
                if (result)
                    reportStepPass("<b>WAIT  </b> " + locator + " - SUCCESS");
                else
                    reportStepFail("<b>WAIT  </b> " + locator + " - FAILED");
            }
            return result;
        } catch (Exception e) {
            if (report)
                reportStepFail("<b>WAIT  </b> " + locator + " timed out");
            return false;
        }
    }

    /**
     * Internal wait helper with 3 arguments. Resolves compilation error.
     * @param locator the element locator
     * @param state the expected state
     * @param timeout timeout in seconds
     * @return true if condition met
     */
    private boolean waitOnBy(By locator, WebelementState state, long timeout) {
        return waitOnBy(locator, state, timeout, false);
    }

    /**
     * Internal wait helper with 2 arguments.
     * @param locator the element locator
     * @param state the expected state
     * @return true if condition met
     */
    private boolean waitOnBy(By locator, WebelementState state) {
        return waitOnBy(locator, state, getDriverImplicitlyWaitTimoutSeconds(), false);
    }

    /**
     * Waits for an element to reach a specific state with reporting and custom timeout.
     * @param locatorDelegate the locator string
     * @param state the expected state
     * @param timeout timeout in seconds
     * @param report whether to report the result
     * @return true if condition met
     */
    @Override
    public boolean waitOn(String locatorDelegate, WebelementState state, long timeout, boolean report) {
        return waitOnBy(By.xpath(LocatorHelper.getLocator(locatorDelegate)), state, timeout, report);
    }

    /**
     * Waits for an element using default timeout.
     * @param locatorDelegate the locator string
     * @param state the expected state
     * @return true if condition met
     */
    @Override
    public boolean waitOn(String locatorDelegate, WebelementState state) {
        return waitOn(locatorDelegate, state, getDriverImplicitlyWaitTimoutSeconds(), false);
    }

    /**
     * Waits for an element with custom reporting.
     * @param locatorDelegate the locator string
     * @param state the expected state
     * @param report whether to report
     * @return true if condition met
     */
    @Override
    public boolean waitOn(String locatorDelegate, WebelementState state, boolean report) {
        return waitOn(locatorDelegate, state, getDriverImplicitlyWaitTimoutSeconds(), report);
    }

    /**
     * Waits for an element with custom timeout.
     * @param locatorDelegate the locator string
     * @param state the expected state
     * @param timeout timeout in seconds
     * @return true if condition met
     */
    @Override
    public boolean waitOn(String locatorDelegate, WebelementState state, long timeout) {
        return waitOn(locatorDelegate, state, timeout, false);
    }

    /**
     * Static helper to wait for full page load.
     * @param timeoutSeconds timeout
     */
    public static void waitOnFullyLoaded(long timeoutSeconds) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(timeoutSeconds))
                .until(d -> ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));
    }

    /**
     * Loads test data from Excel.
     * @param testDataPath base path
     * @param simpleName   sheet name
     * @return data list
     * @throws IOException on error
     */
    public static List<Object[]> getTestdata(String testDataPath, String simpleName) throws IOException {
        File file = new File(testDataPath, SeleniumStrings.TESTDATA_XLS);
        return new ExcelHelper(file, simpleName).getData();
    }

    /**
     * Internal helper to load test properties.
     * @return properties object
     */
    private static Properties getTestProperties() {
        if (testProperties == null) {
            String path = CommonHelper.getTestDataPathByMandantZone() + File.separator + "test-platform.properties";
            testProperties = CommonHelper.getProperties(path);
        }
        return testProperties;
    }

    /**
     * Retrieves a property value.
     * @param key the key
     * @return the value
     */
    public static String getTestPlatformProperty(String key) {
        return getTestProperties().getProperty(key);
    }
}