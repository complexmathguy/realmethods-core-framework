/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.parameter;

//************************************
// Imports
//************************************

/**
 * Maps a Long (long) type to a java.sql.Types.BIGINT
 *
 * <p>
 * @author    realMethods, Inc.
 */
public class LongParameter extends Parameter
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * constructor used to create a LongParameter
     *
     * @param   l
     * @param   isInParameter    in or out param indicator
     */
    public LongParameter( Long l, boolean isInParameter )
    {
        super( l, java.sql.Types.BIGINT, isInParameter );       
    }

    /**
     * constructor used to create a LongParameter
     *
     * @param   l
     * @param   isInParameter    in or out param indicator
     */
    public LongParameter( long l, boolean isInParameter )
    {
        super( new Long(l), java.sql.Types.BIGINT, isInParameter );       
    }

}

/*
 * Change Log:
 * $Log: LongParameter.java,v $
 */




