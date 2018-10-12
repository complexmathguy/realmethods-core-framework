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
 * Exception class for failure to disconnect from the application server
 *
 * <p>
 * @author    realMethods, Inc.
 * @deprecated
 */
public class ServerDisconnectionFailureException extends FrameworkCheckedException
{
//****************************************************
// Public Methods
//****************************************************

    public ServerDisconnectionFailureException()
    {
        super();
    }

    public ServerDisconnectionFailureException( String s )
    {
        super( s );
    }
    
    /**
     * Constructor with a Throwable for chained exception and a message.
     * 
     * @param message
     * @param exception
     */
    public ServerDisconnectionFailureException( String message,
                                    			Throwable exception )
    {
        super( message, exception ); 
    }
    
}

/*
 * Change Log:
 * $Log: ServerDisconnectionFailureException.java,v $
 */




