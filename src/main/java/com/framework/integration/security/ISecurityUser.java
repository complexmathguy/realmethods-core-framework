/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.security;

//*****************************
// Imports
//*****************************
import java.security.Principal;

/** 
 * This interface extends java.security.Principal and should provide more
 * details of a given user.  
 * <p>
 * @author    realMethods, Inc.
 */
public interface ISecurityUser extends Principal
{
    /** 
     * Retrieves the UserID.
     * @return String
     */
    public String getUserID();
}

/*
 * Change Log:
 * $Log: ISecurityUser.java,v $
 */
