/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
*************************************************************************/

package com.framework.integration.hook;

import com.framework.common.exception.HookProcessException;

import com.framework.integration.security.IFrameworkSecurityManager;

/** 
 * Used as a means to handle authentication success and failure
 * events.
 * 
 * <p>
 * @author    realMethods, Inc.
 */
public interface IAuthenticationHook extends IFrameworkHook
{    
    /**
     * Authentication passed.
     * 
     * @param       userID		user identifier
     * @param       password	user's password
     * @param		secMgr		security manager interface
     * @exception   HookProcessException
     */
    public void authenticationSuccess( String userID, String password, IFrameworkSecurityManager secMgr )
    throws HookProcessException;
    
	/**
	 * Authentication failed.
	 * 
	 * @param       userID		user identifier
	 * @param       password	user's password
	 * @param		secMgr		security manager interface
	 * @exception   HookProcessException
	 */
	public void authenticationFailure( String userID, String password, IFrameworkSecurityManager secMgr )
	throws HookProcessException;    
}

/*
 * Change Log:
 * $Log: IAuthenticationHook.java,v $
 */
