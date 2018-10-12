/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.hook;

import javax.servlet.http.HttpServletRequest;

import com.framework.common.exception.HookProcessException;

/** 
 * Used for the purpose of hooking the end of HttpServletRequest processing
 * within the HTTPRequestHandler
 * <p>
 * @author    realMethods, Inc.
 */
public interface IPostHttpServletRequestProcessorHook extends IFrameworkHook
{    
    /**
     * the purpose of the hook
     * <p>
     * @param       request
     * @exception   HookProcessException
     */
    public void process( HttpServletRequest request )
    throws HookProcessException;
}

/*
 * Change Log:
 * $Log: IPostHttpServletRequestProcessorHook.java,v $
 */
