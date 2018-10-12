/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.task;

import java.util.Iterator;
import java.util.Map;

import com.framework.common.exception.ObjectCreationException;
import com.framework.common.exception.TaskJMSRegistrationException;

import com.framework.common.misc.Utility;

import com.framework.common.namespace.FrameworkNameSpace;

import com.framework.common.properties.PropertyFileLoader;

/**
 * This class is dynamically invoked by the FrameworkManager during startup in order
 * to create the necessary objects required to handle Task execution via JMS.
 * <p>
 * Use this class to register a TaskJMSExecutionHandler with a Task and name.
 * <p>
 * @author    realMethods, Inc.
 */
public class TaskJMSExecutionRegistry 
    extends TaskJMSExecutionRegistryMBean implements ITaskJMSExecutionRegistry
{
    
// constructors  
     
    /**
     * default constructor - 
     * <p>
     * In order to ensure a singleton pattern with respect to this object type,
     * use the TaskJMSExecutionRegistryFactory.getObject() method
     * instead of creating one directly
     */
    public TaskJMSExecutionRegistry()
    throws InstantiationException
    {
        try
        {			         	
            dynamicallyRegisterJMSTasks();
        }
        catch( TaskJMSRegistrationException exc )
        {
            throw new InstantiationException( "TaskJMSExecutionRegistry:constructor() - Unable to dynamically load the registered JMS related Tasks: " + exc );
        }
    }
    
// action methods

    /**
     * Factory method to enforce the usage of a singelton per VM.
     * @return      TaskJMSExecutionRegistry
     */
    static public TaskJMSExecutionRegistry getInstance()
    throws ObjectCreationException
    {
        if ( singleton == null )
        {
        	try
        	{
            	singleton = new TaskJMSExecutionRegistry();
        	}
        	catch( Throwable exc )
        	{
        		throw new ObjectCreationException( "TaskJMSExecutionRegistry:getInstance() - " + exc, exc );
        	}
        }
        
        return( singleton );                
                        
    }

    /**
     * Use this method to register a Task class name with a message to 
     * execute on.  A TaskJMSRegistrationException is thrown if the registration fails.
     * If a Task by the classname is already registered, first call unregister, otherwise
     * a TaskJMSRegistrationException will be thrown.
     * <p>
     * @param       taskClassName                   fully qualified IFrameworkTask implementation                            
     * @param       executionMessage				message to associate with the task
     * @exception   IllegalArgumentException		thrown if either arg is null or empty
     * @exception   TaskJMSRegistrationException
     * 
     */
    public void registerTask( String taskClassName, String executionMessage )
    throws IllegalArgumentException, TaskJMSRegistrationException
    {
        if ( taskClassName == null || taskClassName.length() == 0 )
            throw new IllegalArgumentException( "TaskJMSExecutionRegistry:registerTask(...) - invalid task class name provided" );
            
        if ( executionMessage == null || executionMessage.length() == 0 )
            throw new IllegalArgumentException( "TaskJMSExecutionRegistry:registerTask(...) - invalid execution message string provided" );

        if ( taskJMSExecutionHandlers.containsKey( executionMessage ) ) 
            throw new TaskJMSRegistrationException( "TaskJMSExecutionRegistry:registerTask(...) - task already registered with execution message " + executionMessage );   
        	
        try
        {
            // create a TaskJMSExecutionHandler, determine if it should listen on JMS, 
            // and then track it            
            boolean listen = true;
            String sTaskMDBWrapperInUseIndicator = Utility.getFrameworkProperties().getProperty(FrameworkNameSpace.TASK_MDB_WRAPPER_IN_USE);
            
            if ( sTaskMDBWrapperInUseIndicator != null && sTaskMDBWrapperInUseIndicator.equalsIgnoreCase( "TRUE" ) )
                listen = false;
                
            // listen = false means the TaskMessageDriveBeanWrapper should be in use...                    
            taskJMSExecutionHandlers.put( executionMessage, new TaskJMSExecutionHandler( taskClassName, executionMessage, listen ) );            
        }
        catch( InstantiationException instExc )
        {
            throw new TaskJMSRegistrationException( "TaskJMSExecutionRegistry:registerTask(...) - failed to register task " + taskClassName + " with msg " + executionMessage + instExc, instExc );
        }
    }

    /**
     * Locate those Tasks within JNDI specified for registering at run-time
     * <p>
     * @exception       TaskJMSRegistrationException
     */
    private void dynamicallyRegisterJMSTasks()
    throws TaskJMSRegistrationException
    {
        try
        {        	    			         	
            // a Map of key-executionMsg, value-taskClassName pairings
            Map tasks 						= PropertyFileLoader.getInstance().getTaskPropertiesHandler().getTasks();
            Map taskOrCommandMap 			= null;
            String theTaskOrCommandValue 	= null;
            
            // loop through the Map and register the tasks            
            Iterator keys            	= tasks.keySet().iterator();
            Iterator taskCommandKeys = null;
            String type                	= null;
            String taskName            	= null;

            while( keys.hasNext() )
            {
                taskName = (String)keys.next();
                
                // get the
                taskOrCommandMap = PropertyFileLoader.getInstance().getTaskPropertiesHandler().getTaskParams( taskName );
                taskCommandKeys  = taskOrCommandMap.keySet().iterator();
                
                // should only be one entry                
                type = (String)taskCommandKeys.next();                
                
                // get the associated value
                theTaskOrCommandValue = (String)taskOrCommandMap.get( type );
                
                System.out.println( "Registering Task name: " + taskName+ ", value: " + type + "=" + theTaskOrCommandValue );
                
                this.registerTask( type + "=" + theTaskOrCommandValue, taskName );
            }
        }
        catch( Exception exc )
        {
            throw new TaskJMSRegistrationException( "TaskJMSExecutionRegistry:dynamicallyRegisterJMSTasks() failed - " + exc, exc );
        }
    }
    
// attributes
    
    /** 
     * the single instance of the class per VM
     */
    static private TaskJMSExecutionRegistry singleton     = null;

}

/*
 * Change Log:
 * $Log: TaskJMSExecutionRegistry.java,v $
 */
