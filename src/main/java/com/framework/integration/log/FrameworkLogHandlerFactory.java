/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.framework.common.exception.FrameworkStartupException;
import com.framework.common.exception.InitializationException;
import com.framework.common.exception.ObjectCreationException;

import com.framework.common.properties.PropertyFileLoader;

import com.framework.common.startup.FrameworkStartup;

/**
 * Factory singleton pattern implementation for a FrameworkLogHandler.
 * <p>
 * @author		realMethods, Inc.
 * @see			com.framework.integration.log.FrameworkLogHandler
 * @see			com.framework.integration.log.FrameworkDefaultLogger
 */
public class FrameworkLogHandlerFactory 
	extends FrameworkStartup
{
    /** 
     * constructor - public for dynamic  creation by the  StartupManager 
     */ 
    public FrameworkLogHandlerFactory()
    {
    	instance = this;
    }
   
	/**
	 * start invocation
	 * <p>
	 * @exception	FrameworkStartupException
	 */
	public void start()
	throws FrameworkStartupException
	{
		try
		{
			loadLogHandlers();
		}
		catch( Throwable exc )
		{
			throw new FrameworkStartupException( "FrameworkLogHandlerFactory.start() - " + exc, exc );    
		}
	
	}

	/**
	 * stop invocation
	 * 
	 * @exception	FrameworkStartupException
	 */
	public void stop()
	{	
		logHandlers.clear();
	}
	
	/**
	 * factory method
	 * @return FrameworkLogHandlerFactory
	 * @throws ObjectCreationException
	 */
	static synchronized public FrameworkLogHandlerFactory getInstance()
	throws ObjectCreationException
	{
		if  ( instance == null )
		{
			try
			{			
				instance = new FrameworkLogHandlerFactory();
				instance.start();
			}
			catch( Throwable exc )
			{
				throw new ObjectCreationException( "FrameworkLogHandlerFactory.getInstance() - " + exc );
			}
		}
		
		return( instance );
	}
	
    /**
     * Used to acquire a IFrameworkLogHandler, using the unique name assigned to it 
     * within log.proerties
     * <p>
     * @param       name					key to locate within the cache of IFrameworkLogHandlers
     * @return      IFrameworkLogHandler
     */
    public IFrameworkLogHandler getLogHandler( String name )
    {
        IFrameworkLogHandler logHandler = (IFrameworkLogHandler)logHandlers.get( name );
        
        return( logHandler );
    }
    
    /**
     * Returns the map of IFrameworkLogHandlers
     *
     * @return      Map
     */
    public Map getLogHandlers()
    { return( logHandlers ); }


// helper methods

	/**
	 * loads the log handlers from the log.properties file.
	 */
	protected void loadLogHandlers()
	throws InitializationException
	{
		// already loaded
		if ( logHandlers != null )
			return;		 
					
		try
		{
			// safe to now auto-load the dynamically accessible Log Handler            			  			         		
			logHandlers = new HashMap();
            
			Map tmpLogHandlers = null;
            
			try
			{   
				tmpLogHandlers = PropertyFileLoader.getInstance().getLogPropertiesHandler().getLogHandlers();
			}
			catch( Throwable exc )
			{
				throw new ObjectCreationException( "FrameworkLogHandlerFactory:getObject(String) " + exc, exc );                
			}
            
			if ( tmpLogHandlers != null )
			{
				Iterator keys    	= tmpLogHandlers.keySet().iterator();
				String key         	= null;
				Map props     		= null;
                
				while ( keys.hasNext() )
				{
					key = (String)keys.next();
                    
					props = (Map)tmpLogHandlers.get( key );    
                    
					logHandlers.put( key, new FrameworkLogHandler( key, props ) );
				}
			}
		}
		catch( Throwable exc )
		{		
			throw new InitializationException( "FrameworkLogHandlerFactory.loadLogHandlers() - " + exc, exc );
		}
	}
	
// attributes

	/**
	 * cache of IFrameworkLogHandlers
	 */
    private Map logHandlers = null;
    /**
     * self pointer
     */
    private static FrameworkLogHandlerFactory instance = null;
}

/*
 * Change Log:
 * $Log: FrameworkLogHandlerFactory.java,v $
 */
