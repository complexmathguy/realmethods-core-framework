/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.validator;

//***************************
//Imports
//***************************

import com.framework.common.FrameworkBaseObjectFactory;

/**
 * Factory for IFrameworkValidator implementations
 * <p>
 * Turns caching on in the base class, meaning it will contain nothing
 * but singleton instances of any supporting class types.
 * <p>
 * @author		realMethods, Inc.
 */
public class FrameworkValidatorFactory
	extends FrameworkBaseObjectFactory
{

//************************************************************************    
// Constructors
//************************************************************************
	/**
	 * default constructor - deter outside instantiation, use getInstance().
	 * <p>
	 * Turns caching on in the base class, meaning it will contain nothing
	 * but singleton instances of any supporting class types.
	 */
	protected FrameworkValidatorFactory()
	{
	    super( true /*caching on*/ );
	}

//************************************************************************    
// Public Methods
//************************************************************************

	/**
	 * Returns an instance of the class name, as IFrameworkValidator type.
	 * <p>
	 * The instance returned is expected to thread-safe, since only one will
	 * of any class type will be created. 
	 */
	public IFrameworkValidator getFrameworkValidator( String className )
	{
		return( (IFrameworkValidator)getObject( className ) );
	}

	/**
	 * factory method
	 * @return		the factory itself
	 */
	static final public synchronized FrameworkValidatorFactory getInstance()
	{
		if ( instance == null )
			instance = new FrameworkValidatorFactory();
			
		return( instance );			
	}
	
//*************************************************************************    
// Protected / Protected Methods
//************************************************************************

//************************************************************************    
// Attributes
//************************************************************************

	/**
	 * singleton factory instance
	 */
	static private FrameworkValidatorFactory instance = null;

}

/*
 * Change Log:
 * $Log$
 */
