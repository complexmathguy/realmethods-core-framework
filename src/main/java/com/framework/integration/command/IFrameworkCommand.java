/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.command;

import com.framework.common.exception.CommandExecutionException;

import com.framework.common.message.IFrameworkMessage;

import com.framework.integration.task.IFrameworkTask;

/**
 * Common interface for all Framework related Commands 
 * of the JMS Command/Task Architecture.
 * <p>
 * @author    realMethods, Inc.
 */
public interface IFrameworkCommand extends java.io.Serializable
{
    /**
     * returns the associated IFrameworkMessage
     * @return      IFrameworkMessage
     */
    public IFrameworkMessage getMessage();

    /**
     * returns the bound IFrameworkTask
     * @return		IFrameworkTask
     */
    public IFrameworkTask getBoundTask();

    /**
     * Notification to the Commmand to do it's work
     * with respect to the input msg.
     *
     * @param     	msg			external data context to work within
     * @param     	task  		can optionally be null
     * @exception   CommandExecutionException
     * @exception   IllegalArgumentException
     *
     */
    public void execute( IFrameworkMessage msg, IFrameworkTask task )
    throws CommandExecutionException, IllegalArgumentException;

}

/*
 * Change Log:
 * $Log: IFrameworkCommand.java,v $
 */
