/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.test.junit.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.event.FrameworkLogEventType;

import com.framework.common.exception.FrameworkCheckedException;

import com.framework.integration.log.FrameworkDefaultLogger;
import com.framework.integration.log.FrameworkLogHandler;
import com.framework.integration.log.FrameworkLogHandlerFactory;
import com.framework.integration.log.FrameworkLogMessage;
import com.framework.integration.log.IFrameworkLogHandler;

import com.framework.test.junit.FrameworkTestCase;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * This class tests the key functionality of all components that comprise the Framework's logging service.
 * Testing includes :
 * <ul>
 * <li>Verifying the ability of the FrameworkLogHandlerFactory to create the necessary FrameworkLogHandlers
 * from the entries loaded from the task.properties file.
 * <li>Create a FrameworkLogMessage and for each FrameworkLogHandler created by the FrameworkLogHandlerFactory,
 * and verify the FrameworkLogMessage instance' attributes.
 * <li>Create a single instance of an IFrameworkLogHandler and bind it with a set of known properties, then
 * verify the properties.
 * <li>Verify the attributes for each FrameworkLogHandler created by the FrameworkLogHandlerFactory.
 * <li>Acquire the Framework default log handler, and attempt to send a log message for each of the different
 * log event types (debug, info, warn, and error).
 * </ul>
 * 
 * <p>
 * @author    realMethods, Inc.
 */
public class LogServiceTest extends FrameworkTestCase 
{

// constructors
	
    /**
     * construtor
     *
     * @param    testCaseName		unique name applied to this TestCase
     */    
    public LogServiceTest( String testCaseName )
    {
        super( testCaseName );

    }
    
    /**
	 * main test driver
     */
    public static void main(String[] args) 
    {
        TestSuite suite = new TestSuite();
        suite.addTest( new LogServiceTest( "fullTest" ) );

        junit.textui.TestRunner.run( suite );
    }

// TestCase overloads

    /**
     * Overload from JUnit TestCase, providing the method name to invoke for testing.  
     * The other methods can be invoked on their own, but fullTest covers all the bases.
     *
     * @return    junit.framework.Test
     */
    public static Test suite() 
    { 
        TestSuite suite= new TestSuite(); 
        suite.addTest(new LogServiceTest("fullTest"));
        return suite; 
    }
    
// test methods

    /** 
     * full test of the component
     */
    public void fullTest()
    throws Throwable
    {
		new FrameworkBaseObject().logMessage( "*****\nLogServiceTest:fullTest() - starting...\n*****" );
		    	
		testFrameworkLogHandlerFactory();
		testFrameworkLogMessage();
		testFrameworkLogHandler();
		testFrameworkLogHandlers();
		testFrameworkDefaultLogger();
		
		new FrameworkBaseObject().logMessage( "*****\nLogServiceTest:fullTest() - passed...\n*****" );			
    }    
    
    /**
     * Tests FrameworkLogHandlerFactory
     * <p>
     * @throws Throwable
     */
    public void testFrameworkLogHandlerFactory()
    throws Throwable 
    {
    	try
    	{
    		assertNull( FrameworkLogHandlerFactory.getInstance().getLogHandler( "empty") );    		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "LogServiceTest.testFrameworkLogHandlerFactory() failed - " + exc.toString() );
            throw exc;    		
    	}    
    	
    	new FrameworkBaseObject().logMessage( "LogServiceTest.testFrameworkLogHandlerFactory() - passed..." );      		
    }
    
    
    /**
     * Tests FrameworkLogMessage
     * <p>
     * @throws Throwable
     */
    public void testFrameworkLogMessage()
    throws Throwable 
    {
    	Map logHandlers = null;
    	
    	try
    	{
	    	logHandlers = FrameworkLogHandlerFactory.getInstance().getLogHandlers();	
	    	
	    	assertNotNull( logHandlers );
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "LogServiceTest.testFrameworkLogMessage() failed due to no FrameworkLogHandlers loaded by the FrameworkLogHandlerFactory.  Check log.properties and its location. - " + exc.toString() );
            throw exc;    		
    	}    

		// iterator through each log handler and create a log message
		
		Iterator keys 					= logHandlers.keySet().iterator();
		IFrameworkLogHandler logHandler = null;
		FrameworkLogMessage logMessage	= null;
		String msg						= "test message";
		FrameworkLogEventType logEventType	= FrameworkLogEventType.DEBUG_LOG_EVENT_TYPE;
				
		while( keys.hasNext() )
		{
			logHandler = (IFrameworkLogHandler)logHandlers.get( keys.next() );
			
			// create the log message
			try
			{
				logMessage = new FrameworkLogMessage( logHandler, logEventType, msg );
			}
			catch( Throwable exc )
			{
				new FrameworkBaseObject().logMessage( "LogServiceTest.testFrameworkLogMessage() failed during construction of a FrameworkLogMessage - " + exc.toString() );
	            throw exc; 
			}
			
			// review it's attributes
			try
			{
				assertEquals( logMessage.getFrameworkLogHandler(), logHandler );
			}
			catch( Throwable exc )
			{
				new FrameworkBaseObject().logMessage( "LogServiceTest.testFrameworkLogMessage() failed during comparison of the FrameworkLogHandler - " + exc.toString() );
	            throw exc; 
			}
			
			try
			{
				assertEquals( logMessage.getFrameworkLogEventType(), logEventType );
			}
			catch( Throwable exc )
			{
				new FrameworkBaseObject().logMessage( "LogServiceTest.testFrameworkLogMessage() failed during comparison of the FrameworkLogEventType - " + exc.toString() );
	            throw exc; 
			}		
							
			// verify it's ability to create a formatted message
			try
			{
				assertNotNull( logMessage.getLogFormattedMessage() );
			}
			catch( Throwable exc )
			{
				new FrameworkBaseObject().logMessage( "LogServiceTest.testFrameworkLogMessage() - call to getLogFormattedMessage() failed  - " + exc.toString() );
	            throw exc; 
			}			
		}
		    	
    	new FrameworkBaseObject().logMessage( "LogServiceTest.testFrameworkLogMessage() - passed..." );      		
    }
            
    /**
     * Tests FrameworkLogHandlers loaded from the log.properties and created by
     * the FrameworkLogHandlerFactory
     * <p>
     * @throws Throwable
     */
    public void testFrameworkLogHandlers()
    throws Throwable 
    {
    	Map logHandlers = null;
    	
    	try
    	{
	    	logHandlers = FrameworkLogHandlerFactory.getInstance().getLogHandlers();	
	    	
	    	assertNotNull( logHandlers );
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "LogServiceTest.testFrameworkLogHandlers() failed due to no FrameworkLogHandlers loaded by the FrameworkLogHandlerFactory.  Check log.properties and its location. - " + exc.toString() );
            throw exc;    		
    	}
    	
    	// iterate through each to verify their attributes and issue a log message 
		Iterator keys 					= logHandlers.keySet().iterator();
		IFrameworkLogHandler logHandler = null;
		String varName					= null;
				
		while( keys.hasNext() )
		{
			logHandler = (IFrameworkLogHandler)logHandlers.get( keys.next() );    	
    		
    		// verify attributes
	    	try
	    	{
	    		varName = "logName";
			    assertNotNull( logHandler.getLogName() );

			  	varName = "debugDestinations";
			    assertNotNull( logHandler.getDebugDestinations() );
			  
			  	varName = "infoDestinations";
			    assertNotNull( logHandler.getInfoDestinations() );
			  
			  	varName = "warningDestinations";
			    assertNotNull( logHandler.getWarningDestinations() );

			  	varName = "errorDestinations";			   
			    assertNotNull( logHandler.getErrorDestinations() );
			 
			  	varName = "debugDestinations";			 
			    assertNotNull( logHandler.getDestinations( FrameworkLogEventType.DEBUG_LOG_EVENT_TYPE ) );  
			    
			  	varName = "infoDestinations";			    
			    assertNotNull( logHandler.getDestinations( FrameworkLogEventType.INFO_LOG_EVENT_TYPE ) );  
			    
			  	varName = "warningDestinations";			    
			    assertNotNull( logHandler.getDestinations( FrameworkLogEventType.WARNING_LOG_EVENT_TYPE ) );  
			    
			  	varName = "errorDestinations";			    
			    assertNotNull( logHandler.getDestinations( FrameworkLogEventType.ERROR_LOG_EVENT_TYPE ) );  
			
			  	varName = "dateTimeStampFormatted";			
			    assertNotNull( logHandler.getDateTimeStampFormatted() ); 	    		  		
	    	}
	       	catch( Throwable exc )
	       	{
				new FrameworkBaseObject().logMessage( "LogServiceTest.testFrameworkLogHandlers() failed - Invalid state for member variable " + varName + " - " + exc.toString() );
	            throw exc;    		
	    	}    
	    	
	    	// verify it's ability to log
	    	try
	    	{
		    	internalFrameworkLogHandlerTestHelper( logHandler );
	    	}
	       	catch( Throwable exc )
	       	{
				new FrameworkBaseObject().logMessage( "LogServiceTest.testFrameworkLogHandlers() failed - " + exc.toString() );
	            throw exc;    		
	    	} 	    	
		}
		    	
    	new FrameworkBaseObject().logMessage( "LogServiceTest.testFrameworkLogHandlers() - passed..." );      		
    }
    
   /**
     * Tests a single FrameworkLogHandler by creating a single instance and providing
     * it a set of known properties.
     * <p>
     * @throws Throwable
     */
    public void testFrameworkLogHandler()
    throws Throwable 
    {
    	Map logProperties 	= new HashMap();
    	String logName		= "testLogger";
    	String varName		= null;
    	
    	logProperties.put( "synchronous", "true" );
    	logProperties.put( "debug", "testDebugCategory" );
    	logProperties.put( "info", "testInfoCategory" );
    	logProperties.put( "warn", "testWarningCategory" );
    	logProperties.put( "error", "testErrorCategory" );
    	logProperties.put( "dateTimeStampFormat", "yyyy.mm.dd-hh:mm:ss" );
    	    	
		IFrameworkLogHandler logHandler = new FrameworkLogHandler( logName, logProperties );
    		
		// verify attributes
    	try
    	{
    		
    		varName = "isSynchronous";
		    assertTrue( logHandler.isSynchronous() );

    		varName = "logName";
		    assertEquals( logHandler.getLogName(), logName );

		  	varName = "debugDestinations";
		    assertEquals( (String)logHandler.getDebugDestinations().toArray()[0], "testDebugCategory" );
		  
		  	varName = "infoDestinations";
		    assertEquals( (String)logHandler.getInfoDestinations().toArray()[0], "testInfoCategory" );
		  
		  	varName = "warningDestinations";
		    assertEquals( (String)logHandler.getWarningDestinations().toArray()[0], "testWarningCategory" );

		  	varName = "errorDestinations";			   
		    assertEquals( (String)logHandler.getErrorDestinations().toArray()[0], "testErrorCategory" );
		 
		  	varName = "debugDestinations";			 
		    assertEquals( logHandler.getDestinations( FrameworkLogEventType.DEBUG_LOG_EVENT_TYPE ).toArray()[0], "testDebugCategory" );  
		    
		  	varName = "infoDestinations";			    
		    assertEquals( logHandler.getDestinations( FrameworkLogEventType.INFO_LOG_EVENT_TYPE ).toArray()[0], "testInfoCategory" ); 
		    
		  	varName = "warningDestinations";			    
		    assertEquals( logHandler.getDestinations( FrameworkLogEventType.WARNING_LOG_EVENT_TYPE ).toArray()[0], "testWarningCategory" );;  
		    
		  	varName = "errorDestinations";			    
		    assertEquals( logHandler.getDestinations( FrameworkLogEventType.ERROR_LOG_EVENT_TYPE ).toArray()[0], "testErrorCategory" );;  
		
		  	varName = "dateTimeStampFormatted";			
		    assertEquals( logHandler.getProperties().get(IFrameworkLogHandler.DATE_TIME_STAMP_FORMAT).toString(), "yyyy.mm.dd-hh:mm:ss" ); 	    		  		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "LogServiceTest.testFrameworkLogHandler() failed - failed in value comparison for variable FrameworkLogHandler." + varName + " - " + exc.toString() );
            throw exc;    		
    	}    
    	
    	// verify it's ability to log
    	try
    	{
	    	internalFrameworkLogHandlerTestHelper( logHandler );
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "LogServiceTest.testFrameworkLogHandler() failed - " + exc.toString() );
            throw exc;    		
    	} 	    	
		    	
    	new FrameworkBaseObject().logMessage( "LogServiceTest.testFrameworkLogHandler() - passed..." );      		
    }    
                
    /**
     * Tests FrameworkDefaultLogger access as well as logging
     * <p>
     * @throws Throwable
     */
    public void testFrameworkDefaultLogger()
    throws Throwable 
    {
    	IFrameworkLogHandler logHandler = null;
    	
    	// access default log handler
    	try
    	{
    		logHandler = FrameworkDefaultLogger.getDefaultLogHandler();
    		
    		assertNotNull( logHandler );    		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "LogServiceTest.testFrameworkDefaultLogger() failed - " + exc.toString() );
            throw exc;    		
    	}    
    	
    	// exercise it by logging for the 4 different types

		String msg = "test log message";
		
		// debug
    	try
    	{
    		FrameworkDefaultLogger.sendDefaultLogMessage( FrameworkLogEventType.DEBUG_LOG_EVENT_TYPE, "Debug " + msg );	    		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "LogServiceTest.testFrameworkDefaultLogger() failed to send a debug log msg - " + exc.toString() );
            throw exc;    		
    	} 
    	
		// info
    	try
    	{
    		FrameworkDefaultLogger.sendDefaultLogMessage( FrameworkLogEventType.INFO_LOG_EVENT_TYPE, "Info " + msg );	    		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "LogServiceTest.testFrameworkDefaultLogger() failed to send an info log msg - " + exc.toString() );
            throw exc;    		
    	}
    	
		// warn
    	try
    	{
    		FrameworkDefaultLogger.sendDefaultLogMessage( FrameworkLogEventType.WARNING_LOG_EVENT_TYPE, "Warning " + msg );	    		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "LogServiceTest.testFrameworkDefaultLogger() failed to send a warning log msg - " + exc.toString() );
            throw exc;    		
    	}
    	
		// error
    	try
    	{
    		FrameworkDefaultLogger.sendDefaultLogMessage( FrameworkLogEventType.ERROR_LOG_EVENT_TYPE, "Error " + msg );	    		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "LogServiceTest.testFrameworkDefaultLogger() failed to send an error  log msg - " + exc.toString() );
            throw exc;    		
    	}    	    	    	    	
    	
    	new FrameworkBaseObject().logMessage( "LogServiceTest.testFrameworkDefaultLogger() - Debug,Info,Warn, and Error logging passed..." );      		
    }    
    
    /**
     * Internal helper method used to apply each of the supported FrameworkLogEventTypes to the 
     * provided IFrameworkLogHandler
     * <p>
     * @param logHandler		test target
     * @throws FrameworkCheckedException
     */
    protected void internalFrameworkLogHandlerTestHelper( IFrameworkLogHandler logHandler )
    throws FrameworkCheckedException
    {
		String msg 					= "test message";
		FrameworkLogEventType type  =  null;
		
		// apply logging to each event type
		try
		{
			type = FrameworkLogEventType.DEBUG_LOG_EVENT_TYPE;
    		logHandler.log( type, msg );
    		
			type = FrameworkLogEventType.INFO_LOG_EVENT_TYPE;
    		logHandler.log( type, msg );

			type = FrameworkLogEventType.WARNING_LOG_EVENT_TYPE;
    		logHandler.log( type, msg );

			type = FrameworkLogEventType.ERROR_LOG_EVENT_TYPE;
    		logHandler.log( type, msg );
		}
		catch( Throwable exc )
		{ 
			throw new FrameworkCheckedException( "LogServiceTest - failed to log a " + type.toString() + " type message - " + exc, exc ); 
		}    	
    }
        
}
    
/*
 * Change Log:
 * $Log: LogServiceTest.java,v $
 */    
