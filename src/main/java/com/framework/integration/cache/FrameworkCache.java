/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.cache;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.framework.common.FrameworkBaseObject;

/**
 * This class provides a wrapper implementation of a synchronized HashMap.
 * It defines the standard for caching Objects within the Framework. 
 * <p>
 * @author    realMethods, Inc.
 * @see		  com.framework.integration.cache.FrameworkCacheFactory
 * @see		  java.lang.ref.SoftReference
 */
public class FrameworkCache extends FrameworkBaseObject
    implements IFrameworkCache
{

//************************************************************************    
// Public Methods
//************************************************************************

    /**
     * default constructor
     */
    public FrameworkCache()
    {
        // Create PSOM Cache
        cache = Collections.synchronizedMap( new HashMap() );              
    }

	/**
	 * Applies an object to the cache using the provided key.
	 * <p>
	 * @param		key		key to associate the object with in the cache 
	 * @param       data	data to bind in cache with the primarKey
	 * @exception	IllegalArgumentException	thrown if key or data is null
	 */
	 public void assign( Object key, Object data )
	 throws IllegalArgumentException
     {
     	if ( key == null )
     		throw new IllegalArgumentException( "FrameworkCache:assign() - key cannot be null." );
     		
     	if ( data == null )
     		throw new IllegalArgumentException( "FrameworkCache:assign() - data cannot be null." );

		cache.put( key, data );  
		   		
     }
     
     
	/**
	 * Removes the object referenced by key from the cache
	 * @param   	key
	 */
	public void remove( Object key )
    {
    	cache.remove( key );
    }
    
    /**
     * Removes the object from the cache, wrapped by a SoftReference.
     * @param data
     */
    public void removeData( Object data )
    {
    	cache.values().remove( data );
    }
    
	/**
	 * Returns the associated Object based on the notion of equals on the provided
	 * key.
	 * @param      key
	 * @return     associated object, or null if not foud
	 */
	public Object get( Object key )
    {
     	Object data	= cache.get( key );
     	
		return( data );	     	
    }
     
    
	/**
	 * Returns true if the cache contains a mapping for the specified key.
	 * <p>
	 * @param 		key
	 * @return		boolean
	 */
	public boolean contains( Object key )
	{
		return ( cache.containsKey( key ) );
	}
    
        
	/**
	 * Empty the cache
	 */     
	public void emptyCache()
	{
		cache.clear();
	}
    
	/**
	 * Returns a Collection of the keys in the cache
	 * @return Collection
	 */
	public Collection getAllKeysCached()
	{
		return( cache.keySet() );
	}
    
    
//************************************************************************    
// Private / Protected Methods

	/**
	 * Returns the cache.
	 * @return		the cache
	 */
	protected Map getCache()
	{
		return cache;
	}

	/**
	 * Provides a Collection of those objects still referenced by a SoftReference
	 * in the cache.
	 * @return	Collection of cache values
	 */
	protected Collection getCachedData()
	{	
		return( cache.values() );
	}
	
//************************************************************************    
// Attributes
//************************************************************************

   /**
    * the cache of objects
    */
    private Map cache = null;     
}

/*
 * Change Log:
 * $Log: FrameworkCache.java,v $
 */
