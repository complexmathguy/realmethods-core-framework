/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.business.pk;

//***************************
//Imports
//***************************


/**
 * Standard interface for single object which represent a single unique key.
 * <p>
 * Possibley used in the unique identity related to FrameworkValueObjects.
 * <p> 
 * @author    realMethods, Inc.
 * @see		  com.framework.business.vo.FrameworkValueObject
 * @see		  com.framework.common.parameter.Parameter
 */
public interface ISimplePrimaryKey
{	
	public Object getID(); 
    
}

/*
 * Change Log:
 * $Log: IFrameworkPrimaryKey.java,v $
 */
