/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.objectpool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.exception.ConnectionAcquisitionException;
import com.framework.common.exception.ConnectionActionException;
import com.framework.common.exception.ConnectionPoolActionException;
import com.framework.common.exception.InitializationException;

import com.framework.common.properties.IConnectionPoolPropertiesHandler;
import com.framework.common.properties.PropertyFileLoader;

/**
 * Encapsulates the generic notion of managing a pool of IConnectionImpls.  Management implies
 * the overseeing of the the ConnectionImpl's life cycle, well being, as well the expansion
 * and contraction of a particular pool of ConnectionImpls.
 * <p>
 * @author    realMethods, Inc.
 */
public class ConnectionPoolManager 
    extends FrameworkBaseObject implements IConnectionPoolManager
{

//************************************************************************    
// Public Methods
//************************************************************************

	/**
	 * default constructor - instead use the getObject() method on ConnectionPoolManagerFactory
	 */
    public ConnectionPoolManager()
    {
    } 

    /** 
     * Initializes all pools.
     * @exception InitializationException
     */
    public synchronized void initializePools() 
    throws InitializationException
    { 
    	try
    	{

	        // --------------------------------------------------------------
    	    // Populate the connection pool using the just loaded properties
        	// --------------------------------------------------------------
	        createPools();
    	}
    	catch( Throwable exc )
    	{
    		throw new InitializationException( "ConnectionPoolManager:initializePools() - " + exc, exc );
    	}
    }
    
    /**
     * Using the ConnectionPool properties contained within the FrameworkPropertiesHandler, 
     * populate the contained connectionPools.
     * <p>
     * @exception	ConnectionPoolActionException
     */
    synchronized protected void createPools()
    throws ConnectionPoolActionException
    {
        // get the ConnectionPool properties
        Map thePools = null;
		IConnectionPoolPropertiesHandler propertyHandler = null;
		
        try
        {
        	propertyHandler	= PropertyFileLoader.getInstance().getConectionPoolPropertiesHandler();
			thePools = propertyHandler.getConnectionPools();
        }
        catch( Throwable exc )
        {
        	throw new ConnectionPoolActionException( "ConnectionPoolManager:createPools() - failed to get the FrameworkPropertiesHandler from it factory  - exc" );        	
        }
        
        if ( thePools == null )
        	throw new ConnectionPoolActionException( "ConnectionPoolManager:createPools() - failed to acquire properties " + PROPERTIES_NAME + " from the FrameworkPropertiesHandlerFactory." );
        	
        // get number of entries to create for each entry
        int tableSize = thePools.size();
        
        
        // --------------------------------------------------------------
        // Create the connections pool member
        // --------------------------------------------------------------
        connectionPools = new HashMap( tableSize );

        // Initialize Loop Variables
        Map properties                		= null;
        String currentPoolName              = null;
        Iterator keys            			= thePools.keySet().iterator();
        IConnectionPool currentConnectionPool = null;
        
        // Cycle through and populate map for each pool
        while( keys.hasNext() )
        {
            try
            {                
                // get the current pool name 
                currentPoolName = (String)keys.next();
                
                // acquire it's bindings from the framework properties handler
                properties = (Map)thePools.get( currentPoolName );
                
                // create the current connection pool
                currentConnectionPool = new ConnectionPool( currentPoolName, properties );

                // cache the current connection pool
                connectionPools.put( currentPoolName, currentConnectionPool );
                
            }
            catch ( Throwable exc )
            {
                logMessage( "ConnectionPoolManager::initializePools() - " + exc );
            }
        }
    }

    /**
     * Releases a Connection back to its pool.
     * @param connectionIn The connection to release from the pool.
     * @exception	ConnectionActionException
     */
    public synchronized void releaseConnection( IConnectionImpl connectionIn )
    throws ConnectionActionException
    {
        // If the connection is not null
        if ( connectionIn != null )
        {
//            logMessage( "Releasing connection - " + connectionIn );
            
            // -----------------------------------
            // notify the connection of the action
            // -----------------------------------
            try
            {
                connectionIn.connectionBeingReleased();
            }
            catch ( Throwable exc )
            {
            	throw new ConnectionActionException( "ConnectionPoolManager:releaseConnection() - notification via connectionBeingReleased() on " + connectionIn.getPoolName() + " failed - " + exc, exc );
            }
            finally
            {
	            // -----------------------------------
	            // Mark the connection as not in use
	            // -----------------------------------
	            connectionIn.setInUse( new Boolean( false ) );
	
	            // ---------------------------------------------------------
	            // delegate to the contained associated connection pool
	            // ---------------------------------------------------------
	            try
	            {
	                IConnectionPool connectionPool = getConnectionPool( connectionIn.getPoolName() );
	                
	                // if cached internally, simply shrink the pool
	                if ( connectionPool.usingInternalCaching().booleanValue() == true )
	                {                	
	                    connectionPool.shrink();
	                }
	                else // otherwise, null out the object
	                {                	
	                	connectionIn.disconnect();
	                    connectionIn = null;
	                }
	            }
	            catch( Throwable exc )
	            {
	                throw new ConnectionActionException( "ConnectionPoolManager:releaseConnection() - " + exc, exc );
	            }
            }
        }
    }

    /**
     * Returns the associated IConnectionPool. 
     * <p>
     * @param   name
     * @return  IConnectionPoolImpl
     * @exception ConnectionPoolActionException
     */
    public synchronized IConnectionPool getConnectionPool( String name ) 
    throws ConnectionPoolActionException
    {
        IConnectionPool connectionPool = null;
        
        // If we have a container of pools
        if ( connectionPools != null )
        {
            connectionPool = (IConnectionPool)this.connectionPools.get( name );
        }

		if ( connectionPool == null )
			throw new ConnectionPoolActionException( "ConnnectionPoolManager:getConnectionPool(String) - failed to acquire connection pool " + name );
			
        return (connectionPool);
    }

    /**
     * Delegates to the IConnectionPool associated with the pool name.  
     * Throws a ConnectionAcquisitionException if not found
     * <p>
     * @param   name
     * @return  IConnectionImpl
     * @exception ConnectionAcquisitionException
     */
    public synchronized IConnectionImpl getConnection( String name ) 
    throws ConnectionAcquisitionException
    {
        IConnectionImpl connectionImpl = null;
        
        // If we have a container of pools
        if ( connectionPools != null )
        {
        	try
        	{
        	    connectionImpl = getConnectionPool( name ).getConnection();
        	}
        	catch( Throwable exc )
        	{
				throw new ConnectionAcquisitionException( "ConnnectionPoolManager:getConnection(String) - failed to acquire connection " + name + " : " + exc, exc );        		
        	}
        }
        
        return (connectionImpl);
    }

    /**
     * When shutting down the pool, you need to first empty it.
     */
    public synchronized void emptyPools()
    {
        // If we actually have pools
        if ( connectionPools != null )
        {
        	logMessage( "ConnectionPoolManager emptying Connection pools...");
        	
            Iterator iter	                = connectionPools.values().iterator();
            IConnectionPool connectionPool  = null;
            
            while( iter.hasNext() )
            {
            	connectionPool = (IConnectionPool)iter.next();
            	
            	try
            	{
            		if ( connectionPool != null )
	                	connectionPool.empty();
            	}
            	catch( Throwable exc )
            	{
            		// want to keep looping
            		logMessage( "ConnectionPoolManager:emptyPools() - failed to empty connection pool " + connectionPool.getPoolName() + " - " + exc );
            	}	
                
                // expedite gc
                connectionPool = null;
            }

        	logMessage( "ConnectionPoolManager done emptying Connection pools...");
        	connectionPools.clear();
        	
        } 
    }

    /**
     * Returns a Map containing the IConnectionPools being managed.
     *
     * @return      Map
     */
    public Map getConnectionPools()
    {
        return( connectionPools );
    }
    
    
//************************************************************************    
// Private / Protected Methods
//************************************************************************

//************************************************************************    
// Attributes
//************************************************************************

    /**
     * Map of IConnectionPools
     */
    protected Map connectionPools      = null;
    
    /**
     * Name used to access the value from the FrameworkPropertiesHandler
     */
    final static public String PROPERTIES_NAME    = "ConnectionPool";
}

/*
 * Change Log:
 * $Log: ConnectionPoolManager.java,v $
 */
