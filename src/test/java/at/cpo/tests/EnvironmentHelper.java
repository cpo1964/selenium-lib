package at.cpo.tests;

import java.io.File;

public class EnvironmentHelper {

	static EnvironmentInterface environment = new SeleniumHelper();
	
	static String platform = "Selenium";
	
	static String value = "";

	boolean ok;

	protected void setupDriver() {
		environment.setupDriver();	
	}
	
	protected void driverSwitchToDefaultContent() {
		environment.driverSwitchToDefaultContent();
	}

	public void driverImplicitlyWait(long value) {
		environment.driverImplicitlyWait(value);
	}

	protected boolean driverSwitchToIFrame(String name) {
		return environment.driverSwitchToIFrame(name);	
	}

	protected void driverGet(String url) {
		environment.driverGet(url);	
	}

	protected void closeBrowser() {
		environment.closeBrowser();
	}

	protected void click(String locatorDelegate) {
		environment.click(locatorDelegate);	
	}

	protected void input(String locatorDelegate, String value) {
		environment.input(locatorDelegate, value);
	}

	protected String output(String locatorDelegate) {
		return environment.output(locatorDelegate);	
	}

	protected boolean exists(String locatorDelegate) {
		return environment.exists(locatorDelegate);	
	}

	protected void logInfo(String msg) {
		environment.logInfo(msg);
	}

	protected void testLogFail(String msg) {
		environment.testLogFail(msg);
	}

	protected void screenshotNodeFail() {
		environment.screenshotNodeFail();
	}

	protected void testLogPass(String msg) {
		environment.testLogPass(msg);
	}

	protected void screenshotNodePass() {
		environment.screenshotNodePass();
	}

	protected void testCreateNode(String msg) {
		environment.testCreateNode(msg);
	}

	protected void reportCreateTest(String msg) {
		environment.reportCreateTest(msg);
	}

	protected static void tearDownExtent() {
		environment.tearDownExtent();	
	}

	protected static File getTestDataFile() {
		return environment.getTestDataFile();	
	}

	protected void logAll() {
		environment.logAll();
	}

}
