/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.business.bo;

import com.framework.business.pk.FrameworkPrimaryKey;

/**
 * Base class for all business objects as generated and/or used by the Framework.
 * Such objects are simply used to encapsulate business data and not business processing, although
 * intraobject validation should be seen as acceptable.   
 * <p>
 * Subclass FrameworkValueObject for backwards compatibility, but will take
 * on its functionality in the near future. 
 * <p>
 * @author    realMethods, Inc.
 */
abstract public class FrameworkBusinessObject 
    extends com.framework.business.vo.FrameworkValueObject 
    implements IFrameworkBusinessObject
{
//***************************************************   
// Public Methods
//***************************************************
    /**
     * Default constructor.
     */
    public FrameworkBusinessObject( )
    {
    	super();
    }    

    /**
     * @param   key		assigned unique identifier for this instance
     */
    public FrameworkBusinessObject( FrameworkPrimaryKey key )
    {
		super( key );
    }
    
    /**
     * Performs a deep copy into the provided object.
     * @param object 					copy target
     * @exception IllegalArgumentException 	Thrown if the passed in obj is null. It is also
     * 							thrown if the passed in businessObject is not of the correct type.
     */
    public void copyInto( IFrameworkBusinessObject object ) 
    throws IllegalArgumentException
	{
    	// handle what is in FrameworkValueObject
    	object.applyVersioning( isVersioned() );
    	object.setVersionID( getVersionID() );
	}

//***************************************************   
// Protected/Private Methods
//***************************************************
 
//***************************************************   
// Attributes
//***************************************************
}

/*
 * Change Log:
 * $Log: FrameworkBusinessObject.java,v $
 */
