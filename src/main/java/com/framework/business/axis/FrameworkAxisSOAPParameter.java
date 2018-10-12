/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.business.axis;

import javax.xml.namespace.QName;

import javax.xml.rpc.ParameterMode;

import com.framework.common.FrameworkBaseObject;

/**
 * Helper class used to encapsulate Axis method invocation parameters.
 * <p>
 * @author    realMethods, Inc.
 * @see		  com.framework.business.axis.ApacheAxisHelper
 */
public class FrameworkAxisSOAPParameter extends FrameworkBaseObject
{
	
// constructors

	/**
	 * Delegates to the overloaded version, defaulting the ParameterMode to ParameterMode.IN.
	 * <p>
	 * @param	name		parameter name
	 * @param	parameter	the parameter itself
	 * @param	xmlType		registered xml type
	 */
	public FrameworkAxisSOAPParameter( String name, Object parameter, QName xmlType )
	{
		this( name, parameter, xmlType, ParameterMode.IN );
	}
	
	/**
	 * 
	 * @param	name		parameter name
	 * @param	parameter	the parameter itself
	 * @param	xmlType		registered xml type
	 * @param	paramMode	parameter direction, either IN OR OUT 
	 */
	public FrameworkAxisSOAPParameter( String name, Object parameter, QName xmlType, ParameterMode paramMode )
	{
		this.name 			= name;
		this.parameter 		= parameter;
		this.parameterMode 	= paramMode;
		this.xmlType 		= xmlType;
		
	}
		
// access methods

	/**
	 * Returns the name of the parameter.
	 * @return	String
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Returns the registered xml type.
	 * @return	QName
	 */
	public QName getXMLType()
	{
		return xmlType;
	}
	
	/**
	 * Returns the parameter.
	 * @return 	the parameter data
	 */
	public Object getParameter()
	{
		return parameter;
	}

	/**
	 * Returns the parameter direction as a ParameterMode.
	 * @return	parameter mode  - in or out
	 */
	public ParameterMode getParameterMode()
	{
		return parameterMode;
	}
	
// attributes

	/**
	 * parameter name
	 */
	private String name	= null;	
	/**\
	 * registered xml type
	 */
	private QName xmlType = null;
	/**
	 * the parameter itself
	 */
	private Object parameter = null;
	/**
	 * parameter direction
	 */
	private ParameterMode parameterMode = null;

}
/*
 * Change Log:
 * $Log:  $
 */
