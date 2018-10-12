/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.security.jaas;

	     
//**********************************
// Imports
//**********************************
import java.io.InputStream;
import java.io.IOException;

import java.net.URL;

import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.security.auth.login.LoginException;

/**
 * 
 * File-based LoginModule used to authenticate and authorize a user.
 * <p>
 * Authentication requires a CallbackHandler to provide a userid and password.
 * <p>
 * This implementation requires the existence of two property files whose names
 * are provided as options: usersFile and rolesFile.  If not provided, they default
 * to user.properties and roles.properties respectively.
 * <p>
 * The format for users.properties is: <i>username=password</i>
 * <p>
 * The format for roles.properties is: <i>username=role1,role2,role3</i>
 * <p>
 * @author    realMethods, Inc.
 */
public class FileLoginModule extends FrameworkBaseLoginModule 
{
    
//************************************************************************
// Protected / Private Methods
//************************************************************************    

	/**
	 * Internal helper method used to authenticate the user/password combination.
	 * If successful, will the load the users credentials.
	 * <p>
	 * @return		boolean				authentication indicator
	 * @exception	LoginException		thrown if authentication or credential loading fails
	 */
	protected boolean authenticateAndLoadCredentials()
	throws LoginException
	{	
		String usersFile 	= (String)getOptions().get( USERS_FILE_KEY );
		String rolesFile	= (String)getOptions().get( ROLES_FILE_KEY );

		// if userFile not provided, default to name user.properties
		if ( usersFile == null || usersFile.length() == 0 )
		{
			usersFile = "users.properties";
			logWarnMessage( "FileLoginModule.authenticateAndLoadRoles() - empty value for key " + USERS_FILE_KEY + " is causing the use of default name users.properties." );
		}
	
		// if roleFile not provided, default to name roles.properties
		if ( rolesFile == null || rolesFile.length() == 0 )
		{
			rolesFile = "roles.properties";
			logWarnMessage( "FileLoginModule.authenticateAndLoadRoles() - empty value for key " + ROLES_FILE_KEY + " is causing the use of default name roles.properties." );
		}		
		
		boolean bAuthenticated	= false;			

		try
		{
			// load the user property file, and discover if the provided username/password is included
			
			Properties usersProperties 	= loadProperties( usersFile );
			String loadedPassword		= null;
			
			if( usersProperties != null )
			{
				loadedPassword = usersProperties.getProperty( getUsername() );
				
				if ( loadedPassword != null ) // mean the username was correct...
					bAuthenticated = getPassword().compareTo( loadedPassword ) == 0 ? true : false;
			}
						
			// load the roles property file, and discover the  roles for the provided username
			if ( bAuthenticated == true )
			{
				Properties rolesProperties 	= loadProperties( rolesFile );
				String rolesDelimited		= null;
				ArrayList roles				= new ArrayList();
								
				if( rolesProperties != null )
				{
					rolesDelimited = rolesProperties.getProperty( getUsername() );
					
					if ( rolesDelimited != null )		
					{
						// comma-delimited, so tokenize
						StringTokenizer tokenizer = new StringTokenizer( rolesDelimited, "," );
						
						while( tokenizer.hasMoreTokens() )
						{							
							roles.add( tokenizer.nextToken() );
						}
						
						applyRolesAsPrincipalsToSubject( roles );							
					}
					else
					{
						// log the fact that the user doesn't have any roles
						logWarnMessage( "FileLoginModule.authenticateAndLoadRoles() - no roles loaded for user " + getUsername() );
					}
				}
			}
		}
		catch( Throwable exc )
		{
			throw new LoginException( "FileLoginModule.authenticateAndLoadCredentials() failed - " + exc );	
		}
			
		return( bAuthenticated );
	}

//************************************************************************
// Protected / Private Methods
//************************************************************************   

    /**
    * Using the classpath, locates the provided property file, loads its contents into
    * a Properties object, and returns
    * 
    * @param		propertyFile
    * @return		content of the property file.
    * @exception	IOExceptio
    */
    private Properties loadProperties( String propertyFile ) 
    throws IOException
    {
        Properties props 	= null;
        URL url 			= Thread.currentThread().getContextClassLoader().getResource( propertyFile );
        
        if( url == null )
            throw new IOException( "FileLoginModule.loadProperties() - property file " + propertyFile + " not found");

        InputStream inputStream = url.openStream();
        
        if( inputStream != null )
        {
            props = new Properties();
            props.load( inputStream );
            inputStream.close();
        }
        else
        {        	
            throw new IOException( "FileLoginModule.loadProperties() - property file " + propertyFile + " located but unusable at this time.");        	
        }
        
        return props;
    }
	
//***************************************************   
// Attributes
//***************************************************

	final static private String USERS_FILE_KEY					= "usersFile";
	final static private String ROLES_FILE_KEY					= "rolesFile";
	
}

/*
 * Change Log:
 * $Log: FileLoginModule.java,v $
 * Revision 1.1  2003/10/07 22:14:42  tylertravis
 * initial version
 *
 */
