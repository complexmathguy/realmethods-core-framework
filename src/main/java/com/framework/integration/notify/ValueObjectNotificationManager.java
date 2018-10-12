/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.notify;

import com.framework.business.vo.IFrameworkValueObject;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.exception.ObjectCreationException;
import com.framework.common.exception.ValueObjectNotificationException;

import com.framework.common.misc.Utility;

import com.framework.common.namespace.FrameworkNameSpace;

import com.framework.integration.objectpool.ConnectionPoolManagerFactory;

import com.framework.integration.objectpool.jms.JMSImpl;

/**
 * This singleton handles notification aspects surrounding notification events
 * of a FrameworkValueObject.  Using the ValueObjectNotificationBehavior value
 * setting in the framework.xml determines how this singleton behaves.
 * <p> 
 * @author    realMethods, Inc.
 * @see		  com.framework.integration.notify.ValueObjectNotificationEvent
 * @see       com.framework.integration.dao.FrameworkDAO
 * @see		  com.framework.integration.cache.UserSessionObjectManager
 */
public class ValueObjectNotificationManager 
    extends FrameworkBaseObject implements IValueObjectNotificationManager
{
	/**
	 * default constructor
	 * @throws InstantiationException
	 */
    public ValueObjectNotificationManager()
    throws InstantiationException
    {
    	try
    	{
	        String notificationIndicator    = Utility.getFrameworkProperties().getProperty( FrameworkNameSpace.VALUE_OBJECT_NOTIFICATION, "FALSE" );
	    
	        if ( notificationIndicator.equalsIgnoreCase( "TRUE" ) )
	        {
	            notifyOn = true;                
	            logInfoMessage( "Value Object Notification is turned on." );
	        }
	        else
	        {
	            logInfoMessage( "Value Object Notification is turned off." );
	        }
    	}
    	catch( Throwable exc )
    	{
    		throw new InstantiationException( "ValueObjectNotificationManager() : " + exc );
    	}
    }

    
// action methods    

    /**
     * Determines how to use JMS in order to subscribe for
     * events related to the provided value object.  Provide a non-null
     * value for sMsgSelector to filter on the which IFrameworkValueObject's
     * you would like to be notified about.  See the JDK 1.3 specs for more
     * information on JMS and message selector syntax.
     * <p>
     * @param		listener		who to notify
     * @param       valueObject		object target to notify on - can be null
     * @param     	msgSelector		query like criteria to notify on
     * @exception	ValueObjectNotificationException
     */
    public void subscribeForValueObjectNotification(	javax.jms.MessageListener listener, 
                                                        IFrameworkValueObject valueObject,
                                                        String msgSelector )
	throws ValueObjectNotificationException                                                        
    {
        // only fall through if there is a listener assigned, 
        // a valueObject to work on, and notification is turned on
        if ( listener != null && /*valueObject != null &&*/ notifyOn )
        {
            JMSImpl jmsImpl = getNotificationJMSImpl( );
            
            if ( jmsImpl != null )
            {
                try
                {
                    jmsImpl.assignAsListener( listener, msgSelector );
                    
                    ConnectionPoolManagerFactory.getObject().releaseConnection( jmsImpl );
                }
                catch( Throwable exc )
                {
                    throw new ValueObjectNotificationException( "ValueObjectNotificationManager:subscribeForValueObjectNotification() - " + exc, exc );
                }
            }            
        }
    }
    
    /**
     * Notify any listeners of the ValueObjectNotificationEvent.
     * <p>
     * @param		notificationEvent
     * @exception	ValueObjectNotificationException
     */
    public void notifyValueObjectListeners( ValueObjectNotificationEvent notificationEvent )
    throws ValueObjectNotificationException
    {
        if ( notifyOn == true && notificationEvent != null )
        {
            JMSImpl jmsImpl = getNotificationJMSImpl( /*notificationEvent.getValueObject()*/ );
            
            logDebugMessage( "ValueObjectNotificationManager:notifyValueObjectListeners on " + jmsImpl );
            
            if ( jmsImpl != null )
            {
                // assign the correlation ID, in order to connect to
                // the correct listener                
                try
                {
                	String correlationID = notificationEvent.getValueObject().getFrameworkPrimaryKey().toString();
	                jmsImpl.sendObject( notificationEvent, correlationID );
                
    	            ConnectionPoolManagerFactory.getObject().releaseConnection( jmsImpl );
                }
                catch( Throwable exc )
                {
                	throw new ValueObjectNotificationException( "ValueObjectNotificationManager:notifyValueObjectListeners(ValueObjectNotificationEvent)" + exc, exc );
                }    	            
            }
        }
    }
    
// helper methods

   /**
    * Factory method.
    * <p>
    * @return       IValueObjectNotificationManager
    * @exception	ObjectCreationException
    */
    static synchronized public IValueObjectNotificationManager getValueObjectNotificationManager()
    throws ObjectCreationException
    { 
    	if ( self == null )
    	{
    		try
    		{
	    		self = new ValueObjectNotificationManager();
    		}
    		catch( Throwable exc )
    		{
    			throw new ObjectCreationException( "ValueObjectNotificationManager:getValueObjectNotificationManager() - " + exc, exc );
    		}
    	}
    	
    	return( self );
    }
    
    /**
     *Helper method used to determine the JMSTopicImpl to make use of.
     * <p>
     * @return      JMSImpl
     */
    private JMSImpl getNotificationJMSImpl()
    {
        JMSImpl jmsImpl  = null;
        
        try
        {
            jmsImpl = (JMSImpl)ConnectionPoolManagerFactory.getObject().getConnection( FrameworkNameSpace.FRAMEWORK_VALUE_OBJECT_NOTIFICATION_JMS );
        }
        catch( Throwable exc )
        {
            logErrorMessage( "ValueObjectNotificationManager:getNotificationJMSImpl() - sJMSConnectionName - " + exc );
        }
        
        if ( jmsImpl == null )
        {
            logWarnMessage( "ValueObjectNotificationManager - Notification is ON, but unable to locate the FrameworkValueObjectNotificationJMS" );
        }
        
        return( jmsImpl );
    }
    
// attributes

	static protected IValueObjectNotificationManager self = null;
    protected boolean notifyOn            				= false;
}

/*
 * Change Log:
 * $Log: ValueObjectNotificationManager.java,v $
 */
