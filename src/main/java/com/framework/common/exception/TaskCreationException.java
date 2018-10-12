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
 * Exception class associated with failure during Task creation.
 *
 * <p>
 * @author    realMethods, Inc.
 * @see		  com.framework.integration.task.FrameworkTask
 */
public class TaskCreationException extends FrameworkCheckedException
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * default constructor
     */
    public TaskCreationException()
    {
    }

    /**
     * constructor
     *
     @param message   text of the exception
     */
    public TaskCreationException( String message )
    {
        super( message );
    }
    
    /**
     * Constructor with a Throwable for chained exception and a message.
     * 
     * @param message
     * @param exception
     */
    public TaskCreationException( String message, Throwable exception )
    {
        super( message, exception ); 
    }
    
}

/*
 * Change Log:
 * $Log: TaskCreationException.java,v $
 */




