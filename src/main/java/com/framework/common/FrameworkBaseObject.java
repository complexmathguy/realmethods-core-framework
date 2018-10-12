/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common;

// **********************
// Imports
// **********************
import com.framework.common.event.FrameworkLogEventType;

import com.framework.common.namespace.FrameworkNameSpace;

import com.framework.common.logging.FrameworkLoggingHelper;

//import org.apache.log4j.Logger;
import java.io.*;
import java.util.logging.Logger;


/**
 * Base class of many objects utilized and observed by the Framework.  
 * Provides minimal capabilities to access the default Log4J logger, but
 * mainly serves for type identity.
 * <p> 
 * @author    realMethods, Inc.
 */
public class FrameworkBaseObject 
    extends Object implements IFrameworkBaseObject
{
	// static block
	{
    	if ( loggingConfigFile == null ) // assign one
    	{
        	loggingConfigFile = System.getProperty("java.util.logging.config.file");

    		if ( loggingConfigFile == null ) // assign one
    		{
        		System.out.println( "System property java.util.logging.config.file not defined, so using logging.properties" );
        		loggingConfigFile = "logging.properties";
    		}

    		if ( FrameworkNameSpace.GOOGLE_LICENSE_ENV == false ) // with Google can't load the logging.properties file through the LogManager
    		{
	    		InputStream inputStream = inputStream = new DataInputStream( Thread.currentThread().getContextClassLoader().getResourceAsStream(loggingConfigFile) ); 
	    		
	    		try 
	    		{
	    			java.util.logging.LogManager.getLogManager().readConfiguration(inputStream);
	    		} 
	    		catch (Exception e) 
	    		{
	    			System.out.println("Unable to configure logging for " + loggingConfigFile + " because the file is not in the classpath");
	    			e.printStackTrace();
	    		}
    		}
		}
		
	}
	
//************************************************************************
// Public Methods
//************************************************************************
    /** 
     * default constructor
     */
    public FrameworkBaseObject()
    {
        super();
    }

	/** 
	 * copy constructor
	 */
	public FrameworkBaseObject( IFrameworkBaseObject obj )
	{
		super();
		
		// nothing to copy...
	}

    /** 
     * Logs the message to the default log4J logger.
     * <p>
     * <b>Note:</b></br>
     * This methods is around for backwards capatability.  Use logMessage( String, FramemworkLogEventType ) instead.
     * 
     * @param	msg		message to log
     */
    public void printMessage( String msg )
    {        
    	logMessage( msg );
    }
    

	/** 
	 * Logs the message to the default log4J logger, useful for non sub-class usage.
	 * <p>
	 * <b>Note:</b></br>
	 * This methods is around for backwards capatability.  Use logMessage( String, FramemworkLogEventType ) instead.
	 * 
	 * @param	msg		message to log
	 */
    public void printTheMessage( String msg )
    {
       logMessage( msg );
    }
    
    
    public void log( String msg )
    {
    	logMessage( msg );
    }
    
    /** 
     * Logs the message to Log4j as default type INFO
     * <p>
     * Delegates internally to overloaded version logMessage(String,FrameworkLogEventType)
     * <p>
     * @param	msg		message to log
     */
    public void logMessage( String msg )
    {
        logMessage( msg, FrameworkLogEventType.INFO_LOG_EVENT_TYPE );
    }
      
	/** 
	 * Logs the message to Log4j as type INFO
	 * <p>
	 * Delegates internally to overloaded version logMessage(String,FrameworkLogEventType)
	 * <p>
	 * @param	msg		message to log
	 */        
	public void logInfoMessage( String msg )
	{
		logMessage(  msg, FrameworkLogEventType.INFO_LOG_EVENT_TYPE );
	}
	   
	/** 
	 * Logs the message to Log4j as type WARN
	 * <p>
	 * Delegates internally to overloaded version logMessage(String,FrameworkLogEventType)
	 * <p>
	 * @param	msg		message to log
	 */	   
	public void logWarnMessage( String msg )
	{
		logMessage(  msg, FrameworkLogEventType.WARNING_LOG_EVENT_TYPE );
	}
	
	/** 
	 * Logs the message to Log4j as type ERROR
	 * <p>
	 * Delegates internally to overloaded version logMessage(String,FrameworkLogEventType)
	 * <p>
	 * @param	msg		message to log
	 */	
	public void logErrorMessage( String msg )
	{
		logMessage(  msg, FrameworkLogEventType.ERROR_LOG_EVENT_TYPE );
	}
	
	/** 
	 * Logs the message to Log4j as type DEBUG
	 * <p>
	 * Delegates internally to overloaded version logMessage(String,FrameworkLogEventType)
	 * <p>
	 * @param	msg		message to log
	 */	
	public void logDebugMessage( String msg )
	{
		logMessage(  msg, FrameworkLogEventType.DEBUG_LOG_EVENT_TYPE );
	}
	
	/**
	 * Logs the message to the default Log4J Logger, either as debug, info, warn, or  error.
	 * <p>
	 * @param msg			message to log
	 * @param logEventType	message category
	 */        
	public void logMessage( String msg, FrameworkLogEventType logEventType )
	{
		logger = FrameworkLoggingHelper.getLogger( getClass().getName() );  
        
		if  ( logger != null )          
        {
        	if ( logEventType.isInfoLogEventType() )
    	        logger. info( msg );
			else if ( logEventType.isDebugLogEventType() ) 	
				logger.info( msg );
			else if ( logEventType.isErrorLogEventType() ) 
				logger.severe( msg );
			else if ( logEventType.isWarningLogEventType() ) 
				logger.warning( msg );
			else
				System.out.println( msg );					    	        
        }
		else
    		System.out.println( msg );
	}
	
    /**
     * Performs a deep copy, but has nothing to do, and should be overloaded.
     * 
     * @param		object		entity to copy from 
     * @exception 	IllegalArgumentException 
     */
    public void copy( IFrameworkBaseObject object ) 
    throws IllegalArgumentException
    {
    	// nothing to do
    }

//************************************************************************
// Protected / Private Methods
//************************************************************************


//************************************************************************
// Attributes
//************************************************************************

	/**
	 * Singleton self pointer
	 */
//    final private static IFrameworkBaseObject self 	= new FrameworkBaseObject();
    final protected static long serialVersionUID = -1;
    private static String loggingConfigFile;
    private transient Logger logger = null;
}

/*
 * Change Log:
 * $Log: FrameworkBaseObject.java,v $
 */
