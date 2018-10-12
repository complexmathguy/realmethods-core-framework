/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.hook;

import javax.servlet.http.HttpSessionBindingEvent;

import com.framework.common.exception.HookProcessException;

import javax.servlet.http.*;

/** 
 * Used as a means of post processing of a recently bound
 * user session.
 * 
 * <p>
 * @author    realMethods, Inc.
 */
public interface IUserSessionBoundHook extends IFrameworkHook
{    
    /**
     * The purpose of the hook.  Provides the HttpSessionBindingEvent
     * passed along by the HttpSession just after unbinding.
     * 
     * @param       session		the current HttpSession context
     * @param       event		HttpSession binding event
     * @exception   HookProcessException
     */
    public void process( HttpSession session, HttpSessionBindingEvent event )
    throws HookProcessException;
}

/*
 * Change Log:
 * $Log: IUserSessionBoundHook.java,v $
 */
