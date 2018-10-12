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
 * Used to encapsulate a parameter of sql type Integer.  Use IntegerParameter instead.
 *
 * <p>
 * @author    realMethods, Inc.
 */
public class IntParameter extends Parameter
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * constructor used to create a IntParameter
     *
     * @param   i
     * @param   isInParameter    in or out param indicator
     */
    public IntParameter( Integer i, boolean isInParameter )
    {
        super( i, java.sql.Types.INTEGER, isInParameter );       
    }

    /**
     * constructor used to create a Integer Paramter
     *
     * @param   i
     * @param   isInParameter    in or out param indicator
     */
    public IntParameter( int i, boolean isInParameter )
    {
        super( new Integer(i), java.sql.Types.INTEGER, isInParameter );       
    }

}

/*
 * Change Log:
 * $Log: IntParameter.java,v $
 */




