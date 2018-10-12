/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.objectpool.jms;

import java.util.Map;

import javax.jms.MessageListener;

import com.framework.common.exception.ConnectionActionException;
import com.framework.common.exception.FrameworkJMSException;
import com.framework.common.exception.InitializationException;

import com.framework.integration.objectpool.ConnectionImpl;

/**
 * A poolable helper class for making use of JMS connection (Queue or Topic) in a generic way.
 * <p>
 * @author    realMethods, Inc.
 */
abstract public class JMSImpl extends ConnectionImpl
{
//****************************************************
// Public Methods
//****************************************************
    public JMSImpl()
    {
    }

    /** 
     * Initializes and makes a connection to the connection source.
     * This base implementation does not make the actual connection.
     * This method must be overriden in the subclasses.
     * @param name 	The pool name that this connection is associated with.
     * @param props Map of properties needed to create the connection.
     * @exception	IllegalArgumentException 
     * @exception 	InitializationException
     */
    public void initialize( String name, Map props )
    throws IllegalArgumentException, InitializationException
    {
        super.initialize( name, props );

        try
        {
            initJMS();
        }
        catch ( FrameworkJMSException exc )
        {
            throw new InitializationException( "JMSImpl:initialize(...) - " + exc, exc );
        }
    }    

    /**
     * Force subclass to implement an initialization routine.
     * @exception	FrameworkJMSException
     */
    abstract protected void initJMS()
    throws FrameworkJMSException;
    
    /**
     * client notification that a transaction is about to begin
     */
    public void startTransaction()
    throws ConnectionActionException
    {}

    /**
     * client notification to commit the current transaction
     */
    public void commit()
    throws ConnectionActionException
    {}

    /**
     * client notification to rollback the current transaction
     */
    public void rollback()
    throws ConnectionActionException
    {}


    /**
     * assigns a MessageListener interface as an interested party
     * in this JMS msg server
     *
     * @param           listener
     * @exception       FrameworkJMSException
     */
    abstract public void assignAsListener( MessageListener listener )
    throws FrameworkJMSException;

    /**
     * assigns a MessageListener interface as an interested party
     * in this JMS msg server
     *
     * @param           listener
     * @param           msgselector
     * @exception       FrameworkJMSException
     */
    abstract public void assignAsListener( MessageListener listener, String msgselector )
    throws FrameworkJMSException;

    /**
     * the name to associate with this JMS instance as read in from
     * the properties file
     *
     * @return      String
     */
    public String getJMSName()
    {
        //return( getPoolName() );
        return( (String)getProperties().get( "JNDIName" )  );
    }
    
    /**
     * The name to associate with the factory applied to this
     * this JMS instance as read in from the properties file.
     * <p>
     * @return      String
     */
    public String getFactoryName()
    {
        return( (String)getProperties().get( "factoryName" )  );
    }
    

    /**
     * Sends a message as a String.
     * <p>
     * @param      	msg
     * @exception	FrameworkJMSException
     */
    abstract public void sendMessage( String msg )
    throws FrameworkJMSException;

    /**
     * Sends a serializable object.
     * <p>
     * @param      	serializable
     * @exception	FrameworkJMSException
     */
    abstract public void sendObject( java.io.Serializable serializable )
    throws FrameworkJMSException;
               
    /**
     * Sends a serialized object, along with its correlation id
     * <p>
     * @param       serializable
     * @param       correlationid
     * @exception	FrameworkJMSException
     */
    abstract public void sendObject( java.io.Serializable serializable, String correlationid )
    throws FrameworkJMSException;
    
    /**
     * After sending, immediately receives a javax.jms.Message, waiting for
     * the specified period of time.  O indicates to wait indefinitely. Provide
     * null for the correlation id, if not applicable. A null return Message means
     * nothing was received within the time period specified.
     *
     * @param   	serializable
     * @param   	correlationid
     * @param   	timeout
     * @exception	FrameworkJMSException
     * @return  javax.jms.Message
     */
    abstract public javax.jms.Message sendObjectandReceive( java.io.Serializable serializable,
    															String correlationid, 
    															long timeout )
    throws FrameworkJMSException;
    
// attributes

    /**
     * The JMS factory
     */
    protected static String jmsFactory               = null;

}

/*
 * Change Log:
 * $Log: JMSImpl.java,v $
 */
