/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.objectpool;

import com.framework.common.exception.FrameworkStartupException;
import com.framework.common.exception.InitializationException;
import com.framework.common.exception.ObjectCreationException;

import com.framework.common.startup.FrameworkStartup;

/**
 * Factory for creating IConnectionPoolManagers.  
 * <p>
 * @author    realMethods, Inc.
 */
public class ConnectionPoolManagerFactory
    extends FrameworkStartup
{
    /** 
     * constructor
     */
    public ConnectionPoolManagerFactory()
    {
    }
    
// IFrameworkStartup implementation

	/**
	 * start invocation
	 * @exception	FrameworkStartupException
	 */
	public void start()
	throws FrameworkStartupException
	{
		try
		{
			getObject();
		}
		catch( Throwable exc )
		{
			throw new FrameworkStartupException( "ConnectionPoolManagerFactory.start() - " + exc, exc );    
		}
	}
	
	public void stop()
	{
		if ( object != null )
			object.emptyPools();		
	}
    /**
     * Factory method used to create a IConnectionPoolManager
     * <p>
     * @return    	IConnectionPoolManager
     * @exception	ObjectCreationException
     */
    static synchronized public IConnectionPoolManager getObject()
    throws ObjectCreationException
    {
        // lazy initialization
        if ( object == null )
        {
        	try
        	{
            	// Create the connection pool manager 
            	object = new ConnectionPoolManager();
				object.initializePools();            	
        	}
        	catch( InitializationException exc )
        	{
        		throw new ObjectCreationException( "ConnectionPoolManagerFactory:getObject() - failed to initialize the ConnectionPoolManager - " + exc, exc );        		
        	}
        	catch( Throwable exc1 )
        	{
        		throw new ObjectCreationException( "ConnectionPoolManagerFactory:getObject() - failed to create the ConnectionPoolManager - " + exc1, exc1 );
        	}
        }
        
        return( object );
    }
    
// attributes

	/**
	 * purpose of the factory
	 */
    private static IConnectionPoolManager object = null;
}

/*
 * Change Log:
 * $Log: ConnectionPoolManagerFactory.java,v $
 */
