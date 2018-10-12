/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.objectpool.jms;

import java.util.Properties;

import javax.jms.*;

import javax.naming.NamingException;

import com.framework.common.exception.ConnectionActionException;
import com.framework.common.exception.FrameworkJMSException;
import com.framework.common.exception.ObjectCreationException;

import com.framework.common.misc.Utility;

import com.framework.common.namespace.FrameworkNameSpace;

import com.framework.integration.locator.EJBServiceLocator;

/**
 * A poolable helper class for making use of JMSTopic connection
 * <p>
 * @author		realMethods, Inc.
 * @see			com.framework.integration.objectpool.jms.JMSQueueImpl
 */
public class JMSTopicImpl extends JMSImpl
{
//****************************************************
// Public Methods
//****************************************************

    public JMSTopicImpl()
    {
    }

    /**
     * Closes the underlying Topic connection, publisher, and session
     * @exception		ConnectionActionException
     */
    public void disconnect()
    throws ConnectionActionException
    {
    	try
    	{
	        if ( topicPublisher != null )
	        {
	            topicPublisher.close();
	        }
	        
	        if ( topicSubsriber != null )
	        {
	            topicSubsriber.close();
	        }
	
			if ( topicSession != null)
			{
				if ( topicSession.getTransacted() == true )
					topicSession.commit();
					
		        topicSession.close();	
			}
			
			if ( topicConnection  != null )
			{
		        topicConnection.close();
			}
    	}
    	catch( Throwable exc )
    	{
    		throw new ConnectionActionException( "JMSTopicImpl:disconnect() - " + exc, exc );
    	}
    	finally
    	{        
        	super.disconnect();
    	}
                
    }
    
    /**
     * Called by the Connection pool manager to notify the connection it is 
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
     * client notification to commit the current transaction.
     * @exception	ConnectionActionException
     */
    public void commit()
    throws ConnectionActionException
    {
    	if ( topicSession != null )
    	{
    		try
    		{
    			if ( topicSession.getTransacted() ==true )
		        	topicSession.commit();
    		}
    		catch( JMSException exc )
    		{
    			throw new ConnectionActionException( "JMSTopicImpl.commit() -  failed to commit the TopicSession - " + exc, exc );
    		}
    	}
    }

    /**
     * client notification to rollback the current transaction.
     * @exception	ConnectionActionException
     */
    public void rollback()
    throws ConnectionActionException
    {
    	if ( topicSession != null ) 
    	{   	
    		try
    		{
				if ( topicSession.getTransacted() ==true )    			
	        		topicSession.rollback();
    		}
    		catch( JMSException exc )
    		{
    			throw new ConnectionActionException( "JMSTopicImpl.rollback() -  failed to commit the TopicSession - " + exc, exc );
    		}
    	}
    }
    
    /**
     * assigns a MessageListener interface as an interested party
     * in this JMS msg server
     * <p>
     * @param		listener
     * @exception   FrameworkJMSException
     */
    public void assignAsListener( MessageListener listener )
    throws FrameworkJMSException
    {
        if ( listener != null && topicSession != null )
        {
        	try
        	{
	            topicSubsriber = topicSession.createSubscriber( topic );	        
	            topicSubsriber.setMessageListener( listener );
        	}
        	catch( Throwable exc )
        	{
        		throw new FrameworkJMSException( "JMSTopicImpl:assignAsListener(MessageListener) - " + exc, exc);
        	}
        }
    }


    /**
     * Assigns a MessageListener interface as an interested party
     * in this JMS msg server
     * <p>
     * @param		listener
     * @param       msgselector				filter to listen by
     * @exception   FrameworkJMSException
     */
    public void assignAsListener( MessageListener listener, String msgselector )
    throws FrameworkJMSException
    {
        if ( listener != null && topicSession != null )
        {
        	try
        	{
            	topicSubsriber = topicSession.createSubscriber( topic, msgselector, false /* don't send on same session */ );            
            	topicSubsriber.setMessageListener( listener );
        	}
        	catch( Throwable exc )
        	{
        		throw new FrameworkJMSException( "JMSTopicImpl:assignAsListener(MessageListener,String) - " + exc, exc);
        	}            
        }
        
    }
    
    
    /**
     * Sends the message to the encapsulated JMS topic.
     * <p>
     * @param 		msg   					text to send
     * @exception	FrameworkJMSException
     */
    public void sendMessage( String msg )
    throws FrameworkJMSException
    {
        try
        {
            if ( topicSession != null )
            {
                TextMessage textmessage = topicSession.createTextMessage();

                textmessage.setText( msg );
                topicPublisher.publish( textmessage );
            }
       	}
       	catch( Throwable exc )
       	{
       		throw new FrameworkJMSException( "JMSTopicImpl:assignAsListener( MessageListener) - " + exc, exc);
       	}
    }    

    /**
     * Sends a serializable object to the encapsulated JMS topic.
     * <p>
     * @param       serializable
     * @exception	FrameworkJMSException
     */
    public void sendObject( java.io.Serializable serializable )
    throws FrameworkJMSException
    {
        sendObject( serializable, null );
    }
    
    /**
     * Send a serialized object, along with its correlation id, to the encapsulated JMS topic. 
     * <p>
     * @param       serializable    
     * @param		correlationid
     * @exception	FrameworkJMSException
     */
    public void sendObject( java.io.Serializable serializable, String correlationid )
    throws FrameworkJMSException
    {
        try
        {
            if ( topicSession != null )
            {
                ObjectMessage msg = topicSession.createObjectMessage();
                
                msg.setObject( serializable );
                
                // conditionally assign the correlation id
                if ( correlationid != null )
                {
                    msg.setJMSCorrelationID( correlationid );
                }                                            
                
                topicPublisher.publish( msg );
            }
        }
        catch( Throwable exc )
        {
            throw new FrameworkJMSException( "JMSTopicImpl:sendObject(Serializable,String) - " + exc, exc );
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
     * @param   	timeout				how long to wait, in milliseconds, to receive the  msg
     * @return  	javax.jms.Message
     * @exception	FrameworkJMSException
     */
    public javax.jms.Message sendObjectandReceive( java.io.Serializable serializable, 
    												String correlationid, 
    												long timeout )
    throws FrameworkJMSException
    {
        TopicSubscriber topicSubscriber = null;
        javax.jms.Message message       = null;
        
        try
        {        
            topicSubscriber = topicSession.createSubscriber( topic, correlationid, false /* don't send on same session */ );

            // delegate internally
            sendObject( serializable, correlationid );
            
            // wait and receive...
            message = topicSubscriber.receive( timeout );
            
        }
        catch( Throwable exc )
        {
            throw new FrameworkJMSException( "JMSTopicImpl:sendObjectandReceive(Serializable,String,long) - " + exc, exc );
        }
        
        return( message );
    }
    
// helper methods

    /**
     * factory helper method for creation
     * <p>
     * @return	    JMSTopicImpl
     * @exception	ObjectCreationException 		
     */
    static public JMSTopicImpl create()
    throws ObjectCreationException
    {
    	try
    	{ 
    		return( new JMSTopicImpl() );
    	}
    	catch( Throwable exc )
    	{
			throw new ObjectCreationException( "JMSTopicImpl:create() - " + exc, exc );
    	}
    }

    /**
    * Create all the necessary objects for sending
    * messages to a JMS topic.
    * <p>
    * @exception		FrameworkJMSException
    */
    protected void initJMS()
    throws FrameworkJMSException
    {
        EJBServiceLocator serviceLocator = EJBServiceLocator.getEJBServiceLocator();
    	
        try
        {
            String qName = getJMSName();
            
            topicConnectionFactory  = getJMSFactory();
            
            if ( topicConnectionFactory == null )
            	throw new FrameworkJMSException( "JMSTopicImpl:initJMS() -  getJMSFactory() returned a null value." ); 
            	
            topicConnection   		= topicConnectionFactory.createTopicConnection();
            topicSession      		= topicConnection.createTopicSession( false, Session.AUTO_ACKNOWLEDGE );
    
            try
            {
                topic = (Topic)serviceLocator.lookup( qName );
            }
            catch ( NamingException ne )
            {
                logWarnMessage( "Unable to locate JMS Topic in JNDI- " + qName + ". Will try to create it" );
                topic = topicSession.createTopic( qName );
                serviceLocator.bind( qName, topic );
                logInfoMessage( "Successfully bound JMS Topic - " + qName );                
            }

            topicPublisher = topicSession.createPublisher( topic );
            topicConnection.start();
        }
        catch( Throwable exc )
        {
            throw new FrameworkJMSException( "JMSTopicImpl:initJMS() - " + exc, exc );   
        }
    }

// helper methods

	/**
	 * Looks in JNDI for the TopicConnectionFactory.  First uses the key provided in the connectionpool.properties
	 * for this instance.  If unsuccessful, then uses  the value provided for property JMS_TOPIC_FACTORY
	 * in framework.xml.
	 * <p>
	 * @return			TopicConnectionFactory
	 * @exception		FrameworkJMSException
	 */
    public TopicConnectionFactory getJMSFactory()
    throws FrameworkJMSException
    {
        
        EJBServiceLocator serviceLocator 		= EJBServiceLocator.getEJBServiceLocator();
        TopicConnectionFactory tcFactory    = null;
        String factoryName					= null;
        
        try
        {
            factoryName = getFactoryName();
            
            if ( factoryName != null )
            {
                // try using the factoryname provided with the connection
                tcFactory = (TopicConnectionFactory)serviceLocator.lookup( factoryName );
            }
        	
        }
        catch( Throwable exc )
        {
        	// try looking for the factory in JNDI using the 
        	// JMS_TOPIC_FACTORY property from framework.xml
        	logErrorMessage( "JMSTopicImpl:getJMSFactory() - Failed to locate topic factory " + factoryName + ".Be sure the app. server has created factory " + factoryName + ". Now trying to use the default TopicFactory specified in framework.xml." );            		
                
	        if ( jmsFactory == null )
	        {
	            Properties props = Utility.getFrameworkProperties();
	            
	            jmsFactory = props.getProperty( FrameworkNameSpace.JMS_TOPIC_FACTORY, "ConnectionFactory" );
	        }
            
            try
            {
	            tcFactory = (TopicConnectionFactory)serviceLocator.lookup( jmsFactory );            	
            }
            catch( Throwable nExc )
            {
				throw new FrameworkJMSException( "JMSTopicImpl:getJMSFactory() - " + nExc, nExc );
            }
        }        
        
        return( tcFactory);
    }
    
// attributes


    /**
     * JMS topic connection factory - need this to create topic connection(channel)
     */
    protected TopicConnectionFactory topicConnectionFactory      = null;

    /**
     * JMS topic connection - need this to create topic session
     */
    protected TopicConnection topicConnection             = null;

    /**
     * JMS topic session - need this to create Topic sender
     */
    protected TopicSession topicSession                   = null;

    /**
     * JMS topic publisher
     */
    protected TopicPublisher topicPublisher               = null;

    /**
     * JMS topic subscriber
     */
    protected TopicSubscriber topicSubsriber             = null;
    
    /**
     * JMS topic name
     */
    protected Topic topic                             = null;
    
}

/*
 * Change Log:
 * $Log: JMSTopicImpl.java,v $
 */
