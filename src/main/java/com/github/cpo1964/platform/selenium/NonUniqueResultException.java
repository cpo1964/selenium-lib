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
public class NonUniqueResultException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1757423191400510323L;

	/**
	 * Instantiates a new Non unique result exception.
	 */
	public NonUniqueResultException() {
		super("Only one result is allowed for fetchOne calls");
	}

	/**
	 * Instantiates a new Non unique result exception.
	 *
	 * @param message the message
	 */
	public NonUniqueResultException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new Non unique result exception.
	 *
	 * @param e the e
	 */
	public NonUniqueResultException(Exception e) {
		super(e);
	}
}
