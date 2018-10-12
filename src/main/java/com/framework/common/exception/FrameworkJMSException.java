/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.exception;

//************************************
// Imports
//************************************

import javax.jms.JMSException;

/**
 * Exception class for generated Framework-based JMS exception.
 *
 * <p>
 * @author    realMethods, Inc.
 * @see		  com.framework.integration.objectpool.jms.JMSImpl
 * @see		  com.framework.integration.objectpool.jms.JMSQueueImpl
 * @see		  com.framework.integration.objectpool.jms.JMSTopicImpl
 */
public class FrameworkJMSException extends JMSException
{
//****************************************************
// Public Methods
//****************************************************

    /**
     * constructor
     *
     * @param       message
     */
    public FrameworkJMSException( String message )
    {
        super( message );
        frameworkCheckedException = new FrameworkCheckedException( message );
    }
    
    /**
     * Constructor with a Throwable for chained exception and a message.
     * @param message text of the exception
     * @param exception Throwable that is the prior chained exception.
     */
    public FrameworkJMSException( String message, Throwable exception )
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

    protected FrameworkCheckedException frameworkCheckedException = null;
    
}

/*
 * Change Log:
 * $Log: FrameworkJMSException.java,v $
 */
