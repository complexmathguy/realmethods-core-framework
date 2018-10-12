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
 * Exception class associated with failure during Task execution.
 *
 * <p>
 * @author    realMethods, Inc.
 * @see		  com.framework.integration.task.FrameworkTask
 */
public class TaskExecutionException extends FrameworkCheckedException
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * default constructor
     */
    public TaskExecutionException()
    {
    }

    /**
     * constructor
     *
     @param message   text of the exception
     */
    public TaskExecutionException( String message )
    {
        super( message );
    }
    
    /**
     * Constructor with a Throwable for chained exception and a message.
     * 
     * @param message
     * @param exception
     */
    public TaskExecutionException( String message, Throwable exception )
    {
        super( message, exception ); 
    }
    
}

/*
 * Change Log:
 * $Log: TaskExecutionException.java,v $
 */




