/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.objectpool;

//***********************************
// Imports
//***********************************
import java.util.Map;

import com.framework.common.exception.ConnectionActionException;
import com.framework.common.exception.InitializationException;
import com.framework.common.exception.ObjectCreationException;

/**
 * Base class for all connection types that are pooled in a ConnectionPool.
 *
 * <p>
 * @author    realMethods, Inc.
 */
abstract public class ConnectionImpl 
    extends ConnectionImplMBean
{

// constructors

    /** 
     * Base constructor.
     */
    public ConnectionImpl()
    {
        super();
    }   


// IConnectionImpl implementations

    /** 
     * Initializes and makes a connection to the connection source.
     * This base implementation does not make the actual connection.
     * This method must be overriden in the subclasses.
     * <p>
     * @param	name 	The pool that this connection is associated with.
     * @param  	props 	Map of properties needed to create the connection.
     * 					Actual values are dependent on the associated type of connection.
     * @exception IllegalArgumentException Thrown if the props is null.
     * @exception InitializationException 
     */
    public void initialize( String name, Map props )
    throws IllegalArgumentException, InitializationException
    {
        // Validate parameters
        if ( props == null )
        {
            printMessage("ConnectionImpl::initialize() - Map of properties cannot be null." );
            throw new IllegalArgumentException( "ConnectionImpl::initialize() - Map of properties cannot be null." );
        }

		try
		{
	        // Set member attributes
    	    setPoolName( name );
        	setInUse( new Boolean( false ) );
	        setProperties( props );
        
    	    // now safe to do JMX registration
        	handleSelfRegistration();
		}
		catch( Throwable exc )
		{
			throw new InitializationException( "ConnectionImpl:initialize(String,Map) - " + exc, exc );
		}
    }

    /**
     * client notification that a transaction is about to begin.
     * 
     * @exception	ConnectionActionException
     */
    abstract public void startTransaction()
    throws ConnectionActionException;

    /**
     * client notification to commit the current transaction.
     * 
     * @exception	ConnectionActionException
     */
    abstract public void commit()
    throws ConnectionActionException;

    /**
     * client notification to rollback the current transaction.
     * 
     * @exception	ConnectionActionException
     */
    abstract public void rollback()
    throws ConnectionActionException;
    
    /**
     * Handles the JMX deregistrations
     * Should be overriden in subclasses to provide the correct symantics,\
     * but delegated back to.
     * 
     * @exception	ConnectionActionException
     */
    public void disconnect()
    throws ConnectionActionException
	{
		try
		{
			unRegisterMBeanWithServer();
		}
		catch( Throwable exc )
		{
			throw new ConnectionActionException( "ConnectionImpl:disconnect() - " + exc, exc );
		}
	}
	
    /**
     * called by the pool manager to notify the connection it is 
     * being released.
     * 
     * @exception	ConnectionActionException
     */
    public void connectionBeingReleased()
    throws ConnectionActionException
    {}

    /**
     * Releases itself to the ConnectionPoolManager singleton.
     * 
     * @exception	ConnectionActionException   
     */
    public void releaseToConnectionPoolManager()
    throws ConnectionActionException
    {
    	try
    	{
        	ConnectionPoolManagerFactory.getObject().releaseConnection( this );
    	}
    	catch( ObjectCreationException exc )
    	{
    		throw new ConnectionActionException( "ConnectionImpl:releaseToConnectionPoolManager() - failed to get the ConnectionPoolManager from its factory - " + exc, exc );
    	}
    	catch( ConnectionActionException exc1 )
    	{
    		throw new ConnectionActionException( "ConnectionImpl:releaseToConnectionPoolManager() - failed to release self from the ConnectionPoolManager - " + exc1, exc1 );    		
    	}
    	catch( Throwable exc2 )
    	{
    		throw new ConnectionActionException( "ConnectionImpl:releaseToConnectionPoolManager() - " + exc2, exc2 );    		    		
    	}
    }

//************************************************************************    
// Private / Protected Methods
//************************************************************************    


//************************************************************************    
// Attributes
//************************************************************************

}

/*
 * Change Log:
 * $Log: ConnectionImpl.java,v $
 */
