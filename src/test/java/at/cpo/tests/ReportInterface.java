package at.cpo.tests;

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
	 * Test log fail.
	 *
	 * @param msg the msg
	 */
	void reportTestLogFail(String msg);

	/**
	 * Test log pass.
	 *
	 * @param msg the msg
	 */
	void reportTestLogPass(String msg);

	/**
	 * Test create node.
	 step
	 * @param msg the msg
	 */
	void reportCreateStep(String msg);

	/**
	 * Screenshot step pass.
	 */
	void reportScreenshotStepPass();

	/**
	 * Screenshot step fail.
	 */
	void reportScreenshotStepFail();

	/**
	 * Tear down extent.
	 */
	void reportTearDown();

	// logging stuff

	/**
	 * Log info.
	 *
	 * @param msg the msg
	 */
	void logInfo(String msg);

	/**
	 * Log debug.
	 *
	 * @param msg the msg
	 */
	void logDebug(String msg);

	/**
	 * Log error.
	 *
	 * @param msg the msg
	 */
	void logError(String msg);

	/**
	 * Log all.
	 */
	void logAll();
}
