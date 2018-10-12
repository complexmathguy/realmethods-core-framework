/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.security.jaas;

import java.security.Principal;

import java.security.acl.Group;

import java.util.*;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

import javax.security.auth.login.LoginContext;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.exception.FrameworkSecurityManagerException;
import com.framework.common.exception.InitializationException;

import com.framework.integration.security.IAuthenticateUser;
import com.framework.integration.security.ISecurityRoleLoader;
import com.framework.integration.security.ISecurityUser;
import com.framework.integration.security.ISecurityUserLoader;
import com.framework.integration.security.SecurityRole;
import com.framework.integration.security.SecurityUser;

/**
 * 
 * Framework JAAS LoginModule implementation.  Can be used as a proxy to delegate
 * to an external implementation of LoginModule.
 * <p>
 * @author    realMethods, Inc.
 */
public class FrameworkJAASLoginModuleWrapper extends FrameworkBaseObject
    implements IAuthenticateUser, ISecurityRoleLoader, ISecurityUserLoader
 
{
	
//***************************************************   
// Constructors
//***************************************************

	/**
	 * default constructor
	 */
    protected FrameworkJAASLoginModuleWrapper()
    {
        loginContexts = Collections.synchronizedMap( new HashMap() );     	
    }
    
	/**
	 * constructor
	 * 
	 * @param		properties		key/value config params
	 * @exception	InitializationException
	 */
    public FrameworkJAASLoginModuleWrapper( Map properties )    
    throws InitializationException
    {    		
		loginModuleName = (String)properties.get( LOGIN_MODULE_NAME_KEY );	
        loginContexts = Collections.synchronizedMap( new HashMap() ); 		
    } 
    
// access methods    

	/**
	 * Returns the Map of LoginContexts
	 * @return		Map
	 */
	public Map getLoginContexts()
	{
		return( loginContexts );
	}
	
	
// IAuthenticateUser implementation

    /** 
     * Authenticates the user.
     * @param	userID		user identifier
     * @param 	password	associated password
     * @param 	appID		unused, but could provide for further authentication context
     * @return boolean true if the user is authenticated and false otherwise.
     * @exception FrameworkSecurityManagerException Thrown when any error occurs.
     */
    public boolean authenticateUser( String userID, String password, String appID)
        throws FrameworkSecurityManagerException
    {    	
    	boolean bAuthenticated 	= false;
    	LoginContext loginContext 	= null;
    	
    	try
    	{		
	      	// Create LoginContext and provide the username/password callback inner class
     		loginContext = new LoginContext( loginModuleName, new UsernamePasswordHandler( userID, password ) );
	    }
    	catch(Throwable exc)
    	{	
    		// a thrown exception during login() implies authentication failure...
    		throw new FrameworkSecurityManagerException( "FrameworkJAASLoginModuleWrapper.authenticateUser() failed to create LoginContext - " + exc, exc );
    	}
    
    	if  ( loginContext != null )
    	{	   
	    	try
    		{		
				loginContext.login();
				
				bAuthenticated = true;
			
				// cache it
				loginContexts.put( userID, loginContext );
		    }
    		catch(Throwable exc)
	    	{	
    			// a thrown exception during login() implies authentication failure...
    			throw new FrameworkSecurityManagerException( "FrameworkJAASLoginModuleWrapper.authenticateUser() failed - " + exc, exc );
	    	}
    	}
    		
		return( bAuthenticated );  	
    }
    
	/**
	 * Notification from client that the supplied user id is no longer to be deemed
	 * authenticated.
	 * <p>
	 * @param 	userID		user identifier
	 */        
	public void unAuthenticateUser( String userID )
	{
		// logout the associated LoginContext
		LoginContext loginContext = (LoginContext)getLoginContexts().get( userID );
		
		if ( loginContext != null )
		{
			try
			{
				loginContext.logout();
			}
			catch( Throwable exc )
			{
				logErrorMessage( "FrameworkJAASLoginModuleWrapper.unAuthenticateUser(...) - " + exc );
			}
			
			// remove it from the cache...
			loginContexts.remove( userID );
		}
	}
	    
// ISecurityRoleLoader implementation 

    /** 
     * Loads the SecurityRoles for the associate/d SecurityUser and application id.
     * In this case, the Set of loginContext.getSubject().getPrincipals() is
     * turned into a Collection of ISecurityRoles
	 * <p>
     * @param  		user	user identifier		
     * @param 		appID	unused, but could provide for further authentication context
     * @return 				Collection of SecurityRoles.  If there are no security roles then
     * 						the Collection will be empty.  
     * @exception IllegalArgumentException 			Thrown if user is null.
     * @exception FrameworkSecurityManagerException Thrown when any other error occurs.
     */
    public Collection loadSecurityRolesForUser( ISecurityUser user, String appID ) 
        throws IllegalArgumentException, FrameworkSecurityManagerException
	{
		if ( user == null )
			throw new IllegalArgumentException( "FrameworkJAASLoginModule.loadSecurityRolesForUser(...) - ISecurityUser arg cannot be null." );
		
		ArrayList roles = null;
			
		LoginContext loginContext = (LoginContext)getLoginContexts().get( user.getUserID() );
		
		if  ( loginContext != null )		
		{
			Set principals = loginContext.getSubject().getPrincipals();
						
			roles = new ArrayList( principals.size() );			
						
			Iterator iter = principals.iterator();						
			Principal principal = null;
			
			// could be a principal or group
			while( iter.hasNext() )
			{
				principal = (Principal)iter.next();
				
				if ( principal instanceof Group )
				{
					Enumeration e = ((Group)principal).members();
					while( e.hasMoreElements() )
						roles.add( new SecurityRole( e.nextElement().toString() ) );
				}
				else
					roles.add( new SecurityRole( principal.toString() ) );
			}
		}
		
		return( roles );
    }
     
     
// ISecurityUserLoader implementation

    /** 
     * Creates a SecurityUser from the userID and appID.
     * 
     * @param 		userID			user identifier
     * @param 		appID			unused, but could provide for further authentication context
     * @return 		ISecurityUser 	user security abstraction
     * @exception IllegalArgumentException Thrown if userID is null.
     * @exception FrameworkSecurityManagerException Thrown if anyother errors occur.
     */
    public ISecurityUser retrieveSecurityUser(String userID, String appID)
    throws IllegalArgumentException, FrameworkSecurityManagerException
    {
        ISecurityUser returnUser = null;        
        
        returnUser = new SecurityUser( userID );
        
        return returnUser;    	
    }
    

//***************************************************   
// Attributes
//***************************************************

	/**
	 * login module name property key found in security.properties
	 */
	static private final String LOGIN_MODULE_NAME_KEY  = "loginModuleName";
	
	/**
	 * LoginModule name property value found in security.properties
	 */
	protected String loginModuleName = null;

	/**
	 * Collection of user specific LoginContext
	 */	
	protected Map loginContexts = null;

	
//***************************************
// inner callback class
//***************************************

	/**
	 * Helper class, provided to the JAAS LoginContext, to return
	 * the userid and password, previously provided to the wrapper class itself
	 */
	class UsernamePasswordHandler implements CallbackHandler
	{
	   protected String username;
	   protected String password;
	
	   /** 
		* @param		username		
		* @param		password
	    */
	   public UsernamePasswordHandler( String username, String password )
	   {
	      this.username = username;
	      this.password = password;
	   }
	
	
	   /** 
	    * Assigns any NameCallback name property to the  username,
	    * and assigns any PasswordCallback password property to the password.
	    * @param		callbacks						minimally expects NameCallback and PasswordCallback
	    * @exception 	UnsupportedCallbackException 	thrown if any callback of
		*    											type other than NameCallback or PasswordCallback are seen.
	    */
	   	public void handle(Callback[] callbacks) throws
	         UnsupportedCallbackException
		{			
			Callback c  = null;
			
			for (int i = 0; i < callbacks.length; i++)
			{
	    		c = callbacks[i];
	    	
		   		if (c instanceof NameCallback)
	     		{
	        		NameCallback nc = (NameCallback) c;
	        		nc.setName(username);
	     		}
	     		else if (c instanceof PasswordCallback)
	     		{
	        		PasswordCallback pc = (PasswordCallback) c;
				    pc.setPassword(password.toCharArray());
	     		}			    
	 			else
		 		{
			    	throw new UnsupportedCallbackException( callbacks[i], "Unrecognized Callback type...only support NameCallback and PasswordCallback.");
	     		}
	  		}
		}
		
	}
	
}	

/*
 * Change Log:
 * $Log: FrameworkJAASLoginModuleWrapper.java,v $
 */
