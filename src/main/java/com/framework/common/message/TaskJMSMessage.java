/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.message;

/**
 * Used as the message/data carrier within the JMS Command/Task Architecture.  A client
 * utilizes this object in order to notify a Task to execute using the provided data.
 * <p>
 * @author    realMethods, Inc.
 * @see		  com.framework.integration.task.TaskJMSExecutionHandler
 */
public class TaskJMSMessage
    extends FrameworkMessage 
	implements java.io.Serializable
{

// constructors

    /**
     * default constructor
     */
    public TaskJMSMessage()
    {}
    
    /**
     * constructor
     *
     * @param 		msg   jms strig
     * @exception   IllegalArgumentException
     */ 
    public TaskJMSMessage( String msg )
    throws IllegalArgumentException
    {
        if ( msg == null || msg.length() == 0 )
            throw new IllegalArgumentException( "TaskJMSMessage.TestJMSMessage() - invalid Task JMS message string provided" );
            
        jmsString = msg;
    }
    
    /**
     * constructor
     *
     * @param 		msg			jms string
     * @param       arg			task argument, stored in the base class as the message		
     * @exception   IllegalArgumentException
     */ 
    public TaskJMSMessage( String msg, Object arg )
    throws IllegalArgumentException
    {
        super( arg );
        
        if ( msg == null || msg.length() == 0 )
            throw new IllegalArgumentException( "TaskJMSMessage.TestJMSMessage() - invalid Task JMS message string provided" );
            
        jmsString = msg;
    }

    /**
     * constructor
     *
     * @param 		msg			jms string
     * @param       arg			task argument, stored in the base class as the message
     * @param       handleAsTransaction		true/false indicator for handling task trasaction commit/rollbak
     * @exception   IllegalArgumentException
     */ 
    public TaskJMSMessage( String msg, Object arg, boolean handleAsTransaction )
    throws IllegalArgumentException
    {
        super( arg );
        
        if ( msg == null || msg.length() == 0 )
            throw new IllegalArgumentException( "TaskJMSMessage.TestJMSMessage() - invalid Task JMS message string provided" );
            
        jmsString = msg;
        
        this.handleAsTransaction = handleAsTransaction;
    }
    
// overloaded from Message

    /**
     * Returns the associated JMS message in String form
     * 
     * @return      String
     */
    public String getJMSString()
    {
        return( jmsString );
    }

    /**
     * Returns the associated message in String form
     * 
     * @return      String
     */
    public String getMessageAsString()
    {
        return( getJMSString() );
    }

    /**
     * Returns the transaction handler indicator
     * 
     * @return      boolean
     */    
    public boolean handleAsTransaction()
    { return( handleAsTransaction ); }
    
// attributes

    /**
     * The JMS related string
     */
    protected String    jmsString            = null;
    
    /**
     * boolean indicator to handle transaction
     */
    protected boolean   handleAsTransaction  = true;
    
}

/*
 * Change Log:
 * $Log: TaskJMSMessage.java,v $
 */




