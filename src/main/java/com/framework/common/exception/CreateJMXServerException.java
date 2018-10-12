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
 * Exception class for JMX MBeanServer creation
 * <p>
 * @author    realMethods, Inc.
 * @see		  com.framework.common.jmx.IFrameworkJMXServerFactory
 * @see		  com.framework.common.jmx.FrameworkDynamicMBean
 */
public class CreateJMXServerException extends FrameworkCheckedException
{

//************************************************************************    
// Public Methods
//************************************************************************

    /** 
     * Base constructor.
     */
    public CreateJMXServerException()
    {
        super();
    }   

    /** Constructor with message.
     * @param message text of the exception
     */
    public CreateJMXServerException( String message )
    {
        super( message );
    }

    /**
     * Constructor with a Throwable for chained exception and a message.
     * 
     * @param message
     * @param exception
     */
    public CreateJMXServerException( String message, Throwable exception )
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
 * $Log: CreateJMXServerException.java,v $
 */




