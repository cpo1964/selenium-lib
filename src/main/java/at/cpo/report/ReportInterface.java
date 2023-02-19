/*
 * MIT License
 *
 * Copyright (c) 2018 Elias Nogueira
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
package at.cpo.report;


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
	 * Test create node.
	 step
	 * @param msg the msg
	 */
	void reportCreateStep(String msg);

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
	 */
	void reportStepPassScreenshot();

	/**
	 * Screenshot step fail.
	 */
	void reportStepFailScreenshot();

	/**
	 * Tear down extent.
	 */
	void reportTearDown();

	// logging stuff

//	/**
//	 * Log info.
//	 *
//	 * @param msg the msg
//	 */
//	void logInfo(String msg);
//
//	/**
//	 * Log debug.
//	 *
//	 * @param msg the msg
//	 */
//	void logDebug(String msg);
//
//	/**
//	 * Log error.
//	 *
//	 * @param msg the msg
//	 */
//	void logError(String msg);
//
//	/**
//	 * Log all.
//	 */
//	void logAll();
}
