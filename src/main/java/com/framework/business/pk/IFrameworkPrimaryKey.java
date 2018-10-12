/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.business.pk;

//***************************
//Imports
//***************************

import java.util.Collection;

/**
 * Standard interface for an aggregation of objects which represent a single unique key.
 * <p>
 * Used in the unique identity related to FrameworkValueObjects.
 * <p> 
 * @author    realMethods, Inc.
 * @see		  com.framework.business.vo.FrameworkValueObject
 * @see		  com.framework.common.parameter.Parameter
 */
public interface IFrameworkPrimaryKey
{
	/**
	 * Retrieves the values associated with the implementation of this key.
	 * 
	 * @return	as an array of values
	 */
	public Object[] values();
    
	/**
	 * Retrieves the values as a Collection of Parameters.
	 * @return 	Parameters
	 */
	public Collection valuesAsParameters();	
	
    /**
     * Returns a String representation.
     * @return	String
     */
    public String toString();
    
	/**
	 * Are the contained key values non-null.
	 * @return boolean
	 */
	public boolean isEmpty();
	
	/**
	 * Returns true if a non-null, non-default value has been assigned
	 * as a primary key value.
	 * 
	 * @return	boolean
	 */
	public boolean hasBeenAssigned();
    
}

/*
 * Change Log:
 * $Log: IFrameworkPrimaryKey.java,v $
 */
