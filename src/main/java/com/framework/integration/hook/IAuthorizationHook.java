/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
*************************************************************************/

package com.framework.integration.hook;

import com.framework.common.exception.HookProcessException;

import com.framework.integration.security.ISecurityRole;
import com.framework.integration.security.ISecurityUser;
import com.framework.integration.security.IFrameworkSecurityManager;

/** 
 * Used as a means to handle authorization success and failure
 * events.
 * 
 * <p>
 * @author    realMethods, Inc.
 */
public interface IAuthorizationHook extends IFrameworkHook
{    
    /**
     * Authorization passed.
     * 
	 * @param       user		user security identity
	 * @param       role		security role to authorize against
     * @param		secMgr		security manager interface
     * @exception   HookProcessException
     */
    public void authorizationSuccess( ISecurityUser user, ISecurityRole role, IFrameworkSecurityManager secMgr )
    throws HookProcessException;
    
	/**
	 * Authorization failed.
	 * 
	 * @param       user		user security identity
	 * @param       role		security role to authorize against
	 * @param		secMgr		security manager interface
	 * @exception   HookProcessException
	 */
	public void authorizationFailure( ISecurityUser user, ISecurityRole role, IFrameworkSecurityManager secMgr )
	throws HookProcessException;    
}

/*
 * Change Log:
 * $Log: IAuthorizationHook.java,v $
 */
