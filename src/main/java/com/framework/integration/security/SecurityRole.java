/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.security;

//*********************************
// Imports
//*********************************
import java.security.acl.Permission;

import com.framework.common.FrameworkBaseObject;

/**
 * Encapsultes the java.security.acl.Permission implementation 
 * <p>
 * @author    realMethods, Inc.
 */
public class SecurityRole 
    extends FrameworkBaseObject implements ISecurityRole
{
//***************************************************   
// Public Methods
//***************************************************
	/**
	 * Constructor
	 * 
	 * @param permission	representation of the role.
	 */	 
	public SecurityRole( String permission )
	{
	    super();
	    
		// create the PermissionImpl delegate object
		permissionName = permission;
	}

    /**
     * Retrieves the Security Role's Permission Name.
     * @return String
     */
    public String getPermissionName()
    {
        return permissionName;
    }
    
	/**
	* Checks two Permission objects for equality.
	* Do not use the equals method for making access control decisions; use the implies method.
	* @param    objectIn
	* @return   boolean
	*/
	public boolean equals( Object objectIn )
	{
	    boolean bReturn = false;
	    
	    if (objectIn instanceof Permission)
	    {
	        Permission permissionIn = (Permission)objectIn;
	        bReturn = permissionName.equals(permissionIn.toString());
	    }
	    
		return bReturn;
	}

    /**
     * Returns a string describing this Permission.
     * @return String
     */
	public String toString()
	{
		return permissionName;
	}
//***************************************************   
// Protected/Private Methods
//***************************************************

//***************************************************   
// Attributes
//***************************************************
    /**
     * Security Role' Permission's Name.
     */
    private String permissionName = null;
}

/*
 * Change Log:
 * $Log: SecurityRole.java,v $
 */
