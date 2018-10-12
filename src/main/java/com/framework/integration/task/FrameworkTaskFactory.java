/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.task;

import com.framework.common.FrameworkBaseObjectFactory;

import com.framework.common.exception.ObjectCreationException;

/**
 * Centralized creator of all system related Tasks.  A Task can be created independant
 * of this class, but this method is encouraged
 *
 * <p>
 * @author    realMethods, Inc.
 */
public class FrameworkTaskFactory
    extends FrameworkBaseObjectFactory implements IFrameworkTaskFactory
{
    
    /**
     * default constructor - deter external creation
     */
    protected FrameworkTaskFactory()
    {
       super( false /* cache nothing */ );
    }
    
// action methods

    /**
     * factory method to ensure the creation of one TaskFactory per VM
     * <p>
     * @return    	IFrameworkTaskFactory
     * @exception	ObjectCreationException
     */
    synchronized static public IFrameworkTaskFactory createInstance()
    throws ObjectCreationException
    {
    	try
    	{
	        if ( singleton == null )
    	        singleton = new FrameworkTaskFactory();
    	}
    	catch( Throwable exc )
    	{
    		throw new ObjectCreationException( "FrameworkTaskFactory:createInstance() - " + exc, exc );
    	}
    	    
        return( singleton );            
    }
 
    /**
     * returns the Class representation of the named Task class
     * <p>
     * @param       taskClassName
     * @return      Class
     */
    public Class getTaskAsClass( String taskClassName )
    {
        return( getClass( taskClassName ) );
    }

    /**
     * returns the ITask representation of the named Task class
     * <p>
     * @param       taskClassName
     * @return      Class
     */
    public IFrameworkTask getTaskAsObject( String taskClassName )
    {
        return( (IFrameworkTask)getObject( taskClassName ) );        
    }
    
// attributes

    /**
     * single instance per VM
     */
    static protected IFrameworkTaskFactory   singleton     = null;
}

/*
 * Change Log:
 * $Log: FrameworkTaskFactory.java,v $
 */
