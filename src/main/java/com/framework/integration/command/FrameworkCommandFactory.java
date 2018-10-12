/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.command;

//************************************
// Imports
//************************************
import com.framework.common.FrameworkBaseObjectFactory;

import com.framework.common.exception.ObjectCreationException;

/**
 * class FrameworkCommandFactory
 * <p>
 * Factory for IFrameworkCommand interfaces
 *
 * <p>
 * @author    realMethods, Inc.
 */
public class FrameworkCommandFactory 
    extends FrameworkBaseObjectFactory implements IFrameworkCommandFactory
{
//****************************************************
// Public Methods
//****************************************************

    /**
     * default constructor - deter outside instantiation
     */
    protected FrameworkCommandFactory()
    {
        super( false /* cache parameter */ );
    }

// action methods

    /**
     * factory method to ensure the creation of one CommandFactory per VM
     *
     * @return    	IFrameworkCommandFactory
     * @exception	ObjectCreationException
     */
    synchronized static public IFrameworkCommandFactory createInstance()
    throws ObjectCreationException
    {
        if ( singleton == null )
        {
        	try
        	{
            	singleton = new FrameworkCommandFactory();
        	}
        	catch( Throwable exc )
        	{
        		throw new ObjectCreationException( "FrameworkCommandFactory:createInstance() - " + exc, exc );
        	}
        }
                    
        return( singleton );            
    }
 
	/**
	  * Returns the Class representation of the named Command class
	  * <p>
	  * @param       commandClassName	fully qualified Command class name
	  * @return      					class instance inferred from commandClassName
	  */
	 public Class getCommandAsClass( String commandClassName )
    {
        return( getClass( commandClassName ) );
    }

	/**
	 * Returns the IFrameworkCommand representation of the 
	 * fully qualified named IFrameworkCommand class
	 * <p>
	 * @param       commandClassName	fully qualified Command class name
	 * @return      					instance of object created by commandClassName
	 */
    public IFrameworkCommand getCommandAsObject( String commandClassName )
    {
        return( (IFrameworkCommand)getObject( commandClassName ) );        
    }

// attributes

    /**
     * singleton
     */
    protected static IFrameworkCommandFactory singleton  = null;

}

/*
 * Change Log:
 * $Log: FrameworkCommandFactory.java,v $
 */
