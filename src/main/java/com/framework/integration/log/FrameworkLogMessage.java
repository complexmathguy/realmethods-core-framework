/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.log;

import com.framework.common.event.FrameworkLogEventType;

import com.framework.common.message.FrameworkMessage;

import com.framework.common.namespace.FrameworkNameSpace;

/**
 * Minimum implementation for a Framework Log message
 *
 * <p>
 * @author    realMethods, Inc.
 */
public class FrameworkLogMessage extends FrameworkMessage
    implements IFrameworkLogMessage
{
    
// constructors    

    /**
     * default constructor
     */
    public FrameworkLogMessage()
    {}
    
    /**
     * constructor
     *
     * @param    	logHandler					responsible for logging
     * @param       logEventType				type of log event
     * @param     	msg							text to log
     * @exception	IllegalArgumentException 	thrown if  logHandler or logEventType are null
     */
    public FrameworkLogMessage( 	IFrameworkLogHandler logHandler, 
                               		FrameworkLogEventType logEventType,
                                	String msg )
    throws IllegalArgumentException
    {
        super( msg );
        
        if ( logHandler == null )
            throw new IllegalArgumentException( "FrameworkLogCommand:execute(..) - FrameworkLogMessage contains a null FrameworkLogHandler." );
            
        if ( logEventType == null )
            throw new IllegalArgumentException( "FrameworkLogCommand:execute(..) - FrameworkLogMessage contains a null FrameworkLogEventType." );

        frameworkLogHandler    = logHandler;
        frameworkLogEventType  = logEventType;
    }
    
// IFrameworkLogMessage implementations

    /**
     * returns the bound FrameworkLogHandler 
     * @return      IFrameworkLogHandler
     */
    public IFrameworkLogHandler getFrameworkLogHandler()
    {
        return( frameworkLogHandler );
    }
    
    /**
     * returns the bound FrameworkLogEventType
     * @return		FrameworkLogEventType
     */
    public FrameworkLogEventType getFrameworkLogEventType()
    {
        return( frameworkLogEventType );
    }

    /**
     * Returns the associated message 
     * in a standard format using the log event type, log name, and message.
     * <p>
     * @return      formated message
     */
    public String getLogFormattedMessage()
    {
        StringBuffer msgBuffer = new StringBuffer( FrameworkNameSpace.INSTANCE_NAME );
        
        msgBuffer.append( " - " );
        msgBuffer.append( frameworkLogEventType.toString() );        

        msgBuffer.append( " [" );
        
        msgBuffer.append( frameworkLogHandler.getLogName() );
        msgBuffer.append( "] - " );
        
        msgBuffer.append( super.getMessageAsString() );
        
        return( msgBuffer.toString()  );
    }

// attributes

	/**
	 * log handler
	 */
    protected IFrameworkLogHandler  frameworkLogHandler    = null;
    /**
     * log event type
     */
    protected FrameworkLogEventType frameworkLogEventType  = null;
}

/*
 * Change Log:
 * $Log: FrameworkLogMessage.java,v $
 */
