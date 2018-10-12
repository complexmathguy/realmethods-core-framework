/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/

package com.framework.business.bo;

//***********************
// Imports
//***********************

/** 
 * Base interface for all Business Objects
 * <p> 
 * @author    realMethods, Inc.
 */
public interface IFrameworkBusinessObject 
	extends com.framework.business.vo.IFrameworkValueObject
{
    /**
     * Performs a deep copy into the provided object.
     * @param object 					copy target
     * @exception IllegalArgumentException 	Thrown if the passed in obj is null. It is also
     * 							thrown if the passed in businessObject is not of the correct type.
     */
    public void copyInto( IFrameworkBusinessObject object ) 
    throws IllegalArgumentException;	
}

/*
 * Change Log:
 * $Log: IFrameworkBusinessObject.java,v $
 */
