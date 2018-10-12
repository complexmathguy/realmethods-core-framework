/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/



package com.framework.common.exception;

//*****************************************
// Imports
//*****************************************
import java.sql.SQLException;

/**
 * Exception class for failures in database inserts.
 * <p>
 * @author    realMethods, Inc.
 * @see		  com.framework.integration.objectpool.db.DatabaseQuerier
 */
public class InsertDatabaseException extends DatabaseActionException
{

//************************************************************************    
// Public Methods
//************************************************************************

    /** 
     * Base constructor.
     */
    public InsertDatabaseException()
    {
        super();
    }

    /** Constructor with message.
     *
     * @param message
     */
    public InsertDatabaseException( String message )
    {
        super(message );    
    }
    
    /** Constructor with message.
     *
     * @param message
     * @param sqlExc
     */
    public InsertDatabaseException( String message, SQLException sqlExc )
    {
        super(message, sqlExc );
    }

    /** Constructor with message.
     *
     * @param message
     * @param throwable
     */
    public InsertDatabaseException( String message, Throwable throwable )
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
 * $Log: InsertDatabaseException.java,v $
 * Revision 1.2  2003/08/05 12:16:17  tylertravis
 * - Updated header language to LGPL
 * - Improved exception handling globally
 * - Modified member variables to conform to Java Beans std.
 *
 * Revision 1.1.1.1  2003/07/05 03:05:18  tylertravis
 * initial sourceforge cvs revision
 *
 */
