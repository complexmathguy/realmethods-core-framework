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
 * Exception class for failing to execute a Command.
 * <p>
 * @author    realMethods, Inc.
 * @see		  com.framework.integration.command.FrameworkCommand
 */
public class CommandExecutionException extends FrameworkCheckedException
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * default constructor
     */
    public CommandExecutionException()
    {
    }

    /**
     * constructor
     *
     * @param message   text of the exception
     */
    public CommandExecutionException( String message )
    {
        super( message );
    }
    
    /**
     * Constructor with a Throwable for chained exception and a message.
     * @param message
     * @param exception
     */
    public CommandExecutionException( String message, Throwable exception )
    {
        super( message, exception ); 
    }
    
}

/*
 * Change Log:
 * $Log: CommandExecutionException.java,v $
 */




