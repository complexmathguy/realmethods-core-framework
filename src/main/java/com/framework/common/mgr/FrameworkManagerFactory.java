/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.mgr;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.startup.StartupManager;

/**
 * Simple factory singleton pattern implementation for a FrameworkManager.
 * <p> 
 * @author    realMethods, Inc.
 * @see		  com.framework.common.startup.StartupManager
 */
public class FrameworkManagerFactory extends FrameworkBaseObject
{
    /** 
     * constructor
     */ 
    protected FrameworkManagerFactory()
    {}
    
    /**
     * The entry point to getting the framework started.  
     * <p>
     * Kept around for backward compatibility, but will be deprecated during the spring of 2004.
     * <p>
     * <i>Use com.framework.common.startup.StartupManager instead.</i>
     * @param   applicationName			name of the application
     * @param   propertiesLocation		file path to the propertyfiles
     */
    public static void start( String applicationName, String propertiesLocation )
    {
    	StartupManager.getInstance().start( applicationName, propertiesLocation );        
    }
    
    /**
     * The entry point to getting the Framework started.  Called by FrameworkBaseServlet
     * and FrameworkStrutsActionServlet.  If not using one of these two classes,
     * use the overloaded version start( String applicationName, String propertiesLocation );
     * <p>
     * @param   applicationName		name of the application
     */    
    public static void start( String applicationName )
    {        
		StartupManager.getInstance().start( applicationName );
    }
 
	    
	/**
	 * Stops the Framework
	 * <p>
	 * Kept around for backward compatibility, but will be deprecated during the spring of 2004.
     * <p>
     * <i>Use com.framework.common.startup.StartupManager instead.</i>
	 */     
	public static void stop()
	{
		StartupManager.getInstance().stop();	
	}
	     
    /**
     * factory method used to create a IFrameworkManager
     *
     * @return      IFrameworkManager
     */
    public static IFrameworkManager getObject()
    {
        // lazy initialization
        if ( object == null )
        {
			StartupManager.getInstance().start(null,null);        	
        	object =  new FrameworkManager();
        }
        
        return( object );
    }
    
    /**
     * help the dependant application to get the ball rolling
     * for this VM
     */
    static public void main(String args[])
    {
        // get the ball rolling
        getObject();
    }
    
// attributes
	/**
	 * singleton object created by this factory
	 */
    private static IFrameworkManager object 	= null;}

/*
 * Change Log:
 * $Log: FrameworkManagerFactory.java,v $
 */
