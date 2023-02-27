/*
 * MIT License
 *
 * Copyright (c) 2023 Christian Pöcksteinera
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
package at.cpo.selenium.common.pageobjects;

import at.cpo.platform.selenium.SeleniumHelper;

/**
 * The Class SeleniumLoginPage.
 */
public class MToursLoginPage extends SeleniumHelper {

	/** The page. */
	protected static String page = "MToursLoginPage.";

	/**
	 * The constant USERNAME.
	 */
	public static final String USERNAME = page + EDITFIELD + ".UsernameIN";

	/**
	 * The constant PASSWORD.
	 */
	public static final String PASSWORD = page + EDITFIELD + ".PasswordIN";

	/**
	 * The constant Login.
	 */
	public static final String LOGIN = page + BUTTON + ".LoginBT";
	
	/**
	 * The constant LoginOk.
	 */
	public static final String LOGINOK = page + TEXT + ".LoginOkTXT";

	/**
	 * The constant Login.
	 */
	public static final String NOTICE = page + BUTTON + ".NoticeBT";

	/**
	 * The constant HOME.
	 */
	public static final String HOME = page + LINK + ".HomeLN";

	/**
	 * The constant HOME.
	 */
	public static final String FLIGHTS = page + LINK + ".FlightsLN";

	/**
	 * The constant SIGNININFO.
	 */
	public static final String SIGNININFO = page + TEXT + ".SigInInfoTXT";

}
