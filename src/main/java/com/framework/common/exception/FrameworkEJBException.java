/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.exception;

//************************************
// Imports
//************************************

import javax.ejb.EJBException;

/**
 * Exception class for generated Framework-based EJB exceptions.
 * <p>
 * @author    realMethods, Inc.
 */
public class FrameworkEJBException extends EJBException
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * default constructor
     */
    public FrameworkEJBException()
    {
        frameworkCheckedException = new FrameworkCheckedException();
    }

    /**
     * constructor
     *
     @param message   text of the exception
     */
    public FrameworkEJBException( String message )
    {
        super( message );
        frameworkCheckedException = new FrameworkCheckedException( message );
    }
    
    /**
     * Constructor with a Throwable for chained exception and a message.
     * @param message text of the exception
     * @param exceptionIn Throwable that is the prior chained exception.
     */
    public FrameworkEJBException( String message,
                                        Throwable exceptionIn )
    {
        super( message );
        frameworkCheckedException = new FrameworkCheckedException( message, exceptionIn );
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
 * $Log: FrameworkEJBException.java,v $
 */
