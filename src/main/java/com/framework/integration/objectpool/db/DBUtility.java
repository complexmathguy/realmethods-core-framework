/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.objectpool.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.framework.common.FrameworkBaseObject;

/**
 * Utility class used to provide the db implementations of the Integration Tier with some
 * common helper methods.
 * <p>
 * @author    realMethods, Inc.
 */
public class DBUtility extends com.framework.common.misc.Utility
{

    /**
     * Closes an open JDBC connection.
     * <p>
     * @param		conn
     */
    public static void CloseConnection( Connection conn )
    {
        if ( conn != null )
        {
            try
            {
            	if ( conn.isClosed() == false )
                	conn.close();
				else
					new FrameworkBaseObject().logWarnMessage( "DBUtility.CloseConnection() - connection provided is already closed." );                	
            }
            catch ( Exception exc )
            {
            	new FrameworkBaseObject().logErrorMessage( "DBUtility:CloseConnection() - " + exc );
            }
        }
    }

    /**
     * Closes an open JDBC statement.
     * <p>
     * @param		stmt
     */
    public static void CloseStatement( Statement stmt )
    {
        if ( stmt != null )
        {
            try
            {
                stmt.close();
            }
            catch ( Exception exc )
            {
            	new FrameworkBaseObject().logErrorMessage( "DBUtility:CloseStatement() - " + exc );
            }
        }
    }

    /**
     * Closes an open database result set.
     * <p>
     * @param 	rs
     */ 
    public static void CloseResultSet( ResultSet rs )
    {
        if ( rs != null )
        {
            try
            {
                rs.close();
            }
            catch ( Exception exc )
            {
            	new FrameworkBaseObject().logErrorMessage( "DBUtility:CloseStatement() - " + exc );
            }
        }
    }   	
}   

/*
 * Change Log:
 * $Log:  $
 */
