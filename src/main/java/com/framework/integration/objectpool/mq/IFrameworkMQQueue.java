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

import com.framework.common.message.IFrameworkMQMessage;

import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;

/**
 * This interface acts a Framework require facade to an IBM MQQueue.
 *
 * <p>
 * @author    realMethods, Inc.
 */
public interface IFrameworkMQQueue
{
    /**
     * Places the message onto the bound queue.
     * <p>
     * @param       msg
     * @exception   FrameworkMQBaseException
     */
    public void put( IFrameworkMQMessage msg )
    throws FrameworkMQBaseException;

    /**
     * Returns the message as a String from the bound queue.
     * <p>
     * @param       msg
     * @return      String
     * @exception   FrameworkMQBaseException
     */
    public String get( IFrameworkMQMessage msg )
    throws FrameworkMQBaseException;

    /**
     * Returns the contained IBM MQ Queue.
     * @return      com.ibm.mqbind.MQQueue 
     */
    public MQQueue getTheIBMMQQueue();

// accessor methods

    /**
     * Returns the get related MQ message options.
     * <p>
     * @return      MQGetMessageOptions
     */
    public MQGetMessageOptions getGetMessageOptions();

    /**
     * Returns the put related MQ message options.
     * <p>
     * @return      MQGetMessageOptions
     */
    public MQPutMessageOptions getPutMessageOptions();
   
}

/*
 * Change Log:
 * $Log: IFrameworkMQQueue.java,v $
 */
