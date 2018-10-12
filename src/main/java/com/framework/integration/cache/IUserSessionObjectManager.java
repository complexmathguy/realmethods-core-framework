/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.cache;

import com.framework.business.pk.FrameworkPrimaryKey;

import com.framework.business.vo.IFrameworkValueObject;

/**
 * This interface defines the standard for caching IFrameworkValueObjects within the Framework.
 * <p>
 * It provides additional cache semantics atop of IFrameworkCache and IGlobalValueObjectCache.
 * <p>
 * @author  realMethods, Inc.
 * @see		com.framework.integration.cache.FrameworkCacheFactory
 * @see		com.framework.integration.cache.FrameworkCache
 */
public interface IUserSessionObjectManager 
    extends IFrameworkCache, IGlobalValueObjectCache
{
    /**
     * Use this method to apply a IFrameworkValueObject to the cache using the provided key.
     * <p>
     * @param		primaryKey		key to associate valueObject with in the cache 
     * @param       valueObject		data to bind in cache with the primarKey
     * @exception	IllegalArgumentException
     */
     public void assignValueObject( FrameworkPrimaryKey primaryKey, IFrameworkValueObject valueObject )
     throws IllegalArgumentException;
     
    /**
     * Use this method to apply a IFrameworkValueObject to the cache, using the value object's
     * own FrameworkPrimaryKey as the key into the cache.
     * <p>
     * @param		valueObject
     * @exception	IllegalArgumentException Thrown if any of the parameters are null.
     */
     public void assignValueObject( IFrameworkValueObject valueObject )
     throws IllegalArgumentException;
     
    /**
     * Removes the associated IFrameworkValueObject
     * @param   	key
     */
    public void removeValueObject( FrameworkPrimaryKey key );
    
     /**
      * Returns the associated IFrameworkValueObject based on the notion of equals on the provided
      * FrameworkPrimaryKey.
      * <p>
      * Returns the value to which this map maps the specified key. Returns null if the map 
      * contains no mapping for this key. A return value of null does not necessarily 
      * indicate that the map contains no mapping for the key; 
      * it's also possible that the map explicitly maps the key to null. .
      * <p>
      * @param      primaryKey
      * @return     IFrameworkValueObject
      */
     public IFrameworkValueObject getValueObject( FrameworkPrimaryKey primaryKey );
     
     /**
      * Returns the associated IFrameworkValueObject based on the notion of equals on the provided
      * String.
      * <p>
      * @param      key
      * @return     IFrameworkValueObject
      */
     public IFrameworkValueObject getValueObject( String key );          
    
	/**
	 * Returns true if the map contains a mapping for the specified key.
	 * 
	 * @param 	key
	 * @return 	boolean
	 */
	public boolean containsKey( String key );
	
	/**
	 * Returns true if the cache contains a mapping for the specified key.
	 * <p>
	 * @param 		key
	 * @return		boolean
	 */
	public boolean containsKey( FrameworkPrimaryKey key );
	       
    
    /**
     * Empty the cache of ValueObjectListProxies.  
     */     
    public void emptyCacheOfListProxies();

}

/*
 * Change Log:
 * $Log: IUserSessionObjectManager.java,v $
 */
