/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.presentation.translator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.framework.business.vo.IFrameworkValueObject;

import com.framework.business.vo.list.IListProxy;

/**
 * Base class of application specific list related translators.  The purpose of this
 * class is to provide foundational functional for type specifc sub-classes.  In general
 * a sub-class, which will know how to manipulate and format date for a specific type
 * of IFrameworkValueObject, should be used instead.
 * <p>
 * The core functionality provided is:
 * <p>
 * 1. Provide a Collection of Strings that represent the attribute names
 * of the IFrameworkValueObjects in a Collection.  
 * <p>
 * 2. Provide a Collection of HashMaps representing the attribute names (keys to HashMap),
 * and the attribute String values (data in HashMap).
 * <p>
 * @author    realMethods, Inc.
 */
abstract public class FrameworkListTranslator extends BaseTranslator
{
// constructor

    protected FrameworkListTranslator()
    { 
        super();
    }
    
// access methods


    /**
     * Returns a Collection of a Object Collections related to the ListProxy of 
     * IFrameworkValueObjects held in the cache.
     * <p>
     * @param       valueObjects
     * @return      Collection
     */
    public Collection getListAttributes( Collection valueObjects )
    {
        ArrayList dataValues                            = new ArrayList();        
        IFrameworkValueObjectTranslator voTranslator    = getFrameworkValueObjectTranslatorHelper();

        if ( valueObjects != null )
        {
            Iterator iter                       = valueObjects.iterator();
            IFrameworkValueObject valueObject   = null;
            
            while( iter.hasNext() )
            {
                valueObject = (IFrameworkValueObject)iter.next();
                dataValues.add( voTranslator.getListAttributes( valueObject ) );
            }
        }
    
        return( dataValues );            
    }
    
    /**
     * Returns a Collection of a Object Collections related to the ListProxy of 
     * IFrameworkValueObjects held in the cache.
     * <p>
     * @param       keyToCachedListProxy
     * @return      Collection
     */
    public Collection getListAttributes( String keyToCachedListProxy )
    {
        Collection dataValues                           = null;
        Collection valueObjects                         = null;
        
        IListProxy listProxy = getList( keyToCachedListProxy );
            
        if( listProxy != null )
        {
            valueObjects 	=  listProxy.getDataValues();                
            dataValues 		= getListAttributes( valueObjects );
        }
        
        return( dataValues );
    }
    
    
    /**
     * Abstract method used to force the sub-class to instantiate the correct
     * IFrameworkValueObjectTranslator implementation.
     */
    abstract public IFrameworkValueObjectTranslator getFrameworkValueObjectTranslator();
    
// helper methods

    /**
     * Internal helper method use to instantiate a single IFrameworkValueObjectTranslator*
     * <p>
     * @return      IFrameworkValueObjectTranslator
     */
    private IFrameworkValueObjectTranslator getFrameworkValueObjectTranslatorHelper()
    {
        if ( valueObjectTranslator == null )
        {
            valueObjectTranslator = getFrameworkValueObjectTranslator();
            
            // initialize it...just in case it needs to be
            valueObjectTranslator.init( getServletRequest(), getBaseUserSessionObjectManager() );
        }
        
        return( valueObjectTranslator );
    }
    
// attributes

    private IFrameworkValueObjectTranslator valueObjectTranslator = null;
}

/*
 * Change Log:
 * $Log: FrameworkListTranslator.java,v $
 */
