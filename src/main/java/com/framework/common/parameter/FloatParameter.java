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
 * Maps a Float (float) to a java.sql.Types.REAL
 *
 * <p>
 * @author    realMethods, Inc.
 */
public class FloatParameter extends Parameter
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * constructor used to create a FloatParameter
     *
     * @param   f
     * @param   isInParameter    in or out param indicator
     */
    public FloatParameter( Float f, boolean isInParameter )
    {
        super( f, java.sql.Types.REAL, isInParameter );       
    }

    /**
     * constructor used to create a FloatParameter
     *
     * @param   f
     * @param   isInParameter    in or out param indicator
     */
    public FloatParameter( float f, boolean isInParameter )
    {
        super( new Float(f), java.sql.Types.REAL, isInParameter );       
    }

}


/*
 * Change Log:
 * $Log: FloatParameter.java,v $
 */



