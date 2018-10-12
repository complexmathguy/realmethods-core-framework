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
 * Base interface for all application Enumerators
 * <p> 
 * @author    realMethods, Inc.
 */
public interface IFrameworkEnumerator extends java.io.Serializable
{
	/**
	 * Returns the names and values of the applicable enumerated values
	 * @return	Map
	 */
	public java.util.Map getValues();
	
	public Object getValue();
	
    public String getDescription();
    
    public Integer getIndex();

	/**
	 * Returns a string representation of the object.
	 * @return String
	 */
	public String toString();
}

/*
 * Change Log:
 * $Log: IFrameworkEnumerator.java,v $
 */
