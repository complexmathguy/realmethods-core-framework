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
 * Exception class for failure to execute a drop stored procedure.
 * <p>
 * @author    realMethods, Inc.
 */
public class DropStoredProcedureException extends DatabaseActionException
{

//************************************************************************    
// Public Methods
//************************************************************************

    /** 
     * Base constructor.
     */
    public DropStoredProcedureException()
    {
        super();
    }

    /** Constructor with message.
     *
     * @param message
     */
    public DropStoredProcedureException( String message )
    {
        super(message );    
    }
    
    /** Constructor with message.
     *
     * @param message
     * @param sqlExc
     */
    public DropStoredProcedureException( String message, SQLException sqlExc )
    {
        super(message, sqlExc );    
    }

    /** Constructor with message.
     *
     * @param message
     * @param throwable
     */
    public DropStoredProcedureException( String message, Throwable throwable )
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
 * $Log: DropStoredProcedureException.java,v $
 */
