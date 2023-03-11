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
package com.github.cpo1964.report.extent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.logging.Logger;
import java.util.stream.Stream;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.Media;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.github.cpo1964.platform.selenium.CommonSeleniumException;
import com.github.cpo1964.report.ReportInterface;

/**
 * The class tearDownExtent.
 */
public class ExtentHelper implements ReportInterface {

	/** The passed status */
	private static boolean passed = true;

	/** The exists count. */
	private static int waitCount;

	/** The clicks count. */
	private static int clicksCount;

	/** The inputs count. */
	private static int inputsCount;

	/** The ouputs count. */
	private static int outputsCount;

	/** The logger. */
	private static final Logger logExtent = Logger.getLogger(ExtentHelper.class.getSimpleName());

	/** The test. */
	private static ExtentTest test;

	/** The getNode(). */
	private static ExtentTest node;

	/** The getReport(). */
	private static ExtentReports report = ExtentHelper.prepareExtentReport();

	public static int WaitCount() {
		return waitCount;
	}

	public static void setWaitCount(int value) {
		ExtentHelper.waitCount = value;
	}

	public static int getClicksCount() {
		return clicksCount;
	}

	public static void setClicksCount(int value) {
		ExtentHelper.clicksCount = value;
	}

	public static int getInputsCount() {
		return inputsCount;
	}

	public static void setInputsCount(int value) {
		ExtentHelper.inputsCount = value;
	}

	public static int getOutputsCount() {
		return outputsCount;
	}

	public static void setOutputsCount(int value) {
		ExtentHelper.outputsCount = value;
	}

	/**
	 * Checks if is failed.
	 *
	 * @return true, if is failed
	 */
	public static boolean isFailed() {
		return passed == false;
	}

	/**
	 * Checks if is passed.
	 *
	 * @return true, if is passed
	 */
	public static boolean isPassed() {
		return passed == true;
	}

	/**
	 * Sets the passed.
	 */
	public static void setPassed() {
		passed = true;
	}

	/**
	 * Sets the failed.
	 */
	public static void setFailed() {
		passed = false;
	}

	/**
	 * Gets the test.
	 *
	 * @return the test
	 */
	public static ExtentTest getTest() {
		return test;
	}

	/**
	 * Sets the test.
	 *
	 * @param test the new test
	 */
	public static void setTest(ExtentTest test) {
		ExtentHelper.test = test;
	}

	/**
	 * Gets the node.
	 *
	 * @return the node
	 */
	public static ExtentTest getNode() {
		return node;
	}

	/**
	 * Sets the node.
	 *
	 * @param node the new node
	 */
	public static void setNode(ExtentTest node) {
		ExtentHelper.node = node;
	}

	/**
	 * Gets the report.
	 *
	 * @return the report
	 */
	public static ExtentReports getReport() {
		return report;
	}

	/**
	 * Sets the report.
	 *
	 * @param report the new report
	 */
	public static void setReport(ExtentReports report) {
		ExtentHelper.report = report;
	}

	/**
	 * Prepare extent getReport().
	 *
	 * @return the extent reports
	 */
	public static ExtentReports prepareExtentReport() {
		String runResultsDir = Paths.get("").toAbsolutePath().toString() + File.separatorChar + "RunResults";
		deleteDirectory(runResultsDir);
		createDirectories(runResultsDir + File.separatorChar + "Resources" + File.separatorChar + "Snapshots");
		ExtentSparkReporter r = new ExtentSparkReporter(runResultsDir + File.separatorChar + "runresults.html");
		setReport(new ExtentReports());
		getReport().attachReporter(r);
		return getReport();
	}

	/**
	 * Creates the directories.
	 *
	 * @param runResultsDir the run results dir
	 */
	private static void createDirectories(String runResultsDir) {
		try {
			Files.createDirectories(Paths.get(runResultsDir));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Delete directory.
	 *
	 * @param path the path
	 */
	private static void deleteDirectory(String path) {
		Path index = Paths.get(path);
		if (!Files.exists(index)) {
			try {
				Files.createDirectories(index);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try (Stream<Path> stream = Files.walk(index);) {
			// as the file tree is traversed depth-first and
			// that deleted dirs have to be empty
			stream.sorted(Comparator.reverseOrder()).forEach(t -> {
				try {
					Files.delete(t);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Report create test.
	 *
	 * @param msg the msg
	 */
	@Override
	public void reportCreateTest(String msg) {
		if (isFailed()) {
			return;
		}
		setWaitCount(0);
		setClicksCount(0);
		setInputsCount(0);
		setOutputsCount(0);
		setTest(getReport().createTest("<b>" + msg + "</b>"));
		logExtent.info("##################");
		logExtent.info(() -> "## " + msg);
		logExtent.info("##################");
	}

	/**
	 * Report end test.
	 *
	 * @param msg the msg
	 */
	@Override
	public void reportEndTest(String msg) {
		if (isFailed()) {
			getTest().log(Status.FAIL, "test failed");
			logExtent.info("test failed");
		}
		if (msg != null && !msg.isEmpty()) {
			getTest().log(Status.INFO, msg);
			msg = msg.replace("<b>", "");
			msg = msg.replace("</b>", "");
			msg = msg.replace("<br>", "");
			logExtent.info(msg);
		}
		String countMsg = "# Actions ####################<br>" + "waits: " + WaitCount() + "<br>" + "clicks: "
				+ getClicksCount() + "<br>" + "inputs: " + getInputsCount() + "<br>" + "outputs: " + getOutputsCount()
				+ "<br>";
		getTest().log(Status.INFO, countMsg);
		countMsg = countMsg.replace("<br>", System.lineSeparator());
		logExtent.info(countMsg);
		getReport().flush();
	}

	/**
	 * Test log fail.
	 *
	 * @param msg the msg
	 */
	public void reportTestFail(String msg) {
		test.log(Status.FAIL, msg);
		logExtent.severe(() -> msg.replace("<br>", System.lineSeparator()));
	}

	/**
	 * Test log pass.
	 *
	 * @param msg the msg
	 */
	public void reportTestPass(String msg) {
		if (isFailed()) {
			return;
		}
		test.log(Status.PASS, msg);
		logExtent.info(() -> msg.replace("<br>", System.lineSeparator()));
	}

	/**
	 * Test log pass.
	 *
	 * @param msg the msg
	 */
	@Override
	public void reportTestInfo(String msg) {
		if (isFailed()) {
			return;
		}
		test.log(Status.INFO, msg);
		logExtent.info(() -> msg.replace("<br>", System.lineSeparator()));
	}

	/**
	 * Test create getNode().
	 *
	 * @param msg the msg
	 */
	@Override
	public void reportCreateStep(String msg) {
		if (isFailed()) {
			return;
		}
		setNode(test.createNode("<b>" + msg + "</b>"));
		logExtent.info(() -> "### " + msg.replace("<br>", System.lineSeparator()));
	}

	/**
	 * Report end step.
	 *
	 * @param msg the msg
	 */
	@Override
	public void reportEndStep(String msg) {
		if (isFailed()) {
			return;
		}
		reportStepInfo(msg);
	}

	/**
	 * Report step info.
	 *
	 * @param msg the msg
	 */
	@Override
	public void reportStepInfo(String msg) {
		if (isFailed()) {
			return;
		}
		getNode().log(Status.INFO, msg);
		msg = msg.replace("<b>", "");
		msg = msg.replace("</b>", "");
		logExtent.info(msg);
	}

	/**
	 * Node log pass.
	 *
	 * @param msg the msg
	 */
	@Override
	public void reportStepPass(String msg) {
		if (isFailed()) {
			return;
		}
		getNode().log(Status.PASS, msg);
		msg = msg.replace("<b>", "");
		msg = msg.replace("</b>", "");
		logExtent.info(msg);
	}

	/**
	 * Node log fail.
	 *
	 * @param msg the msg
	 */
	@Override
	public void reportStepFail(String msg) {
		setFailed();
		getNode().log(Status.FAIL, msg);
		msg = msg.replace("<b>", "");
		msg = msg.replace("</b>", "");
		logExtent.severe(msg);
	}

	/**
	 * Screenshot node fail.
	 *
	 * @param screenShot the screen shot
	 */
	@Override
	public void reportStepFailScreenshot(String screenShot) {
		screenshotNode(screenShot, Status.FAIL);
	}

	/**
	 * Screenshot node pass.
	 *
	 * @param screenShot the screen shot
	 */
	@Override
	public void reportStepPassScreenshot(String screenShot) {
		if (isFailed()) {
			return;
		}
		screenshotNode(screenShot, Status.PASS);
	}

	/**
	 * Screenshot getNode().
	 *
	 * @param screenShot the screen shot
	 * @param s          the s
	 */
	public void screenshotNode(String screenShot, Status s) {
		// ExtentReport 5
		try {
			Media media = getNode().addScreenCaptureFromPath(screenShot).getModel().getMedia().get(0);
			getNode().getModel().getMedia().clear();
			getNode().log(s, media);
		} catch (Exception e) {
            throw new CommonSeleniumException(e.getMessage());
		}
	}

	/**
	 * Gets the class by qualified name.
	 *
	 * @param cn the cn
	 * @return the class by qualified name
	 */
	public static Class<?> getClassByQualifiedName(String cn) {
		Class<?> c = null;
		try {
			c = Class.forName(cn);
		} catch (ClassNotFoundException e) {
			return null;
		}
		return c;
	}

}
