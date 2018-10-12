/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.command;

import java.util.Collection;

import com.framework.common.message.IFrameworkMessage;

import com.framework.integration.objectpool.db.IResultSetCallback;

/**
 * Base interface for Commands that are related to SQL and a database connection acquired
 * from the ConnectionPoolManager.
 * <p>
 * @author          realMethods, Inc.
 */
public interface ISQLCommand 
    extends IResultSetCallback, java.io.Serializable
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * returns a Collection of SQL parameters
     *
     * @param       msg			the source by which to determine the Parameters
     * @return      String
     */
    public Collection getSQLParameters( IFrameworkMessage msg );

    /**
     * returns the SQL associated with this message
     *
     * @param       msg			the source by which to determine the SQL statement
     * @return      String
     */
    public String getSQL( IFrameworkMessage msg );

    /**
     * is this SQL Command select statement related?
     * <p>
     * @return      boolean
     */
    public boolean isSelectedRelated();

}

/*
 * Change Log:
 * $Log: ISQLCommand.java,v $
 */
