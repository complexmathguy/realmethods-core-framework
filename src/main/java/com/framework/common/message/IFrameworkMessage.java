/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.message;

//************************************
// Imports
//************************************

/**
 * Common interface for all Framework related message classes.
 * <p>
 * @author    realMethods, Inc.
 */
public interface IFrameworkMessage 
    extends java.io.Serializable
{
    /**
     * Returns the object associated with
     *
     * @return          Object
     */
    public Object getMessage();

    /**
     * assign the mesage object
     *
     * @param   o
     */
    public void setMessage( Object o );

    /**
     * Returns the associated message in String form
     * 
     * @return      String
     */
    public String getMessageAsString();
    
}

/*
 * Change Log:
 * $Log: IFrameworkMessage.java,v $
 */




