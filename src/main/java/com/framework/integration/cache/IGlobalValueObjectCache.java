/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.cache;


import com.framework.business.pk.FrameworkPrimaryKey;

import com.framework.business.vo.IFrameworkValueObject;

/**
 * This interface provides a convenient set of methods for global application 
 * scope caching of value objects.
 * <p>
 * Its implement should also insure that only one copy of the value objects
 * are cached for each application.
 * <p> 
 * @author    realMethods, Inc.
 */
public interface IGlobalValueObjectCache 
    extends java.io.Serializable
{

    /**
     * Use this method to obtain a value object from the global cache
     * <p>
     * @param       primaryKey
     * @return      IFrameworkValueObject
     */
    public IFrameworkValueObject getGlobalValueObject( FrameworkPrimaryKey primaryKey );
    
    /**
     * Use this method to globally cache a value object available.
     * <p>
     * @param		primaryKey		key to apply to global cache
     * @param       valueObject		value to associate with the key
     * @exception   IllegalArgumentException Thrown if any of the parameters are null.
     */
     public void assignGlobalValueObject( FrameworkPrimaryKey primaryKey, IFrameworkValueObject valueObject )
     throws IllegalArgumentException;
     
    /**
     * Use this method to globally cache a value object available.
     * The Key for the model is the DefaultKey of the base FrameworkValueObject 
     * implemntation.
     * <p>
     * @param       valueObject
     * @exception	IllegalArgumentException Thrown if value valueObject is null.
     */
     public void assignGlobalValueObject( IFrameworkValueObject valueObject )
     throws IllegalArgumentException;

    /**
     * Removes the associated value object from the global cache.
     * <p>
     * @param		key
     */
    public void removeGlobalValueObject( FrameworkPrimaryKey key );

    /**
     * Empty the global cache.  Use with caution since it has application level
     * ramifications.
     */     
    public void emptyGlobalCache();
        
}

/*
 * Change Log:
 * $Log: IGlobalValueObjectCache.java,v $
 */
