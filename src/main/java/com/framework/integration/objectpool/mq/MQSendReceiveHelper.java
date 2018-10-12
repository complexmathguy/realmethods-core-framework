/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.objectpool.mq;

import java.util.Collection;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.exception.FrameworkMQBaseException;

import com.framework.integration.objectpool.ConnectionPoolManagerFactory;

/**
 * Helper class used to co-ordinate the send/receive handshake
 * using separate send and receive MQConnectionImpl from the
 * connection pool manager
 *
 * <p>
 * @author    realMethods, Inc.
 */
public class MQSendReceiveHelper extends FrameworkBaseObject
{

// constructor

    /**
     * default constructor
     */
    public MQSendReceiveHelper()
    {
    }
    
    /**
     * Encapsulate the synchronous process of sending and receiving a message on
     * two separate named MQ Queues.
     *
     * @param   sendPooledQueueName
     * @param   recvPooledQueueName
     * @param   MQAdapter
     * @exception IllegalArgumentException
     * @exception FrameworkMQBaseException
     * @return  Collection      
     */
    public Collection sendReceive(	String sendPooledQueueName, 
                                    String recvPooledQueueName, 
                                    IFrameworkMQAdapter MQAdapter )
    throws IllegalArgumentException, FrameworkMQBaseException
    {
        if ( sendPooledQueueName == null || sendPooledQueueName.length() == 0 )
        {
            throw new IllegalArgumentException( "MQSendReceiveHelper:sendReceive() - send pooled queue name is invalid." );
        }

        if ( recvPooledQueueName == null || recvPooledQueueName.length() == 0 )
        {
            throw new IllegalArgumentException( "MQSendReceiveHelper:sendReceive() - rcv pooled queue name is invalid." );
        }
        
        if ( MQAdapter == null )
        {
            throw new IllegalArgumentException( "MQSendReceiveHelper:sendReceive() - MQAdapater is null." );
        }
        
        try
        {
	        // Get the MQ Connections ready for use
	        prepareMQConnections( sendPooledQueueName, recvPooledQueueName);
	
	      	logDebugMessage( "MQConnections: " + sendMQConnectionImpl + " " +  receiveMQConnectionImpl );               
	        
	        // apply same correlation ID to both the send and receive messages
	        String sCorrelationID   = generateUniqueCorrelationID();
	        byte[] bytes = sCorrelationID.getBytes();
	        
	        sendMQConnectionImpl.setCorrelationID( bytes );
	        receiveMQConnectionImpl.setCorrelationID( bytes );
	                       
	        logDebugMessage( "CorrelationID is " + bytes );
	        
	        // seed the send message initially with the first message to send
	        // from the provided MQAdapter
	        IFrameworkMQAdapter sendMQAdapter   = MQAdapter;
	        IFrameworkMQAdapter recvMQAdapter   = null;
	        String sSendMessage                 = null; 
	        boolean bKeepReading                = true;
	        
	        // continue loop until reading is no longer necessary
	        while( bKeepReading == true )
	        {
	            logDebugMessage( "Sending msg: " + sendMQAdapter.provideDataToSend().getMessageAsString() );
	            
	            // first send the message
	            sendMQConnectionImpl.send( sendMQAdapter );
	            sendMQConnectionImpl.commit();
	            
	            // next, receive the message into an IMQAdapter
	            recvMQAdapter = receiveMQConnectionImpl.receive();
	            receiveMQConnectionImpl.commit();
	            
	            // next read the message into the provided MQAdapter
	            MQAdapter.receiveData( recvMQAdapter.getLastReceivedData() );
	            
	            // determine if reading should continue by looking at the message just received...
	            sSendMessage = MQAdapter.determineMessageToReSend( MQAdapter.getLastReceivedData() );
	            
	            if ( sSendMessage == null ) // termination defined
	            {
	                // done reading...
	                bKeepReading = false;
	            }
	            else
	            {
	                sendMQAdapter = new FrameworkMQAdapter( sSendMessage );
	            }
	        }
	        
	        // release the MQ Connections
	        releaseMQConnections();
        }
        catch( Throwable exc )
        {
        	throw new FrameworkMQBaseException( "MQSendReceiveHelper:sendReceive() - " + exc, exc );
        }
                
        return( MQAdapter.getResultsAsCollection() );
        
    }
    
// helper methods

    /**
     * Helper method used to internally access and assign the send and receive
     * MQConnectionImpls from the ConnectionPoolManager.
     * <p>
     * @param           sendQueue
     * @param           recvQueue
     * @exception       FrameworkMQBaseException
     */
    protected void prepareMQConnections( String sendQueue, String recvQueue )
    throws FrameworkMQBaseException
    {
        try
        {
            sendMQConnectionImpl = (IMQConnectionImpl)ConnectionPoolManagerFactory.getObject().getConnection( sendQueue );
        }
        catch( Throwable exc )
        {
            throw new FrameworkMQBaseException( "MQSendReceiveHelper:prepareMQConnections() - failed to acquire Send MQConnectionImpl named " + sendQueue, exc );
        }

        try
        {
            receiveMQConnectionImpl = (IMQConnectionImpl)ConnectionPoolManagerFactory.getObject().getConnection( recvQueue );
        }
        catch( Throwable exc )
        {
            throw new FrameworkMQBaseException( "MQSendReceiveHelper:prepareMQConnections() - failed to acquire Receive MQConnectionImpl named " + recvQueue, exc );
        }
    }

    /**
     * Helper method used to release the send and receive queues back to the
     * ConnectionPoolManager.
     * <p>
     * @exception       FrameworkMQBaseException
     */
    protected void releaseMQConnections()
    throws FrameworkMQBaseException
    {
        if ( sendMQConnectionImpl != null )
        {
            try
            {
                ConnectionPoolManagerFactory.getObject().releaseConnection( sendMQConnectionImpl );
            }
            catch( Throwable exc )
            {
                throw new FrameworkMQBaseException( "MQSendReceiveHelper:releaseMQConnections() - failed to release Send MQConnectionImpl named " + sendMQConnectionImpl.getPoolName(), exc );
            }
        }
        
        if ( receiveMQConnectionImpl != null )
        {
            try
            {
                ConnectionPoolManagerFactory.getObject().releaseConnection( receiveMQConnectionImpl );
            }
            catch( Throwable exc )
            {
                throw new FrameworkMQBaseException( "MQSendReceiveHelper:releaseMQConnections() - failed to release Receive MQConnectionImpl named " + receiveMQConnectionImpl.getPoolName(), exc);
            }
        }
    }
    
    /**
     * Generates a unique correlation ID. Delegates to Utility.generateUID().
     * <p>
     * @return 	the geneated uid
     * @see		com.framework.common.misc.Utility#generateUID()
     */
    static public String generateUniqueCorrelationID()
    {
        return( com.framework.common.misc.Utility.generateUID() );
    }

// attributes

    protected IMQConnectionImpl sendMQConnectionImpl      = null;
    protected IMQConnectionImpl receiveMQConnectionImpl   = null;

}

/*
 * Change Log:
 * $Log: MQSendReceiveHelper.java,v $
 * Revision 1.4  2003/10/08 02:01:28  tylertravis
 * Use logxxxMessage in place of logInfoMessage.
 *
 * Revision 1.3  2003/09/11 19:50:37  tylertravis
 * Removed Exception and BaseException declarations in javadocs for sendReceive(...)
 *
 * Revision 1.2  2003/08/05 12:16:17  tylertravis
 * - Updated header language to LGPL
 * - Improved exception handling globally
 * - Modified member variables to conform to Java Beans std.
 *
 * Revision 1.1.1.1  2003/07/05 03:05:44  tylertravis
 * initial sourceforge cvs revision
 *
 */
