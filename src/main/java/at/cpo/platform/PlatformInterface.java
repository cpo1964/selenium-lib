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
import java.io.IOException;
import java.util.Properties;

import at.cpo.report.ReportInterface;

/**
 * The Interface EnvironmentInterface.
 */
public interface PlatformInterface extends ReportInterface {
	
	// definitions of standard webelement types
	// use this values for element definitions in page objects
	
	/** The Constant EDITFIELD. */
	public static final String EDITFIELD = "EditField";

	/** The Constant LISTBOX. */
	public static final String LISTBOX = "ListBox";

	/** The Constant RADIOGROUP. */
	public static final String RADIOGROUP = "RadioGroup";

	/** The Constant RADIOBUTTON. */
	public static final String RADIOBUTTON = "RadioButton";

	/** The Constant CHECKBOX. */
	public static final String CHECKBOX = "CheckBox";

	/** The Constant NUMERICFIELD. */
	public static final String NUMERICFIELD = "NumericField";

	/** The Constant FILEFIELD. */
	public static final String FILEFIELD = "FileField";

	/** The Constant SLIDER. */
	public static final String SLIDER = "Slider";

	/** The Constant BUTTON. */
	public static final String BUTTON = "Button";

	/** The Constant LINK. */
	public static final String LINK = "Link";

	/** The Constant TEXT. */
	public static final String TEXT = "Text";
	
	// values for the verify action
	
	/** The Constant EDITABLE. */
	public static final String EDITABLE = "Editable";

	/** The Constant ENABLED. */
	public static final String ENABLED = "Enabled";

	/** The Constant EXISTS. */
	public static final String EXISTSKEY = "Exists";

	/** The Constant VISIBLE. */
	public static final String VISIBLE = "Visible";

	// values for the click action

	/** The Constant ALTCLICK. */
	public static final String ALTCLICK = "left click while holding the Alt key down";

	/** The Constant CLICK. */
	public static final String CLICKKEY = "click with the left mouse button";

	/** The Constant CONTROLCLICK. */
	public static final String CONTROLCLICK = "left click while holding the Ctrl key down";

	/** The Constant DOUBLECLICK. */
	public static final String DOUBLECLICK = "double click with the left mouse button";

	/** The Constant LONGCLICK. */
	public static final String LONGCLICK = "click with the left mouse button which lasts for two seconds";
	
	/** The Constant MOUSEOVER. */
	public static final String MOUSEOVER = "moves the mouse pointer over the control";

	/** The Constant RIGHTCLICK. */
	public static final String RIGHTCLICK = "click with the right mouse button";

	/** The Constant SHIFTCLICK. */
	public static final String SHIFTCLICK = "left click while holding the Shift key down";

	// platform stuff
	
	public static final String PRODUKTKEY = "produkt";

	/** The Constant TEST_ENVIRONMENT. */
	public static final String TEST_ENVIRONMENT = "testEnvironment";

	/** The Constant MANDANT. */
	public static final String MANDANTKEY = "mandant";

	/** The Constant TEST_PLATFORM_PROPERTIES. */
	public static final String TEST_PLATFORM_PROPERTIES = "test-platform.properties";
	
	/** The Constant TESTDATA_XLS. */
	public static final String TESTDATA_XLS = "Testdata.xls";
	
	/** The Constant TESTDATADIR. */
	public static final String TESTDATADIR = "src" + File.separator + "test" + File.separator + "data";
	
	/** The test platform properties. */
	static Properties testPlatformProperties = new Properties();

	/** The ok. */
	public static boolean OKKEY = false;

	/** The value. */
	public static String VALUEKEY = "";

	/**
	 * Setup driver.
	 *
	 * @return the object
	 */
	Object setupDriver();
	
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
	 * navigate to given url.
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
	 * same behaviour as click(locatorDelegate, CLICK)
	 *
	 * @param locatorDelegate the locator defined by a delegate
	 */
	void click(String locatorDelegate);

	/**
	 * Click at the webelement referenced by locatorDelegate
	 * 
	 * see: https://www.w3schools.com/jsref/event_onclick.asp
	 * 
	 * The onclick event occurs when the user clicks on an HTML element.
	 * onclick is a DOM Level 2 (2001) feature
	 *
	 * see: values for the click action (defined above)
	 *
	 * @param locatorDelegate the locator defined by a delegate
	 * @param value the click action
	 */
	void click(String locatorDelegate, String value);

	/**
	 * Click by xpath.
	 *
	 * @param xpath the xpath
	 */
	void clickByXpath(String xpath);

	/**
	 * Click by xpath.
	 *
	 * @param xpath the xpath
	 * @param value the value
	 */
	void clickByXpath(String xpath, String value);

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

	/**
	 * Input by xpath.
	 *
	 * @param xpath the xpath
	 * @param className the class name
	 * @param value the value
	 */
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
	
	/**
	 * Output by xpath.
	 *
	 * @param xpath the xpath
	 * @return the string
	 */
	String outputByXpath(String xpath);
	
	/**
	 * Exists - check if webelement referenced by locatorDelegate exists.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return true, if successful
	 */
	boolean exists(String locatorDelegate);

	/**
	 * Exists.
	 *
	 * @param locatorDelegate the locator delegate
	 * @param timeout the timeout
	 * @return true, if successful
	 */
	boolean exists(String locatorDelegate, int timeout);

	/**
	 * Exists.
	 *
	 * @param locatorDelegate the locator delegate
	 * @param reportFailed the report failed
	 * @param timeout the timeout
	 * @return true, if successful
	 */
	boolean exists(String locatorDelegate, boolean reportFailed, long timeout);

	/**
	 * Exists by xpath.
	 *
	 * @param xpath the xpath
	 * @return true, if successful
	 */
	boolean existsByXpath(String xpath);

	/**
	 * Exists by xpath.
	 *
	 * @param xpath the xpath
	 * @param reportFailed the report failed
	 * @return true, if successful
	 */
	boolean existsByXpath(String xpath, boolean reportFailed);

	/**
	 * Validate.
	 *
	 * @param condition the condition
	 * @param description the description
	 * @return 
	 */
	boolean validate(boolean condition, String description);
	
	/**
	 * Drag and drop.
	 *
	 * @param locatorFrom the locator from
	 * @param locatorTo the locator to
	 */
	void dragAndDrop(String locatorFrom, String locatorTo);

	/**
	 * Drag and drop by xpath.
	 *
	 * @param xpathFrom the xpath from
	 * @param xpathTo the xpath to
	 */
	void dragAndDropByXpath(String xpathFrom, String xpathTo);

	/**
	 * Screenshot file.
	 *
	 * @param driver the driver
	 * @return the string
	 */
	String screenshotFile(Object driver);
	
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
	 *
	 * @return the platform interface
	 * @throws IOException 
	 */
	PlatformInterface commonSetup() throws IOException;

}
