/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/

package com.framework.common.logging;

 

import com.framework.common.properties.PropertyFileLoader;

import java.util.logging.*;

/**
 * Helper class that handles the loading of framework related logging.properties configuration file.
 *
 * @author    realMethods, Inc.
 */
public class FrameworkLoggingHelper
{
	/**
	 * Returns the associated logging implemetation
	 * @param 		loggerName	unique name mapped to a logging definition in the logging config file
	 * @return		the associated Logger
	 */
	static public java.util.logging.Logger getLogger( String loggerName )
	{
		java.util.logging.Logger logger = java.util.logging.Logger.getLogger( loggerName );

		return logger;
	}
	 
// attributes

	
}



/*
 * Change Log:
 * $Log:  $
 */
