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
 * Maps a Short (short) to a java.sql.Types.SMALLINT.
 * <p>
 * @author    realMethods, Inc.
 */
public class ShortParameter extends Parameter
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * constructor used to create a ShortParameter
     *
     * @param   s
     * @param   isInParameter    in or out param indicator
     */
    public ShortParameter( Short s, boolean isInParameter )
    {
        super( s, java.sql.Types.SMALLINT, isInParameter );       
    }

    /**
     * constructor used to create a ShortParameter
     *
     * @param   s
     * @param   isInParameter    in or out param indicator
     */
    public ShortParameter( short s, boolean isInParameter )
    {
        super( new Short(s), java.sql.Types.SMALLINT, isInParameter );       
    }

}

/*
 * Change Log:
 * $Log: ShortParameter.java,v $
 */



