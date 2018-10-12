/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.objectpool.jms;

import java.util.Properties;

import javax.jms.*;

import com.framework.common.exception.ConnectionActionException;
import com.framework.common.exception.FrameworkJMSException;
import com.framework.common.exception.ObjectCreationException;

import com.framework.common.misc.Utility;

import com.framework.common.namespace.FrameworkNameSpace;

import com.framework.integration.locator.ServiceLocator;

 /**
 * A poolable helper class for making use of a JMSQueue connection
 * <p>
 * @author		realMethods, Inc.
 * @see			com.framework.integration.objectpool.jms.JMSTopicImpl
 */
public class JMSQueueImpl extends JMSImpl
{
//****************************************************
// Public Methods
//****************************************************

    public JMSQueueImpl()
    {
    }

/////////////////////////////////////////    
// JMSImpl overloads
/////////////////////////////////////////    
    
     /**
     * Closes the QueueSender and QueueSessions.
     * @exception		ConnectionActionException
     */
    public void disconnect()
    throws ConnectionActionException
    {
    	try
    	{
	    	if ( queueSender != null )
		        queueSender.close();
	    
	    	if ( queueSessionSender != null )
	    	{    
				if ( queueSessionSender.getTransacted() == true )	    		
		        	queueSessionSender.commit();
		        	
		        queueSessionSender.close();
	    	}
	    	
	    	if ( queueSessionReceiver != null )        
	    	{
	    		if ( queueSessionReceiver.getTransacted() == true )
			        queueSessionReceiver.commit();        
			        
		        queueSessionReceiver.close();
	    	}
	    	
	    	if ( queueConnection != null )
		        queueConnection.close();
    	}
    	catch( Throwable exc )
    	{
    		throw new ConnectionActionException( "JMSQueueImpl:disconnect() - " + exc, exc );
    	}        
    	finally
    	{
	        super.disconnect();
    	}
    }
    
    /**
     * Called by the pool manager to notify the connection it is 
     * being released.
     * <p>
     * @exception	ConnectionActionException
     */
    public void connectionBeingReleased()
    throws ConnectionActionException
    {
        commit();        
    }
    
    /**
     * Client notification to commit the current transaction
     * <p>
     * @exception	ConnectionActionException
     */
    public void commit()
    throws ConnectionActionException
    {
    	try
    	{
	    	if ( queueSessionSender != null )
	    	{    
	    		try
	    		{
	    			if ( queueSessionSender.getTransacted() == true )
			        	queueSessionSender.commit();
    			}
		    	catch( JMSException exc )
    			{
    				throw new ConnectionActionException( "JMSQueueImpl:rollback() - failed to commit the sender QueueSession -  " + exc, exc );
		    	}		        
	    	}
	    	
	    	if ( queueSessionReceiver != null )        
	    	{      
	    		try
	    		{
	    			if ( queueSessionReceiver.getTransacted() == true )	    			
			        	queueSessionReceiver.commit();
    			}
		    	catch( JMSException exc )
    			{
    				throw new ConnectionActionException( "JMSQueueImpl:commit() - failed to commit the receiver QueueSession -  " + exc, exc );
		    	}		        	
		        	
	    	}
    	}
    	catch( Throwable exc )
    	{
    		throw new ConnectionActionException( "JMSQueueImpl:commit() - " + exc, exc );
    	}
    }

    /**
     * client notification to rollback the current transaction
     */
    public void rollback()
    throws ConnectionActionException
    {
		if ( queueSessionSender != null )
    	{    
    		try
    		{
				if ( queueSessionSender.getTransacted() == true )    			
		        	queueSessionSender.rollback();
			}
	    	catch( JMSException exc )
			{
				throw new ConnectionActionException( "JMSQueueImpl:rollback() - failed to rollback the sender QueueSession -  " + exc, exc );
	    	}		        			        
    	}
    	
    	if ( queueSessionReceiver != null )        
    	{      
    		try
    		{
				if ( queueSessionReceiver.getTransacted() == true )    			
	        		queueSessionReceiver.rollback();
			}
	    	catch( JMSException exc )
			{
				throw new ConnectionActionException( "JMSQueueImpl:rollback() - failed to rollback the receiver QueueSession -  " + exc, exc );
	    	}		        	
    	}
	    	
    } 
           
    /**
     * Assigns a MessageListener interface as an interested party
     * in this JMS msg server
     * <p>
     * @param     	listener
     * @exception	FrameworkJMSException
     */
    public void assignAsListener( MessageListener listener )
    throws FrameworkJMSException
    {
        if ( listener != null && queueSessionReceiver != null )
        {
        	try
        	{
	            queueSessionReceiver.createReceiver( queue ).setMessageListener( listener );
        	}
        	catch( Throwable exc )
        	{
        		throw new FrameworkJMSException( "JMSQueueImpl:assignAsListener(MessageListener) - " + exc, exc );
        	}
        }
    }
     /**
     * Assigns a MessageListener interface as an interested party
     * in this JMS msg server
     * <p>
     * @param    	listener
     * @param      	msgselector
     * @exception	FrameworkJMSException
     */
    public void assignAsListener( MessageListener listener, String msgselector )
    throws FrameworkJMSException
    {
        if ( listener != null && queueSessionReceiver != null )
        {
        	try
        	{
    	        queueSessionReceiver.createReceiver( queue, msgselector ).setMessageListener( listener );
        	}
	       	catch( JMSException exc )
        	{
        		throw new FrameworkJMSException( "JMSQueueImpl:assignAsListener(MessageListener, String) - " + exc, exc );
        	}            
        }
        
    }
     /**
     * Send a message as a String to the encapsulated JMS queue
     * <p>
     * @param    	message
     * @exception	FrameworkJMSException
     */
    public void sendMessage( String message )
    throws FrameworkJMSException
    {
        try
        {
            if ( queueSessionSender != null )
            {
                TextMessage msg = queueSessionSender.createTextMessage();
                msg.setText( message );
                queueSender.send( msg );
            }
        }
        catch ( Throwable exc )
        {
            throw new FrameworkJMSException( "JMSQueueImpl:sendMessage(String) - " + exc, exc );
        }
    }    
     /**
     * Sends a serializable object to the encapsulated JMS queue.
     *
     * @param    	serializable
     * @exception	FrameworkJMSException
     */
    public void sendObject( java.io.Serializable serializable )
    throws FrameworkJMSException
    {
        sendObject( serializable, null );
    }
    
    /**
     *Send a serialized object, along with its correlation id, to the encapsulated JMS queue.
     *
     * @param      	serializable
     * @param     	correlationid
     * @exception	FrameworkJMSException
     */
    public void sendObject( java.io.Serializable serializable, String correlationid )
    throws FrameworkJMSException
    {
        try
        {
            if ( queueSessionSender != null )
            {
                ObjectMessage msg = queueSessionSender.createObjectMessage();
                
                msg.setObject( serializable );
                
                // conditionally assign the correlation id
                if ( correlationid != null )
                {
                    msg.setJMSCorrelationID( correlationid );
                }
                
                queueSender.send( msg );
            }
        }
        catch( Throwable exc )
        {
            throw new FrameworkJMSException( "JMSQueueImpl:sendObject(Serializable,String) - " + exc, exc );
        }
    }
    
    /**
     * After sending, immediately receives a javax.jms.Message, waiting for
     * the specified period of time.  O indicates to wait indefinitely. Provide
     * null for the correlation id, if not applicable. A null return Message means
     * nothing was received within the time period specified.
     * <p>
     * @param   	serializable
     * @param   	correlationid
     * @param   	timeout				how long to wait to rec'v a matching msg
     * @return  	javax.jms.Message
     * @exception	FrameworkJMSException
     */
    public javax.jms.Message sendObjectandReceive( java.io.Serializable serializable, 
    												String correlationid, 
    												long timeout )
	throws FrameworkJMSException    												
    {
        QueueReceiver queueReceiver = null;        
        javax.jms.Message message   = null;
        
        try
        {                	
            queueReceiver = queueSessionReceiver.createReceiver( queue, correlationid );

             // delegate internally
            sendObject( serializable, correlationid );
            
            // wait and receive...
            message = queueReceiver.receive( timeout );
            
        }
        catch( Throwable exc )
        {
            throw new FrameworkJMSException( "JMSQueueImpl:sendObjectandReceive(Serializable,String,long) - " + exc, exc );
        }
        
        return( message );
    }
    
// helper methods
    /**
     * Factory helper method for creation
     * <p>
     * @return	    JMSQueueImpl
     * @exception	ObjectCreationException 		
     */
    static public JMSQueueImpl create()
    throws ObjectCreationException
    {
    	try
    	{ 
    		return( new JMSQueueImpl() );
    	}
    	catch( Throwable exc )
    	{
			throw new ObjectCreationException( "JMSQueueImpl:create() - " + exc, exc );
    	}
    }
    
   /**
    * Create all the necessary objects for sending messages to a JMS queue.
    * <p>
    * @exception	FrameworkJMSException
    */
    protected void initJMS()
    throws FrameworkJMSException
    {
        ServiceLocator serviceLocator 		= ServiceLocator.getInstance();
    	
        try
        {
            String qName   	= getJMSName();
            
            queueConnectionFactory   = getJMSFactory();
            
            if ( queueConnectionFactory == null )
            	throw new FrameworkJMSException( "JMSQueueImpl:initJMS() -  getJMSFactory() returned a null value." );  
	            
            queueConnection   		= queueConnectionFactory.createQueueConnection();
            
            queueSessionSender 		= queueConnection.createQueueSession( false, Session.AUTO_ACKNOWLEDGE );
            queueSessionReceiver  	= queueConnection.createQueueSession( false, Session.AUTO_ACKNOWLEDGE );
            
            try
            {
                queue = (Queue) serviceLocator.lookup( qName );
            }
            catch ( Throwable ne )
            {
                logWarnMessage( "Unable to locate JMS Queue in JNDI- " + qName + ". Will try to create it" );
                queue = queueSessionSender.createQueue( qName );
                serviceLocator.bind( qName, queue );
                logInfoMessage( "Successfully bound JMS Queue - " + qName );                
            }
            
            queueSender = queueSessionSender.createSender( queue );
            queueConnection.start();
            
        }
        catch( Throwable exc )
        {
            throw new FrameworkJMSException( "JMSQueueImpl:initJMS() - " + exc, exc );   
        }
    }

 // helper methods
 
   /**
    * Looks in JNDI for the QueueConnectionFactory.  First uses the key provided in the connectionpool.properties
    * for this instance.  If unsuccessful, then uses  the value provided for property JMS_QUEUE_FACTORY
    * in framework.xml.
    * <p>
    * @return			QueueConnectionFactory
    * @exception		FrameworkJMSException
    */
    public QueueConnectionFactory getJMSFactory()
    throws FrameworkJMSException
    {
        
        ServiceLocator serviceLocator 		= ServiceLocator.getInstance();
        QueueConnectionFactory qFactory     = null;
        String factoryName					= null;
        
        try
        {
            factoryName = getFactoryName();
            
            if ( factoryName != null )
            {
                //logInfoMessage( "Looking in JNDI for " + getFactoryName() );
                
                // try using the factoryname provided with the connection
                qFactory = (QueueConnectionFactory)serviceLocator.lookup( getFactoryName() );
            }
        	
        }
        catch( Throwable exc )
        {
        	// try looking for the factory in JNDI using the 
        	// JMS_QUEUE_FACTORY property from framework.xml
        	logErrorMessage( "JMSQueueTopicImpl:getJMSFactory() - Failed to locate queue factory " + factoryName + ".Be sure the app. server has created factory " + factoryName + ". Now trying to use the default QueueFactory specified in framework.xml." );            		
        	
	        if ( jmsFactory == null )
	        {
	            Properties props = Utility.getFrameworkProperties();
	            
	            jmsFactory = props.getProperty( FrameworkNameSpace.JMS_QUEUE_FACTORY, "ConnectionFactory" );
	//            logInfoMessage( "jmsFactory is : " + jmsFactory );            
	        }
            
            try
            {
	            qFactory = (QueueConnectionFactory)serviceLocator.lookup( jmsFactory );            	
            }
            catch( Throwable ne )
            {
	            throw new FrameworkJMSException( "JMSQueueImpl:getJMSFactory() - " + ne, ne );            	
            }
        }
        
        
        return( qFactory);
    }
     
// attributes
 
    /**
     * JMS queue connection factory - need this to create queue connection(channel)
     */
    protected QueueConnectionFactory queueConnectionFactory   	= null;
     /**
     * JMS queue connection - need this to create queue session
     */
    protected QueueConnection queueConnection         	= null;
     /**
     * JMS queue session - need this to create Queue sender
     */
    protected QueueSession queueSessionSender          	= null;
     /**
     * JMS queue session - need this to create Queue receiver
     */
    protected QueueSession queueSessionReceiver       	= null;
     /**
     * JMS queue sender
     */
    protected QueueSender queueSender                 	= null;
     /**
     * JMS queue name
     */
    protected Queue queue                       	= null;
}

/*
 * Change Log:
 * $Log: JMSQueueImpl.java,v $
 */
