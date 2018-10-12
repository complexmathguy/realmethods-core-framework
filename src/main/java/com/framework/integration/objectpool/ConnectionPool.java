/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.objectpool;

//***********************************
// Imports
//***********************************
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import com.framework.common.exception.ConnectionAcquisitionException;
import com.framework.common.exception.ConnectionPoolActionException;
import com.framework.common.exception.InitializationException;
import com.framework.common.exception.ObjectCreationException;

/**
 * Encapsulates the generic notion of managing a pool of IConnectionImpls.  Management implies
 * the overseeing of the the ConnectionImpl's life cycle, well being, as well the expansion
 * and contraction of a particular pool of ConnectionImpls.
 * <p>
 * @author    realMethods, Inc.
 */
public class ConnectionPool
    extends ConnectionPoolMBean implements IConnectionPool
{

// constructors

    /**
     * deter instantiation
     */
    public ConnectionPool()
    {
    }
    
    /**
     * sole public constructor
     *
     * @param       poolname		unique identifier
     * @param       props			properties to configure the pool by
     * @exception   ObjectCreationException
     */
    public ConnectionPool( String poolname, Map props )
    throws ObjectCreationException
    {        
        if ( props == null )
            throw new ObjectCreationException( "ConnectionPool:constructor - Map arg is null for pool " + poolname );

        // assign first in order to utilize 
        setPoolName( poolname );
        setProperties( props );
        
        // Is there a pool name within the properties?
        if ( getPoolName() == null )
            throw new ObjectCreationException( "ConnectionPool:constructor - getPoolNamed() returned null." );
            
        // Is there a connection class name within the properties?  
        try
        {          
	        if ( getConnectionClassName() == null )
    	        throw new ObjectCreationException( "ConnectionPool:constructor - getConnectionClassName() returned null for pool " + poolname  );
        }
        catch( IllegalAccessException accessExc )
        {
			throw new ObjectCreationException( "ConnectionPool:constructor - getConnectionClassName() returned the following exception " + accessExc );        	
        }
            
		// now it is safe to JMX register
		handleSelfRegistration();            
		
        // populate the Connection container using the initalize size parameter
        try
        {
            if ( this.usingInternalCaching().booleanValue() == true )
                connections = createPoolConnections( getInitialCapacity().intValue() );
        }
        catch( Throwable exc )
        {
            throw new ObjectCreationException( "ConnectionPool:constructor - for pool " + poolname + " - " + exc, exc );
        }
        
        logDebugMessage( "ConnectionPool() - created connection for pool " + getPoolName() + " with properties " + props );
    }
    
// access methods
    
    /**
     * Returns the contained IConnectionImpls.
     * @return      Collection
     */
    public Collection getConnections()
    {        
        return( connections );
    }
    
// action methods

    /**
     * Find an available Connection.
     * @return IConnectionImpl - An available connection object or waits until one is available.
     * @exception ConnectionAcquisitionException
     */
    public synchronized IConnectionImpl getConnection() 
    throws ConnectionAcquisitionException
    {
        IConnectionImpl returnConnectionImpl = null;

		try
		{
	        // are we caching or not...
	        if ( usingInternalCaching().booleanValue() == false )
	        {
	            // simply create a single connection but don't bother caching it...
	            returnConnectionImpl = this.createAndInitializeConnection();
	            return( returnConnectionImpl );
	        }
	        
	        // If we have a container of connections and are required to cache...
	        if ( connections != null )
	        {
	            //-------------------------------------------------------
	            // Step #1 - first see if there is an IConnectionImpl
	            // not in use within the connections
	            //-------------------------------------------------------
	            returnConnectionImpl = locateAvailableConnection();
	        
	            // -------------------------------------------------------
	            // A connection not in use and cannot be found, 
	            // -------------------------------------------------------
	            if ( returnConnectionImpl == null )
	            {            
	                
	                logWarnMessage( "ConnectionPool:getConnection() - unable to locate an available connection for " + getPoolName() );
	                
	                // Retrieve the max capacity from the dictionary
	                int nMaxCapacityAllowedValue    = this.getMaxCapacity().intValue();
	                int nPoolSize                   = connections.size();
	                
	                // If the current pool size is less than the max capacity allowed
	                // therefore there is room to grow
	                if ( nPoolSize < nMaxCapacityAllowedValue )
	                {
	                    // Retrieve the capacity incrementation value for the pool
	                    int nCapacityIncrementValue = getCapacityIncrement().intValue();
	
	                    // Figure out how many to create based upon the max capacity allowed
	                    // and the capacity increment value for the pool
	                    // If the difference between the max capacity and current pool
	                    // value is greater than or equal to the capacity increment then create
	                    // the capacity increment value; or if the difference is less than the capacity
	                    // increment only create the number that equals the difference between the 
	                    // max capacity allowed and the current pool size
	                    int nRoomToGrow = nMaxCapacityAllowedValue - nPoolSize;
	
	                    // If we can not grow to the full extent
	                    int nValueToCreate = 0;
	
	                    if ( nRoomToGrow < nCapacityIncrementValue )
	                    {
	                        // Only create what we are able to grow
	                        nValueToCreate = nRoomToGrow;
	                    }
	                    else
	                    {
	                        // Create the capacity increment value
	                        nValueToCreate = nCapacityIncrementValue;
	                    }
	
	                    // Now create the new connections
	                    Collection newConnections = null;
	                    newConnections = createPoolConnections( nValueToCreate );
	
	                    // If we actually create some
	                    if ( newConnections.size( ) > 0 )
	                    {
	                        // Mark the first connection as in use and set it as the return value
	                        returnConnectionImpl = ( IConnectionImpl ) newConnections.toArray()[0];                    
							
							// add them to the connection cache
							connections.addAll( newConnections );								 
	                    } 
	                } 
	                else    // room to grow condition failed, so recurse
	                {
	                	if ( getWaitUntilAvailable().booleanValue() == true )
	                	{	                	
	                		Thread.currentThread().sleep(3000);
	                		return( getConnection() );
	                	}
	                	else
	                    	throw new ConnectionAcquisitionException( "ConnectionPool:getConnection() - unable to create another connection - reached max pool size for " + getPoolName() );                        
	                }                
	            }
	            else    // local availability check condition
	            {
	                logDebugMessage( "ConnectionPool:getConnection() - located an available one for " + getPoolName() );
	            }            
	        }
        }         
	    catch( Throwable exc )
	    {
	      	throw new ConnectionAcquisitionException( "ConnectionPool:getConnection() - " + exc, exc );
	    }        
	    
	    if ( returnConnectionImpl != null )
	        // set it "in-use"
    	    returnConnectionImpl.setInUse( new Boolean( true ) );
		else
			throw new ConnectionAcquisitionException( "ConnectionPool:getConnection() - unable to acquire a connection for " + getPoolName() );   	    
        
        return (returnConnectionImpl);
    }

    /**
     * When shutting down the pool, first empty it and disconnect each connection.
     * @exception	ConnectionPoolActionException
     */
    public synchronized void empty()
    throws ConnectionPoolActionException
    {
        // If we actually have connections
        if ( connections != null )
        {
            Iterator iter						= connections.iterator();
            IConnectionImpl currentConnection   = null;
            
            // loop through the connections
            while ( iter.hasNext() )
            {
                currentConnection = (IConnectionImpl)iter.next();
                
                // If the connection is not in use, close it
                if ( currentConnection.getInUse( ).booleanValue( ) == false )
                {
                    try
                    {
                        currentConnection.disconnect();
                    }
                    catch ( Throwable exc )
                    {                    	
                        throw new ConnectionPoolActionException( "ConnectionPoolManager:empty() - Error in disconnecting a connection - " + exc, exc );
                    }
                }
                // else it is in use
                else
                {
                    // If it is in use sleep for 5 seconds and force closure
                    try
                    {
                        java.lang.Thread.currentThread().sleep( 5000 );
                        currentConnection.disconnect();
                    }
                    catch ( Throwable exc )
                    {
                        throw new ConnectionPoolActionException( "ConnectionPool:empty() - Error in disconnecting a connection - " + exc, exc );
                    }
                } 
				
                currentConnection  = null;
            } 
                    
	        // empty the Collection
    	    connections.clear();            
        }

    }

    /**
     * Shrinks the pool if necessary to the initial size.
     * @exception	ConnectionPoolActionException
     */
    public synchronized void shrink()
    throws ConnectionPoolActionException
    {
        try
        {
            // if not caching internally, simply return return
            if ( usingInternalCaching().booleanValue() == false )
            {
                return;
            }
            
            // First retrieve the Allow Shrinkage flag
            Boolean bAllowShrinkage = this.getAllowShrinking();

            // If shrinkage is allowed for the pool
            if ( bAllowShrinkage.booleanValue() == true )
            {
                // Retrieve the pool's size
                int nConnectionPoolSize = connections.size( );

                // Retrieve the pool's iniitial capacity
                Integer nInitialCapacity = this.getInitialCapacity();

                // If the pool's size is greater than the initial capacity
                int nInitialCapacityValue = nInitialCapacity.intValue( );
                if ( nConnectionPoolSize > nInitialCapacityValue )
                {
                    // ---------------------------------------------------    
                    // Cycle through the pool and shrink it down to the initial
                    // capacity if possible (connection not in use)
                    // ---------------------------------------------------
                    int nNumberToShrink = nConnectionPoolSize - nInitialCapacityValue;

                    IConnectionImpl currentConnection = null;
					Iterator iter = connections.iterator();
					
					while( iter.hasNext() && nNumberToShrink != 0 )					
                    {
                        // Get connection
                        currentConnection = (IConnectionImpl)iter.next();

                        // Check if the connection is in use
                        if ( currentConnection != null )
                        {
                            if ( currentConnection.getInUse( ).booleanValue( ) == false )
                            {
                            	// disconnect the connection
                            	try
                            	{
                            		currentConnection.disconnect();
                            	}
                            	catch( Throwable exc )
                            	{
                            		// if the disconect fails, still continue to shrink...
                            		logErrorMessage( "ConnectionPool:shrink() - Failure on call to disconnect connection " + currentConnection.getPoolName() + "- " + exc );
                            	}
                            	
                                // Remove the connection from the Collection
                                iter.remove();

                                // expedite gc
                                currentConnection = null;

                                // Decrease the value that needs to be shrunk
                                nNumberToShrink = nNumberToShrink - 1;
                            }
                        }
                    }
                }
            }
        }
        catch( Throwable exc )
        {
        	throw new ConnectionPoolActionException( "ConnectionPool:shrink() - " + exc, exc );
        }
    }

    /**
     * Simple String representation.
     * @return      String
     */
    public String toString()
    {
        return( getPoolName() );
    }
    
    /**
     * Loop through the Collection of pooled connections in 
     * an attempt to discover one not in use
     * <p>
     * @return      IConnectionImpl - null if none available
     */
    protected IConnectionImpl locateAvailableConnection()
    {
        IConnectionImpl connectionImpl  = null;
        Iterator iter					= connections.iterator();
        boolean bFound                  = false;
        
        // loop through the Collection of pooled connections in 
        // an attempt to discover one not in use
        while( iter.hasNext() && bFound == false )
        {
            connectionImpl = (IConnectionImpl)iter.next();
            
            if ( connectionImpl != null && connectionImpl.getInUse().booleanValue() == false )
                bFound = true;
        }
        
        // null out the IConnectionImpl before returning
        if ( bFound == false )
            connectionImpl = null;
            
        return( connectionImpl );
    }

    /**
     * Creates more connections if possible, based on number to grow versus
     * the maximum pool size.
     * <p>
     * @param		numberToGrow 		the number of connections to create.
     * @return 		IConnectionImpl's that are created.
     * @exception	ConnectionPoolActionException
     */
    protected Collection createPoolConnections( int numberToGrow )
    throws ConnectionPoolActionException
    {
        logDebugMessage( "ConnectionPool:createPoolConnections(...) - attempting to create  " + String.valueOf( numberToGrow ) + " connections for " + getPoolName() );
        
        // Create the return Collection
        Collection returnValue = new ArrayList( numberToGrow );

        // Retrieve the connection class name
        IConnectionImpl currentConnection   = null;

        // loop through and call the factory method on the newly created IConnectionImpl
        for ( int nCounter = 0; nCounter < numberToGrow; nCounter++ )
        {
            // obtain the corresponding class by creating dynamically
            try
            {
            	currentConnection = createAndInitializeConnection();
            }
            catch( InitializationException exc )
            {
            	throw new ConnectionPoolActionException( "ConnectionPool.createPoolConnections(int) - failed to  create pool connections - " + exc, exc );
            }
            
            // Initalize and Add the connection impl to the return Collection
            if ( currentConnection != null )
            {
                returnValue.add( currentConnection );
                
                logDebugMessage( "ConnectionPool:createPoolConnections(...) - created connection #" + String.valueOf( nCounter+1 ) + " for " + getPoolName() );                
            }
            
        } // Looping through connection creation
        return returnValue;
    }
    
    /**
     * Creates and initialize a single connection.
     * @return IConnectionImpl
     * @exception InitializationException 
     */    
    protected IConnectionImpl createAndInitializeConnection()
    throws InitializationException
    {
        Object currentConnection    = null;
        String sConnectionClassName = null;
        
        try
        {
        	sConnectionClassName = getConnectionClassName();
        	
            // obtain the corresponding class by creating dynamically
            currentConnection = Class.forName( sConnectionClassName ).newInstance();
            
            // make sure the class created implements IConnectionImpl
            if ( !(currentConnection instanceof IConnectionImpl) )
            {
                throw new InitializationException( "ConnectionPool.createAndInitializeConnection() - Class " + sConnectionClassName + " isn't of type IConnectionImpl but is a " + currentConnection.getClass().getName() );
            }                    
        }
        catch( Throwable exc )
        {
            throw new InitializationException( "ConnectionPool.createAndInitializeConnection() - Failed to create class the " + sConnectionClassName + " - " + exc, exc ); 
        }                        
            
        // Initalize and Add the connection impl to the return Collection
        if ( currentConnection != null )
        {
            // each IConnectionImpl must implement this interface
            ((IConnectionImpl)currentConnection).initialize( getPoolName(), getProperties());
        }

        return( (IConnectionImpl)currentConnection );
    }
    
// attributes

    /**
     * Collection of Connection Pools.  Each pool is a Collection of IConnectionImpl's.
     */
    protected Collection connections                      = null;
            
}

/*
 * Change Log:
 * $Log: ConnectionPool.java,v $
 */
