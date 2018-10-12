/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.security.jaas;

import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.Collection;

import javax.security.auth.login.LoginException;

import com.framework.common.exception.ConnectionAcquisitionException;
import com.framework.common.exception.ObjectCreationException;
import com.framework.common.exception.ResultSetCallbackException;

import com.framework.common.misc.Utility;

import com.framework.common.namespace.FrameworkNameSpace;

import com.framework.common.parameter.StringParameter;

import com.framework.integration.objectpool.ConnectionPoolManagerFactory;

import com.framework.integration.objectpool.db.IDatabaseQuerier;
import com.framework.integration.objectpool.db.IResultSetCallback;
import com.framework.integration.objectpool.db.ResultsetToStringUnloader;

/**
 * 
 * Database LoginModule used to authenticate and authorize a user.
 * <p>
 * Authentication requires a CallbackHandler to provide a userid and password.
 * <p>
 * Requires the following option parameters be provided:
 * selectUserSQLStmt - the sql select statement used to query on the userid and password
 * selectRolesSQLStmt - the sql select statement used to load the roles for the  provided userid
 * <p>
 * If option parameter connectionPoolName is not provided, the DEFAULT_CONNECTION_NAME 
 * from framework.xml will be used instead.
 * <p>
 * @author    realMethods, Inc.
 */
public class DatabaseLoginModule extends FrameworkBaseLoginModule 
{
    
//************************************************************************
// Protected / Private Methods
//************************************************************************    

	/**
	 * Internal helper method used to authenticate the user/password combination.
	 * If successful, will the load the users credentials.
	 * 
	 * @return		boolean			authentication status indicator
	 * @exception	LoginException	
	 */
	protected boolean authenticateAndLoadCredentials()
	throws LoginException
	{	
		String userSQLStmt	= (String)getOptions().get( SELECT_USER_SQL_STMT_KEY );
		String rolesSQLStmt = (String)getOptions().get( SELECT_ROLES_SQL_STMT_KEY );
		String connPoolName	= (String)getOptions().get( CONN_POOL_KEY );
		
		if ( userSQLStmt == null )
			throw new IllegalArgumentException( "DatabaseLoginModule.authenticateAndLoadRoles() - option key " + SELECT_USER_SQL_STMT_KEY + " is not present." );
			
		if ( rolesSQLStmt == null )
			throw new IllegalArgumentException( "DatabaseLoginModule.authenticateAndLoadRoles() - option key " + SELECT_ROLES_SQL_STMT_KEY + " is not present." );
			
		// if the connection pool name is not provided, use the default as indicated in
		// the framework.xml
		if ( connPoolName == null || connPoolName.length() == 0 )
		{
			connPoolName = Utility.getFrameworkProperties().getProperty( FrameworkNameSpace.DEFAULT_CONNECTION_NAME );
			logWarnMessage( "DatabaseLoginModule.authenticateAndLoadRoles() - empty value for key " + CONN_POOL_KEY + " is causing the use of default connection name " + connPoolName );
		}

		IDatabaseQuerier dbQuerier 		= null;
		LoginException loginException	= null;
		
		try
		{
			dbQuerier = (IDatabaseQuerier)ConnectionPoolManagerFactory.getObject().getConnection( connPoolName );					
		}
		catch( ObjectCreationException exc )
		{
			throw new LoginException( "DatabaseLoginModule.authenticateAndLoadRoles() - failed to acquire an IConnectionPoolManager from ConnectionPoolManagerFactory - " + exc );
		}
		catch( ConnectionAcquisitionException exc )
		{
			throw new LoginException( "DatabaseLoginModule.authenticateAndLoadRoles() - failed to acquire a conection from ConnectionPoolManager " + connPoolName + " - " + exc );			
		}
		catch( ClassCastException exc )
		{
			loginException = new LoginException( "DatabaseLoginModule.authenticateAndLoadRoles() - failed to cast IConnectionImpl to IDatabaseQuerier.  Make sure connection " + connPoolName + " resolves to type com.framework.common.misc.IDatabaseQuerier - " + exc );						
		}	
		
		boolean bAuthenticated	= false;
			
		if ( loginException == null )
		{			
			// execute the user sql stmt in order to authenticate the user
			ResultsetToStringUnloader userCallback 	= new ResultsetToStringUnloader();
			Collection params						= new ArrayList( 2 );
			Collection coll 						= null;

			
			// apply the user and password as parameters
			params.add( new StringParameter( getUsername(), true ));
			params.add( new StringParameter( getPassword(), true ));
			
			try
			{
				coll = dbQuerier.executeSelectStatement( userSQLStmt, params, userCallback );			
				
				if ( coll != null && coll.size() > 0 )
				{
					bAuthenticated = !((Collection)coll.iterator().next()).isEmpty();
				}
			}
			catch( Throwable exc )
			{					
				loginException = new LoginException( "DatabaseLoginModule.authenticateAndLoadRoles() - failed to authenticate user  " + getUsername() + " - " + exc );						
			}
			
			//
			if ( bAuthenticated == true )
			{
				
				// since we have a db connection, might as well as re-use it to acquire the credentials (or roles)
				CredentialsUnloader credentialsCallback	= new CredentialsUnloader();
				params = null;
				params = new ArrayList( 1 );
				params.add( new StringParameter( getUsername(), true ));
				
				try
				{
					coll = dbQuerier.executeSelectStatement( rolesSQLStmt, params, credentialsCallback );
					
					if ( coll != null && coll.size() > 0 )
					{	
						applyRolesAsPrincipalsToSubject( coll );			
					}
					else
					{
						// log the fact that the user doesn't have any credentials
						logWarnMessage( "DatabaseLoginModule.authenticateAndLoadRoles() - no roles loaded for user " + getUsername() );
					}
				}
				catch( Throwable exc )
				{					
					loginException = new LoginException( "DatabaseLoginModule.authenticateAndLoadRoles() - failed to authenticate user  " + getUsername() + " - " + exc );						
				}	
			}
			else
			{
				logWarnMessage( "DatabaseLoginModule.authenticateAndLoadRoles() - failed to authenticate user  " + getUsername() + " with password " + String.valueOf( getPassword() ) );							
			}
		}	
		
		try
		{	
			dbQuerier.releaseConnection();			
		}
		catch( Throwable exc )
		{
				logErrorMessage( "DatabaseLoginModule.authenticateAndLoadRoles() - failed to release connection " + connPoolName + " back to its ConnectionPool." );										
		}
		
		if ( loginException != null )
			throw loginException;
			
		return( bAuthenticated );
	}

	
//***************************************************   
// Attributes
//***************************************************

	final static private String SELECT_USER_SQL_STMT_KEY 		= "selectUserSQLStmt";
	final static private String SELECT_ROLES_SQL_STMT_KEY		= "selectRolesSQLStmt";
	final static private String CONN_POOL_KEY					= "connectionPoolName";
	
//***************************************************
// inner class
//***************************************************

	/**
	 *  Helper class used to retrieve the credentials from a single result set
	 */
	public class CredentialsUnloader
	    implements IResultSetCallback
	{
	//************************************************************************
	// Public Methods
	//************************************************************************
	    /**
	     * Default Constructor.
	     */
	    public CredentialsUnloader()
	    {
	    }
	    
	//*****************************************	
	// IResultSetCallback implementation
	//*****************************************
	    /**
	     * callback method to notify of a result set for the executed sql stmt. 
	     * Creates a collection of Strings that represent credentials.
	     *
	     * @param       rs			the ResultSet of security related credentials (roles)
	     * @param       sqlstmt		SQL statement
	     * @return      			Collection of Strings that represent the Security Roles
	     * @exception   ResultSetCallbackException
	     */
	    public Collection notifyResultSet( ResultSet rs, String sqlstmt )
	        throws ResultSetCallbackException
	    {
	        Collection returnCollection = new ArrayList();
	
	        if ( rs == null )
	        {
	            throw new ResultSetCallbackException( "CredentialUnloader:notifyResultSet - null ResultSet parameter." );
	        }
	
	        try
	        {
	            // If there are records then retrieve the results and populate the returnCollection
	            String credential = null;
	            
	            if ( rs.next() == true )
	            {
	                do
	                {
                        // obtain the credential by position
                        credential = rs.getString(1);
                        
	                    // Add to ArrayList
	                    returnCollection.add( credential );
	                }while ( rs.next() );
	            }
	        }
	        catch ( Exception e )
	        {
	            String sExcMsg = "CredentialUnloader:notifyResultSet - could not access the resultset - ";
	                        
	            throw new ResultSetCallbackException( sExcMsg + ":" + e );
	        }
	
	        return( returnCollection );
	    }
	}    
}

/*
 * Change Log:
 * $Log: DatabaseLoginModule.java,v $
 * Revision 1.1  2003/10/07 22:14:42  tylertravis
 * initial version
 *
 */
