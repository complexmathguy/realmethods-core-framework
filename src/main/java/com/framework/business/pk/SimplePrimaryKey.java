 /************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.business.pk;

//************************************
// Imports
//************************************
import java.util.*;

/** 
 * Standard key class for those requiring a single Integer as a pk. 
 * <p> 
 * @author    realMethods, Inc.
 */
public class SimplePrimaryKey
    extends FrameworkPrimaryKey
    implements ISimplePrimaryKey, java.io.Serializable
{
//************************************************************************    
// Public Methods
//************************************************************************

    /**
     * default constructor 
     */
    protected SimplePrimaryKey()
    {
    }

	/**
	 * Constructor
	 * 
	 * @param		id		Object
	 * @exception	IllegalArgumentException
	 */
	public SimplePrimaryKey( Object id ) 
    throws IllegalArgumentException
	{
		super();
		
		if ( id == null )
			throw new IllegalArgumentException( "SimplePrimaryKey(Object) arg cannot be null." );

		this.id = id;
	}


	/**
	 * Accepts a Collection of values which define this key.
	 * <p>
	 * @param       values						the key data
	 * @exception   IllegalArgumentException	thrown if values is null, or doesn't equal one
	 */
	public SimplePrimaryKey( Collection values )
	throws IllegalArgumentException
	{
		if ( values == null || values.size() != 1 )
		{
			throw new IllegalArgumentException( "SimplePrimaryKey() - must provide a non-null, non-empty Collection of values." );
		}
        
		assignValues( values );
	}
    
    /**
     * Returns the internal id
     * @return	id
     */
	public Object getID()
	{
		return (this.id);
	}


	/**
	 * Retrieves the values as a Collection
	 * @return Collection
	 */
	public Collection valuesAsCollection()
	{
		ArrayList list = new ArrayList();
		list.add( id );
		return( list );
	}
	   
	/**
	 * Returns a String representation of the key
	 * <p>
	 * @return      String
	 */
	public String toString()
	{
		return( "key: " + (id != null ? id.toString() : "null") );
	}
    
    

//************************************************************************
// Protected / Private Methods
//************************************************************************

   /**
    * Internal assignment of key values.
    *
    * @param	values		key data
    */
    protected void assignValues( Collection values )
    {
		if ( values != null && values.size() > 0 )
		{
			id = values.iterator().next();    
		}    	
		else
			id = null;
    }

	/**
	 * Assigns the id.  For internal usage only
	 * 
	 * @param	id		Object
	 */
	protected void setID( Object id )
	{
		this.id = id;
	}
	
//************************************************************************
// Attributes
//************************************************************************

	public Object id;

}

/*
 * Change Log:
 * $Log: SimplePrimaryKey.java,v $
 */
