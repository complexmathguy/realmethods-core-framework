/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.security;

//*****************************
// Imports
//*****************************
import java.util.Collection;

import com.framework.common.exception.FrameworkSecurityManagerException;

/** 
 * This interface is the call back mechanism by the Framework
 * SecurityManager to load the SecurityRoles for a specified user and app.
 * <p>
 * @author    realMethods, Inc.
 */
public interface ISecurityRoleLoader
{
    /** 
     * Loads the SecurityRoles for the associated SecurityUser and application id.
     * 
     * @param	user	security user abstraction
     * @param 	appID	ignored, but could provide for additional security context info
     * @return 			Collection of SecurityRoles.  If there are no security roles then
     * 					the map will be empty.  
     * @exception IllegalArgumentException 			
     * @exception FrameworkSecurityManagerException 	
     */
    public Collection loadSecurityRolesForUser( ISecurityUser user, String appID ) 
    throws IllegalArgumentException, FrameworkSecurityManagerException;
}

/*
 * Change Log:
 * $Log: ISecurityRoleLoader.java,v $
 */
