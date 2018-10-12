/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
*************************************************************************/
package com.framework.integration.objectpool.db;

import java.sql.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.naming.NamingException;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;

import com.framework.common.exception.ConnectionActionException;
import com.framework.common.exception.DatabaseConnectionFailureException;
import com.framework.common.exception.ExecuteStatementException;
import com.framework.common.exception.InitializationException;
import com.framework.common.exception.ResultSetCallbackException;
import com.framework.common.exception.SelectStatementException;
import com.framework.common.exception.StoredProcedureException;

import com.framework.common.parameter.Parameter;

import com.framework.integration.locator.ServiceLocator;

import com.framework.integration.objectpool.ConnectionImpl;

/**
 * Abstract class for making a database connection, and fulfilling a specific
 * query on behalf of the client.  As an abstract class, it is fairly  self-sufficient
 * except for the preperation of the Callstatement, where the sub-class is then required
 * to intervene by implementing the abstract protected method:<p>
 * <i>CallableStatement prepareCallableStatement( Connection connection, String sql )</i>
 * <p>
 * @author    realMethods, Inc.
 */
public abstract class DatabaseQuerier extends ConnectionImpl
    implements IDatabaseQuerier
{
//****************************************************
// Public Methods
//****************************************************

// ConnectionImpl extensions

    /** 
     * Initializes and makes a connection to the connection source.
     * This base implementation does not make the actual connection.
     * This method must be delegated to from the sub-class.
     * <p>
     * @param		name 		the pool that this connection is associated with.
     * @param 		props 		Properties needed to create the connection.
     * 							Actual values are dependent on the associated type of connection.
     * @exception	IllegalArgumentException
     * @exception	InitializationException
     */
    public void initialize( String name, Map props )
    throws IllegalArgumentException, InitializationException
    {
        super.initialize( name, props );
        
        logDebugMessage( "DatabaseQuerier:initialize(...) for poolName - " + name );
        
        // assign the maxConnectionAttempts if provided
                
        try
        {
            String value = (String)props.get( MAX_CONNECTION_ATTEMPTS );
            
            if ( value != null )	                  
				maxConnectionAttempts =  new Integer( value ).intValue();
        }
        catch( Exception e )
        {
            logDebugMessage( "DatabaseQuerier:initialize() - failed to read property " + MAX_CONNECTION_ATTEMPTS + ".  Defaulting to 1.");
            maxConnectionAttempts = 1;
        }
    }
    
    /**
     * Close the database connection.
     * @exception		ConnectionActionException
     */
    public void disconnect()
    throws ConnectionActionException
    {
        try
        {
        	System.out.println( "Closing connection in " + getPoolName() );
        	
            //********************************************
            // close the connection only if it's open
            //********************************************
            if ( theConnection != null && theConnection.isClosed() == false )
            {
                theConnection.close();
				logDebugMessage( "DatabaseQuerier:disconnect() succeeded - " + getPoolName() );
				dataSourceMappings.remove( theConnection );
            }            
        }
        catch ( Throwable exc )
        {
			throw new ConnectionActionException( "DatabaseQuerier:disconnect() - failed - " + getPoolName() + " - " + exc, exc );
        }
        finally
        {
            theConnection = null;     
            super.disconnect();       
        }
    }    

// IResultSetCallback implementations

   /**
	* Callback method to notify of result set for a provided stored
	* procedure name
	*
	* @param       rs
	* @param       sqlStatement
	* @return      Collection of 2 ArrayLists...see class description for format
	* @exception   ResultSetCallbackException
	*/
    public Collection notifyResultSet( ResultSet rs, String sqlStatement )
        throws ResultSetCallbackException
    {
        ArrayList data           = new ArrayList();

        if ( rs != null )
        {
            Object colValue         = null;
            boolean bContinue       = true;

            try
            {
                // loop through each record
                while ( rs.next() )
                {
                    StringBuffer sBuffer    = new StringBuffer();

                    data.add( rs.getObject( 1 ).toString() );

                    // loop through and attempt to output all columns
                    for ( int i = 1; ( i <= 100 ) && ( bContinue == true ); i++ )
                    {
                        try
                        {
                            colValue = rs.getObject( i );

                            if ( colValue != null )
                                sBuffer.append( colValue.toString() );
                            else
                                sBuffer.append( "null" );

                            sBuffer.append( "  " );
                        }
                        catch ( Exception e )
                        {
                            bContinue = false;
                        }
                    }

                    // display the rows data
                    logDebugMessage( sBuffer.toString() );

                    bContinue = true;
                }
            }
            catch ( SQLException sqlExc )
            {
            	logErrorMessage( "DatabaseQuerier.notifyResultSet() - " + sqlExc );
            }
        }

        return( data );

    }

// IDatabaseQuerier implementations

    /**
     * Returns the name of the JDBC driver name.
     * @return      String
     */
    public String getDatabaseDriverName()
    {
        return( (String)getProperties().get( DatabaseDriverParameter ) );
    }

    /**
     * returns the URL of the JDBC driver
     * @return      String
     */
    public String getDatabaseURL()
    {
        return( (String)getProperties().get( DatabaseURLParameter ) );
    }

    /**
     * returns the database userID
     * @return		String
     */
    public String getUserID()
    {
        return( (String)getProperties().get( UserID ) );
    }

    /**
     * returns the database password
     * @return		String
     */
    public String getPassword()
    {
        return( (String)getProperties().get( Password ) );
    }

    /**
     * returns the database password
     * @return		String
     */
    public String getJNDIDataSourceName()
    {
        return( (String)getProperties().get( JNDIDataSourceName ) );
    }

    /**
     * returns the boolean indicating if defined as a datasource, does it use pooling
     * @return      boolean - defaults to false
     */
    public boolean isUsingDataSourcePooling()
    {
        boolean bUsingDSPooling = false;
        String tmp            = (String)getProperties().get( UsingDataSourcePooling );

        if ( tmp != null )
            bUsingDSPooling = new Boolean( tmp ).booleanValue();
            
        return( bUsingDSPooling );
    }
    
    /**
     * returns a subset of the contained Properties, specific
     * to establishing a database connection
     * <p>
     * @return      Properties
     */
    public Properties getDatabaseAccessProperties()
    {
        Properties subProperties = new Properties();
        
        // these two fields are mandatory
        subProperties.put( "user", getUserID() );
        subProperties.put( "password", getPassword() );
        
        // the following fields are optionaly, but should
        // only be include if initially provided
        String sDB = (String)getProperties().get( "db" );
        
        if ( sDB != null && sDB.length() > 0 )
            subProperties.put( "db", sDB );
        
        return( subProperties );
    }



    /**
     * called by the pool manager to notify the connection it is 
     * being released
     * <p>
     * @exception		ConnectionActionException
     */
    public void connectionBeingReleased()
    throws ConnectionActionException
    {
        try
        {
            commit();
        }
        catch ( Exception ex )
        {
            rollback();
            
            throw new ConnectionActionException( "DatabaseQuerier:connectionBeingReleased() - " + getPoolName() + " - " + ex );
        }
        finally
        {      	
            super.connectionBeingReleased();
        }    	
    }

    /**
     * client notification that a transaction is about to begin.
     * @exception		ConnectionActionException
     */
    public void startTransaction()
    throws ConnectionActionException
    {
    }
    
    /**
     * client notification to commit the current transaction.
     * @exception		ConnectionActionException
     */
    public void commit()
    throws ConnectionActionException
    {
    	// cannot commit transactional related JDBC connections
        if ( theConnection != null && dataSourceMappings.contains( theConnection ) == false )
        {
        	try
        	{
	            theConnection.commit();
        	}
        	catch( Throwable exc )
        	{
        		throw new ConnectionActionException( "DatabaseQuerier:commit()  - " + getPoolName() + " - " + exc, exc );
        	}
        }
    }


    /**
     * client notification to rollback the current transaction
     * @exception		ConnectionActionException
     */
    public void rollback()
    throws ConnectionActionException
    {
		if ( theConnection != null && dataSourceMappings.contains( theConnection ) == false )
        {
        	try
        	{
	            theConnection.rollback();
        	}
        	catch( Throwable exc )
        	{
        		throw new ConnectionActionException( "DatabaseQuerier:commit()  - " + getPoolName() + " - " + exc );
        	}
            
        }
    }

    
    /**
     * query to indicate if the encapsulated JDBC connection is connected.
     * @return	boolean
     */
    public boolean isConnected()
    {
    	boolean isConnection = false;
    	
    	try
    	{
    		if ( theConnection != null && theConnection.isClosed() == false )
    			isConnection = true ;
    	}
    	catch( Throwable exc ) 
    	{
    		logErrorMessage( "DatabaseQuerier:isConnected() - failed to query the sql Connection isClosed() - " + exc );
    	}   			
		
		return( isConnection );    		
    }

// accessor methods

    /**
     * returns the database connection currently in use
     * @return		the encapsulated JDBC connection
     */
    public Connection getConnection()
    {
        return( theConnection ); 
    }

// action methods

   /**
	* Execute a select statement that will result in
	* returning a ResultSet.  The resultset is handed over to the provided
	* IResultSetCallback handler, to turn the rows of data into a Collection of
	* more useful objects.
	* <p>
	* @param		selectStatement
	* @param		callback			transforms a resultset into a Collection of more useful objects
	* @return		Collection	formatted, more useful, objects
	* @exception	IllegalArgumentException
	* @exception	DatabaseConnectionFailureException
	* @exception	SelectStatementException
	*/
    public Collection executeSelectStatement( String selectStatement, IResultSetCallback callback )
        throws IllegalArgumentException, DatabaseConnectionFailureException, SelectStatementException       
    {
        //******************************************************************
        // validate the select statement
        //******************************************************************
        if ( selectStatement == null || selectStatement.length() == 0 )
        {
            throw new IllegalArgumentException( "DatabaseQuerier::executeSelectStatement() - invalid select statement" );
        }

        //******************************************************************		
        // validate the callback interface
        //******************************************************************

        if ( callback == null )
        {
            throw new IllegalArgumentException( "DatabaseQuerier::executeSelectStatement() - null callback interface" );
        }

		logDebugMessage( "DatabaseQuerier:executeSelectStatement() - Executing select statement " + selectStatement );
		    	
        Collection returnCollection = null;
        Statement stmt          = null;
        Connection connection   = null;

        try
        {
            //******************************************************************
            // obtain a safe connection
            //******************************************************************
            connection = getSafeConnection();

            //******************************************************************
            // create the statement
            //******************************************************************
            stmt = connection.createStatement();

            //******************************************************************
            // assign a cursor name for those interested in associated
            // this statement with subsequent statements within this 
            // connection context
            //******************************************************************
//			stmt.setCursorName( selectStatement );

            //******************************************************************
            // execute the query
            //******************************************************************
            ResultSet rs = stmt.executeQuery( selectStatement );

            if ( rs != null && callback != null )
            {
                try
                {
                    //********************************************
                    // IResultSetCallback notification
                    //********************************************
                    returnCollection = callback.notifyResultSet( rs, selectStatement );
                }
                catch ( ResultSetCallbackException rscbe )
                {
                    logErrorMessage( "DatabaseQuerier:executeSelectStatement() - " + rscbe.getMessage() );   
                }

                //********************************************
                // close the result set
                //********************************************
                DBUtility.CloseResultSet( rs );
            }
        }
        catch ( SQLException exc )
        {
            throw new SelectStatementException( "DatabaseQuerier::executeSelectStatement - " + outputExceptionMessage( selectStatement, null ) + " ; " + exc.getMessage(), exc );
        }
        finally
        {
            // unconditionally release the statement
            DBUtility.CloseStatement( stmt );            
//            releaseConnection();
        }

        return( returnCollection );     
    }

	/**
	 * Used to execute update, insert, or delete related SQL statements
	 * which may result in a row count being returned
	 * 
	 * @param		selectStatement	SQL statement
	 * @param		parameters		com.framework.common.parameter.Parameter instances
	 * @return		int				rowCount
	 * @exception	IllegalArgumentException
	 * @exception   DatabaseConnectionFailureException
	 * @exception	ExecuteStatementException
	 */
    public Collection executeSelectStatement(   String selectStatement, 
                                                Collection parameters,
                                                IResultSetCallback callback )
        throws IllegalArgumentException, DatabaseConnectionFailureException, SelectStatementException       
    {
        //******************************************************************
        // validate the select statement
        //******************************************************************
        if ( selectStatement == null || selectStatement.length() == 0 )
        {
            throw new IllegalArgumentException( "DatabaseQuerier::executeSelectStatement() - invalid select statement" );
        }

        //******************************************************************		
        // validate the callback interface
        //******************************************************************

        if ( callback == null )
        {
            throw new IllegalArgumentException( "DatabaseQuerier::executeSelectStatement() - null callback interface" );
        }

		logDebugMessage( "DatabaseQuerier:executeSelectStatement() - Executing select statement " + selectStatement + " with param(s) " + (parameters != null ? parameters.toString() : "[none provided]") );

        Collection returnCollection = null;
        PreparedStatement stmt      = null;
        Connection connection       = null;
        

        try
        {
            //******************************************************************
            // obtain a safe connection
            //******************************************************************
            connection = getSafeConnection();

            //******************************************************************
            // create the prepared statement
            //******************************************************************
            stmt = connection.prepareStatement( selectStatement );

            //******************************************************************
            // assign a cursor name for those interested in associated
            // this statement with subsequent statements within this 
            // connection context
            //******************************************************************
//			stmt.setCursorName( selectStatement );

            //******************************************************************
            // iterate through each Parameter and only assign if it's an
            // in parameter.   
            //******************************************************************
            if ( parameters != null )
            {
                Parameter param = null;
                int index       = 1;
                Object o		= null;                
				Iterator iter	= parameters.iterator();
				
                while( iter.hasNext())
                {
                    param = (Parameter)iter.next();

                    if ( param.isInParameter() )
                    {
						o = param.getParameter();
                    
	                    if ( o != null )
        	                stmt.setObject( index, param.getParameter(), param.getType() );
                	    else
							stmt.setNull( index, param.getType() );
                    	    //stmt.setObject( index, param.getParameter(), java.sql.Types.NULL );                    	
                    }
                    else    // out parameter
                    {
                        // skip it..
                    }

                    index++;
                }
            }

            //******************************************************************
            // execute the query
            //******************************************************************
            ResultSet rs = stmt.executeQuery();

            if ( rs != null && callback != null )
            {
                try
                {
                    //********************************************
                    // IResultSetCallback notification
                    //********************************************
                    returnCollection = callback.notifyResultSet( rs, selectStatement );
                }
                catch ( ResultSetCallbackException rscbe )
                {
                    logErrorMessage( "DatabaseQuerier:executeSelectStatement() - " + outputExceptionMessage( selectStatement, parameters ) + " : " + rscbe.getMessage() );   
                }

                //********************************************
                // close the result set
                //********************************************
                DBUtility.CloseResultSet( rs );
            }
        }
        catch ( SQLException exc )
        {
            throw new SelectStatementException( "DatabaseQuerier::executeSelectStatement - " + outputExceptionMessage( selectStatement, parameters ) + " : " + exc.getMessage(), exc );
        }
        finally
        {
            // unconditionally release the connection
            DBUtility.CloseStatement( stmt );            
//            releaseConnection();
        }

        return( returnCollection );     
    }

	/**
	 * Used to execute update, insert, or delete related SQL statements
	 * which may result in a row count being returned
	 * 
	 * @param		statement	SQL statement
	 * @param		parameters	com.framework.common.parameter.Parameter instances
	 * @return		int			rowCount
	 * @exception	IllegalArgumentException
	 * @exception   DatabaseConnectionFailureException
	 * @exception	ExecuteStatementException
	 */
    public int executeStatement( String statement, Collection parameters )
    throws IllegalArgumentException, DatabaseConnectionFailureException, ExecuteStatementException      
    {
        //******************************************************************
        // validate the select statement
        //******************************************************************
        if ( statement == null || statement.length() == 0 )
        {
            throw new IllegalArgumentException( "DatabaseQuerier::executeStatement()" );
        }

		logDebugMessage( "DatabaseQuerier:executeStatement() - Executing statement " + statement + " with param(s) " + (parameters != null ? parameters.toString() : "[none provided]") );

        Connection connection   = null;
        int rowCount            = -1;
        PreparedStatement stmt  = null;

        try
        {
            //******************************************************************
            // obtain a safe connection
            //******************************************************************
            connection = getSafeConnection();

            //******************************************************************
            // prepare the statement
            //******************************************************************
            stmt = connection.prepareStatement( statement );

            //******************************************************************
            // assign a cursor name for those interested in associated
            // this statement with subsequent statements within this 
            // connection context
            //******************************************************************
//			stmt.setCursorName( sStatement );

            //******************************************************************
            // iterate through each Parameter and only assign if it's an
            // in parameter.   Update,Insert,Delete shouldn't take an out param.
            //******************************************************************
            if ( parameters != null )
            {
                Parameter param = null;
                int index       = 1;
                Object o		= null;
				Iterator iter	= parameters.iterator();
				
                while( iter.hasNext() )
                {
                    param = (Parameter)iter.next();

					if ( param.isInParameter() )
					{
						o = param.getParameter();

						if ( o != null )
							stmt.setObject( index, param.getParameter(), param.getType() );
						else
							stmt.setNull( index, param.getType() );
							//stmt.setObject( index, param.getParameter(), java.sql.Types.NULL );                    	
					}
					else    // out parameter
					{
						// skip it..
					}

                    index++;
                }
            }
            
            //******************************************************************
            // execute the statement and assign the count of rows effected
            //******************************************************************
            rowCount = stmt.executeUpdate();
        }
        catch ( SQLException exc )
        {
            throw new ExecuteStatementException( "DatabaseQuerier::executeStatement - " + outputExceptionMessage( statement, parameters ) + " : " + exc.getMessage(), exc );
        }
        finally
        {
            DBUtility.CloseStatement( stmt );            
            // unconditionally release the connection
//            releaseConnection();
        }

        return( rowCount );
    }

	/**
	 * executes a stored procedure which makes use of a result set.  The resultset is handed over to the provided
	 * IResultSetCallback handler, to turn the rows of data into a Collection of
	 * more useful objects.
	 * 
	 * @param		procedureName
	 * @param		parameters			Parameter objects - can be null
	 * @param		callback			transforms a resultset into a Collection of more useful objects
	 * @return							Colletion returned by the callback
	 * @exception	IllegalArgumentException
	 * @exception   DatabaseConnectionFailureException
	 * @exception	StoredProcedureException
	 */
    public Collection executeStoredProcedure(  String procedureName, 
                                                Collection parameters, 
                                                IResultSetCallback callback )
    throws IllegalArgumentException, DatabaseConnectionFailureException, StoredProcedureException
    {
		if ( procedureName == null || procedureName.length() == 0 )
		{
			throw new IllegalArgumentException( "DatabaseQuerier::executeStoredProcedure()" );
		}
    	
		logDebugMessage( "DatabaseQuerier:executeStoreProcedure() - Executing stored procudure " + procedureName + " with param(s) " + (parameters != null ? parameters.toString() : "[none provided]") );
		
        //********************************************                
        // check that the procedure name is valid
        //********************************************                
        if ( procedureName == null || procedureName.length() == 0 )
        {
            throw new IllegalArgumentException( "DatabaseQuerier::executeStoredProcedure()" );
        }

        Connection connection   = null;
        Collection returnCollection = null;
        String sql             = null;
        CallableStatement stmt  = null;

        try
        {

            //********************************************                
            // obtain a database connection
            //********************************************                
            connection = getSafeConnection();

            //********************************************
            // build the sql string
            //********************************************
            sql = buildSQLForStoredProcedure( procedureName, parameters, true /*result set expected*/) ;
        
            //********************************************
            // prepare the callable statement
            //********************************************
            stmt = prepareCallableStatementHelper( connection, sql );            

            //********************************************
            // now assign the provided parameters to 
            // the statement
            //********************************************
            assignParametersToStatement( stmt, parameters, true /*result set expected*/ );
            
            //********************************************                
            // execute the statement
            //********************************************
            ResultSet rs = executeCallableStatement( stmt );
            
            //********************************************
            // attempt to call the callback interface 
            // to notify of the obtained result set
            //********************************************
            if ( callback != null && rs != null )
            {
                try
                {
                    //********************************************
                    // IResultSetCallback notification
                    //********************************************
                    returnCollection = callback.notifyResultSet( rs, procedureName );

                    //********************************************
                    // close the result set
                    //********************************************
                    DBUtility.CloseResultSet( rs );
                }
                catch ( ResultSetCallbackException rscbe )
                {                   
                    DBUtility.CloseStatement( stmt );            
                    // unconditionally release the connection
//                    releaseConnection();
                    
                    throw new StoredProcedureException( "DatabaseQuerier:executeStoredProcedure() - " + outputExceptionMessage( procedureName, parameters  ) + " : " + rscbe.getMessage(), null );
                }
            }
        }
        catch ( SQLException exc )
        {
            String sExcMessage = "DatabaseQuerier::executeStoredProcedure - " + outputExceptionMessage( procedureName, parameters ) + " : " + exc;
            
            throw new StoredProcedureException( sExcMessage, exc );
        }
        finally
        {
            DBUtility.CloseStatement( stmt );            
            // unconditionally release the connection
//            releaseConnection();
        }

        return( returnCollection );
    }   

	/**
	 * executes a stored procedure which returns a Collection of objects, according to the
	 * out parameters assigned to the Collection of Parameters.
	 * <p>
	 * @param		procedureName		name of stored procedure
	 * @param		parameters			in/out Parameters, can be null
	 * @return		Collection          data according to provided out Parameters.
	 * @exception	IllegalArgumentException 
	 * @exception   DatabaseConnectionFailureException
	 * @exception	StoredProcedureException
	 */
    public Collection executeStoredProcedure( String procedureName, Collection parameters )
    throws IllegalArgumentException, DatabaseConnectionFailureException, StoredProcedureException
    {
        //********************************************                
        // check that the procedure name is valid
        //********************************************
        if ( procedureName == null || procedureName.length() == 0 )
        {
            throw new IllegalArgumentException( "DatabaseQuerier::executeStoredProcedure()" );
        }

		logDebugMessage( "DatabaseQuerier:executeStoreProcedure() - Executing stored procudure " + procedureName + " with param(s) " + (parameters != null ? parameters.toString() : "[none provided]") );

        Collection returnValues     = null;           
        Parameter param         = null;
        CallableStatement stmt  = null;
        String sql             = null;
        Connection connection   = null;

        try
        {
            //********************************************                
            // obtain a safe connection
            //********************************************                
            connection = getSafeConnection();

            //********************************************                
            // build the sql string
            //********************************************                
            sql = buildSQLForStoredProcedure( procedureName, parameters, false /*no result set*/);

            //********************************************
            // prepare the callable statement
            //********************************************
            stmt = connection.prepareCall( sql );

            //******************************************************************
            // assign a cursor name for those interested in associated
            // this statement with subsequent statements within this 
            // connection context
            //******************************************************************
//			stmt.setCursorName( procedureName );

            //********************************************
            // assign parameters to the statement
            //********************************************
            assignParametersToStatement( stmt, parameters, false /*no result set*/  );

            //********************************************
            // execute the statement
            //********************************************
            stmt.execute();
            
            returnValues = new ArrayList();

            //********************************************
            // determine if there are any out parameters 
            // which need to be returned
            //********************************************
            if ( parameters != null && parameters.size() > 0 )
            {
                int index 		= 1;                          
				Iterator iter 	= null;
				
                //********************************************
                // iterate over each of the provided params
                //********************************************
                while( iter.hasNext() )
                {
                    param = (Parameter)iter.next();

                    if ( param.isOutParameter() )
                    {
                        //********************************************
                        // populate the return value Collection
                        //********************************************
                        returnValues.add( stmt.getObject( index ) );  
                    }

                    // Increment counter
                    index++;                    
                }
            }
        }
        catch ( SQLException exc )
        {
            throw new StoredProcedureException( "DatabaseQuerier::executeStoredProcedure - " + outputExceptionMessage( procedureName, parameters ) + " - " + exc.getMessage(), exc );
        }
        finally
        {
            DBUtility.CloseStatement( stmt );
            // unconditionally release the connection
//            releaseConnection();
        }

        return( returnValues );
    }   

	/**
	 * Helper method used to return the JDBC connection to use.
	 * <p>
	 * @return		Connection
	 * @exception	DatabaseConnectionFailureException
	 */
    public Connection getSafeConnection()
    throws DatabaseConnectionFailureException       
    {
        //******************************************************
        // reset the connection attempts
        //******************************************************
        theConnectionAttempts = 0;

        //******************************************************
        // invoke the safe connection helper
        //******************************************************
        return( getSafeConnectionHelper() );
    }

	/**
	 * Used to verify that a valid JDBC connection exists. 
	 * @return boolean
	 */
    public boolean verifyConnection()
    {
        boolean bVerify = false;

        try
        {
            //******************************************************
            // if there is a connection object and the connection 
            // is not closed, assume the connection is valid
            //******************************************************
            if ( theConnection != null && theConnection.isClosed() == false )
            {
                bVerify = true;
            }
        }
        catch ( Exception e )
        {
            logErrorMessage( "DatabaseQuerier::verfiyConnection - connection cannot be verified." );
        }

        return( bVerify );
    }

    /**
     * notify that the connection is done being used
     */
    public void releaseConnection()
    {
//        ConnectionPoolManagerFactory.getObject().releaseConnection( this );        
    }
    
///////////////////////////////////
// protected/private methods
///////////////////////////////////

  /**
	 * Helper method to help figure out which overloaded makeConnection method
	 * to call.  If the connection fails on the first pool ID, a "1" is
	 * appended to the pool ID and the connection is attempted again.
	 * <p>
	 * @return      Connection
	 * @exception   DatabaseConnectionFailureException
	 */
	protected Connection makeConnection()
	throws DatabaseConnectionFailureException
	{
		Connection connection       = null;
        
		try
		{
			String jndiDataSourceName      = getJNDIDataSourceName();
            
			if ( jndiDataSourceName != null )  // use it
			{
				// acquire the initial context through the Framework
				String theUserID                  = getUserID();
				String thePassword                = getPassword();
                
				// determine if the datasource uses pooling
				boolean bUseDataSourcePooling   = isUsingDataSourcePooling();
                
				if ( bUseDataSourcePooling == false )   // doesn't use pooling
				{              
					connection =  getConnectionFromDataSource( getDataSourceFromCache( jndiDataSourceName ), theUserID, thePassword );	                	  	               					
				}
				else // use pooling
				{
					connection =  getConnectionFromPoolableDataSource( getDataSourceFromCache( jndiDataSourceName ), theUserID, thePassword );					
				}
                
				if ( connection != null )
				{
					// add it to the datasource collection for reference
					dataSourceMappings.add( connection );
				}
               
				if ( connection == null )
				{
					logDebugMessage( "DatabaseQuerier:makeConnection() - Using a datasource, the connection returned is null." ); 
					throw new DatabaseConnectionFailureException( "DatabaseQuerier:makeConnection() - Using a datasource, the connection returned is null." );
				}                                      
			}
			else    // create it dynamically
			{           
				Class.forName( getDatabaseDriverName() ).newInstance();                     
				connection = DriverManager.getConnection( getDatabaseURL(), getUserID(), getPassword() );               
                
				if ( connection != null )
				{
					connection.setAutoCommit( false );
				}
				else
				{
					logDebugMessage( "DatabaseQuerier:makeConnection() - DriverManager.getConnection() returned null." );
					throw new DatabaseConnectionFailureException( "DatabaseQuerier:makeConnection() - DriverManager.getConnection() returned null." );
				}
			}
		}
		catch ( Exception e1 )
		{
			logDebugMessage( "DatabaseQuerier:makeConnection() - " + e1 );
			throw new DatabaseConnectionFailureException( "DatabaseQuerier:makeConnection() - " + e1 );
		}

		return( connection );
	}

   /**
	* Given a connection and the formatted SQL statement, prepares a CallableStatement.
	* <p>
	* @param               connection
	* @param               sql
	* @return              CallableStatement
	* @exception           SQLException
	*/
	abstract protected CallableStatement prepareCallableStatement( Connection connection, String sql )
    throws SQLException;

	/**
	 * Helper method used to return the connection from a non-poolable datasource.  
	 * <p>
	 * <b><i>Note:</i></b> this method is to accomodate the fact that not all JDBC vendors support
	 * the correct JDBC interface with respect to a DataSource.
	 * <p>
	 * @param		datasource		the data source
	 * @param		userID			getting the connection from the datasource may require the user id
	 * @param		password		getting the connection from the datasource may require the password
	 * @return		java.sql.Connection
	 * @exception 	java.sql.SQLException
	 */
	protected java.sql.Connection getConnectionFromDataSource( Object datasource, String userID, String password )
	throws java.sql.SQLException
	{
		java.sql.Connection connection = null;
		DataSource ds = (DataSource)datasource;
                    
		// determine which of the overloaded getConnection() methods
		// to call, depending upon if a userid is provided or not...
		if ( userID == null || userID.length() == 0 )
			connection = ds.getConnection(); 
		else
			connection = ds.getConnection( userID, password ); 
			
		return connection;						
	}

	/**
	 * Helper method used to return the connection from a poolable datasource.
	 * <p>
	 * <b><i>Note:</i></b> this method is to accomodate the fact that not all JDBC vendors support
	 * the correct JDBC interface with respect to a DataSource.
	 * <p>
	 * @param		datasource		the data source
	 * @param		userID			getting the connection from the datasource may require the user id
	 * @param		password		getting the connection from the datasource may require the password
	 * @return		java.sql.Connection
	 * @exception 	java.sql.SQLException
	 */
	protected java.sql.Connection getConnectionFromPoolableDataSource( Object datasource, String userID, String password )
	throws java.sql.SQLException
	{
		java.sql.Connection connection = null;		
		ConnectionPoolDataSource ds = (ConnectionPoolDataSource)datasource;
                    
		// determine which of the overloaded getConnection() methods
		// to call, depending upon if a userid is provided or not...
		if ( userID == null || userID.length() == 0 )
			connection = ds.getPooledConnection().getConnection(); 
		else
			connection = ds.getPooledConnection( userID, password ).getConnection(); 
			
		return connection;						
	}
	
	
	/**
	 * Attempts to return the current database connection.  
	 * If the connection is no longer valid, a new connection to the same 
	 * datasource is attempted
	 * 
	 * @return	    Connection
	 * @exception	DatabaseConnectionFailureException
	 */ 
	protected Connection getSafeConnectionHelper()
	throws DatabaseConnectionFailureException               
	{
		//******************************************************
		// increment the connection attempts by 1
		//******************************************************
		theConnectionAttempts++;

		try
		{
			//******************************************************
			// if the connetion cannot be verified, try to establish
			// another one
			//******************************************************
			if ( verifyConnection() == false )
			{
				theConnection = makeConnection();                        
			}
		}
		catch ( DatabaseConnectionFailureException e )
		{
			//******************************************************
			// if the connection attempts has not exceeded the max
			//******************************************************
			if ( theConnectionAttempts < maxConnectionAttempts )
				getSafeConnectionHelper();  // try again
			else
				throw new DatabaseConnectionFailureException( e.getMessage() ); 
		}

		return( theConnection );     
	}

    /**
     * Helper method for building a SQL string for a stored procedure
     * <p>
     * @param   procedureName	stored procedure name
     * @param   parameters		in/out com.framework.common.parameter.Parameters
     * @param   resultsetFlag  	flag to determine if a resultset will be returned
     */
    protected String buildSQLForStoredProcedure( String procedureName, Collection parameters, boolean resultsetFlag )
    {
        // sample: "BEGIN single_cursor(?, ?); END;"

        StringBuffer sql       = new StringBuffer( "BEGIN " );     

        //******************************************************
        // append the procedure name
        //******************************************************
        sql.append( procedureName );

        //******************************************************
        // if there are parameters, or a result set is an
        // out parameter of this stored procedure
        //******************************************************
        if ( (parameters != null && parameters.size() > 0) || resultsetFlag == true )
        {
            sql.append( "(" );

            int size = 0;

            if ( parameters != null )
                size = parameters.size();

            //******************************************************
            // for each parameter, add a ?
            //******************************************************
            for ( int i = 0; i < size; )
            {
                sql.append( "?" );
                i++;

                //**********************************					
                // don't append one for the last one
                //**********************************					
                if ( i < size )
                {
                    sql.append( ", " );
                }
            }

            //******************************************************
            // if a result set is expected as an out paramter,
            // add an extra ?
            //******************************************************
            if ( resultsetFlag == true )
            {
                if ( size > 0 )
                    sql.append( ",?);" );
                else
                    sql.append( "?);" );    
            }
            else
            {
                sql.append( ");" );    
            }                   
        }

        sql.append( "END;" );    

        return( sql.toString() );
    }

	/**
	 * Assign in and out parameters to a statement
	 * <p>
	 * @param       stmt   				SQL statement
	 * @param       parameters			input/output parameters
	 * @param       resultsetFlag       result set parameter being used indicator
	 */
    protected void assignParametersToStatement( CallableStatement stmt, Collection parameters, boolean resultsetFlag  )
    throws SQLException
    {

        //******************************************************
        // iterate through the parameters Collection and assign
        // each parameter to the callable statement
        //******************************************************
        if ( parameters != null && parameters.size() > 0 )
        {
            int index       = 1;
            Parameter param = null;
            Object o        = null;
            Iterator iter	= parameters.iterator();
            
            //******************************************************
            // if a result set is a return value for this stored procedure,
            // it will have already been assigned, so start at index 2
            //******************************************************
            if ( resultsetFlag )
                index = 2;

            //******************************************************		        
            // iterate through each element, and assign to the 
            // statement according to the element being an in or 
            // and out parameter
            //******************************************************
          	while( iter.hasNext() )
            {
                param = (Parameter)iter.next();

				if ( param.isInParameter() )
				{
					o = param.getParameter();

					if ( o != null )
						stmt.setObject( index, param.getParameter(), param.getType() );
					else
						stmt.setNull( index, param.getType() );
						//stmt.setObject( index, param.getParameter(), java.sql.Types.NULL );                    	
				}
				else    // out parameter
                {
                    stmt.registerOutParameter( index, param.getType() );
                }

                index++;
            }
        }
    }

    /**
    * This method is used to determine if we should get the statement from
    * a cache of CallableStatement's, or delegate to the sub-class and
    * call it's implementation of prepareCallableStatement(....)
    * <p>
    * @param			connection			JDBC connection
    * @param            sql					SQL statement
    * @return           CallableStatement	
    * @exception        SQLException
    */
    protected CallableStatement prepareCallableStatementHelper( Connection connection, String sql )
    throws SQLException
    {
        CallableStatement cs = prepareCallableStatement( connection, sql );

        return( cs );
    }

    /**
     * Helper method used to return the ResultSet after executing a stored procedure.
     * Overload this method if special handling is required in order to obtain the Result set
     * <p>
     * @param       stmt			CallableStateent to execute on
     * @return      ResultSet
     * @exception   SQLException
     */
    protected ResultSet executeCallableStatement( CallableStatement stmt )
    throws SQLException
    {
        ResultSet rs = null;
        
        if ( stmt != null )
        {
            rs = stmt.executeQuery();
        }    
        
        return( rs );
    }

	/**
	 * Helper used to format the output message when throwing an exception.
	 * <p>
	 * @param 	statement		SQL statement
	 * @param 	parameters		in/out com.framework.common.parameter.Parameters
	 * @return	formatted String
	 */
    protected String outputExceptionMessage( String statement, Collection parameters )
    {
        StringBuffer buffer = new StringBuffer( "\nSQL Statement: " );
        buffer.append( statement );
        
        buffer.append( "\nInput Params: " );
        
        if ( parameters != null )
            buffer.append( parameters.toString() );
        else
            buffer.append( parameters );
            
        return( buffer.toString() + "\n");            
    }
    
    /**
     * Locate the datasource usig the ServiceLocator.
     * <p>
     * @param 	jndiName		key of datasource to retreive from JNDI
     * @return					the datasource
     * @throws NamingException
     */
    protected Object getDataSourceFromCache( String jndiName )
    throws NamingException
    {	
    	return( ServiceLocator.getInstance().lookup( jndiName ) );
    }
    
    
// ********************************
// attributes
// ********************************
	
	static protected Collection dataSourceMappings = new java.util.ArrayList();
	
    /**
     * the encapsulated JDBC connection
     */ 
    protected Connection theConnection   = null;

    /**
     * number of connection attempts for a single cycle of attemps
     */
    protected int theConnectionAttempts  = 0;

    /**
     * max. number of connection attempts for a single cycle of attempts
     */
    protected int maxConnectionAttempts   = 1;

    /**
     * static database driver parameter name
     */
    private final static String     DatabaseDriverParameter = "driver";

    /**
     * static database driver URL parameter name
     */
    private final static String     DatabaseURLParameter    = "url";

    /**
     * static database user id parameter name
     */
    private final static String     UserID    = "user";

    /**
     * static database password parameter name
     */
    private final static String     Password    = "password";

    /**
     * static parameter used to indicate the connection originates
     * from a DataSource in JNDI by the name associated with the parameter
     */
    private final static String     JNDIDataSourceName = "JNDIDataSourceName";

    /**
     * static parameter used to indicate the if the DataSource in JNDI uses pooling
     */
    private final static String     UsingDataSourcePooling = "usingDataSourcePooling";    
    
    /**
     * maximum connection attempts property key
     */
    final private static String MAX_CONNECTION_ATTEMPTS =  "maxConnectionAttempts";

}

