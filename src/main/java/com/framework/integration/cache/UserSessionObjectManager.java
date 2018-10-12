/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.cache;

import java.util.*;

import javax.jms.Message;
import javax.jms.ObjectMessage;

import com.framework.business.pk.FrameworkPrimaryKey;

import com.framework.business.vo.IFrameworkValueObject;

import com.framework.business.vo.list.ValueObjectListProxy;

import com.framework.common.exception.ObjectCreationException;
import com.framework.common.exception.ValueObjectNotificationException;

import com.framework.integration.notify.IValueObjectNotificationManager;
import com.framework.integration.notify.ValueObjectNotificationEvent;
import com.framework.integration.notify.ValueObjectNotificationManager;

/**
 * This class provides a value object facade over the FrameworkCache. 
 * <p>
 * @author    realMethods, Inc.
 * @see		  com.framework.presentation.user.UserSession
 * @see		  com.framework.integration.notify.ValueObjectNotificationManager
 */
public class UserSessionObjectManager extends FrameworkCache
    implements IUserSessionObjectManager, javax.jms.MessageListener
{

//************************************************************************    
// Public Methods
//************************************************************************

    /**
     * default constructor
     */
    public UserSessionObjectManager()
    {         
    }

	/**
	 * Use this method to apply a IFrameworkValueObject to the cache using the provided key.
	 * <p>
	 * @param		primaryKey		key to associate valueObject with in the cache 
	 * @param       valueObject		data to bind in cache with the primarKey
	 * @exception	IllegalArgumentException
	 */
     public void assignValueObject( FrameworkPrimaryKey primaryKey, IFrameworkValueObject valueObject )
        throws IllegalArgumentException
     {
     	if ( primaryKey == null )
     		throw new IllegalArgumentException( "UserSessionObjectManager:assignValueObject() - FrameworkPrimaryKey cannot be null." );
     		
     	if ( valueObject == null )
     		throw new IllegalArgumentException( "UserSessionObjectManager:assignValueObject() - IFrameworkValueObject cannot be null." );

		// delegate to base class
		assign( primaryKey, valueObject );  
		
        
        //************************************************************************
        // Subscribe for notification on it, only if not dealing with an IListProxy.  
        //************************************************************************
        if ( !(valueObject instanceof ValueObjectListProxy)  )
        {
            String msgSelector = buildMsgSelector();            
            
            IValueObjectNotificationManager mgr = null;
            
            try
            {
            	mgr = ValueObjectNotificationManager.getValueObjectNotificationManager();
            }
            catch( ObjectCreationException exc )            	
            {
            	logErrorMessage( "UserSessionObjectManager.assignValueObject() - failed to get the ValueObjectNotificationManager - " + exc );
            }
            
            try
            {
            	mgr.subscribeForValueObjectNotification( this, valueObject, msgSelector );
            }
            catch( ValueObjectNotificationException exc2 )
            {
            	logErrorMessage( "UserSessionObjectManager.assignValueObject() - failed to subscribe for notification with the ValueObjectNotificationManager - " + exc2 );
            }
        }		
				   		
     }
     
	/**
	 * Use this method to apply a IFrameworkValueObject to the cache, using the value object's
	 * own FrameworkPrimaryKey as the key into the cache.
	 * <p>
	 * @param		valueObject
	 * @exception	IllegalArgumentException Thrown if any of the parameters are null.
	 */
     public void assignValueObject( IFrameworkValueObject valueObject )
     throws IllegalArgumentException
     {
     	if ( valueObject == null )
     		throw new IllegalArgumentException( "UserSessionObjectManager:assignValueObject() - IFrameworkValueObject cannot be null." );

        assignValueObject( valueObject.getFrameworkPrimaryKey(), valueObject );
     }
     
	/**
	 * Removes the associated IFrameworkValueObject
	 * @param   	key
	 */
    public void removeValueObject( FrameworkPrimaryKey key )
    {
    	// delegate to base class
    	remove( key );
    }
    
	/**
	  * Returns the associated IFrameworkValueObject based on the notion of equals on the provided
	  * FrameworkPrimaryKey.
	  * <p>
	  * Returns the value to which this map maps the specified key. Returns null if the map 
	  * contains no mapping for this key. A return value of null does not necessarily 
	  * indicate that the map contains no mapping for the key; 
	  * it's also possible that the map explicitly maps the key to null. .
	  * <p>
	  * @param      primaryKey
	  * @return     IFrameworkValueObject
	  */
     public IFrameworkValueObject getValueObject( FrameworkPrimaryKey primaryKey )
     {
     	IFrameworkValueObject valueObject = null;
     	
     	if ( primaryKey != null )
     	{
     		// delegate to base class     	
	     	valueObject = (IFrameworkValueObject)get( primaryKey );
     	}
	     	
		return( valueObject );	     	
     }
     
	/**
	 * Returns the associated IFrameworkValueObject based on the notion of equals on the provided
	 * String.
	 * <p>
	 * @param      key
	 * @return     IFrameworkValueObject
	 */
     public IFrameworkValueObject getValueObject( String key )
     {
        // Create a FrameworkPrimaryKey and delegate internally
        
        return( this.getValueObject( new FrameworkPrimaryKey( key ) ) );
     }
     
    /**
     * Returns true if the map contains a mapping for the specified key.
     * ,p.
     * @param 	key
     * @return 	boolean
     */
    public boolean containsKey( String key )
    {
        // create a FrameworkPrimaryKey and delegate internally
        return ( containsKey( new FrameworkPrimaryKey( key ) ) );
    }
    
	/**
	 * Returns true if the cache contains a mapping for the specified key.
	 * <p>
	 * @param 		key
	 * @return		boolean
	 */
    public boolean containsKey( FrameworkPrimaryKey key )
    {
    	// delegate to base class
    	return( contains( key ) );
    }
    
	/**
	 * Empty the cache of ValueObjectListProxies.  
	 */         
    public void emptyCacheOfListProxies()
    {
    	synchronized( getCache() )
    	{
			Collection cache 	= getCachedData();    		
    		Object data			= null;  
        	Iterator iter   	= cache.iterator();
	            
	        // loop through and check to see if it is an ValueObjectListProxy	        
	        while( iter.hasNext() )
	        {
	            data = iter.next();

	            if ( data instanceof com.framework.business.vo.list.ValueObjectListProxy )
	            {
	            	// delegate to base class
					removeData( data );
	            }
	        }             
    	}
    }
    
    
    /**
     * Returns the global IUserSessionObjectManager cache
     * @return IUserSessionObjectManager
     */
    static public IUserSessionObjectManager getGlobalCache()
	{
		return( globalCache );
	}    
	
// IGlobalValueObjectCache implementations    
    
   /**
	* Use this method to obtain a value object from the global cache
	* <p>
	* @param       primaryKey
	* @return      IFrameworkValueObject
	*/
    public IFrameworkValueObject getGlobalValueObject( FrameworkPrimaryKey primaryKey )
    {
        return( (IFrameworkValueObject)globalCache.getValueObject( primaryKey ) );
    }
    
	/**
	 * Use this method to globally cache a value object available.
	 * <p>
	 * @param		primaryKey		key to apply to global cache
	 * @param       valueObject		value to associate with the key
	 * @exception   IllegalArgumentException Thrown if any of the parameters are null.
	 */
	public void assignGlobalValueObject( FrameworkPrimaryKey primaryKey, IFrameworkValueObject valueObject )
    throws IllegalArgumentException
    {
    	// delegate also handles the exception case
        globalCache.assignValueObject( primaryKey, valueObject );                
    }
     
	/**
	 * Use this method to globally cache a value object available.
	 * The Key for the model is the DefaultKey of the base FrameworkValueObject 
	 * implemntation.
	 * <p>
	 * @param       valueObject
	 * @exception	IllegalArgumentException Thrown if value valueObject is null.
	 */
    public void assignGlobalValueObject( IFrameworkValueObject valueObject )
	throws IllegalArgumentException
    {
     	if ( valueObject == null )
     		throw new IllegalArgumentException( "UserSessionObjectManager:assignGlobalValueObject() - IFrameworkValueObject cannot be null." );
     		            
        // delegate internally        
        assignGlobalValueObject( valueObject.getFrameworkPrimaryKey(), valueObject );
    }        

	/**
	 * Removes the associated value object from the global cache.
	 * <p>
	 * @param		key
	 */
    public void removeGlobalValueObject( FrameworkPrimaryKey key )
    {
	    globalCache.removeValueObject( key );
    }

	/**
	 * Empty the global cache.  Use with caution since it has application level
	 * ramifications.
	 */     
    public void emptyGlobalCache()
    {
        globalCache.emptyCache();
    }
    
// MessageListener Implementations

    /**
     * Handle the provided message, sent from the JMS connection associated
     * with the Framework's ValueObjectNotification process
     * <p>
     * If turned on within the framework.xml, notification comes in the
     * form of events related to the creation, updating, and deletion of an 
     * IFrameworkValueObject that should be in this cache instance.
     * <p> 
     * @param	message
     */
    public void onMessage( Message message ) 
    {        
        if ( message != null && message instanceof ObjectMessage )
        {                        
            // the ObjectMessage should contain ValueObjectNotificationEvent
            ValueObjectNotificationEvent notificationEvent = null;
            
            // get the ValueObjectNotificationEvent buried within the ObjectMessage
            try
            {
                // should get a casting execption if the buried object is not
                // an instanceof TaskJMSMessage
                notificationEvent = (ValueObjectNotificationEvent)((ObjectMessage)message).getObject();
            }
            catch( Exception excTaskMsg )
            {
                logErrorMessage( "UserSessionObjectManager:onMessage() - Exception thrown while extracting ValueObjectNotificationEvent from the JMS Message" + excTaskMsg );                
                return;
            }

            IFrameworkValueObject theValueObject = notificationEvent.getValueObject();
            
            if ( theValueObject != null && containsKey( theValueObject.getFrameworkPrimaryKey() ) )
            {
                if ( notificationEvent.getNotificationType().isUpdate() )
                {                    
                    logInfoMessage( "UserSessionObjectManager:onMessage() - Updating Value Object - " + theValueObject.getFrameworkPrimaryKey() );

                    // replaces an old one with this one...
                    assignValueObject( theValueObject );
                }
                else if ( notificationEvent.getNotificationType().isDelete() )
                {
                    logInfoMessage( "UserSessionObjectManager:onMessage() - Deleting Value Object - " + theValueObject.getFrameworkPrimaryKey() );

                    // delete from the cache
                    removeValueObject( theValueObject.getFrameworkPrimaryKey() );
                }
            }
        }
    }

    
//************************************************************************    
// Private / Protected Methods
//************************************************************************
    
    /**
     * Helper method used to build the JMS message selection criteria in order to 
     * only be notified of events related to the IFrameworkValueObjects internally cached 
     * by this instance, intentionally ignoring a ValueObjectListProxy.
     * <p>
     * @return      msg selector
     */
     protected String buildMsgSelector()
     {  
		StringBuffer msgSelector	= new StringBuffer(); 
				     	
     	synchronized( getCache() )
     	{     	       
	        Iterator iter 	= getCachedData().iterator();
	        Object data		= null;	
	                    
	        // loop through each cached Value Object and add it's key to the selector
	        
	        while( iter.hasNext() )
	        {
	            data = iter.next();
	            
	            if ( data != null 
	            		&& (data instanceof IFrameworkValueObject) 
	            		&& !(data instanceof ValueObjectListProxy) )
	            {
	                msgSelector.append( "JMSCorrelationID = \'" );
	                msgSelector.append(  ((IFrameworkValueObject)data).getFrameworkPrimaryKey() );
	                msgSelector.append( "\'" );
	                    
	                if ( iter.hasNext() )
	                {
	                    msgSelector.append( " OR " );
	                }
	            }
	        }
     	}
     	        

        return( msgSelector.toString() );
     }


//************************************************************************    
// Attributes
//************************************************************************

    /**
     * static singleton representing the global cache 
     */
    private static IUserSessionObjectManager globalCache = new UserSessionObjectManager();       
}

/*
 * Change Log:
 * $Log: UserSessionObjectManager.java,v $
 */
