/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.log;

import java.util.*;

import com.framework.common.event.FrameworkLogEventType;

import com.framework.common.exception.FrameworkLogException;

import com.framework.common.mgr.FrameworkManagerFactory;
import com.framework.common.mgr.IFrameworkManager;

import com.framework.common.namespace.FrameworkNameSpace;

import com.framework.integration.task.IFrameworkTask;
import com.framework.integration.task.TaskJMSExecutionHandler;
import com.framework.integration.task.TaskJMSExecutionRegistryFactory;
import com.framework.integration.task.TaskStatus;

/**
 * Base class for all log handlers in the framework.
 * <p>
 * For each entry defined in the log.properties, the FrameworkLogHandlerFactory creates an instance
 * of this class.
 * <p>
 * @author    realMethods, Inc.
 * @see		  com.framework.integration.log.FrameworkLogHandlerFactory
 * @see		  com.framework.integration.log.FrameworkDefaultLogger
 */
public class FrameworkLogHandler 
	extends FrameworkLogHandlerMBean
{ 
//****************************************************
// Public Methods
//****************************************************

    /**
     * default constructor
     */
	public FrameworkLogHandler()
	{}
	
    /**
     * constructor
     *
     * @param       name		unique name assigned
     * @param       props		configuration parameters
     */
    public FrameworkLogHandler( String name, Map props )
    throws IllegalArgumentException
    {
        logName     = name;
        properties	= props;
        
        try
        {
            // parse the info, warning, and error destination for faster access
            // later on
            debugDestinations     = getDestinations( DEBUG );
            infoDestinations      = getDestinations( INFO );
            warningDestinations   = getDestinations( WARNING );
            errorDestinations     = getDestinations( ERROR );
            
        }
        catch ( Throwable e )
        {
            throw new IllegalArgumentException( "FrameworkLogHandler:initialize() - " + e );
        }
        
        // once attributes are established, safe to attempt JMX MBean Registration
        handleSelfRegistration();
    }    

// IFrameworkLogHandler implementations

// accessor methods

    /**
     * Returns the Collection of destinations associated with the provided
     * FrameworkLogEventType.
     * <p>
     * @param		type			log event type
     * @return		Collection		log destinations, depending on input log event type
     */
    public Collection getDestinations( FrameworkLogEventType type )
    {
        Collection destinations = null;
        
        if ( type.isInfoLogEventType() )
        {
            destinations = infoDestinations;
        }
        else if ( type.isWarningLogEventType() )
        {
            destinations = warningDestinations;
        }
        else if ( type.isErrorLogEventType() )
        {
            destinations = errorDestinations;
        }
        else if ( type.isDebugLogEventType() )
        {
            destinations = debugDestinations;
        }

        
        return( destinations );
    }

    /**
     * returns the datetime format to use
     *
     * @return		date/timestamp formatted Strimg
     */
    public String getDateTimeStampFormatted()
    {
        String sDateTimeFormat      = (String)getProperties().get( DATE_TIME_STAMP_FORMAT );
        StringBuffer sFormatBuffer  = new StringBuffer( "" );
        
        if ( sDateTimeFormat != null )
        {
            sFormatBuffer.append( "<" );
            java.util.Calendar now  = java.util.Calendar.getInstance();
            sFormatBuffer.append( new java.text.SimpleDateFormat( sDateTimeFormat ).format( now.getTime() ) );
            sFormatBuffer.append( ">" );
        }
        
        return( sFormatBuffer.toString() );            
    }



// action methods

    
    /**
     * Used to send a message, of a particular type, to the associated log destinations.
     * <p>
     * @param		logEventType		log event type
     * @param    	message				text to log
     * @exception	FrameworkLogException
     */
    public void log( FrameworkLogEventType logEventType, String message )
    throws FrameworkLogException
    {
    	IFrameworkLogMessage logMessage 	= null;
    	
        try
        {
            // still want to cycle through the factory for this...
            logMessage = new FrameworkLogMessage( this, logEventType, message );
                                                                        
            if ( isSynchronous() == true )    // send it directly to the task
            {
				IFrameworkManager frameworkManager = FrameworkManagerFactory.getObject();            	
                frameworkManager.executeTask( FrameworkNameSpace.DEFAULT_LOG_TASK_NAME, logMessage );
            }
            else    // user Task related JMS Connection
            {
            	if ( logTask == null )
            	{	            	
	                HashMap taskHandlers = TaskJMSExecutionRegistryFactory.getInstance().getObject().getTaskJMSExecutionHandlers();
	                
	                TaskJMSExecutionHandler taskHandler = (TaskJMSExecutionHandler)(taskHandlers.get( FrameworkNameSpace.DEFAULT_LOG_TASK_NAME ));
					logTask                 			= taskHandler.getFrameworkTask();
            	}
            	
	            if ( logTask != null )
	            {
	            	while( logTask.getTaskStatus().isIdle() == false );
	            	
					logTask.execute( logMessage, false );
	            }
	            else
	            {
	                logWarnMessage( "FrameworkManager.sendDefaultLogMessage() - " + logEventType.toString() + " failed to acquire the default Log Task.  Check the task.properties for an entry named " + FrameworkNameSpace.DEFAULT_LOG_TASK_NAME );
	            }                    
            }                
        }
        catch( Throwable exc )
        {
            throw new FrameworkLogException( "FrameworkLogHandler.log() - " + logEventType.toString() + " - " + exc, exc );
        }
        finally
        {
        	if ( logTask != null )
        		logTask.setTaskStatus( TaskStatus.Idle );
        	
        	// to expedite gc
        	logMessage = null;        	
        }
    }

    /**
     * returns the global Framework common Log Handler
     * @return      IFrameworkLogHandler
     * @exception   FrameworkLogException
     */
    static public IFrameworkLogHandler getDefaultLogHandler()
    throws FrameworkLogException
    {
		return( FrameworkDefaultLogger.getDefaultLogHandler() );            
    }

    
    /**
     * Internal helper method used to parse the comm delimitted
     * destinations for the provided key.
     *
     * @param       key			
     * @return      Collection      destinations as Strings
     */
    protected Collection getDestinations( String key )
    {
        Collection destinations = new ArrayList();
        String value       		= (String)getProperties().get( key );
        
        if ( value != null && value.length() > 0 )
        {
            // parse on the token ',' and add to the Collection
            StringTokenizer tokenizer   = new StringTokenizer( value, "," );
            String sDestination         = null;            

            while( tokenizer.hasMoreTokens() )
            {
                sDestination = ((String)(tokenizer.nextToken())).trim();                
                destinations.add( sDestination );
            }
        }
        else    // nothing provided, so default to SYSTEM_OUT
        {
            destinations.add( SYSTEM_OUT );
        }
        
        return( destinations );
    }
    
// attributes
	
	/**
	 * cached logging task
	 */
   	IFrameworkTask logTask  			= null;
}

/*
 * Change Log:
 * $Log: FrameworkLogHandler.java,v $
 */
