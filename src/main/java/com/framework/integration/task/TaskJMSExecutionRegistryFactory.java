/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.task;

import com.framework.common.exception.FrameworkStartupException;
import com.framework.common.exception.ObjectCreationException;

import com.framework.common.startup.FrameworkStartup;

/**
 * Factory singleton pattern implementation for a TaskJMSExecutionRegistry.
 * <p>
 * @author    realMethods, Inc.
 */
public class TaskJMSExecutionRegistryFactory 
	extends FrameworkStartup
{
    /** 
     * constructor - deter instantiation 
     */ 
    public TaskJMSExecutionRegistryFactory()
    {
    	instance = this;
    }
     
//	IFrameworkStartup implementation

	/**
	 * start invocation
	 * @exception	FrameworkStartupException
	 */
	public void start()
	throws FrameworkStartupException
    {
	}

	public void stop()
	{	      		
	}
	
	/**
	 * Factory  method to return singleton.
	 * <p>
	 * @return TaskJMSExecutionRegistryFactory
	 * @exception ObjectCreationException
	 */
	static synchronized public TaskJMSExecutionRegistryFactory getInstance()
	throws ObjectCreationException
	{
		if ( instance == null )
		 {  	
			 try
			 {	        					         		
				instance = new TaskJMSExecutionRegistryFactory();       
				instance.start();	                
			 }
			 catch( Throwable exc )
			 {
				 throw new ObjectCreationException( "TaskJMSExecutionRegistryFactory:getInstance() - " + exc, exc );
			 }    	        
		 }
        
		 return( instance );		
	}
	
    /**
     * method used to create a ITaskJMSExecutionRegistry
     * <p>
     * @return      ITaskJMSExecutionRegistry
     * @exception   ObjectCreationException
     */
    public ITaskJMSExecutionRegistry getObject()
    throws ObjectCreationException
    {
        // lazy initialization
        if ( object == null )
        {  	
        	try
        	{	        					         		
	            // Safe to create the TaskJMSExecutionRegistry 
    	        object = new TaskJMSExecutionRegistry();       	                
        	}
        	catch( Throwable exc )
        	{
        		throw new ObjectCreationException( "TaskJMSExecutionRegistryFactory:getObject() - " + exc, exc );
        	}    	        
        }
        
        return( object );
    }
    
// attributes

	/**
	 * factory self instance
	 */
	static private TaskJMSExecutionRegistryFactory instance = null;
	/**
	 * singleton instance of factory purpose
	 */
    private ITaskJMSExecutionRegistry object = null;
}

/*
 * Change Log:
 * $Log: TaskJMSExecutionRegistryFactory.java,v $
 */

