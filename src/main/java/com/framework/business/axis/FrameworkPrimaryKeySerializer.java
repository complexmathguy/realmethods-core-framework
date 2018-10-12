/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.business.axis;

//***********************************
// Imports
//***********************************

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import com.framework.business.pk.FrameworkPrimaryKey;

import org.apache.axis.Constants;

import org.apache.axis.encoding.DeserializationContext;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.FieldTarget;
import org.apache.axis.encoding.SerializationContext;
import org.apache.axis.encoding.XMLType;

import org.apache.axis.message.SOAPHandler;

import org.apache.axis.wsdl.fromJava.Types;

import org.w3c.dom.Element;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Axis serializer/deserializer for IFrameworkPrimaryKey
 * <p> 
 * @author    realMethods, Inc.
 * @see	      com.framework.business.axis.FrameworkPrimaryKeySerializerFactory
 */
public class FrameworkPrimaryKeySerializer 
	extends org.apache.axis.encoding.DeserializerImpl
    implements org.apache.axis.encoding.Serializer
{

// constructor

	/**
	 * default constructor
	 */
	public FrameworkPrimaryKeySerializer()
	{
		applyTypesByMemberName();
		value  = createPKInstance();
	}	
	
	/**
	 * Serialize an element named name, with the indicated attributes
	 * and value.
	 * @param name 			the element name of the value
	 * @param attributes 	member data of the value being serialized
	 * @param obj	 		target to serialize
	 * @param context
	 */
	public void serialize( QName name, 
							Attributes attributes,
						  	Object obj, 
						  	SerializationContext context )
		throws IOException
	{
	   	if (!(obj instanceof FrameworkPrimaryKey ))
			throw new IOException("FrameworkPrimaryKey.serialize() - Cannot serialize a " + obj.getClass().getName() + " with a FrameworkPrimaryKeyBeanSerializer.");
		
		FrameworkPrimaryKey pk = (FrameworkPrimaryKey)obj;
				
	   	context.startElement(name, attributes);
	   	context.serialize( new QName("", "valuesAsCollection"), null, pk.valuesAsCollection() );		
	   	context.endElement();
	}	
		

// DeserializerImpl overloads

   /**
	* This method is invoked when an element start tag is encountered.
	* @param 	namespace 		element namespace
	* @param 	localName 		is the name of the element
	* @param 	prefix 
	* @param 	attributes 		member data to be deserialized
	* @param 	context 
	* @return	SOAPHadler
	*/
   public SOAPHandler onStartChild( String namespace,
						 			String localName,
						 			String prefix,
						 			Attributes attributes,
						 			DeserializationContext context )
   throws SAXException
   {   	   	   	
	   	QName typeQName = (QName)typesByMemberName.get(localName);	
	   	
	   	if (typeQName == null)
		   return null;

	   	Deserializer dSer = context.getDeserializerForType( typeQName );
	   
		if (dSer == null)
			throw new SAXException("EmployerPrimaryKeyBeanSerializer.onStartChild() - No deserializer for a " + typeQName );
		
	   	try 
	   	{	   	
		   dSer.registerValueTarget( new FieldTarget( value, localName) );
	   	} 
	   	catch (NoSuchFieldException e) 
	   	{			
		   throw new SAXException(e);
	   	}
                
		return (SOAPHandler)dSer;
   	}
   
   /**
    * supported mechanism type
    * @return	Constants.AXIS_SAX
    */
	public String getMechanismType() { return Constants.AXIS_SAX; }

	/**
	 * does nothing with it, and returns null sinceo only one type is supported.
	 * 
	 * @param	javaType
	 * @param	types
	 * @return	null
	 */
	public Element writeSchema( Class javaType, Types types ) 
	throws Exception 
	{
		return null;
	}


//	************************************************************************
//	 Protected / Private Methods
//	************************************************************************

	/**
	 * Assign the names and types for the members to be a part of serialization or deserialization.
	 */
	protected void applyTypesByMemberName()
	{
		typesByMemberName.put( "valuesAsCollection", XMLType.SOAP_VECTOR);
	}
	
	/**
	 * Can be overriden to provide a typesafe instance
	 * @return	FrameworkPrimaryKey 
	 */
	protected FrameworkPrimaryKey createPKInstance()
	{
		return new FrameworkPrimaryKey();
	}
	   
// attributes

	/**
	 * key/value container for the names and types of each member to be a part of this process
	 */
   protected Map  typesByMemberName 	= new HashMap();    
}

/*
 * Change Log:
 * $Log:  $
 */

