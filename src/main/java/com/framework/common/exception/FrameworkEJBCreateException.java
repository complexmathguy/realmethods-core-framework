/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.exception;

//************************************
// Imports
//************************************

import javax.ejb.CreateException;

/**
 * Exception class for failing to create a Framework-based EJB.
 * <p>
 * @author    realMethods, Inc.
 */
public class FrameworkEJBCreateException extends CreateException
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * default constructor
     */
    public FrameworkEJBCreateException()
    {
        frameworkCheckedException = new FrameworkCheckedException();
    }

    /**
     * constructor
     *
     @param message   text of the exception
     */
    public FrameworkEJBCreateException( String message )
    {
        super( message );
        frameworkCheckedException = new FrameworkCheckedException( message );
    }
    
    /**
     * Constructor with a Throwable for chained exception and a message.
     * 
     * @param message
     * @param exception
     */
    public FrameworkEJBCreateException( String message, Throwable exception )
    {
        super( message );
        frameworkCheckedException = new FrameworkCheckedException( message, exception );
    }

    /**
     * Returns the contained FrameworkCheckedException
     *
     * @return  FrameworkCheckedException
     */
    public FrameworkCheckedException getFrameworkCheckedException()
    {
        return( frameworkCheckedException );
    }
    
// attributes

    protected FrameworkCheckedException frameworkCheckedException = null;
    
}

/*
 * Change Log:
 * $Log: FrameworkEJBCreateException.java,v $
 */
