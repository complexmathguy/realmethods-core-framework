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
 * Exception class for Connection acquisition related errors.
 * <p>
 * @author    realMethods, Inc.
 * @see		  com.framework.integration.objectpool.ConnectionPool
 * @see		  com.framework.integration.objectpool.ConnectionPoolManager
 * @see		  com.framework.integration.security.jaas.DatabaseLoginModule
 * @see		  com.framework.business.ejb.FrameworkDAOSessionBean
 * @see		  com.framework.common.mgr.FrameworkManager
 * 
 */
public class ConnectionAcquisitionException extends FrameworkCheckedException
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * default constructor
     */
    public ConnectionAcquisitionException()
    {
    }

    /**
     * constructor
     *
     * @param message   text of the exception
     */
    public ConnectionAcquisitionException( String message )
    {
        super( message );
    }
    
    /**
     * Constructor with a Throwable for chained exception and a message.
     * @param message
     * @param exception
     */
    public ConnectionAcquisitionException( String message, Throwable exception )
    {
        super( message, exception ); 
    }
    
}

/*
 * Change Log:
 * $Log: ConnectionAcquisitionException.java,v $
 */




