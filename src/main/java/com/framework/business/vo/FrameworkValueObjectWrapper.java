/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.business.vo;

//***************************
//Imports
//***************************

import com.framework.business.pk.FrameworkPrimaryKey;

/**
 * Wrapper used to disguise any object as a FrameworkValueObject.
 * <p>
 * Most useful when caching an object to an IUserSessionObjectManager implementation.
 * <p>
 * @author		realMethods, Inc.
 * @see			com.framework.integration.cache.IUserSessionObjectManager
 * @see			com.framework.integration.cache.UserSessionObjectManager
 */
public class FrameworkValueObjectWrapper extends FrameworkValueObject
{

//************************************************************************   
// Constructors
//************************************************************************ 

   /**
    * @param	obj		target to wrap	
    * @param	key		assigned unique identifier for this instance
    */
	public FrameworkValueObjectWrapper( Object obj, FrameworkPrimaryKey key )
  	{
  		super( key );
  		object = obj;
  	}
  	
   /**
	* Delegates to constructor FrameworkValueObject( Object obj, FrameworkPrimaryKey key )
	* <p>
	* @param	object	target to wrap
	* @param   	key		assigned unique identifier for this instance
	*/
	public FrameworkValueObjectWrapper( Object object, Object key )
	{
		this( object, new FrameworkPrimaryKey( key ) );  	
	}
	
//************************************************************************   
//	Public Methods
//************************************************************************ 

//////////////////////////////////
// access methods
//////////////////////////////////
	
	/**
	 * Returns the wrapped object.
	 * @return		Object
	 */
	public Object getObject()
	{
		return object;
	}
//////////////////////////////////
// java.lang.Object overloads
//////////////////////////////////

   /**
	* Overloaded equals method to determine object equivalence.
   	* <p>
   	* Will first do a comparison between the argument and the wrapped argument
   	* @param       object		what to compare to
   	* @return      boolean
   	*/
  	public boolean equals( Object object )
  	{
		boolean bEquals     					= false;
        
		if ( object != null )
		{
			if ( object instanceof FrameworkValueObjectWrapper )  
			{
				if ( getObject().equals( ((FrameworkValueObjectWrapper)object).getObject() ) )
					bEquals = super.equals( object );
			}
		}		
		
		return( bEquals );
  	}
  
//************************************************************************  
// Protected / Private Methods
//************************************************************************

//************************************************************************    
// Attributes
//************************************************************************
	
	/**
	 * wrapped target
	 */	
	private Object object = null;

}

/*
 * Change Log:
 * $Log$
 */
