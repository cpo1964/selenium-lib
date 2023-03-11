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

/**
 * The Class MaxlevelStreamHandler.
 */
public class MaxlevelStreamHandler extends StreamHandler {

	/** The maxlevel. 
	 *
	 * by default, put out everything
	 */
	private Level maxlevel = Level.SEVERE;
	
	/** The format. */
	private static String format = "[%1$tF %1$tT] [%2$-13s] %3$s %n";

	/**
	 * The only method we really change to check whether the message is smaller than
	 * maxlevel. We also flush here to make sure that the message is shown
	 * immediately.
	 *
	 * @param record the record
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
	 * getter for maxlevel.
	 *
	 * @return the maxlevel
	 */
	public Level getMaxlevel() {
		return maxlevel;
	}

	/**
	 * Setter for maxlevel. If a logging event is larger than this level, it won't
	 * be displayed
	 *
	 * @param maxlevel the new maxlevel
	 */
	public void setMaxlevel(Level maxlevel) {
		this.maxlevel = maxlevel;
	}

	/**
	 *  Constructor forwarding.
	 *
	 * @param out the out
	 * @param formatter the formatter
	 */
	public MaxlevelStreamHandler(PrintStream out, Formatter formatter) {
		super(out, formatter);
	}

	/**
	 *  Constructor forwarding.
	 */
	public MaxlevelStreamHandler() {
		super();
	}

	/**
	 * Sets the up max level stream handler.
	 *
	 * @param logger the new up max level stream handler
	 */
	public static void setupMaxLevelStreamHandler(Logger logger) {
		if (logger.getHandlers().length > 0) {
			return;
		}
		Formatter formatter = new SimpleFormatter();
		// must set before the Logger
		// setup all logs that are smaller than WARNINGS to stdout
		MaxlevelStreamHandler outSh = new MaxlevelStreamHandler(System.out, formatter);
		outSh.setLevel(Level.ALL);
		outSh.setMaxlevel(Level.SEVERE);
		outSh.setFormatter(new SimpleFormatter() {
//			private static final String format = "[%1$tF %1$tT] [%2$-7s] %3$s %n";

			@Override
			public synchronized String format(LogRecord lr) {
				return String.format(getFormat() , new Date(lr.getMillis()), lr.getLevel().getLocalizedName(),
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

	/**
	 * Gets the format.
	 *
	 * @return the format
	 */
	public static String getFormat() {
		return format;
	}

	/**
	 * Sets the format.
	 *
	 * @param formatStr the new format
	 */
	public static void setFormat(String formatStr) {
		format = formatStr;
	}
}