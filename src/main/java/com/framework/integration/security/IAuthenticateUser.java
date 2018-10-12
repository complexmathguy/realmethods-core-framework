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
 * This interface is the callback mechanism from
 * the Framework Security Manager when a user needs to authenticated.
 * <p>
 * @author    realMethods, Inc.
 */
public interface IAuthenticateUser
{
    /** 
     * Authenticates the user.
     * @param	userID		user identifier
     * @param 	password	user's password
     * @param 	appID		ignored, but may be used for additional security context infor
     * @return 				boolean true if the user is authenticated and false otherwise.
     * @exception 			FrameworkSecurityManagerException 
     */
    public boolean authenticateUser( String userID, String password, String appID)
        throws FrameworkSecurityManagerException;
               
}

/*
 * Change Log:
 * $Log: IAuthenticateUser.java,v $
 */
