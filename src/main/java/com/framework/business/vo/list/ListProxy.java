/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.business.vo.list;

// **********************
// Imports
// **********************

import java.util.*;

import com.framework.common.FrameworkBaseObject;
import com.framework.common.IFrameworkBaseObject;

/**
 * Useful as a Collection wrapper, but is itself a FrameworkBaseObject. 
 * <p> 
 * The format of the provided Collection are not generic, but must take the following form
 * of an ArrayList, where each entry of the list represents a HashMap.  The Hashmap itself contains
 * key/value pairs where the key is a field name and the value is the data.
 * <p>
 * @author    realMethods, Inc.
 */
public class ListProxy extends FrameworkBaseObject
	implements IListProxy
{
//************************************************************************
// Public Methods
//************************************************************************
	/** 
	  * @param 		data		source to wrap around
	  * @exception 	IllegalArgumentException Thrown if data is null.
	  */
	public ListProxy( Collection data ) 
	{        
		if (data == null)
		{
			throw new IllegalArgumentException("Inside ListProxy::ListProxy(..) - data Collection arg cannot be null.");
		}
	
		applyDataValues( data );
	}
	    
	/** 
	 * @param		proxy		source to wrap aroud
	 * @exception 	IllegalArgumentException Thrown when the proxy is null.
	 */
	public ListProxy( IListProxy proxy ) 
	throws IllegalArgumentException
	{
		copy( proxy );
	}   

    /**
     * Returns the previously assigned data.
     * @return Collection
     */
    public Collection getDataValues()
    {
        return dataValues;
    }
    
    /** 
     * Returns the String representation for the data value corresponding to
     * the current record.
     * @param 	key		the data of interest		
     * @return 	String	value discovered
     */
    public String getValueAsString( String key )
    {
        String returnValue = null;
        
        returnValue = (String)currentRecord.get( key );
        
        return returnValue;
    }

    /** 
     * Returns the value as a Object.
     * <p>
     * @param	key
     * @return 	Object 
     */
    public Object getObject( String key )
    {
        Object returnValue = null;
        
        returnValue = currentRecord.get( key );

        return returnValue;
    }
    
    /**
     * Moves the pointer to the next record if there is one.
     * <p>
     * @return      Object
     * @exception   NoSuchElementException
     */
    public synchronized Object next()
        throws NoSuchElementException
    {
        if ( hasNext() )
        {
            // Retrieve value
            currentRecord = (HashMap)dataValues.get(recordCounter);
            
            // Increment the count
            recordCounter++;
            
            return( currentRecord );
        }
        else
        {
            throw new NoSuchElementException( "ListProxy:next() - there is no next entry in the list." );
        }
    }

    /**
     * Moves the pointer to the previous record if there is one.
     * <p>
     * @return      Object
     * @exception   NoSuchElementException
     */
    public Object previous()
    throws NoSuchElementException
    {
        if ( hasPrevious() )
        {
            // Decrement the count
            recordCounter--;
            
            // Retrieve value
            currentRecord = (HashMap)dataValues.get(recordCounter);
            
            return( currentRecord );
        }
        else
        {
            throw new NoSuchElementException( "ListProxy:previous() - there is no previous entry in the list." );
        }
    }

    /**
     * Moves the pointer to the next n records, or as many as possible.
     * <p>
     * @param       numberToGet
     * @return      Collection
     */
    public Collection next( int numberToGet )
    throws NoSuchElementException
    {
        boolean done	= false;
        ArrayList data	= new ArrayList( numberToGet );
        
        for ( int i = 0; (i < numberToGet) && !done ; i++ )
        {
            try
            {
                data.add( next() );
            }
            catch( NoSuchElementException exc )
            {
                done = true;
            }
        }
        
        return( data );
    }
    
    /**
     * Moves the pointer to the previous n records, or as many as possible.
     * <p>
     * @param       numberToGet
     * @return      Collection
     */
    public Collection previous( int numberToGet )
    throws NoSuchElementException
    {
        boolean done = false;
        ArrayList data = new ArrayList( numberToGet );
        
        for ( int i = 0; (i < numberToGet) && !done ; i++ )
        {
            try
            {
                data.add( previous() );
            }
            catch( NoSuchElementException exc )
            {
                done = true;
            }
        }
        
        return( data );
        
    }
    
    /**
     * Returns whether there are any more records in the iterator.
     * @return boolean
     */
    public boolean hasNext()
    {
        boolean hasNext = false;
        
        if (dataValues != null)
        {
            if ((dataValues.size()-1) >= recordCounter)
            {
                hasNext = true;   
            }
        }
        
        return hasNext;
    }
    
    /**
     * Returns whether there is a previous record in the iterator.
     * @return boolean
     */
    public boolean hasPrevious()
    {
        boolean hasPrevious = false;
        
        if (dataValues != null)
        {
            if ( recordCounter > 0 )
            {
                hasPrevious = true;   
            }
        }
        
        return hasPrevious;
    }

	/**
	 * Returns whether there are any more then n records left in the iterator.
	 * <p>
	 * @param	numberToConsider	# to apply
	 * @return 	boolean
	 */
    public boolean hasNext( int numberToConsider )
    {
        boolean hasNext = false;
        
        if (dataValues != null)
        {
            if ((dataValues.size()) >= recordCounter + numberToConsider)
            {
                hasNext = true;   
            }
        }
        
        return hasNext;        
    }

	/**
	* Returns whether there are any more than n records before the beginning
	* of the iterator.
	* <p>
	* @param	numberToConsider	# to apply
	* @return 	boolean
	*/
    public boolean hasPrevious( int numberToConsider )
    {
        boolean hasPrevious = false;
        
        if (dataValues != null)
        {
            if ( recordCounter-numberToConsider-1 > 0 )
            {
                hasPrevious = true;   
            }
        }
        
        return hasPrevious;        
    }
    
    
	/**
	 * Returns the first item that matches the provided criteria.
	 * <p>
	 * @param       fieldName		name to match
	 * @param       fieldValue		having said value to match
	 * @return      Object
	 */
    public Object getItem( String fieldName, String fieldValue )
    {
        HashMap matchedHashMap = null;
        
        if ( dataValues != null )
        {
            Iterator iter           = dataValues.iterator();
            String compareValue    	= null;
            HashMap currentHashMap  = null;
            
            while( iter.hasNext() && matchedHashMap == null )
            {
                currentHashMap = (HashMap)iter.next();
                
                compareValue = (String)currentHashMap.get(fieldName);
                
                if ( compareValue.equalsIgnoreCase( fieldValue ) )
                {
                    matchedHashMap = currentHashMap;
                }
            }
        }

        return( matchedHashMap );
    }
    

	/**
	 * Returns all items that matches the provided criteria.
	 * <p>
	 * @param       fieldName		name to match
	 * @param       fieldValue		having said value to match
	 * @return      Collection
	 */
    public Collection getItems(String fieldName, String fieldValue)
    {
        Collection collection = new ArrayList();
        
        if ( dataValues != null )
        {
            Iterator iter           = dataValues.iterator();
            String compareValue    	= null;
            HashMap currentHashMap  = null;
            
            while( iter.hasNext() )
            {
                currentHashMap = (HashMap)iter.next();
                
                compareValue = (String)currentHashMap.get(fieldName);
                
                if ( compareValue.equalsIgnoreCase( fieldValue ) )
                {
                    collection.add( currentHashMap );
                }
            }
        }

        return( collection );        
    }
    
    /**
     * Apply a Collection of data values.
     * @param       values	Collection of HashMap's that represent records of data.
     */    
    public void applyDataValues( Collection values )
    {
        // Set member attributes
        dataValues = new ArrayList( values );        
    }

    /**
     * Performs a deep copy.
     * <p>
     * @param		listproxy
     * @exception 	IllegalArgumentException
     */
    public void copy( IListProxy listproxy ) 
    throws IllegalArgumentException
    {
        // Validate that the proxyIn is the correct type
        if ( listproxy == null )
        {
            throw new IllegalArgumentException("Inside ListProxy:copy(..) - listproxy cannot be null.");           
        }

        // Call base class copy
        super.copy((IFrameworkBaseObject)listproxy);
        
        // Set member attributes
        applyDataValues( listproxy.getDataValues() );
    }

    /**
     * reset iteration related attributes    
     */
    public void reset()
    {
        currentRecord     = null;
        recordCounter    = 0;        
    }
    
    /**
     * Returns a String representation the Collection of values.
     * @return String
     */
    public String toString()
    {
        return "Values =  " + dataValues.toString() +
                ",  " + super.toString();
    }

//************************************************************************
// Protected / Private Methods
//************************************************************************

//************************************************************************
// Attributes
//************************************************************************
    /**
     * encapsulated collection of data
     */
    protected ArrayList dataValues = null;
    
    /**
     * current record
     */
    protected HashMap currentRecord = null;
    
    /**
     * current record counter
     */
    protected int recordCounter = 0;
}

/*
 * Change Log:
 * $Log: ListProxy.java,v $
 */
