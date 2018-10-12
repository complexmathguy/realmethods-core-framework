/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.objectpool;

import java.util.Map;

import com.framework.common.exception.ConnectionAcquisitionException;
import com.framework.common.exception.ConnectionActionException;
import com.framework.common.exception.InitializationException;

/**
 * ConnectionPoolManager interface
 *
 * <p>
 * @author    realMethods, Inc.
 */
public interface IConnectionPoolManager
{
    /** 
     * Initializes the pool
     * @exception Exception Thrown when having trouble creating the pool.
     */
    public void initializePools()
    throws InitializationException;

    /**
     * Releases a Pooled Connection from the pool.
     * @param 		connection 			The connection to release from the pool.
     * @exception	ConnectionActionException
     */
    public void releaseConnection( IConnectionImpl connection )
    throws ConnectionActionException;

    /**
     * Find an available IConnectionImpl.
     * @param	poolname
     * @return 	IConnectionImpl		An available connection object or null if one is not available
     * @exception ConnectionAcquisitionException
     */
    public IConnectionImpl getConnection( String poolname ) 
    throws ConnectionAcquisitionException;

    /**
     * Empties the pools
     */
    public void emptyPools();
    
    /**
     * Returns a Map containing the IConnectionPools being managed.
     * @return      Map
     */
    public Map getConnectionPools();
    
}

/*
 * Change Log:
 * $Log: IConnectionPoolManager.java,v $
 */
