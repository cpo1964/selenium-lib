package com.github.cpo1964.platform.selenium;

import java.io.File;
import java.util.logging.Logger;

import org.openqa.selenium.NotFoundException;

import com.github.cpo1964.report.extent.ExtentHelper;
import com.github.cpo1964.utils.CommonHelper;

public class LocatorHelper {

	/**
	 * The logger.
	 */
	final static Logger log = Logger.getLogger(LocatorHelper.class.getSimpleName());

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

	/**
	 * Gets the locator.
	 *
	 * @param locatorDelegate the locator delegate
	 * @return the locator
	 */
	static String getLocator(String locatorDelegate) {
		if (isXpath(locatorDelegate)) {
			return locatorDelegate;
		}
		String[] locatorDelegateSplit = locatorDelegate.split(File.pathSeparator);
		if (locatorDelegateSplit.length != 3) {
			String errMsg = "locatorDelegate must match pattern 'classname" + File.pathSeparator + "locatortype"
					+ File.pathSeparator + "locatorDelegate': '" + locatorDelegate + "'";
			throw new NotFoundException(errMsg);
		}
		String cn = locatorDelegateSplit[0];
		String key = locatorDelegateSplit[2];
		Class<?> c = ExtentHelper.getClassByQualifiedName(cn);
		if (c == null) {
			throw new NotFoundException("class not found: " + cn);
		}
		// expected: a xpath from the property file
		String locator = CommonHelper.getClassPropertyValueByKey(c, key);
		log.finest("Found value '" + (!key.equals("password") ? locator : "*****") + "' by key '" + key
				+ "' from file '" + cn + ".properties'");
		return locator;
	}

	private static boolean isXpath(String locatorDelegate) {
		return locatorDelegate.startsWith("//") || locatorDelegate.startsWith("(//");
	}

}
