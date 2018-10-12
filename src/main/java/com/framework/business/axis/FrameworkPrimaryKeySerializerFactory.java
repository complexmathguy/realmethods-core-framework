/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.business.axis;

        
 

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collection;

import org.apache.axis.Constants;

/**
 * SerializerFactory and DeserializerFactory for FrameworkPrimaryKeySerializer
 * <p> 
 * @author    realMethods, Inc.
 */
             
public class FrameworkPrimaryKeySerializerFactory 
	implements org.apache.axis.encoding.DeserializerFactory,
			 org.apache.axis.encoding.SerializerFactory 
{
// constructors

	/**
	 * necessary for Axis instantiation
	 */
	public FrameworkPrimaryKeySerializerFactory() 
	{
	}	
	
	/**
	 * The only type supported is Constants.AXIS_SAX.
	 * <p>
	 * @return	iterator of the Collection the types are applied to
	 */
	public Iterator getSupportedMechanismTypes() 
	{
		if (mechanisms == null) 
		{
			mechanisms = new ArrayList();
			mechanisms.add(Constants.AXIS_SAX);
		}
		return mechanisms.iterator();
	}
	
	
//	DeserializerFactory implementations
	
	/**
	 * Returns an instance of a newly created FrameworkPrimaryKeySerializer, ignoring
	 * the mechanism type since only one is supported at this time.
	 * <p>
	 * @return	avax.xml.rpc.encoding.Deserializer
	 */
	 public javax.xml.rpc.encoding.Deserializer getDeserializerAs(String mechanismType) 
	 {
		 return new com.framework.business.axis.FrameworkPrimaryKeySerializer();
	 }	
	
//	 SserializerFactory implementations			
	/**
	 * Returns an instance of a newly created FrameworkPrimaryKeySerializer, ignoring
	 * the mechanism type since only one is supported at this time.
	 * <p>
	 * @return	avax.xml.rpc.encoding.Serializer
	 */				
	 public javax.xml.rpc.encoding.Serializer getSerializerAs( String mechanismType ) 
	 {
		 return new com.framework.business.axis.FrameworkPrimaryKeySerializer();
	 }	
	
// attributes

	/**
	 * store for supported mechanism types 
	 */
	private Collection mechanisms = null;
	
}
/*
 * Change Log:
 * $Log:  $
 */
 
