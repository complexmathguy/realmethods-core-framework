/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.cache;

import java.util.Properties;

import com.framework.common.FrameworkBaseObject;
import com.framework.common.FrameworkBaseObjectFactory;

import com.framework.common.exception.ObjectCreationException;
import com.framework.common.misc.Utility;
import com.framework.common.namespace.FrameworkNameSpace;

/**
 * Factory used to create one or more IFrameworkCache
 * implementations as specified in the framework.xml for property value
 * FrameworkCacheName.  Property UserSessionObjectManagerClassName is still acknowledged,
 * but should not be used, instead using FrameworkCacheName. 
 * <p> 
 * @author    realMethods, Inc.
 */
public class FrameworkCacheFactory extends FrameworkBaseObjectFactory
{
	/**
	 * default constructor
	 */
	protected FrameworkCacheFactory()
	{
		super( false /* do not treat created class instances as singletons */ );
	}
	
	/**
	 * factory methods
	 * @return		singlteon framework cache factory
	 */
	static public FrameworkCacheFactory getInstance()
	{
		 if ( instance == null )
		 {
		 	instance = new FrameworkCacheFactory();
		 }
		 
		 return ( instance );
	}
	
	/**
	 * Returns a dynamically created IFrameworkCache implementation
	 * The specified class name is read from the framework.xml file.
	 * <p>
	 * @return 	IFrameworkCache
	 * @exception	ObjectCreationException
	 */
	public IFrameworkCache createFrameworkCache()
	throws ObjectCreationException
	{
		if ( className == null )
		{
			// Read this from class name from the framework.xml
			Properties props = null;
			try
			{
				props = Utility.getFrameworkProperties();
	            
				if ( props == null )
				{
					throw new ObjectCreationException( "FrameworkCacheFactory::createFrameworkCache() - framework.roperties have not been loaded.");
				}
			}
			catch ( Exception e )
			{
				throw new ObjectCreationException("FrameworkCacheFactory::createFrameworkCache() - can't get properties. - " + e, e);
			}
	

			className = props.getProperty( FrameworkNameSpace.APPLICATION_CACHE_NAME, "com.framework.integration.cache.FrameworkReferencedCache" );			        
	
			new FrameworkBaseObject().logMessage("FrameworkCacheFactory::createFrameworkCache() - FrameworkCache Class Name is : " + className );
	        
			// If the property was not found then throw an ObjectCreationException
			if ( className == null )
			{
				throw new ObjectCreationException("FrameworkCacheFactory::createFrameworkCache() - could not find UserSessionObjectManagerClassName in framewrk.properties.");   
			}
		}
		
		// delegate to the base class for the actual class instantiation
		return( createFrameworkCache( className ) );
	} 
	
	/**
	 * Returns a dynamically created IFrameworkCache implementation using the provided
	 * fully qualified class name.
	 * <p>
	 * @param		className
	 * @return		create IFrameworkCache implementation
	 * @exception	ObjectCreationException
	 */
	public IFrameworkCache createFrameworkCache( String className )
	throws ObjectCreationException
	{
		IFrameworkCache cache = null;
		
		try
		{		
			cache = (IFrameworkCache)getObject( className );
		        
			if ( cache == null )
				throw new ObjectCreationException("FrameworkCacheFactory::createFrameworkCache() - could not create the class associated with UserSessionObjectManagerClassName. Make sure " +  className + " is in the CLASSPATH.");   
		}
		catch( Throwable exc )
		{
			throw new ObjectCreationException("FrameworkCacheFactory::createFrameworkCache() - could not create the class associated with UserSessionObjectManagerClassName. Make sure " +  className + " is in the CLASSPATH."); 
		}
		
		return( cache );
		
	}

	/**
	 * Delegates to overloaded version createFrameworkCache( String ) by providing the fully
	 * qualified class name com.framework.intergration.cache.FrameworkReferencedCache.
	 * <p>
	 * @return	created IFrameworkCache implementation
	 * @throws 	ObjectCreationException
	 */
	public IFrameworkCache createFrameworkReferencedCache()
	throws ObjectCreationException
	{
		return createFrameworkCache( "com.framework.integration.cache.FrameworkReferencedCache" );
	} 
	
	/**
	 * Delegates to overloaded version createFrameworkCache( String ) by providing the fully
	 * qualified class name com.framework.intergration.cache.FrameworkCache.
	 * <p>
	 * @return	created IFrameworkCache implementation
	 * @throws 	ObjectCreationException
	 */
	public IFrameworkCache createDefaultFrameworkCache()
    throws ObjectCreationException
	{
        return createFrameworkCache( "com.framework.integration.cache.FrameworkCache" );
	} 
		
// attributes

	/**
	 * factory singleton instance
	 */	
	private static FrameworkCacheFactory instance = null;
	
	/**
	 * framework.xml FrameworkCache class name
	 */
	private String className = null;
}

/*
 * Change Log:
 * $Log: FrameworkCacheFactory.java,v $
 */
