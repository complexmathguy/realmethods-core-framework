/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.security;

import java.beans.Beans;

import java.io.IOException;

import java.security.Principal;

import java.security.acl.Acl;
import java.security.acl.AclEntry;
import java.security.acl.NotOwnerException;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import com.framework.common.exception.FrameworkSecurityManagerException;
import com.framework.common.exception.InitializationException;

import sun.security.acl.AclEntryImpl;
import sun.security.acl.AclImpl;
import sun.security.acl.PrincipalImpl;

/**
 * Java ACL-based Framework security manager
 * <p>
 * @author    realMethods, Inc.
 */
public class FrameworkACLSecurityManager extends FrameworkSecurityManager
{
//***************************************************   
// Public Methods
//***************************************************
    /**
     * Constructor -  required for Bean-based instantiation
     */
	public FrameworkACLSecurityManager()                    
    {        
    }
    
    /**
     * External initializer, which loads the security related interfaces
     * required of an acl based Framework security manager
     * <p>
     * @param		name		sec. mgr. unique name
     * @param 		props		key/value pairings to configure by
     * @exception	InitializationException
     */
    public void initialize( String name, Map props )
    throws InitializationException
    {
		super.initialize( name, props );    
		
        //---------------------------------------------------------
        // Now create the acl owner and the acl itself
        //---------------------------------------------------------
        aclOwner = (Principal)new PrincipalImpl( appID );        
        aclList = new AclImpl(aclOwner, appID ); 		 	
    }   

    /**
     * Helper to create the implementation and security
     * specific IAuthenticateUser.
     * <p>
     * @return		IAuthenticateUser
     * @exception	InitializationException
     */
	protected IAuthenticateUser createAuthenticator()
	throws InitializationException
	{				         
        //---------------------------------------------------------
        // Retrieve the IAuthenticateUser interface
        //---------------------------------------------------------        
        //get properties - IAuthenticateUserClassName

       	String authenticateUserClassName	= (String)getProperties().get(AUTHENTICATE_USER_CLASS_NAME);        
        
        // If the property was not found then throw an ObjectCreationException
        if ( authenticateUserClassName == null || authenticateUserClassName.length() == 0 )
        {
            throw new InitializationException("FrameworkACLSecurityManager:createAuthenticator() - could not find authenticator in properties file.");   
        }

        // Now try to instantiate the class
        Object authenticateUserClassInstance 	= null;
        IAuthenticateUser authenticator			= null;        
        
        try
        {
            // Create the AuthenticateUser class
            authenticateUserClassInstance = Beans.instantiate(this.getClass().getClassLoader(), authenticateUserClassName);
            authenticator = (IAuthenticateUser)authenticateUserClassInstance;
        }
        catch ( IOException exc )
        {
            throw new InitializationException("FrameworkACLSecurityManager:createAuthenticator() - could not create the authenticator " + authenticateUserClassName + " due to an IO Error", exc);
        }
        catch ( ClassNotFoundException exc )
        {
            throw new InitializationException("FrameworkACLSecurityManager:createAuthenticator() - could not find the class associated with authenticator " + authenticateUserClassName, exc);   
        }
        catch( Throwable exc )
        {
            throw new InitializationException("FrameworkACLSecurityManager:createAuthenticator() creating the authenticator " + authenticateUserClassName + " failed - " + exc, exc);   
        }        
        
        return( authenticator );
    }
    
    /**
     * Helper to create the implementation and security
     * specific ISecurityRoleLoader.
     * <p>
     * @return		ISecurityRoleLoader
     * @exception	InitializationException
     */     
	protected ISecurityRoleLoader createSecurityRoleLoader()
	throws InitializationException
	{	        
        //---------------------------------------------------------
        // Need to retrieve the ISecurityRoleLoader interface
        //---------------------------------------------------------
        //get properties - ISecurityRoleLoaderClassName
        String securityRoleLoaderClassName 		= (String)getProperties().get(SECURITY_ROLE_LOADER_CLASS_NAME);                
        
        // If the property was not found then throw an InitializationException
        if ( securityRoleLoaderClassName == null || securityRoleLoaderClassName.length() == 0 )
        {
            throw new InitializationException("FrameworkACLSecurityManager:createSecurityRoleLoader() - could not find roleLoader in properties file.");   
        }
        
        // Now check to see if the SecurityRoleLoaderClassName is the same as the 
        // IAuthenticateUser class; If they are not the same then instantiate the new class
        // else use the already created class as the SecurityRoleLoader also
        Object securityRoleLoaderClassInstance	= null;
		ISecurityRoleLoader securityRoleLoader	= null;
		
        try
        {
            // Create the SecurityRoleLoader class
            securityRoleLoaderClassInstance = Beans.instantiate(this.getClass().getClassLoader(), securityRoleLoaderClassName);
            securityRoleLoader = (ISecurityRoleLoader)securityRoleLoaderClassInstance;
        }
        catch ( IOException exc )
        {
            throw new InitializationException("FrameworkACLSecurityManager:createSecurityRoleLoader() - could not create the roleLoader " + securityRoleLoaderClassName + "due to an IO Error - " + exc, exc);
        }
        catch ( ClassNotFoundException exc )
        {
            throw new InitializationException("FrameworkACLSecurityManager:createSecurityRoleLoader() - could not find the class associated with roleLoader " + securityRoleLoaderClassName, exc);   
        }
        catch( Throwable exc )
        {
            throw new InitializationException("FrameworkACLSecurityManager:createSecurityRoleLoader() creating the Security Role Loader - " + securityRoleLoaderClassName, exc);   
        }                               
        
        return( securityRoleLoader );
	}
	
	
    /**
     * Helper to create the implementation and security
     * specific ISecurityUserLoader.
     * <p>
     * @return		ISecurityUserLoader
     * @exception	InitializationException
     */	
	protected ISecurityUserLoader createSecurityUserLoader()
	throws InitializationException
	{
        //---------------------------------------------------------
        // Need to retrieve the ISecurityUserLoader interface
        //---------------------------------------------------------
        
        //get properties - ISecurityUserLoaderClassName
        String securityUserLoaderClassName 		= (String)getProperties().get(SECURITY_USER_LOADER_CLASS_NAME);        
        
        // If the property was not found then throw an InitializationException
        if ( securityUserLoaderClassName == null || securityUserLoaderClassName.length() == 0 )
        {
            throw new InitializationException("FrameworkACLSecurityManager:createSecurityRoleLoader() - could not find userLoader in properties file.");   
        }
        
        ISecurityUserLoader securityUserLoader = null;
        Object securityUserLoaderClassInstance = null;        

        try
        {
            // Create the SecurityUserLoader class
            securityUserLoaderClassInstance = Beans.instantiate(this.getClass().getClassLoader(), securityUserLoaderClassName);
            securityUserLoader = (ISecurityUserLoader)securityUserLoaderClassInstance;
        }
        catch ( IOException exc )
        {
            throw new InitializationException("FrameworkACLSecurityManager:createSecurityRoleLoader() - could not create the userLoader " + securityUserLoaderClassName + " due to an IO Error - " + exc, exc);
        }
        catch ( ClassNotFoundException exc )
        {
            throw new InitializationException("FrameworkACLSecurityManager:createSecurityRoleLoader() - could not find the class associated with userLoader " + securityUserLoaderClassName, exc);   
        }
        catch( Throwable exc )
        {
            throw new InitializationException("FrameworkACLSecurityManager:createSecurityRoleLoader() creating the SecurityUserLoader " + securityUserLoaderClassName, exc);   
        }
        
        return( securityUserLoader );
         	
    }    
    /**
     * Retrieves whether the user has the specified permission.
     * <p>
     * @param 	user	security user abstraction
     * @param 	role	to authorize the user against
     * @return boolean 	
     */
    public boolean isUserAuthorized( ISecurityUser user, ISecurityRole role ) 
    {    	
    	logInfoMessage( "FrameworkACLSecurityManager:isUserAuthorized() - checking authorization of " + user.getUserID() +  " for role " + role.getPermissionName() );
    	
		// Just use the java.security.acl.ACL class to handle this
		boolean b = aclList.checkPermission( user, role );
		
		// delegate to the base class for notification of authorization event
		notifyUserAuthorized( b, user, role );
			 
		return ( b );
    }

	/**
	 * Notification from client that the supplied user id is no longer to be deemed
	 * authenticated.
	 * <p>
	 * @param 	user	security user abstraction
	 * @return	boolean
	 */        
	public boolean unAuthenticateUser( ISecurityUser user )
	{
		// iterate through the ACL's entries looking for user as principal...
		Enumeration aclEntries 	= aclList.entries();
		AclEntry entry			= null;
		boolean b				= false;
		
		while( aclEntries.hasMoreElements() && b == false )
		{
			entry =  (AclEntry)aclEntries.nextElement();
			
			if ( entry.getPrincipal() == user )
			{
				try
				{
					b = aclList.removeEntry( aclOwner, entry );
					logInfoMessage( "User " +  user.getUserID() + " is now unauthenticated from security manager - " + getName() );										
				}				
				catch( Throwable exc )
				{
					logErrorMessage( "FrameworkACLSecurityManager.unAuthenticateUser(...) - failed for user " +  user.getUserID() + " - " + exc );
					return( false );
				}
			}
		}
		
		return( b );
		
	}    
      
	/**
	 * Notification to unauthenticate all users.
	 */        
	public void unAuthenticateUsers()
	{
		Enumeration aclEntries 	= aclList.entries();
		AclEntry entry			= null;
		
		while( aclEntries.hasMoreElements() )
		{
			entry =  (AclEntry)aclEntries.nextElement();
			
			try
			{
				aclList.removeEntry( aclOwner, entry );
				logInfoMessage( "-- Sec. Mgr. " + getName() + " - User " +  entry.getPrincipal() + " is now unauthenticated from security manager - " + getName() );										
			}				
			catch( Throwable exc )
			{
				logErrorMessage( "FrameworkACLSecurityManager.unAuthenticateUser(...) - failed for user " +  entry.getPrincipal() + " - " + exc );
			}
		}		
	}

//***************************************************   
// Private/Protected Methods
//***************************************************

	/**
	 * Subclass must apply the secured user and its loaded roles
	 * <p>
	 * @param		user
	 * @param		roles
	 * @exception	FrameworkSecurityManagerException
	 */
	protected void applyUserAndRoles( ISecurityUser user, Collection roles )
	throws FrameworkSecurityManagerException    
    {
        // Validate the parameters
        if (user == null)
        {
            throw new IllegalArgumentException("FrameworkACLSecurityManager:applyUserAndRoles(..) - user cannot be null.");   
        }
        if (roles == null)
        {
            throw new IllegalArgumentException("FrameworkACLSecurityManager:applyUserAndRoles(..) - roles be null.");   
        }

		// create a new Acl entry for this user
		AclEntry newAclEntry = new AclEntryImpl( user );

		// initialize some temporary variables
//		String securityRoleCode = null;
		ISecurityRole currentSecurityRole = null;

        logInfoMessage( "Creating ACL for " + user + " with permissions " + roles.toString() );
        
		// Cycle through the keys of the hashmap
		Iterator securityRolesIterator = roles.iterator();
		while ( securityRolesIterator.hasNext() )
		{
			// Get the key name from the enumeration
			currentSecurityRole = (ISecurityRole) securityRolesIterator.next();

			// add the permission to the aclEntry
			newAclEntry.addPermission( (java.security.acl.Permission)currentSecurityRole );
		}

        // Now add the ACLEntry to the Acl
		try
		{
			// add the aclEntry to the ACL for the aclOwner
			aclList.addEntry(aclOwner, newAclEntry);
		} 
		catch (NotOwnerException noE)
		{
			throw new FrameworkSecurityManagerException("FrameworkACLSecurityManager:applyUserAndRoles(..) - Could not add the AclEntry because : " + noE, noE);
		}
    }
    

//***************************************************   
// Attributes
//***************************************************

    /**
     * The Authenticate User Class Name Tag.
     */
    private final String AUTHENTICATE_USER_CLASS_NAME = "authenticator";

    /**
     * The Security Role Loader Class Name Tag.
     */
    private final String SECURITY_ROLE_LOADER_CLASS_NAME = "roleLoader";

    /**
     * The Security User Loader Class Name Tag.
     */
    private final String SECURITY_USER_LOADER_CLASS_NAME = "userLoader";
    
	/**
	 * Security Owner User ID.
	 */
	protected String securityOwnerUserID = null;
	/**
	 * AclIdentifier String.
	 */
	protected String aclIdentifier = null;
	/**
	 * Acl List.
	 */
	protected Acl aclList = null;
	
	/**
	 * Principal that is the Owner of the Acl.
	 */
	private Principal aclOwner = null;

   
}

/*
 * Change Log:
 * $Log: FrameworkACLSecurityManager.java,v $
 */
