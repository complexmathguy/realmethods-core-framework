/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.objectpool.db;

//************************************
// Imports
//************************************
import java.sql.Connection;

import java.util.Collection;

import com.framework.common.exception.DatabaseConnectionFailureException;
import com.framework.common.exception.ExecuteStatementException;
import com.framework.common.exception.SelectStatementException;
import com.framework.common.exception.StoredProcedureException;

/** 
 * Base interface for all database related queriers.
 * <p>
 * @author    realMethods, Inc.
 */
public interface IDatabaseQuerier extends IResultSetCallback
{
	
//****************************************************
// Public Methods
//****************************************************

    /**
     * Returns the database connection currently in use.
     * @return      Connection
     */
    public Connection getConnection();

    /**
     * Returns the name of the JDBC driver name.
     * @return      String
     */
    public String getDatabaseDriverName();

    /**
     * Returns the URL of the database.
     * @return      String
     */
    public String getDatabaseURL();


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
    throws IllegalArgumentException, DatabaseConnectionFailureException, SelectStatementException;       

    /**
     * Used to execute update, insert, or delete related SQL statements
     * which may result in a row count being returned
     * 
     * @param		statement	SQL statement
     * @param		parameters	com.framework.common.parameter.Parameter instances
     * @return		rowCount
     * @exception	IllegalArgumentException
     * @exception   DatabaseConnectionFailureException
     * @exception	ExecuteStatementException
     */
    public int executeStatement( String statement, Collection parameters )
    throws IllegalArgumentException, DatabaseConnectionFailureException, ExecuteStatementException;     

    /**
     * Used to execute a select statement that wil result in returning a ResultSet. The resultset is handed over to the provided
     * IResultSetCallback handler, to turn the rows of data into a Collection of
     * more useful objects.
     * 
     * @param		selectStatement		SQL select statement
     * @param      	parameters			com.framework.common.parameter.Parameter instances
     * @param       callback			transforms a resultset into a Collection of more useful objects
     * @return							Collection returnd by the callback
     * @exception	IllegalArgumentException
     * @exception  DatabaseConnectionFailureException
     * @exception	SelectStatementException
     */
    public Collection executeSelectStatement(  String selectStatement, 
                                                Collection parameters,
                                                IResultSetCallback callback )
    throws IllegalArgumentException, DatabaseConnectionFailureException, SelectStatementException;

    /**
     * executes a stored procedure which makes use of a result set.  The resultset is handed over to the provided
     * IResultSetCallback handler, to turn the rows of data into a Collection of
     * more useful objects.
     * 
     * @param		procedureName
     * @param		parameters	        Parameter objects - can be null
     * @param		callback			transforms a resultset into a Collection of more useful objects
     * @return							Colletion returned by the callback
     * @exception	IllegalArgumentException
     * @exception   DatabaseConnectionFailureException
     * @exception	StoredProcedureException
     */
    public Collection executeStoredProcedure(  String procedureName, 
                                                Collection parameters, 
                                                IResultSetCallback callback )
    throws IllegalArgumentException, DatabaseConnectionFailureException, StoredProcedureException;

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
    throws IllegalArgumentException, DatabaseConnectionFailureException, StoredProcedureException;

    /**
     * Helper method used to return the JDBC connection to use.
     * <p>
     * @return		Connection
     * @exception	DatabaseConnectionFailureException
     */
    public Connection getSafeConnection()
    throws DatabaseConnectionFailureException;      

    /**
     * Used to verify that a valid JDBC connection exists. 
     * @return boolean
     */
    public boolean verifyConnection();

    /**
     * notify that the connection is done being used.
     */
    public void releaseConnection();

}

/*
 * Change Log:
 * $Log: IDatabaseQuerier.java,v $
 */
