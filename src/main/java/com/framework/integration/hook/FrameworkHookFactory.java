/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.hook;

import com.framework.common.FrameworkBaseObjectFactory;

import com.framework.common.exception.ObjectCreationException;

/**
 * Centralized creator of all system related Framework Hooks.  A Framework Hook can be created independant
 * of this class, but this method is encouraged
 * <p>
 * @author    realMethods, Inc.
 */
 
public class FrameworkHookFactory
    extends FrameworkBaseObjectFactory
{
    
    /**
     * default constructor - deter external creation
     */
    protected FrameworkHookFactory()
    {
        super( true /* caching */ );
    }
    
    
    
// action methods

    /**
     * factory method to ensure the creation of one FrameworkHookFactory per VM
     * <p>
     * @return      FrameworkHookFactory
     * @exception   ObjectCreationException
     */
    synchronized static public FrameworkHookFactory getInstance()
    throws ObjectCreationException
    {
    	try
    	{
	        if ( singleton == null )
    	        singleton = new FrameworkHookFactory();
    	}
    	catch( Throwable exc )
    	{
    		throw new ObjectCreationException( "FrameworkHookFactory:getInstance() - " + exc, exc );
    	}
    	    
        return( singleton );            
    }
 
    /**
     * returns the Class representation of the named Hook class
     * <p>
     * @param       hookClassName		fully qualified class name to create
     * @return      Class
     */
    public Class getHookAsClass( String hookClassName )
    {
        return( getClass( hookClassName ) );
    }

    /**
     * returns the IHook representation of the named Hook class
     * <p>
     * @param       hookClassName		fully qualified class name to create
     * @return      Class
     */
    public IFrameworkHook getHookAsObject( String hookClassName )
    {
        return( (IFrameworkHook)getObject( hookClassName ) );        
    }
    
// attributes

    /**
     * single instance per VM
     */
    static protected FrameworkHookFactory   singleton     = null;
}

/*
 * Change Log:
 * $Log: FrameworkHookFactory.java,v $
 */

