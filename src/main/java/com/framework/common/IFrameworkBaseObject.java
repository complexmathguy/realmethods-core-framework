/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common;

//************************************
// Imports
//************************************

/** 
 * Base interface for many of the objects used within the Framework. 
 * <p>
 * Its main purpose is for identity only.
 * <p> 
 * @author    realMethods, Inc.
 */
public interface IFrameworkBaseObject extends java.io.Serializable
{
	/** 
	 * Prints the message using the logging.properties settings.
	 * Around for backwards capatability.  Use logMessage( String, FramemworkLogEventType ) on FrameworkBaseObject instead.
	 * <p>
	 * @param   msg		text to log
	 * @deprecated
	 */
    public void printMessage( String msg );
    
}

/*
 * Change Log:
 * $Log: IFrameworkBaseObject.java,v $
 */



