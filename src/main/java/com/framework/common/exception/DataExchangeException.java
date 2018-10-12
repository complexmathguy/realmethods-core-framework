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
 * Exception class for failure to exchange data between two sources
 * <p>
 * @author    realMethods, Inc.
 */
public class DataExchangeException extends FrameworkCheckedException
{

// constructors
	
    public DataExchangeException()
    {
        super();
    }
       
    public DataExchangeException( String message )
    {
        super( message );
    }    
    
	/**
	 * Constructor with a Throwable for chained exception and a message.
     * @param message
     * @param exception
	 */
	public DataExchangeException( String message, Throwable exception )
	{
		super( message, exception ); 
	}    
}

/*
 * Change Log:
 * $Log: DataExchangeException.java,v $
 */



