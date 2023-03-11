package com.github.cpo1964.utils;

/**
 * The only difference to the standard StreamHandler is 
 * that a MAXLEVEL can be defined (which then is not published)
 * 
 * @author Kai Goergen
 */

import java.io.PrintStream;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

public class MaxlevelStreamHandler extends StreamHandler {

	private Level maxlevel = Level.SEVERE; // by default, put out everything

	/**
	 * The only method we really change to check whether the message is smaller than
	 * maxlevel. We also flush here to make sure that the message is shown
	 * immediately.
	 */
	@Override
	public synchronized void publish(LogRecord record) {
		if (record.getLevel().intValue() > this.maxlevel.intValue()) {
			// do nothing if the level is above maxlevel
		} else {
			// if we arrived here, do what we always do
			super.publish(record);
			super.flush();
		}
	}

	/**
	 * getter for maxlevel
	 * 
	 * @return
	 */
	public Level getMaxlevel() {
		return maxlevel;
	}

	/**
	 * Setter for maxlevel. If a logging event is larger than this level, it won't
	 * be displayed
	 * 
	 * @param maxlevel
	 */
	public void setMaxlevel(Level maxlevel) {
		this.maxlevel = maxlevel;
	}

	/** Constructor forwarding */
	public MaxlevelStreamHandler(PrintStream out, Formatter formatter) {
		super(out, formatter);
	}

	/** Constructor forwarding */
	public MaxlevelStreamHandler() {
		super();
	}

	public static void setupMaxLevelStreamHandler(Logger logger) {
		if (logger.getHandlers().length > 0) {
			return;
		}
		Formatter formatter = new SimpleFormatter();
		// must set before the Logger
		System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT %4$s %2$s %5$s%6$s%n");
		// setup all logs that are smaller than WARNINGS to stdout
		MaxlevelStreamHandler outSh = new MaxlevelStreamHandler(System.out, formatter);
		outSh.setLevel(Level.ALL);
		outSh.setMaxlevel(Level.INFO);
		outSh.setFormatter(new SimpleFormatter() {
			private static final String format = "[%1$tF %1$tT] [%2$-7s] %3$s %n";

			@Override
			public synchronized String format(LogRecord lr) {
				return String.format(format, new Date(lr.getMillis()), lr.getLevel().getLocalizedName(),
						lr.getMessage());
			}
		});
		logger.addHandler(outSh);

		// setup all warnings to stdout & warnings and higher to stderr
		StreamHandler errSh = new StreamHandler(System.err, formatter);
		errSh.setLevel(Level.WARNING);

		logger.addHandler(errSh);

		// remove default console logger
		logger.setUseParentHandlers(false);
	}
}