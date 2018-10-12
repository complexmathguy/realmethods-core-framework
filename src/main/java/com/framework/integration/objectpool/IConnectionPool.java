/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.objectpool;

//***********************************
// Imports
//***********************************
import java.util.Collection;
import java.util.Map;

import com.framework.common.exception.ConnectionAcquisitionException;
import com.framework.common.exception.ConnectionPoolActionException;

/**
 * ConnectionPool interface
 *
 * <p>
 * @author    realMethods, Inc.
 */
public interface IConnectionPool
{
    /**
     * Returns the pool name.
     * @return  String
     */
    public String getPoolName();
    
    /**
     * Returns the connection class name.
     * @return  	String
     * @exception  IllegalAccessException
     */    
    public String getConnectionClassName()
    throws IllegalAccessException;    
    
    /**
     * Returns the initial capacity.
     * @return      Integer
     * @exception  IllegalAccessException
     */    
    public Integer getInitialCapacity()
    throws IllegalAccessException;
    
    /**
     * Returns the max capacity.
     * @return      Integer
     * @exception  IllegalAccessException
     */    
    public Integer getMaxCapacity()
    throws IllegalAccessException;
    
    /**
     * Returns the capacity incremenet
     *
     * @return      Integer
     * @exception  IllegalAccessException
     */    
    public Integer getCapacityIncrement()
    throws IllegalAccessException;
    
    /**
     * Returns the allow shrinking indicator.
     * @return      Boolean
     * @exception   IllegalAccessException
     */
    public Boolean getAllowShrinking()
    throws IllegalAccessException;
    
    /**
     * Returns the using caching indicator.
     * @return      Boolean
     */
    public Boolean usingInternalCaching();
    
    /**
     * Returns the contained properties.
     * @return      Map
     */
    public Map getProperties();
    
    /**
     * Returns the contained IConnectionImpls.
     * @return      Collection
     */
    public Collection getConnections();

    /**
     * Find an available Connection.
     * @return An available connection object or waits until one is available.
     * @exception ConnectionAcquisitionException
     */
    public IConnectionImpl getConnection() 
    throws ConnectionAcquisitionException;
    
    /**
     * Remove all cached connections.
     * @exception	ConnectionPoolActionException
     */
    public void empty()
    throws ConnectionPoolActionException;

    /**
     * Shrinks the pool if necessary to the initial size.
     * @exception	ConnectionPoolActionException
     */
    public void shrink()
    throws ConnectionPoolActionException;    
    
}

/*
 * Change Log:
 * $Log: IConnectionPool.java,v $
 */
