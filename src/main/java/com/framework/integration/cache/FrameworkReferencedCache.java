/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.cache;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Objects assigned to the cache are wrapped in a SoftReference, to ensure
 * that those objects not regularly used are purged during garbage collection.
 * Always check for a null return value when retrieving data.
 * <p>
 * @author    realMethods, Inc.
 * @see		  com.framework.integration.cache.FrameworkCacheFactory
 * @see		  java.lang.ref.SoftReference
 */
public class FrameworkReferencedCache extends FrameworkCache
{

//************************************************************************    
// Public Methods
//************************************************************************

    /**
     * default constructor
     */
    public FrameworkReferencedCache()
    {             
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
     		throw new IllegalArgumentException( "FrameworkReferencedCache:assign() - key cannot be null." );
     		
     	if ( data == null )
     		throw new IllegalArgumentException( "FrameworkReferencedCache:assign() - data cannot be null." );

		super.assign( key, new SoftReference( data ) );  
		   		
     }
    
    /**
     * Removes the object from the cache, wrapped by a SoftReference.
     * @param data
     */
    public void removeData( Object data )
    {
    	// need to go and find it because it will be wrapped with a SoftReference
    	Reference ref = getReference( data );
    	
    	if ( useableReference( ref ) == true )
    	{
    		super.removeData( ref );
    	}
    }
    
	/**
	 * Returns the associated Object based on the notion of equals on the provided
	 * key.
	 * @param      key
	 * @return     associated object, or null if not foud
	 */
	public Object get( Object key )
    {
     	Object data					= null;
     	
     	if ( key != null )
     	{
     		// retrieve the associated SoftReference
     		data = getReferencedData( (SoftReference)(super.get( key )) );
     	}
     	
		return( data );	     	
    }

    
    
//************************************************************************    
// Private / Protected Methods

	/**
	 * Provides a Collection of those objects still referenced by a SoftReference
	 * in the cache.
	 * @return	Collection
	 */
	protected Collection getCachedData()
	{
		ArrayList dataList 	= new ArrayList();
		Map cache			= getCache();
		
		synchronized( cache )
		{
			SoftReference ref	= null;
			Iterator iter 		= cache.values().iterator();
			
			while( iter.hasNext() )
			{
				ref = (SoftReference)(iter.next());
				
				if  ( useableReference( ref ) )
				{
					dataList.add( ref.get() );
				}
			}
		}
		
		return( dataList );
	}
	
	/** 
	 * Gets the underlying object from the Reference.
	 * <p>
	 * @param 	reference
	 * @return	Object referred to
	 */
	protected Object getReferencedData( Reference reference )
	{
		Object data = null;
		
		// if there is one, and the reference isn't tagged for gc 
		if ( useableReference( reference ) )
		{
			data = reference.get();	     			
		}			
		
		return( data );	
	}
	
	/**
	 * Locates the Reference that is wrapping the provided data.
	 * @param 	data
	 * @return	its reference
	 */
	private Reference getReference( Object data )
	{
		Reference ref 	= null;
		Map cache		= getCache();
						
		synchronized( cache )
		{
			Iterator iter 		= cache.values().iterator();
			Object tmpData		= null;
			Reference tmpRef	= null;
			
			while ( iter.hasNext() && ref == null )
			{
				tmpRef = (Reference)iter.next();
				
				if ( useableReference( tmpRef ) )
				{
					tmpData = tmpRef.get();
									
					if ( tmpData != null && tmpData == data )
					{				
						ref = tmpRef;	
					}
				}
			}					
		}
		
		return( ref );
	}

	/**
	 * Checks that the ref is non-null and isn't enqueued.
	 * <p>
	 * @param 		ref
	 * @return		true/false indicator
	 */
	private boolean useableReference( Reference ref )
	{
		return ( ref != null && ref.isEnqueued() == false ); 		
	}
	
//************************************************************************    
// Attributes
//************************************************************************

}

/*
 * Change Log:
 * $Log: FrameworkReferencedCache.java,v $
 */
