/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.exception;

//************************************
// Imports
//************************************

import javax.ejb.FinderException;

/**
 * Exception class for failing to find a Framework-based EJB.
 * <p>
 * @author    realMethods, Inc.
 */
public class FrameworkEJBFinderException extends FinderException
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * default constructor
     */
    public FrameworkEJBFinderException()
    {
        frameworkCheckedException = new FrameworkCheckedException();
    }

    /**
     * constructor
     *
     * @param 	message
     */
    public FrameworkEJBFinderException( String message )
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
    public FrameworkEJBFinderException( String message, Throwable exception )
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
 * $Log: FrameworkEJBFinderException.java,v $
 */
