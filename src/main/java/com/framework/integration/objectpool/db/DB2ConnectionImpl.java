/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.objectpool.db;

//************************************
// Imports
//************************************
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Collection;

import com.framework.common.exception.ObjectCreationException;

/**
 * Base class for all IBM DB2 database connection types that are pooled in the Connection Pool.
 * <p>
 * @author    realMethods, Inc.
 */
public class DB2ConnectionImpl extends DatabaseQuerier
{
    
//****************************************************
// Public Methods
//****************************************************
    /**
     * default constructor - use the static public void create()
     */
    public DB2ConnectionImpl()
    {
        super(); 
    }

// DatabaseQuerier overloads

    /**
     * returns the name of the JDBC driver name.   If not provided as a property,
     * uses COM.ibm.db2.jdbc.app.DB2Driver.
     * <p>
     * @return      String
     */
    public String getDatabaseDriverName()
    {
        String sDriverName = super.getDatabaseDriverName();

        if ( sDriverName == null )
            sDriverName = "COM.ibm.db2.jdbc.app.DB2Driver";

        return( sDriverName );            
    }

    /**
     * Returns the URL of the database.  If not provided as a property,
     * uses, jdbc:db2:@.
     * @return      String
     */
    public String getDatabaseURL()
    {
        String sURL = super.getDatabaseURL();

        if ( sURL == null )
            sURL = "jdbc:db2:@";

        return( sURL );
    }



    /**
     * helper method for building a SQL string for a 
     * stored procedure
     *
     * @param   procedureName   procedure name
     * @param   parameters      input/ouput parameters
     * @param   resultsetFlag  	Flag to determine if a resultset will be returned
     */
    protected String buildSQLForStoredProcedure( String procedureName, Collection parameters, boolean resultsetFlag )
    {
        StringBuffer sSQL       = new StringBuffer( "" );     

        // Depending on if there is a resultset returned
        // If a resultset is returned
//        if ( resultsetFlag == true )
//        {
  //          sSQL.append("{? = call ");
//        }
        // Else no resultset
    //    else
        {
            sSQL.append("CALL ");
        }

        //******************************************************
        // append the procedure name
        //******************************************************
        sSQL.append( procedureName );

        //******************************************************
        // if there are parameters, or a result set is an
        // out parameter of this stored procedure
        //******************************************************
        if ( (parameters != null && parameters.size() > 0) || resultsetFlag == true )
        {
            sSQL.append( "(" );

            int size = 0;

            if ( parameters != null )
                size = parameters.size();

            //******************************************************
            // for each parameter, add a ?
            //******************************************************
            for ( int i = 0; i < size; )
            {
                sSQL.append( "?" );
                i++;

                //**********************************					
                // don't append one for the last one
                //**********************************					
                if ( i < size )
                {
                    sSQL.append( ", " );
                }
            }

            sSQL.append( ")" );    
        }
        
        return( sSQL.toString() );
    }

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
        
        return( stmt );
    }

    /**
     * assign in and out parameters to a statement
     *
     * @param       stmt   				SQL statement
     * @param       parameters			input/output parameters
     * @param       resultsetFlag       result set parameter being used indicator
     */
    protected void assignParametersToStatement( CallableStatement stmt, Collection parameters, boolean resultsetFlag  )
    throws SQLException
    {
    	// force result parameter to always use false...
        super.assignParametersToStatement( stmt, parameters, false );
    }
    
    /**
     * Helper method used to return the ResultSet after executing a stored procedure.
     * Special conditions required to obtain ResultSet from an DB2 Stored Procedure.
     * <p>
     * @param       stmt
     * @return      ResultSet
     * @exception   SQLException
     */
    protected ResultSet executeCallableStatement( CallableStatement stmt )
    throws SQLException
    {
        return( super.executeCallableStatement( stmt ) );
    }


// helper methods

    /**
     * Factory helper method for creation.
     * <p>
     * @return	    DB2ConnectionImpl
     * @exception	ObjectCreationException 		
     */
    static public DB2ConnectionImpl create()
    throws ObjectCreationException
    {
    	try
    	{ 
    		return( new DB2ConnectionImpl() );
    	}
    	catch( Throwable exc )
    	{
			throw new ObjectCreationException( "DB2ConnectionImpl:create() - " + exc, exc );
    	}
    }

}

/*
 * Change Log:
 * $Log: DB2ConnectionImpl.java,v $
 * Revision 1.2  2003/08/05 12:16:17  tylertravis
 * - Updated header language to LGPL
 * - Improved exception handling globally
 * - Modified member variables to conform to Java Beans std.
 *
 * Revision 1.1.1.1  2003/07/05 03:05:41  tylertravis
 * initial sourceforge cvs revision
 *
 */
