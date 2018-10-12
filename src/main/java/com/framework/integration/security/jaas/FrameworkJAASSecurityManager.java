/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.security.jaas;

import java.security.Principal;

import java.security.acl.Group;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.security.auth.login.LoginContext;

import com.framework.common.exception.FrameworkSecurityManagerException;
import com.framework.common.exception.InitializationException;

import com.framework.integration.security.FrameworkSecurityManager;
import com.framework.integration.security.IAuthenticateUser;
import com.framework.integration.security.ISecurityRole;
import com.framework.integration.security.ISecurityRoleLoader;
import com.framework.integration.security.ISecurityUser;
import com.framework.integration.security.ISecurityUserLoader;
import com.framework.integration.security.SecurityUser;

/**
 * 
 * Java JAAS-based Framework security manager
 * <p>
 * @author    realMethods, Inc.
 */
public class FrameworkJAASSecurityManager extends FrameworkSecurityManager
{
//***************************************************   
// Public Methods
//***************************************************
    /**
     * Constructor -  required for Bean-based instantiation
     */
	public FrameworkJAASSecurityManager()                    
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
		loginModuleWrapper = new FrameworkJAASLoginModuleWrapper( props );
		      	
		super.initialize( name, props );     	
    }    
    
    /**
     * Retrieves whether the user has the specified permission.  Does so by
     * using the correlated LoginContext.getSubject().getPublicCredentials() 
     * for the provided ISecurityUser.
     * <p>
     * @param 	user		security user abstraction
     * @param	role		security role abstraction
     * @return 	boolean 	True if the user has authorization.
     */
    public boolean isUserAuthorized( ISecurityUser user, ISecurityRole role ) 
    {    	
		boolean b = false;
		
		if ( user == null )
			throw new IllegalArgumentException( "FrameworkJAASSecurityManager.isUserAuthorized(...) - ISecurityUser arg cannot be null." );
					
		LoginContext loginContext = (LoginContext)loginModuleWrapper.getLoginContexts().get( user.getUserID() );
		
		if  ( loginContext != null )		
		{
			Iterator principals = loginContext.getSubject().getPrincipals().iterator();
			Principal principal = null;
			
			while( principals.hasNext() && b == false )
			{
				principal = (Principal)principals.next();
				
				if ( principal instanceof Group )		
				{					
				    b = ((Group)principal).isMember( new SecurityUser( role.getPermissionName() ) );
				}
				else
					b = role.getPermissionName().equalsIgnoreCase( principal.toString() );
			} 
		}
		else
		{			
			logWarnMessage( "FrameworkJAASSecurityManager.unAuthenticateUser(...) - failed to check if user " + user.getUserID() + " is authorized for role " + role.getPermissionName() + " since no associated LoginContext could be found." );			
		}
		
		// delegate to the base class for notification of authorization event
		notifyUserAuthorized( b, user, role );
		
		return( b );
    }
    
	/**
	 * Notification from client that the supplied user is no longer to be deemed
	 * authenticated. Returns true if unathentication was successful
	 * <p>
	 * @param 	user		security user abstraction
	 * @return	boolean		success indicator
	 */        
	public boolean unAuthenticateUser( ISecurityUser user )
	{
		boolean b = false;
		
		if ( user == null )
			throw new IllegalArgumentException( "FrameworkJAASSecurityManager.unAuthenticateUser(...) - ISecurityUser arg cannot be null." );
			
		LoginContext loginContext = (LoginContext)loginModuleWrapper.getLoginContexts().get( user.getUserID() );
		
		if  ( loginContext != null )
		{
			try
			{
				loginContext.logout();
				loginModuleWrapper.getLoginContexts().remove( user.getUserID() );
				logInfoMessage( "FrameworkJAASSecurityManager.unAuthenticateUser(...) - User " +  user.getUserID() + " is now unauthenticated from security manager - " + getName() );				
				
				b = true;
			}
			catch( Throwable exc )
			{
				logErrorMessage( "FrameworkJAASSecurityManager.unAuthenticateUser(...) - failed to unauthenticate user " + user.getUserID() + " - " + exc );
			}
		}
		else
		{			
			logWarnMessage( "FrameworkJAASSecurityManager.unAuthenticateUser(...) - failed to unauthenticate user " + user.getUserID() + " since no associated LoginContext could be found." );			
		}
		
		return(  b );
			
	}
	 
	/**
	 * Notification to unauthenticate all users.
	 */        
	public void unAuthenticateUsers()
	{
		Map loginContexts 			= loginModuleWrapper.getLoginContexts();
		Iterator keys 				= loginContexts.keySet().iterator();
		LoginContext loginContext	= null; 
		String userid				= null;
							
		while( keys.hasNext() )
		{
			userid 			= (String)keys.next();
			loginContext 	= (LoginContext)loginContexts.get( userid );
						
			try
			{
				loginContext.logout();
				logInfoMessage( "User " +  userid + " is now unauthenticated from security manager - " + getName() );										
			}
			catch( Throwable exc )
			{
				logErrorMessage( "FrameworkJAASSecurityManager.unAuthenticateUser(...) - failed to unauthenticate user " + userid + " - " + exc );
			}
		}		
	}	 
	
//***************************************************   
// Private/Protected Methods
//***************************************************

    /**
     * Helper to create the implementation and security
     * specific IAuthenticateUser
     * <p>
     * @return		IAuthenticateUser
     * @exception	InitializationException
     */
	protected IAuthenticateUser createAuthenticator()
	throws InitializationException
	{
		return( loginModuleWrapper );
	}
    
    /**
     * Helper to create the implementation and security
     * specific ISecurityRoleLoader
     * <p>
     * @return		ISecurityRoleLoader
     * @exception	InitializationException
     */    
	protected ISecurityRoleLoader createSecurityRoleLoader()
	throws InitializationException
	{
		return( loginModuleWrapper );
	}
	
    /**
     * Helper to create the implementation and security
     * specific ISecurityUserLoader
     * <p>
     * @return		ISecurityUserLoader
     * @exception	InitializationException
     */	
	protected ISecurityUserLoader createSecurityUserLoader()
	throws InitializationException
	{
		return( loginModuleWrapper );		
	}

 	/**
	 * Apply the secured user and its loaded roles by simply storing the roles as is
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
            throw new IllegalArgumentException("FrameworkJAASSecurityManager:applyUserAndRoles(..) - user cannot be null.");   
        }
        if (roles == null)
        {
            throw new IllegalArgumentException("FrameworkJAASSecurityManager:applyUserAndRoles(..) - roles be null.");   
        }

		// no-op, since the logincontext's subject's principals is what we are after
    }
    

//***************************************************   
// Attributes
//***************************************************

	/**
	 * wrapper to the JAAS Login Module in use by the LoginContext
	 */
	private FrameworkJAASLoginModuleWrapper loginModuleWrapper = null;
	
}

/*
 * Change Log:
 * $Log: FrameworkJAASSecurityManager.java,v $
 *
 */
