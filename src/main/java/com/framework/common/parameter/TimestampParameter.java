/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.common.parameter;

/**
 * Maps a java.sql.Timestamp to a java.sql.Types.TIMESTAMP
 * <p>
 * @author    realMethods, Inc.
 */
public class TimestampParameter extends Parameter
{
//****************************************************
// Public Methods
//****************************************************

    /**
     * constructor used to create a TimestampParameter
     *
     * @param   timestamp
     * @param   isInParameter	isInParameter    in or out param indicator
     */
    public TimestampParameter( java.sql.Timestamp timestamp, boolean isInParameter )
    {
        super( timestamp, java.sql.Types.TIMESTAMP, isInParameter );       
    }
}


/*
 * Change Log:
 * $Log: TimeStampParameter.java,v $
 */
