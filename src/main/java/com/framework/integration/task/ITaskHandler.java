/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.task;

import com.framework.common.exception.TaskHandlerException;

/**
 * Base interface for a Task handler
 * <p>
 * @author    realMethods, Inc.
 */
public interface ITaskHandler 
    extends java.io.Serializable
{
    /**
     * query method asking if the instance can handle the task
     * <p>
     * @param		task
     * @return		boolean
     */
    public boolean canHandleTask( IFrameworkTask task );

    /**
     * task handler invocation
     * <p>
     * @param    	task
     * @exception   TaskHandlerException
     */
    public void handleTask( IFrameworkTask task )
    throws TaskHandlerException;

    /**
     * max. # of tasks to get from the scheduler at a time
     * <p>
     * @return      int
     */
    public int getMaxTasksToHandle();    

}

/*
 * Change Log:
 * $Log: ITaskHandler.java,v $
 * Revision 1.2  2003/08/05 12:16:18  tylertravis
 * - Updated header language to LGPL
 * - Improved exception handling globally
 * - Modified member variables to conform to Java Beans std.
 *
 * Revision 1.1.1.1  2003/07/05 03:05:50  tylertravis
 * initial sourceforge cvs revision
 *
 */
