/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.log;

import com.framework.common.event.FrameworkLogEventType;

import com.framework.common.message.IFrameworkMessage;

/**
 * Base interface for a Framework Log message
 * <p>
 * @author    realMethods, Inc.
 */
public interface IFrameworkLogMessage extends IFrameworkMessage
{
    /**
     * Returns the bound FrameworkLogHandler 
     * @return      IFrameworkLogHandler
     */
    public IFrameworkLogHandler getFrameworkLogHandler();
    
    /**
     * Returns the bound FrameworkLogEventType
     */
    public FrameworkLogEventType getFrameworkLogEventType();         
    
    /**
     * Returns the associated message 
     * in a standard format using the log event type, log name, and message.
     * @return      String
     */
    public String getLogFormattedMessage();    
}

/*
 * Change Log:
 * $Log: IFrameworkLogMessage.java,v $
 */
