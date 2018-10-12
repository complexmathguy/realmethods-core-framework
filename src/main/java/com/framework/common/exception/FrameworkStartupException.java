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
 * Exception class for framework startup errors
 *
 * <p>
 * @author    realMethods, Inc.
 * @see		  com.framework.common.startup.IFrameworkStartup
 * @see		  com.framework.common.startup.FrameworkStartup
 * @see		  com.framework.common.startup.StartupManager
 */
public class FrameworkStartupException extends FrameworkCheckedException
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * default constructor
     */
    public FrameworkStartupException()
    {
    }

    /**
     * constructor
     *
     * @param message   text of the exception
     */
    public FrameworkStartupException( String message )
    {
        super( message );
    }
    
    /**
     * Constructor with a Throwable for chained exception and a message.
     * @param message
     * @param exception
     */
    public FrameworkStartupException( String message, Throwable exception )
    {
        super( message, exception ); 
    }
    
}

/*
 * Change Log:
 * $Log:  $
 */




