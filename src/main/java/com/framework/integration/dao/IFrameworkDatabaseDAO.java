/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.dao;

import com.framework.common.exception.DatabaseConnectionFailureException;

import com.framework.integration.objectpool.db.IDatabaseQuerier;

/**
 * Place holder interface for all data access objects associated with a database. 
 * <p> 
 * @author    realMethods, Inc.
 */
public interface IFrameworkDatabaseDAO 
    extends IFrameworkDAO
{    
    
    /**
     * Returns the default application database connection
     * 
     * @return 		DatabaseQuerier      
     * @exception  	DatabaseConnectionFailureException
     */
    public IDatabaseQuerier getMainApplicationConnection()
    throws DatabaseConnectionFailureException;  
    
	/**
	 * Returns a previously provided Connection pool name.
	 * @return      String
	 */
	public String getConnectionPoolName();

	/**
	 * Assigns a Connection pool name.
	 * @param       connectionPoolName
	 */
	public void setConnectionPoolName( String connectionPoolName );  
	
	/**
	 * Assign true/false to whether or not the DAO should automatically
	 * release the associated connection after using it.
	 * <p>
	 * @param       autorelease
	 */
	public void autoReleaseConnection( boolean autorelease );



}

/*
 * Change Log:
 * $Log: IFrameworkDatabaseDAO.java,v $
 */
