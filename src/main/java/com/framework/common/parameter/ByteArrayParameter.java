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
 * Maps a byte array to a java.sql.Types.LONGVARBINARY.
 * <p>
 * @author    realMethods, Inc.
 */
public class ByteArrayParameter extends Parameter
{
//****************************************************
// Public Methods
//****************************************************
    /**
    * constructor used to create a ByteArrayParameter
    *
    * @param   value
    * @param   isInParameter    in or out param indicator
    */
    public ByteArrayParameter( byte[] value, boolean isInParameter )
    {
        super( value, java.sql.Types.LONGVARBINARY, isInParameter );       
    }

}


/*
 * Change Log:
 * $Log: ByteArrayParameter.java,v $
 */



