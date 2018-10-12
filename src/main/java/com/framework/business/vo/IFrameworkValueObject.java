/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.business.vo;

//***********************
// Imports
//***********************
import com.framework.business.pk.FrameworkPrimaryKey;

import com.framework.common.IFrameworkBaseObject;

/** 
 * Use FrameworkBusinessObject instead.  This class remains for backwards compatibility.
 * <p> 
 * @author    realMethods, Inc.
 */
public interface IFrameworkValueObject extends IFrameworkBaseObject
{
        
    /**
     * Returns the associated IFrameworkPrimaryKey
     * @return IFrameworkPrimaryKey
     */
    public FrameworkPrimaryKey getFrameworkPrimaryKey();            

	/**
	 * Assigns the provided IFrameworkPrimaryKey
	 * @param  	 	fpk
	 * @exception	IllegalArgumentException
	 */    
	public void setFrameworkPrimaryKey( FrameworkPrimaryKey fpk )
	throws IllegalArgumentException;     
    
	/**
	 * Stringifies the object.
	 * @return String
	 */
	public String toString();
    
    /**
     * Performs a deep copy.
     * 
     * @param 		valueObjectIn 				what to copy from 
     * @exception 	IllegalArgumentException 
     */
    public void copy(IFrameworkValueObject valueObjectIn ) 
    throws IllegalArgumentException;   
    
    /**
     * Returns the version id for this object instance.
     * A value of -1 denotes it doesn't have a version
     * @return  Long
     */
    public Long getVersionID();
    
    /**
     * Applies the id to the version value for this object. 
     * @param    	id
     * @exception	IllegalArgumentException	thrown if the id is less than the current 
     * 											established value.
     */
    public void setVersionID( Long id )
    throws IllegalArgumentException;
    
    /**
     * Indicates if this instance is versioned or not.
     * @return  boolean
     */
    public boolean isVersioned();
    
    /**
     * Turns versioning on or off.  It is best to call this method 
     * during construction

     * @param      apply
     */
    public void applyVersioning( boolean apply );
    
        
}

/*
 * Change Log:
 * $Log: IFrameworkValueObject.java,v $
 */
