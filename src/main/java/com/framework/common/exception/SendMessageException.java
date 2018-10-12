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
 * Exception class for sending a message.
 * 
 * <p>
 * @author    realMethods, Inc.
 */
public class SendMessageException extends FrameworkCheckedException
{

//************************************************************************    
// Public Methods
//************************************************************************

    /** 
     * Base constructor.
     */
    public SendMessageException()
    {
        super();
    }   

    /** 
     * Constructor with message.
     * @param message	text of the exception
     */
    public SendMessageException( String message )
    {
        super( message );
    }

    /**
     * Constructor with a Throwable for chained exception and a message.
     * 
     * @param message
     * @param exception
     */
    public SendMessageException( String message, Throwable exception )
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
 * $Log: SendMessageException.java,v $
 */





