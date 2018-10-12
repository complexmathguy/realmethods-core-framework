/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.validator;

//***************************
//Imports
//***************************

import com.framework.common.context.IFrameworkContext;

import com.framework.common.exception.DataValidationException;

/**
 * Common interface for all data validators.
 * <p>
 * Typically, the implementation class will make use of a specialized IFrameworkContext instance
 * in order to further refine the scope of a context.
 * <p>
 * @author		realMethods, Inc.
 */
public interface IFrameworkValidator
{
	/**
	 * Validation method against a context.  
	 * <p>
	 * @param 		context						the context by which to validate under
	 * @return									result of the validation
	 * @exception	DataValidationException		thrown if validation fails
	 */
	 public Object validate( IFrameworkContext context )
	 throws DataValidationException;
}

/*
 * Change Log:
 * $Log$
 */
