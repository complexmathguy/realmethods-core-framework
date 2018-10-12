/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.exception;

//***********************************
// Imports
//***********************************
import java.sql.SQLException;

/**
 * Exception class for failure to execute a select statement.
 * 
 * <p>
 * @author    realMethods, Inc.
 * @see		  com.framework.integration.objectpool.db.DatabaseQuerier
 */
public class SelectStatementException extends DatabaseActionException
{

//************************************************************************    
// Public Methods
//************************************************************************

    /** 
     * Base constructor.
     */
    public SelectStatementException()
    {
        super();
    }

    /** Constructor with message.
     * @param message
     * @param exc
     */
    public SelectStatementException( String message, SQLException exc)
    {
        super(message, exc);    
    }


//************************************************************************    
// Private / Protected Methods
//************************************************************************

//************************************************************************    
// Attributes
//************************************************************************
}

/*
 * Change Log:
 * $Log: SelectStatementException.java,v $
 */
