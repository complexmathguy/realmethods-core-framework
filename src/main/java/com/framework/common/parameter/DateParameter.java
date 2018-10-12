/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.common.parameter;

/**
 * Used to map a java.sql.Date, java.util.Calendar, or java.sql.Date, to a java.sql.Types.DATE
 *
 * <p>
 * @author    realMethods, Inc.
 */
public class DateParameter extends Parameter
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * constructor used to create a DateParameter
     *
     * @param   date
     * @param   isInParameter    in or out param indicator
     */
    public DateParameter( java.sql.Date date, boolean isInParameter )
    {
        super( date, java.sql.Types.DATE, isInParameter );       
    }

    /**
     * constructor used to create a DateParameter
     *
     * @param   date
     * @param   isInParameter    in or out param indicator
     */
    public DateParameter( java.util.Date date, boolean isInParameter )
    {
        super( (date == null) ? null : new java.sql.Date( date.getTime() ), java.sql.Types.DATE, isInParameter );       
    }
    
    /**
     * constructor used to create a DateParameter
     *
     * @param   date
     * @param   isInParameter    in or out param indicator
     */
    public DateParameter( java.util.Calendar date, boolean isInParameter )
    {
        super( (date == null) ? null : new java.sql.Date( date.getTime().getTime() ), java.sql.Types.DATE, isInParameter );       
    }
}

/*
 * Change Log:
 * $Log: DateParameter.java,v $
 */
