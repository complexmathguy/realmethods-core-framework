/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/

package com.framework.integration.command;

//************************************
// Imports
//************************************
import com.framework.common.exception.CommandExecutionException;
import com.framework.common.exception.ConnectionAcquisitionException;

import com.framework.common.message.IFrameworkMessage;

import com.framework.integration.objectpool.ConnectionPoolManagerFactory;
import com.framework.integration.objectpool.IConnectionImpl;

import com.framework.integration.task.IFrameworkTask;

/**
 * Base class for all Resource Connection related Command objects
 * <p>
 * @author          realMethods, Inc.
 */
public abstract class ResourceConnectionCommand extends FrameworkCommand
{
//****************************************************
// Public Methods
//****************************************************


    /**
     * returns the associated ConnectionImpl
     *
     * @return      Connection
     */
    public IConnectionImpl getConnectionInUse()
    {
        return( connecctionInUse );
    }

    /**
     * returns the connection identifier
     * @return		String
     */
    abstract public String getConnectionID();
    
    /**
     * execution method
     *
     * @param       msg
     * @param       task  can optionally be null
     * @exception   CommandExecutionException
     * @exception   IllegalArgumentException	thrown if msg is null
     */
    public void execute( IFrameworkMessage msg, IFrameworkTask task )
    throws CommandExecutionException, IllegalArgumentException
    {
        if ( msg == null )
            throw new IllegalArgumentException( "ResourceConnectionCommand:execute() - null msg argument." );

        //get the connection
        try
        {
            connecctionInUse = determineConnection();
        }
        catch( Throwable exc )
        {
            throw new CommandExecutionException( "ResourceConnectionCommand.execute(...) failed to determine the connection", exc );
        }

        if ( connecctionInUse != null )
        {
            // delegate to the base class
            super.execute( msg, task );
        }
        else
        {
            throw new CommandExecutionException( "ResourceConnectionCommand:execute() - unable to obtain a from the ConnectionPool." );
        }            
    }

// helper methods

    /**
     * Figures out how to get a connection from the ConnectionManager.  If the Command is
     * is bound with a Task, acquire the connection via the Task, so it can track it for
     * for a Task scope commit or rollback.  If there is no bound Task, go to the ConnectionManager
     * and get it directly.
     * <p>
     * @return    	IConnectionImpl
     * @exception	ConnectionAcquisitionException
     */
    protected IConnectionImpl determineConnection()
    throws ConnectionAcquisitionException
    {
        IConnectionImpl connectionImpl = null;
        
        // if the command is bound to a Task, get the connection to use via it
        // otherwise
        
        try
        {                
		    if ( getBoundTask() != null ) 
		    {
		        connectionImpl = getBoundTask().getConnection( getConnectionID() );
		    }
		    else // no associated task so go get it ourself
		    {
		        connectionImpl = ConnectionPoolManagerFactory.getObject().getConnection( getConnectionID() );
		    }
        }
        catch( Throwable exc )
        {
        	throw new ConnectionAcquisitionException( "ResourceConnectionCommand:determineConnection() - " + exc, exc );
        }        
                
        return( connectionImpl );
    }
    
// attributes

    /**
     * associated connection
     */
    protected IConnectionImpl connecctionInUse       = null;

}

/*
 * Change Log:
 * $Log: ResourceConnectionCommand.java,v $
 */
