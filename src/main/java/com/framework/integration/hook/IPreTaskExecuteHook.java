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
 * Used for the purpose of identifying an IPreTaskExecuteHook
 * <p>
 * @author    realMethods, Inc.
 */
public interface IPreTaskExecuteHook extends IFrameworkHook
{
    /**
     * query method used to determine if the hook can handle hooking
     * based on the provided command and the message to work over
     * <p>
     * @param       task	calling task
     * @param       msg		work provided to the task
     */
    public boolean canHandle( IFrameworkTask task, IFrameworkMessage msg );
    
    /**
     * the purpose of the hook
     * <p>
     * @param       msg		work provided to the task
     * @param       bHandleCommit
     * @exception   HookProcessException     
     */
    public void preTaskExecute( IFrameworkMessage msg, boolean bHandleCommit )
    throws HookProcessException;
}

/*
 * Change Log:
 * $Log: IPreTaskExecuteHook.java,v $
 */
