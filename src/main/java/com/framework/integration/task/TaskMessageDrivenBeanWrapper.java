/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.task;

import java.util.HashMap;

import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;

import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.exception.ObjectCreationException;

import com.framework.common.message.TaskJMSMessage;

/**
 * Wrapper class that gives message bean behavior to 
 * a Task.  This does not replace the functionality provided by
 * using the Framework pre-defined FrameworkTaskExecutionJMS as defined within
 * the connectionpool.properties.  Both provide the same result of invoking a 
 * Task.  One techinque may be more beneficial than the other, depending upon
 * the situation.
 * <p>
 * @author    realMethods, Inc.
 */
public class TaskMessageDrivenBeanWrapper extends FrameworkBaseObject
    implements MessageDrivenBean, MessageListener
{
    
// MessageDrivenBean implementation

   /**
    * Required but not used by the EJB Specification,
    */
    public void ejbCreate() 
    {
    }

    
   /**
    * Required but not used by the EJB Specification,
    */
    public void ejbActivate() 
    {
    }

   /**
    * Required but not used by the EJB Specification,
    */
    public void ejbRemove() 
    {
    }

   /**
    * Required but not used by the EJB Specification,
    */
    public void ejbPassivate() 
    {
    }

    /**
    * Sets the message drive context.
    * @param 	ctx             
    */
    public void setMessageDrivenContext( MessageDrivenContext ctx ) 
    {
        messageDrivenContext = ctx;
    }

// MessageListener implementation

    /**
     * Method called by JMS to handle the reception of a Message
     * <p>
     * @param	message
     */
    public void onMessage( javax.jms.Message message) 
    {
        logDebugMessage( "TaskMessageDriveBeanWrapper:onMessage() called with " + message );
        
        // Step 1 - retrieve the list of all TaskJMSExecutionHandlers known by
        HashMap handlers = null;
        
        try
        {
            handlers = TaskJMSExecutionRegistry.getInstance().getTaskJMSExecutionHandlers();
        }
        catch( ObjectCreationException insExc )
        {
            logErrorMessage( "TaskMessageDriveBeanWrapper:onMessage() - " + insExc );
        }
        
        // Step 2 - based on the message, get the TaskJMSExecutionHandler from the Map        
        if ( message != null && message instanceof ObjectMessage )
        {                        
            // the ObjectMessage should contain the TaskJMSMessage of interest
            TaskJMSMessage taskJMSMessage               = null;
            TaskJMSExecutionHandler taskJMSExecHandler  = null;
            
            // get the TaskJMSMessage buried within the ObjectMessage
            try
            {
                // should get a casting execption if the buried object is not
                // an instanceof TaskJMSMessage
                taskJMSMessage = (TaskJMSMessage)((ObjectMessage)message).getObject();                
                
                taskJMSExecHandler = (TaskJMSExecutionHandler)handlers.get( taskJMSMessage.getJMSString() );
                
                if ( taskJMSExecHandler != null )
                {
                    // have the handler determine the ITask to create...
                    IFrameworkTask task = taskJMSExecHandler.getFrameworkTask();
                    
                    if ( task != null )
                    {
                        task.execute( taskJMSMessage , taskJMSMessage.handleAsTransaction() );
                    }
                    else
                    {
                        logWarnMessage( "TaskMessageDriveBeanWrapper:onMessage() - unable to dynamically create the Task and/or Command associated with " + taskJMSMessage.getJMSString() + ". Please check the Task format within the task.properties file." );
                    }
                }
                else
                {
                    logWarnMessage( "TaskMessageDriveBeanWrapper:onMessage() - no named Task registered for " + taskJMSMessage.getJMSString() );
                }                
            }
            catch( Exception excTaskMsg )
            {
                logErrorMessage( "TaskMessageDriveBeanWrapper:onMessage() - " + excTaskMsg );
                
                return;
            }
        }                        
    }
    
// attributes

  protected MessageDrivenContext messageDrivenContext = null;

}

/*
 * Change Log:
 * $Log: TaskMessageDrivenBeanWrapper.java,v $
 */
    
