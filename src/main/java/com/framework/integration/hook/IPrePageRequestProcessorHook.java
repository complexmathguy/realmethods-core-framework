/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.hook;

import javax.servlet.http.HttpServletRequest;

import com.framework.common.exception.HookProcessException;

/** 
 * Used for the purpose of identifying a hook for pre-page request processing
 * <p>
 * @author    realMethods, Inc.
 */
public interface IPrePageRequestProcessorHook extends IFrameworkHook
{    
    /**
     * the purpose of the hook
     * 
     * @param       action			current action being processed
     * @param       request
     * @exception   HookProcessException
     */
    public void process( String action, HttpServletRequest request )
    throws HookProcessException;
}

/*
 * Change Log:
 * $Log: IPrePageRequestProcessorHook.java,v $
 */
