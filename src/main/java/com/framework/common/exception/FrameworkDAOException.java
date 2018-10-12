/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.exception;

/**
 * Exception class for failure of making use of a FrameworkDAO object.
 * <p>
 * @author    realMethods, Inc.
 * @see		  com.framework.integration.dao.FrameworkDAO
 */
public class FrameworkDAOException extends FrameworkCheckedException
{
    /** 
     * Base constructor.
     */
    public FrameworkDAOException()
    {
        super(); 
    }

    /** Constructor with message.
     * @param message text of the exception
     */
    public FrameworkDAOException( String message )
    {
        super( message ); 
    }
    
    /**
     * Constructor with a Throwable for chained exception and a message.
     * 
     * @param message
     * @param exception
     */
    public FrameworkDAOException( String message, Throwable exception )
    {
        super( message, exception ); 
    }
        
}

/*
 * Change Log:
 * $Log: FrameworkDAOException.java,v $
 */




