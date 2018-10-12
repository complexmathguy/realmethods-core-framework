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
 * Exception class for failing to remove a Framework-based EJB.
 *
 * <p>
 * @author    realMethods, Inc.
 * @see		  com.framework.business.ejb.FrameworkDAOEntityBean
 */
public class FrameworkEJBRemoveException extends javax.ejb.RemoveException
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * default constructor
     */
    public FrameworkEJBRemoveException()
    {
        frameworkCheckedException = new FrameworkCheckedException();
    }

    /**
     * constructor
     *
     @param message   text of the exception
     */
    public FrameworkEJBRemoveException( String message )
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
    public FrameworkEJBRemoveException( String message, Throwable exception )
    {
        super( message );
        frameworkCheckedException = new FrameworkCheckedException( message, exception );
    }
    
    /**
     * Returns the contained BaseException
     *
     * @return  FrameworkCheckedException
     */
    public FrameworkCheckedException getFrameworkCheckedException()
    {
        return( frameworkCheckedException );
    }
    
// attributes

	/**
	 * wrapped framework base exception
	 */
    protected FrameworkCheckedException frameworkCheckedException = null;
    
}

/*
 * Change Log:
 * $Log: FrameworkEJBRemoveException.java,v $
 * Revision 1.2  2003/08/05 12:16:17  tylertravis
 * - Updated header language to LGPL
 * - Improved exception handling globally
 * - Modified member variables to conform to Java Beans std.
 *
 * Revision 1.1.1.1  2003/07/05 03:05:18  tylertravis
 * initial sourceforge cvs revision
 *
 */
