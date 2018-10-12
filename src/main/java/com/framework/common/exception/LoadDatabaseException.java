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
 * Exception class for failures in database loads.
 * 
 * <p>
 * @author    realMethods, Inc.
 */
public class LoadDatabaseException extends DatabaseActionException
{

//************************************************************************    
// Public Methods
//************************************************************************

    /** 
     * Base constructor.
     */
    public LoadDatabaseException()
    {
        super();
    }

    /** Constructor with message.
     *
     * @param message
     */
    public LoadDatabaseException( String message )
    {
        super(message );    
    }
    
    /** Constructor with message.
     *
     * @param message
     * @param sqlExc
     */
    public LoadDatabaseException( String message, SQLException sqlExc )
    {
        super(message, sqlExc );
    }

    /** Constructor with message.
     *
     * @param message
     * @param throwable
     */
    public LoadDatabaseException( String message, Throwable throwable )
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
 * $Log: LoadDatabaseException.java,v $
 * Revision 1.2  2003/08/05 12:16:17  tylertravis
 * - Updated header language to LGPL
 * - Improved exception handling globally
 * - Modified member variables to conform to Java Beans std.
 *
 * Revision 1.1.1.1  2003/07/05 03:05:18  tylertravis
 * initial sourceforge cvs revision
 *
 */
