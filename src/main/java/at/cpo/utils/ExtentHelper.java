package at.cpo.utils;

import java.io.File;
import java.nio.file.Paths;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

/**
 * The class tearDownExtent.
 */
public class ExtentHelper {

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
}
