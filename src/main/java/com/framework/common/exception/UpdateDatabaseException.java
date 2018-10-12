/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.exception;

//*****************************************
// Imports
//*****************************************

/**
 * Exception class for failures in database updates.
 * 
 * <p>
 * @author    realMethods, Inc.
 * @see       com.framework.integration.objectpool.db.DatabaseQuerier
 */
public class UpdateDatabaseException extends FrameworkCheckedException
{
//************************************************************************    
// Public Methods
//************************************************************************

    /** 
     * Base constructor.
     */
    public UpdateDatabaseException()
    {
        super();
    }

    /** 
     * Constructor with message
     * @param message   text of the exception
     */
    public UpdateDatabaseException( String message )
    {
        super(message);    
    }

    /**
     * Constructor with a Throwable for chained exception and a message.
     * 
     * @param message
     * @param exception
     */
    public UpdateDatabaseException( String message,
                                    Throwable exception )
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
 * $Log: UpdateDatabaseException.java,v $
 */




