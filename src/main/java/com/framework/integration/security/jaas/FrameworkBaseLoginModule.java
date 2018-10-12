/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.security.jaas;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

import javax.security.auth.login.LoginException;

import javax.security.auth.spi.LoginModule;

import com.framework.common.FrameworkBaseObject;

import com.framework.integration.security.ISecurityUser;
import com.framework.integration.security.SecurityUser;

/**
 * 
 * Base Framework LoginModule used authenticate and authorize a user.
 * <p>
 * Authentication requires a CallbackHandler to provide a userid and password.
 * <p>
 * @author    realMethods, Inc.
 */
abstract public class FrameworkBaseLoginModule 
extends FrameworkBaseObject
implements LoginModule 
{

    /**
     * Initialize this <code>LoginModule</code>.

     * @param subject the <code>Subject</code> to be authenticated.
     * @param callbackHandler a <code>CallbackHandler</code> for communicating
     *			with the end user for the username and password
     * @param sharedState shared <code>LoginModule</code> state.
     * @param options options specified in the login
     *			<code>Configuration</code> for this particular
     *			<code>LoginModule</code>.
     */
    public void initialize( Subject subject, CallbackHandler callbackHandler,
								Map sharedState, Map options) 
	{
		this.subject 			= subject;
		this.callbackHandler 	= callbackHandler;
		this.sharedState 		= sharedState;
		this.options 			= options;
						
		debug = "true".equalsIgnoreCase((String)getOptions().get("debug"));		
    }

    /**
     * Authenticate the user by prompting for a username and password.
     *
     * <p>
     *
     * @return true in all cases since this <code>LoginModule</code>
     *		should not be ignored.
     *
     * @exception FailedLoginException if the authentication fails. <p>
     *
     * @exception LoginException if this <code>LoginModule</code>
     *		is unable to perform the authentication.
     */
    public boolean login() 
    throws LoginException 
    {		
		// prompt for a username and password
		if (callbackHandler == null)
	    	throw new LoginException("FrameworkBaseLoginModule.login() - no CallbackHandler available " +
				"to garner authentication information from the user");

		Callback[] callbacks = new Callback[2];
		callbacks[0] = new NameCallback("Username: ");
		callbacks[1] = new PasswordCallback("Password: ", false);
 
		try 
		{
		    callbackHandler.handle(callbacks);
		    username = ((NameCallback)callbacks[0]).getName();
	    	char[] tmpPassword = ((PasswordCallback)callbacks[1]).getPassword();
	    	
		    if (tmpPassword == null) 
		    {
				// treat a NULL password as an empty password
				tmpPassword = new char[0];
		    }
		    
		    password = new char[tmpPassword.length];
	    	System.arraycopy(tmpPassword, 0,
				password, 0, tmpPassword.length);
		    ((PasswordCallback)callbacks[1]).clearPassword();
 
		} 
		catch (java.io.IOException ioe) 
		{
	    	throw new LoginException(ioe.toString());
		} 
		catch (UnsupportedCallbackException uce) 
		{
	    	throw new LoginException("FrameworkBaseLoginModule.login() - " + uce.getCallback().toString() +
				" not available.");
		}

		try
		{
			succeeded = authenticateAndLoadCredentials();
		}
		catch( Throwable exc )
		{		    			
		    throw new LoginException( "FrameworkBaseLoginModule.login() - " + exc );
		} 
		
		return( succeeded );
    }

    /**
     * <p> This method is called if the LoginContext's
     * overall authentication succeeded
     * (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL LoginModules
     * succeeded).
     * <p> If this LoginModule's own authentication attempt
     * succeeded (checked by retrieving the private state saved by the
     * <code>login</code> method), then this method associates an
     * <code>ISecurityUser</code>
     * with the <code>Subject</code> located in the
     * <code>LoginModule</code>.  If this LoginModule's own
     * authentication attempted failed, then this method removes
     * any state that was originally saved.
     *
     * @exception LoginException if the commit fails.
     *
     * @return true if this LoginModule's own login and commit
     *		attempts succeeded, or false otherwise.
     */
    public boolean commit() 
    throws LoginException 
    {
		if (succeeded == false) 
		{		
	    	return false;
		} 
		else 
		{
		    // add a Principal (authenticated identity)
		    // to the Subject

	    	// assume the user we authenticated is the SamplePrincipal
		    userPrincipal = new SecurityUser(username);
		    
		    if (!subject.getPrincipals().contains(userPrincipal))
				subject.getPrincipals().add(userPrincipal);

		    commitSucceeded = true;
		    
		    return true;
		}
    }

    /**
     * Called if the LoginContext's overall authentication failed.
     * (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL LoginModules
     * did not succeed).
     * <p>
     * If this LoginModule's own authentication attempt
     * succeeded (checked by retrieving the private state saved by the
     * <code>login</code> and <code>commit</code> methods),
     * then this method cleans up any state that was originally saved.
     *
     * @return false if this LoginModule's own login and/or commit attempts
     *		failed, and true otherwise.
     * @exception	LoginException
     */
    public boolean abort() 
    throws LoginException 
    {
		if (succeeded == false) 
		{
	    	return false;
		} 
		else if (succeeded == true && commitSucceeded == false) 
		{
		    // login succeeded but overall authentication failed
		    succeeded = false;					    
		    userPrincipal = null;
		    cleanOutState();
		} 
		else 
		{
		    // overall authentication succeeded and commit succeeded,
		    // but someone else's commit failed
	    	logout();
		}
		return true;
    }

    /**
     * Logout the user.
     *
     * This method removes the <code>SecurityUser</code>
     * that was added by the <code>commit</code> method.
     * <p>
     * @return true in all cases since this <code>LoginModule</code>
     *          should not be ignored.
     * @exception LoginException if the logout fails.
     */
    public boolean logout() 
    throws LoginException 
    {
		subject.getPrincipals().remove(userPrincipal);
		
		succeeded = false;
		succeeded = commitSucceeded;		
		userPrincipal = null;
		
		cleanOutState();
		
		return true;
    }
    

//************************************************************************
// Protected / Private Methods
//************************************************************************    

	/**
	 * internal helper method used to authenticate the user/password combination.
	 * If successful, will the load the users credentials.
	 * <p>
	 * @return		boolean
	 * @exception	LoginException
	 */
	abstract protected boolean authenticateAndLoadCredentials()
	throws LoginException;
	
	/**
	 * Helper method used to turn apply a Collection of ISecurityRoles into
	 * Principals to be applied to the Subject.
	 * <p>
	 * @param roles
	 */
	protected void applyRolesAsPrincipalsToSubject( Collection roles )
	{
		Iterator iter 	= roles.iterator();
		Set principals	= subject.getPrincipals();
		 
		while( iter.hasNext() )
		{
			// apply to the subjects principals
			principals.add( new SecurityUser( iter.next().toString() ) );	
		}		
	}
	
	protected Subject getSubject()
	{
		return( subject );
	}
	
	protected String getUsername()
	{
		return( username );
	}
	
	protected String getPassword()
	{
		return( String.valueOf( password ) );
	}
	
	protected Map getOptions()
	{
		return( options );
	}
	
	protected boolean debug()
	{
		return( debug );
	}
	
	/**
	 * resets the username and password variables
	 */
	protected void cleanOutState()
	{
	    username = null;
	    
    	for (int i = 0; i < password.length; i++)
			password[i] = ' ';
			
    	password = null;		
	}
	
	
//***************************************************   
// Attributes
//***************************************************
    
    // initial JAAS state
    private Subject subject;
    private CallbackHandler callbackHandler;
    protected Map sharedState;
    private Map options;

    // the authentication status
    private boolean succeeded = false;
    private boolean commitSucceeded = false;

    // username and password
    private String username;
    private char[] password;

    // Security Principal
    private ISecurityUser userPrincipal;   
    
	
	private boolean debug	= false;     
}

/*
 * Change Log:
 * $Log: FrameworkBaseLoginModule.java,v $
 * Revision 1.1  2003/10/07 22:14:42  tylertravis
 * initial version
 *
 */
