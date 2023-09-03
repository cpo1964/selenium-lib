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
package com.github.cpo1964.report;

/**
 * The Interface EnvironmentInterface.
 */
public interface ReportInterface {

	/**
	 * Report create test.
	 *
	 * @param msg the msg
	 */
	void reportCreateTest(String msg);

	/**
	 * Report end test.
	 *
	 * @param msg the msg
	 */
	void reportEndTest(String msg);

	/**
	 * Test log info.
	 *
	 * @param msg the msg
	 */
	void reportTestInfo(String msg);

	/**
	 * Test log pass.
	 *
	 * @param msg the msg
	 */
	void reportTestPass(String msg);

	/**
	 * Test log fail.
	 *
	 * @param msg the msg
	 */
	void reportTestFail(String msg);

	/**
	 * Test create node. step
	 * 
	 * @param msg the msg
	 */
	void reportCreateStep(String msg);

	/**
	 * Report end step.
	 *
	 * @param msg the msg
	 */
	void reportEndStep(String msg);

	/**
	 * Report step info.
	 *
	 * @param msg the msg
	 */
	void reportStepInfo(String msg);

	/**
	 * Step log pass.
	 *
	 * @param msg the msg
	 */
	void reportStepPass(String msg);

	/**
	 * Step log fail.
	 *
	 * @param msg the msg
	 */
	void reportStepFail(String msg);

	/**
	 * Screenshot step pass.
	 *
	 * @param screenShot the screen shot
	 */
	void reportStepPassScreenshot(String screenShot);

	/**
	 * Screenshot step fail.
	 *
	 * @param screenShot the screen shot
	 */
	void reportStepFailScreenshot(String screenShot);

}
