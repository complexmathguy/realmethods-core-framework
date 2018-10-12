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
 * @author      realMethods, Inc.
 * @version     1.0
 */
public interface IPreHttpServletRequestProcessorHook extends IFrameworkHook
{    
    /**
     * the purpose of the hook
     * @param       request
     * @exception   HookProcessException
     */
    public void process( HttpServletRequest request )
    throws HookProcessException;
}

/*
 * Change Log:
 * $Log: IPreHttpServletRequestProcessorHook.java,v $
 */
