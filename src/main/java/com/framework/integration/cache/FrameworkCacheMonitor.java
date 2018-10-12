/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.cache;

import javax.management.*;

import javax.management.monitor.CounterMonitor;
import javax.management.monitor.Monitor;
import javax.management.monitor.MonitorNotification;

import com.framework.common.event.FrameworkLogEventType;

import com.framework.common.jmx.FrameworkDynamicMBean;
import com.framework.common.jmx.FrameworkJMXServerFactoryLocator;

import com.framework.integration.cache.IFrameworkCache;

/**
 * Provides a JMX Monitor to a provided IFrameworkCache
 * <p>
 * @author		realMethods, Inc.
 * @see			com.framework.integration.cache.IFrameworkCache
 */
public class FrameworkCacheMonitor 
	extends FrameworkDynamicMBean
	implements NotificationListener
{

//************************************************************************    
// Constructors
//************************************************************************

	/**
	 * deter access - use version which requires an IFrameworkCache
	 */
	private FrameworkCacheMonitor()
	{
	}

	/**
	 * @param 		cache				the monitor target
	 * @param		cacheOwnerName		the name of the cache owner
	 * @exception	IllegalArgumentException	thrown if cache is null
	 */
	public FrameworkCacheMonitor( IFrameworkCache cache, String cacheOwnerName )
	{
		if ( cache == null )
			throw new IllegalArgumentException( "FrameworkCacheMonito(IFrameworkCache) - cache arg cannot be null." );
			
		this.cache 			= cache;			
		this.cacheOwnerName = cacheOwnerName;
	}
		
	
//************************************************************************    
// Public Methods
//************************************************************************

	/**
	 * Returns the cache
	 * @return		the cache
	 */
	public IFrameworkCache getCache()
	{
		return( cache );
	}
	
	/**
	 * Start monitoring notification.
	 */
	public void start()
	{
		if ( threshold == -1 )
		{
			logMessage( "A JMX Monitor for owner " + cacheOwnerName + " will not be created for the FrameworkCache since the threshold value has either not been specified or has been assigned as -1", FrameworkLogEventType.DEBUG_LOG_EVENT_TYPE );
			return;			
		}
				
		if ( monitor == null )
		{			
			monitor = new CounterMonitor();
		
			// register it with the MBeanServer			
			try
			{
				monitorName = getMBeanName();
				FrameworkJMXServerFactoryLocator.locate().createMBeanServer().registerMBean( monitor, monitorName );
			}
			catch( InstanceAlreadyExistsException exc )
			{
				// this is OK so continue on..
				logMessage( "FrameworkCacheMonitor.start() - " + exc, FrameworkLogEventType.ERROR_LOG_EVENT_TYPE );
			}
			catch( Throwable exc )
			{
				logMessage( "Failed to register a USOM JMX Monitor for UserSession-" + getInstanceName() + ".  A JMX Monitor will not be created for the USOM - " + exc, FrameworkLogEventType.ERROR_LOG_EVENT_TYPE );
				monitor = null;
				return;
			}
		
			// add ourselves as a listener, supplying the monitor name
			monitor.addNotificationListener( this, null, monitorName );		
					
			try
			{
				monitor.addObservedObject( getMBeanName() );
			}
			catch( Throwable exc )
			{
				logMessage( "Failed to assign " + monitorName + " as the observed object using the current MBean Server.  A JMX Monitor will not be created for the USOM.", FrameworkLogEventType.ERROR_LOG_EVENT_TYPE );
				monitor = null;
				return;
			}
		
			monitor.setObservedAttribute( "NumEntries" );   
			 
			// turn notification on only for the high value
			
			monitor.setNotify( true );
			monitor.setOffset( new Integer( 0 ) );
			monitor.setInitThreshold( new Long( threshold ) );
			monitor.setGranularityPeriod( checkInterval );
	    
			monitor.start();			
	    
			logMessage( "FrameworkCacheMonitor has started JMX Monitor for cache owned by " + cacheOwnerName, FrameworkLogEventType.INFO_LOG_EVENT_TYPE );
		}	
	}
	
	/**
	 * Stop monitoring notification.
	 */
	public void stop()
	{
		if ( monitor == null )
		{
			logWarnMessage( "FrameworkCacheMonitor.stopMonitoring() - monitor is not running." );
			return;
		}
					
		monitor.stop();
		
		try
		{        
			// attempt to stop the USOM related JMX Monitor and unregister it
			if ( monitor != null && monitorName != null )
			{
				monitor.stop();
				
				logMessage( "FrameworkCacheMonitor has stopped JMX Monitor for cache owned by " + cacheOwnerName, FrameworkLogEventType.INFO_LOG_EVENT_TYPE );
								
				FrameworkJMXServerFactoryLocator.locate().createMBeanServer().unregisterMBean( monitorName );        	        	
			}
		}
		catch( Throwable exc ) 
		{
			logMessage( "UserSession:valueUnound() - " + exc, FrameworkLogEventType.ERROR_LOG_EVENT_TYPE );			
		}       		
	}
	
// access methods

	/**
	 * Assigns the high threshold value, before JMX notification takes place,
	 * causing the cache to be purged.
	 * <p>
	 * @param	threshold
	 */
	public void setThreshold( int threshold )
	{
		this.threshold	= threshold;
	}
	
	/**
	 * Assigns the interval by which JMX should query for the cache size.
	 * <p>
	 * @param interval
	 */
	public void setCheckInterval( long interval )
	{
		checkInterval = interval;
	}

	/**
	 * Returns the number of entries currently cached.
	 * @return		Integer
	 */
	public Integer getNumEntries()
	{
		return( new Integer( cache.getAllKeysCached().size() ) );
	}
	
// FrameworkDynamicMBean overloads

   /**
	* Allows the value of the specified attribute of the Dynamic MBean to be obtained.
	*
	* @param       attributeName		named of a registered attribute
	* @return      Object
	* @exception   AttributeNotFoundException
	* @exception   MBeanException
	* @exception   ReflectionException 
	*/
	public Object getAttribute( String attributeName ) 
	throws AttributeNotFoundException, MBeanException, ReflectionException 
	{
		Object attribute = null;
	    
		if ( attributeName.equals( "NumEntries" ) )
		{
			attribute = getNumEntries();
		}	    
		else
		{
			// If attribute_name has not been recognized throw an AttributeNotFoundException
			throw(new AttributeNotFoundException("Cannot find " + attributeName + " attribute in " + getClassName()));
		}
	    
		return( attribute );
	}

	/**
	  * Sets the value of the specified attribute of the Dynamic MBean.
	  * 
	  * @param       attribute 
	  * @exception   AttributeNotFoundException
	  * @exception   InvalidAttributeValueException,
	  * @exception   MBeanException
	  * @exception   ReflectionException 
	  */
	 public void setAttribute( Attribute attribute ) 
	 throws AttributeNotFoundException, InvalidAttributeValueException,
			MBeanException, ReflectionException 
	 {

		 // Check attribute is not null to avoid NullPointerException later on
		 if (attribute == null) 
		 {
			 throw new RuntimeOperationsException(new IllegalArgumentException("Attribute cannot be null"), 
							  "Cannot invoke a setter of " + getClassName() + " with null attribute");
		 }
	    
		 String name = attribute.getName();
//		 Object value = attribute.getValue();

		 if (name == null) 
		 {
			 throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name cannot be null"), 
						  "Cannot invoke the setter of " + getClassName() + " with null attribute name");
		 }	    
		 else	    	    
		 {
			 throw(new AttributeNotFoundException("Attribute " + name +
							  " not found in " + this.getClass().getName()));
		 }
	 }
	 
	/**
	 * Allows an operation to be invoked on the Dynamic MBean.
	 * @param		operationName		method name to call
	 * @param		params				method parameters
	 * @param		signature			method signature, just in case it is overloaded
	 * @return		return value from the method invocation
	 * @throws		MBeanException		thrown if operationName is null
	 * @throws		ReflectionException	if the operationName is not supported
	 */
	public Object invoke( String operationName, Object params[], String signature[] )
	throws MBeanException, ReflectionException 
	{
		Object returnObject = null;
		
		// Check operationName is not null to avoid NullPointerException later on
		if (operationName == null) 
		{
			throw new RuntimeOperationsException(new IllegalArgumentException("Operation name cannot be null"), 
								 "Cannot invoke a null operation in " + getClassName());
		}
	/*	{ 
			// unrecognized operation name:
			throw new ReflectionException(new NoSuchMethodException(operationName), 
					  "Cannot find the operation " + operationName + " in " + getClassName());
		}
	*/	
		return returnObject;
	}
	
	
   /**
	* Returns all the attributes for this class as an array of MBeanAttributeInfos.
	* <p>
	* @return MBeanAttributeInfo[]
	*/
   	protected MBeanAttributeInfo[] getMBeanAttributes()
   	{
	   MBeanAttributeInfo[] mbAttributeInfo = new MBeanAttributeInfo[1];
       
       mbAttributeInfo[0] = new MBeanAttributeInfo( "NumEntries", "java.lang.Integer", "Number of Entries", true, false, false );
                
	   return( mbAttributeInfo );
	}
   
	/**
	 * Returns the name of this class instance.
	 * <p>
	 * @return		instance name
	 */
	public String getInstanceName() 
	{
		return( "FrameworkCacheMonitor-" + getUniqueID() );
	}

		
//	NotificationListener implementations

	 /**
	  * JMX counter monitor callback method.
	  * @param		notification
	  * @param		handback
	  */
	 public void handleNotification( Notification notification, Object handback ) 
	 {
        
		 MonitorNotification notif = (MonitorNotification)notification;
        
		 // Get a handle on the Monitor responsible for the notification emmited.
		 //
		 Monitor monitor = (Monitor)notif.getSource();
        
		 if ( monitor == monitor )
		 {
			 // Process the different types of notifications fired by the GaugeMonitor
			 //
			 String type = notif.getType();
	        
			 try 
			 {
				 if ( type.equals( MonitorNotification.THRESHOLD_VALUE_EXCEEDED ) )
				 {
					 if ( cache != null )
					 {
						 logMessage( "FrameworkCacheMonitor - Recv'd JMX Monitor notification of USOM threshold being exceeded.  Emptying entire cache for " + handback.toString(), FrameworkLogEventType.DEBUG_LOG_EVENT_TYPE );		        										
						 cache.emptyCache();        			
					 }	        		
				 }
			 }
			 catch( Throwable exc )
			 {
				 logMessage( "FrameworkCacheMonitor:handleNotification() - " + exc, FrameworkLogEventType.ERROR_LOG_EVENT_TYPE );
			 }
		 }        
	 }
    	
//*************************************************************************    
// Protected / Protected Methods
//************************************************************************

//************************************************************************    
// Attributes
//************************************************************************

	/**
	 * the monitoring target
	 */
	private IFrameworkCache cache	=  null;
	
	/**
	 * cache owner's name...more than likely the class that creates this instance
	 */
	private String cacheOwnerName = null;
	
	/**
	 * ObjectName of the CounterMonitor
	 */
	private ObjectName monitorName = null;	
	
	/**
	 * JMX Monitor
	 */
	private CounterMonitor monitor = null;	
	
	/**
	 * upper threshold value before JMX provides notification
	 */
	private int threshold  	= -1;
	
	/**
	 * How often should the cache be checked
	 */
	private long checkInterval = -1;		
}

/*
 * Change Log:
 * $Log$
 */
