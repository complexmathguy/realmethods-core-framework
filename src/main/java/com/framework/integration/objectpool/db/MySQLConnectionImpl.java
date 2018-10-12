/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.objectpool.db;

//************************************
// Imports
//************************************
import com.framework.common.exception.ObjectCreationException;

/**
 * MySQL connection implementation.
 * <p>
 * @author    realMethods, Inc.
 */
public class MySQLConnectionImpl extends GenericDBConnectionImpl
{    
    /**
     * default constructor - use the static public void create()
     */
    public MySQLConnectionImpl()
    {
        super(); 
    }
    
    /**
     * Factory helper method for creation, overloaded from GenericDBConnectionImpl
     * <p>
     * @return	    GenericDBConnectionImpl
     * @exception	ObjectCreationException 		
     */
    static public GenericDBConnectionImpl create()
    throws ObjectCreationException
    {
    	try
    	{ 
    		return( new MySQLConnectionImpl() );
    	}
    	catch( Throwable exc )
    	{
			throw new ObjectCreationException( "MySQLConnectionImpl:create() - " + exc, exc );
    	}
    }
    
}

/*
 * Change Log:
 * $Log: MySQLConnectionImpl.java,v $
 * Revision 1.2  2003/08/05 12:16:18  tylertravis
 * - Updated header language to LGPL
 * - Improved exception handling globally
 * - Modified member variables to conform to Java Beans std.
 *
 * Revision 1.1.1.1  2003/07/05 03:05:44  tylertravis
 * initial sourceforge cvs revision
 *
 */
