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
 * Exception class for authorization related errors
 * <p>
 * @author    realMethods, Inc.
 * @see		  com.framework.presentation.request.HTTPRequestHandler
 */
public class AuthorizationException extends FrameworkCheckedException
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * default constructor
     */
    public AuthorizationException()
    {
    }

    /**
     * constructor
     *
     * @param message   text of the exception
     */
    public AuthorizationException( String message )
    {
        super( message );
    }
    
    /**
     * Constructor with a Throwable for chained exception and a message.
     * @param message
     * @param exception
     */
    public AuthorizationException( String message, Throwable exception )
    {
        super( message, exception ); 
    }
    
}

/*
 * Change Log:
 * $Log: AuthorizationException.java,v $
 */




