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
import java.util.Iterator;

import com.framework.common.exception.ObjectCreationException;

import com.framework.common.parameter.Parameter;

/**
 * Base class for all Cloudscape database connection types 
 * that are pooled in the Connection Pool.
 *
 * <p>
 * @author    realMethods, Inc.
 */
public class CloudscapeConnectionImpl extends DatabaseQuerier
{
//****************************************************
// Public Methods
//****************************************************

    /**
     * default constructor - use the static public create()
     */
    public CloudscapeConnectionImpl()
    {
        super(); 
    }

// DatabaseQuerier overloads

    /**
     * Returns the name of the JDBC driver name.
     * @return      String
     */
     
    public String getDatabaseDriverName()
    {
        String sDriverName = super.getDatabaseDriverName();

        if ( sDriverName == null )
            sDriverName = "com.cloudscape.core.JDBCDriver";

        return( sDriverName );            
    }
   
    
    /**
     * Returns the URL of the database.  If not provided, returns
     * jdbc:cloudscape:CloudscapeDB;create=true
     * @return      String
     */
     
    public String getDatabaseURL()
    {
        String sURL = super.getDatabaseURL();

        if ( sURL == null )
            sURL = "jdbc:cloudscape:CloudscapeDB";

        sURL = sURL + ";create=true";
        
        return( sURL );
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
        return( connection.prepareCall( sql ) );
    }


    /**
     * The sub-class must handle the acquisition of a result set.
     * <p>
     * @param               stmt
     * @return              ResultSet
     * @exception           SQLException
     */
    protected ResultSet retrieveResultSet( CallableStatement stmt )
    throws SQLException
    {
        return( (ResultSet)stmt.getResultSet() );
    }

    /**
     * Assign in and out parameters to a statement.
     * <p>
     * @param       stmt			SQL statement
     * @param       parameters		in/out parameters
     * @param       resultsetFlag   result set parameter being used indicator - ignored
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
                        stmt.setObject( index, o );
                    else
                        stmt.setNull( index, param.getType() );
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
     * helper method for building a SQL string for a 
     * stored procedure.
     * <p>
     * @param   procedureName  	procedure name
     * @param   parameters    	input/output parameters
     * @param   resultsetFlag  	Flag to determine if a resultset will be returned
     */
    protected String buildSQLForStoredProcedure( String procedureName, Collection parameters, boolean resultsetFlag )
    {
        StringBuffer sql       = new StringBuffer( "EXECUTE STATEMENT " );     
        sql.append( procedureName );
        
        return( sql.toString() );
    }

// helper methods

    /**
     * Factory helper method for creation.
     * <p>
     * @return	    CloudscapeConnectionImpl
     * @exception	ObjectCreationException 		
     */
    static public CloudscapeConnectionImpl create()
    throws ObjectCreationException
    {
    	try
    	{ 
    		return( new CloudscapeConnectionImpl() );
    	}
    	catch( Throwable exc )
    	{
			throw new ObjectCreationException( "CloudscapeConnectionImpl:create() - " + exc, exc );
    	}
    }
}

/*
 * Change Log:
 * $Log: CloudscapeConnectionImpl.java,v $
 */
