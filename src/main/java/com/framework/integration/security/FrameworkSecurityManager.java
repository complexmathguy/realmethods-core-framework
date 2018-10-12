/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.security;

//**********************************
// Imports
//**********************************
import java.util.Collection;
import java.util.Map;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.event.FrameworkEventType;
import com.framework.common.exception.FrameworkSecurityManagerException;
import com.framework.common.exception.HookAccessException;
import com.framework.common.exception.HookProcessException;
import com.framework.common.exception.InitializationException;

import com.framework.common.misc.Utility;

import com.framework.common.namespace.FrameworkNameSpace;

import com.framework.integration.hook.FrameworkHookManager;
import com.framework.integration.hook.IAuthenticationHook;
import com.framework.integration.hook.IAuthorizationHook;
/**
 * Base class of all Framework security managers
 * <p>
 * @author    realMethods, Inc.
 */
abstract public class FrameworkSecurityManager 
	extends FrameworkBaseObject
    implements IFrameworkSecurityManager
{
//***************************************************   
// Public Methods
//***************************************************
    /**
     * Constructor
     */
	public FrameworkSecurityManager()                    
    {
    	try
    	{    	
			getAuthenticationHook();
			getAuthorizationHook();
    	}
		catch( HookAccessException exc )
		{
			logWarnMessage( "FrameworkSecurityManager.constructor - " + exc );
		}		

    }
    
    /**
     * External initializer, which loads the security related interfaces
     * <p>
     * @param		name		sec. mgr. unique name
     * @param 		props		key/value pairings to configure by
     * @exception	InitializationException
     */
    public void initialize( String name, Map props )
    throws InitializationException
    {
    	this.name 			= name;
    	this.properties	= props;
    	
       	//---------------------------------------------------------    	
    	// assign the default indicator
       	//---------------------------------------------------------
    	
    	String defaultIndicator = (String)props.get( DEFAULT_INDICATOR );
    	
    	if ( defaultIndicator != null)
			defaultSecurityManager = new Boolean(defaultIndicator).booleanValue();
		
       	//---------------------------------------------------------
        // Retrieve the application identifier from the framework.xml
        //---------------------------------------------------------
        
	    appID = Utility.getFrameworkProperties().getProperty( FrameworkNameSpace.APPLICATION_ID_TAG, "" );
                                
		authenticator 		= createAuthenticator();
		securityUserLoader 	= createSecurityUserLoader();
		securityRoleLoader 	= createSecurityRoleLoader();
    }				         
				         

    
    /**
     * Authenticates user, loads user's Security Roles, and
     * creates the SecurityUser, and then notifies the subclass to
     * do something with them
     * <p>
     * @param		userID			user identifier
     * @param 		password		user's password
     * @param  		appID			ignore, 
     * @return		ISecurityUser	user security abstraction
     * @exception 	FrameworkSecurityManagerException Thrown if processing error occurs.
     */
    public ISecurityUser retrieveSecurityUser( String userID, String password, String appID ) 
    throws FrameworkSecurityManagerException
    {
		// load user 
		boolean userExists = false;
		
		try
		{
			userExists = authenticator.authenticateUser( userID, password, appID );
		}
		catch( Throwable exc )
		{ 
			exc.printStackTrace();
			throw new FrameworkSecurityManagerException("FrameworkSecurityManager:retrieveSecurityUser(..) - User not found for : " + appID + ":" + userID + ":" + password + " - " + exc, exc );			
		}
		
		// if not found, throw exception
		if ( userExists == true )
		{
			try
			{			
				if ( authenticationHook != null )
					authenticationHook.authenticationSuccess( userID, password, this );
			}
			catch( HookProcessException exc )
			{
				logWarnMessage( "FrameworkSecurityManager.retrieveSecurityUser() - " + exc );
			}
		}
		else		
		{
			try
			{	
				if ( authenticationHook != null )		
					authenticationHook.authenticationSuccess( userID, password, this );
			}
			catch( HookProcessException exc )
			{
				logWarnMessage( "FrameworkSecurityManager.retrieveSecurityUser() - " + exc );
			}
							
			throw new FrameworkSecurityManagerException("FrameworkSecurityManager:retrieveSecurityUser(..) - User not found for : " + appID + ":" + userID + ":" + password);
		}

		// Create the User object
		ISecurityUser securityUser = null;
		
		try
		{
			securityUser = (ISecurityUser)securityUserLoader.retrieveSecurityUser( userID , appID );
		}
		catch (Throwable exc)
		{
		    throw new FrameworkSecurityManagerException("FrameworkSecurityManager:retrieveSecurityUser(..) - Could not create the Security User because of: " + exc, exc);   
		}

		// retrieve the user's roles
		Collection securityRoles = securityRoleLoader.loadSecurityRolesForUser( securityUser, appID );

		// notify subclass to do something specific with secured user and its roles
		applyUserAndRoles( securityUser, securityRoles );	
		
		logInfoMessage( "Permitted identities for user " + securityUser.getUserID() + " are " + securityRoles.toString() );

		return securityUser;
           
    }
    
    /**
     * Retrieves whether the user has the permission based upone
     * the passed in action and the contained SecurityContext.
     * The context and action will concatenated with a period.
     * <p>
     * @param	userid		user identifier
     * @param 	role		role to authorize against
     * @return 	boolean		
     */
    public boolean isUserAuthorized( String userid, String role ) 
    {                                
		// delegate to the overloaded version isUserAuthorized( ISecurityUser user, ISecurityRole role ) 
		return isUserAuthorized( new SecurityUser( userid ), new SecurityRole( role ) ); 
    }    
	    
    /**
     * Creates a Security Role with the specified permissionName.
     * <p>
     * @param 	permissionName
     * @return  ISecurityRole
     * @exception IllegalArgumentException Thrown if permissionName is null
     */
    public ISecurityRole getSecurityRole( String permissionName ) 
    throws IllegalArgumentException
    {
       if ( permissionName  == null ) 
       {
            throw new IllegalArgumentException("FrameworkSecurityManager:getSecurityRole(..) - permissionNameIn cannot be null.");
       }

       SecurityRole returnSecurityRole = new SecurityRole( permissionName );
       
       return returnSecurityRole;
    }
    
    /**
     * Returns the indicator as to if this instance is the default security managers.
     * <p>
     * @return boolean
     */
    public boolean isDefaultSecurityManager()
    {
    	return( defaultSecurityManager );
    }
        
    /**
     * Assigns the indicator as to if this instance is the default security managers.
     * <p>
     * @param def
     */
    public void isDefaultSecurityManager( boolean def )
    {
    	defaultSecurityManager = def;
    }
           
	/**
	 * Returns the IAuthenticateUser implementation in use.
	 * <p>
	 * @return IAuthenticateUser
	 */           
	public IAuthenticateUser getAuthenticator()
	{
		return( authenticator );
	}
	
	/**
	 * Returns the ISecurityRoleLoader implementation in use.
	 * <p>
	 * @return ISecurityRoleLoader
	 */
	public ISecurityRoleLoader getRoleLoader()
	{
		return( securityRoleLoader );
	}
	
	/**
	 * Returns the ISecurityUserLoader implementation in use.
	 * <p>
	 * @return ISecurityUserLoader
	 */
	public ISecurityUserLoader getUserLoader()
	{
		return( securityUserLoader );
	}		
	
	/**
	 * Returns the name of the security manager.
	 * <p>
	 * @return String
	 */
	public String getName()
	{
		return( name );
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
	abstract protected IAuthenticateUser createAuthenticator()
	throws InitializationException;
    
    /**
     * Helper to create the implementation and security
     * specific ISecurityRoleLoader
     * <p>
     * @return		ISecurityRoleLoader
     * @exception	InitializationException
     */    
	abstract protected ISecurityRoleLoader createSecurityRoleLoader()
	throws InitializationException;
	
    /**
     * Helper to create the implementation and security
     * specific ISecurityUserLoader
     * <p>
     * @return		ISecurityUserLoader
     * @exception	InitializationException
     */	
	abstract protected ISecurityUserLoader createSecurityUserLoader()
	throws InitializationException;
	
	/**
	 * Subclass must apply the secured user and its loaded roles
	 * <p>
	 * @param		user			security user abstraction
	 * @param		roles			security roles to apply with the provided user
	 * @exception	FrameworkSecurityManagerException
	 */
	abstract protected void applyUserAndRoles( ISecurityUser user, Collection roles )
	throws FrameworkSecurityManagerException;
	
	/**
	 * Properties initialized with.
	 * <p>
	 * @return Map
	 */
	protected Map getProperties()
	{
		return( properties );
	}
	
	/**
	 * 
	 * @return	authorization hook
	 * @throws HookAccessException
	 */
	protected IAuthorizationHook getAuthorizationHook()
	throws HookAccessException
	{
		if ( authorizationHook == null )
			authorizationHook = (IAuthorizationHook)FrameworkHookManager.getHookManager().getHook( FrameworkEventType.Authorization.getEventType() );
			
		return( authorizationHook );			
	}
    
    /**
     * 
     * @return	authentication hook
     * @throws HookAccessException
     */
	protected IAuthenticationHook getAuthenticationHook()
	throws HookAccessException
	{
		if ( authenticationHook == null )
			authenticationHook = (IAuthenticationHook)FrameworkHookManager.getHookManager().getHook( FrameworkEventType.Authentication.getEventType() );
			
		return( authenticationHook );			
	}	
	
	/**
	 * Access and execute the authorization hook, if available
	 * 
	 * @param 	isAuthorized
	 * @param 	user		security user abstraction
     * @param	role		security role abstraction
	 */
	protected void notifyUserAuthorized( boolean isAuthorized, ISecurityUser user, ISecurityRole role )
	{		
		try
		{
			if ( authorizationHook != null )
			{			
				if ( isAuthorized )
					authorizationHook.authorizationSuccess( user, role, this );
				else
					authorizationHook.authorizationFailure( user, role, this );
			}				
		}
		catch( HookProcessException exc )
		{
			logWarnMessage( "FrameworkSecurityManager.notifyUserAuthorized(boolean) - " + exc );
		}		
	}
	
//***************************************************   
// Attributes
//***************************************************
    
    /**
     * default indicator
     */    
    private final String DEFAULT_INDICATOR = "default";
    
    /**
     * unique name amongst other instances
     */
    protected String name = null;
    
    /**
     * application identifier
     */
    protected String appID = null;
    
    /**
     * default security manager indicatr
     */
    protected boolean defaultSecurityManager = false;    
    
    /**
     * properties initialized with
     */
    protected Map properties  = null;
    
	/**
	 * The User Authenticator interface.
	 */
	private IAuthenticateUser authenticator = null;
    /**
     * The Security Role Loader.
     */
	private ISecurityRoleLoader securityRoleLoader = null;
	/**
	 * The Security User Loader.
	 */
	private ISecurityUserLoader securityUserLoader = null;
	
	private IAuthenticationHook authenticationHook = null;	
	private IAuthorizationHook authorizationHook = null;   
}

/*
 * Change Log:
 */
