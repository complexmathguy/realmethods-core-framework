/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.dao;


import com.framework.business.pk.FrameworkPrimaryKey;

import com.framework.business.vo.IFrameworkValueObject;

import com.framework.common.exception.FrameworkDAOException;
import com.framework.common.exception.VersionUpdateException;

/**
 * This interface represents the minimal contract required to be
 * implemented by a Framework Data Access Object
 *
 * 
 * <p> 
 * @author    realMethods, Inc.
 */
public interface IFrameworkDAO 
    extends java.io.Serializable
{    
	/**
	 * Creates an associated value object.
	 * <p>
	 * This method will create/insert the corresponding 
	 * bean in the persistent store based on the provided model
	 * <p>
	 * @param       vo			the source to create within the persistent store
	 * @return      			the result of the creation	
	 * @exception   FrameworkDAOException
	 */
	public IFrameworkValueObject create( IFrameworkValueObject vo )
	throws FrameworkDAOException;
    
	/**
	 * Retrieves the model from the persistent store, using the provided primary key. 
	 * If no match is found, a null IFrameworkValueObject is returned.
	 * <p>
	 * @param       pk  	identifier of entity to locate from the persistent store   
	 * @return      IFrameworkValueObject
	 * @exception   FrameworkDAOException
	 */
	public IFrameworkValueObject find( FrameworkPrimaryKey pk ) 
	throws FrameworkDAOException;


	/**
	 * Stores the provided value object to the persistent store.
	 * <p>
	 * @param       vo		source of data to store
	 * @return		the results of saving to the persistent store
	 * @exception   FrameworkDAOException
	 * @exception   VersionUpdateException	thrown if vo suppoorts versioning and what is persisted isn't this version
	 */
	public IFrameworkValueObject save( IFrameworkValueObject vo )
	throws FrameworkDAOException, VersionUpdateException;

	/**
	* Removes the associated model from the persistent store.
	* <p>
	* @param        key		identifier of object to remove from the persistent store
	* @return       success or fail
	* @exception    FrameworkDAOException
	*/
	public boolean delete( FrameworkPrimaryKey key ) 
	throws FrameworkDAOException;    
    
    /**
     * Factory notification method of being released that the 
     * consumer is finished.
     */    
    public void release();
    
    /**
     * Commit the assocate connection.
     * @exception       FrameworkDAOException
     */
    public void commit()
    throws FrameworkDAOException;
    
    /**
     * Rollback the associated connection.
     * @exception       FrameworkDAOException
     */
    public void rollback()
    throws FrameworkDAOException;
    
	/**
	 * Assign true/false to whether or not the DAO should automatically
	 * release the associated connection after using it.
	 * <p>
	 * @param       release
	 */
	public void autoReleaseConnection( boolean release );    
	
	/**
	 * Returnes true/false to whether or not the DAO should automatically
	 * release the associated connection after using it.
	 * <p>
	 * @return  boolean
	 */
	public boolean autoReleaseConnection();	
        
}

/*
 * Change Log:
 */
