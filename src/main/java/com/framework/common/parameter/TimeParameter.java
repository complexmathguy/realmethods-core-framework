/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.common.parameter;

/**
 * Used to encapsulate a parameter of sql type TimeParameter.
 *
 * <p>
 * @author    realMethods, Inc.
 */
public class TimeParameter extends Parameter
{
//****************************************************
// Public Methods
//****************************************************

    /**
     * constructor used to create a TimeParameter
     *
     * @param   time
     * @param   isInParameter    in or out param indicator    in or out param indicator
     */
    public TimeParameter( java.sql.Time time, boolean isInParameter )
    {
        super( time, java.sql.Types.TIME, isInParameter );       
    }
    
    /**
     * constructor used to create a TimeParameter using a Calendar
     *
     * @param   cal
     * @param   isInParameter    in or out param indicator    in or out param indicator
     */    
    public TimeParameter( java.util.Calendar cal, boolean isInParameter )
    {
        super( new java.sql.Time( cal.getTime().getTime() ), isInParameter );
    }
}

/*
 * Change Log:
 * $Log: TimeParameter.java,v $
 * Revision 1.2  2003/08/05 12:16:17  tylertravis
 * - Updated header language to LGPL
 * - Improved exception handling globally
 * - Modified member variables to conform to Java Beans std.
 *
 * Revision 1.1.1.1  2003/07/05 03:05:37  tylertravis
 * initial sourceforge cvs revision
 *
 */
