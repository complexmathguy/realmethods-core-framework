/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.cache;

import java.util.Collection;

/**
 * This interface defines the standard for caching within the Framework.
 * <p>
 * @author    realMethods, Inc.
 * @see		  com.framework.integration.cache.FrameworkCacheFactory
 */
public interface IFrameworkCache extends java.io.Serializable
{
    /**
     * Applies an object to the cache using the provided key.
     * <p>
     * @param		key		key to associate the object with in the cache 
     * @param       data	data to bind in cache with the primarKey
     * @exception	IllegalArgumentException
     */
     public void assign( Object key, Object data )
     throws IllegalArgumentException;
     
    /**
     * Removes the associated object from the cache
     * @param   	key
     */
    public void remove( Object key );
	
	
	/**
	 * Removes the object from the cache, wrapped by a SoftReference.
	 * @param data
	 */
	public void removeData( Object data );
	
	   
     /**
      * Returns the associated Object based on the notion of equals on the provided
      * key.
      * @param      key
      * @return     associated object, or null if not foud
      */
     public Object get( Object key );
     
          
    /**
     * Returns true if the cache contains a mapping for the specified key.
     * <p>
     * @param 		key
     * @return		boolean
     */
    public boolean contains( Object key );
    
        
    /**
     * Empty the cache
     */     
    public void emptyCache();
    
    /**
     * Returns a Collection of the keys in the cache
     * @return Collection
     */
    public Collection getAllKeysCached();

}

/*
 * Change Log:
 * $Log: IUserSessionObjectManager.java,v $
 */
