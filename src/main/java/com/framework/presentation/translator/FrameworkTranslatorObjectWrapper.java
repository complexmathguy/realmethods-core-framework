/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.presentation.translator;

/**
 * Helper class used to wrap an Object to translate, and certain characteristics
 * about the Object wrapped.
 * <p>
 * @author    realMethods, Inc.
 */
public class FrameworkTranslatorObjectWrapper extends Object
{

// constructors

    /**
     * default constructor - deter external instantiation
     */
    private FrameworkTranslatorObjectWrapper()
    {}
    
    /**
     * Constructor.  For pre 3.1 compatibility
     * <p>
	 * @param       object		the wrapped object
	 * @param       canBeNull	null is a valid value
     */
    public FrameworkTranslatorObjectWrapper( Object object, boolean canBeNull )
    {        
        if ( object instanceof java.util.Date )
            theObject =  BaseTranslator.formatDate( (java.util.Date)object );
        else if ( object instanceof java.util.Calendar )
            theObject =  BaseTranslator.formatDate( (java.util.Calendar )object );
        else
            theObject = object;
        
        this.canBeNull    = canBeNull;
    }
    
	/**
	 * Constructor
	 * <p>
	 * @param		name		name of wrapped object
	 * @param       object		the wrapped object
	 * @param       canBeNull	null is a valid value
	 */
	public FrameworkTranslatorObjectWrapper( String name, Object object, boolean canBeNull )
	{
		this( object, canBeNull );
		this.name = name; 
	}
	
// accessor methods

	/**
	 * Returns the name.
	 * <p>
	 * @return		the name
	 */
	public String getName()
	{
		return( name );
	}
	
    /**
     * Returns the wrapped Object.
     * <p>
     * @return      Object
     */
    public Object getTheObject()
    {
        return( theObject );
    }
    
    /**
     * Returns a T/F indicating if the wrapped Object can be null.
     * <p>
     * @return      boolean
     */
    public boolean canBeNull()
    {
        return( canBeNull );
    }
    
// Object overloads

    /**
     * Provides a String representation of the wrapped Object.
     * <p>
     * @return      String
     */
    public String toString()
    {
        return( theObject != null ? theObject.toString() : "" );
    }
    
// attributes

	/**
	 * name of the wrapped object
	 */
	protected String name		= null;
	/**
	 * wrapped entity
	 */
    protected Object theObject   = null;
    /**
     * null allowed indicator
     */
    protected boolean canBeNull	= false;

}

/*
 * Change Log:
 * $Log: FrameworkTranslatorObjectWrapper.java,v $
 */




