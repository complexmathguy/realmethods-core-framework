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
 * Exception class for failures related to connecting to a database.
 * <p>
 * @author    realMethods, Inc.
 * @see		  com.framework.integration.objectpool.db.DatabaseQuerier
 * @see		  com.framework.integration.security.jaas.DatabaseLoginModule
 * @see		  com.framework.integration.dao.FrameworkDatabaseDAO
 * 
 */
public class DatabaseConnectionFailureException extends FrameworkCheckedException
{

//************************************************************************    
// Public Methods
//************************************************************************

    /** 
     * Base constructor.
     */
    public DatabaseConnectionFailureException()
    {
        super();
    }


    /** Constructor with message.
     * @param message text of the exception
     */
    public DatabaseConnectionFailureException( String message )
    {
        super( message ); 
    }

    /**
     * Constructor with a Throwable for chained exception and a message.
     * 
     * @param message
     * @param exception
     */
    public DatabaseConnectionFailureException( String message, Throwable exception )
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
 * $Log: DatabaseConnectionFailureException.java,v $
 */




