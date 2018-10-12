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
 * Used to indicate an error occured in generic processing.
 * <p>
 * @author    realMethods, Inc.
 * @see		  com.framework.presentation.workerbean.HTTPWorkerBean
 * @see		  com.framework.presentation.workerbean.WorkerBean
 */
public class ProcessingException extends FrameworkCheckedException
{

//************************************************************************    
// Public Methods
//************************************************************************

    /** 
     * Base constructor.
     */
    public ProcessingException()
    {
        super(); 
    }

    /** Constructor with message.
     * @param message text of the exception
     */
    public ProcessingException( String message )
    {
        super( message ); 
    }

    /**
     * Constructor with a Throwabe for chained exception and a message.
     * 
     * @param message
     * @param exception
     */
    public ProcessingException( String message, Throwable exception )
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
 * $Log: ProcessingException.java,v $
 * Revision 1.2  2003/08/05 12:16:17  tylertravis
 * - Updated header language to LGPL
 * - Improved exception handling globally
 * - Modified member variables to conform to Java Beans std.
 *
 * Revision 1.1.1.1  2003/07/05 03:05:18  tylertravis
 * initial sourceforge cvs revision
 *
 */




