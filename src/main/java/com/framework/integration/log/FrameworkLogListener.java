/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.log;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.event.FrameworkLogEventType;

import com.framework.common.exception.FrameworkLogException;

import com.framework.common.logging.FrameworkLoggingHelper;
import com.framework.common.misc.Utility;

import com.framework.common.namespace.FrameworkNameSpace;
import java.util.logging.Logger;

/**
 * Helper class used to provide easy access to the default logging mechanism
 *
 * <p>
 * @author    realMethods, Inc.

 */
public class FrameworkLogListener extends FrameworkBaseObject
{
    /**
     * default constructor
     */
    public FrameworkLogListener()
    {}
    
    
    /**
     * Convenience method for sending a message of certain log event type to the
     * default log handler.
     * <p>
     * @param     	logEventType			type of log event
     * @param       msg						text of message to log
     * @exception	FrameworkLogException
     */
    public void logMessage( FrameworkLogEventType logEventType, String msg )
    throws FrameworkLogException
    {
    	logMessage( msg, logEventType );
    }
    
    /**
     * returns the default IFrameworkLogHandler
     * <p>
     * @return      IFrameworkLogHandler
     * @exception   FrameworkLogException
     */
    IFrameworkLogHandler getDefaultLogHandler()
    throws FrameworkLogException
    {
		if ( defaultLogHandler == null )        
        {        
        	try
    	    {
				defaultLogHandler = FrameworkLogHandlerFactory.getInstance().getLogHandler( Utility.getFrameworkProperties().getProperty( FrameworkNameSpace.DEFAULT_LOG_HANDLER_NAME ) );            
        	}
        	catch( Throwable exc )
        	{
        		throw new FrameworkLogException( "FrameworkDefaultLogger:getDefaultLogHandler() - " + exc, exc );
        	}
        }
        
        return( defaultLogHandler );            
    }
    
// attributes

	/**
	 * the default log handler
	 */
	private static IFrameworkLogHandler defaultLogHandler = null;    
    
}

/*
 * Change Log:
 * $Log: FrameworkDefaultLogger.java,v $
 */
