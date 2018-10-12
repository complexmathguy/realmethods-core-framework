/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.objectpool.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.framework.common.exception.ObjectCreationException;

import oracle.jdbc.driver.OracleTypes;

/**
 * Base class for all Oracle database connection types that are pooled in the Connection Pool.
 * <p>
 * @author    realMethods, Inc.
 */
public class OracleConnectionImpl extends DatabaseQuerier
{
    
//****************************************************
// Public Methods
//****************************************************

    /**
     * default constructor - use the static public create()
     */
    public OracleConnectionImpl()
    {
        super(); 
    }

// DatabaseQuerier overloads

    /**
     * Returns the name of the JDBC driver name. If not provided, returns
     * oracle.jdbc.driver.OracleDriver.
     * <p>
     * @return      String
     */
    public String getDatabaseDriverName()
    {
        String sDriverName = super.getDatabaseDriverName();

        if ( sDriverName == null )
            sDriverName = "oracle.jdbc.driver.OracleDriver";

        return( sDriverName );            
    }

    /**
     * Returns the URL of the database.  If not provided, returns
     * jdbc:oracle:oci8:@.
     * <p>
     * @return      String
     */
    public String getDatabaseURL()
    {
        String sURL = super.getDatabaseURL();

        if ( sURL == null )
            sURL = "jdbc:oracle:oci8:@";

        return( sURL );
    }

// overloads from DatabaseQuerier

   /**
	* Given a connection and the formatted SQL statement, prepares a CallableStatement.
	* <p>
	* @param               connection
	* @param               sql
	* @return              CallableStatement
	* @exception           SQLException
	*/
    protected CallableStatement prepareCallableStatement( Connection connection, String sql )
    throws SQLException
    {
        CallableStatement stmt = connection.prepareCall( sql );
        
        //********************************************
        // the ResultSet is always the first 
        // parameter for the stored procedure
        //********************************************
        stmt.registerOutParameter(1, OracleTypes.CURSOR);

        return( stmt );
    }

	/**
	 * helper method used to return the connection from a non-poolable datasource
	 * <p>
	 * 03-12-2004 - First delegate to the base class, where javax.sql.DataSource
	 * is expected.  If it fails, such is the case for early Oracle 8i implementations
	 * the datasource is cast to a oracle.jdbc.pool.OracleDataSource.
	 * <p>
	 * @param		datasource		poolable data source
	 * @param		userID			may be required to get the pooled connection
	 * @param		password		may be required to get the pooled connection
	 * @return		java.sql.Connection
	 * @exception 	java.sql.SQLException
	 */
	protected java.sql.Connection getConnectionFromDataSource( Object datasource, String userID, String password )
	throws java.sql.SQLException
	{
		java.sql.Connection connection = null;
		
		try
		{
			connection = super.getConnectionFromDataSource( datasource, userID, password );		
		}
		catch( ClassCastException exc )
		{
			oracle.jdbc.pool.OracleDataSource ds = (oracle.jdbc.pool.OracleDataSource)datasource;
                    
			// determine which of the overloaded getConnection() methods
			// to call, depending upon if a userid is provided or not...
			if ( userID == null || userID.length() == 0 )
				connection = ds.getConnection(); 
			else
				connection = ds.getConnection( userID, password ); 
		}
					
		return connection;						
	}
	
	/**
	 * Helper method used to return the connection from a poolable datasource
	 * <p>
	 * 03-12-2004 - First delegate to the base class, where javax.sql.ConnectionPoolDataSource
	 * is expected.  If it fails, such is the case for early Oracle 8i implementations
	 * the datasource is cast to a oracle.jdbc.pool.OracleConnectionPoolDataSource.
	 * <p>
	 * @param		datasource		poolable data source
	 * @param		userID			may be required to get the pooled connection
	 * @param		password		may be required to get the pooled connection
	 * @return		java.sql.Connection
	 * @exception 	java.sql.SQLException
	 */
	protected java.sql.Connection getConnectionFromPoolableDataSource( Object datasource, String userID, String password )
	throws java.sql.SQLException
	{
		java.sql.Connection connection = null;		
		
		try
		{
			super.getConnectionFromPoolableDataSource( datasource, userID, password );
		}
		catch( ClassCastException exc )
		{		
			oracle.jdbc.pool.OracleConnectionPoolDataSource ds = (oracle.jdbc.pool.OracleConnectionPoolDataSource)datasource;
                    
			// determine which of the overloaded getConnection() methods
			// to call, depending upon if a userid is provided or not...
			if ( userID == null || userID.length() == 0 )
				connection = ds.getPooledConnection().getConnection(); 
			else
				connection = ds.getPooledConnection( userID, password ).getConnection(); 
		}
			
		return connection;						
	}
    /**
     * Helper method used to return the ResultSet after executing a stored procedure.
     * Special conditions required to obtain ResultSet from an Oracle Stored Procedure.
     * <p>
     * @param       stmt		CallabledStatement to execute
     * @return      ResultSet
     * @exception   SQLException
     */
    protected ResultSet executeCallableStatement( CallableStatement stmt )
    throws SQLException
    {
        ResultSet rs = null;
        
        if ( stmt != null )
        {
            stmt.executeQuery();
            // first output parameter is the result set, by convention
            rs = (ResultSet)stmt.getObject(1);
        }    
        
        return( rs );
    }


// helper methods

    /**
     * Factory helper method for creation.
     * <p>
     * @return	    OracleConnectionImpl
     * @exception	ObjectCreationException 		
     */
    static public OracleConnectionImpl create()
    throws ObjectCreationException
    {
    	try
    	{ 
    		return( new OracleConnectionImpl() );
    	}
    	catch( Throwable exc )
    	{
			throw new ObjectCreationException( "OracleConnectionImpl:create() - " + exc, exc );
    	}
    }

}

/*
 * Change Log:
 * $Log: OracleConnectionImpl.java,v $
 */
