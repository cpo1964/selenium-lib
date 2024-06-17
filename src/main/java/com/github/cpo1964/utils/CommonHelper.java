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
package com.github.cpo1964.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Logger;

import com.github.cpo1964.platform.selenium.SeleniumStrings;

/**
 * The Class CommonHelper.
 */
public class CommonHelper {

	public static Properties getProperties(String filePath) {
		Properties props = new Properties();
		try (Reader inStream = new InputStreamReader(new FileInputStream(filePath))) {
			props.load(inStream);
		} catch (IOException e) {
			Logger.getLogger(CommonHelper.class.getSimpleName()).finest(e.getMessage());
		}
	return props;
}

	/**
	 * Gets the class property value by key.
	 *
	 * @param propHolder the prop holder
	 * @param key        the key
	 * @return the class property value by key
	 */
	public static String getClassPropertyValueByKey(final Class<?> propHolder, String key) {
		String propertiesFileDestination = propHolder.getSimpleName() + ".properties";
		InputStream stream = propHolder.getResourceAsStream(propertiesFileDestination);
		if (stream == null) {
			URL url = propHolder.getClassLoader().getResource(propertiesFileDestination);
			if (url != null) {
				try (InputStream streamOpen = url.openStream()) {
					stream = streamOpen;
				} catch (IOException e1) {
					throw new UnsupportedOperationException("Can not find property file: " + propertiesFileDestination);
				}
			} else {
				throw new UnsupportedOperationException("Can not find property file: " + propertiesFileDestination);
			}
		}
		Properties prop = new Properties();
		try {
			prop.load(stream);
		} catch (IOException e) {
			throw new UnsupportedOperationException("Can not open property file: " + propertiesFileDestination);
		}

		String value = prop.getProperty(key);
		if (value == null || value.isEmpty()) {
			throw new IllegalArgumentException(
					"Property '" + key + "' from file " + propertiesFileDestination + " does not exists or is empty!");
		}
		return value;
	}

	/**
	 * Checks if is true.
	 *
	 * @param value the value
	 * @return true, if is true
	 */
	public static boolean isTrue(String value) {
		if (value == null || value.isEmpty()) {
			value = "false";
		}
		String[] values = { "true", "ok", "on" };
		return Arrays.asList(values).contains(value.toLowerCase());
	}

	/**
	 * Gets the secret string.
	 *
	 * @param value  the value
	 * @param secret the value
	 * @return the secret string
	 */
	public static String getSecretString(String value, boolean secret) {
		if (!secret) {
			return value;
		}
		StringBuilder bld = new StringBuilder();
		for (int i = 1; i < value.length(); i++) {
			bld.append("*");
		}
		return bld.toString();
	}

	/**
	 * Wait.
	 * <p>
	 * Waits the given milliseconds
	 *
	 * @param milliseconds the milliseconds
	 */
	public static void wait(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Gets the test data path.
	 *
	 * @return the test data path
	 */
	public static String getTestDataPathByMandantZone() {
		String dirMandantZone = "";
		String mandant = System.getProperty("mandant");
		String zone = System.getProperty("zone");
		System.out.println("mandant: " + mandant);
		System.out.println("zone: " + zone);
		if (mandant != null && !mandant.isEmpty()) {
			dirMandantZone = File.separator + mandant;
			if (zone != null && !zone.isEmpty()) {
				dirMandantZone = File.separator + mandant + "-" + zone;
			}
		}
		dirMandantZone = Paths.get("").toAbsolutePath().toString() + File.separator +
				SeleniumStrings.TESTDATADIR + dirMandantZone;
		return dirMandantZone;
	}

}
