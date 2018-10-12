/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.log;

import java.util.Collection;
import java.util.Map;

import javax.management.*;

import com.framework.common.event.FrameworkLogEventType;

import com.framework.common.exception.FrameworkLogException;

import com.framework.common.jmx.FrameworkDynamicMBean;

/**
 * FrameworwkLogHandler DynamicMBean, in order
 * to accommodate the JMX specification towards object management.
 *
 * <p>
 * @author    realMethods, Inc.
 */
abstract public class FrameworkLogHandlerMBean extends FrameworkDynamicMBean
    implements IFrameworkLogHandler

{
// constructor

    /**
     * default constructor - public and empty per JMX specification for a DynamicMBean
     */
    public FrameworkLogHandlerMBean()
    {
        super( false /* do not JMX MBean Register yet...*/ );
    }

// must implements from FrameworkDynamicMBean

    /**
     * Allows the value of the specified attribute of the Dynamic MBean to be obtained.
     *
     * @param       attributeName	previously assigned name to a particular member variable
     * @return      Object			the member variable corresponding to attributeName
     * @exception   AttributeNotFoundException
     * @exception   MBeanException
     * @exception   ReflectionException 
     */
    public Object getAttribute( String attributeName ) 
	throws AttributeNotFoundException, MBeanException, ReflectionException 
	{
	    Object attribute = null;
	    
	    if ( attributeName.equals( "LogName" ) )
	    {
	        attribute = logName;
	    }
		else if ( attributeName.equals( "Properties" ) )
		{
			attribute = properties.toString();
		}
		else if ( attributeName.equals( "DebugDestinations" ) )
		{
	    	attribute = debugDestinations.toString();			
		}
		else if ( attributeName.equals( "WarningDestinations" ) )
		{
	    	attribute = warningDestinations.toString();			
		}
		else if ( attributeName.equals( "InfoDestinations" ) )
		{
	    	attribute = infoDestinations.toString();			
		}
		else if ( attributeName.equals( "ErrorDestinations") )
		{
	    	attribute = errorDestinations.toString();			
		}	    
	    else
	    {
    	    // If attribute_name has not been recognized throw an AttributeNotFoundException
	        throw(new AttributeNotFoundException("Cannot find " + attributeName + " attribute in " + getClassName()));
	    }
	    
	    return( attribute );
    }

    /**
     * Sets the value of the specified attribute of the Dynamic MBean.
     * 
     * @param       attribute   previously assigned name to a particular member variable 
     * @exception   AttributeNotFoundException
     * @exception   InvalidAttributeValueException,
	 * @exception   MBeanException
	 * @exception   ReflectionException 
     */
    public void setAttribute( Attribute attribute ) 
	throws AttributeNotFoundException, InvalidAttributeValueException,
	       MBeanException, ReflectionException 
	{

	    // Check attribute is not null to avoid NullPointerException later on
	    if (attribute == null) 
	    {
	        throw new RuntimeOperationsException(new IllegalArgumentException("Attribute cannot be null"), 
			    			 "Cannot invoke a setter of " + getClassName() + " with null attribute");
	    }
	    
	    String name = attribute.getName();
//	    Object value = attribute.getValue();

	    if (name == null) 
	    {
	        throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name cannot be null"), 
						 "Cannot invoke the setter of " + getClassName() + " with null attribute name");
	    }
	    
	    // Check for a recognized attribute name and call the corresponding setter	    
	    {
	        throw(new AttributeNotFoundException("Attribute " + name +
			    			 " not found in " + this.getClass().getName()));
		}
    }

	/**
	 * Allows an operation to be invoked on the Dynamic MBean.
	 * <p>
	 * @param       operationName		registered method name
	 * @param       params				method parameters
	 * @param       signature
	 * @return      Object
	 * @exception   MBeanException
	 * @exception   ReflectionException
	 */
    public Object invoke( String operationName, Object params[], String signature[] )
	throws MBeanException, ReflectionException 
	{
		Object retObject = null;
		
	    // Check operationName is not null to avoid NullPointerException later on
	    if (operationName == null) 
	    {
	        throw new RuntimeOperationsException(new IllegalArgumentException("Operation name cannot be null"), 
			        			 "Cannot invoke a null operation in " + getClassName());
	    }
	    
	    String sMsg = (String)params[0];
	    
	    if ( operationName.equals( DEBUG ) )
	    {
	    	debug( sMsg );
	    }
	    else if ( operationName.equals( INFO ) )
	    {
	    	info( sMsg );
	    }
	    else if ( operationName.equals( WARNING ) )
	    {
	    	warning( sMsg );
	    }
	    else if ( operationName.equals( ERROR ) )
	    {
	    	error( sMsg );
	    }	    
	    else
	    // Check for a recognized operation name and call the corresponding operation
	    { 
	        // unrecognized operation name:
	        throw new ReflectionException(new NoSuchMethodException(operationName), 
					  "Cannot find the operation " + operationName + " in " + getClassName());
	    }
	    
	    return( retObject );
    }


// accessor methods - according to Java Bean spec.

    /**
     * Returns the log name
     * @return      String 
     */
    public String getLogName()
    {
        return( logName );
    }
    
    /**
     * Returns the synchronous flag
     * @return      boolean 
     */
    public boolean isSynchronous()
    {
        boolean is 	= true;       
        String sync    	= (String)getProperties().get( SYNCHRONOUS );

        if ( sync != null && sync.length() > 0 )
        {
            is = new Boolean( sync ).booleanValue();
        }
        
        return( is );            
    }
    
    /**
     * Returns the Collection of info related destinations as Strings
     * @return      Collection
     */    
    public Collection getInfoDestinations()
    {
        return( infoDestinations );
    }
    
    /**
     * Returns the Collection of warning related destinations as Strings
     * @return      Collection
     */    
    public Collection getWarningDestinations()
    {
        return( warningDestinations );
    }
    
    /**
     * Returns the Collection of error related destinations as Strings.
     * @return      Collection
     */    
    public Collection getErrorDestinations()
    {
        return( errorDestinations );
    }

    /**
     * Returns the Collection of debug related destinations as Strings.
     * @return      Collection
     */    
    public Collection getDebugDestinations()
    {
        return( debugDestinations );
    }

    /**
     * Returns the internal properties.
     * @return      Map
     */
    public Map getProperties()
    {
        return( properties );
    }


// action methods

    /** 
     * Convenience method for sending a message of certain log event type to the
     * assigned log handlers.
     * <p>
     * @param      	logEventType
     * @param     	msg
     * @exception	FrameworkLogException
     */
    public void log( FrameworkLogEventType logEventType, String msg )
    throws FrameworkLogException
    {
    	throw new RuntimeException( "FrameworkLogHandlerMBean:log() - should be overloaded in subclass.");
    }

    /**
     * Default handler for logging debug related messages.  
     * <p>
     * @param msg   text to log     
     */
    public void debug( String msg )
    {        
    	try
    	{
        	log( FrameworkLogEventType.DEBUG_LOG_EVENT_TYPE, msg );
    	}
    	catch( Throwable exc )
    	{
    		logErrorMessage( "FrameworkLogHandlerMBean:debug(String) - " + exc );
    	}
    }

    /**
     * Default handler for logging information related messages.
     * <p>
     * @param msg   text of the exception
     */
    public void info( String msg )
    {        
    	try
    	{
	        log( FrameworkLogEventType.INFO_LOG_EVENT_TYPE, msg );
    	}
    	catch( Throwable exc )
    	{
    		logErrorMessage( "FrameworkLogHandlerMBean:info(String) - " + exc );
    	}
	        
    }

    /**
     * Default handler for logging warning related messages.
     * <p>
     * @param msg   text of the exception
     */
    public void warning( String msg )
    {        
    	try
    	{    	
        	log( FrameworkLogEventType.WARNING_LOG_EVENT_TYPE, msg );
    	}
    	catch( Throwable exc )
    	{
    		logErrorMessage( "FrameworkLogHandlerMBean:warning(String) - " + exc );
    	}
        	
    }
    
    /**
     * Default handler for logging error related messages.
     * <p>
     * @param msg   text of the exception
     */
    public void error( String msg )
    {
    	try
    	{
        	log( FrameworkLogEventType.ERROR_LOG_EVENT_TYPE, msg );
    	}
    	catch( Throwable exc )
    	{
    		logErrorMessage( "FrameworkLogHandlerMBean:error(String) - " + exc );
    	}
        	
    }

// FrameworkDynamicMBean overloads

    /**
     * Returns the name of this class instance.  This implementation is
     * sufficient for singletons, but should be overloaded for sub-classes
     * requiring multiple instances.
     * <p>
     * @return		instance name
     */
    public String getInstanceName() 
    {
        return( "FrameworkLogHandler-" + logName );
    }

    /**
     * Returns the description of this MBean.
     * @return      String      
     */
    protected String getMBeanDescription()
    {
        return( "Framework Log Handler Dynamic MBean" );
    }
    
    
    /**
     * Returns all the attributes for this class as an array of MBeanAttributeInfos.
     * <p>
     * @return MBeanAttributeInfo[]
     */
    protected MBeanAttributeInfo[] getMBeanAttributes()
    {
        MBeanAttributeInfo[] mbAttributeInfo = new MBeanAttributeInfo[6];
        
        mbAttributeInfo[0] = new MBeanAttributeInfo( "LogName", "java.lang.String", "Log name: Unique log handler name.", true, false, false );
        mbAttributeInfo[1] = new MBeanAttributeInfo( "Properties", "String", "Properties:", true, false, false );
        mbAttributeInfo[2] = new MBeanAttributeInfo( "DebugDestinations", "String", "Log output locations for debug type messages", true, false, false );
        mbAttributeInfo[3] = new MBeanAttributeInfo( "InfoDestinations", "String", "Log output locations for info type messages", true, false, false );
        mbAttributeInfo[4] = new MBeanAttributeInfo( "WarningDestinations", "String", "Log output locations for warning type messages", true, false, false );
        mbAttributeInfo[5] = new MBeanAttributeInfo( "ErrorDestinations", "String", "Log output locations for error type messages", true, false, false );
               
        return( mbAttributeInfo );
    }

	/**
	 * Returns all operations as an array of MBeanOperationInfos.
	 * <p>
	 * @return MBeanOperationInfo[]
	 */
    protected MBeanOperationInfo[] getMBeanOperations()
    {
        MBeanOperationInfo[] mbOperations = new MBeanOperationInfo[4];
        MBeanParameterInfo[] params     = new MBeanParameterInfo[1];        
        
        params[0] = new MBeanParameterInfo( "msg", "java.util.String", "the message to log" );
        	
	    mbOperations[0] = new MBeanOperationInfo( DEBUG,
						DEBUG + "(): Log a debug message.",
						params , 
						"void", 
						MBeanOperationInfo.ACTION);        

	    mbOperations[1] = new MBeanOperationInfo( INFO,
						INFO + "(): Log an informational message.",
						params , 
						"void", 
						MBeanOperationInfo.ACTION);        

	    mbOperations[2] = new MBeanOperationInfo( WARNING,
						WARNING + "(): Log a warning message.",
						params , 
						"void", 
						MBeanOperationInfo.ACTION);        

	    mbOperations[3] = new MBeanOperationInfo( ERROR,
						ERROR + "(): Log an error message.",
						params , 
						"void", 
						MBeanOperationInfo.ACTION);        
						
        return( mbOperations );						
    }

// attributes

	/**
	 * Log handler name - should be unique amongst its peers
	 */
    protected String logName                 = null;
    
    /**
     * Related log properties
     */
    protected Map properties            = null;


	/**
	 * debug related log destinations
	 */
    protected Collection debugDestinations        = null; 
    
    /**
     * information related log destinations
     */    
    protected Collection infoDestinations         = null; 
    
    /**
     * warning related log destinations
     */
    protected Collection warningDestinations      = null; 
    
    /**
     * error related log destinations
     */
    protected Collection errorDestinations        = null; 
}

/*
 * Change Log:
 * $Log: FrameworkLogHandlerMBean.java,v $
 */
