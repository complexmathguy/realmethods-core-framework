/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.locator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.jms.JMSException;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.properties.PropertyFileLoader;

import com.framework.common.startup.StartupManager;

/**
 * Base class for application generated service locator classes.
 * <p>
 * Delivered as a singleton, it can be used outside of a sub-class facade. 
 * <p>
 * The initial context is obtained by appending the JNDI_ARGS property as found
 * in the framework.xml file in use.
 * <p>
 * Maintains a Map as a local cache, containing those items already looked up.
 * <p>
 * @author    realMethods, Inc.
 */
public class ServiceLocator extends FrameworkBaseObject
{
	/**
	 * default constructor - deter external creation
	 */
	protected ServiceLocator()
	{
		// attempt to start the framework, if this is the client entry point to the framework
		StartupManager.getInstance().start( null, null );
		
		localCache = Collections.synchronizedMap( new HashMap() ); 
		
        try
        {
			jndiArgs = new Hashtable();
        	jndiArgs.putAll( PropertyFileLoader.getInstance().getFrameworkPropertiesHandler().getJNDIArgs() );
        }
        catch( Throwable exc )
        {
        	logErrorMessage( "ServiceLocator - failed to locate JNDI_ARGS property in the framework.xml - " + exc );
        	jndiArgs = new Hashtable();
        } 		
	}
	
// factory method

	/**
	 * Singleton factory method
	 * 
	 * @return			ServiceLocator
	 */
	static public ServiceLocator getInstance()
	{
		if ( singleton == null )
		{
			singleton = new ServiceLocator();									
		}
		
		return( singleton );
	}
	
	
// action methods	

	/**
	 * JNDI lookup method, using the provided object key String.
	 * <p>
	 * Will first look to see if it is in the local cache, meaning it was
	 * already search for and located.
	 * <p>
	 * @param		jndiKey		key to the actual object to locate
	 * @throws		NamingException
	 */
	public Object lookup( String jndiKey )
	throws NamingException
	{	
		// is it already cached locally?
		Object object = localCache.get( jndiKey );
		
		if ( object == null )
		{
			object = getInitialContext().lookup( jndiKey );
			putInCache( jndiKey, object );
		}
    	return( object );
	}	

	/**
	 * Using the provided JNDI Key, binds the object within JNDI
	 * <p>
	 * @param		jndiKey		key to apply to objectToBind
	 * @param		objectToBind
	 * @throws		NamingException
	 */
	public void bind( String jndiKey, Object objectToBind )
	throws JMSException, NamingException
	{
    	getInitialContext().bind( jndiKey, objectToBind );
	}	
		
// accessor methods


	/**
	 * Creates a single instance of the InitialContext, using the framework.xml
	 * JNDI_ARG if provided.
	 * <p>
	 * @return		InitialContext
	 */
	public InitialContext getInitialContext()
	throws NamingException
	{
		if ( this.initialContext == null )
		{
			this.initialContext = new InitialContext( jndiArgs );			
		}
		
		return( this.initialContext );		
	}
	
	/**
	 * helper method to place the JNDI entity in the cache
	 * @param key		reference to entity in the cache
	 * @param object	what to put in the cache
	 */
	protected void putInCache( String key, Object object )
	{
		localCache.put( key, object );
	}
		
// attributes

   /**
	* singleton instance, purpose of the class
	*/	
   static protected ServiceLocator singleton	= null;
   
   /**
	* JNDI properties to apply when getting the initial context
	*/
   protected Hashtable jndiArgs	= null;	
   
	/**
	 * sole instance of the InitialContext
	 */
	protected InitialContext initialContext 	= null;	
		
	/**
	 * cache for already looked up objects
	 */
	protected Map localCache  = null;	
	
}


/*
 * Change Log:
 * $Log: ServiceLocator.java,v $
 */
