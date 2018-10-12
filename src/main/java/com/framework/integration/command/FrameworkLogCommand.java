/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.command;

//************************************
// Imports
//************************************
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.*;

import com.framework.common.FrameworkBaseObject;
import com.framework.common.event.FrameworkLogEventType;

import com.framework.common.exception.CommandExecutionException;
import com.framework.common.exception.ConnectionAcquisitionException;
import com.framework.common.exception.FrameworkJMSException;

import com.framework.common.logging.FrameworkLoggingHelper;

import com.framework.common.message.IFrameworkMessage;

import com.framework.integration.log.IFrameworkLogHandler;
import com.framework.integration.log.IFrameworkLogMessage;

import com.framework.integration.objectpool.ConnectionPoolManagerFactory;
import com.framework.integration.objectpool.IConnectionImpl;

import com.framework.integration.objectpool.jms.JMSImpl;

import com.framework.integration.objectpool.mq.FrameworkMQAdapter;
import com.framework.integration.objectpool.mq.IMQConnectionImpl;

import com.framework.integration.task.IFrameworkTask;

/**
 * Handles default semantics for handling the logging of a Framework related
 * message.
 * <p>
 * @author    realMethods, Inc.
 */
public class FrameworkLogCommand extends FrameworkCommand
{
    /**
     * place where the command is notified to do it's work
     * with respect to the input msg.
     *
     * @param     	theMessage	external data context to work within
     * @param     	task  		can optionally be null
     * @exception   CommandExecutionException
     *
     */
    public void onExecute( IFrameworkMessage theMessage, IFrameworkTask task )
    throws CommandExecutionException
    {
    	// ignore such a request...
    	if ( theMessage == null )
    		return;	
    		
        // ensure that the msg is of type IFrameworkLogMessage msg 
        if ( !(theMessage instanceof IFrameworkLogMessage) )
        {
            // dig deeper to see if it is contained in the message
            if ( (theMessage.getMessage() instanceof IFrameworkLogMessage) )
                theMessage = (IFrameworkMessage)theMessage.getMessage();
            else
            {
                logWarnMessage( "FrameworkLogCommand:execute(...) - msg is not of type IFrameworkLogMessage" );
                return;
            }
        }
        
        
        // acquire the correct Destinations from the FrameworkLogHandler
        Collection destinations = ((IFrameworkLogMessage)theMessage).getFrameworkLogHandler().getDestinations( ((IFrameworkLogMessage)theMessage).getFrameworkLogEventType() );
        
        if ( destinations != null )
        {
            // iterate over the destinations, determine each type, 
            // and handle accordingly
            Iterator iter       = destinations.iterator();
            String destination = null;
            
            while( iter.hasNext() )
            {
                destination = (String)iter.next();
                handleLoggingDuties((IFrameworkLogMessage)theMessage, destination );                
            }
        }
        else
            throw new CommandExecutionException( "FrameworkLogCommand:execute(...) - no destinations to delegate to for " + ((IFrameworkLogMessage)theMessage).getFrameworkLogEventType().toString() );

    }
    
// helper methods

	/**
	 * Helper method used to determine where to send the log message, based on the provided destination
	 * 
	 * @param		frameworkLogMessage
	 * @param		destination				where the msg is to be dispatched to
	 */
    protected void handleLoggingDuties( IFrameworkLogMessage frameworkLogMessage, String destination )
    {
    	IFrameworkLogHandler frameworkLogHandler    = frameworkLogMessage.getFrameworkLogHandler();
    	FrameworkLogEventType frameworkLogEventType = frameworkLogMessage.getFrameworkLogEventType();
    	
        // no_op if there is no destination 
        if ( destination == null || destination.length() == 0 )
            return;
            
        boolean bHandled            = false;
        StringBuffer logMsgBuffer  = new StringBuffer();
        
        // need to determine what is the destination

        // first check to see if it is a SYSTEM_OUT        
        if( destination.equalsIgnoreCase( IFrameworkLogHandler.SYSTEM_OUT ) )
        {
            logMsgBuffer.append( frameworkLogHandler.getDateTimeStampFormatted() );
            logMsgBuffer.append( " : " );
            logMsgBuffer.append( frameworkLogMessage.getLogFormattedMessage() );
            
            System.out.println( logMsgBuffer.toString() );
            
            bHandled = true;
        }
        else // check to see if it something from the connection pool
        {
            IConnectionImpl connImpl = null;
            
            try
            {
                connImpl = ConnectionPoolManagerFactory.getObject().getConnection( destination );
            }
            catch( ConnectionAcquisitionException exc )
            {
            	// this is acceptable since the name may not belong to a pooled connection
            }
            catch( Throwable exc )
            {
                logErrorMessage( "FrameworkLogCommand:handleLoggingDuties() failed to acquire a ConnectionPoolImp " + connImpl + " by the name of " + destination + " - " + exc );
            }
            
            if ( connImpl != null )
            {
                logMsgBuffer.append( frameworkLogHandler.getDateTimeStampFormatted() );
                logMsgBuffer.append( " : " );
                logMsgBuffer.append( frameworkLogMessage.getLogFormattedMessage() );
                
                // got something, so determine what it is
                if ( connImpl instanceof JMSImpl )  // JMS
                {
                    JMSImpl jmsImpl = (JMSImpl)connImpl;
                    
                    try
                    {
	                    jmsImpl.sendMessage( logMsgBuffer.toString() );	                                                        
	                    bHandled = true;
                    }
                    catch( FrameworkJMSException exc )
                    {
                    	logErrorMessage( "FrameworkLogCommand:handleLoggingDuties() " + exc );
                    }
                }
                else if ( connImpl instanceof IMQConnectionImpl ) // IBM MQ
                {                    
                    IMQConnectionImpl mqConnImpl = (IMQConnectionImpl)connImpl;
                    
                    try
                    {
                        mqConnImpl.send( new FrameworkMQAdapter( logMsgBuffer.toString() ) );                        
                    }
                    catch( Exception exc )
                    {
                    }
                    
                    bHandled = true;
                }
                else
                    logWarnMessage( "Invalid logging destination type discovered for " + destination );
                
                try
                {
                    // commit and release the connection
                    connImpl.commit();                    
                    ConnectionPoolManagerFactory.getObject().releaseConnection( connImpl );
                }
                catch( Throwable exc )
                {
                    logErrorMessage( "FrameworkLogCommand:handleLoggingDuties() " + exc );
                }
            }
        }
        
        if ( bHandled == false ) // assume the name is associated with a logging.properties logger
        {
        	new FrameworkBaseObject().logMessage( frameworkLogMessage.getLogFormattedMessage(), frameworkLogEventType );
        }

		// expedite gc        
    	frameworkLogHandler 	= null;
    	frameworkLogEventType 	= null;
        
    }
    
// attributes

}

/*
 * Change Log:
 * $Log: FrameworkLogCommand.java,v $
 */

