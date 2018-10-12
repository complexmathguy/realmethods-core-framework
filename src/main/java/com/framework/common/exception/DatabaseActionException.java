/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.exception;

//************************************
// Imports
//************************************
import java.sql.SQLException;

/**
 * Base Exception class for database related actions.
 * 
 * <p>
 * @author    realMethods, Inc.
 */
public class DatabaseActionException extends FrameworkCheckedException
{

//************************************************************************    
// Public Methods
//************************************************************************

    /** 
     * Base constructor.
     */
    public DatabaseActionException()
    {
        super();
    }

    /** Constructor with message.
     *
     * @param message
     */
    public DatabaseActionException( String message )
    {
        super(message );    
    }
    
    /** Constructor with message.
     *
     * @param message
     * @param sqlExc
     */
    public DatabaseActionException( String message, SQLException sqlExc )
    {
        super(message, sqlExc );    
        sqlException = sqlExc;
    }

    /** Constructor with message.
     *
     * @param message
     * @param throwable
     */
    public DatabaseActionException( String message, Throwable throwable )
    {
        super(message, throwable );    
        sqlException = null;
    }
    
	/**
	 * Returns the contained SQLException
	 * 
	 * @return SQLException
	 */
    final public SQLException getSQLException()
    {
        return( sqlException );
    }


//************************************************************************    
// Private / Protected Methods
//************************************************************************

//************************************************************************    
// Attributes
//************************************************************************
    private SQLException   sqlException = null;
}

/*
 * Change Log:
 * $Log: DatabaseActionException.java,v $
 */
