/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.security.jaas;

import java.util.Collection;

import javax.security.auth.login.LoginException;

import com.framework.common.misc.Utility;

import com.framework.common.namespace.FrameworkNameSpace;

import com.framework.integration.security.FrameworkLDAPSecurityAdapter;
import com.framework.integration.security.SecurityUser;

/**
 * 
 * LDAP LoginModule used to authenticate and authorize a user.
 * <p>
 * Authentication requires a CallbackHandler to provide a userid and password.
 * <p>
 * This implementation requires the appropriate configuratio and connection
 * of a pool of LDAPConnectionImpls. specified in connectionpool.properties.
 * <p>
 * If option parameter connectionPoolName is not provided, the default LDAP 
 * connection name from framework.xml will be used instead.  
 * <p>
 * @author    realMethods, Inc.
 */
public class LDAPLoginModule extends FrameworkBaseLoginModule 
{
    
//************************************************************************
// Protected / Private Methods
//************************************************************************    

	/**
	 * Internal helper method used to authenticate the user/password combination.
	 * If successful, will the load the users credentials.
	 * 
	 * @return		boolean
	 * @exception	LoginException
	 */
	protected boolean authenticateAndLoadCredentials()
	throws LoginException
	{	

		String connPoolName	= (String)getOptions().get( CONN_POOL_KEY );
		
		debug = "true".equalsIgnoreCase((String)getOptions().get("debug"));

		// if the connection pool name is not provided, use the default as indicated in
		// the framework.xml
		if ( connPoolName == null || connPoolName.length() == 0 )
		{
			connPoolName = Utility.getFrameworkProperties().getProperty( FrameworkNameSpace.DEFAULT_LDAP_CONNECTION_NAME );
			logWarnMessage( "LDAPLoginModule.authenticateAndLoadRoles() - empty value for key " + CONN_POOL_KEY + " is causing the use of default LDAP connection name " + connPoolName );
		}
		
		boolean bAuthenticated	= false;
			
		// create a FrameworkLDAPSecurityAdapter to assist in authentication and role loading
		// using LDAP
		FrameworkLDAPSecurityAdapter ldapSecurityAdapter = new FrameworkLDAPSecurityAdapter( connPoolName );
		
		try
		{
			bAuthenticated =  ldapSecurityAdapter.authenticateUser( getUsername(), getPassword(), "" /*application id ignored*/ );
			
			if ( bAuthenticated == true )
			{
				Collection roles = ldapSecurityAdapter.loadSecurityRolesForUser( new SecurityUser( getUsername() ), "" /*application id ignored*/ );
				
				if ( roles != null && roles.size() > 0 )
				{	
					applyRolesAsPrincipalsToSubject( roles );			
				}
				else
				{
					// log the fact that the user doesn't have any credentials
					logWarnMessage( "LDAPLoginModule.authenticateAndLoadRoles() - no roles loaded for user " + getUsername() );
				}
			}
		}
		catch( Throwable exc )
		{
			throw new LoginException( "LDAPLoginModule.authenticateAndLoadCredentials() failed - " + exc );	
		}
			
		return( bAuthenticated );
	}

	
//***************************************************   
// Attributes
//***************************************************

	final static private String CONN_POOL_KEY					= "connectionPoolName";

	protected boolean debug										= false;
	
}

/*
 * Change Log:
 * $Log: LDAPLoginModule.java,v $
 * Revision 1.1  2003/10/07 22:14:42  tylertravis
 * initial version
 *
 */
