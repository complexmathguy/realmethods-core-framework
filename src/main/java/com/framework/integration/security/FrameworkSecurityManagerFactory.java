/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.security;

import java.beans.Beans;

import java.io.IOException;

import java.util.*;

import com.framework.common.exception.FrameworkStartupException;
import com.framework.common.exception.ObjectCreationException;

import com.framework.common.properties.PropertyFileLoader;

import com.framework.common.startup.FrameworkStartup;

/** 
 * Contains logic for loading and instantiating all IFrameworkSecurityManager abstractions
 * defined in the security.properties file in use.
 * <p>
 * @author    realMethods, Inc.
 */
public class FrameworkSecurityManagerFactory 
	extends FrameworkStartup
{

// constructors
	
	/**
	 * default constructor 
	 */	
	public FrameworkSecurityManagerFactory()
	throws ObjectCreationException
	{
		instance = this;
	}

//	IFrameworkStartup implementation

	/**
	 * Start up invocation.
	 * <p>
	 * @exception	FrameworkStartupException
	 */
	public void start()
	throws FrameworkStartupException
	{
	 	try
	 	{	 	
			loadSecurityManagers();
		}
		catch( Throwable exc )
		{
			throw new FrameworkStartupException( "FrameworkSecurityManagerFactory.start() - " + exc, exc );    
		}			    
	}
	
	/**
	 * stop invocation.
	 * @exception	FrameworkStartupException
	 */
	public void stop()
	{	
		synchronized(securityManagers)
		{
			// notify to logout all users
			IFrameworkSecurityManager secMgr = null;
			Iterator keys = securityManagers.keySet().iterator();
			
			while( keys.hasNext() )
			{
				secMgr = (IFrameworkSecurityManager)securityManagers.get( keys.next() );
				
				logInfoMessage( "Unauthenticating all users against sec. mgr. " + secMgr.getName() );
				secMgr.unAuthenticateUsers();
			}
		
			securityManagers.clear();
		}					
	}
	
// factory method

	/**
	 * factory method
	 * @return			FrameworkSecurityManagerFactory
	 */
   	static synchronized public FrameworkSecurityManagerFactory getInstance()
	throws ObjectCreationException
	{
		if ( instance == null )
		{
			instance = new FrameworkSecurityManagerFactory();
			try
			{			
				instance.start();
			}
			catch( FrameworkStartupException exc )
			{
				throw new ObjectCreationException( "FrameworkSecurityManagerFactory.getInstance() - failed to start the FrameworkSecurityManangerFactory - " + exc, exc);			
			}
		}	
		
		return( instance );			
	}
		
// helper method

	/**
	 * Using the contents of security.properties, attempmts to instantiate the
	 * related IFrameworkSecurityManagers.
	 */
	protected void loadSecurityManagers()
	throws ObjectCreationException
	
	{		
		securityManagers  = Collections.synchronizedMap( new HashMap() );				
        
        Map securityProps = null;
        
        try
        {
        	// get the security properties from  the security properties handler
        	securityProps = PropertyFileLoader.getInstance().getSecurityPropertiesHandler().getSecurityManagers();
        }
        catch( Throwable exc )
        {
        	throw new ObjectCreationException( "FrameworkSecurityManagerFactory:loadSecurityManagers() - failed to get the FrameworkPropertiesHandler from it factory  - " + exc,  exc );        	
        }   
        
        if ( securityProps == null )
        {
            logInfoMessage( "FrameworkSecurityManagerFactory has no Framework Security Managers to load." );
            return;
        }

        // iterate through the security property groupings to peek at the class name associated with the group
        Iterator keys					= securityProps.keySet().iterator();
        String name						= null;
        Map props						= null;
        IFrameworkSecurityManager mgr 	= null;
        
        while ( keys.hasNext() )
        {     	
            try
            {                
                // get the name of the security manager
                name = (String)keys.next();
                
                // acquire it's bindings from the framework properties handler
                props = (Map)securityProps.get( name );
                
				if ( props != null )    
				{           
			        try
			        {
	    	            // create the security manager class using the classname property
	 	            	mgr = (IFrameworkSecurityManager)Beans.instantiate(this.getClass().getClassLoader(), (String)props.get( SECURITY_MGR_CLASS_NAME_KEY ) );
	 
		                mgr.initialize( name, props );		
		                
		                System.out.println( "...Loaded and initialized Security Manager " + name );          
			        }
			        catch ( IOException exc )
			        {
			            throw new ObjectCreationException("FrameworkSecurityManagerFactory::loadSecurityManagers() - could not create the IFrameworkSecurityManager by the name of  " + name + " due to an IO Error - " + exc, exc);
			        }
			        catch ( ClassNotFoundException exc )
			        {
			            throw new ObjectCreationException("FrameworkSecurityManagerFactory::loadSecurityManagers() - could not find the class associated with security manager name " + name + " - " + exc, exc);   
			        }                
			        catch( Throwable exc )                
			        {
						throw new ObjectCreationException( "FrameworkSecurityManagerFactory::loadSecurityManagers() - " + exc, exc );			        	
			        }
	
					// attempt to assign the defaultSecurityManagerName
					if ( defaultSecurityManagerName == null && mgr.isDefaultSecurityManager() )
					{
						defaultSecurityManagerName = name;
					}
					
	                // cache it
	                securityManagers.put( name, mgr );
	                
				}    
				else
				{
					throw new ObjectCreationException( "FrameworkSecurityManagerFactory::loadSecurityManagers() - properties could not be loaded for manager " + name );					
				}            
            }
            catch ( Throwable exc )
            {
                throw new ObjectCreationException( "FrameworkSecurityManagerFactory:loadSecurityManagers() - " + exc );
            }        
        }
        
        // if  the defaultSecurityManagerName is still unassigned, apply the last in the Map
        if ( defaultSecurityManagerName == null && securityManagers.size() > 0 && name != null )
        {
        	defaultSecurityManagerName = name;
        	
        	// make it the default
        	((IFrameworkSecurityManager)securityManagers.get(name )).isDefaultSecurityManager(true);
        }
        
        if ( defaultSecurityManagerName != null )
        	System.out.println( "...Security Manager " + defaultSecurityManagerName + " is established as the default." );
		else
			System.out.println( "...No security managers discovered." );        	          
        
	}
		
// access methods

	/**
	 * Returns the named IFrameworkSecurityManager.  If null arg is provided, call getDefaultSecurityManager()
	 * <p>
	 * @param			name	unique name to search by as key
	 * @return			IFrameworkSecurityManager
	 */
	public IFrameworkSecurityManager getSecurityManager( String name )
	{
		IFrameworkSecurityManager mgr = (IFrameworkSecurityManager)securityManagers.get( name );
		
		if ( mgr == null && name == null )
		{
			mgr = getDefaultSecurityManager();
		}
		
		return( mgr );
	}
	
	/**
	 * Returns the designated default Security Manager.
	 * <p>
	 * @return		IFrameworkSecurityManager
	 */
	public IFrameworkSecurityManager getDefaultSecurityManager()
	{	
		return( securityManagers != null ?
				(IFrameworkSecurityManager)securityManagers.get( defaultSecurityManagerName ) :
					null );
	}
			

	/**
	 * Return the security managers
	 * <p>
	 * @return Map
	 */
	public Map getSecurityManagers()
	{
		return( securityManagers );
	}
		
// attributes

	/**
	 * Security Manager fully qualified class name
	 */
    private final String SECURITY_MGR_CLASS_NAME_KEY = "securityManagerClassName";        
    
    /**
     * available IFrameworkSecurityManager implementations
     */
    private Map securityManagers = null;
    
    /**
     * name of the default security manager
     */
    private String defaultSecurityManagerName = null;    
		
	/**
	 * static self instance
	 */		
	static private FrameworkSecurityManagerFactory instance = null;
	
	
}

/*
 * Change Log:
 * $Log: FrameworkSecurityManagerFactory.java,v $
 */
