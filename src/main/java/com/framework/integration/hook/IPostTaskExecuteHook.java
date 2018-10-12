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
 * IPostTaskExecuteHook interface
 *
 * Used for the purpose of identifying an IPostTaskExecuteHook
 * 
 * <p>
 * @author    realMethods, Inc.
 */
public interface IPostTaskExecuteHook extends IFrameworkHook
{
    /**
     * query method used to determine if the hook can handle hooking
     * based on the provided command and the message to work over
     * <p>
     * @param       task
     * @param       msg
     */
    public boolean canHandle( IFrameworkTask task, IFrameworkMessage msg );
    
    /**
     * the purpose of the hook
     * <p>
     * @param       msg
     * @param       handleCommit
     * @exception   HookProcessException     
     */
    public void postTaskExecute( IFrameworkMessage msg, boolean handleCommit )
    throws HookProcessException;
}

/*
 * Change Log:
 * $Log: IPostTaskExecuteHook.java,v $
 */
