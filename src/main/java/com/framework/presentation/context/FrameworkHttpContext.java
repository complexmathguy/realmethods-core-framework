/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.presentation.context;

//***************************
//Imports
//***************************

import javax.servlet.http.HttpServletRequest;

import com.framework.common.context.IFrameworkContext;

/**
 * Defines an Http context and a seed object.
 * <p>
 * Used to better share data within the presenation tier
 * <p>
 * @author		realMethodos
 */
public class FrameworkHttpContext
	implements IFrameworkContext
{

//************************************************************************    
// Constructors
//************************************************************************

	/**
	 * default constructor - deter usage
	 */
	private FrameworkHttpContext()
	{}
	
	/**
	 * sole constructor
	 * <p>
	 * @param 	request		http servlet request
	 * @param	seed		focal point of the context, to possibly interrogate and/or manipulating
	 */
	public FrameworkHttpContext( HttpServletRequest request, Object seed )	
	{
		this.request 	= request;
		this.seed		= seed;
	}
//************************************************************************    
// Public Methods
//************************************************************************
	
	/**
	 * Returns the HttpServletRequest
	 * @return		HttpServletRequest
	 */
	public HttpServletRequest getHttpServletRequest()
	{
		return request;
	}

	/**
	 * A focal object of the given context, to possibly manipulate or interrogate
	 * @return		Object
	 */
	public Object getSeed()
	{
		return seed;
	}
	
	/**
	 * Assign the seed.
	 * @param		seed
	 */
	public void setSeed( Object seed )
	{
		this.seed = seed;
	}
	
//************************************************************************    
// Attributes
//************************************************************************
	/**
	 * http servlet request
	 */
	protected HttpServletRequest request = null;	
	
	/**
	 * seed object
	 */	
	protected Object seed = null;
}

/*
 * Change Log:
 * $Log$
 */
