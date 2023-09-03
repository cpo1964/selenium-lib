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

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * The Enum RestMethod.
 */
public enum RestMethod {

	/** The get. */
	GET(new HttpGet()),

	/** The put. */
	PUT(new HttpPut()),

	/** The post. */
	POST(new HttpPost()),

	/** The delete. */
	DELETE(new HttpPost());

	/** The method. */
	private HttpRequestBase method;

	/**
	 * Instantiates a new rest method.
	 *
	 * @param method the method
	 */
	RestMethod(HttpRequestBase method) {
		this.method = method;
	}

	/**
	 * Gets the method.
	 *
	 * @return the method
	 */
	public HttpRequestBase getMethod() {
		return method;
	}

	/**
	 * Sets the method.
	 *
	 * @param method the new method
	 */
	protected void setMethod(HttpRequestBase method) {
		this.method = method;
	}

}
