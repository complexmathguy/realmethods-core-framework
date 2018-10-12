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
 * Exception class for ValueObject Notification related errors
 *
 * <p>
 * @author    realMethods, Inc.
 * @see		  com.framework.integration.notify.ValueObjectNotificationManager
 */
public class ValueObjectNotificationException extends FrameworkCheckedException
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * default constructor
     */
    public ValueObjectNotificationException()
    {
    }

    /**
     * constructor
     *
     * @param message   text of the exception
     */
    public ValueObjectNotificationException( String message )
    {
        super( message );
    }
    
    /**
     * Constructor with a Throwable for chained exception and a message.
     * @param message
     * @param exception
     */
    public ValueObjectNotificationException( String message, Throwable exception )
    {
        super( message, exception ); 
    }
    
}

/*
 * Change Log:
 * $Log: ValueObjectNotificationException.java,v $
 */




