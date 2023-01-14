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
package at.cpo.platform;

import java.io.File;
import java.util.Properties;

import at.cpo.report.ReportInterface;

/**
 * The Interface EnvironmentInterface.
 */
public interface PlatformInterface extends ReportInterface {
	
	// platform stuff
	
	/** The after with failed information. */
	//	@Rule
	//	public TestRule afterWithFailedInformation;
	
	public static final String PRODUKT = "produkt";

	public static final String TEST_ENVIRONMENT = "testEnvironment";

	public static final String MANDANT = "mandant";

	public static final String TEST_PLATFORM_PROPERTIES = "test-platform.properties";
	
	public static final String TESTDATA_XLS = "Testdata.xls";
	
	public static final String TESTDATADIR = "src" + File.separator + "test" + File.separator + "data";
	
	public static Properties testPlatformProperties = new Properties();

	/** The ok. */
	public static boolean ok = false;

	/** The value. */
	public static String value = "";

	// definition of standard webelement types
	
	public static final String EDITFIELD = "EditField";

	public static final String LISTBOX = "ListBox";

	public static final String RADIOGROUP = "RadioGroup";

	public static final String CHECKBOX = "CheckBox";

	public static final String NUMERICFIELD = "NumericField";

	public static final String FILEFIELD = "FileField";

	public static final String SLIDER = "Slider";

	public static final String BUTTON = "Button";

	public static final String LINK = "Link";

	public static final String TEXT = "Text";

	/**
	 * Setup driver.
	 */
	void setupDriver();
	
	/**
	 * Driver switch to default content.
	 */
	void driverSwitchToDefaultContent();
	
	/**
	 * Driver implicitly wait.
	 *
	 * @param value the value
	 */
	void driverImplicitlyWait(long value);

	/**
	 * Driver switch to I frame.
	 *
	 * @param name the name
	 * @return true, if successful
	 */
	boolean driverSwitchToIFrame(String name);

	/**
	 * Close browser.
	 */
	void closeBrowser();

	/**
	 * Driver get.
	 *
	 * @param url the url
	 */
	void driverGet(String url);

	/**
	 * Click.
	 *
	 * @param locatorDelegate the locator delegate
	 */
	void click(String locatorDelegate);

	/**
	 * Input.
	 *
	 * @param locatorDelegate the locator delegate
	 * @param value the value
	 */
	void input(String locatorDelegate, String value);

	/**
	 * Output.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the string
	 */
	String output(String locatorDelegate);
	
	/**
	 * Validate.
	 *
	 * @param description the description
	 */
	void validate(boolean condition, String description);


	/**
	 * Exists.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return true, if successful
	 */
	boolean exists(String locatorDelegate);

	// other stuff

	/**
	 * Gets the mandant.
	 *
	 * @return the mandant
	 */
	String getMandant();
	
	/**
	 * Gets the test environment.
	 *
	 * @return the test environment
	 */
	String getTestEnvironment();

	/**
	 * Gets the produkt.
	 *
	 * @return the produkt
	 */
	String getProdukt();
	
	/**
	 * Gets the test data file.
	 *
	 * @return the test data file
	 */
	File getTestDataFile();
	
	/**
	 * Common setup.
	 */
	void commonSetup();

}
