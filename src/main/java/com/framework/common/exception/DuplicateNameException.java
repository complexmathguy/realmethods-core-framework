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
public class DuplicateNameException extends FrameworkCheckedException
{

// constructors
	
    public DuplicateNameException()
    {
        super();
    }
       
    public DuplicateNameException( String message )
    {
        super( message );
    }    
    
	/**
	 * Constructor with a Throwable for chained exception and a message.
     * @param message
     * @param exception
	 */
	public DuplicateNameException( String message, Throwable exception )
	{
		super( message, exception ); 
	}    
}

/*
 * Change Log:
 * $Log: DuplicateNameException.java,v $
 */



