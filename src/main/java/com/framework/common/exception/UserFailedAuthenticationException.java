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
 * Used to indicate that the user was unable to be authenticated.
 *
 * <p>
 * @author    realMethods, Inc.
 */
public class UserFailedAuthenticationException extends FrameworkCheckedException
{

//************************************************************************    
// Public Methods
//************************************************************************

    /** 
     * Base constructor.
     */
    public UserFailedAuthenticationException()
    {
        super(); 
    }

    /** 
     * Constructor with message.
     * @param message   text of the exception
     */
    public UserFailedAuthenticationException( String message )
    {
        super( message ); 
    }

    /**
     * Constructor with a Throwable for chained exception and a message.
     * @param message
     * @param exception
     */
    public UserFailedAuthenticationException(	String message,
                                    			Throwable exception )
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
 * $Log: UserFailedAuthenticationException.java,v $
 */




