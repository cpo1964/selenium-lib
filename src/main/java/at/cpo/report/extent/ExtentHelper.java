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
package at.cpo.report.extent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.Media;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import at.cpo.report.ReportInterface;

/**
 * The class tearDownExtent.
 */
public class ExtentHelper implements ReportInterface {
	/** The driver. */
	protected static RemoteWebDriver driver;

	/** The log buffer. */
	protected static ArrayList<String> logBuffer = new ArrayList<String>();
	
	/** The test. */
	protected static ExtentTest test;
	
	/** The node. */
	protected static ExtentTest node;

	/** The report. */
	protected static ExtentReports report;

	{
		report = ExtentHelper.prepareExtentReport();
	}

	/**
	 * Prepare extent report.
	 *
	 * @return the extent reports
	 */
	public static ExtentReports prepareExtentReport() {
		String runResultsDir = Paths.get("").toAbsolutePath().toString() + "\\RunResults";
		new File(runResultsDir).delete();
		ExtentSparkReporter r = new ExtentSparkReporter(runResultsDir + "\\runresults.html");
		ExtentReports report = new ExtentReports();
		report.attachReporter(r);
		return report;
	}
	
	/**
	 * Tear down extent.
	 */
	public void reportTearDown() {
		report.flush();
	}

	/**
	 * Report create test.
	 *
	 * @param msg the msg
	 */
	public void reportCreateTest(String msg) {
		test = report.createTest(msg);
	}

	/**
	 * Test log fail.
	 *
	 * @param msg the msg
	 */
	public void reportTestLogFail(String msg) {
		test.log(Status.FAIL, msg);
	}

	/**
	 * Test log pass.
	 *
	 * @param msg the msg
	 */
	public void reportTestLogPass(String msg) {
		test.log(Status.PASS, msg);
	}

	/**
	 * Test create node.
	 *
	 * @param msg the msg
	 */
	public void reportCreateStep(String msg) {
		node = test.createNode(msg);
	}

	/**
	 * Node log fail.
	 *
	 * @param msg the msg
	 */
	public void reportStepLogFail(String msg) {
		logBuffer.add("ERROR#" + msg);
		node.log(Status.FAIL, msg);
	}

	/**
	 * Node log pass.
	 *
	 * @param msg the msg
	 */
	public void reportStepLogPass(String msg) {
		logBuffer.add("INFO#" + msg);
		node.log(Status.PASS, msg);
	}

	/**
	 * Screenshot node fail.
	 */
	public void reportScreenshotStepFail() {
		screenshotNode(driver, node, Status.FAIL);
	}
	
	/**
	 * Screenshot node pass.
	 */
	public void reportScreenshotStepPass() {
		screenshotNode(driver, node, Status.PASS);
	}

	/**
	 * Screenshot file to "RunResults" + File.separator
				+ "Resources" + File.separator + "Snapshots"
	 *
	 * @param driver the driver
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String screenshotFile(TakesScreenshot driver) throws IOException {
		long time = new Date().getTime();
		File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE); // clears old console output
		String snapshotsDir = Paths.get("").toAbsolutePath().toString() + File.separator + "RunResults" + File.separator
				+ "Resources" + File.separator + "Snapshots";
		Files.createDirectories(Paths.get(snapshotsDir));
		String screenShotPath = snapshotsDir + File.separator + time + ".png";
		File destination = new File(screenShotPath);
		Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
		return "Resources" + File.separator + "Snapshots" + File.separator + time + ".png";
	}

	/**
	 * Screenshot node.
	 *
	 * @param driver the driver
	 * @param node the node
	 * @param s the s
	 */
	public void screenshotNode(TakesScreenshot driver, ExtentTest node, Status s) {
		try {

			// ExtentReport 4
//			node.addScreenCaptureFromPath(screenshotFile(driver));
//			node.log(s, "");

			// ExtentReport 5
			Media media = node.addScreenCaptureFromPath(screenshotFile(driver)).getModel().getMedia().get(0);
			node.getModel().getMedia().clear();
			node.log(s, media);
			
//			node.log(Status.PASS, node.addScreenCaptureFromPath(base64conversion()).getModel().getMedia().get(0));
//			node.log(Status.PASS, "start MTours", node.addScreenCaptureFromPath(base64conversion()).getModel().getMedia().get(0));
//			node.log(Status.PASS, "start MTours", node.addScreenCaptureFromBase64String(base64conversion()).getModel().getMedia().get(0));
//			node.addScreenCaptureFromPath(base64conversion());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Screenshot base 64.
	 *
	 * @param driver the driver
	 * @return the string
	 * @throws Exception the exception
	 */
	public static String screenshotBase64(TakesScreenshot driver) throws Exception {
		TakesScreenshot newScreen = (TakesScreenshot) driver;
		String scnShot = newScreen.getScreenshotAs(OutputType.BASE64);
		return "data:image/jpg;base64, " + scnShot;

	}

	/**
	 * Log info.
	 *
	 * @param msg the msg
	 */
	public void logInfo(String msg) {
		logBuffer.add("INFO#" + msg);
	}

	/**
	 * Log debug.
	 *
	 * @param msg the msg
	 */
	public void logDebug(String msg) {
		logBuffer.add("DEBUG#" + msg);
	}

	/**
	 * Log error.
	 *
	 * @param msg the msg
	 */
	public void logError(String msg) {
		logBuffer.add("ERROR#" + msg);
	}

	/**
	 * Clear console.
	 */
	public final static void clearConsole() {
		for (int i = 0; i < 50; ++i) System.out.println("");

//		for (int i = 0; i < 50; ++i) System.out.println("\\r\\b");
//		
//		System.out.print("\033[H\033[2J");
//        System.out.flush();
//        
//        try {
//			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
//		} catch (InterruptedException | IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		System.out.print("\033\143");
//
//		String ESC = "\033[";
//		System.out.print(ESC + "2J");
//		
//		try {
//			final String os = System.getProperty("os.name");
//
//			if (os.contains("Windows")) {
//				Runtime.getRuntime().exec("cls");
//			} else {
//				Runtime.getRuntime().exec("clear");
//			}
//		} catch (final Exception e) {
//			// Handle any exceptions.
//		}
	}

	/**
	 * Log all.
	 */
	public void logAll() {
//		LogFactory.getFactory().release();
//		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");

//		org.apache.log4j.Logger LOGGER = null;
		Logger LOGGER = null;
			LOGGER = LogManager.getLogger(this.getClass().getSimpleName());
//			try {
// 				LOGGER = LogManager.getLogger(Class.forName("at.cpo.report.extent.ExtentHelper"));
//			} catch (ClassNotFoundException e) {
//				return;		
//			}

//		clearConsole();

		for (String el : logBuffer) {
			if (el.startsWith("INFO#")) {
				LOGGER.info(el.substring(5));
			} else if (el.startsWith("DEBUG#")) {
				LOGGER.debug(el.substring(5));
			} else if (el.startsWith("ERROR#")) {
				LOGGER.error(el.substring(6));
			}
		}

	}

}