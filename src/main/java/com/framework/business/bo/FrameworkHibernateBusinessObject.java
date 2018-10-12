/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.business.bo;

import com.framework.business.pk.FrameworkPrimaryKey;

import com.framework.integration.persistent.Persistent;

/**
 * Base class for all hibernate value object as generated and/or used by the Framework.
 * <p> 
 * @author    realMethods, Inc.
 */
abstract public class FrameworkHibernateBusinessObject 
    extends FrameworkBusinessObject implements Persistent
{
//***************************************************   
// Public Methods
//***************************************************
    /**
     * Default constructor.
     */
    public FrameworkHibernateBusinessObject()
    {
    }    

    /**
     * @param   key		assigned unique identifier for this instance
     */
    public FrameworkHibernateBusinessObject( FrameworkPrimaryKey key )
    {
        super( key );
    }
    

    /**
     * Performs a shallow copy value.
     * @param 		valueObject 					source to copy from
     * @exception 	IllegalArgumentException		if valueObject is null
     */
    public void shallowCopy( FrameworkHibernateBusinessObject valueObject ) 
    throws IllegalArgumentException
    {
    	super.shallowCopy( valueObject );
    	
        // Validate incoming parameter
        if ( valueObject == null )
        {
            throw new IllegalArgumentException("Inside FrameworkHibernateBusinessObject::copy(..) - FrameworkValueObjectIn cannot be null.");   
        }

		saved = valueObject.isSaved();
		       
    }

    /**
     * Performs a copy value.
     * @param 		valueObject 					source to copy from
     * @exception 	IllegalArgumentException		if valueObject is null
     */
    public void copy( FrameworkHibernateBusinessObject valueObject ) 
    throws IllegalArgumentException
    {
    	super.copy( valueObject );
    	
        // Validate incoming parameter
        if ( valueObject == null )
        {
            throw new IllegalArgumentException("Inside FrameworkHibernateBusinessObject::copy(..) - FrameworkValueObjectIn cannot be null.");   
        }

		saved = valueObject.isSaved();
		       
    }
    
    /**
     * String representation
     * @return String
     */
    public String toString()
    {
        return "Save Indicator = " + new Boolean( saved ).toString();
    }

// ***************************************************   
// Persistent Interface Implemenatations
// ***************************************************   

   /**
	* Called when saved
	*/	
    public void onSave()
    {
   		saved = true;
    }
	
   /**
	* Called when loaded
	*/
	public void onLoad()
	{
		saved = true;
	}
	
   /**
	* Returns a saved T/F indicated
	* @return	boolean
    */
    public boolean isSaved()
    {
    	return( saved );
    }
   
// bean properties
    
    public boolean getSaved()
    {
    	return( saved );
    }
    
    public void setSaved( boolean saved )
    {
    	this.saved = saved;
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
        boolean bEquals = false;
        
        if ( object != null )
        {
            if ( object instanceof FrameworkHibernateBusinessObject )
            {
                if ( ((FrameworkHibernateBusinessObject)object).isSaved() == saved )
                	bEquals = true;
            }                
        }
        
        return( bEquals );
    }
 
 
//***************************************************   
// Attributes
//***************************************************
    
    /**
     * Saved indicator - true or false
     */
    protected boolean saved = false;
    
}

/*
 * Change Log:
 * $Log: FrameworkValueObject.java,v $
 */
