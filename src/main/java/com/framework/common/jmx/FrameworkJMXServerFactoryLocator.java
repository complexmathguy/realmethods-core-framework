/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.jmx;

//***************************
//Imports
//***************************

import java.beans.Beans;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.namespace.FrameworkNameSpace;

import com.framework.common.misc.Utility;

/**
 * Locator for the IFrameworkJMXServerFactory implementation
 */
public class FrameworkJMXServerFactoryLocator extends FrameworkBaseObject
{
	
//************************************************************************    
// Constructors
//************************************************************************

   /**
   	* deter access
   	*/
  	private FrameworkJMXServerFactoryLocator()
	{}
  
//************************************************************************    
// Public Methods
//************************************************************************

	/**
	 * First time through, dynamically instantiates the IFrameworkJMXServerFactory specified by
	 * framework property JMX_MBEAN_SERVER_IMPLEMENTATION.  Defaults to com.framework.jmx.StandardJMXServerFactory 
	 * if none is specified
	 */
	synchronized static public IFrameworkJMXServerFactory locate()
	{
		if ( factory == null )
		{		
			String mBeanServerImplementation = Utility.getFrameworkProperties().getProperty( FrameworkNameSpace.JMX_MBEAN_SERVER_IMPLEMENTATION, "com.framework.jmx.StandardJMXServerFactory");
			try
			{
				factory = (com.framework.common.jmx.IFrameworkJMXServerFactory) Beans.instantiate( Thread.currentThread().getContextClassLoader(), mBeanServerImplementation );
			}
			catch( Throwable exc )
			{
				new FrameworkBaseObject().logErrorMessage( "FrameworkJMXServerFactoryLocator.locate() - failed due to " + exc );
			}
		}
		
		return ( factory );		
	}

//*************************************************************************    
// Protected / Protected Methods
//************************************************************************

//************************************************************************    
// Attributes
//************************************************************************

	static IFrameworkJMXServerFactory factory = null;
}

/*
 * Change Log:
 * $Log$
 */
