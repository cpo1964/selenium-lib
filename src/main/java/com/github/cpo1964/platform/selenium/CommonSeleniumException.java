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

/**
 * The type Non unique result exception.
 */
public class CommonSeleniumException extends RuntimeException {
	private static final long serialVersionUID = -1757423191400510324L;

	/**
	 * Instantiates a new Non unique result exception.
	 */
	public CommonSeleniumException() {
		super("common selenium exception");
	}

	/**
	 * Instantiates a new Non unique result exception.
	 *
	 * @param message the message
	 */
	public CommonSeleniumException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new Non unique result exception.
	 *
	 * @param e the e
	 */
	public CommonSeleniumException(Exception e) {
		super(e);
	}
}
