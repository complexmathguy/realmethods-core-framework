/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.hook;

import com.framework.common.exception.HookProcessException;

import com.framework.common.message.IFrameworkMessage;

import com.framework.integration.task.IFrameworkTask;

/** 
 * Used for the purpose of applying unique identify to an ICommandExecutionFailureHook
 * <p>
 * @author    realMethods, Inc.
 */
public interface ICommandExecutionFailureHook extends IFrameworkHook
{
    /**
     * the purpose of the hook
     * 
     * @param       msg
     * @param       task
     * @exception   HookProcessException     
     */
    public void commandExecutionFailure( IFrameworkMessage msg, IFrameworkTask task )
    throws HookProcessException;
}

/*
 * Change Log:
 * $Log: ICommandExecutionFailureHook.java,v $
 */
