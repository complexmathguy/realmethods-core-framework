/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.objectpool;

import java.util.Map;

import com.framework.common.exception.ConnectionActionException;
import com.framework.common.exception.InitializationException;

/**
 * Base interface for all connection types that are pooled in the Connection Pool.
 * <p>
 * @author    realMethods, Inc.
 */
public interface IConnectionImpl
{
    /** 
     * Initializes and makes a connection to the connection source.
     * This base implementation does not make the actual connection.
     * @param name The pool that this connection is associated with.
     * @param props Map of properties needed to create the connection.
     * Actual values are dependent on the associated type of connection.
     * @exception IllegalArgumentException Thrown if the propertiesIn is null.
     * @exception InitializationException
     */
    public void initialize( String name, Map props )
    throws IllegalArgumentException, InitializationException;

    /**
     * Retrieves the name of the pool that this connection is part of.
     * @return Pool Name.
     */
    public String getPoolName();

    /**
     * Returns the previously assign Map of properties
     * @return      Map
     */
    public Map getProperties();

    /**
     * Retrieves the InUse flag.
     * @return A Boolean indicating whether the connection is being used.
     */
    public Boolean getInUse();

    /** 
     * Sets the InUse flag.
     * @param flagIn New InUse flag value.
     */
    public void setInUse( Boolean flagIn );

    /**
     * client notification that a transaction is about to begin
     * @exception	ConnectionActionException
     */
    public void startTransaction()
    throws ConnectionActionException;

    /**
     * client notification to commit the current transaction.
     * @exception	ConnectionActionException
     */
    public void commit()
    throws ConnectionActionException;

    /**
     * client notification to rollback the current transaction.
     * @exception	ConnectionActionException
     */
    public void rollback()
    throws ConnectionActionException;

    /**
     * Disconnects the underlying connection.  
     * Should be overriden in subclasses.
     * @exception ConnectionActionException
     */
    public void disconnect()
    throws ConnectionActionException;

    /**
     * called by the pool manager to notify the connection it is 
     * being released.
     * @exception ConnectionActionException
     */
    public void connectionBeingReleased()
    throws ConnectionActionException;
    
    /**
     * Releases itself to the ConnectionPoolManager singleton
     * @exception	ConnectionActionException   
     */
    public void releaseToConnectionPoolManager()
    throws ConnectionActionException;    
    

    public final static String COMMIT      = "commit";
    public final static String ROLLBACK    = "rollback";
    public final static String DISCONNECT  = "disconnect";    
    
}

/*
 * Change Log:
 * $Log: IConnectionImpl.java,v $
 */
