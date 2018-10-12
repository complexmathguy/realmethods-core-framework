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
 * Exception class for failure to connect from the application server.
 *
 * <p>
 * @author    	realMethods, Inc.
 * @deprecated	
 */
public class ServerConnectionFailureException extends FrameworkCheckedException
{
//****************************************************
// Public Methods
//****************************************************

    public ServerConnectionFailureException()
    {
        super();
    }   
    
    /**
     * Constructor
     * 
     * @param message	text related to the Exception
     */
    public ServerConnectionFailureException( String message )
    {
        super( message );
    }

    /**
     * Constructor with a Throwable for chained exception and a message.
     * 
     * @param message
     * @param exception
     */
    public ServerConnectionFailureException( String message,
                                    		Throwable exception )
    {
        super( message, exception ); 
    }
    
}

/*
 * Change Log:
 * $Log: ServerConnectionFailureException.java,v $
 */




