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
 * Used to indicate an error during the result set callback processing.
 *
 * <p>
 * @author    realMethods, Inc.
 * @see       com.framework.integration.objectpool.db.IResultSetCallback		
 * @see		  com.framework.integration.objectpool.db.DatabaseQuerier	
 */
public class ResultSetCallbackException extends FrameworkCheckedException
{

//************************************************************************    
// Public Methods
//************************************************************************

    /** 
     * Base constructor.
     */
    public ResultSetCallbackException()
    {
        super();
    }

    /** Constructor with message.
     * @param message text of the exception
     */
    public ResultSetCallbackException( String message )
    {
        super( message ); 
    }

    /**
     * Constructor with a Throwable for chained exception and a message.
     * 
     * @param message
     * @param exception
     */
    public ResultSetCallbackException( String message, Throwable exception )
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
 * $Log: ResultSetCallbackException.java,v $
 */




