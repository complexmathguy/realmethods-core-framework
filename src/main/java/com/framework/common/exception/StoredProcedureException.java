/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.exception;

//***********************************
// Imports
//***********************************
import java.sql.SQLException;

/**
 * Exception class for failure to execute a stored procedure
 * 
 * <p>
 * @author    realMethods, Inc.
 * @see		  com.framework.integration.objectpool.db.DatabaseQuerier
 */
public class StoredProcedureException extends DatabaseActionException
{

//************************************************************************    
// Public Methods
//************************************************************************

    /** 
     * Base constructor.
     */
    public StoredProcedureException()
    {
        super();
    }

   /** Constructor with message.
     *
     * @param message
     */
    public StoredProcedureException( String message )
    {
        super(message );    
    }
    
    /** Constructor with message.
     *
     * @param message
     * @param sqlExc
     */
    public StoredProcedureException( String message, SQLException sqlExc )
    {
        super(message, sqlExc );    
    }

    /** Constructor with message.
     *
     * @param message
     * @param throwable
     */
    public StoredProcedureException( String message, Throwable throwable )
    {
        super(message, throwable );    
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
 * $Log: StoredProcedureException.java,v $
 */
