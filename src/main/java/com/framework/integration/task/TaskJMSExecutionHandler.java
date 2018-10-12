/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.task;

import java.util.Collection;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import com.framework.common.event.FrameworkLogEventType;

import com.framework.common.exception.ObjectCreationException;
import com.framework.common.exception.TaskExecutionException;

import com.framework.common.message.IFrameworkMessage;
import com.framework.common.message.TaskJMSMessage;

import com.framework.common.namespace.FrameworkNameSpace;

import com.framework.integration.command.FrameworkCommandFactory;

import com.framework.integration.objectpool.ConnectionPoolManagerFactory;

import com.framework.integration.objectpool.jms.JMSImpl;

/**
 * Helper class used by the TaskJMSExecutionRegistry in order to bind a Task and Message
 * on a JMSImpl connection.  A JMSImpl type of connection by the name of "FrameworkTaskExecutionJMS" 
 * must be defined in your connectionpool.properties file in order for this class to listen
 * and handle an appropriately named bound task.
 * <p>
 * @author    realMethods, Inc.
 * @see		  com.framework.common.message.TaskJMSMessage
 * @see		  com.framework.integration.task.FrameworkTask
 * @see		  com.framework.integration.task.TaskMessageDrivenBeanWrapper
 */
public class TaskJMSExecutionHandler 
    extends TaskJMSExecutionHandlerMBean  
    implements javax.jms.MessageListener
{
    
// constructors  

    /**
     * constructor
     *
     * @param       taskStatement
     * @param       executionMsg
     * @exception   InstantiationException
     */
    public TaskJMSExecutionHandler( String taskStatement, String executionMsg )
    throws InstantiationException
    {
        this( taskStatement, executionMsg, true );
    }
    
    /**
     * constructor - listen flag used to determine whether to listen on a 
     * predefined JMS Connection
     *
     * @param       statement	tokenized string from task.properties
     * @param 		msg   		text used to associated with the statement as Task
     * @param       listen		if false, could be that TaskMessageDrivenBeanWrapper is in use 
     * @exception   InstantiationException
     */
    public TaskJMSExecutionHandler( String statement, String msg, boolean listen )
    throws InstantiationException
    {    
        logDebugMessage( "Constructing a TaskJMSExecutionHandler() - task statement is " + statement + ", task msg is " + msg );
        
        // acquire the JMSImpl from the Connection Manager using the name of this
        // class as the name of the connection
        try
        {
            if ( listen == true )
            {
                jmsImpl = (JMSImpl)ConnectionPoolManagerFactory.getObject().getConnection( FrameworkNameSpace.TASK_EXECUTION_QUEUE );
                
                if ( jmsImpl != null )
                {
                    // the centralized caller and the handler each know that the 
                    // JMSCorrelationID will be used to assign the Name of the Task
                    
                    StringBuffer taskJMSMessageSelector = new StringBuffer( "JMSCorrelationID = \'");
                    taskJMSMessageSelector.append( msg );
                    taskJMSMessageSelector.append( "\'" );
                    
                    try
                    {
                        // assign as a listener
                        jmsImpl.assignAsListener( this, taskJMSMessageSelector.toString() );
                    }
                    catch( JMSException exc )
                    {
                        throw new InstantiationException( "TaskJMSExecutionHandler - constructor() - failed to assignAsListener " + exc );
                    }
                    finally
                    {
                    	// release when done
	                    ConnectionPoolManagerFactory.getObject().releaseConnection( jmsImpl );
                    }
                }
                else
                {
                    logMessage( "TaskJMSExecutionHandler:constructor() - failed to get the JMSImpl named " + FrameworkNameSpace.TASK_EXECUTION_QUEUE, FrameworkLogEventType.WARNING_LOG_EVENT_TYPE );
                }
            }
            
        }
        catch( Exception exc )
        {
            logMessage( "TaskJMSExecutionHandler - constructor() - " + exc, FrameworkLogEventType.ERROR_LOG_EVENT_TYPE );
        }
        finally
        {
            // assign local variables
            this.taskStatement    = statement;
            this.executionMsg     = msg; 
            
            // OK to now JMX register
            handleSelfRegistration();           
        }
    }


// MessageListener implementations

    /**
     * Pass a message to the Listener.
     * @param	message
     */
    public void onMessage( javax.jms.Message message ) 
    {
        logDebugMessage( taskStatement + " : Inside of TaskJMSExecutionHandler:onMessage()" );
        
        if ( message != null && message instanceof ObjectMessage )
        {                        
            // the ObjectMessage should contain the TaskJMSMessage of interest
            TaskJMSMessage taskJMSMessage = null;
            
            // get the TaskJMSMessage buried within the ObjectMessage
            try
            {
            	Object obj = ((ObjectMessage)message).getObject();
            	
                // should get a casting execption if the buried object is not
                // an instanceof TaskJMSMessage
                if ( obj != null && obj instanceof com.framework.common.message.TaskJMSMessage )         
	                taskJMSMessage = (com.framework.common.message.TaskJMSMessage)obj;                
				else
				{
  	              	logErrorMessage( "TaskJMSExecutionHandler:onMessage() failed for task  " + getExecutionMsg() + " - getObject() of ObjectMessage returned type " + obj.getClass().getName() + " instead of TaskJMSMessage " );
    	            return;  					
				}				                
            }
            catch( Throwable exc )
            {
                logErrorMessage( "TaskJMSExecutionHandler:onMessage() failed for task  " + getExecutionMsg() + " -  "+ exc );                
                return;
            }
                   
            // pull the message as a String from the Message
            String messageKey = taskJMSMessage.getJMSString();
            
            // valid message and the one we are interested in
            if ( messageKey != null && messageKey.length() != 0 &&  messageKey.equalsIgnoreCase( executionMsg ) )
            {                
                IFrameworkTask task = null;
                
                try
                {
                	task = getFrameworkTask();
                }
                catch( ObjectCreationException exc )
                {
	                logErrorMessage( "TaskJMSExecutionHandler:onMessage() failed for task  " + getExecutionMsg() + " - "  +  exc );                	
                }
                
                if ( task == null )
                {
                    return;
                }   
                
                if ( task.getCommands() == null )
                {
	                logWarnMessage( "TaskJMSExecutionHandler:onMessage() - task " + getExecutionMsg() + " using message for msg " + messageKey + " does not have any FrameworkCommands associated with it, but proceeding." );
                }
                
                if ( task != null )
                {
                    // extract the argument com.framework.common.message.Message to pass
                    // on to the task
                    executeTask( task, taskJMSMessage );
                }
                
                // to expedite gc
                task = null;
            }   
            else
            {
                logWarnMessage( "TaskJMSExecutionHandler:onMessage() - task " + getExecutionMsg() + " : Not assigned to handle FrameworkTask execution for msg " + messageKey );                
            }
        }
        
    }
          

    /**
     * Parses an input string and produces the Task and potentially, the
     * associated Commands
     * <p>
     * @return		IFrameworkTask
     * @throws		ObjectCreationException	
     */
    public IFrameworkTask getFrameworkTask()
    throws ObjectCreationException
    {
        IFrameworkTask task = null;
        
        /*
            The taskClassName is either of the form:
                
            (A) Commands=com.acme.command.OpenAccountCommand;com.acme.command.ContactCustomerCommand;com.acme.command.LogCommand
                
            or
                
            (B) Task=com.poc.task.DeleteAccountTask
        */
        try
        {
            StringTokenizer tokenizer   = new StringTokenizer( taskStatement, "=" );
            String taskOrCommandKey    = null;
            String taskCommandClass    = null;
            boolean isAppDefinedTask   = false;
                
            if ( tokenizer.hasMoreTokens() )
            {
                taskOrCommandKey = tokenizer.nextToken();
                    
                if ( taskOrCommandKey.equalsIgnoreCase( "Task" ) )
                    isAppDefinedTask = true;    
                else
                    isAppDefinedTask = false;
                    
                // assign the class name
                taskCommandClass = tokenizer.nextToken();
                    
                if ( isAppDefinedTask )
                {
                    // load the Task from the TaskFactory
                    task = FrameworkTaskFactory.createInstance().getTaskAsObject( taskCommandClass );
                }
                else    // list of commands
                {
                    //Step #1:  define a default Framework task
                    task = FrameworkTaskFactory.createInstance().getTaskAsObject( "com.framework.integration.task.FrameworkTask" );
                        
                    //Step #2:  create each associate command and add to a Collection
                    Collection commands = new ArrayList();
                        
                    tokenizer = new StringTokenizer( taskCommandClass, ";" );
                        
                    while( tokenizer.hasMoreTokens() )
                    {
                        commands.add( FrameworkCommandFactory.createInstance().getCommandAsObject( tokenizer.nextToken() ) );
                    }

                    //Step #3:  finally, bind the commands to the created task                                
                    task.bindCommands( commands );
                }
            }
        }
        catch( Throwable  exc ) 
        { 
            throw new ObjectCreationException( "TaskJMSExecutionHandler:getFrameworkTask() failed to for " + getExecutionMsg() + "- " + exc );
        }
        
        return( task );
    }
    
    /**
     * Causes the execution of the provided task
     * <p>
     * @param   task
     * @param   message		data to apply to the task to execute over
     */
    protected void executeTask( IFrameworkTask task, IFrameworkMessage message )
    {
        if ( task != null )
        {
            try
            {
                logDebugMessage( this.taskStatement + " : About to call execute on the FrameworkTask" );
                task.execute( message, true /* task handles commit, rollback*/ );
                logDebugMessage( this.taskStatement + " : Successfully called execute on the FrameworkTask" );
            }
            catch( TaskExecutionException exc )
            {
                logErrorMessage( this.taskStatement + " : Failed during FrameworkTask execution - " + exc );
            }
        }
    }
 
// attributes

    /**
     * JMS connection from the Connection Pool
     */
    protected JMSImpl jmsImpl             = null;        
    
}

/*
 * Change Log:
 * $Log: TaskJMSExecutionHandler.java,v $
 */
