/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.business.axis;

import javax.xml.namespace.QName;

import com.framework.common.FrameworkBaseObject;

/**
 * Helper class used to encapsulate Axis type mapping data, required in making a Call.
 * <p>
 * @author    realMethods, Inc.
 * @see		  com.framework.business.axis.ApacheAxisHelper
 */
public class FrameworkAxisTypeMapping extends FrameworkBaseObject
{
// constructors
	/**
	 * deter usage
	 */
	private FrameworkAxisTypeMapping()
	{}
	
	/**
	 * sole constructor
	 * <p>
	 * @param mappingType			class type to map to
	 * @param xmltype				registered xml name of the type
	 * @param serializerFactory		class of factory to handle serialization
	 * @param deserializerFactory	class of factory to handle deserialization
	 */
	public FrameworkAxisTypeMapping( 	Class mappingType,
										QName xmltype,
										Class serializerFactory,
										Class deserializerFactory )
	{		
		this.mappingType 			= mappingType;
		this.xmlType 				= xmltype;
		this.serializerFactory  	= serializerFactory;
		this.deserializerFactory	= deserializerFactory;
	}										

// access methods

	/**
	 * Returns the class type to map to.
	 * <p>
	 * @return	Class
	 */
	public Class getMappingType()
	{
		return mappingType;
	}
	
	/**
	 * Returns the registered xml name of the type associcated with the mapping type.
	 * <p>
	 * @return	QName
	 */
	public QName getXMLType()
	{
		return xmlType;
	}

	/**
	 * Returns the Class of the factory to handle serialization
	 * <p>
	 * @return	serialization factory class
	 */	
	public Class getSerializerFactory()
	{
		return serializerFactory;
	}
	
	/**
	 * Returns the Class of the factory to handle deserialization
	 * <p>
	 * @return	deserialization factory class
	 */		
	public Class getDeserializerFactory()
	{
		return deserializerFactory;
	}
	
// Object overloads

	/**
	 * String representation
	 * <p>
	 * @return	String
	 */
	public String toString()
	{
		StringBuffer buf = new StringBuffer();
		
		buf.append( "MappingType:= " );
		buf.append( mappingType );
		buf.append( ", xmlType:=" );
		buf.append( xmlType );
		buf.append( ",  serializerFactory:= " ); 
		buf.append( serializerFactory );
		buf.append( ", deserializerFactory:= " );
		buf.append( deserializerFactory );		
			
		return( buf.toString() );
	}
	
// attributes

	/**
	 * class type to map to
	 */
	private Class mappingType  = null;
	/**
	 * registered xml name of the type
	 */
	private QName xmlType = null;
	/**
	 * class of factory to handle serialization
	 */
	private Class serializerFactory = null;
	/**
	 * class of factory to handle deserialization
	 */
	private Class deserializerFactory = null;
		
}


/*
 * Change Log:
 * $Log:  $
 */
