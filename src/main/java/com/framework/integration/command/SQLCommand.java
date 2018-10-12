/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.command;

import java.sql.ResultSet;

import java.util.Collection;

import com.framework.common.exception.CommandExecutionException;
import com.framework.common.exception.ResultSetCallbackException;

import com.framework.common.message.IFrameworkMessage;
import com.framework.integration.objectpool.db.IDatabaseQuerier;

import com.framework.integration.task.IFrameworkTask;

/**
 * Base class for all SQL related Commands
 * <p>
 * @author          realMethods, Inc.
 */
public abstract class SQLCommand extends ResourceConnectionCommand
    implements ISQLCommand
{
//****************************************************
// Public Methods
//****************************************************

//  ISQLCommand implementations

    /**
     * returns the database conneciton
     * @return      IDatabaseQuerier
     */
    public IDatabaseQuerier getDatabaseQuerier()
    {
        return( (IDatabaseQuerier)super.getConnectionInUse() );
    }

    /**
     * callback method to notify of result set for a provided stored
     * procedure name.
     * <p>
     * @param       rs
     * @param       storedProcedureName
     * @return      Collection
     * @exception   ResultSetCallbackException
     */
    public Collection notifyResultSet( ResultSet rs, String storedProcedureName )
    throws ResultSetCallbackException
    {
        /////////////////////////////////////////////////////////////////////////////
        // Note!
        // this should be overloaded, but the DatabaseQuerier implementation handles
        // this, but not in an application useful manner.
        /////////////////////////////////////////////////////////////////////////////

        if ( databaseQuerier != null )
        {
            return( databaseQuerier.notifyResultSet( rs, storedProcedureName ) );
        }
        else
        {
            return( null );
        }            
    }

    /**
     * place where the command is notified to do it's work
     * with respect to the input msg.
     *
     * @param       msg
     * @param       task
     * @exception   CommandExecutionException
     *
     */    
    public void onExecute( IFrameworkMessage msg, IFrameworkTask task )
    throws CommandExecutionException
    {
        try
        {
            databaseQuerier = getDatabaseQuerier();

            // get the SQL 
            String sql         = getSQL( msg );

            // determine if the SQL is select related
            if ( isSelectedRelated() == true )
            {
                // the subclass will have overloaded the notifyResultSet(...) method
                // from IResultCallback interface in order to do something with the
                // returned result set
                databaseQuerier.executeSelectStatement( sql, this );
            }
            else    // an update, insert, or delete
            {
                Collection sqlParams    = getSQLParameters( msg );

                databaseQuerier.executeStatement( sql, sqlParams );
            }
        }
        catch ( Throwable exc )
        {
            throw new CommandExecutionException( "SQLCommand:execute(...) - " + exc, exc );                
        }
    }   

    /**
     * returns a Collection of SQL parameters
     *
     * @param       message
     * @return      parameters
     */
    public Collection getSQLParameters( IFrameworkMessage message )
    {
        return( null );
    }

// must be implemented in subclass

    // public String getConnectionID();    
    // public boolean isSelectedRelated();

// attributes

    /**
     * the db querier interface in use
     */
    protected IDatabaseQuerier databaseQuerier	= null;

}

/*
 * Change Log:
 * $Log: SQLCommand.java,v $
 */
