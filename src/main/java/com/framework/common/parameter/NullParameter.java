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
 * Maps a null value to java.sql.Types.NULL.
 * <p>
 * @author    realMethods, Inc.
 */
public class NullParameter extends Parameter
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * constructor used to create a NullParameter
     * <p>
     * @param	isInParameter		in/out indicator	
     */
    public NullParameter( boolean isInParameter )
    {
        super( null, java.sql.Types.NULL, isInParameter );       
    }
}


/*
 * Change Log:
 * $Log: NullParameter.java,v $
 */



