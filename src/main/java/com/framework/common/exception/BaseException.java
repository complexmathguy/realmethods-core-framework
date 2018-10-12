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
 * Deprecated Base class of all Framework checked exception types.  Now extends
 * FrameworkCheckedException
 * <p>
 * @deprecated
 * <p>
 * @author    realMethods, Inc.
 */
public class BaseException extends FrameworkCheckedException
{
//************************************************************************    
// Public Methods
//************************************************************************
    /** 
     * Base constructor.
     */
    public BaseException()
    {
        // call base class
        super();    
    }

    /** 
     * Constructor with message.
     * @param	message
     */
    public BaseException( String message )
    {
        super(message);   

    }    

    /**
     * Constructor with a Throwable for chained exception and a message.
     * @param message 	text of the exception
     * @param exception Throwable that is the prior chained exception.
     */
    public BaseException( String message, Throwable exception)
    {
        // call base class
        super(message, exception );
    }


    
//************************************************************************    
// Private / Protected Methods
//************************************************************************

//************************************************************************    
// Attributes
//************************************************************************

}

/*
 * $Log: BaseException.java,v $
 * Revision 1.2  2003/08/05 12:16:17  tylertravis
 */


