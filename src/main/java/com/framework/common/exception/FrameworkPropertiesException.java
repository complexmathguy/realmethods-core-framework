/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.exception;

/**
 * Exception for failures associated with establishing or acquiring properties from
 * the FrameworkPropertiesHandler.
 * <p>
 * @author    realMethods, Inc.
 * @see		  com.framework.common.properties.IFrameworkPropertiesHandler
 */
public class FrameworkPropertiesException extends FrameworkCheckedException
{
    /** 
     * Base constructor.
     */
    public FrameworkPropertiesException()
    {
        super(); 
    }

    /** Constructor with message.
     * @param message text of the exception
     */
    public FrameworkPropertiesException( String message )
    {
        super( message ); 
    }
    
    /**
     * Constructor with a Throwable for chained exception and a message.
     * 
     * @param message
     * @param exception
     */
    public FrameworkPropertiesException( String message, Throwable exception )
    {
        super( message, exception ); 
    }
    
}

/*
 * Change Log:
 * $Log: FrameworkPropertiesException.java,v $
 */




