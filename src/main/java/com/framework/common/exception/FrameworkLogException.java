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
 * Exception class for logging related errors
 *
 * <p>
 * @author    realMethods, Inc.
 */
public class FrameworkLogException extends FrameworkCheckedException
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * default constructor
     */
    public FrameworkLogException()
    {
    }

    /**
     * constructor
     *
     * @param message   text of the exception
     */
    public FrameworkLogException( String message )
    {
        super( message );
    }
    
    /**
     * Constructor with a Throwable for chained exception and a message.
     * @param message
     * @param exception
     */
    public FrameworkLogException( String message, Throwable exception )
    {
        super( message, exception ); 
    }
    
}

/*
 * Change Log:
 */




