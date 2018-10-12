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
 * Used to map a String to a java.sql.Types.LONGVARCHAR
 *
 * <p>
 * @author    realMethods, Inc.
 */
public class StringParameter extends Parameter
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * constructor used to create a StringParamter
     *
     * @param   s
     * @param   isInParameter    in or out param indicator    isInParameter    in or out param indicator
     */
    public StringParameter( String s, boolean isInParameter )
    {
        super( s, java.sql.Types.LONGVARCHAR, isInParameter );       
    }
}


/*
 * Change Log:
 * $Log: StringParameter.java,v $
 */



