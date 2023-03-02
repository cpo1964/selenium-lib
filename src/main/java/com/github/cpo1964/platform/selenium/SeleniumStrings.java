package com.github.cpo1964.platform.selenium;

import java.io.File;

public class SeleniumStrings {

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
	
	/** The Constant PRODUKTKEY. */
	public static final String PRODUKTKEY = "produkt";

	/** The Constant TEST_ENVIRONMENT. */
	public static final String TEST_ENVIRONMENT = "testEnvironment";

	/** The Constant MANDANT. */
	public static final String MANDANTKEY = "mandant";

	/** The Constant ZONEKEY. */
	public static final String ZONEKEY = "zone";
	
	/** The Constant TESTDATA_XLS. */
	public static final String TESTDATA_XLS = "Testdata.xls";
	
	/** The Constant TESTDATADIR. */
	public static final String TESTDATADIR = "src" + File.separator + "test" + File.separator + "data";
	
	/** The ok. */
	public static boolean OKKEY = false;

	/** The value. */
	public static String VALUEKEY = "";
}
