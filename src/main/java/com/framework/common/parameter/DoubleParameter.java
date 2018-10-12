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
 * Maps a Double (double) to a java.sql.Types.DOUBLE
 *
 * <p>
 * @author    realMethods, Inc.
 */
public class DoubleParameter extends Parameter
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * constructor used to create a DoubleParameter
     *
     * @param   d
     * @param   isInParameter    in or out param indicator
     */
    public DoubleParameter( Double d, boolean isInParameter )
    {
        super( d, java.sql.Types.DOUBLE, isInParameter );       
    }

    /**
     * constructor used to create a DoubleParameter
     *
     * @param   d
     * @param   isInParameter    in or out param indicator
     */
    public DoubleParameter( double d, boolean isInParameter )
    {
        super( new Double(d), java.sql.Types.DOUBLE, isInParameter );       
    }

}


/*
 * Change Log:
 * $Log: DoubleParameter.java,v $
 */



