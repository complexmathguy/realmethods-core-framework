/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.security;

//*****************************
// Imports
//*****************************
import com.framework.common.exception.FrameworkSecurityManagerException;

/** 
 * This interface is the call back mechanism by the Framework
 * SecurityManager to load the a SecurityUser for specified userID and app.
 *
 * <p>
 * @author    realMethods, Inc.
 */
public interface ISecurityUserLoader
{
    /** 
     * Creates a SecurityUser from the userID and appID.
     * <p>
     * @param		userID		user identifier
     * @param 		appID		ignored, but may later provide additional security context info.
     * @return 		user security abstraction 
     * @exception IllegalArgumentException Thrown if userID is null.
     * @exception FrameworkSecurityManagerException
     */
    public ISecurityUser retrieveSecurityUser(String userID, String appID)
    throws IllegalArgumentException, FrameworkSecurityManagerException;
}

/*
 * Change Log:
 * $Log: ISecurityUserLoader.java,v $
 */
