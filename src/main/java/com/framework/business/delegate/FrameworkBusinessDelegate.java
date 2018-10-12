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

import com.framework.common.FrameworkBaseObject;

/**
 * Base class of all business delegates generated and/or used within the framework.
 * <p>
 * Used by HttpWorkBeans or framework struts action objects, but should be used by
 * any client wishing to consume an application business service.
 * <p>
 * The only way to assign a business object is through the constructor, meaning once assigned
 * it isn't unassignabled.
 * <p>
 * @author		realMethods, Inc. 
 */
public class FrameworkBusinessDelegate 
	extends FrameworkBaseObject
	implements IFrameworkBusinessDelegate
{
	
//************************************************************************    
// Constructors
//************************************************************************

	/**
	 * deter external instantiation 
	 */
	protected FrameworkBusinessDelegate()
	{}

	/**
	 * Sole constructor, making the provision of a business object mandatory.
	 * <p>
	 * @param 		bo							the business object to wrap
	 * @exception	IllegalArgumentException	thrown if bo is null 
	 */
	public FrameworkBusinessDelegate( IFrameworkBusinessObject bo )
	{
		if ( bo == null )
			throw new IllegalArgumentException( "FrameworkBusinessDelegate() - IFrameworkBusinessObject arg cannot be null." );
			
		businessObject = bo;			
	}

	
//************************************************************************    
// Public Methods
//************************************************************************

	/**
	 * Returns the bound business object
	 * @return		the bound business object
	 */
	public IFrameworkBusinessObject getFrameworkBusinessObject()
	{
		return businessObject;
	}

//*************************************************************************    
// Protected / Protected Methods
//************************************************************************

	/**
	 * Allows internal assignment of a business object.
	 * @param		bo		
	 * @exception	IllegalArgumentException	thrown if bo is null
	 */
	protected void setFrameworkBusinessObject( IFrameworkBusinessObject bo )
	{
		if ( bo == null )
			throw new IllegalArgumentException( "FrameworkBusinessDelegate.setFrameworkBusinessObject() - IFrameworkBusinessObject arg cannot be null." );
			
		businessObject = bo;		
	}
	
//************************************************************************    
// Attributes
//************************************************************************

	private IFrameworkBusinessObject businessObject = null;
}
/*
 * Change Log:
 * $Log$
 */
 
