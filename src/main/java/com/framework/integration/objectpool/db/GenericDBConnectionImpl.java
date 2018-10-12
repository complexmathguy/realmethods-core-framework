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
import java.sql.SQLException;

import java.util.Collection;

import com.framework.common.exception.ObjectCreationException;

/**
 * Generic class for all database connection types that are pooled 
 * in the Connection Pool.  Those database that do not support
 * stored procedures, or required specially semantics to be encapsulated
 * will make use of this class
 *
 * <p>
 * @author    realMethods, Inc.
 */
public class GenericDBConnectionImpl extends DatabaseQuerier
{
    
//****************************************************
// Public Methods
//****************************************************
    /**
     * default constructor - use the static public void create()
     */
    public GenericDBConnectionImpl()
    {
        super(); 
    }

// DatabaseQuerier overloads

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
        stmt.registerOutParameter(1, java.sql.Types.INTEGER );

        return( stmt );
    }


    /**
     * helper method for building a SQL string for a 
     * stored procedure
     *
     * @param   procedureName      	procedure name
     * @param   parameters			input parameters
     * @param   resultsetFlag     	Flag to determine if a resultset will be returned
     */
    protected String buildSQLForStoredProcedure( String procedureName, Collection parameters, boolean resultsetFlag )
    {
        throw new RuntimeException( "GenericDBConnectionImpl:buildSQLForStoredProcedure() - method not supported." );
    }

// helper methods

    /**
     * Factory helper method for creation.
     * <p>
     * @return	    GenericDBConnectionImpl
     * @exception	ObjectCreationException 		
     */
    static public GenericDBConnectionImpl create()
    throws ObjectCreationException
    {
    	try
    	{ 
    		return( new GenericDBConnectionImpl() );
    	}
    	catch( Throwable exc )
    	{
			throw new ObjectCreationException( "GenericDBConnectionImpl:create() - " + exc, exc );
    	}
    }

}

/*
 * Change Log:
 * $Log: GenericDBConnectionImpl.java,v $
 */
