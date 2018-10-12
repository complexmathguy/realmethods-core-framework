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
 * This exception is thrown in the event that the registration of Tasks during
 * startup fails.
 *
 * <p>
 * @author    realMethods, Inc.
 * @see		  com.framework.integration.task.TaskJMSExecutionRegistry
 */
public class TaskJMSRegistrationException extends FrameworkCheckedException
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * default constructor
     */
    public TaskJMSRegistrationException()
    {
    }

    /**
     * constructor
     *
     @param message   text of the exception
     */
    public TaskJMSRegistrationException( String message )
    {
        super( message );
    }
    
    /**
     * Constructor with a Throwable for chained exception and a message.
     * @param message
     * @param exception
     */
    public TaskJMSRegistrationException( String message,
                                    Throwable exception )
    {
        super( message, exception ); 
    }
    
}

/*
 * Change Log:
 * $Log: TaskJMSRegistrationException.java,v $
 */




