/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.esb;

import java.beans.Beans;
import java.util.*;


import com.framework.common.exception.FrameworkStartupException;
import com.framework.common.exception.InitializationException;
import com.framework.common.exception.ObjectCreationException;

import com.framework.common.properties.PropertyFileLoader;
import com.framework.common.startup.FrameworkStartup;
import com.framework.common.properties.*;

/**
 * Factory singleton pattern implementation for a ESBManager.
 * <p>
 * @author		realMethods, Inc.
 */
public class ESBManagerFactory 
	extends FrameworkStartup
{
    /** 
     * constructor - public for dynamic  creation by the  StartupManager 
     */ 
    public ESBManagerFactory()
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
			loadESBManagers();
		}
		catch( Throwable exc )
		{
			throw new FrameworkStartupException( "ESBManagerFactory.start() - " + exc, exc );    
		}
	
	}

	/**
	 * stop invocation
	 * 
	 * @exception	FrameworkStartupException
	 */
	public void stop()
	{	
		esbManagers.clear();
	}
	
	/**
	 * factory method
	 * @return ESBManagerFactory
	 * @throws ObjectCreationException
	 */
	static synchronized public ESBManagerFactory getInstance()
	throws ObjectCreationException
	{
		if  ( instance == null )
		{
			try
			{			
				instance = new ESBManagerFactory();
				instance.start();
			}
			catch( Throwable exc )
			{
				throw new ObjectCreationException( "ESBManagerFactory.getInstance() - " + exc );
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
    public IESBManager getESBManager( String name )
    {
    	IESBManager esbManager = null;
    	
    	if ( esbManagers != null )
    		esbManager = esbManagers.get( name );
    	
        return( esbManager );
    }
    
    /**
     * Returns the map of IESBManager interfaces
     *
     * @return      Map
     */
    public Map getESBManagers()
    { return( esbManagers ); }


// helper methods

	/**
	 * loads the esb managers...but for now simply instantiates the Mule
	 */
	protected void loadESBManagers()
	throws InitializationException
	{
		// already loaded
		if ( esbManagers != null )
			return;		 

		// safe to now auto-load the dynamically accessible Log Handler            			  			         		
		esbManagers = new HashMap<String, IESBManager>();
		
		try
		{
			
            String name = null;
            String esbMgrClassName = null;
            Map esbMgrData = null;
            IESBManager esbManager = null;

            IESBPropertiesHandler propsHandler = PropertyFileLoader.getInstance().getESBPropertiesHandler();
//			System.out.println( "\n\n** propsHandler = " + propsHandler.getESBManagers() + "\n\n" );
			Map esbMgrs = propsHandler.getESBManagers();
			Iterator mgrKeys = esbMgrs.keySet().iterator();
			
//			System.out.println( "\n\n** esbmgrs = " + esbMgrs.keySet() + "\n\n" );

			while( mgrKeys.hasNext() )
		 	{
				name = (String)mgrKeys.next();			
				esbMgrData = (Map)esbMgrs.get( name );
				esbMgrClassName = (String)esbMgrData.get( "className" ); 
			
				esbManager = (IESBManager)java.beans.Beans.instantiate(this.getClass().getClassLoader(), esbMgrClassName );
				esbManagers.put( name, esbManager );
			}
		}
		catch( Throwable exc )
		{		
			throw new InitializationException( "ESBManagerFactory.loadESBManagers() - " + exc, exc );
		}
	}
	
	
// attributes

	/**
	 * cache of IFrameworkLogHandlers
	 */
    private Map<String, IESBManager> esbManagers = null;
    /**
     * self pointer
     */
    private static ESBManagerFactory instance = null;

}

/*
 * Change Log:
 * $Log: ESBManagerFactory.java,v $
 */
