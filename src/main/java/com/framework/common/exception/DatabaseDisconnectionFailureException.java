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
 * Exception class for failures database disconnects.
 * <p>
 * @author    realMethods, Inc.
 * @see		  com.framework.integration.security.jaas.DatabaseLoginModule
 */
public class DatabaseDisconnectionFailureException extends FrameworkCheckedException
{

//************************************************************************    
// Public Methods
//************************************************************************

    /** 
     * Base constructor.
     */
    public DatabaseDisconnectionFailureException()
    {
        super();
    }   

    /** 
     * Constructor with message.
     * 
     * @param message   text of the exception
     */
    public DatabaseDisconnectionFailureException( String message )
    {
        super( message );
    }

    /**
     * Constructor with a Throwable for chained exception and a message.
     * 
     * @param message
     * @param exception
     */
    public DatabaseDisconnectionFailureException( String message, Throwable exception )
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
 * $Log: DatabaseDisconnectionFailureException.java,v $
 */




