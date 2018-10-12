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
 * Exception class for ConnectionImpl related errors.
 * <p>
 * @author    realMethods, Inc.
 * @see		  com.framework.integration.objectpool.ConnectionImpl
 * @see		  com.framework.integration.objectpool.ConnectionPoolManager
 */
public class ConnectionActionException extends FrameworkCheckedException
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * default constructor
     */
    public ConnectionActionException()
    {
    }

    /**
     * constructor
     *
     * @param message   text of the exception
     */
    public ConnectionActionException( String message )
    {
        super( message );
    }
    
    /**
     * Constructor with a Throwable for chained exception and a message.
     * @param message
     * @param exception
     */
    public ConnectionActionException( String message, Throwable exception )
    {
        super( message, exception ); 
    }
    
}

/*
 * Change Log:
 * $Log: ConnectionActionException.java,v $
 */




