/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.objectpool.db;

//************************************
// Imports
//************************************
import java.sql.ResultSet;

import java.util.Collection;

import com.framework.common.exception.ResultSetCallbackException;

/**
 * Interface used to handle the callback of the reception of a ResultSet
 * for a SQL statement.
 *
 * 
 * <p> 
 * @author    realMethods, Inc.
 */
public interface IResultSetCallback
{
	/**
	 * Callback method to notify of result set for a provided stored
	 * procedure name
	 *
	 * @param       rs
	 * @param       sSQLStatementExecuted
	 * @return      Collection of 2 ArrayLists...see class description for format
	 * @exception   ResultSetCallbackException
	 */
    public Collection notifyResultSet( ResultSet rs, String sSQLStatementExecuted )
    throws ResultSetCallbackException;
}

/*
 * Change Log:
 * $Log: IResultSetCallback.java,v $
 */
