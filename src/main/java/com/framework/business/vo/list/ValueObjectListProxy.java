/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.business.vo.list;

// **********************
// Imports
// **********************
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.framework.business.pk.FrameworkPrimaryKey;
import com.framework.business.pk.IFrameworkPrimaryKey;

import com.framework.business.vo.FrameworkValueObject;
import com.framework.business.vo.IFrameworkValueObject;

import com.framework.common.IFrameworkBaseObject;

/**
 * ValueObjectListProxy class.
 * <p>
 * Acting as a wrapper to a Collection of IFrameworkValueObjects, but is itself an 
 * IFrameworkValueObject so the Framework will treat is as any other IFrameworkValueObject.  
 * This is useful for caching, notification, etc..
 * <p> 
 * @author    realMethods, Inc.
 */
public class ValueObjectListProxy 
	extends FrameworkValueObject
	implements IListProxy 
{
//************************************************************************
// Public Methods
//************************************************************************
    /** 
     * Constructor
     * @param 		data
     * @exception	IllegalArgumentException
     */
    public ValueObjectListProxy( Collection data ) 
    throws IllegalArgumentException
    {
        // Call base class
        super();
        
        // Validate parameters
        if ( data == null )
        {
            throw new IllegalArgumentException("Inside ListProxy::ListProxy(..) - Collection arg cannot be null.");
        }
        
        applyDataValues( data );
    }

    /** 
     * Constructor - assign a unique identifier to this provided Collection of IFrameworkValueObjects
     *
     * @param       key		unique id  to apply to this instance
     * @param     	data	values of interest
     * @exception	IllegalArgumentException 
     */
    public ValueObjectListProxy( String key, Collection data )
    throws IllegalArgumentException
    {
        // Call base class
        super();
        
        // Validate parameters
        if ( data == null )
        {
            throw new IllegalArgumentException("Inside ListProxy::ListProxy(..) - Collection arg cannot be null.");
        }
        
        // apply the provided key as the Framework primary key
        setFrameworkPrimaryKey( new FrameworkPrimaryKey( key ) );
        
        applyDataValues( data );
    }


    /** 
     * Constructor will copy a IListProxy
     * <p>
     * @param 		listproxy
     * @exception	IllegalArgumentException 
     */
    public ValueObjectListProxy( IListProxy listproxy ) 
    throws IllegalArgumentException
    {
        copy( listproxy );
    }   

    /**
     * Returns a Collection of HashMaps, each representing records of data.
     * @return		Collection
     */
    public Collection getDataValues()
    {
        return dataValues;
    } 
    
    /**
     * Moves the pointer to the next record and returns the Object there, 
     * if there is one.  If there isn't one, throws a NoSuchElementException
     * <p>
     * @return		Object
     * @exception	NoSuchElementException
     */
    public Object next()
    throws NoSuchElementException
    {
        if ( hasNext() )
        {
            // Retrieve value
            currentValueObject = (IFrameworkValueObject)dataValues.get(recordCounter);
            
            // Increment the count
            recordCounter++;
            
            return( currentValueObject );          
        }
        else
        { 
            throw new NoSuchElementException( "ValueObjectListProxy:next() - there is no next entry in the list." );
        }
        
    }
    
    /**
     * Moves the pointer to the previous record and returns the Object there, 
     * if there is one.  If there isn't one, throws a NoSuchElementException
     * <p>
     * @return		Object
     * @exception	NoSuchElementException
     */
    public Object previous()
    throws NoSuchElementException
    {
        if ( hasPrevious() )
        {
            // Decrement the count
            recordCounter--;
            
            // Retrieve value
            currentValueObject = (IFrameworkValueObject)dataValues.get(recordCounter);
            
            return( currentValueObject );
        }
        else
        {
            throw new NoSuchElementException( "ValueObjectListProxy:previous() - there is no previous entry in the list." );
        }
    }
    
    /**
     * Moves the pointer to the next n records, or as many as possible, and returns them.
     * <p>
     * @param       numberToGet
     * @return      Collection
     */
    public Collection next( int numberToGet )
    {
        boolean done 		= false;
        ArrayList tmpData 	= new ArrayList( numberToGet );
        
        for ( int i = 0; (i < numberToGet) && !done ; i++ )
        {
            try
            {
                tmpData.add( next() );
            }
            catch( NoSuchElementException exc )
            {
                done = true;
            }
        }
        
        return( tmpData );
    }
    
    /**
     * Moves the pointer to the previous n records, or as many as possible,
     * and returns them
     * <p>
     * @param       numberToGet
     * @return      Collection
     */
    public Collection previous( int numberToGet )
    {
        boolean done 		= false;
        ArrayList tmpData 	= new ArrayList( numberToGet );
        
        for ( int i = 0; (i < numberToGet) && !done ; i++ )
        {
            try
            {
                tmpData.add( previous() );
            }
            catch( NoSuchElementException exc )
            {
                done = true;
            }
        }
        
        return( tmpData );
        
    }

    /**
     * Returns whether there is a previous record in the iterator.
     * <p>
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
     * Returns whether there are n records before the beginning
     * of the iterator.
     * @param	numberToConsider
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
     * Returns whether there are any more than n records left in the iterator.
     * @return boolean
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
     * Checks to see if there is a contained IFrameworkValueObject matching the provided primary key.
     * <p>
     * @param       key
     * @return      boolean
     */
    public boolean containsValueObject( IFrameworkPrimaryKey key )
    {
        boolean contains = false;
        
        if ( dataValues != null )
        {
            Iterator iter               = dataValues.iterator();
            IFrameworkValueObject vo    = null;
            
            while( iter.hasNext() && contains == false )
            {
                vo = (IFrameworkValueObject)iter.next();
                
                if( vo.getFrameworkPrimaryKey().equals( key ) )
                {
                    contains = true;
                }
            }
        }
        
        return( contains );
    }
    
    /**
     * Returns the associated IFrameworkValueObject mated to the provided key.
     * @param       key
     * @return      IFrameworkValueObject
     */
    public IFrameworkValueObject getValueObject( IFrameworkPrimaryKey key )
    {
        IFrameworkValueObject vo    = null;
        boolean found				= false;
        
        if ( dataValues != null )
        {
            Iterator iter       = dataValues.iterator();
            
            while( iter.hasNext() && found == false )
            {
                vo = (IFrameworkValueObject)iter.next();
                
                if( vo.getFrameworkPrimaryKey().equals( key ) )
                {
                    found = true;
                }
            }
        }
        
        if ( found == false )
            return( null );
        else
            return( vo );
    }

    /**
     * Apply a Collection of data values. 
     * @param	values
     */    
    public void applyDataValues( Collection values )
    {
        // Set member attributes
        dataValues = new ArrayList( values );        
    }
    

    /**
     * Performs a deep copy.
     * <p.
     * @param  		listproxy
     * @exception	IllegalArgumentException	if listproxy is null
     */
    public void copy( IListProxy listproxy ) 
    throws IllegalArgumentException
    {
        // Validate that the proxy is the correct type
        if ( listproxy == null )
        {
            throw new IllegalArgumentException("Inside ValueObjectListProxy:copy(..) - proxy cannot be null.");           
        }

        // Call base class copy
        super.copy( (IFrameworkBaseObject)listproxy );
        
        // Set member attributes
        applyDataValues( listproxy.getDataValues() );
    }

    /**
     * reset iteration related attributes    
     */
    public void reset()
    {
        currentValueObject  = null;
        recordCounter    	= 0;        
    }
    
    /**
     * Returns a String representation of the underlying data values.
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
     * the data being wrapped
     */
    protected ArrayList dataValues = null;
    /**
     * current value object.
     */
    protected IFrameworkValueObject currentValueObject = null;
    /**
     * current record counter.
     */
    protected int recordCounter = 0;
}

/*
 * Change Log:
 * $Log: ValueObjectListProxy.java,v $
 * Revision 1.2  2003/08/05 12:16:17  tylertravis
 * - Updated header language to LGPL
 * - Improved exception handling globally
 * - Modified member variables to conform to Java Beans std.
 *
 * Revision 1.1.1.1  2003/07/05 03:05:14  tylertravis
 * initial sourceforge cvs revision
 *
 */

/*
 * Change Log:
 * $Log: ValueObjectListProxy.java,v $
 */
