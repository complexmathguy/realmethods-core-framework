/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.common.jmx;

 

import java.lang.reflect.Constructor;

import java.util.*;

import javax.management.*;

import com.framework.common.FrameworkBaseObject;
import com.framework.common.exception.CreateJMXServerException;
import com.framework.common.misc.Utility;
import com.framework.common.namespace.FrameworkNameSpace;

import com.framework.integration.log.FrameworkDefaultLogger;

/**
 * Abstract base class for all Framework components that take on DynamicMBean
 * characteristics, according to the JMX specification.
 * <p>
 * @author    realMethods, Inc.
 */
abstract public class FrameworkDynamicMBean extends FrameworkBaseObject
    implements IFrameworkDynamicMBean, MBeanRegistration
{
    
// constructors    

	/**
	 * Attepts to automatically handle JMX registration on behalf of the subclass
	 */
    public FrameworkDynamicMBean() 
    {	    
	    // handle self registration
	    handleSelfRegistration();
    }

	/**
	 * Pass false to not check to on handling Self registration
	 * <p>
	 * @param  callToHandleSelfRegistration
	 */
    public FrameworkDynamicMBean( boolean callToHandleSelfRegistration ) 
    {
	    if ( callToHandleSelfRegistration == true)
	    {
		    // handle self registration
		    handleSelfRegistration();
	    }
    }

// access methods
    
    /**
     * Returns the name of this instances class name
     * <p>
     * @return      String
     */
    public String getClassName()
    {
        return( className );
    }
    
    /**
     * This method provides the exposed attributes and operations of the Dynamic MBean.
     * It provides this information using an MBeanInfo object.
     * <p>
     * @return		MBeanInfo
     */
    public MBeanInfo getMBeanInfo() 
    {
    	if ( mBeanInfo == null )
    	{
		    // build the management information to be exposed by the dynamic MBean
		    buildDynamicMBeanInfo();	 	    
    		
    	}    	
	    // return the information we want to expose for management:
	    return( mBeanInfo) ;
    }

    /**
     * Enables the to get the values of several attributes of the Dynamic MBean.
     * @param		attributeNames		names of the registered  attributes to provide values for
     * @return		a list of the values, one for each attribute name
     * @exception	RuntimeOperationsException	if attrributeNames is null
     */
    public AttributeList getAttributes( String[] attributeNames ) 
    {

	    // Check attributeNames is not null to avoid NullPointerException later on
	    if (attributeNames == null) 
	    {
	        throw new RuntimeOperationsException(new IllegalArgumentException("attributeNames[] cannot be null"),
						    "Cannot invoke a getter of " + getClassName());
    	}
    	
	    AttributeList resultList = new AttributeList();

	    // if attributeNames is empty, return an empty result list
	    if (attributeNames.length == 0)
	        return resultList;
        
    	// build the result attribute list
	    for (int i=0 ; i<attributeNames.length ; i++)
	    {
	        try 
	        {        
		        Object value = getAttribute((String) attributeNames[i]);     
		        resultList.add(new Attribute(attributeNames[i],value));
	        } 
	        catch (Throwable exc) 
	        {
		        FrameworkDefaultLogger.warning( "FrameworkDynamicMBean:getAttributes(...) - " + exc );
	        }
	    }
	    
	    return(resultList);
    }

    /**
     * Sets the values of several attributes of the Dynamic MBean, and returns the
     * list of attributes that have atually been set.
     * @param		attributes		names of the registered attributes to set a value on
     * @return		a list of the values, one for each attribute name data was set on
     * @exception	RuntimeOperationsException	if attrributeNames is null
     */
    public AttributeList setAttributes( AttributeList attributes ) 
    {
	    // Check attributes is not null to avoid NullPointerException later on
	    if (attributes == null) 
	    {
	        throw new RuntimeOperationsException(new IllegalArgumentException("AttributeList attributes cannot be null"),
						    "Cannot invoke a setter of " + getClassName());
    	}
	    AttributeList resultList = new AttributeList();

	    // if attributeNames is empty, nothing more to do
	    if (attributes.isEmpty())
	        return resultList;

    	// for each attribute, try to set it and add to the result list if successfull
	    for (Iterator i = attributes.iterator(); i.hasNext();) 
	    {
	        Attribute attr = (Attribute) i.next();
	        try 
	        {
		        setAttribute(attr);
		        String name = attr.getName();
		        Object value = getAttribute(name); 
		        resultList.add(new Attribute(name,value));
	        } 
	        catch(Throwable exc) 
	        {
		        FrameworkDefaultLogger.warning( "FrameworkDynamicMBean:setAttributes(...) - " + exc );
	        }
	    }
	    
	    return(resultList);
    }
    
    /**
     *  Handle MBean registration with MBeanServer
     */
    public void registerMBeanWithServer()
    {        
    	if ( FrameworkNameSpace.GOOGLE_LICENSE_ENV == true )
    	{
    		System.out.println( "Unable to self register the JMX MBServer due to it not being supported by Google App Engine..." );
    		return;
    	}
    	
        try
        {
			// dynamically acquire the named interface implementation from the framework.xml
			// to handle acquiring the correct MBeanServer
			mBeanServer = dynamicallyAcquireMBeanServer();        	
	             	
			mBeanName = generateObjectName();
				             	
			if ( mBeanServer != null && mBeanServer.isRegistered( mBeanName ) == false )
			{			             	
            	mBeanServer.registerMBean( this, mBeanName );
            	if ( System.getProperty("DEBUG") != null )
            		logInfoMessage( "Registered Dynamic MBean : " + mBeanName );            	
			}            
        }
        catch (Exception e) 
        {
            logErrorMessage( "FrameworkDynamicMBean:registerMBeanWithServer() - Failed to register Dynamic MBean " + getInstanceName() + " - "  + e );
        }
    }

    /**
     *  Handle MBean unregistration with MBeanServer
     */
    public void unRegisterMBeanWithServer()
    {        
        try
        {
        	if ( mBeanName != null)
        	{
        		if ( dynamicallyAcquireMBeanServer().isRegistered( mBeanName ) )
        		{
		            dynamicallyAcquireMBeanServer().unregisterMBean( mBeanName );
			        logInfoMessage( "Unregistered Dynamic MBean : " + mBeanName );
        		}
        	}
        }
        catch (Throwable exc) 
        {
            logErrorMessage( "FrameworkDynamicMBean:unRegisterMBeanWithServer() - Failed to unregister Dynamic MBean " + getInstanceName() + " - "  + exc );
        }
    }

	/**
	 * Override this method for application specific derived classes.  Default return
	 * value is FrameworkNameSpace.MBEAN_DOMAIN
	 * 
	 * @return String
	 */        
	public String getMBeanServerDomain()
	{
		return( FrameworkNameSpace.MBEAN_DOMAIN );
	}
	        
// helper methods - should be overloaded as appropriate within the sub-class

	/**
	 * Attempts to acquire the class implementation specified for property 
	 * JMX_MBEAN_SERVER_IMPLEMENTATION specified in the framework.xml file
	 * 
	 * @return		MBeanServer
	 * @exception	CreateJMXServerException
	 */
	protected MBeanServer dynamicallyAcquireMBeanServer()
	throws CreateJMXServerException
	{
		try
		{
			if ( mBeanServerFactory == null )
			{
				mBeanServerFactory = FrameworkJMXServerFactoryLocator.locate();
			}
			
			mBeanServer = mBeanServerFactory.createMBeanServer();
		}
		catch( Throwable exc )
		{
			throw new CreateJMXServerException( "FrameworkDynamicMBean:dynamicallyAcquireMBeanServer() - " + exc, exc );
		}
			
	    return( mBeanServer );
		
	}
	
    /**
     * Returns the name of this class instance.  This implementation is
     * sufficient for singletons, but should be overloaded for sub-classes
     * requiring multiple instances.
     * <p>
     * Recommended to be overloaded.
     * <p>
     * @return		instance name
     */
    public String getInstanceName() 
    {
        return( "FrameworkDynamicMBean-" + getUniqueID() );
    }
    
	/**
	 * Returns the MBean's ObjectName
	 * <p>
	 * @return ObjectName
	 */
	public ObjectName getMBeanName()
	{
		return( mBeanName );
	}    
    /**
     * Returns the description of this MBean.  Should be overloaded.
     * <p>
     * Recommended to be overloaded.
     * <p>
     * @return      String      
     */
    protected String getMBeanDescription()
    {    	
        return( "Framework Dynamic MBean" );
    }
    
    /**
     * Returns all the attributes for this class as an array of MBeanAttributeInfos.
     *  <p>
     * @return MBeanAttributeInfo[]
     */
    protected MBeanAttributeInfo[] getMBeanAttributes()
    {
        return( null );
    }
    
    /**
     * Returns all the constructors for this class as an array of MBeanConstructorInfos.
     * <p>
     * @return MBeanConstructorInfo[]
     */
    protected MBeanConstructorInfo[] getMBeanConstructors()
    {
        MBeanConstructorInfo[] mbConstructors   = new MBeanConstructorInfo[1];        
	    Constructor[] constructors              = this.getClass().getConstructors();
	    
	    if ( constructors.length > 0 )
	    {
	        mbConstructors[0] = new MBeanConstructorInfo( getClassName() + " : Constructs a " + getClassName() + " object",
						        constructors[0]);
		}
		
        return( mbConstructors );						        
    }
    
    /**
     * Returns all the operations for this class as 
     * an array of MBeanOperationInfos.
     * <p>
     * @return MBeanOperationInfo[]
     */    
    protected MBeanOperationInfo[] getMBeanOperations()
    {
        return( null );
    }
    
    /**
     * Build the private dMBeanInfo field,
     * which represents the management interface exposed by the MBean;
     * that is, the set of attributes, constructors, operations and notifications
     * which are available for management. 
     * <p>
     * A reference to the created MBeanInfo object within this method is returned by the getMBeanInfo() method
     * of the DynamicMBean interface. Note that, once constructed, an MBeanInfo object is immutable.
     */
    private void buildDynamicMBeanInfo() 
    {                   
	    mBeanInfo = new MBeanInfo(getClassName(),
				            getMBeanDescription(),
				            getMBeanAttributes(),
				            getMBeanConstructors(),
				            getMBeanOperations(),
				            new MBeanNotificationInfo[0] );				            				            
    }

    /**
     * Class internal method used to determine if a JMX MBean component should
     * handle it's own registration
     */
    protected void handleSelfRegistration()
    {
if ( handleSelfRegistration == null )
        {
            try
            {
                handleSelfRegistration = new Boolean( Utility.getFrameworkProperties().getProperty( FrameworkNameSpace.JMX_SELF_REGISTRATION, "FALSE"  ));
                logInfoMessage( "FrameworkDynamicMBean:handleSelfRegistration() is turned " + (handleSelfRegistration.booleanValue() ? "ON" : "OFF" ));                
            }
            catch( Exception exc )
            {
                logErrorMessage( "FrameworkDynamicMBean:handleSelfRegistration() - " + exc );                
                handleSelfRegistration = new Boolean( true );
            }
        }
        
        if ( handleSelfRegistration.booleanValue() == true )
        {
        	registerMBeanWithServer();
        	
        }
    }

    /**
     * Generates a unique value for the purpose of MBean naming during registration.
     * <p>
     * Delegates to Utility.generateUID()
     * @return      String
     * @see			com.framework.common.misc.Utility#generateUID()
     */
    protected String getUniqueID()
    {
        return( Utility.generateUID() );
    }

    /**
     * Combines the attributes of a parent and a child into one MBeanAttributeInfo[]
     * <p>
     * @param   parent
     * @param   child
     * @return  MBeanAttributeInfo[]
     */
    protected MBeanAttributeInfo[] combineAttributeInfos( MBeanAttributeInfo[] parent, MBeanAttributeInfo[] child )
    {
        int parentLength    = 0;
        int childLength     = 0;
        
        if ( parent != null )
            parentLength = parent.length;

        if ( child != null )
            childLength = child.length;
            
        MBeanAttributeInfo[] totalAttributeInfo = new MBeanAttributeInfo[ parentLength + childLength ];
        
        // apply the parent attributes
        int i = 0;
        for( ; i < parentLength; i++ )
        {
            totalAttributeInfo[i] = parent[i];
        }
        
        // apply the child attributes
        for( int j = 0; j < childLength; j++ )
        {
            totalAttributeInfo[i+j] = child[j];
        }
        
        return( totalAttributeInfo );
    }

    /**
     * Combines the operations of a parent and a child into one MBeanOperationInfo[]
     * 
     * @param   parent
     * @param   child
     * @return  MBeanOperationInfo[]
     */
    protected MBeanOperationInfo[] combineOperationInfos( MBeanOperationInfo[] parent, MBeanOperationInfo[] child )
    {
        int parentLength    = 0;
        int childLength     = 0;
        
        if ( parent != null )
            parentLength = parent.length;

        if ( child != null )
            childLength = child.length;
            
        MBeanOperationInfo[] totalOperationInfo = new MBeanOperationInfo[ parentLength + childLength ];
        
        // apply the parent operations
        int i = 0;
        for( ; i < parentLength; i++ )
        {
            totalOperationInfo [i] = parent[i];
        }
        
        // apply the child operations
        for( int j = 0; j < childLength; j++ )
        {
            totalOperationInfo [i+j] = child[j];
        }
        
        return( totalOperationInfo );
        
    }
    
// MBeanRegistration implementations

	/**
	 * 
	 */
    public ObjectName preRegister(MBeanServer server, ObjectName name) 
    throws java.lang.Exception 
    {        
        return name;
    } 

	/**
	 * override as necessary
	 */
    public void postRegister(Boolean registrationDone) 
    {
    } 

	/**
	 * override as neccessary
	 */
    public void preDeregister() 
    throws java.lang.Exception 
    {
    }

	/**
	 * override as nessary
	 */
    public void postDeregister() 
    {
    }
    
// helper methods

	/**
	 * Helper for consistantly creating the ObjectName
	 * 
	 * @return		ObjectName
	 * @exception	MalformedObjectNameException
	 * @exception	NullPointerException
	 */
	protected ObjectName generateObjectName()
	throws MalformedObjectNameException, NullPointerException
	{
		return( new ObjectName( getMBeanServerDomain() + ":name=" + getInstanceName() ) );
	}
	
	/**
	 * Helper method used to assign a key/value set of entities to a Collection
	 * of MBeanAttributes...
	 * @param	map				key/value pairings
	 * @param	writeEnabled	
	 * @return	Collection
	 */
	protected Collection assignMapToMBeanAttributes( Map map, boolean writeEnabled )
	{
		Collection attributes 	= new ArrayList( map.size() );
		Iterator iter		= map.keySet().iterator();
		String key			= null;
		
		while( iter.hasNext() )
		{
			key = (String)iter.next();			
			attributes.add( new MBeanAttributeInfo( key, "java.lang.String", key, true, writeEnabled, false ) );			
		}
		
		return( attributes );
	}
	
	/**
	 * Helper method used to assign a value to a Map if the key already exists.
	 * 
	 * @param map		what to assign the key/value to	
	 * @param key		key to apply to the provided value into the map
	 * @param value		value to associate
	 * @return boolean
	 */
	protected boolean assignToMapIfContained( Map map, String key, Object value )
	{
		boolean contained = map.containsKey( key );
		
		if ( contained == true )
		{
			map.put( key, value );
		}
		
		return( contained );
	}
	
	/**
	 * Helper method used to combine a Collection and array of MBeanAttributeInfos into one array
	 * @param attributes
	 * @param array
	 * @return MBeanAttributeInfo[]
	 */
	protected MBeanAttributeInfo[] buildMBeanAttributeInfoArray( Collection attributes, MBeanAttributeInfo[] array )
	{
		MBeanAttributeInfo[] attributeInfos = new MBeanAttributeInfo[attributes.size() + array.length ];
		int index							= 0;
		
		// assign from the provided array
		for( index = 0; index < array.length; index++ )
			attributeInfos[ index ] = array[ index ];
			
		// assign from the provided Collection
		Iterator iter = attributes.iterator();
		
		for( ; iter.hasNext(); index++ )
		{
			attributeInfos[ index ] = (MBeanAttributeInfo)iter.next();
		}
		
		return( attributeInfos );
	}
    
// attributes

	/**
	 * Unique name for this class of dynamic mbeans, defaults to the sub-classes class name.
	 */
    private String className                         	= getClass().getName();
    
    /**
     * Instance unique identifier
     */
//    private String uniqueID								= null;
    
    /**
     * Holds all of the MBeanInfo related to this instance
     */
    protected MBeanInfo mBeanInfo                    	= null;
    
    /**
     * Should handle registration of this instance to the mBeanServer
     */
    protected static Boolean handleSelfRegistration		= null;
    
    /**
     * MBeanServerFactory in use
     */
    protected static IFrameworkJMXServerFactory mBeanServerFactory = null;
    
    /**
     * MBeanServer associated with this dynamic mbean instance
     */
    protected MBeanServer mBeanServer           			= null;
    
    /**
     * MBean objectname
     */
    protected ObjectName mBeanName							= null;

}

/*
 * Change Log:
 * $Log: FrameworkDynamicMBean.java,v $
 */
