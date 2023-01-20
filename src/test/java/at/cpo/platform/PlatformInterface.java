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
	
	// definition of standard webelement types
	
	public static final String EDITFIELD = "EditField";

	public static final String LISTBOX = "ListBox";

	public static final String RADIOGROUP = "RadioGroup";

	public static final String RADIOBUTTON = "RadioButton";

	public static final String CHECKBOX = "CheckBox";

	public static final String NUMERICFIELD = "NumericField";

	public static final String FILEFIELD = "FileField";

	public static final String SLIDER = "Slider";

	public static final String BUTTON = "Button";

	public static final String LINK = "Link";

	public static final String TEXT = "Text";

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

	/**
	 * Setup driver.
	 */
	void setupDriver();
	
	/**
	 * Driver implicitly wait.
	 *
	 * @param value the value
	 */
	void driverImplicitlyWait(long value);

	/**
	 * Driver switch to default content.
	 * 
	 * Selects either the first frame on the page, or the main document when a page contains iframes.
	 */
	void driverSwitchToDefaultContent();
	
	/**
	 * Driver switch to I frame.
	 * 
	 * Send future commands to iFrame
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
	 * navigate to given url
	 *
	 * @param url the url
	 */
	void driverGet(String url);

	/**
	 * Click at the webelement referenced by locatorDelegate
	 * 
	 * see: https://www.w3schools.com/jsref/event_onclick.asp
	 * 
	 * The onclick event occurs when the user clicks on an HTML element.
	 * onclick is a DOM Level 2 (2001) feature
	 *
	 * @param locatorDelegate the locator delegate
	 */
	void click(String locatorDelegate);

	void clickByXpath(String xpath);

	/**
	 * Input at the webelement referenced by locatorDelegate
	 * 
	 * see: https://www.w3schools.com/html/html_form_input_types.asp
	 * 
	 * possible types:
	 *     <input type="radio">
	 *     <input type="checkbox">
	 *     <input type="number">
	 *     <input type="file">
	 *     <input type="range"> // vulgo 'slider'
	 *     <input type="text">
	 *     
	 *     text fields with value validation:
	 *     <input type="email"> // text field with email validation
	 *     <input type="password"> // text field with hidden input
	 *     <input type="tel"> // text field with tel validation
	 *     <input type="date"> // text field with date validation
	 *     <input type="datetime-local"> // text field with datetime-local validation
	 *     <input type="time"> // text field with time validation
	 *     <input type="month"> // text field with month validation
	 *     <input type="week"> // text field with week validation
	 *     <input type="url"> // text field with url validation
	 *     
	 *     <input type="color"> 
	 *     		is used for input fields that should contain a color.
	 *     		Depending on browser support, a color picker can show up in the input field.
	 *     
	 *     <input type="hidden">
	 *     		defines a hidden input field (not visible to a user).
	 *     		not clickable => should not be supported by this method 'input'
	 *     
	 *     <input type="button"> 
	 *     		defines a button, value = text at button
	 *     		default used as click weblement => should not be supported by this method 'input'
	 *     <input type="image">
	 *     		used as submit button
	 *     		default used as click weblement => should not be supported by this method 'input'
	 *     <input type="submit">
	 *     		used as submit button
	 *     		default used as click weblement => should not be supported by this method 'input'
	 *     <input type="reset">
	 *     		defines a reset button that will reset all form values to their default values
	 *     		default used as click weblement => should not be supported by this method 'input'
	 *     <input type="search">
	 *     		is used for search fields (a search field behaves like a regular text field).
	 *     		default used as click weblement => should not be supported by this method 'input'
	 *
	 * @param locatorDelegate the locator delegate
	 * @param value the value
	 */
	void input(String locatorDelegate, String value);

	void inputByXpath(String xpath, String className, String value);

	/**
	 * Output from the webelement referenced by locatorDelegate
	 * 
	 * see: https://www.w3schools.com/jsref/prop_node_textcontent.asp
	 * 
	 * element.textContent is a DOM Level 3 (2004) feature.
	 * 
	 * The textContent property sets or returns the text content of the specified node, and all its descendants.
	 * eg by text = element.textContent;
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the string
	 */
	String output(String locatorDelegate);
	
	String outputByXpath(String xpath);
	
	/**
	 * Exists - check if webelement referenced by locatorDelegate exists
	 *
	 * @param locatorDelegate the locator delegate
	 * @return true, if successful
	 */
	boolean exists(String locatorDelegate);

	boolean existsByXpath(String xpath);

	/**
	 * Validate.
	 *
	 * @param description the description
	 */
	void validate(boolean condition, String description);

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

	boolean existsByXpath(String xpath, boolean reportFailed);

}
