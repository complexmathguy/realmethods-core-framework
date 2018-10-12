package com.framework.common.logging;

import java.util.*;
import java.util.logging.*;

/**
 * class LogFileFormatter
 * 
 * Provides less information when logging with the java.util.logging API
 * @author realMethods
 *
 */

public class LoggingFormatter extends java.util.logging.SimpleFormatter 
{

	public LoggingFormatter() 
	{
		super();
	}

	public String format(LogRecord record) 
	{	
		
		// Create a StringBuffer to contain the formatted record
		// start with the date.
		StringBuffer sb = new StringBuffer();
		
		// Get the date from the LogRecord and add it to the buffer
		Date date = new Date(record.getMillis());
		sb.append(date.toString());
		sb.append(" ");
		
		// Get the level name and add it to the buffer
		sb.append(record.getLevel().getName());
		sb.append(" ");
		 
		// Get the formatted message (includes localization 
		// and substitution of paramters) and add it to the buffer
		sb.append(formatMessage(record));
		sb.append("\n");

		return sb.toString();
	}
}
