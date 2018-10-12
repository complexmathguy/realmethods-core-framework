/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.log;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.event.FrameworkLogEventType;
import com.framework.common.exception.FrameworkLogException;
import com.framework.common.misc.Utility;
import com.framework.common.namespace.FrameworkNameSpace;
import com.framework.integration.esb.*;

/**
 * Helper class used to provide easy access to the default logging mechanism
 *
 * <p>
 * @author    realMethods, Inc.
 * @see		  com.framework.integration.log.FrameworkLogHandlerFactory
 * @see		  com.framework.integration.log.FrameworkLogHandler
 */
public class FrameworkDefaultLogger extends FrameworkBaseObject
{
	{
        try
        {        	
        	esbManager = ESBManagerFactory.getInstance().getESBManager( FrameworkNameSpace.DefaultESBManager );
        }
        catch( Throwable exc )
        {
        	System.out.println( "FrameworkDefaultLogger.static.block - failed to created Default ESB Manager - ESB not in use.");
        }
	}
	
	/**
     * default constructor : deter access
     */
    protected FrameworkDefaultLogger()
    {}
    
    /**
     * Default handler for logging debug related messages.  Delegates to sendDefaultLogMessage.
     * @param	msg		debug message to log
     */
    static public void debug( String msg )
    {        
    	try
    	{
	        sendDefaultLogMessage( FrameworkLogEventType.DEBUG_LOG_EVENT_TYPE, msg );
    	}
    	catch( FrameworkLogException exc )
    	{
    		new FrameworkBaseObject().logDebugMessage( "FrameworkDefaultLogger:debug(String) - Unable to log message - " + exc );
    	}
	        
    }

    /**
     * Default handler for logging information related messages. Delegates to sendDefaultLogMessage.
     * @param	msg		info message to log
     */
    static public void info( String msg )
    {        
    	try
    	{
	        sendDefaultLogMessage( FrameworkLogEventType.INFO_LOG_EVENT_TYPE, msg );
    	}
    	catch( FrameworkLogException exc )
    	{
    		new FrameworkBaseObject().logDebugMessage( "FrameworkDefaultLogger:info(String) - Unable to log message - " + exc );
    	}	        
    }

    /**
     * Default handler for logging warning related messages.  Delegates to sendDefaultLogMessage.
     * @param	msg		warning message to log
     */
    static public void warning( String msg )
    {        
    	try
    	{
	        sendDefaultLogMessage( FrameworkLogEventType.WARNING_LOG_EVENT_TYPE, msg );
    	}
    	catch( FrameworkLogException exc )
    	{
    		new FrameworkBaseObject().logMessage( "FrameworkDefaultLogger:warning(String) - Unable to log message - " + exc );
    	}
	        
    }
    
    /**
     * Default handler for logging error related messages.  Delegates to sendDefaultLogMessage.
     * @param	msg		error message to log
     */
    static public void error( String msg )
    {        
    	try
    	{
        	sendDefaultLogMessage( FrameworkLogEventType.ERROR_LOG_EVENT_TYPE, msg );
    	}
    	catch( FrameworkLogException exc )
    	{
    		new FrameworkBaseObject().logDebugMessage( "FrameworkDefaultLogger:error(String) - Unable to log message - " + exc );
    	}
    }    
    
    /**
     * Convenience method for sending a message of certain log event type to the
     * default log handler.
     * <p>
     * @param     	logEventType			type of log event
     * @param       msg						text of message to log
     * @exception	FrameworkLogException
     */
    static public void sendDefaultLogMessage( FrameworkLogEventType logEventType, String msg )
    throws FrameworkLogException
    {
        // still want to cycle through the factory for this...
        try
        {        	
           // IFrameworkLogHandler logHandler = getDefaultLogHandler();                    
            // logHandler.log( logEventType, msg );
            /* ESB in use */
        	java.util.Properties props = null;
        	Object [] args = {logEventType, msg};
        	
        	if ( esbManager != null )
        		esbManager.dispatch( FrameworkNameSpace.framework_log_service, null, args, props);
        	else // must have failed, but still need to log
        		logViaLogListener( logEventType, msg  );
        	
        }
        catch( Throwable exc )
        {
        	// esb startup or connection may have failed, so go this route
    		logViaLogListener( logEventType, msg  );

            //throw new FrameworkLogException( "FrameworkDefaultLogger:sendDefaultLogMessage() - " + exc, exc );
        }
    }

    protected static void logViaLogListener( FrameworkLogEventType logEventType, String msg )
    {
    	try
    	{
    		logListener.logMessage( logEventType, msg );
    	}
    	catch( Throwable exc )
    	{
    		System.out.println( logEventType.toString() + ":" + msg );
    	}
    }
    /**
     * returns the default IFrameworkLogHandler
     * <p>
     * @return      IFrameworkLogHandler
     * @exception   FrameworkLogException
     */
    static public IFrameworkLogHandler getDefaultLogHandler()
    throws FrameworkLogException
    {
        return( logListener.getDefaultLogHandler() );            
    }
    
// attributes

	/**
	 * the default log handler
	 */
	private static FrameworkLogListener logListener = new FrameworkLogListener();    
    private static IESBManager esbManager = null;
}

/*
 * Change Log:
 * $Log: FrameworkDefaultLogger.java,v $
 */
