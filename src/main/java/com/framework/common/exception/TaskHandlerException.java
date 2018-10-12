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
 * This exception is thrown in response to a TaskHandler failure.
 *
 * <p>
 * @author    realMethods, Inc.
 * @see		  com.framework.integration.task.TaskHandler
 */
public class TaskHandlerException extends FrameworkCheckedException
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * default constructor
     */
    public TaskHandlerException()
    {
    }

    /**
     * constructor
     *
     @param message   text of the exception
     */
    public TaskHandlerException( String message )
    {
        super( message );
    }
    
    /**
     * Constructor with a Throwable for chained exception and a message.
     * @param message
     * @param exception
     */
    public TaskHandlerException( String message, Throwable exception )
    {
        super( message, exception ); 
    }
    
}

/*
 * Change Log:
 * $Log: TaskHandlerException.java,v $
 */




