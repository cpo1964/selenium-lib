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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

	/** The logger. */
	Logger log = LogManager.getLogger(this.getClass().getSimpleName());

	/** The driver. */
	protected static RemoteWebDriver driver;

	/** The test. */
	protected static ExtentTest test;

	/** The node. */
	protected static ExtentTest node;

	/** The report. */
	protected static ExtentReports report = ExtentHelper.prepareExtentReport();
	
	/**
	 * Prepare extent report.
	 *
	 * @return the extent reports
	 */
	public static ExtentReports prepareExtentReport() {
		String runResultsDir = Paths.get("").toAbsolutePath().toString() + File.separatorChar + "RunResults";
		deleteDirectory(runResultsDir);
		createDirectories(runResultsDir+ File.separatorChar + "Resources"+ File.separatorChar + "Snapshots");
		ExtentSparkReporter r = new ExtentSparkReporter(runResultsDir + File.separatorChar + "runresults.html");
		report = new ExtentReports();
		report.attachReporter(r);
		return report;
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
		try (Stream<Path> stream = Files.walk(index);) {
			if (!Files.exists(index)) {
				Files.createDirectories(index);
			} else {
				// as the file tree is traversed depth-first and 
				// that deleted dirs have to be empty
				stream.sorted(Comparator.reverseOrder()).forEach(t -> {
					try {
						Files.delete(t);
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
				if (!Files.exists(index)) {
					Files.createDirectories(index);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Report create test.
	 *
	 * @param msg the msg
	 */
	public void reportCreateTest(String msg) {
		createTest(msg);
		log.info("##################");
		log.info(() -> "## " + msg);
		log.info("##################");
	}

	private static void createTest(String msg) {
		test = report.createTest("<b>" + msg + "</b>");
	}

	/**
	 * Test log fail.
	 *
	 * @param msg the msg
	 */
	public void reportTestFail(String msg) {
		test.log(Status.FAIL, msg);
		log.error(() -> msg.replace("<br>", System.lineSeparator()));
	}

	/**
	 * Test log pass.
	 *
	 * @param msg the msg
	 */
	public void reportTestPass(String msg) {
		test.log(Status.PASS, msg);
		log.info(() -> msg.replace("<br>", System.lineSeparator()));
	}

	/**
	 * Test log pass.
	 *
	 * @param msg the msg
	 */
	public void reportTestInfo(String msg) {
		test.log(Status.INFO, msg);
		log.info(() -> msg.replace("<br>", System.lineSeparator()));
	}

	/**
	 * Test create node.
	 *
	 * @param msg the msg
	 */
	public void reportCreateStep(String msg) {
		createStep(msg);
		log.info(() -> "### " + msg.replace("<br>", System.lineSeparator()));
	}

	private static void createStep(String msg) {
		node = test.createNode("<b>" + msg + "</b>");
	}

	/**
	 * Node log fail.
	 *
	 * @param msg the msg
	 */
	public void reportStepFail(String msg) {
		node.log(Status.FAIL, msg);
		final String msgRP = msg.replace("<b>", "");
		log.error(() -> msgRP.replace("</b>", ""));
	}

	/**
	 * Node log pass.
	 *
	 * @param msg the msg
	 */
	public void reportStepPass(String msg) {
		node.log(Status.PASS, msg);
		final String msgRP = msg.replace("<b>", "");
		log.info(() -> msgRP.replace("</b>", ""));
	}

	/**
	 * Screenshot node fail.
	 *
	 * @param screenShot the screen shot
	 */
	public void reportStepFailScreenshot(String screenShot) {
		screenshotNode(screenShot, node, Status.FAIL);
	}

	/**
	 * Screenshot node pass.
	 *
	 * @param screenShot the screen shot
	 */
	public void reportStepPassScreenshot(String screenShot) {
		screenshotNode(screenShot, node, Status.PASS);
	}

	/**
	 * Tear down extent.
	 */
	public void reportTearDown() {
		report.flush();
	}

	/**
	 * Screenshot node.
	 *
	 * @param screenShot the screen shot
	 * @param node   the node
	 * @param s      the s
	 */
	public void screenshotNode(String screenShot, ExtentTest node, Status s) {
		// ExtentReport 5
		Media media = node.addScreenCaptureFromPath(screenShot).getModel().getMedia().get(0);
		node.getModel().getMedia().clear();
		node.log(s, media);
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
