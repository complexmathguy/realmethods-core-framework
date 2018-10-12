/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.hook;

import javax.servlet.http.HttpServletRequest;

import com.framework.common.exception.HookProcessException;

/** 
 * IPageRequestProcessorErrorHook interface
 *
 * Used for the purpose of identifying an page request processing error
 * 
 * <p>
 * @author    realMethods, Inc.
 */
public interface IPageRequestProcessorErrorHook extends IFrameworkHook
{    
    /**
     * the purpose of the hook
     * 
     * @param       action		action name currently being processed				
     * @param       request
     * @exception   HookProcessException
     */
    public void process( String action, HttpServletRequest request )
    throws HookProcessException;
}

/*
 * Change Log:
 * $Log: IPageRequestProcessorErrorHook.java,v $
 */
