/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.hook;

import com.framework.common.exception.HookProcessException;

import com.framework.common.message.IFrameworkMessage;

import com.framework.integration.command.IFrameworkCommand;

import com.framework.integration.task.IFrameworkTask;

/**
 * Used for the purpose of identifying an IPostCommandExecuteHook
 * <p>
 * @author    realMethods, Inc.
 */
public interface IPostCommandExecuteHook extends IFrameworkHook
{
    /**
     * query method used to determine if the hook can handle hooking
     * based on the provided command and the message to work over
     * <p>
     * @param       command
     * @param       msg
     */
    public boolean canHandle( IFrameworkCommand command, IFrameworkMessage msg );
    
    /**
     * the purpose of the hook
     * 
     * @param       msg		input work provided to the current command
     * @param       task	context of execution
     * @exception   HookProcessException
     */
    public void postCommandExecute( IFrameworkMessage msg, IFrameworkTask task )
    throws HookProcessException;
}

/*
 * Change Log:
 * $Log: IPostCommandExecuteHook.java,v $
 */
