/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.exception;

//************************************
// Imports
//************************************

/**
 * Exception class for ConnectionPool related errors
 * <p>
 * @author    realMethods, Inc.
 * @see		  com.framework.integration.objectpool.ConnectionPool
 * @see		  com.framework.integration.objectpool.ConnectionPoolManager
 */
public class ConnectionPoolActionException extends FrameworkCheckedException
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * default constructor
     */
    public ConnectionPoolActionException()
    {
    }

    /**
     * constructor
     *
     * @param message   text of the exception
     */
    public ConnectionPoolActionException( String message )
    {
        super( message );
    }
    
    /**
     * Constructor with a Throwable for chained exception and a message.
     * @param message
     * @param exception
     */
    public ConnectionPoolActionException( String message, Throwable exception )
    {
        super( message, exception ); 
    }
    
}

/*
 * Change Log:
 * $Log: ConnectionPoolActionException.java,v $
 */




