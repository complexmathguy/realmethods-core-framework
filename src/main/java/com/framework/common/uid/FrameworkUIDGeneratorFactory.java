/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.common.uid;

import java.beans.Beans;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.exception.ObjectCreationException;

import com.framework.common.misc.Utility;

import com.framework.common.namespace.FrameworkNameSpace;

/**
 * Simple factory singleton pattern implementation for a FrameworkUIDGenerator.
 * <p>
 * @author    realMethods, Inc.
 */
public class FrameworkUIDGeneratorFactory extends FrameworkBaseObject
{
    /** 
     * constructor - deter instantiation 
     */ 
    protected FrameworkUIDGeneratorFactory()
    {}
     
    /**
     * factory method used to acquire a IFrameworkUIDGenerator
     *
     * @return      IFrameworkUIDGenerator
     * @exception   ObjectCreationException
     */
    public static IFrameworkUIDGenerator getObject()
    throws ObjectCreationException
    {
		if ( uidGenerator == null )
		{
			// attempt to load the implementation
    	    String className = Utility.getFrameworkProperties().getProperty(FrameworkNameSpace.FRAMEWORK_UID_GENERATOR, "com.framework.common.uid.FrameworkUIDGenerator");  			
    	    
			try
			{
	            uidGenerator = (IFrameworkUIDGenerator)(Beans.instantiate( Thread.currentThread().getContextClassLoader(), className ) );
	            new FrameworkBaseObject().logDebugMessage( "FrameworkUIDGeneratorFactory:getObject() - using UID Generator class " + className );				
			}
			catch( Throwable exc )
			{
				new FrameworkBaseObject().logWarnMessage( "FrameworkUIDGeneratorFactory:getObject() - failed in loading the UID Generator class " + className + " - " + exc + ".  Using default Framework implementation com.framework.common.FrameworkUIDGenerator" );
				uidGenerator = new com.framework.common.uid.FrameworkUIDGenerator();
			}
		}
		
		return( uidGenerator );	    	
    }
    
// attributes

	/**
	 * singleton factory output
	 */
	static protected IFrameworkUIDGenerator uidGenerator = null;    
}    
    
/*
 * Change Log:
 */    
