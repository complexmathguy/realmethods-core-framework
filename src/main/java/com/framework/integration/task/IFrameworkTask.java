/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.task;

import java.util.Collection;
import java.util.Map;

import com.framework.common.exception.TaskExecutionException;

import com.framework.common.message.IFrameworkMessage;

import com.framework.integration.objectpool.IConnectionImpl;

/**
 * Base interface for all Framework related tasks.
 * <p>
 * @author    realMethods, Inc.
 */
public interface IFrameworkTask 
	extends java.io.Serializable
{

    /**
     * Allows a client to bind a message to be handled directly to a Task. 
     * Otherwise, the client will expect a Task to be able to acquire it's own message
     * to be worked on
     * <p>
     * @param	msg
     */
    public void bindMessage( IFrameworkMessage msg );

    /**
     * Allows a client to bind a set of IFrameworkCommands.  Use this method in situations
     * where the CommandFactory is not to be used for command association.
     * <p>
     * @param	commands
     */
    public void bindCommands( Collection commands );     

    /**
     * Returns the associated IFrameworkCommand interfaces.
     * <p>
     * @return      Collection
     */
    public Collection getCommands();

    /**
     * Notification to get a message.
     * <p>
     * @return      IMessage  
     */
    public IFrameworkMessage acquireMessage();

    /**
     * Call acquireMessage() in order to execute and assumes it will handle
     * the commit or rollback of the transaction.
     * <p>
     * @exception   TaskExecutionException
     */
    public void execute()
    throws TaskExecutionException;

    /**
     * Call acquireMessage() in order to execute.
     * <p>
     * @param      	handleCommit
     * @exception   TaskExecutionException
     */
    public void execute( boolean handleCommit )
    throws TaskExecutionException;

    /**
     * Execute the logical grouping of commands over the provided msg.
     * <p>
     * @param       msg
     * @param       handleCommit
     * @exception   TaskExecutionException
     */
    public void execute( IFrameworkMessage msg, boolean handleCommit )
    throws TaskExecutionException;

    /**
     * Is this task in the middle of execution?
     * <p>
     * @return       boolean
     */
    public boolean isExecuting();


    /**
     * Awake indicator.
     * @return  boolean
     */
    public boolean isAwake();

    /**
     * Asleep indicator.
     * @return  boolean
     */
    public boolean isAsleep();


    /**
     * Commits all transactions for the current execution cycle.
     */
    public void commitTransactions();

    /**
     * Rollback all transactions for the current execution cycle.
     */
    public void rollbackTransactions();

    /**
     * Returns the associated connection.
     * <p>
     * @param       name
     * @return      IConnection
     */
    public IConnectionImpl getConnection( String name );

    /**
     * Returns the associated connections.
     * @return      Map
     */
    public Map getConnectionsInUse();

    /**
     * Returns the designated time to start this task - a null implies now.
     * @return		java.util.Calendar
     */
    public java.util.Calendar getStartTime();

    /**
     * Assign the time to start this task - a null implies now.
     * @param   	startTime
     */
    public void setStartTime( java.util.Calendar startTime );

    /**
     * returns the time this task went to sleep
     * @return	java.util.Calendar
     */
    public java.util.Calendar getWentToSleepTime();

    /**
     * Puts the task to "sleep".
     */
    public void sleep();

    /**
     * Returns the time to wait before restarting the task - < 0 implies immediately.
     * <p>
     * @return		long
     */
    public long getSleepTimeInMillis();

    /**
     * Assigns the time to wait before restarting the task - < 0 implies immediately.
     * <p>
     * @param       sleepTime
     */
    public void setSleepTimeInMillis( long sleepTime );

    /**
     * Returns the current task status.
     * @return      TaskStatus
     */
    public TaskStatus getTaskStatus();

    /**
     * Assign a task status.
     * @param   status
     */
    public void setTaskStatus( TaskStatus status );

    /**
     * Is the task available for execution at this time.
     * <p>
     * @param       bindNow		indicates to put the task into a bound state
     *                                     for synchronization purposes 
     * @return      boolean
     */
    public boolean canBeExecuted( boolean bindNow );    
}

/*
 * Change Log:
 * $Log: IFrameworkTask.java,v $
 */
