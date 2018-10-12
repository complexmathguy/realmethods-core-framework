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
 * Exception class for SOAP related errors
 *
 * <p>
 * @author    realMethods, Inc.
 * 
 */
public class FrameworkSOAPException extends FrameworkCheckedException
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * default constructor
     */
    public FrameworkSOAPException()
    {
    }

    /**
     * constructor
     *
     * @param message   text of the exception
     */
    public FrameworkSOAPException( String message )
    {
        super( message );
    }
    
    /**
     * Constructor with a Throwable for chained exception and a message.
     * @param message
     * @param exception
     */
    public FrameworkSOAPException( String message, Throwable exception )
    {
        super( message, exception ); 
    }
    
}

/*
 * Change Log:
 * $Log: FrameworkSOAPException.java,v $
 * Revision 1.1  2003/08/05 12:41:08  tylertravis
 * - initial release
 *
 *
 */




