/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.objectpool.mq;

//************************************
// Imports
//************************************
import com.framework.common.exception.FrameworkMQBaseException;

import com.framework.integration.objectpool.IConnectionImpl;

import com.ibm.mq.MQQueueManager;

/**
 * Interface used to encapsulate a connection to an MQ Queue 
 *
 * <p>
 * @author    realMethods, Inc.
 */
public interface IMQConnectionImpl extends IConnectionImpl
{   
    /**
     * Send an IFrameworkMQMessage, supplied by the IFrameworkMQAdapater implementor, 
     * onto the corresponding IFrameworkMQQueue.
     * <p>
     * @param       adapter
     * @exception   FrameworkMQBaseException.
     */
    public void send( IFrameworkMQAdapter adapter ) 
    throws FrameworkMQBaseException;

    /**
     * Receive a message from the queue into the specifed adapter, and wait
     * for an unlimited amount of time.
     * <p>
     * @return  IFrameworkMQAdapter
     * @exception FrameworkMQBaseException.
     */
    public IFrameworkMQAdapter receive() 
    throws FrameworkMQBaseException;

    /**
     * Receive a message from the queue into a new FrameworkMQAdapter, and wait
     * for the specified amount of time.  0 implies wait forever
     * <p>
     * @return  IFrameworkMQAdapter
     * @exception FrameworkMQBaseException.
     */
    public IFrameworkMQAdapter receive(int wait_interval_in_seconds ) 
    throws FrameworkMQBaseException;

    /**
     * Calls send and then receive asynchronously
     * <p>
     * @param       adapter
     * @return      IFrameworkMQAdapter     
     * @exception   FrameworkMQBaseException
     */
    public IFrameworkMQAdapter sendReceive( IFrameworkMQAdapter adapter ) 
    throws FrameworkMQBaseException;    

    /**
     * Indicates if internally, this MQConnectionImpl is still acknowledged.
     * If false, it is no longer considered, in favor of the associated 
     * back up connection.
     * <p>
     * @return      boolean
     */
    public boolean isPrimaryInUse();
    
    /**
     * Returns the MQQueueManager in use.
     * <p>
     * @return      MQQueueManager 
     * @exception   FrameworkMQBaseException
     */
    public MQQueueManager getQueueManager()
    throws FrameworkMQBaseException;

    /**
     * determines the queue name
     *
     * @return      String
     */
    public String getQueueName();

    /**
     * returns the name of the Queue manager
     *
     * @return       String      
     */
    public String getQueueManagerName();

    /**
     * returns the environment name this connection exists within
     *
     * @return       String      
     */
    public String getEnvironment();    

    /**
     * returns the host name
     *
     * @return      String 
     */
    public String getHostName();
    
    /**
     * returns the channel
     *
     * @return      String 
     */
    public String getChannel();

    /**
     * returns the port
     *
     * @return      int
     */
    public int getPort();    

    /**
     * returns the name of the backup connection MQ name
     *
     * @return       String      
     */
    public String getBackupMQPoolName();
    
    /**
     * assigns the correlationID to use
     *
     * @param       correlationID
     */
    public void setCorrelationID( byte[] correlationID );    
}


/*
 * Change Log:
 * $Log: IMQConnectionImpl.java,v $
 */
