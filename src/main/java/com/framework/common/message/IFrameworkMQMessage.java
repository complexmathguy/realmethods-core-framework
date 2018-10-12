/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.message;

//************************************
// Imports
//************************************
import com.ibm.mq.MQMessage;

/**
 * Extends the notion of a FrameworkMessage in relation to an MQ message.
 * <p>
 * @author    realMethods, Inc.
 */
public interface IFrameworkMQMessage extends IFrameworkMessage
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * notify to format the message on a send action
     */
    public void formatOnSend();

    /**
     * notify to format the message on a receive action
     */
    public void formatOnReceive();

    /**
     * returns the contained MQMessage
     *
     * @return      MQMessage
     */
    public MQMessage getMQMessage();

    /**
     * returns the standard token delimiter
     *
     * @return      String
     */
    public String getStandardDelimiter();

    /**
     * apply the correlation ID
     *
     * @param       correlationID		value to apply to a send/receive handshake scenario
     */    
    public void setCorrelationID( byte[] correlationID );
    
}    

/*
 * Change Log:
 * $Log: IFrameworkMQMessage.java,v $
 */
