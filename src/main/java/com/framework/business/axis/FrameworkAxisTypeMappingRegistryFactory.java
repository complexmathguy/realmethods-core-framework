/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.business.axis;

import com.framework.common.FrameworkBaseObjectFactory;
import com.framework.common.exception.ObjectCreationException;
import com.framework.common.misc.Utility;
import com.framework.common.namespace.FrameworkNameSpace;

/**
 * Factory singleton pattern implementation for a IFrameworkAxisTypeMappingRegistry
 * <p>
 * @author    realMethods, Inc.
 */
public class FrameworkAxisTypeMappingRegistryFactory 
extends FrameworkBaseObjectFactory
{
    /** 
     * constructor - deter instantiation 
     */ 
    protected FrameworkAxisTypeMappingRegistryFactory()
    {
    	super( false );
    }
     
//	factory method
	 
	 static public FrameworkAxisTypeMappingRegistryFactory getInstance()
	 { 
	 	if ( instance == null )
	 		instance  =  new FrameworkAxisTypeMappingRegistryFactory();
	 		
		return( instance );	 		
	 }	 
    
	/**
	 * Factory method used to dynamically create a 
	 * <p>
	 * @return      IBasePropertiesHandler
	 * @exception   ObjectCreationException
	 */
	public IFrameworkAxisTypeMappingRegistry getAxisTypeMappingRegistry()
	throws ObjectCreationException
	{	    
		if ( registry == null )
		{
			// get the class name from the framework properties file
			String className = Utility.getFrameworkProperties().getProperty( FrameworkNameSpace.AxisTypeMappingRegistry );
			
			if ( className != null )
			{
				registry = (IFrameworkAxisTypeMappingRegistry)getObject( className );
				logInfoMessage( "FrameworkAxisTypeMappingRegistryFactory created registry class " + className );
			}
			else
				logDebugMessage( "FrameworkAxisTypeMappingRegistryFactory is unable to read the className for property " + FrameworkNameSpace.AxisTypeMappingRegistry + " from the framework.xml config file." );
		}
		
		return( registry );    	
	}
		

// attributes

	/**
	 * Singleton factory instance
	 */
    private static FrameworkAxisTypeMappingRegistryFactory instance = null;
    
    private IFrameworkAxisTypeMappingRegistry registry = null;
}

/*
 * Change Log:
 * $Log: FrameworkPropertiesHandlerFactory.java,v $
 */
