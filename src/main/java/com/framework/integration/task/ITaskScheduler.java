/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.task;

import java.util.Collection;

/**
 * Base interface for a Task scheduler
 * <p>
 * @author    realMethods, Inc.
 */
public interface ITaskScheduler 
    extends java.io.Serializable
{
    /**
     * add a task
     * @param	task
     */
    public void addTask( IFrameworkTask task );

    /**
     * add a Collection of tasks
     * @param	tasks
     */
    public void addTasks( Collection tasks );

    /**
     * attempts to remove the provided task
     * @param	task
     */
    public void removeTask( IFrameworkTask task );

    /**
     * removes all tasks
     */
    public void removeAllTasks();

    /**
     * returns all scheduled tasks
     * @return      Collection
     */
    public Collection getTasks();

    /**
     * returns a number of "handleable" tasks  - <= 0 means all applicable
     * @return      Collection
     */
    public Collection getTasks( ITaskHandler taskHandler, int numTasks );
}

/*
 * Change Log:
 * $Log: ITaskScheduler.java,v $
 */
