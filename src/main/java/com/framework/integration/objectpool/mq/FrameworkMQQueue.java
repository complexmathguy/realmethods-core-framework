/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.objectpool.mq;

//************************************
// Imports
//************************************
import com.framework.common.FrameworkBaseObject;

import com.framework.common.exception.FrameworkMQBaseException;
import com.framework.common.exception.ObjectCreationException;

import com.framework.common.message.IFrameworkMQMessage;

import com.ibm.mq.MQC;
import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;

/**
 * Extends the notion of an IBM MQQueue
 *
 * <p>
 * @author    realMethods, Inc.
 */
public class FrameworkMQQueue 
    extends FrameworkBaseObject implements IFrameworkMQQueue
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * default constructor - protected to deter instatiation
     */
    protected FrameworkMQQueue()
    {
    }

    /**
     * 
     * @param      	qname
     * @param       connection
     * @param      	bOpenInSendMode - open for gets or puts
     * @exception	ObjectCreationException
     */
    protected FrameworkMQQueue( String qname, IMQConnectionImpl connection, boolean bOpenInSendMode )
    throws ObjectCreationException
    {
        int openOptions = MQC.MQOO_FAIL_IF_QUIESCING;

        if ( bOpenInSendMode == true )
        {
            openOptions += MQC.MQOO_OUTPUT;
        }
        else
        {
            openOptions += MQC.MQOO_INPUT_AS_Q_DEF;
        }

        queueName        = qname;
        mqConnection     = connection;

        try
        {
            // member data assignment
            theIBMMQQueue = mqConnection.getQueueManager().accessQueue(  qname, openOptions, null, null, null );         
        }
        catch ( Throwable exc )
        {
            // call it quits!!
            throw new ObjectCreationException( "FrameworkMQQueue constructor() - " + exc.getMessage(), exc );
        }
    }

    /**
     * factory method
     *
     * @param       qname
     * @param       mqConnection
     * @param       bOpenInSendMode
     * @exception   IllegalArgumentException
     * @exception   ObjectCreationException
     */
    static public IFrameworkMQQueue getInstance( String qname, IMQConnectionImpl mqConnection, boolean bOpenInSendMode )
    throws IllegalArgumentException, ObjectCreationException
    {
        if ( qname == null || qname.length() == 0 )
            throw new IllegalArgumentException( "MQQueue.getInstance() - invalid queue name provided." );

        if ( mqConnection == null )
            throw new IllegalArgumentException( "MQQueue.getInstance() - invalid MQConnection provided." );

        return( new FrameworkMQQueue( qname, mqConnection, bOpenInSendMode ) );
    }

// IFrameworkMQQueue implementations

    /**
     * places the message onto the bound queue
     *
     * @param       msg
     * @exception   FrameworkMQBaseException
     */
    public void put( IFrameworkMQMessage msg )
    throws FrameworkMQBaseException
    {
        putMessageOptions.options = MQC.MQPMO_SYNCPOINT + MQC.MQPMO_FAIL_IF_QUIESCING;

        try
        {
            msg.formatOnSend();
            getTheIBMMQQueue().put( msg.getMQMessage(), putMessageOptions );
        }
        catch ( MQException mqExc )
        {
            handleMQException( mqExc );
        }
        catch ( Throwable exc )
        {
            throw new FrameworkMQBaseException( "MQQueue:put() - Failed to put the string onto the queue - " + exc.getMessage(), exc );
        }
    }

    /**
     * returns the message as a String from the bound queue
     *
     * @param       message
     * @return      String
     * @exception   FrameworkMQBaseException
     */
    public String get( IFrameworkMQMessage message )
    throws FrameworkMQBaseException
    {
        MQMessage mqMessage = message.getMQMessage();
        String s            = null;

        getMessageOptions.options = MQC.MQGMO_WAIT | MQC.MQGMO_SYNCPOINT | MQC.MQGMO_FAIL_IF_QUIESCING;
        getMessageOptions.matchOptions = MQC.MQMO_MATCH_CORREL_ID;
        
        try
        {
            getTheIBMMQQueue().get( message.getMQMessage(), getMessageOptions );
            s = mqMessage.readString( mqMessage.getTotalMessageLength() );

            // give it to the provide MQMessage
            message.setMessage( s );

            // tell it to format on receive
            message.formatOnReceive();
        }
        catch ( MQException mqExc )
        {
            handleMQException( mqExc );
        }
        catch ( Throwable exc )
        {
            throw new FrameworkMQBaseException( "MQQueue:get() - Failed to read the string from the queue - " + exc.getMessage(), exc );
        }

        // return the result as a String
        return( message.getMessageAsString() );
    }    

// accessor methods

    public MQGetMessageOptions getGetMessageOptions()
    {
        return( getMessageOptions );
    }

    public MQPutMessageOptions getPutMessageOptions()
    {
        return( putMessageOptions );
    }


    /**
     * returns the contained IBM MQQueue
     *
     * @return  MQQueue 
     */
    public MQQueue getTheIBMMQQueue()
    {
        return( this.theIBMMQQueue );
    }

// helper methods

    /**
     * common handler for MQException
     *
     * @param       ex
     * @exception   FrameworkMQBaseException
     */
    protected void handleMQException( MQException ex )
    throws FrameworkMQBaseException
    {
        int reasonCode  = ex.reasonCode;

        switch ( reasonCode )
        {
            case 2033:
                {
                    /**
                     * MQRC_NO_MSG_AVAILABLE
                     */
                     
                    logErrorMessage("WARNING:  MQQueue->receive: Q_Empty...reasonCode is " + ex.reasonCode);
                    throw new FrameworkMQBaseException( 5 );
                }

            case 2009|2161|2162|2018:
                {
                    /**
                     * MQRC_CONNECTION_BROKEN OR MQRC_Q_MGR_QUIESCING
                     * OR MQRC_Q_MGR_STOPPING
                     */

                    logErrorMessage("CRITICAL:  MQQueue->receive: MQ_Unavailable...reasonCode is " + ex.reasonCode);
                    throw new FrameworkMQBaseException( 1 );
                }

            case 2080:
                {
                    /**
                     * MQRC_TRUNCATED_MSG_FAILED
                     */
                    logErrorMessage("WARNING:  MQQueue->receive: Message_Too_Big...reasonCode is " + ex.reasonCode);
                    throw new FrameworkMQBaseException( 4 );
                }

            case 2016:
                {
                    /**
                     * MQRC_GET_INHIBITED
                     */
                    logErrorMessage("CRITICAL:  MQQueue->receive: Q_Unavailable...reasonCode is " + ex.reasonCode);
                    throw new FrameworkMQBaseException( 2 );
                }

            case 2101:
                {
                    /**
                     * MQRC_OBJECT_DAMAGED
                     */
                    logErrorMessage("CRITICAL:  MQQueue->receive: Object_Damaged...reasonCode is " + ex.reasonCode);
                    throw new FrameworkMQBaseException( 7 );
                }

            default:
                {
                    logErrorMessage("CRITICAL:  MQQueue->receive: MQ_Error...reasonCode is " + ex.reasonCode);
                    throw new FrameworkMQBaseException( 9 );
                }               
        }
    }

// attributes

    /**
     * queue name
     */
    protected String queueName                   		= null;

    /**
     * bound MQQueue
     */
    protected com.ibm.mq.MQQueue theIBMMQQueue 		= null;

    /**
     * associate MQConneciton
     */
    protected IMQConnectionImpl mqConnection      	= null;     

    /**
     * default send and receive queue options
     */
    protected MQGetMessageOptions getMessageOptions 	= new MQGetMessageOptions();
    protected MQPutMessageOptions putMessageOptions	= new MQPutMessageOptions();

}

/*
 * Change Log:
 * $Log: FrameworkMQQueue.java,v $
 */

