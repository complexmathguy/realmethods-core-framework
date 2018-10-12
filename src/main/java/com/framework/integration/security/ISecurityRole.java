/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.security;

//**************************
// Imports
//**************************
import java.security.acl.Permission;

/** 
 * A class that implements this interface should provide 
 * access more detailed information about the Permission. 
 * <p>
 * @author    realMethods, Inc.
 */
public interface ISecurityRole extends Permission
{
    /**
     * Retrieves the Security Role's Permission Name.
     * @return String
     */
    public String getPermissionName();

}

/*
 * Change Log:
 * $Log: ISecurityRole.java,v $
 */
