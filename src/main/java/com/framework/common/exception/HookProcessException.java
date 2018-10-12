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
 * Exception class for hook processing related errors
 * <p>
 * @author    realMethods, Inc.
 * @see		  com.framework.integration.hook.ICheckedExceptionThrownHook
 * @see		  com.framework.integration.hook.ICommandExecutionFailureHook
 * @see		  com.framework.integration.hook.IHttpServletRequestProcessorErrorHook
 * @see		  com.framework.integration.hook.IPageRequestProcessorErrorHook
 * @see		  com.framework.integration.hook.IPostCommandExecuteHook
 * @see		  com.framework.integration.hook.IPostHttpServletRequestProcessorHook
 * @see		  com.framework.integration.hook.IPostPageRequestProcessorHook
 * @see		  com.framework.integration.hook.IPostTaskExecuteHook
 * @see		  com.framework.integration.hook.IPreCommandExecuteHook
 * @see		  com.framework.integration.hook.IPreHttpServletRequestProcessorHook	
 * @see		  com.framework.integration.hook.IPrePageRequestProcessorHook
 * @see		  com.framework.integration.hook.IPreTaskExecuteHook
 * @see		  com.framework.integration.hook.ITaskExecutionFailureHook
 * @see		  com.framework.integration.hook.IUserSessionUnBoundHook
 * @see		  com.framework.common.exception.HookProcessException
 */
public class HookProcessException extends FrameworkCheckedException
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * default constructor
     */
    public HookProcessException()
    {
    }

    /**
     * constructor
     *
     * @param message   text of the exception
     */
    public HookProcessException( String message )
    {
        super( message );
    }
    
    /**
     * Constructor with a Throwable for chained exception and a message.
     * @param message
     * @param exception
     */
    public HookProcessException( String message, Throwable exception )
    {
        super( message, exception ); 
    }
    
}

/*
 * Change Log:
 * $Log: HookProcessException.java,v $
 * Revision 1.1  2003/08/05 12:41:08  tylertravis
 * - initial release
 *
 *
 */




