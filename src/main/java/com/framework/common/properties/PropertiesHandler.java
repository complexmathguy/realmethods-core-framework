/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.common.properties;

import com.framework.common.FrameworkBaseObject;

import com.framework.integration.cache.FrameworkCache;

/** 
 * Abstract base class of all properties handlers.
 * <p>
 * Provides an internal cache for property storage.
 * <p>
 * @author  	realMethods, Inc.  
 * @see			com.framework.integration.cache.FrameworkCache
 */
abstract public class PropertiesHandler 
    extends FrameworkBaseObject implements IPropertiesHandler
{

// attributes   

	public PropertiesHandler()
	{
		if ( cache == null )
		{
			cache = new FrameworkCache();
		}		
	}
		
// cache helper methods	
	
	/**
	 * Assigns the properties to the cache, referred to by the key.
	 * <p>
	 * @return	key
	 * @return	props
	 */
	protected void cache( Object key, Object props )
	{
		cache.assign( key, props );
	}
    
    /**
     * Returns the referred to object, or null if not in cache.
     * <p>
     * @param 	key
     * @return	referenced object
     */
	protected Object cache( Object key )
	{
		return( cache.get( key ) );
	}
	
// attributes
    
    /**
     * internal properties cache
     */
    private FrameworkCache cache = null;
    
}

/*
 * Change Log:
 * $Log: PropertiesHandler.java,v $
 */
