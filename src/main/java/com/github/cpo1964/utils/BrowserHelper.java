package com.github.cpo1964.utils;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import com.github.cpo1964.platform.selenium.SeleniumHelper;

/**
 * The Class BrowserHelper provides utility methods for browser management. It
 * handles OS detection, browser path resolution, driver initialization, and
 * system-level cleanup of driver processes.
 */
public class BrowserHelper {

    /** Constant for Chrome browser identifier. */
    public static final String CHROME = "chrome";

    /** Constant for Firefox browser identifier. */
    public static final String FIREFOX = "firefox";

    /**
     * Determines a simplified OS label at runtime.
     * 
     * @return "win", "lin" or "mac" based on the system properties.
     */
    public static String getOperatingSystem() {
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("win")) {
            return "win";
        } else if (osName.contains("mac")) {
            return "mac";
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")
                || osName.contains("freebsd")) {
            // Since XigmaNAS is based on FreeBSD, it is correctly classified here as "lin"
            // (Unix-like)
            return "lin";
        }
        return "lin"; // Fallback to Linux/Unix standard
    }

    /**
     * Determines the browser executable path based on OS and browser type.
     * 
     * @param os      the operating system string ("win", "lin", or "mac")
     * @param browser the browser type ("firefox" or "chrome")
     * @return The absolute path to the executable or null if not found.
     */
    public static String getBrowserPath(String os, String browser) {
        String path = null;
        os = os.toLowerCase();
        browser = browser.toLowerCase();

        if (browser.equals(FIREFOX)) {
            if (os.contains("win")) {
                path = findFirstExisting("C:\\Program Files\\Mozilla Firefox\\firefox.exe",
                        "C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe");
            } else if (os.contains("mac")) {
                path = "/Applications/Firefox.app/Contents/MacOS/firefox";
            } else if (os.contains("lin")) {
                path = findFirstExisting("/snap/bin/firefox", "/usr/bin/firefox", "/usr/local/bin/firefox", // Typical for FreeBSD/XigmaNAS
                        "/opt/firefox/firefox");
            }
            System.out.println("found firefox: " + path);
        } else if (browser.equals(CHROME)) {
            if (os.contains("win")) {
                path = findFirstExisting("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe",
                        "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe",
                        System.getProperty("user.home") + "\\AppData\\Local\\Google\\Chrome\\Application\\chrome.exe");
            } else if (os.contains("mac")) {
                path = "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome";
            } else if (os.contains("lin")) {
                path = findFirstExisting("/usr/bin/google-chrome", "/usr/bin/google-chrome-stable", "/usr/bin/chromium-browser",
                        "/usr/local/bin/chrome");
            }
            System.out.println("found chrome: " + path);
        }

        return path;
    }

    /**
     * Finds the first existing file path from a list of candidates.
     * 
     * @param paths varargs of potential file paths
     * @return the first path that exists on the file system, or null if none exist
     */
    private static String findFirstExisting(String... paths) {
        for (String p : paths) {
            if (new File(p).exists()) {
                return p;
            }
        }
        return null;
    }

    /**
     * Creates a ChromeDriver instance using native Selenium Manager. Configures
     * sandbox, GPU settings, and headless mode based on system properties.
     * 
     * @return the initialized ChromeDriver
     */
    public static ChromeDriver getChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox", "--remote-allow-origins=*", "--disable-gpu");
        if ("true".equalsIgnoreCase(System.getProperty("headless"))) {
            options.addArguments("--headless=true");
        }

        // Proxy support via Selenium Options if needed
        if (SeleniumHelper.proxy != null && !SeleniumHelper.proxy.isEmpty()) {
            org.openqa.selenium.Proxy seleniumProxy = new org.openqa.selenium.Proxy();
            seleniumProxy.setHttpProxy(SeleniumHelper.proxy).setSslProxy(SeleniumHelper.proxy);
            options.setProxy(seleniumProxy);
        }

        return new ChromeDriver(options);
    }

    /**
     * Creates a FirefoxDriver instance using native Selenium Manager. Configures
     * binary path, headless mode, and page load strategy.
     * 
     * @return the initialized FirefoxDriver
     */
    public static FirefoxDriver getFirefoxDriver() {
        FirefoxOptions options = new FirefoxOptions();
        if ("true".equalsIgnoreCase(System.getProperty("headless"))) {
            //options.setBinary(getBrowserPath(getOperatingSystem(), FIREFOX));
            options.addArguments("--headless");
            options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        }

        if (SeleniumHelper.proxy != null && !SeleniumHelper.proxy.isEmpty()) {
            org.openqa.selenium.Proxy seleniumProxy = new org.openqa.selenium.Proxy();
            seleniumProxy.setHttpProxy(SeleniumHelper.proxy).setSslProxy(SeleniumHelper.proxy);
            options.setProxy(seleniumProxy);
        }

        return new FirefoxDriver(options);
    }

    /**
     * Terminates orphaned driver processes (chromedriver/geckodriver) on Windows
     * and Linux systems. This ensures a clean state before starting a new test
     * execution.
     */
    public static void cleanDriver() {
        if (getOperatingSystem().contains("win")) {
            try {
                new ProcessBuilder("taskkill", "/F", "/IM", "chromedriver.exe").start();
                new ProcessBuilder("taskkill", "/F", "/IM", "geckodriver.exe").start();
            } catch (IOException e) {
                SeleniumHelper.log.finest("No active driver processes found to terminate.");
            }
        } else if (getOperatingSystem().contains("lin") || getOperatingSystem().contains("mac")) {
            try {
                new ProcessBuilder("pkill", "chromedriver").start();
                new ProcessBuilder("pkill", "geckodriver").start();
            } catch (IOException e) {
                SeleniumHelper.log.finest("No active driver processes found to terminate.");
            }
        }
    }

    /**
     * Checks if a specific URL is available and returns a successful HTTP 200 response.
     * <p>
     *
     * @param urlString the full URL string to be checked
     * @return {@code true} if the connection is successful and the server responds with HTTP 200 (OK);
     * {@code false} otherwise or if an exception occurs during the check.
     */
    public static boolean isUrlAvailable(String urlString) {
        try {
            HttpURLConnection con = (HttpURLConnection) URI.create(urlString)
                    .toURL()
                    .openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(2000);
            con.setReadTimeout(2000);
            return (con.getResponseCode() == 200);
        } catch (Exception e) {
            return false;
        }
    }
}
