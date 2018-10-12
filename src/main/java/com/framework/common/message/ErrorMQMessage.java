/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.message;

//************************************
// Imports
//************************************

/**
 * Base class for error related MQ messages
 * <p>
 * @author    realMethods, Inc.
 * @see		  com.framework.integration.objectpool.mq.MQConnectionImpl
 */
public class ErrorMQMessage extends FrameworkMQMessage
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * default constructor - deter creation
     */
    protected ErrorMQMessage()
    {
    }

    /**
     * constructor
     *
     * @param       msg
     * @param       severityLevel				INFORMATIONAL, WARNING, CRITICAL
     * @exception	IllegalArgumentException 	thrown if the severityLevel is not within the 
     * 											bounds of INFORMATAIONAL and CRITICAL
     */
    public ErrorMQMessage( String msg, int severityLevel )
    throws IllegalArgumentException
    {
        super( msg );

        if ( severityLevel < INFORMATIONAL || severityLevel > CRITICAL )
            throw new IllegalArgumentException( "ErrorMQMessage constructor - severityLevel out of range " + String.valueOf( severityLevel ) );

        this.severityLevel = severityLevel;            
    }

    /**
     * Returns the associated message in String form
     * 
     * @return      String
     */
    public String getMessageAsString()
    {
        StringBuffer sBuf = new StringBuffer( super.getMessageAsString() );

        sBuf.append( "|**|**|" );
        sBuf.append( String.valueOf( severityLevel ) );

        return( sBuf.toString() );
    }

// attributes

    /**
     * severity level
     */
    protected int severityLevel           = -1;

	/**
	 * information severitiy indicator
	 */
    public static final int INFORMATIONAL   = 0;
	/**
	 * warning severitiy indicator
	 */    
    public static final int WARNING         = 1;
	/**
	 * critical severitiy indicator
	 */    
    public static final int CRITICAL        = 2;

}

/*
 * Change Log:
 * $Log: ErrorMQMessage.java,v $
 * Revision 1.2  2003/08/05 12:16:17  tylertravis
 * - Updated header language to LGPL
 * - Improved exception handling globally
 * - Modified member variables to conform to Java Beans std.
 *
 * Revision 1.1.1.1  2003/07/05 03:05:31  tylertravis
 * initial sourceforge cvs revision
 *
 */




