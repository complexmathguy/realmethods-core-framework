/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.test.junit.etc; 

// **********************
// Imports
// **********************

import java.util.ArrayList;
import java.util.Collection;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.exception.FrameworkSecurityManagerException;

import com.framework.integration.security.IAuthenticateUser;
import com.framework.integration.security.ISecurityRoleLoader;
import com.framework.integration.security.ISecurityUser;
import com.framework.integration.security.ISecurityUserLoader;
import com.framework.integration.security.SecurityRole;
import com.framework.integration.security.SecurityUser;

/**
 * Security implementation for JUnit testing
 * <p> 
 * @author    realMethods, Inc.
 */
public class TestSecurityAdapter extends FrameworkBaseObject
    implements IAuthenticateUser, ISecurityRoleLoader, ISecurityUserLoader
{
//************************************************************************
// Public Methods
//************************************************************************
    /**
     * Default Constructor
     */
    public TestSecurityAdapter()
    {
        super();
    }

    /** 
     * Authenticates the user.
     * @param userIDIn String
     * @param passwordIn String
     * @param appIDIn String
     * @return boolean true if the user is authenticated and false otherwise.
     * @exception FrameworkSecurityManagerException Thrown when any error occurs.
     */
    public boolean authenticateUser(String userIDIn, 
                                    String passwordIn, 
                                    String appIDIn)
        throws FrameworkSecurityManagerException
    {
         
        return true;   
    }

    /** 
     * Loads the SecurityRoles for the associated SecurityUser and application id.
     * @param userIn
     * @param appIDIn
     * @return Collection of ISecurityRoles.  If there are no security roles then
     * the it will be empty.  
     * @exception IllegalArgumentException Thrown if userIn is null.
     * @exception FrameworkSecurityManagerException Thrown when any other error occurs.
     */
    public Collection loadSecurityRolesForUser(ISecurityUser userIn, 
                                            String appIDIn) 
        throws IllegalArgumentException, FrameworkSecurityManagerException
    {

        ArrayList returnList = new ArrayList();
            
        returnList.add( new SecurityRole("Employee") );
		returnList.add( new SecurityRole("Manager") );
		returnList.add( new SecurityRole("Director") );

        return returnList;
    }

    /** 
     * Creates a SecurityUser from the userID and appID.
     * @param userIDIn 
     * @param appIDIn 
     * @return ISecurityUser 
     * @exception IllegalArgumentException Thrown if userIDIn is null.
     * @exception FrameworkSecurityManagerException Thrown if anyother errors occur.
     */
    public ISecurityUser retrieveSecurityUser(String userIDIn,
                                                String appIDIn)
        throws IllegalArgumentException, FrameworkSecurityManagerException
    {
        ISecurityUser returnUser = null;
        
        returnUser = new SecurityUser(userIDIn);
        
        return returnUser;
    }

//************************************************************************
// Protected / Private Methods
//************************************************************************

//************************************************************************
// Attributes
//************************************************************************
}

/*
 * Change Log:
 * $Log: $
 */


