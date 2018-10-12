/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.task;

//************************************
// Imports
//************************************
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;


/**
 * class Taskinstance
 * <p>
 * As a singleton, used to schedule a FrameworkTask for execution.
 * <p>
 * Work in progress....will more than likely with a BPEL implementation
 * <p>
 * @author    realMethods, Inc.
 */
public class TaskScheduler implements ITaskScheduler
{
//****************************************************
// Public Methods
//****************************************************

    /**
     * default constructor - deter instantiation
     */
    protected TaskScheduler()
    {
		tasks = Collections.synchronizedCollection( new ArrayList() );
    }

// ITaskScheduler implementations

    /**
     * add a task.
     * @param	task
     */
    public void addTask( IFrameworkTask task )
    {
    	 tasks.add( task );            
    }

    /**
     * add a Collection of tasks.
     * @param	tasks	
     */
    public void addTasks( Collection tasks )
    {
		if ( tasks != null && tasks.size() > 0 )
        {
        	tasks.addAll( tasks );             
        }            
    }

   /**
    * attempts to remove the provided task.
    * @param	task
    */
    public void removeTask( IFrameworkTask task )
    {
		tasks.remove( task );            
    }

    /**
     * removes all tasks.
     */
    public void removeAllTasks()
    {
    	tasks.clear();            
    }

    /**
     * returns all scheduled tasks
     * @return      Collection
     */
    public Collection getTasks()
    {
        return( tasks ); 
    }

    /**
     * returns a number of "handle-able" tasks in a Collection - <= 0 means all applicable
     * <p>
     * @param		taskHandler		implementation to handle certain task types
     * @param		numTasks		# of tasks to return
     * @return		Collection
     */
    public Collection getTasks( ITaskHandler taskHandler, int numTasks )
    {
        int taskCounter   		= 1;
        IFrameworkTask task 	= null;
        Collection taskColl   	= new ArrayList( numTasks );

		synchronized( tasks )
		{		
			Iterator iter = tasks.iterator();
						
	        // enumerate through each task, asking the taskHandler if it can handle the task
	        while ( iter.hasNext() && taskCounter <= numTasks )
	        {
	            task = (IFrameworkTask)iter.next();
	
	            //if the handler can handle the task, add it to the Collection,
	            // and bump up the counter
	            if ( taskHandler.canHandleTask( task ) )
	            {
	                // if the task can be executed
	                if ( task.canBeExecuted( true /*it is bound now for synchronization purposes*/ ) )
	                {
	                    taskColl.add( task );
	                    taskCounter++;
	                }
	            }	            
	        }
        }

        return( taskColl );
    }

// singleton access

    /**
     * returns a single instance of a scheduler
     * @return		ITaskScheduler
     */
    synchronized public static final ITaskScheduler getInstance()
    {
        if ( instance == null )
        {
            instance = new TaskScheduler();
        }

        return( instance );
    }

// attributes

    /**
     * single instance of a Scheduler
     */
    static protected TaskScheduler instance  	= null;

    /**
     * Collection of Tasks
     */
    protected Collection tasks					= null;
}

/*
 * Change Log:
 * $Log: TaskScheduler.java,v $
 */
