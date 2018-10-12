/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.properties;

import com.framework.common.FrameworkBaseObjectFactory;

import com.framework.common.exception.ObjectCreationException;

/**
 * Factory singleton pattern implementation for a FrameworkPropertiesHandler.
 * <p>
 * @author    realMethods, Inc.
 */
public class FrameworkPropertiesHandlerFactory extends FrameworkBaseObjectFactory
{
    /** 
     * constructor - deter instantiation 
     */ 
    protected FrameworkPropertiesHandlerFactory()
    {
    	super( true /*cache all distinct class instances as singletons*/ );
    }
     
//	factory method
	 
	 static public FrameworkPropertiesHandlerFactory getInstance()
	 { 
	 	if ( instance == null )
	 		instance  =  new FrameworkPropertiesHandlerFactory();
	 		
		return( instance );	 		
	 }	 
    
	/**
	 * Factory method used to create a IPropertiesHandler, using the provided class name.
	 * <p>
	 * @param		nameToInstantiate
	 * @return      IBasePropertiesHandler
	 * @exception   ObjectCreationException
	 */
	public IPropertiesHandler getPropertyHandler( String nameToInstantiate )
	throws ObjectCreationException
	{	    
		IPropertiesHandler handler = null;
		   
		try
		{
			handler = (IPropertiesHandler)getObject( nameToInstantiate );	
		}
		catch( Throwable exc )
		{
			throw new  ObjectCreationException( "FrameworkPropertiesHandlerFactory.getPropertyHandler(String) - " + exc, exc );
		}
		
		return( handler );    	
	}
		

// attributes

	/**
	 * Singleton factory instance
	 */
    private static FrameworkPropertiesHandlerFactory instance = null;
}

/*
 * Change Log:
 * $Log: FrameworkPropertiesHandlerFactory.java,v $
 */
