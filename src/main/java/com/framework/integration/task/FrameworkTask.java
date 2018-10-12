/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.event.FrameworkEventType;

import com.framework.common.exception.TaskExecutionException;

import com.framework.common.message.FrameworkMessage;
import com.framework.common.message.IFrameworkMessage;

import com.framework.common.mgr.FrameworkManagerFactory;

import com.framework.integration.command.IFrameworkCommand;

import com.framework.integration.hook.FrameworkHookManager;
import com.framework.integration.hook.IPostTaskExecuteHook;
import com.framework.integration.hook.IPreTaskExecuteHook;
import com.framework.integration.hook.ITaskExecutionFailureHook;

import com.framework.integration.objectpool.ConnectionPoolManagerFactory;
import com.framework.integration.objectpool.IConnectionImpl;

import com.framework.integration.objectpool.jms.JMSImpl;

/**
 * Base class for all FrameworkTasks - a logical grouping of IFrameworkCommands
 * <p>
 * Upon creation, adds itself to the TaskScheduler.
 * <p>
 * @author    realMethods, Inc.
 */
public class FrameworkTask extends FrameworkBaseObject
    implements IFrameworkTask
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * default constructor
     * <p>
     * adds itself to the the scheduler
     */
    public FrameworkTask()
    {
        // add this task to the scheduler
        TaskScheduler.getInstance().addTask( this );
    }

    /**
     * constructor
     * <p>
     * @param		timetostart
     * @param		sleepPeriodInMillis
     */
    public FrameworkTask( java.util.Calendar timetostart, long sleepPeriodInMillis )
    {
        this();

        // assign to member variables
        startTime 	= timetostart;
        sleepPeriod = sleepPeriodInMillis;
    }


// IFrameworkTask implementations

    /**
     * Allows a client to bind a message to be handled directly to a Task. 
     * Otherwise, the client will expect a Task to be able to acquire it's own message
     * to be worked on.
     * <p>
     * @param		msg		data context to execute over
     */
    public void bindMessage( IFrameworkMessage msg )
    {
        bindMessage = msg;
    }

    /**
     * Allows a client to bind a set of IFrameworkCommands.  Use this method in situations
     * where the CommandFactory is not to be used for command association.
     * <p>
     * @param	commands
     */
    public void bindCommands( Collection commands )
    {
        this.commands = commands;
    }

    /**
     * returns the associated IFrameworkCommand interfaces
     * <p>
     * @return      Collection
     */
    public Collection getCommands()
    {
        return( commands );
    }

    /**
     * returns the IFrameworkCommand referenced by the provided index
     * <p>
     * @param       index
     * @return      IFrameworkCommand
     * @exception   IndexOutOfBoundsException
     */
    public IFrameworkCommand getCommand( int index )
    throws IndexOutOfBoundsException
    {
        try
        {
            return( (IFrameworkCommand)commands.toArray()[ index ] );
        }
        catch ( ArrayIndexOutOfBoundsException exc )
        {
            throw new IndexOutOfBoundsException( "Task:getCommand(...) - " + exc );
        }
    }

    /**
     * query method to answer if a command is contained
     *
     * @param       command
     * @return      boolean
     */
    public boolean containsCommand( IFrameworkCommand command )
    {
        if ( commands != null )
            return( commands.contains( command ) );
        else
            return( false );
    }

    /**
     * notification to get a message - SHOULD BE IMPLEMENTED IN THE SUB-CLASS
     * <p>
     * @return      IFrameworkMessage  
     */
    public IFrameworkMessage acquireMessage()
    {
        return( new FrameworkMessage() );
    }

    /**
     * call determineMessage() in order to execute and assumes it will handle
     * the commit or rollback of the transaction
     * <p>
     * @exception   TaskExecutionException
     */
    public void execute()
    throws TaskExecutionException
    {
        execute( determineMessageToUse(), true /* handle commit */ );
    }

    /**
     * calls determineMessageToUse() in order to execute
     *<p>
     * @param       bHandleCommit		internally handle the commit of transaction-based resources
     * @exception   TaskExecutionException
     */
    public void execute( boolean bHandleCommit )
    throws TaskExecutionException
    {
        execute( determineMessageToUse(), bHandleCommit );
    }

    /**
     * execute the logical grouping of commands over the provided msg
     * <p>
     * @param       msg					context to execute over
     * @param       bHandleCommit		nternally handle the commit of transaction-based resources
     * @exception   TaskExecutionException
     */
    public void execute( IFrameworkMessage msg, boolean bHandleCommit )
    throws TaskExecutionException
    {
        if ( msg == null )
            throw new TaskExecutionException( "FrameworkTask:execute(...) - null msg argument." );

        // is this task currently in progress
        if ( taskStatus == TaskStatus.Processing )
            throw new TaskExecutionException( "FrameworkTask:execute(...) - task already in progress" );

        // is this task currenly in an error status
        if ( taskStatus == TaskStatus.InError )
            throw new TaskExecutionException( "FrameworkTask:execute(...) - task in error." );

        // put the task into a status of processing
        setTaskStatus( TaskStatus.Processing );
                
        try
        {
            preTaskExecute( msg, bHandleCommit );
            onExecute( msg, bHandleCommit );
            postTaskExecute( msg, bHandleCommit );
        }
        catch( Exception exc )
        {
        	try
        	{
	        	taskExecutionFailure( msg, bHandleCommit );
        	}
        	catch( Throwable failExc )
        	{
        	}
            throw new TaskExecutionException( "Task.execute(...) - " + exc, exc );
        }
        
    }
    
    /**
     * Helper to execute over the contained commmands.
     * <p>
     * @param       msg					context to execute over
     * @param       bHandleCommit		nternally handle the commit of transaction-based resources
     * @exception   TaskExecutionException
     */
    private void onExecute( IFrameworkMessage msg, boolean bHandleCommit )
    throws TaskExecutionException
    {
        
        // check to see if there is something to do
        if ( commands != null && commands.size() > 0 )
        {        
            // loop through the Collection of commands and call execute on each
            IFrameworkCommand command 	= null;
            Iterator iter				= commands.iterator();

            try
            {
                // iterate through each associated command and call execute on it
                while ( iter.hasNext() )
                {
                    command = (IFrameworkCommand)iter.next();    
                    
                    // execute the command
                    command.execute( msg, this );
                }

                // safe to commit all transactions, if we are to handle them...
                if ( bHandleCommit == true )
                    commitTransactions();

            }
            catch ( Throwable exc )
            {
                logErrorMessage( "Task:execute() - Exception caught, rolling back all transactions - " + exc );

                // if we are to handle the commit, assume condition handling of the 
                if ( bHandleCommit == true )
                    rollbackTransactions();

                // put the task into an error status
                setTaskStatus( TaskStatus.InError );

                throw new TaskExecutionException( "Task:execute(...) - " + exc, exc );
            }
            finally
			{
                // put the task back to an idle status                
                setTaskStatus( TaskStatus.Idle );                            	
			}
        }
    }

// notification methods

    /**
     * Notify the rest of the system of the PreTaskExecute event.  Next,
     * attempt to delegate to a IPreTaskExecuteHook registered with the FrameworkHookManager
     * <p>
     * @param       msg					context to execute over
     * @param       bHandleCommit		nternally handle the commit of transaction-based resources
     * @exception   TaskExecutionException
     */
    protected void preTaskExecute( IFrameworkMessage msg, boolean bHandleCommit )
    throws TaskExecutionException
    {        
        // attempt to delegate to a hook
        IPreTaskExecuteHook hook = null;
        
        try
        {
            // gain access to the appropriate JMSImpl and send a PreTaskExecute event
            JMSImpl jmsImpl = (JMSImpl)FrameworkManagerFactory.getObject().getFrameworkEventJMSImpl();
            
            if ( jmsImpl != null )
            {
	            jmsImpl.sendObject( FrameworkEventType.PreTaskExecute );
    	        ConnectionPoolManagerFactory.getObject().releaseConnection( jmsImpl );
            }            
        }
        catch( Throwable exc )
        {
        }
        finally
        {        	
        }
        
        try
        {
        	hook = (IPreTaskExecuteHook)FrameworkHookManager.getHookManager().getHook( FrameworkEventType.PreTaskExecute.getEventType() );
        	
	        // if there is a hook and can handle things...delegate
	        if ( hook != null && hook.canHandle( this, msg ) == true ) 
	        {
	            hook.preTaskExecute( msg, bHandleCommit );
	        }
	        else  
	        {
	            // no op
	        }        	
        }
        catch( Throwable exc )
        {
        	throw new TaskExecutionException( "FrameworkTask:preTaskExecute() - " + exc, exc );
        }
        
    }
    
    /**
     * Notify the rest of the system of the PostTaskExecute event.  Next,
     * attempt to delegate to a IPostTaskExecuteHook registered with the FrameworkHookManager
     *
     * @param       msg
     * @param       bHandleCommit
     * @exception   TaskExecutionException
     */    
    protected void postTaskExecute( IFrameworkMessage msg, boolean bHandleCommit )
    throws TaskExecutionException
    {
        // attempt to delegate to a hook
        IPostTaskExecuteHook hook = null;
        
        try
        {
            // gain access to the appropriate JMSImpl and send a PreTaskExecute event
            JMSImpl jmsImpl = (JMSImpl)FrameworkManagerFactory.getObject().getFrameworkEventJMSImpl();
            
            if ( jmsImpl != null )
            {
	            jmsImpl.sendObject( FrameworkEventType.PostTaskExecute );
    	        ConnectionPoolManagerFactory.getObject().releaseConnection( jmsImpl );
            }
            
        }
        catch( Throwable hookExc )
        {
            // no_op...will do original logic
        }

		try
		{
	        hook = (IPostTaskExecuteHook)FrameworkHookManager.getHookManager().getHook( FrameworkEventType.PostTaskExecute.getEventType() );
	        
	        // if there is a hook and can handle things...delegate            
	        if ( hook != null && hook.canHandle( this, msg ) == true ) 
	        {
	            hook.postTaskExecute( msg, bHandleCommit );
	        }
	        else  
	        {
	            // no op
	        }
		}
		catch( Throwable exc )
		{
			throw new TaskExecutionException( "FrameworkTask:postTaskExecute() - " + exc, exc );
		}
    }

    /**
     * Notify the rest of the system of the TaskExecutionFailure event.  Next,
     * attempt to delegate to a ITaskExecutionFailureHook registered with the FrameworkHookManager
     * <p>
     * @param       msg					context to execute over
     * @param       bHandleCommit		nternally handle the commit of transaction-based resources
     * @exception   TaskExecutionException
     */    
    protected void taskExecutionFailure( IFrameworkMessage msg, boolean bHandleCommit )
    throws TaskExecutionException
    {
        // attempt to delegate to a hook
        ITaskExecutionFailureHook hook = null;
        
        try
        {
            // gain access to the appropriate JMSImpl and send a PreTaskExecute event
            JMSImpl jmsImpl = (JMSImpl)FrameworkManagerFactory.getObject().getFrameworkEventJMSImpl();
            
            if ( jmsImpl != null )
            {
	            jmsImpl.sendObject( FrameworkEventType.TaskExecutionFailure );
    	        ConnectionPoolManagerFactory.getObject().releaseConnection( jmsImpl );
            }
            
        }
        catch( Throwable hookExc )
        {
            // no_op...will do original logic
        }

		try
		{
	        hook = (ITaskExecutionFailureHook)FrameworkHookManager.getHookManager().getHook( FrameworkEventType.TaskExecutionFailure.getEventType() );
	        
	        if ( hook != null  ) 
	        {
	            hook.taskExecutionFailure( msg, bHandleCommit );
	        }
		}
		catch( Throwable exc )
		{
			throw new TaskExecutionException( "FrameworkTask:taskExecuteFailure() - " + exc, exc );
		}

    }

    /**
     * Is this task in the middle of execution.
     * @return       boolean
     */
    public boolean isExecuting()
    {
        return( taskStatus == TaskStatus.Processing );
    }

    /**
     * Awake interrogation.
     * @return  boolean
     */
    public boolean isAwake()
    {
        return( !isAsleep() );
    }

    /**
     * Asleep interrogation.
     * @return  boolean
     */
    public boolean isAsleep()
    {
        boolean bIsAsleep       = false;
        long now_in_millis      = java.util.Calendar.getInstance().getTime().getTime();
        long wake_up_in_millis  = 0;

        // need to figure out whether we are dealing with an initial start-up
        // or a task which has already started and been put to sleep

        if ( getWentToSleepTime() != null ) // it's explicitly been put to sleep
        {
            // use the time put to sleep + the period to sleep
            wake_up_in_millis = getWentToSleepTime().getTime().getTime() + this.sleepPeriod;  
        }
        else    // initial start
        {
            if ( getStartTime() == null )   // start now
            {
                wake_up_in_millis = now_in_millis;
            }
            else    // assign the start time
            {
                wake_up_in_millis = this.getStartTime().getTime().getTime();
            }                
        }

        if ( now_in_millis < wake_up_in_millis )
        {
            bIsAsleep = true;
        }

        return( bIsAsleep );
    }

    /*
     * returns the time this task went to sleep
     *
     * @return          java.util.Calendar
     */
    public java.util.Calendar getWentToSleepTime()
    {
        return( wentToSleepTime );
    }

    /**
     * puts the task to "sleep"
     *
     */
    public void sleep()
    {
        // set the went to sleep time to now
        wentToSleepTime = java.util.Calendar.getInstance();

//        logMessage( "Task : " + this.getClass().getName() + " put to sleep for " + String.valueOf( this.sleepPeriod ) + " milliseconds." );
    }

    /**
     * Is the task available for execution at this time
     * <p>
     * @param	bindNow
     */
    synchronized public boolean canBeExecuted( boolean bindNow )
    {
        boolean canBeExecuted = false;

        if ( isAwake() && taskStatus == TaskStatus.Idle )
        {
            canBeExecuted = true;

            if ( bindNow == true )
            {
                taskStatus = TaskStatus.Bound;
            }
        }

        return( canBeExecuted );
    }

    /**
     * commits all transactions for the current execution cycle
     */
    synchronized public void commitTransactions()
    {
        if ( currentConnections != null && currentConnections.size() > 0 )
        {
            Collection connections     	= new ArrayList( currentConnections.size() );
            Iterator iter				= currentConnections.values().iterator();
            IConnectionImpl connection  = null;

            // Note:  have to release after all commands have made use of the 
            // connection

            // loop through each connection, commit it, and release it
            while ( iter.hasNext() )
            {
                connection = (IConnectionImpl)iter.next();
                commitTransaction( connection );
                connections.add( connection );
            }

            // safe to now do so
            releaseAllConnections( connections );
            
            // to expedite gc
            currentConnections.clear();            
            currentConnections = null;
            
        }
    }

    /**
     * rollback all transactions for the current execution cycle
     */
    synchronized public void rollbackTransactions()
    {
        if ( currentConnections != null && currentConnections.size() > 0 )
        {
            Collection connections      = new ArrayList( currentConnections.size() );
            Iterator iter				= currentConnections.values().iterator();
            IConnectionImpl connection 	= null;

            // Note:  have to release after all commands have made use of the 
            // connection

            // loop through each connection, commit it, and release it
            while ( iter.hasNext() )
            {
                connection = (IConnectionImpl)iter.next();
                rollbackTransaction( connection );

                connections.add( connection );
            }

            // safe to now do so
            releaseAllConnections( connections );
        }
    }    

    /**
     * Returns the associated connection...cache the connection for an
     * execution cycle in order to safely commit or rollback
     * <p>
     * @param       name
     * @return      IConnectionImpl
     */
    public IConnectionImpl getConnection( String name )
    {
        IConnectionImpl connection = null;

		if ( currentConnections == null )	
			currentConnections = new HashMap();
			
        // are we in the middle of an execution cycle

        if ( isExecuting() )
        {
            // is the connection already cached
            connection = (IConnectionImpl)currentConnections.get( name );

            // if not, create it, start a transaction in it, and cache it
            if ( connection == null )
            {
                // create it
                try
                {
                    connection = ConnectionPoolManagerFactory.getObject().getConnection( name );

                    if ( connection != null )
                    {
                        // cache it
                        currentConnections.put( name, connection );

                        // start a transaction
                        connection.startTransaction();
                    }
                }
                catch ( Exception e )
                {
                }
            }
        }

        return( connection );
    }

    /**
     * returns the associated connections
     * @return      Map
     */
    public Map getConnectionsInUse()
    {
        return( currentConnections );
    }

    /**
     * returns the designated time to start this task - a null implies now
     * @return          java.util.Calendar
     */
    public java.util.Calendar getStartTime()
    {
        return( startTime );
    }

    /**
     * assign the time
     * @param		startTime
     */
    public void setStartTime( java.util.Calendar st )
    {
        startTime = st;
    }

    /**
     * returns the time to wait before restarting the task - < 0 implies immediately 
     * @return          long
     */
    public long getSleepTimeInMillis()
    {
        return( sleepPeriod );
    }

    /**
     * assigns the time to wait before restarting the task - < 0 implies immediately
     * @param       sleepTime
     */
    public void setSleepTimeInMillis( long sleepTime )
    {
        sleepPeriod = sleepTime;        
    }

    /**
     * returns the current task status
     * @return      TaskStatus
     */
    public TaskStatus getTaskStatus()
    {
        return( taskStatus );
    }

    /**
     * assign a task status
     * @param   status
     */
    public void setTaskStatus( TaskStatus status )
    {
        taskStatus = status;
    }

// helper methods

	/**
	 * Releases the provided connections to the ConnectionPoolManager.
	 * @param		connections
	 */
    protected void releaseAllConnections( Collection connections )
    {
        Iterator iter 				= connections.iterator();
        IConnectionImpl connection 	= null;
        
        // loop throught to acquire each IConnectionImpl, and then release it back
        // to the ConnectionPoolManager
        while ( iter.hasNext() )
        {
            connection = (IConnectionImpl)iter.next();
            releaseConnection( connection );
        }

        // can now safely empty the corresponding Map
        this.currentConnections.clear();
    }


	/**
	 * Figures out whether to use the provided message or to call acquireMessage, giving
	 * the sub-class a chance to get the messagae to execute the commands over.
	 * <p> 
	 * @return		the message to execute over
	 */
    protected IFrameworkMessage determineMessageToUse()
    {
        // force the child class to acquire the message, since this
        // class doesn't implement the acquireMessage() method
        if ( bindMessage == null )
        {
            return( acquireMessage() );
        }
        else    // return the client bound message to work on
        {
            return( bindMessage );
        }
    }

	/**
	 * Helper used to commit a framework IConnectionImpl.
	 * @param 	connection
	 */
    protected void commitTransaction( IConnectionImpl connection )
    {
        try
        {
            connection.commit();
        }
        catch ( Exception exc )
        {
            logMessage( "Task:commitTransaction() - " + exc );
        }
    }

    /**
     * Rollback the provided ConnectionImpl.
     * @param	connection
     */
    protected void rollbackTransaction( IConnectionImpl connection )
    {
        try
        {
            connection.rollback();
        }
        catch ( Exception exc )
        {
            logMessage( "Task:rollbackTransaction() - " + exc );
        }
    }

    /**
     * Release the provided connection back to the ConnectionPool
     *
     * @param	connection
     */
    protected void releaseConnection( IConnectionImpl connection )
    {
    	try
    	{
        	ConnectionPoolManagerFactory.getObject().releaseConnection( connection );
    	}
    	catch( Throwable exc )
    	{
    		logErrorMessage( "FrameworkTask.releaseConnection(IConnectionImpl) - " +  exc );
    	}
    }

// attributes

    /**
     * connections for an execution cycle
     */
    protected Map currentConnections    = null;

    /**
     * logical grouping of commands for this tak
     */
    protected Collection commands      		= new ArrayList();

    /**
     * task start time - null implies whenever convenient for the TaskHandler
     */
    protected java.util.Calendar startTime        = null;

    /**
      * task start time - null implies whenever convenient for the TaskHandler
      */
    protected java.util.Calendar wentToSleepTime = null;

    /**
     * time between a task ending and restart - < 0 implies immediately
     */
    protected long sleepPeriod                    = 0;     

    /**
     * task status - initialized to idle
     */
    protected TaskStatus taskStatus       		= TaskStatus.Idle;     

    /**
     * bound message - associated with rather than acquired during execution
     */
    protected IFrameworkMessage bindMessage       = null;     

}

/*
 * Change Log:
 * $Log: FrameworkTask.java,v $
 */
