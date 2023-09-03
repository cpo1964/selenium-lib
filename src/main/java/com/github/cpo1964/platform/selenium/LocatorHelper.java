package com.github.cpo1964.platform.selenium;

import java.io.File;

public class LocatorHelper {

	/**
	 * Gets the edits the field locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the edits the field locator
	 */
	
	public static String getEditFieldLocator(Class<?> page, String locatorDelegate) {
		return page.getName() + File.pathSeparator + WebelementType.EDITFIELD.name() + File.pathSeparator
				+ locatorDelegate;
	}

	/**
	 * Gets the button locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the button locator
	 */
	
	public static String getButtonLocator(Class<?> page, String locatorDelegate) {
		return page.getName() + File.pathSeparator + WebelementType.BUTTON.name() + File.pathSeparator
				+ locatorDelegate;
	}

	/**
	 * Gets the text locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the text locator
	 */
	
	public static String getTextLocator(Class<?> page, String locatorDelegate) {
		return page.getName() + File.pathSeparator + WebelementType.TEXT.name() + File.pathSeparator
				+ locatorDelegate;
	}

	/**
	 * Gets the listbox locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the listbox locator
	 */
	
	public static String getListboxLocator(Class<?> page, String locatorDelegate) {
		return page.getName() + File.pathSeparator + WebelementType.LISTBOX.name() + File.pathSeparator
				+ locatorDelegate;
	}

	/**
	 * Gets the radiogroup locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the radiogroup locator
	 */
	
	public static String getRadiogroupLocator(Class<?> page, String locatorDelegate) {
		return page.getName() + File.pathSeparator + WebelementType.RADIOGROUP.name() + File.pathSeparator
				+ locatorDelegate;
	}

	/**
	 * Gets the radiobutton locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the radiobutton locator
	 */
	
	public static String getRadiobuttonLocator(Class<?> page, String locatorDelegate) {
		return page.getName() + File.pathSeparator + WebelementType.RADIOBUTTON.name() + File.pathSeparator
				+ locatorDelegate;
	}

	/**
	 * Gets the checkbox locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the checkbox locator
	 */
	
	public static String getCheckboxLocator(Class<?> page, String locatorDelegate) {
		return page.getName() + File.pathSeparator + WebelementType.CHECKBOX.name() + File.pathSeparator
				+ locatorDelegate;
	}

	/**
	 * Gets the numericfield locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the numericfield locator
	 */
	
	public static String getNumericfieldLocator(Class<?> page, String locatorDelegate) {
		return page.getName() + File.pathSeparator + WebelementType.NUMERICFIELD.name() + File.pathSeparator
				+ locatorDelegate;
	}

	/**
	 * Gets the filefield locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the filefield locator
	 */
	
	public static String getFilefieldLocator(Class<?> page, String locatorDelegate) {
		return page.getName() + File.pathSeparator + WebelementType.FILEFIELD.name() + File.pathSeparator
				+ locatorDelegate;
	}

	/**
	 * Gets the slider locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the slider locator
	 */
	
	public static String getSliderLocator(Class<?> page, String locatorDelegate) {
		return page.getName() + File.pathSeparator + WebelementType.SLIDER.name() + File.pathSeparator
				+ locatorDelegate;
	}

	/**
	 * Gets the link locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the link locator
	 */
	public static String getLinkLocator(Class<?> page, String locatorDelegate) {
		return page.getName() + File.pathSeparator + WebelementType.LINK.name() + File.pathSeparator
				+ locatorDelegate;	}

}
