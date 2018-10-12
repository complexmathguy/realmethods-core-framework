package com.framework.common.properties;

import java.io.*;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.net.URL;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* hibernate 2.1
import net.sf.hibernate.cfg.*;
*/
import org.hibernate.cfg.*;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.exception.InitializationException;
import com.framework.common.exception.XMLFileParsingException;

import com.framework.common.namespace.FrameworkNameSpace;

/**
 * Handles the duty of getting the framework related configuration files loaded, by reading the
 * contents of the config.xml file, and the creating the specified propery handlers.
 * <p>
 * @author    realMethods, Inc.
 */
public class PropertyFileLoader extends FrameworkBaseObject
{
	
// constructors
	
	/**
	 * default constructor
	 */
	protected PropertyFileLoader()
	{
		propertyHandlers = Collections.synchronizedMap( new HashMap() );
	}
	
	/**
	 * Factory method
	 * <p>
	 * @return PropertyFileLoader
	 */
	static synchronized public PropertyFileLoader getInstance()
	{
		if ( instance == null )
		{
			// try to first get it from JNDI if already loaded
			instance = seeIfLoadedInJNDI();

			if ( instance == null  )
			{
				instance = new PropertyFileLoader();
				try
				{
					if ( !FrameworkNameSpace.GOOGLE_LICENSE_ENV )
						new InitialContext().bind( PROPERTYFILELOADER_KEY, instance );
				}
				catch( Throwable exc )
				{
					System.out.println ("PropertyFileLoader:failed to bind self instance to JNDI tree - " + exc );
				}
			}
		}	
		
		return instance;			
	}
	
	/**
	 * Delegates to the overloaded loadPropertyFiles( java.net.URL ).
	 * 
	 * <p>
	 * @exception	InitializationException		thrown if unable to parser config.xml
	 */
	public void loadPropertyFiles()
	throws InitializationException
	{			 				
		loadPropertyFiles(null);			
	} 
		
	/**
	 * Attempts to load configuration files, referenced by config.xml
	 * The provided URL represents the location of the config.xml.
	 * <p>
	 * @param		rootURL		root location of the configuration files, provided as a URL.  If
	 * 							NULL, the classpath is used and the -DFRAMEWORK_HOME property...
	 * @exception	InitializationException		thrown if unable to parser config.xml
	 */
	public void loadPropertyFiles( URL rootURL )	
	throws InitializationException
	{		
		if ( hasLoadedPropertyfiles() == false )
		{	
			this.rootURL = rootURL;
			
			try
			{
				parseConfigXMLFile();
			}
			catch( XMLFileParsingException exc )
			{
				throw new InitializationException( "PropertyFileLoader.loadPropertyFiles() - " + exc, exc );
			}
			
			// do this first so FrameworkBaseObject.logxxxMessage methods can be used...
			
			// loop through the configFileParamMappings	
			Iterator iter = configFileParamMappings.iterator();
			HashMap configParam = null;
			String name = null;
			String handler = null;
			String fileName = null;
			try
			{
				while( iter.hasNext() )
				{ 		
					configParam	= (HashMap)iter.next();
					name 		= (String)configParam.get( "name" );
					handler 	= (String)configParam.get( "handler" );
					fileName  	= (String)configParam.get( "file" );
					
					bindWithPropertyHandler( name, handler, loadProperties( fileName ) );																	
				}				
			}
			catch( Throwable exc )
			{
				logWarnMessage( "PropertyFileLoader.loadPropertyFiles() - " + exc ) ;				
			}
			
			if ( FrameworkNameSpace.USING_HIBERNATE )
				loadHibernateConfiguration();		
		}
		else	
			logWarnMessage( "PropertyFileLoader has already preloaded the standard property files." );
	}

	/**
	 * Indicator for property files having been preloaded.
	 * <p>
	 * @return	boolean
	 */
	public boolean hasLoadedPropertyfiles()
	{
		return( propertyHandlers.size() > 0 );
	}
	
	/**
	 * Closes all cached property files
	 */
	public void unload()
	{
		// closes all cached InputStreams...
		synchronized( propertyHandlers )
		{		
			Iterator keys 				= propertyHandlers.keySet().iterator();
			IPropertiesHandler handler	= null;
			String name					= null;
			
			while ( keys.hasNext() )
			{		
				name = (String)keys.next();	
				handler = (IPropertiesHandler)propertyHandlers.get(name);
						
				logInfoMessage( "Done with " + name + " property handler  --"  );
				
				handler.doneNotification();
			}
		}	
	}
			
// access methods

	/**
	 * Returns the framework property handler.
	 * @return framework property handler
	 */
	public IFrameworkPropertiesHandler getFrameworkPropertiesHandler()
	{
		return( (IFrameworkPropertiesHandler)propertyHandlers.get( "framework" ) );		
	}
	
		
	/**
	 * Returns the connection pool property handler.
	 * @return connection pool property handler
	 */
	public IConnectionPoolPropertiesHandler getConectionPoolPropertiesHandler()
	{
		return( (IConnectionPoolPropertiesHandler)propertyHandlers.get( "connectionpool" ) );		
	}
	
	/**
	 * Returns the security property handler.
	 * @return security property handler
	 */
	public ISecurityPropertiesHandler getSecurityPropertiesHandler()
	{
		return( (ISecurityPropertiesHandler)propertyHandlers.get( "security" ) );		
	}
	
		
	/**
	 * Returns the log property handler.
	 * @return log property handler
	 */
	public ILogPropertiesHandler getLogPropertiesHandler()
	{
		return( (ILogPropertiesHandler)propertyHandlers.get( "loghandlers" ) );		
	}

	/**
	 * Returns the task property handler.
	 * @return task property handler
	 */
	public ITaskPropertiesHandler getTaskPropertiesHandler()
	{
		return( (ITaskPropertiesHandler)propertyHandlers.get( "task" ) );		
	}
	

	/**
	 * Returns the esb property handler.
	 * @return esb handler
	 */
	public IESBPropertiesHandler getESBPropertiesHandler()
	{
		return( (IESBPropertiesHandler)propertyHandlers.get( "esb" ) );		
	}	
	/**
	 * Returns the loaded property handlers.
	 * @return	property handlers
	 */
	public Map getPropertyHandlers()
	{
		return( propertyHandlers );
	}
	
	/**
	 * Returns the Hibernate Configuration
	 * @return	Configuration
	 */
	public Configuration getHibernateConfiguration()
	{
		return( hibernateConfiguration );
	}
	
	public InputStream getJDOInputStream()
	{
		return( jdoInputStream );
	}
	
// helper methods
	
	/**
	 * Loads the JDO speedo.properties file.
	 * If the rootURL has been provided, it is used as the base of the search.
	 * If not found there, looks in the classpath.  If still not found
	 * looks in the directory specified by the -DFRAMEWORK_PROPERTY system variable.
	 */
	protected void loadJDOConfiguration()
	{
		try
		{
			jdoInputStream = loadProperties( "jdoconfig.xml" );
		}
		catch( Throwable exc )
		{
			logInfoMessage( "PropertyFileLoader:loadJDOConfiguration() - failed due to : " + exc );
		}
	}
	/**
	 * Loads the Hibernate Configuration file.
	 * If the rootURL has been provided, it is used as the base of the search.
	 * If not found there, looks in the classpath.  If still not found
	 * looks in the directory specified by the -DFRAMEWORK_PROPERTY system variable.
	 */			
	protected void loadHibernateConfiguration()
	{
		String fileName = FrameworkNameSpace.HIBERNATE_CONFIG_FILE;

		if ( hibernateConfiguration == null  ) // try the classpath
		{		
			try
			{				
				// try the default which uses the classpath
				hibernateConfiguration = new Configuration().configure();
				System.out.println(  "Done loading hibernate config file " + fileName + " from the classpath..." );												
			}
			catch( Throwable exc )
			{
				System.out.println( "** Failed to load hibernate config file " + fileName + " in the classpath due to " + exc + ".  Will try loading from the FRAMEWORK_HOME property " + FrameworkNameSpace.FRAMEWORK_HOME );
			}
		}

		if ( hibernateConfiguration == null && rootURL != null )	
		{						
			try
			{
				System.out.println(  "Loading hibernate config file " + fileName + " from the WAR file..." );				
				hibernateConfiguration = new Configuration().configure( new java.net.URL(rootURL.toString() + "/hibernate/" + fileName) );
				System.out.println(  "Done loading hibernate config file " + fileName + " from the WAR file..." );								
			}
			catch( Throwable exc )
			{
				System.out.println( "** Failed to load hibernate config file " + rootURL.toString() + "/hibernate/" + fileName + " from the WAR file due to " + exc + ".  Will try loading " + fileName + " from the classpath." );				 
			}			
		}

				
		if ( hibernateConfiguration == null  ) // try the FRAMEWORK_HOME
		{
			try
			{						
				hibernateConfiguration = new Configuration().configure( new java.net.URL("file://" + java.io.File.separatorChar + FrameworkNameSpace.FRAMEWORK_HOME + java.io.File.separatorChar + fileName) );
				System.out.println(  "Done loading hibernate config file " + fileName + "System property -DFRAMEWORK_HOME=" + FrameworkNameSpace.FRAMEWORK_HOME );				
			}
			catch( Throwable exc )
			{
			}			
		}		
		
		if ( hibernateConfiguration != null  )
		{
			System.out.println( "-- Successfully loaded hibernate config file " + fileName );			
		}	
		else
		{
			System.out.println( "**\nFailed to load hibernate config file " + fileName + ".\nIf required, Hibernate related operations will not work.\nSee rm_framework_log4j.log for more details.\n**" );
		}										
	}
	
	/**
	 * Loads the config file contents into an InputStream, and caches it by name.
	 * <p>
	 * @param	fileName
	 * @exception	InitializationException
	 */			
	protected InputStream loadProperties( String fileName )
	throws InitializationException
	{
		logInfoMessage(  "-- Loading property file " + fileName + "..." );
		
		InputStream inputStream = null;

		// If a rootURL has been provided and assigned, use it as the
		// relative location to loading the config.xml.  Otherwise
		// ignore and do the steps below

		if ( rootURL != null )	
		{
			try
			{
				System.out.println( "Looking for config file " + fileName + " in the classpath...");
				
				// try the classpath					
				inputStream = new DataInputStream( Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName) );
				//inputStream = getClass().getClassLoader().getResourceAsStream( fileName );
			}
			catch( Throwable exc1 )
			{
				try
				{	
					inputStream = new java.net.URL( rootURL.toString() + "/" + fileName ).openStream();
				}
				catch( Throwable exc )
				{
					System.out.println( "PropertyFileLoader.loadProperties(...) - failed  to load config file " + rootURL.toString() + "/" + fileName + ".  Will look for " + fileName + " in the classpath" );
					 
				}			
				
				throw new InitializationException( "PropertyFileLoader.loadProperties(...) - failed  to load config file " + fileName + " - " + exc1 );
			}
		}
		
		if ( inputStream == null )	// try the classpath
		{
			System.out.println( "Looking for config file " + fileName + " in the classpath...");
			
			inputStream = getClass().getClassLoader().getResourceAsStream( fileName );
		}
					
		if ( inputStream == null  ) // try the FRAMEWORK_HOME
		{
			System.out.println( "Looking for config file " + fileName + " in the " + FrameworkNameSpace.FRAMEWORK_HOME + "...");
			try
			{		
				inputStream = new java.net.URL( "file://" + java.io.File.separatorChar + FrameworkNameSpace.FRAMEWORK_HOME + java.io.File.separatorChar + fileName ).openStream();
			}
			catch( Throwable exc )
			{
				throw new InitializationException( "PropertyFileLoader.loadProperties(...) - failed  to load config file " + fileName + " - " + exc);
			}			
		}									
			
		logInfoMessage(  "-- Done loading property file " + fileName + "..." );		
		return( inputStream );											
	}

	
	/**
	 * @exception	XMLFileParsingException
	 */			
	protected void parseConfigXMLFile()
	throws XMLFileParsingException
	{			
		IConfigPropertiesHandler handler = null;
		
		try
		{
			handler = (IConfigPropertiesHandler)FrameworkPropertiesHandlerFactory.getInstance().getPropertyHandler( "com.framework.common.properties.ConfigXMLPropertiesHandler" );
			
			if ( configFileStream == null )
				configFileStream = loadProperties( CONFIG_FILE_NAME );			
			
			handler.parse( configFileStream );
		}
		catch( Throwable exc )
		{
			throw new XMLFileParsingException( "PropertyFileLoader.parseConfigXMLFile() - failed to load " + CONFIG_FILE_NAME + ".  Validate the files location -  " + exc, exc );
		}
		
		try
		{
	    	log4jParams   			= handler.getLog4JParams();
			configFileParamMappings = handler.getAppConfigFiles();

			if ( System.getProperty("DEBUG") != null )
			{
				System.out.println( "log4jparams = " + log4jParams );
				System.out.println( "configFileParamMappings = " + configFileParamMappings);
			}
		}
		catch( Throwable exc )
		{
			throw new XMLFileParsingException( "PropertyFileLoader.parseConfigXMLFile() - " + exc );			
		}			    
	}
	
	/**
	 * Helper used to open and establish the URL to the log4j config file used for internal
	 * framework logging
	 * <p>
	 * @param		fileName	normally log4j.xml or log4j.properties
	 * @return		load was successful
	 */			
	protected boolean loadLog4JAs( String fileName )
	{		
		boolean valid		= false;	
		
		try
		{	
			log4jURL = Thread.currentThread().getContextClassLoader().getResource(fileName);

//			else
	//			log4jURL = getClass().getClassLoader().getResource( fileName );

					
			log4jURL.openConnection();
									
			System.out.println( "  --  loading of " + FrameworkNameSpace.DEFAULT_LOG4J_CONFIGURATION_FILE_PREFIX + " property file was successful." );
			valid = true;

		}
		catch( Throwable exc )
		{
			try
			{
				if ( rootURL != null )
					log4jURL = Thread.currentThread().getContextClassLoader().getResource(fileName);

				log4jURL = new java.net.URL( "file://" + java.io.File.separatorChar + FrameworkNameSpace.FRAMEWORK_HOME + java.io.File.separatorChar + fileName );
				valid = true;
			}
			catch( Throwable exc1 )
			{
				log4jURL = null;
			}
		}
			
		return valid;			
								
	}
	
	/**
	 * Acquires the handler by name from the FrameworkPropertiesHandlerFactory, and then
	 * provides the inputStream for binding, using the name as the root.
     * <p>
     * @param	name		unique identifier of the property handler
	 * @param	handler		class name to handle loading the properties from a given inputStream
	 * @param   inputStream	stream representation of a configuration file
	 * @throws InitializationException
	 */
	private void bindWithPropertyHandler( String name, String handler, InputStream inputStream )	
	throws InitializationException
	{
		// get the property handler
		IPropertiesHandler propsHandler = null;
		try
		{		
			propsHandler = FrameworkPropertiesHandlerFactory.getInstance().getPropertyHandler( handler );
			propsHandler.parse( inputStream );		

			// cache the property handler						
			propertyHandlers.put( name, propsHandler );
		}
		catch( Throwable exc )
		{
			throw new InitializationException( "PropertyFileLoader.bindWithPropertyHandler() - " + exc, exc );
		}		
	}
	
	static protected PropertyFileLoader seeIfLoadedInJNDI()
	{
		PropertyFileLoader loader = null;
		try
		{
			if( !FrameworkNameSpace.GOOGLE_LICENSE_ENV )
			{
				InitialContext initialContext = new InitialContext();
				loader = (PropertyFileLoader)initialContext.lookup( PROPERTYFILELOADER_KEY );
			}
		}
		catch( NamingException exc )
		{
		}

		return( loader );
	}
// attributes
	
	/**
	 * singleton instance
	 */		
	static PropertyFileLoader instance = null;
				
	/**
	 * root location of config files
	 */				
	private java.net.URL rootURL = null;
		
	/**
	 * Map of property handler names / property handler pairing
	 */
	private Map propertyHandlers = null;					
	
	/**
	 * log4j config file reference is held as a url
	 */
	private java.net.URL log4jURL = null;	
	
	private Map log4jParams  = null;
	
	private Collection configFileParamMappings = null;

	private InputStream configFileStream = null;

	private Configuration hibernateConfiguration = null;
	
	private InputStream jdoInputStream = null;
		
	/**
	 * standard configuration file that points to the main config files in the system
	 */
	public static final String CONFIG_FILE_NAME = "config.xml";
	public static final String PROPERTYFILELOADER_KEY = "PropFileLoader";
}

/*
 * Change Log:
 * $Log:  $
 */
 
