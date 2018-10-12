/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.exception;

/**
 * Exception class for when accessing a Framework hook fails.
 * <p>
 * @author    realMethods, Inc.
 * @see		  com.framework.integration.hook.FrameworkHookManager
 */
public class HookAccessException extends FrameworkCheckedException
{
    /** 
     * Base constructor.
     */
    public HookAccessException()
    {
        super(); 
    }

    /** 
     * Constructor with message.
     * 
     * @param message   text of the exception
     */
    public HookAccessException( String message )
    {
        super( message ); 
    }
    
    /**
     * Constructor with a Throwable for chained exception and a message.
     * 
     * @param message
     * @param exception
     */
    public HookAccessException( String message, Throwable exception )
    {
        super( message, exception ); 
    }

}

/*
 * Change Log:
 * $Log: HookAccessException.java,v $
 * Revision 1.2  2003/08/05 12:16:17  tylertravis
 * - Updated header language to LGPL
 * - Improved exception handling globally
 * - Modified member variables to conform to Java Beans std.
 *
 * Revision 1.1.1.1  2003/07/05 03:05:18  tylertravis
 * initial sourceforge cvs revision
 *
 */




