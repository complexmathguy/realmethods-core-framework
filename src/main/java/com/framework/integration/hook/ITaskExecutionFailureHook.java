/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.hook;

import com.framework.common.exception.HookProcessException;

import com.framework.common.message.IFrameworkMessage;

/** 
 * Used for the purpose of identifying an ITaskExecutionFailureHook
 * <p>
 * @author    realMethods, Inc.
 */
public interface ITaskExecutionFailureHook extends IFrameworkHook
{    
    /**
     * the purpose of the hook
     * 
     * @param       msg				task input
     * @param       handleCommit	
     * @exception   HookProcessException     
     */
    public void taskExecutionFailure( IFrameworkMessage msg, boolean handleCommit )
    throws HookProcessException;
}

/*
 * Change Log:
 * $Log: ITaskExecutionFailureHook.java,v $
 */
