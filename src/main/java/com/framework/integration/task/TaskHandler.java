/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.task;

//************************************
// Imports
//************************************
import java.util.Iterator;

import com.framework.common.exception.TaskExecutionException;
import com.framework.common.exception.TaskHandlerException;

/**
 * For now, this handler is unsynchronized, which means that only one type of
 * handler should handle a specific task type.  A 1 to many mapping will lead to 
 * trouble.
 * <p>
 * Use caution when using this within the VM of an application server.  Application
 * servers typically manage a thread pool, and creating your own could lead to 
 * unforseen, intermittent problems.
 * <p>
 * @author    realMethods, Inc.
 */
public class TaskHandler extends Thread
    implements ITaskHandler
{
//****************************************************
// Public Methods
//****************************************************

// Runnable implementation from ITaskHandler

    /**
     * thread execution method
     */
    public void run()
    {
        ITaskScheduler scheduler = TaskScheduler.getInstance();

        while ( true )
        {
            // iterate over the Collection and attempt to handle each task
            Iterator iter = scheduler.getTasks( this, getMaxTasksToHandle() ).iterator();

            while ( iter.hasNext() )
            {
                try
                {
                    handleTask( (IFrameworkTask)iter.next() );
                }
                catch ( TaskHandlerException handlerExc )
                {
                    // do nothing
                }
            }

            try
            {
                // rest between execution cycles
                sleep( getSleepTime() );
            }
            catch ( Exception e )
            {
            }
        }
    }

// ITaskHandler implementations

    /**
     * Query method asking if the instance can handle the task.
     * <p>
     * @param           task
     * @return          boolean
     */
    public boolean canHandleTask( IFrameworkTask task )
    {
        return( true );
    }

    /**
     * task handler - should be overloaded
     * <p>
     * @param           task
     * @exception       TaskHandlerException
     */
    public void handleTask( IFrameworkTask task )
    throws TaskHandlerException   
    {
        if ( task == null )
            throw new TaskHandlerException( "TaskHandler:handleTask(...) - null task arg." );

        // have the task handle the acquisition of it's own message, and then
        // have it execute over it, with self-commiting enabled
        try
        {
            task.execute();     
        }
        catch ( TaskExecutionException taskExc )
        {
            throw new TaskHandlerException( "TaskHandler:handleTask() - " + taskExc,  taskExc );
        }
        finally
        {
            // put the task to "sleep"
            task.sleep();
        }
    }

    /**
     * max. # of tasks to get from the scheduler at a time.
     * <p>
     * @return      int
     */
    public int getMaxTasksToHandle()
    {
        return( maxTaskToHandle );
    }

    /**
     * assign max. # of tasks to get from the scheduler at a time.
     * <p>
     * @param      max
     */
    public void setMaxTasksToHandle( int max )
    {
    	maxTaskToHandle = max;
    }


    /**
     * how long to sleep between execution cycles in millis
     * <p>
     * @return      int
     */
    public int getSleepTime()
    {
        return( sleepTime );
    }

    /**
     * assign how long to sleep between execution cycles in millis
     * <p>
     * @return      int
     */
    public void setSleepTime( int time )
    {
        sleepTime = time;
    }

// attributes

	/**
	 * max # of tasks to handle
	 */
    protected int       maxTaskToHandle  	= 10;
    
    /**
     * how long to pause between a single task execution
     */
    protected int       sleepTime 			= 2000;    // default to 2 seconds
}

/*
 * Change Log:
 * $Log: TaskHandler.java,v $
 */
