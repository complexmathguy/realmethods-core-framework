/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.exception;

//***********************************
// Imports
//***********************************

/**
 * Indicates a need to backout an MQ message.
 *
 * <p>
 * @author    realMethods, Inc.
 */
public class FrameworkMQBackoutException extends FrameworkCheckedException
{

//************************************************************************    
// Public Methods
//************************************************************************

    /** 
     * Base constructor.
     */
    public FrameworkMQBackoutException()
    {
        super();
    }

    /** 
     * Constructor with message.
     * 
     * @param message   text of the exception
     */
    public FrameworkMQBackoutException( String message )
    {
        super( message );
    }

    /**
     * Constructor with a Throwable for chained exception and a message.
     * 
     * @param message
     * @param exception
     */
    public FrameworkMQBackoutException( String message, Throwable exception )
    {
        super( message, exception ); 
    }

//************************************************************************    
// Private / Protected Methods
//************************************************************************

//************************************************************************    
// Attributes
//************************************************************************
}

/*
 * Change Log:
 * $Log: FrameworkMQBackoutException.java,v $
 */



