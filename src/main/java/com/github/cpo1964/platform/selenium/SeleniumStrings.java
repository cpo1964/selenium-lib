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

import java.io.File;

/**
 * The Class SeleniumStrings.
 */
public interface SeleniumStrings {

	// values for the verify action

	// platform stuff

	/** The Constant PRODUKTKEY. */
	public static final String PRODUKTKEY = "produkt";

	/** The Constant TEST_ENVIRONMENT. */
	public static final String TEST_ENVIRONMENT = "testEnvironment";

	/** The Constant TESTDATA_XLS. */
	public static final String TESTDATA_XLS = "Testdata.xls";

	/** The Constant TESTDATADIR. */
	public static final String TESTDATADIR = "src" + File.separator + "test" + File.separator + "data";

	/** The Constant FIREFOX. */
	String FIREFOX = "firefox";

	/** The Constant CHROME. */
	String CHROME = "chrome";
}
