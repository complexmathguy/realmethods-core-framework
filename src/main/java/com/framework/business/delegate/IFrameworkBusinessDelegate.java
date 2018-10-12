/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.business.delegate;

//***************************
//Imports
//***************************

import com.framework.business.bo.IFrameworkBusinessObject;

/**
 * Commmon interface for business delegates generated and used within the framework.
 * <p>
 * @author		realMethods, Inc. 
 */
public interface IFrameworkBusinessDelegate
{
	
	/**
	 * Returns the bound business object
	 * @return		the bound bound object
	 */
	public IFrameworkBusinessObject getFrameworkBusinessObject();	
}

/*
 * Change Log:
 * $Log$
 */
