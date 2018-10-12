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
 * Exception class for failure to execute a general SQL statement.
 * <p>
 * @author    realMethods, Inc.
 * @see		  com.framework.integration.objectpool.db.DatabaseQuerier
 */
public class ExecuteStatementException extends DatabaseActionException
{

//************************************************************************    
// Public Methods
//************************************************************************

    /** 
     * Base constructor.
     */
    public ExecuteStatementException()
    {
        super();
    }

    /** Constructor with message.
     *
     * @param message
     */
    public ExecuteStatementException( String message )
    {
        super(message );    
    }
    
    /** Constructor with message.
     *
     * @param message
     * @param sqlExc
     */
    public ExecuteStatementException( String message, SQLException sqlExc )
    {
        super(message, sqlExc );    
    }

    /** Constructor with message.
     *
     * @param message
     * @param throwable
     */
    public ExecuteStatementException( String message, Throwable throwable )
    {
        super(message, throwable );    
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
 * $Log: ExecuteStatementException.java,v $
 */
