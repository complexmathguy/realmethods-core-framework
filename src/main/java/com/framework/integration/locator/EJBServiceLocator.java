/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.locator;
 
import javax.naming.NamingException;

import javax.rmi.PortableRemoteObject;

import com.framework.common.namespace.FrameworkNameSpace;

import com.framework.common.properties.PropertyFileLoader;
import com.framework.common.FrameworkBaseObject;

/**
 * Sub-class of ServiceLocator, specializing in handling JNDI access to
 * EJB related services.
 * <p>
 * Delivered as a singleton, it can be used outside of a sub-class facade.
 * <p>
 * @author    realMethods, Inc.
 */
public class EJBServiceLocator extends ServiceLocator
{
	/**
	 * default constructor - deter external creation
	 */
	protected EJBServiceLocator()
	{
	}
	
// factory method

	/**
	 * Singleton factory method
	 * <p>
	 * @return			EJBServiceLocator
	 */
	static public EJBServiceLocator getEJBServiceLocator()
	{
		if ( singleton == null )
		{
			singleton 			= new EJBServiceLocator();			
			try
			{				
				jndiEJBNamePrefix 	= PropertyFileLoader.getInstance().getFrameworkPropertiesHandler().getParam( FrameworkNameSpace.JNDI_EJB_NAME_PREFIX, "");
				new FrameworkBaseObject().logInfoMessage( "Using JNDI EJB Name Prefix: " + jndiEJBNamePrefix );
			}
			catch( Throwable exc )
			{
				new FrameworkBaseObject().logErrorMessage( "EJBServiceLocator - failed to locate JNDI_EJB_NAME_PREFIX property in the framework.xml - " + exc );
				jndiEJBNamePrefix = "";
			} 	
		}
		
		return( singleton );
	}		
		
// overloaded  methods

	/**
	 * JNDI lookup method, using the provided object key String.  
	 * 
	 * @param		jndiKey		key value of object to find in JNDI
	 * @throws		NamingException
	 */
	public Object lookup( String jndiKey )
	throws NamingException
	{		
        Object obj = null;
        
        try
		{
            StringBuffer tempBuffer = new StringBuffer( jndiEJBNamePrefix );
            tempBuffer.append( jndiKey );
        	
        	obj = super.lookup( tempBuffer.toString() );
		}
        catch( NamingException exc )
		{      
        	logDebugMessage( "EJBServiceLocator:lookup - failed to locate " + jndiKey + " using prefix " + jndiEJBNamePrefix + ".  Will try to prefix " + jndiKey + " with ejb/");
        	try
			{
        		obj = super.lookup( "ejb/" + jndiKey );
        		jndiKey = "ejb/";
        		putInCache( jndiKey, obj );
			}
            catch( NamingException exc1 )
			{
            	logDebugMessage( "EJBServiceLocator:lookup - failed to locate " + jndiKey + " using prefix ejb/" + ".  Will try to prefix " + jndiKey + " with local/");            	
            	try
				{            	
            		obj = super.lookup( "local/" + jndiKey );
            		jndiKey = "local/";
            		putInCache( jndiKey, obj );
				}
	            catch( NamingException exc2 )
				{
	            	logDebugMessage( "EJBServiceLocator:lookup - failed to locate " + jndiKey + " using prefix local/" + ".  Will try to prefix " + jndiKey + " with java:comp/env/");	            	
	        		obj = super.lookup( "java:comp/env/" + jndiKey );
	        		jndiKey = "java:comp/env/";
	        		putInCache( jndiKey, obj );
				}
        	}            
		}
        
		return(  obj );
	}
	
// accessor methods
	
	/**
	 * Locates and narrows the home interface
	 * 
	 * @param 	jdniServiceHome		name of service session bean home in jndi
	 * @param 	homeClass			class of the home interface
	 * @return 	Object
	 * @throws 	NamingException
	 */
	public Object getHomeInterface( String jdniServiceHome, Class homeClass )
	throws NamingException
	{ 
//		System.out.println(  "**\n\n " + jdniServiceHome + " narrowing to " + lookup( jdniServiceHome ).toString() );
		
        return( PortableRemoteObject.narrow( lookup( jdniServiceHome ), homeClass ) );
	}
				
// attributes
	
	
	/**
	 * singleton instance, purpose of the class
	 */	
	static protected EJBServiceLocator singleton	= null;
	
	/**
	 * JNDI EJB Prefix as specified in the framework.xml
	 */
	static protected String jndiEJBNamePrefix		= "";	
	
}

/*
 * Change Log:
 * $Log: EJBServiceLocator.java,v $
 */
