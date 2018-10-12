/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.security;

//*****************************
// Imports
//*****************************

import java.util.Map;

import com.framework.common.exception.FrameworkSecurityManagerException;
import com.framework.common.exception.InitializationException;

/** 
 * Contains the necessary contract to manage well-defined security aspects within the Framework.
 * <p>
 * @author    realMethods, Inc.
 */
public interface IFrameworkSecurityManager 
{
	
    /**
     * External initializer, which loads the security related interfaces
     * required of an acl based Framework security manager
     * 
     * @param		name		unique name
     * @param 		props		key/value pairings to initialize by
     * @exception	InitializationException
     */
    public void initialize( String name, Map props )
    throws InitializationException;
    	
    /**
     * Authenticates user, loads user's Security Roles, and
     * creates the SecurityUser along with adding an entry to the AclEntry map.
     * <p.
     * @param		userID		user identifier
     * @param 		password	user's password
     * @param 		appID		ignored, but could be used for additional security context info
     * @return 					user security abstraction
     * @exception	FrameworkSecurityManagerException 
     */
    public ISecurityUser retrieveSecurityUser( String userID, String password, String appID ) 
    throws FrameworkSecurityManagerException;

    /**
     * Retrieves whether the user has the permission based upon
     * the passed in role.
     * <p>
     * @param	userid		user identifier
     * @param 	role		role or credential to compare
     * @return boolean
     */
    public boolean isUserAuthorized( String userid, String role );
    
    
    /**
     * Retrieves whether the user has the specified permission.
     * @param	user
     * @param	role
     * @return boolean True if the user has authorization.
     */
    public boolean isUserAuthorized( ISecurityUser user, ISecurityRole role );
    
	/**
	 * Notification that the supplied user id is no longer to be deemed
	 * authenticated. Returns true if unathentication was successful.
	 * <p>
	 * @param 	user
	 * @return	boolean
	 */        
	public boolean unAuthenticateUser( ISecurityUser user );    
	
	/**
	 * Notification to unauthenticate all users.
	 */        
	public void unAuthenticateUsers();  
    /**
     * Creates a Security Role with the specified permissionName.
     * <p>>
     * @param   	permissionName
     * @return  	ISecurityRole
     * @exception 	IllegalArgumentException Thrown if permissionName is null
     */
    public ISecurityRole getSecurityRole( String permissionName );
     
	/**
	 * Returns the IAuthenticateUser implementation in use
	 * <p>
	 * @return IAuthenticateUser
	 */           
	public IAuthenticateUser getAuthenticator();
	
	/**
	 * Returns the ISecurityRoleLoader implementation in use
	 * <p>
	 * @return ISecurityRoleLoader
	 */
	public ISecurityRoleLoader getRoleLoader();
	
	/**
	 * Returns the ISecurityUserLoader implementation in use
	 * <p>
	 * @return ISecurityUserLoader
	 */
	public ISecurityUserLoader getUserLoader();     
    
	/**
	 * Returns the name of the security manager
	 * <p>
	 * @return String
	 */
	public String getName();
	
    /**
     * Returns an indicator as to if this instance is the default security managers.
     * <p>
     * @return boolean
     */
    public boolean isDefaultSecurityManager();  
    
    /**
     * Assigns an indicator as to if this instance is the default security managers.
     * <p>
     * @param 	is
     */
    public void isDefaultSecurityManager( boolean is );      
    
}

/*
 * Change Log:
 * $Log: IFrameworkSecurityManager.java,v $
 */
