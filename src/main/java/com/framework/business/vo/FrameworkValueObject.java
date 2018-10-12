/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.business.vo;

import com.framework.business.pk.FrameworkPrimaryKey;

import com.framework.common.FrameworkBaseObject;

/**
 * Use FrameworkBusinessObject instead.  This class remains for backwards compatibility.
 * <p> 
 * @author    realMethods, Inc.
 */
abstract public class FrameworkValueObject 
    extends FrameworkBaseObject implements IFrameworkValueObject
{
//***************************************************   
// Public Methods
//***************************************************
    /**
     * Default constructor.
     */
    public FrameworkValueObject( )
    {
    }    


    /**
     * @param   key		assigned unique identifier for this instance
     */
    public FrameworkValueObject( FrameworkPrimaryKey key )
    {
        setFrameworkPrimaryKey( key );
    }
    
    /**
     * Returns the associated FrameworkPrimaryKey
     * @return          FrameworkPrimaryKey
     */
    public FrameworkPrimaryKey getFrameworkPrimaryKey()
    {
		throw new RuntimeException( "FrameworkValueObject.getFrameworkPrimaryKey() must be overloaded in " + getClass().getName() );    	
    }

    /**
     * Assigns the associated IFrameworkPrimaryKey
     * @param       fpk
     */    
    public void setFrameworkPrimaryKey( FrameworkPrimaryKey fpk )
    {
		throw new RuntimeException( "FrameworkValueObject.setFrameworkPrimaryKey() must be overloaded in " + getClass().getName() );
    }

    /**
     * Performs a shallow copy value.
     * @param 		valueObject 					source to copy from
     * @exception 	IllegalArgumentException		if valueObject is null
     */
    public void shallowCopy( IFrameworkValueObject valueObject ) 
    throws IllegalArgumentException
    {
        // Validate incoming parameter
        if ( valueObject == null )
        {
            throw new IllegalArgumentException("Inside FrameworkValueObject::copy(..) - FrameworkValueObjectIn cannot be null.");   
        }
        
        copy( valueObject );
    }

    /**
     * Performs a copy value.
     * @param 		valueObject 					source to copy from
     * @exception 	IllegalArgumentException		if valueObject is null
     */
    public void copy( IFrameworkValueObject valueObject ) 
    throws IllegalArgumentException
    {
        // Validate incoming parameter
        if ( valueObject == null )
        {
            throw new IllegalArgumentException("Inside FrameworkValueObject::copy(..) - FrameworkValueObjectIn cannot be null.");   
        }
        
        // Set member attribute
        versioned				= valueObject.isVersioned();
        versionID             	= valueObject.getVersionID();
    }
    
    /**
     * String representation
     * @return String
     */
    public String toString()
    {
        return "Version ID = " + String.valueOf( versionID ) +
                ", Version Indicator = " + String.valueOf( versioned );
    }

//***************************************************   
// Protected/Private Methods
//***************************************************
    
    /**
     * Overloaded equals method to determine object equivalence.
     * Assumes that if the class names and the ids are the same,
     * and the version ids are the same the objects are equal.  
     * Overload to implement a different algorithm as necessary.
     *
     * @param       object		what to compare to
     * @return      boolean
     */
    public boolean equals( Object object )
    {
    	if ( object == this )
    		return true;
    		
        boolean bEquals     = false;
        FrameworkValueObject theObject = null;
        
        if ( object != null )
        {
            if ( object instanceof FrameworkValueObject )
            {
                theObject = (FrameworkValueObject)object;
                
            	if ( isVersioned() == theObject.isVersioned() )
            	{
                	if ( theObject.getVersionID().equals(getVersionID()) )
                    	bEquals = true;
            	}
            }
        }
        
        return( bEquals );
    }
 
     /**
     * Returns the version id for this instance.
     * @return      Long
     */
    public Long getVersionID()
    {
        return( versionID );
    }
    
    /**
     * Applies the id to the version value for this object.  
     * @param       id
     * @exception   IllegalArgumentException - thrown if the id is less than
     *              the existing version id.
     */
    public void setVersionID( Long id )
    throws IllegalArgumentException
    {
        // Shouldn't be, but assignment external to 
        // the framework may have taken place
        if ( getVersionID() != null )
        {
            if ( id != null && id.longValue() < getVersionID().longValue() )
                throw new IllegalArgumentException( "FrameworkValueObject:setVersionID( int ) - provided version id of " + id.toString() + " is less than the current version id of " + getVersionID().toString() + "." ); 
        }   
        
        versionID = id;            
    }
    
    /**
     * Indicates if this object instance is versioned or not.
     */
    public boolean isVersioned()
    {
        return( versioned );
    }

    /**
     * Turns versionig on or off.  It is best to call this method 
     * during construction
     * @param       apply
     */
    public void applyVersioning( boolean apply )
    {
        versioned = apply;
    }
    
    public int hashCode()
    {
		return( 29 * toString().hashCode() );
    }
 
//***************************************************   
// Attributes
//***************************************************
    
    /**
     * The version value to clearly identify this object instance from
     * another having the same FrameworkPrimarykey
     */
    protected Long versionID = new Long( 0 ); // default to initial value
    
    /**
     * Version indicator - true or false
     */
    protected boolean versioned = false;
    
}

/*
 * Change Log:
 * $Log: FrameworkValueObject.java,v $
 */
