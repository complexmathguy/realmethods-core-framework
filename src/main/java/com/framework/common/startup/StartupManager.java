/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
*************************************************************************/
package com.framework.common.startup;

import java.beans.Beans;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.exception.FrameworkStartupException;
import com.framework.common.exception.ObjectCreationException;

import com.framework.common.namespace.FrameworkNameSpace;

import com.framework.common.properties.IFrameworkPropertiesHandler;
import com.framework.common.properties.PropertyFileLoader;

import com.framework.license.JCECypher;

/**
 * Startup manager responsible for getting the framework started.  This includes
 * property file loading, and starting all entries in the startup section of the framework.xml
 * file.
 * <p> 
 * @author    realMethods, Inc.
 */
public class StartupManager extends FrameworkBaseObject
{
	protected StartupManager()
	{	
		try
		{			
			System.out.println( bar );
			System.out.println( "--- realMethods Framework version " + FrameworkNameSpace.version + " " + FrameworkNameSpace.versionSuffix + " is now initializing..." );
            
			String licenseFileName = FrameworkNameSpace.FRAMEWORK_LICENSEFILE_NAME;
			
			boolean bIsValid =  JCECypher.twist( licenseFileName );
			
			if ( bIsValid == true )
			{
				System.out.println( "-- Valid Framework license discovered" );
	            
				if ( JCECypher.getLicensee() != null )
					System.out.println( "-- License Granted To: " + JCECypher.getLicensee()  );    		
				
				JCECypher.outputSuccess();

			}
			else
			{
				terminateFramework( "** Unable to validate Framework license using license file name " + licenseFileName + ".  Shutting down the Framework.");        	
			}
		}
		catch( Exception exc )
		{
			terminateFramework( "** Unable to validate Framework license. " + exc ); 
		}		
	}
	
	
	/**
	 * factory method used to create a IConnectionPoolManager
	 *
	 * @return    	StartupManager
	 * @exception	ObjectCreationException
	 */
	static public StartupManager getInstance()
	{
		// lazy initialization
		if ( instance == null )
		{
			instance = new StartupManager();
		}
        
		return( instance );
	}	
	/**
	  * The entry point to getting the framework started.
	  * <p>
	  * @param   applicationName		application name
	  * @param   propertiesLocation		file system based location of the property files
	  */
	 public void start( String applicationName, String propertiesLocation )
	 {
	 	// if already started, or in the middle of starting, return
		if ( hasBeenStarted == true || starting == true )
			return;
			
		 try
		 {        
			 if ( propertiesLocation != null )
			 {
				 FrameworkNameSpace.FRAMEWORK_PROPERTIES_LOCATION = propertiesLocation;
				 System.out.println( "--- Provided properties location is " + FrameworkNameSpace.FRAMEWORK_PROPERTIES_LOCATION + "...");
			 }
			 else    // assign to null so the classpath can be used
			 {            	            
				 FrameworkNameSpace.FRAMEWORK_PROPERTIES_LOCATION = null;
			 }
		 }
		 catch( ExceptionInInitializerError error )
		 {
		     terminateFramework( "*** Unable to load property files for " + applicationName + " from " + FrameworkNameSpace.FRAMEWORK_PROPERTIES_LOCATION + "." );
		 }
		        
		 // assign the global input property streams
		 try
		 {
		 	if (PropertyFileLoader.getInstance().hasLoadedPropertyfiles() == false )
		     	PropertyFileLoader.getInstance().loadPropertyFiles();
        
			 start( applicationName );
		 }
		 catch( Throwable exc )
		 {            
			StringBuffer buffer = new StringBuffer( "***************************\nFatal Framework Startup Error" );
			buffer.append( exc );            
			buffer.append( "\n***************************" );
			exc.printStackTrace();		 	
		    terminateFramework( buffer.toString() );		 	
		 }

        
	 }
    
	 /**
	  * The entry point to getting the Framework started.  Called by FrameworkBaseServlet
	  * and FrameworkStrutsActionServlet.  If not using one of these two classes,
	  * use the overloaded version start( String applicationName, String propertiesLocation );
	  * <p>
	  * @param      applicationName		application name
	  */    
	 public void start( String applicationName )
	 {        
		 if ( applicationName != null )
			 FrameworkNameSpace.INSTANCE_NAME = applicationName;

		 // call to get the ball rolling
		 start();
	 } 
     
	 /**
	  * Stops the Framework by calling stop() on all loaded IFrameworkStartup classes
	  * specified in the framework.xml file.
	  */     
	 public void stop()
	 {
	 	if ( hasBeenStarted == true && stopping == false )
	 	{
	 		stopping = true;
	 		
			System.out.println( "--- Stopping realMethods Framework  ---");
	
		 	// iterate through each IFrameworkStartup, calling stop()
			Iterator keys 				= startups.keySet().iterator();	 	
			String name 				= null;
			IFrameworkStartup startup 	= null;
			 	
			while( keys.hasNext() )
			{
				name = (String)keys.next();
				startup = (IFrameworkStartup)startups.get( name );
				System.out.println( "----------------" );
				System.out.println( name + " stopping..." );
				startup.stop();
				startups.put( name, startup );
				System.out.println( name + " stopped" );
				System.out.println( "----------------" ); 						 
	 		}
				
			startups.clear();	
			hasBeenStarted = false;
			
			PropertyFileLoader.getInstance().unload();
									
			System.out.println( "--- realMethods Framework stopped ---");
	 	}	 				
	}
	     
	/**
	 * Shuts down the Framework for the provided reason
	 *
	 * @param	terminateReason		reason for termination     
	 */
	public void terminateFramework( String terminateReason )
	{
		hasBeenStarted = false;		
		System.out.println( "The Framework is shutting down due to the following reason:\n" + terminateReason );
		RuntimeException t = new RuntimeException( "The Framework is shutting down due to the following reason:\n" + terminateReason );
		t.printStackTrace();
		throw t;
		//System.exit( -1 );
	}
		     
	 /**
	  * the real start method
	  */
	 protected void start()
	 {
         // lazy initialization
		 if ( hasBeenStarted == false && starting == false )
		 {
		 	 starting = true;

			 ///////////////////////////////////////////////////////////
			 // the property handler initialization is the most 
			 // important because if it fails, the rest of the system
			 // will not be able to get properties
			 ///////////////////////////////////////////////////////////
			IFrameworkPropertiesHandler propsHandler  = null;
			
			 try
			 {
			 	 System.out.println( bar );
				 System.out.println( "Properties Handler initializing..." );

				 // auto-load the FrameworkPropertiesHandler via its factory
				 propsHandler = PropertyFileLoader.getInstance().getFrameworkPropertiesHandler();
                
				 if ( propsHandler != null )
				 {
					 // For the JMX server, assign the MBEAN_DOMAIN as the ApplicationID
					 // assign the MBEAN_DOMAIN as the name of this servlet
					 FrameworkNameSpace.MBEAN_DOMAIN = propsHandler.getParam( FrameworkNameSpace.APPLICATION_ID_TAG, "rMFramework" );
				 }
				                 
				 System.out.println( "-- Properties Handler ready" );
				 System.out.println( bar );
			 }
			 catch( Throwable exc )
			 {
				 terminateFramework( "Unable to initialize the Framework. " + exc  );
			 }
            
			 // iterate through the startup implementations and call startup on each
			 Map classes = null;
			 
			 try
			 {
				 if ( propsHandler != null )
					 classes = propsHandler.getStartups();
			 }
			 catch( Throwable exc )
			 {
				terminateFramework( "Unable to complete Framework startup due to failure in accessing the list of startup properties from framework.propoerties." + exc  );
			 }
			 		 
			 if ( startups != null && classes != null )
			 {
			 	Iterator keys 				= classes.keySet().iterator();
			 	String name 				= null;
			 	String startupClassName 	= null;
			 	IFrameworkStartup startup 	= null;
			 	
			 	while( keys.hasNext() )
			 	{
					name = (String)keys.next();
					startupClassName = (String)classes.get( name );
					// try to instantiate the class...
					try
					{
						// create the security manager class using the classname property
						startup = (IFrameworkStartup)Beans.instantiate(this.getClass().getClassLoader(), startupClassName );
	 
						try
						{
							System.out.println( bar );							
							System.out.println( "** " + name + " starting..." );
							startup.start();
							startups.put( name, startup );
							System.out.println( "** " + name + " ready" );							
						}
						catch( FrameworkStartupException exc )
						{
							System.out.println( "*** Failed to start " + name + "  which may effect the behavior of the framework."  + exc  );
						} 	 		
						finally						
						{		
							System.out.println( bar );
						}
					}            
					catch( Throwable exc )                
					{
						logErrorMessage( "StartupManager::start() - failed to instantiate class " + startupClassName + " : " + exc );			        	
					}								 	    		
			 	}
            
			 	System.out.println( bar );
				System.out.println( "**** realMethods Framework ready ****" );
				System.out.println( bar );
			 }
			 else
			 {
				terminateFramework( "No \'startups\' element located in framework.xml file." );
			 }			            
			 			                                                                                                                                 
			 hasBeenStarted = true;            
		 }
	 }

	 
	 /**
	  * Main method to help get with testing the startup mechanism
	  * @param args		String[]
	  */
     public static void main(String args[])
     {
    	 
    	 StartupManager.getInstance().start( null /*get app. id from framework.xml*/, 
    			null /*use -D property for prop. file location*/ );
    	 
    	 // test log service
    	 try
    	 {
    		 com.framework.integration.log.FrameworkDefaultLogger.debug( "StartupManager is testing the ESB connectivity to " + FrameworkNameSpace.framework_log_service );
    	 }
    	 catch( Throwable exc )
    	 {
    		 System.out.println( "Failed to log a simple message.  Perhaps the ESB Mule startup service failed...");
    	 }
    	 
    	 // test security service
    	 try
    	 {
 //   		 com.framework.integration.security.FrameworkSecurityDelegateToHelper securityHelper = new com.framework.integration.security.FrameworkSecurityDelegateToHelper();    	 
 //   		 securityHelper.authenticateUser("srandolph", "69cutlass");
    	 }
    	 catch( Throwable exc )
    	 {
    		 System.out.println( "Failed to authenticate user...");
    	 }
     }	 
     
// attributes

	/**
	 * singleton instance
	 */
	static private StartupManager instance 		= null;
	
	/**
	 * mapping of name/IFrameworkStartup pairings
	 */	
	private HashMap startups 					= new HashMap();
	
	/**
	 * true/false indicator for the Framework having been started
	 */
	private boolean hasBeenStarted 				= false;
	
	/**
	 * true/false indicator so the Framework is only stopped once
	 */
	private boolean stopping					= false;
	
	/**
	 * true/false indicator that the Framewok is in the midst of start up
	 */
	private boolean starting					= false;
	
	private final String bar = "================="; 
	
}

/*
 * Change Log:
 * $Log:  $
 */
