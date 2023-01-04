package at.cpo.tests;

import java.io.File;

public interface EnvironmentInterface {

	// Selenium stuff
	
	void setupDriver();
	
	void driverSwitchToDefaultContent();
	
	void driverImplicitlyWait(long value);

	boolean driverSwitchToIFrame(String name);

	void closeBrowser();

	void driverGet(String url);

	void click(String locatorDelegate);

	void input(String locatorDelegate, String value);

	String output(String locatorDelegate);

	boolean exists(String locatorDelegate);

	// ExtentReport stuff

	void reportCreateTest(String msg);

	void testCreateNode(String msg);

	void testLogFail(String msg);

	void testLogPass(String msg);

	void screenshotNodePass();

	void screenshotNodeFail();

	void tearDownExtent();

	// logging stuff

	void logInfo(String msg);

	void logDebug(String msg);

	void logError(String msg);

	void logAll();

	// other stuff

	File getTestDataFile();

}
