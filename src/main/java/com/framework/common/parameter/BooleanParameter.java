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
 * Maps a Boolean (boolean) to a java.sql.Types.BIT.
 * <p>
 * @author    realMethods, Inc.
 */
public class BooleanParameter extends Parameter
{
//****************************************************
// Public Methods
//****************************************************
    /**
    * @param   value
    * @param   isInParameter    in or out param indicator
    */
    public BooleanParameter( Boolean value, boolean isInParameter )
    {
        super( value, java.sql.Types.BIT, isInParameter );       
    }

    /**
    * @param   value
    * @param   isInParameter    in or out param indicator
    */
    public BooleanParameter( boolean value, boolean isInParameter )
    {
        super( new Boolean(value), java.sql.Types.BIT, isInParameter );       
    }

}

/*
 * Change Log:
 * $Log: BooleanParameter.java,v $
 * Revision 1.2  2003/08/05 12:16:17  tylertravis
 * - Updated header language to LGPL
 * - Improved exception handling globally
 * - Modified member variables to conform to Java Beans std.
 *
 * Revision 1.1.1.1  2003/07/05 03:05:32  tylertravis
 * initial sourceforge cvs revision
 *
 */




