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
 * Base class for all MSSQLServer database connection types that are pooled 
 * in the Connection Pool.
 * <p>
 * @author    realMethods, Inc.
 */
public class MSSQLServerConnectionImpl extends DatabaseQuerier
{
    
//****************************************************
// Public Methods
//****************************************************
    /**
     * default constructor - use the static public create()
     */
    public MSSQLServerConnectionImpl()
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
     * @param   parameters      	input parameters
     * @param   bResultsetFlag		Flag to determine if a Resultset will be returned from executing the query
     */
    protected String buildSQLForStoredProcedure( String procedureName, Collection parameters, boolean bResultsetFlag )
    {
        StringBuffer sSQL       = new StringBuffer( "" );     

        // Depending on if there is a resultset returned
        // If a resultset is returned
        if ( bResultsetFlag == true )
        {
            sSQL.append("{? = call ");
        }
        // Else no resultset
        else
        {
            sSQL.append("{call ");
        }

        //******************************************************
        // append the procedure name
        //******************************************************
        sSQL.append( procedureName );

        //******************************************************
        // if there are parameters, or a result set is an
        // out parameter of this stored procedure
        //******************************************************
        if ( (parameters != null && parameters.size() > 0) || bResultsetFlag == true )
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

            //******************************************************
            // if a result set is expected as an out parameter,
            // add an extra ?
            //******************************************************
            sSQL.append( ")}" );    
        }

        return( sSQL.toString() );
    }

// helper methods

    /**
     * Factory helper method for creation.
     * <p>
     * @return	    MSSQLServerConnectionImpl
     * @exception	ObjectCreationException 		
     */
    static public MSSQLServerConnectionImpl create()
    throws ObjectCreationException
    {
    	try
    	{ 
    		return( new MSSQLServerConnectionImpl() );
    	}
    	catch( Throwable exc )
    	{
			throw new ObjectCreationException( "MSSQLServerConnectionImpl:create() - " + exc, exc );
    	}
    }

}

/*
 * Change Log:
 * $Log: MSSQLServerConnectionImpl.java,v $
 */
