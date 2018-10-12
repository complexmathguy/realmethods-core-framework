/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.task;

//************************************
// Imports
//************************************

/**
 * Encapsulates the concepts of a Task status.  The current statuses are:
 * <p>
 * <li>Idle</li>
 * <li>Bound</li>
 * <li>Processing</li>
 * <li>InError</li>
 * <p>
 * @author    realMethods, Inc.
 */
public class TaskStatus implements java.io.Serializable
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * default constructor - deter instantiation.
     * <p>
     * Instead, use the defined static instance attributes.
     */
    protected TaskStatus()
    {
    }

    /**
     * constructor - deter instantiation.
     * <p>
     * @param		status
     */
    protected TaskStatus( int status )
    {
        this.status = status;
    }

// accessor methods

    /**
     * return the status
     * @return          int
     */
    public int getStatus()
    {
        return( status );
    }

    /**
     * is the task currently idle
     * @return          boolean
     */
    public boolean isIdle()
    {
        return( status == 0 );        
    }

    /**
     * is the task currently bound with a ITask
     * @return          boolean
     */
    public boolean isBound()
    {
        return( status == 1 );        
    }


    /**
     * is the task currently processing?
     * @return          boolean
     */
    public boolean isProcessing()
    {
        return( status == 2 );        
    }

    /**
     * is the task currently in error?
     * @return          boolean
     */
    public boolean isInError()
    {
        return( status == 3 );        
    }

// attributes

	/**
	 * status indicator
	 */
    protected int status                              = 0;

	/**
	 * idle global instance
	 */
    public final static TaskStatus Idle         = new TaskStatus( 0 );
    /**
     * bound global instance
     */
    public final static TaskStatus Bound        = new TaskStatus( 1 );
    /**
     * processing global instance
     */
    public final static TaskStatus Processing   = new TaskStatus( 2 );
    /**
     * error global instancce
     */
    public final static TaskStatus InError      = new TaskStatus( 3 );
}


/*
 * Change Log:
 * $Log: TaskStatus.java,v $
 */



