/*
 * Copyright (C) 2023 Christian Pöcksteiner (christian.poecksteiner@aon.at)
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *         https://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.cpo1964.platform.selenium;

import java.io.IOException;
import java.util.Properties;

import javax.naming.ConfigurationException;

import com.github.cpo1964.report.ReportInterface;
import org.openqa.selenium.By;

/**
 * The Interface EnvironmentInterface.
 */
public interface SeleniumInterface extends ReportInterface {

	/**
	 * Common setup.
	 *
	 * @param platformDelegate the platform delegate
	 * @param testDataPath     the test data path
	 * @return the platform interface
	 * @throws IOException            Signals that an I/O exception has occurred.
	 * @throws ConfigurationException the configuration exception
	 */
	SeleniumInterface commonSetup(String platformDelegate, String testDataPath) throws IOException, ConfigurationException;

	/**
	 * Common teardown.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void commonTeardown() throws IOException;

	/**
	 * Setup driver.
	 *
	 * @return the object
	 */
	Object setupDriver();

	/**
	 * Gets the driver implicitly wait timout seconds.
	 *
	 * @return the driver implicitly wait timout seconds
	 */
	long getDriverImplicitlyWaitTimoutSeconds();

	/**
	 * Driver implicitly wait.
	 *
	 * @param value the value
	 */
	void setDriverImplicitlyWaitTimoutSeconds(long value);

	/**
	 * Driver switch to default content.
	 * <p>
	 * Selects either the first frame on the page, or the main document when a page contains iframes.
	 */
	void driverSwitchToDefaultContent();

	/**
	 * Driver switch to I frame.
	 * <p>
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
	 * <p>
	 * see: https://www.w3schools.com/jsref/event_onclick.asp
	 * <p>
	 * The onclick event occurs when the user clicks on an HTML element.
	 * onclick is a DOM Level 2 (2001) feature
	 * <p>
	 * same behaviour as click(locatorDelegate, CLICK)
	 *
	 * @param locatorDelegate the locator defined by a delegate
	 */
	void click(String locatorDelegate);

	/**
	 * Click at the webelement referenced by locatorDelegate
	 * <p>
	 * see: https://www.w3schools.com/jsref/event_onclick.asp
	 * <p>
	 * The onclick event occurs when the user clicks on an HTML element.
	 * onclick is a DOM Level 2 (2001) feature
	 * <p>
	 * see: values for the click action (defined above)
	 *
	 * @param locatorDelegate the locator defined by a delegate
	 * @param clickAction     the click action
	 */
	void click(String locatorDelegate, String clickAction);

	/**
	 * Click by xpath.
	 *
	 * @param xpath the xpath
	 */
	void clickByXpath(String xpath);

	/**
	 * Click by xpath.
	 *
	 * @param xpath       the xpath
	 * @param clickAction the value
	 */
	void clickByXpath(String xpath, String clickAction);

	/**
	 * Input at the webelement referenced by locatorDelegate
	 * <p>
	 * see: https://www.w3schools.com/html/html_form_input_types.asp
	 * <p>
	 * possible types:
	 * input type="radio"
	 * input type="checkbox"
	 * input type="number"
	 * input type="file"
	 * input type="range" // vulgo 'slider'
	 * input type="text"
	 * <p>
	 * text fields with value validation:
	 * input type="email" // text field with email validation
	 * input type="password" // text field with hidden input
	 * input type="tel" // text field with tel validation
	 * input type="date" // text field with date validation
	 * input type="datetime-local" // text field with datetime-local validation
	 * input type="time" // text field with time validation
	 * input type="month" // text field with month validation
	 * input type="week" // text field with week validation
	 * input type="url" // text field with url validation
	 * <p>
	 * input type="color"
	 * is used for input fields that should contain a color.
	 * Depending on browser support, a color picker can show up in the input field.
	 * <p>
	 * input type="hidden"
	 * defines a hidden input field (not visible to a user).
	 * not clickable - should not be supported by this method 'input'
	 * <p>
	 * input type="button"
	 * defines a button, value = text at button
	 * default used as click weblement - should not be supported by this method 'input'
	 * input type="image"
	 * used as submit button
	 * default used as click weblement - should not be supported by this method 'input'
	 * input type="submit"
	 * used as submit button
	 * default used as click weblement - should not be supported by this method 'input'
	 * input type="reset"
	 * defines a reset button that will reset all form values to their default values
	 * default used as click weblement - should not be supported by this method 'input'
	 * input type="search"
	 * is used for search fields (a search field behaves like a regular text field).
	 * default used as click weblement - should not be supported by this method 'input'
	 *
	 * @param locatorDelegate the locator delegate
	 * @param value           the value
	 */
	void input(String locatorDelegate, String value);

	/**
	 * Input.
	 *
	 * @param locatorDelegate the locator delegate
	 * @param value           the value
	 * @param secret          the secret
	 */
	void input(String locatorDelegate, String value, boolean secret);

	/**
	 * Input by xpath.
	 *
	 * @param xpath     the xpath
	 * @param type the class name
	 * @param value     the value
	 */
	void inputByXpath(String xpath, String type, String value);

	/**
	 * Input by xpath.
	 *
	 * @param xpath     the xpath
	 * @param type the class name
	 * @param value     the value
	 * @param secret          the secret
	 */
	void inputByXpath(String xpath, String type, String value, boolean secret);

	/**
	 * Output from the webelement referenced by locatorDelegate
	 * <p>
	 * see: https://www.w3schools.com/jsref/prop_node_textcontent.asp
	 * <p>
	 * element.textContent is a DOM Level 3 (2004) feature.
	 * <p>
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
	 * Validate.
	 *
	 * @param condition   the condition
	 * @param description the description
	 * @return true, if successful
	 */
	boolean validate(boolean condition, String description);

	/**
	 * Drag and drop.
	 *
	 * @param locatorFrom the locator from
	 * @param locatorTo   the locator to
	 */
	void dragAndDrop(String locatorFrom, String locatorTo);

	/**
	 * Drag and drop by xpath.
	 *
	 * @param xpathFrom the xpath from
	 * @param xpathTo   the xpath to
	 */
	void dragAndDropByXpath(String xpathFrom, String xpathTo);

	/**
	 * Screenshot file.
	 *
	 * @return the string
	 */
	String screenshotFile();
	
	// other stuff

	/**
	 * Gets the test platform properties.
	 *
	 * @param value the value
	 * @return the test platform properties
	 */
	Properties getTestPlatformProperties(String value);

	/**
	 * Report step pass screenshot.
	 */
	void reportStepPassScreenshot();

	/**
	 * Report step fail screenshot.
	 */
	void reportStepFailScreenshot();

	/**
	 * Screenshot base 64.
	 *
	 * @return the string
	 */
	String screenshotBase64();

	/**
	 * Wait.
	 * <p>
	 * Waits the given milliseconds
	 *
	 * @param milliseconds the milliseconds
	 */
	void wait(int milliseconds);

	/**
	 * Gets the link locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the link locator
	 */
	String getLinkLocator(String locatorDelegate);

	/**
	 * Gets the slider locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the slider locator
	 */
	String getSliderLocator(String locatorDelegate);

	/**
	 * Gets the filefield locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the filefield locator
	 */
	String getFilefieldLocator(String locatorDelegate);

	/**
	 * Gets the numericfield locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the numericfield locator
	 */
	String getNumericfieldLocator(String locatorDelegate);

	/**
	 * Gets the checkbox locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the checkbox locator
	 */
	String getCheckboxLocator(String locatorDelegate);

	/**
	 * Gets the radiobutton locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the radiobutton locator
	 */
	String getRadiobuttonLocator(String locatorDelegate);

	/**
	 * Gets the radiogroup locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the radiogroup locator
	 */
	String getRadiogroupLocator(String locatorDelegate);

	/**
	 * Gets the listbox locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the listbox locator
	 */
	String getListboxLocator(String locatorDelegate);

	/**
	 * Gets the text locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the text locator
	 */
	String getTextLocator(String locatorDelegate);

	/**
	 * Gets the button locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the button locator
	 */
	String getButtonLocator(String locatorDelegate);

	/**
	 * Gets the edits the field locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the edits the field locator
	 */
	String getEditFieldLocator(String locatorDelegate);

	/**
	 * Wait until locator reaches state.
	 *
	 * @param locator the locator
	 * @param state   the state
	 * @param timeout the timeout
	 * @param report  the report
	 * @return the boolean
	 */
	boolean waitUntilBy(By locator, WebelementState state, long timeout, boolean report);

	/**
	 * Wait until locator reaches state.
	 * Uses default timeout
	 *
	 * @param locator the locator
	 * @param state   the state
	 * @param report  the report
	 * @return the boolean
	 */
	boolean waitUntilBy(By locator, WebelementState state, boolean report);

	/**
	 * Wait until locator reaches state.
	 * Uses default timeout
	 *
	 * @param locator the locator
	 * @param state   the state
	 * @return the boolean
	 */
	boolean waitUntilBy(By locator, WebelementState state);

	/**
	 * Wait until locator reaches state.
	 *
	 * @param locatorDelegate the locator delegate
	 * @param state           the state
	 * @param timeout         the timeout
	 * @param report          the report
	 * @return the boolean
	 */
	boolean waitUntil(String locatorDelegate, WebelementState state, long timeout, boolean report);

	/**
	 * Wait until locator reaches state.
	 * Uses default timeout
	 *
	 * @param locatorDelegate the locator delegate
	 * @param state           the state
	 * @param report          the report
	 * @return the boolean
	 */
	boolean waitUntil(String locatorDelegate, WebelementState state, boolean report);

	/**
	 * Wait until locator reaches state.
	 * Uses default timeout
	 *
	 * @param locatorDelegate the locator delegate
	 * @param state           the state
	 * @return the boolean
	 */
	boolean waitUntil(String locatorDelegate, WebelementState state);

	/**
	 * Wait until locator reaches state.
	 * Uses default timeout
	 *
	 * @param locator the locator
	 * @param state   the state
	 * @param timeout         the timeout
	 * @return the boolean
	 */
	boolean waitUntilBy(By locator, WebelementState state, long timeout);

	/**
	 * Wait until locator reaches state.
	 *
	 * @param locatorDelegate the locator delegate
	 * @param state           the state
	 * @param timeout         the timeout
	 * @return the boolean
	 */
	boolean waitUntil(String locatorDelegate, WebelementState state, long timeout);

}
