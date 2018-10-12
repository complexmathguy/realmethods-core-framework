/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.cache;

import com.framework.common.exception.ObjectCreationException;

import com.framework.integration.cache.FrameworkCacheFactory;
/**
 * Factory used to create one or more IUserSessionObjectManager
 * implementations as specified in the framework.xml for property value
 * UserSessionObjectManagerClassName
 * <p> 
 * @author    realMethods, Inc.
 */
public class USOMFactory extends FrameworkCacheFactory
{
	/**
	 * default constructor
	 */
	public USOMFactory()
	{}
	
	/**
	 * factory methods
	 * @return		singleton framework cache factory
	 */
	static public FrameworkCacheFactory getInstance()
	{
		 if ( instance == null )
		 {
			instance = new USOMFactory();
		 }
		 
		 return ( instance );
	}
		
	/**
	 * Returns a dynamically created IUserSessionObjectManager implementation
	 * The specified class name is read from the framework.xml file.
	 * <p>
	 * @return 	IUserSessionObjectManager
	 * @exception	ObjectCreationException
	 */
	public IUserSessionObjectManager createUSOM()
    throws ObjectCreationException
	{
		IUserSessionObjectManager usom = null;
		
		try
		{		
			usom = (IUserSessionObjectManager)createFrameworkCache();
		}
		catch( Throwable exc )
		{
			throw new ObjectCreationException( "USOMFactory.createUSOM()- " + exc, exc );
		}
		
		return ( usom );
	} 
			
// attributes

   /**
	* factory singleton instance
	*/	
   private static USOMFactory instance = null;
   
   /**
	* FrameworkCache class name value from the framework.xml file
	*/
   protected String className = null;   
   
}

/*
 * Change Log:
 * $Log: USOMFactory.java,v $
 */
