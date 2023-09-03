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
import java.net.UnknownHostException;
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
import com.github.cpo1964.utils.MaxlevelStreamHandler;

import tech.grasshopper.reporter.ExtentPDFReporter;

/**
 * The class tearDownExtent.
 */
public class ExtentHelper implements ReportInterface {

	/**
	 * The logger.
	 */
	static Logger logSelenium = Logger.getLogger(ExtentHelper.class.getSimpleName());

	/** The passed status. */
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

	/** The htmlTest. */
	private static ExtentTest htmlTest;

	/** The pdfTest. */
	private static ExtentTest pdfTest;

	/** The htmlNode. */
	private static ExtentTest htmlNode;

	/** The pdfNode. */
	private static ExtentTest pdfNode;

	/** The htmlReport. */
	private static ExtentReports htmlReport = ExtentHelper.prepareHtmlReport();

	/** The htmlReport. */
	private static ExtentReports pdfReport = ExtentHelper.preparePdfReport();

	/** The run results dir. */
	private static String runResultsDir = null;

	/**
	 * Wait count.
	 *
	 * @return the int
	 */
	public static int WaitCount() {
		return waitCount;
	}

	/**
	 * Sets the wait count.
	 *
	 * @param value the new wait count
	 */
	public static void setWaitCount(int value) {
		ExtentHelper.waitCount = value;
	}

	/**
	 * Gets the clicks count.
	 *
	 * @return the clicks count
	 */
	public static int getClicksCount() {
		return clicksCount;
	}

	/**
	 * Sets the clicks count.
	 *
	 * @param value the new clicks count
	 */
	public static void setClicksCount(int value) {
		ExtentHelper.clicksCount = value;
	}

	/**
	 * Gets the inputs count.
	 *
	 * @return the inputs count
	 */
	public static int getInputsCount() {
		return inputsCount;
	}

	/**
	 * Sets the inputs count.
	 *
	 * @param value the new inputs count
	 */
	public static void setInputsCount(int value) {
		ExtentHelper.inputsCount = value;
	}

	/**
	 * Gets the outputs count.
	 *
	 * @return the outputs count
	 */
	public static int getOutputsCount() {
		return outputsCount;
	}

	/**
	 * Sets the outputs count.
	 *
	 * @param value the new outputs count
	 */
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
	 * Gets the htmlTest.
	 *
	 * @return the test
	 */
	public static ExtentTest getHtmlTest() {
		return htmlTest;
	}

	/**
	 * Sets the htmlTest.
	 *
	 * @param test the new htmlTest
	 */
	public static void setHtmlTest(ExtentTest test) {
		htmlTest = test;
	}

	/**
	 * Gets the pdfTest.
	 *
	 * @return the test
	 */
	public static ExtentTest getPdfTest() {
		return pdfTest;
	}

	/**
	 * Sets the pdfTest.
	 *
	 * @param test the new pdfTest
	 */
	public static void setPdfTest(ExtentTest test) {
		pdfTest = test;
	}

	/**
	 * Gets the html node.
	 *
	 * @return the node
	 */
	public static ExtentTest getHtmlNode() {
		return htmlNode;
	}

	/**
	 * Sets the html node.
	 *
	 * @param node the new node
	 */
	public static void setHtmlNode(ExtentTest node) {
		htmlNode = node;
	}

	/**
	 * Gets the pdfNode node.
	 *
	 * @return the node
	 */
	public static ExtentTest getPdfNode() {
		return pdfNode;
	}

	/**
	 * Sets the pdfNode node.
	 *
	 * @param node the new node
	 */
	public static void setPdfNode(ExtentTest node) {
		pdfNode = node;
	}

	/**
	 * Gets the pdfReport.
	 *
	 * @return the report
	 */
	public static ExtentReports getPdfReport() {
		return pdfReport;
	}

	/**
	 * Sets the pdfReport.
	 *
	 * @param report the new report
	 */
	public static void setPdfReport(ExtentReports report) {
		pdfReport = report;
	}

	/**
	 * Gets the htmlReport.
	 *
	 * @return the report
	 */
	public static ExtentReports getHtmlReport() {
		return htmlReport;
	}

	/**
	 * Sets the htmlReport.
	 *
	 * @param report the new report
	 */
	public static void setHtmlReport(ExtentReports report) {
		htmlReport = report;
	}

	/**
	 * prepare the html report.
	 *
	 * @return the extent reports
	 */
	public static ExtentReports prepareHtmlReport() {
		runResultsDir = Paths.get("").toAbsolutePath().toString() + File.separatorChar + "RunResults";
		deleteDirectory(runResultsDir);
		createDirectories(runResultsDir + File.separatorChar + "Resources" + File.separatorChar + "Snapshots");

		setHtmlReport(new ExtentReports());

		// html report
		ExtentSparkReporter spark = new ExtentSparkReporter(runResultsDir + File.separatorChar + "runresults.html");
		spark.config().setEncoding("UTF-8");
		getHtmlReport().attachReporter(spark);

		try {
			getHtmlReport().setSystemInfo("Hostname", java.net.InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e) {
			logSelenium.finest(e.getMessage());
		}
		getHtmlReport().setSystemInfo("Operation System",
				System.getProperty("os.name") + " " + System.getProperty("os.version"));
		getHtmlReport().setSystemInfo("User", System.getProperty("user.name"));
		return getHtmlReport();
	}

	/**
	 * prepare the pdf report.
	 *
	 * @return the extent reports
	 */
	public static ExtentReports preparePdfReport() {
		setPdfReport(new ExtentReports());

		// pdf report
		ExtentPDFReporter pdfReport = new ExtentPDFReporter("RunResults/runresults.pdf");
		pdfReport.config().setMediaFolders(new String[] { "RunResults/Resources/Snapshots" });
		getPdfReport().attachReporter(pdfReport);

		try {
			getPdfReport().setSystemInfo("Hostname", java.net.InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e) {
			logSelenium.finest(e.getMessage());
		}
		getPdfReport().setSystemInfo("Operation System",
				System.getProperty("os.name") + " " + System.getProperty("os.version"));
		getPdfReport().setSystemInfo("User", System.getProperty("user.name"));
		return getPdfReport();
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
		MaxlevelStreamHandler.setupMaxLevelStreamHandler(logSelenium);
		if (isFailed()) {
			return;
		}
		setWaitCount(0);
		setClicksCount(0);
		setInputsCount(0);
		setOutputsCount(0);
		setHtmlTest(getHtmlReport().createTest("<b>" + replaceUmlaute(msg) + "</b>"));
		msg = msg.replaceAll("\\Wbr\\W", System.lineSeparator());
		msg = msg.replaceAll("\\Wb\\W", "");
		msg = msg.replaceAll("\\W/b\\W", "");
		setPdfTest(getPdfReport().createTest(replaceUmlaute(msg)));
		logExtent.info("##################");
		logExtent.info("## " + msg);
		logExtent.info("##################");
		try {
			logExtent.info("Hostname: " + java.net.InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e) {
			logSelenium.finest(e.getMessage());
		}
		logExtent.info("Operation System: " + System.getProperty("os.name") + " - " + System.getProperty("os.version"));
		logExtent.info("User: " + System.getProperty("user.name"));
		logExtent.info("##################");
	}

	/**
	 * Report end test.
	 *
	 * @param msg the msg
	 */
	@Override
	public void reportEndTest(String msg) {
		if (msg != null && !msg.isEmpty()) {
			String newmsg = msg.replace("<b>", "");
			newmsg = newmsg.replace("</b>", "");
			newmsg = newmsg.replace("<br>", "");
			if (isFailed()) {
				getHtmlTest().log(Status.FAIL, replaceUmlaute(msg));
				getPdfTest().log(Status.FAIL, replaceUmlaute(msg));
				logExtent.severe(newmsg);
			} else {
				getHtmlTest().log(Status.INFO, replaceUmlaute(msg));
				getPdfTest().log(Status.INFO, replaceUmlaute(msg));
				logExtent.info(newmsg);
			}
		}
		if (isFailed()) {
			getHtmlTest().log(Status.FAIL, "test failed");
			getPdfTest().log(Status.FAIL, "test failed");
			logExtent.severe("test failed");
		}
		String countMsg = "# Actions ####################<br>" + "waits: " + WaitCount() + "<br>" + "clicks: "
				+ getClicksCount() + "<br>" + "inputs: " + getInputsCount() + "<br>" + "outputs: " + getOutputsCount()
				+ "<br>";
		getHtmlTest().log(Status.INFO, countMsg);
		countMsg = countMsg.replaceAll("\\Wbr\\W", System.lineSeparator());
		countMsg = countMsg.replaceAll("\\Wb\\W", "");
		countMsg = countMsg.replaceAll("\\W/b\\W", "");
		getPdfTest().log(Status.INFO, countMsg);
		logExtent.info(countMsg);
		getHtmlReport().flush();
		getPdfReport().flush();
	}

	/**
	 * Test log fail.
	 *
	 * @param msg the msg
	 */
	public void reportTestFail(String msg) {
		htmlTest.log(Status.FAIL, msg);
		msg = msg.replaceAll("\\Wbr\\W", System.lineSeparator());
		msg = msg.replaceAll("\\Wb\\W", "");
		msg = msg.replaceAll("\\W/b\\W", "");
		pdfTest.log(Status.FAIL, msg);
		logExtent.severe(msg);
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
		htmlTest.log(Status.PASS, msg);
		msg = msg.replaceAll("\\Wbr\\W", System.lineSeparator());
		msg = msg.replaceAll("\\Wb\\W", "");
		msg = msg.replaceAll("\\W/b\\W", "");
		pdfTest.log(Status.PASS, msg);
		logExtent.info(msg);
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
		htmlTest.log(Status.INFO, msg);
		msg = msg.replaceAll("\\Wbr\\W", System.lineSeparator());
		msg = msg.replaceAll("\\Wb\\W", "");
		msg = msg.replaceAll("\\W/b\\W", "");
		pdfTest.log(Status.INFO, msg);
		logExtent.info(msg);
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
		setHtmlNode(htmlTest.createNode("<b>" + replaceUmlaute(msg) + "</b>"));
		msg = msg.replaceAll("\\Wbr\\W", System.lineSeparator());
		msg = msg.replaceAll("\\Wb\\W", "");
		msg = msg.replaceAll("\\W/b\\W", "");
		setPdfNode(pdfTest.createNode(replaceUmlaute(msg)));
		logExtent.info(msg);
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
		getHtmlNode().log(Status.INFO, replaceUmlaute(msg));
		msg = msg.replaceAll("\\Wbr\\W", System.lineSeparator());
		msg = msg.replaceAll("\\Wb\\W", "");
		msg = msg.replaceAll("\\W/b\\W", "");
		getPdfNode().log(Status.INFO, replaceUmlaute(msg));
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
		getHtmlNode().log(Status.PASS, replaceUmlaute(msg));
		msg = msg.replaceAll("\\Wbr\\W", System.lineSeparator());
		msg = msg.replaceAll("\\Wb\\W", "");
		msg = msg.replaceAll("\\W/b\\W", "");
		getPdfNode().log(Status.PASS, replaceUmlaute(msg));
		logExtent.info(msg);
	}

	/**
	 * Node log fail.
	 *
	 * @param msg the msg
	 */
	@Override
	public void reportStepFail(String msg) {
		getHtmlNode().log(Status.FAIL, replaceUmlaute(msg));
		msg = msg.replaceAll("\\Wbr\\W", System.lineSeparator());
		msg = msg.replaceAll("\\Wb\\W", "");
		msg = msg.replaceAll("\\W/b\\W", "");
		getPdfNode().log(Status.FAIL, replaceUmlaute(msg));
		logExtent.severe(msg);
		setFailed();
	}

	/**
	 * Screenshot node fail.
	 *
	 * @param screenShot the screen shot
	 */
	@Override
	public void reportStepFailScreenshot(String screenShot) {
		screenshotNode(screenShot, Status.FAIL);
		setFailed();
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
			Media media = getHtmlNode().addScreenCaptureFromPath(screenShot).getModel().getMedia().get(0);
			getHtmlNode().getModel().getMedia().clear();
			getHtmlNode().log(s, media);
		} catch (Exception e) {
			throw new CommonSeleniumException(e.getMessage());
		}
	}

	/** The umlaut replacements. */
	private static String[][] UMLAUT_REPLACEMENTS = { { new String("Ä"), "&Auml;" }, { new String("Ü"), "&Uuml;" },
			{ new String("Ö"), "&Ouml;" }, { new String("ä"), "&auml;" }, { new String("ü"), "&uuml;" },
			{ new String("ö"), "&ouml;" }, { new String("ß"), "&szlig;" } };

	/** The umlaut replacements2. */
	@SuppressWarnings("unused")
	private static String[][] UMLAUT_REPLACEMENTS2 = { { new String("Ä"), "Ae" }, { new String("Ü"), "Ue" },
			{ new String("Ö"), "Oe" }, { new String("ä"), "ae" }, { new String("ü"), "ue" }, { new String("ö"), "oe" },
			{ new String("ß"), "ss" } };

	/**
	 * Replace umlaute.
	 *
	 * @param orig the orig
	 * @return the string
	 */
	public static String replaceUmlaute(String orig) {
		String result = orig;

		for (int i = 0; i < UMLAUT_REPLACEMENTS.length; i++) {
			result = result.replace(UMLAUT_REPLACEMENTS[i][0], UMLAUT_REPLACEMENTS[i][1]);
		}

		return result;
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
