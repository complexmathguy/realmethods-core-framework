/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.command;

//************************************
// Imports
//************************************
import com.framework.common.FrameworkBaseObject;

import com.framework.common.event.FrameworkEventType;

import com.framework.common.exception.CommandExecutionException;
import com.framework.common.exception.ConnectionAcquisitionException;

import com.framework.common.message.IFrameworkMessage;

import com.framework.common.mgr.FrameworkManagerFactory;

import com.framework.integration.hook.FrameworkHookManager;
import com.framework.integration.hook.ICommandExecutionFailureHook;
import com.framework.integration.hook.IPostCommandExecuteHook;
import com.framework.integration.hook.IPreCommandExecuteHook;

import com.framework.integration.objectpool.ConnectionPoolManagerFactory;

import com.framework.integration.objectpool.jms.JMSImpl;

import com.framework.integration.task.IFrameworkTask;

/**
 * Base class for all Command objects associated with the JMS Command/Task Architecture
 *
 * 
 * <p> 
 * @author    realMethods, Inc.
 */
public abstract class FrameworkCommand 
    extends FrameworkBaseObject implements IFrameworkCommand
{
//****************************************************
// Public Methods
//****************************************************

    /**
     * returns the associated IFrameworkMessage
     *
     * @return      IFrameworkMessage
     */
    public IFrameworkMessage getMessage()
    {
        return( message );
    }


    /**
     * returns the bound IFrameworkTask
     */
    public IFrameworkTask getBoundTask()
    {
        return( boundTask );
    }

    /**
     * execution method
     * <p>
     * @param     	msg			external data context to work within
     * @param     	task  		can optionally be null
     * @exception	CommandExecutionException		sub-class command fails
     * @exception	IllegalArgumentException		thrown if msg is null
     */
    public void execute( IFrameworkMessage msg, IFrameworkTask task )
    throws CommandExecutionException, IllegalArgumentException
    {
        if ( msg == null )
            throw new IllegalArgumentException( "Command:execute() - null msg argument." );

        try
        {
            // preExecution method...overload to do something
            preExecute( msg, task );
                
            // give the sub-class a chance to do something
            onExecute( msg, task );
                
            // post execution method...overload to do something
            postExecute( msg, task );
        }
        catch ( Throwable e )
        {
        	try
        	{
        		// call hook to notify of command execution failure, 
        		// but re-throw the original ailure
        		commandExecutionFailure( msg, task );
        	}
        	catch( Throwable failExc )
        	{
        	}        	
        	
        	// still need to propagate the original excception
            throw new CommandExecutionException( "FrameworkCommand:execute() - " + e, e );
        	
        }
    }

// notification methods

    /**
     * by default, notify the rest of the system of the PreCommandExecute event.  Next,
     * attempt to delegate to a IPreCommandExecuteHook registered with the FrameworkHookManager
     *
     * @param     	msg			external data context to work within
     * @param     	task  		can optionally be null
     * @exception   CommandExecutionException
     */
    protected void preExecute( IFrameworkMessage msg, IFrameworkTask task )
    throws CommandExecutionException
    {        
        // attempt to delegate to a hook
        IPreCommandExecuteHook hook = null;
        
        try
        {
            // gain access to the appropriate JMSImpl and send a PreCommandExecute event
            JMSImpl jmsImpl = (JMSImpl)FrameworkManagerFactory.getObject().getFrameworkEventJMSImpl();
            
            if ( jmsImpl != null )
            {
	            jmsImpl.sendObject( FrameworkEventType.PreCommandExecute );
    	        ConnectionPoolManagerFactory.getObject().releaseConnection( jmsImpl );
            }
                
            hook = (IPreCommandExecuteHook)FrameworkHookManager.getHookManager().getHook( FrameworkEventType.PreCommandExecute.getEventType() );
        }
        catch( ConnectionAcquisitionException caExc )
        {
        	// not enough to terminate...
			logWarnMessage( "FrameworkCommand:preExecute() - " + caExc );        	
        }
        catch( Throwable exc )
        {
        	throw new CommandExecutionException( "FrameworkCommand:preExecute() - " + exc, exc );
        }            
        
        if ( hook != null && hook.canHandle( this, msg ) == true ) 
        {
        	try
        	{
	            hook.preCommandExecute( msg, task );
        	}
        	catch( Throwable exc )
        	{
        		throw new CommandExecutionException( "FrameworkCommand:preExecute() - " + exc, exc );
        	}
        }
        else  
        {
            // no op
        }
    }
    
    /**
     * by default, notify the rest of the system of the PostCommandExecute event.  Next,
     * attempt to delegate to a IPostCommandExecuteHook registered with the FrameworkHookManager
     * <p>
     * @param     	msg			external data context to work within
     * @param     	task  		can optionally be null
     * @exception   CommandExecutionException
     */    
    protected void postExecute( IFrameworkMessage msg, IFrameworkTask task )
    throws CommandExecutionException
    {

        // attempt to delegate to a hook
        IPostCommandExecuteHook hook = null;
        
        try
        {
            // gain access to the appropriate JMSImpl and send a PreCommandExecute event
            JMSImpl jmsImpl = (JMSImpl)FrameworkManagerFactory.getObject().getFrameworkEventJMSImpl();
            
            if ( jmsImpl != null )
            {
	            jmsImpl.sendObject( FrameworkEventType.PostCommandExecute );
    	        ConnectionPoolManagerFactory.getObject().releaseConnection( jmsImpl );
            }
            
            hook = (IPostCommandExecuteHook)FrameworkHookManager.getHookManager().getHook( FrameworkEventType.PostCommandExecute.getEventType() );
        }        
        catch( ConnectionAcquisitionException caExc )
        {
        	// not enough to terminate
        	logWarnMessage( "FrameworkCommand:postExecute() - " + caExc );
        }
        catch( Throwable exc )
        {
        	throw new CommandExecutionException( "FrameworkCommand:postExecute() - command execution failure" + exc, exc );
        }
		
		try
		{            
	        if ( hook != null && hook.canHandle( this, msg ) == true ) 
	        {
	            hook.postCommandExecute( msg, task );
	        }
	        else  
	        {
	            // no op
	        }
		}
		catch( Throwable exc )
		{
			throw new CommandExecutionException( "FrameworkCommand:postExecute() - " + exc, exc );
		}
    }

    /**
     * By default, notify the rest of the system of the CommandExecutionFailure event.  Next,
     * attempt to delegate to a ICommandExecutionFailureHook registered with the FrameworkHookManager
     * <p>
     * @param     	msg			external data context to work within
     * @param     	task  		can optionally be null
     * @exception   CommandExecutionException
     */    
    protected void commandExecutionFailure( IFrameworkMessage msg, IFrameworkTask task )
    throws CommandExecutionException
    {

        // attempt to delegate to a hook
        ICommandExecutionFailureHook hook = null;
        
        try
        {
            // gain access to the appropriate JMSImpl and send a PreCommandExecute event
            JMSImpl jmsImpl = (JMSImpl)FrameworkManagerFactory.getObject().getFrameworkEventJMSImpl();
            
            if ( jmsImpl != null )
            {
	            jmsImpl.sendObject( FrameworkEventType.CommandExecutionFailure );
    	        ConnectionPoolManagerFactory.getObject().releaseConnection( jmsImpl );
            }
            
            hook = (ICommandExecutionFailureHook)FrameworkHookManager.getHookManager().getHook( FrameworkEventType.CommandExecutionFailure.getEventType() );
        }
        catch( ConnectionAcquisitionException caExc )
        {
			logWarnMessage( "FrameworkCommand:commandExecutionFailure() - " + caExc );        	
        }        
        catch( Throwable exc )
        {
			throw new CommandExecutionException( "FrameworkCommand:commandExecutionFailure() - " + exc, exc );        	
        }
         
		try
		{         
			if ( hook != null ) 
	        {
    	        hook.commandExecutionFailure( msg, task );
        	}
		}
		catch( Throwable exc )
		{
			throw new CommandExecutionException( "FrameworkCommand:commandExecutionFailure() - " + exc, exc );        				
		}
    }
    
// must be implemented in the subclass

    /**
     * Notification abstract method during execution
     * <p>
     * @param     	msg			external data context to work within
     * @param     	task  		can optionally be null
     * @exception   CommandExecutionException     
     */
    abstract public void onExecute( IFrameworkMessage msg, IFrameworkTask task )
    throws CommandExecutionException;

// helper methods
    
// attributes

    /**
     * associated message
     */
    protected IFrameworkMessage message   = null;

    /**
     * associated Task that this Command is doing work on behalf of
     */
    protected IFrameworkTask boundTask	= null;     

}

/*
 * Change Log:
 * $Log: FrameworkCommand.java,v $
 */
