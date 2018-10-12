/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.objectpool;

import java.util.Map;

import javax.management.*;

import com.framework.common.jmx.FrameworkDynamicMBean;

/**
 * Connection Pool DynamicMBean, in order
 * to accommodate the JMX specification towards object management.
 *
 * <p>
 * @author    realMethods, Inc.
 */
abstract public class ConnectionPoolMBean
    extends FrameworkDynamicMBean
{
// constructor

    /**
     * default constructor - public and empty per JMX specification for a DynamicMBean
     */
    public ConnectionPoolMBean()
    {
        super( false /* do not register yet... */ );
    }

// must implements from FrameworkDynamicMBean

    /**
     * Allows the value of the specified attribute of the Dynamic MBean to be obtained.
     * <p>
     * @param       attributeName
     * @return      Object
     * @exception   AttributeNotFoundException
     * @exception   MBeanException
     * @exception   ReflectionException 
     */
    public Object getAttribute( String attributeName ) 
	throws AttributeNotFoundException, MBeanException, ReflectionException 
	{
	    Object attribute = null;
	    
		try
		{				    
		    if ( attributeName.equals( POOLNAMETAG ) )
		    {
		        attribute = getPoolName();
		    }  
			else if ( attributeName.equals( CONNECTIONCLASSNAMETAG ) )
			{
				attribute = getConnectionClassName();
			} 	    
			else if ( attributeName.equals( INITIALCAPACITYTAG ) )
			{
				attribute = getInitialCapacity();
			} 	    		
			else if ( attributeName.equals( MAXCAPACITYTAG ) )
			{
				attribute = getMaxCapacity();
			} 	    				
			else if ( attributeName.equals( CAPACITYINCREMENTTAG ) )
			{
				attribute = getCapacityIncrement();
			} 	    
			else if ( attributeName.equals( ALLOWSHRINKAGETAG ) )
			{
				attribute = getAllowShrinking();
			}
			else if ( attributeName.equals( WAITUNTILAVAILABLE ) )
			{
				attribute = getWaitUntilAvailable();
			}		
		    else
		    {
	    	    // If attribute_name has not been recognized throw an AttributeNotFoundException
		        throw(new AttributeNotFoundException("Cannot find " + attributeName + " attribute in " + getClassName()));
		    }
		}
		catch( IllegalAccessException accessEx )
		{
			throw new AttributeNotFoundException( "Attribute " + attributeName + " unable to be accessed: " + accessEx );
		}
	    
	    return( attribute );
    }

    /**
     * Sets the value of the specified attribute of the Dynamic MBean.
     * <p>
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
	    Object value = attribute.getValue();

		if ( name.equals( CONNECTIONCLASSNAMETAG ) )
		{
			connectionClassName = (String)value;
		}
		else if ( name.equals( INITIALCAPACITYTAG ) )
		{
			initialCapacity = (Integer)value;
		}
		else if ( name.equals( MAXCAPACITYTAG ) )
		{
			maxCapacity = (Integer)value;
		}
		else if ( name.equals( CAPACITYINCREMENTTAG ) )
		{
			capacityIncrement = (Integer)value;
		}
		else if ( name.equals( ALLOWSHRINKAGETAG ) )
		{
			allowShrinking = (Boolean)value;
		}
		else if ( name.equals(WAITUNTILAVAILABLE) )
		{
			waitUntilAvailable = (Boolean)value;
		}				
	    else if (name == null) 
	    {
	        throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name cannot be null"), 
						 "Cannot invoke the setter of " + getClassName() + " with null attribute name");
	    }	    
	    // Check for a recognized attribute name and call the corresponding setter
	    else
	    {
	        throw(new AttributeNotFoundException("Attribute " + name +
			    			 " not found in " + this.getClass().getName()));
		}
    }

	/**
	 * Allows an operation to be invoked on the Dynamic MBean.
	 * <p>
	 * @param       operationName		registered method name
	 * @param       params				method parameters
	 * @param       signature
	 * @return      Object
	 * @exception   MBeanException
	 * @exception   ReflectionException
	 */
    public Object invoke( String operationName, Object params[], String signature[] )
	throws MBeanException, ReflectionException 
	{
	
	    // Check operationName is not null to avoid NullPointerException later on
	    if (operationName == null) 
	    {
	        throw new RuntimeOperationsException(new IllegalArgumentException("Operation name cannot be null"), 
			        			 "Cannot invoke a null operation in " + getClassName());
	    }
	    // Check for a recognized operation name and call the corresponding operation
	    { 
	        // unrecognized operation name:
	        throw new ReflectionException(new NoSuchMethodException(operationName), 
					  "Cannot find the operation " + operationName + " in " + getClassName());
	    }
    }


// accessor methods - according to Java Bean spec.

    /**
     * Retrieves the name of the pool that this connection is part of.
     * @return String
     */
    public String getPoolName()
    {
        return ( poolName );    
    }

    /**
     * Returns the previously assign Map of properties.
     * @return      Map
     */
    public Map getProperties()
    {
        return( poolProperties );
    }

    /**
     * Returns the initial capacity.
     * @return      Integer
     * @exception  IllegalAccessException
     */    
    public Integer getInitialCapacity()
    throws IllegalAccessException
    {
    	if ( initialCapacity == null )
    	{
	        String s = (String)getProperties().get( this.INITIALCAPACITYTAG);
        
    	    if ( s != null )
        	    initialCapacity = new Integer( s );
	        else
    	        throw new IllegalAccessException();
    	}
    	
    	return( initialCapacity );
        
    }
    
    /**
     * Returns the connection class name.
     * @return  	String
     * @exception  IllegalAccessException
     */    
    public String getConnectionClassName()
    throws IllegalAccessException    
    {
    	if ( connectionClassName == null )
    	{
	        String s = (String)getProperties().get( this.CONNECTIONCLASSNAMETAG );
        
    	    if ( s != null )
        	    connectionClassName = s;
	        else
    	        throw new IllegalAccessException();
    	}
    	
        return( connectionClassName );
    }
    
    
    /**
     * Returns the max capacity.
     * @return      Integer
     * @exception  IllegalAccessException
     */    
    public Integer getMaxCapacity()
    throws IllegalAccessException
    {
    	if ( maxCapacity == null )
    	{
	        String s = (String)getProperties().get( this.MAXCAPACITYTAG );
        
    	    if ( s != null )
        	    maxCapacity = new Integer( s );
	        else
    	        throw new IllegalAccessException();
    	}
    	
    	return( maxCapacity );
        
    }
    
    /**
     * Returns the capacity incremenet.
     * @return      Integer
     * @exception  IllegalAccessException
     */    
    public Integer getCapacityIncrement()
    throws IllegalAccessException
    {
    	if ( capacityIncrement == null )
    	{
	        String s = (String)getProperties().get( this.CAPACITYINCREMENTTAG );
        
    	    if ( s != null )
        	    capacityIncrement = new Integer( s );
	        else
    	        throw new IllegalAccessException();
    	}
    	
    	return( capacityIncrement );
    }
    
    /**
     * Returns the allow shrinking indicator.
     * @return      Boolean
     * @exception   IllegalAccessException
     */
    public Boolean getAllowShrinking()
    throws IllegalAccessException
    {
    	if ( allowShrinking == null )
    	{
	        String s = (String)getProperties().get( this.ALLOWSHRINKAGETAG );
	        
	        if ( s != null )
	            allowShrinking = new Boolean( s );
	        else
	            throw new IllegalAccessException();
    	}
    	
    	return( allowShrinking );
    }
    
	/**
	 * Returns the wait until available indicator
	 * @return      Boolean
	 */
	public Boolean getWaitUntilAvailable()
	{
		if ( waitUntilAvailable == null )
		{
			String s = (String)getProperties().get( this.WAITUNTILAVAILABLE );
	        
			if ( s != null )
				waitUntilAvailable = new Boolean( s );
			else
				waitUntilAvailable = new Boolean( false );
		}
    	
		return( waitUntilAvailable );
	}    
    /**
     * Returns the using caching indicator.
     * @return      Boolean
     */
    public Boolean usingInternalCaching()
    {
        if ( usingInternalCaching == null )
        {
            String s = (String)getProperties().get( this.INITIALCAPACITYTAG );
        
            if ( s != null && s.length() > 0 )
                usingInternalCaching = new Boolean( true );
            else
                usingInternalCaching = new Boolean( false );            
        }       
        
        return( usingInternalCaching );
    }
    
// action methods - according to Java Bean spec.
    

// overloaded helper methods

 
    /**
     * Returns the name of this class instance.  This implementation is
     * sufficient for singletons, but should be overloaded for sub-classes
     * requiring multiple instances.
     * @return		instanace name
     */
    public String getInstanceName() 
    {
        return("ConnectionPool-" + getPoolName() );
    }

    /**
     * Returns the description of this MBean
     * @return      String      
     */
    protected String getMBeanDescription()
    {
        return( "Connection Pool MBean" );
    }
    
    
    /**
     * Returns all the attributes for this class as an array of MBeanAttributeInfos.
     * <p>
     * @return MBeanAttributeInfo[]
     */
    protected MBeanAttributeInfo[] getMBeanAttributes()
    {
        MBeanAttributeInfo[] mbAttributeInfo = new MBeanAttributeInfo[7];
        
        mbAttributeInfo[0] = new MBeanAttributeInfo( POOLNAMETAG, "java.lang.String", "Pool Name: ", true, false, false );
        mbAttributeInfo[1] = new MBeanAttributeInfo( CONNECTIONCLASSNAMETAG, "java.lang.String", "com.framework.objectpool Connection class in-use", true, true, false );
        mbAttributeInfo[2] = new MBeanAttributeInfo( INITIALCAPACITYTAG, "java.lang.Integer", "Initial Capacity of the Pool", true, true, false );
        mbAttributeInfo[3] = new MBeanAttributeInfo( MAXCAPACITYTAG, "java.util.Integer", "Max. Capacity of the Pool", true, true, false );
        mbAttributeInfo[4] = new MBeanAttributeInfo( CAPACITYINCREMENTTAG, "java.util.Integer", "How much to increment the pool capacity by.", true, true, false );
        mbAttributeInfo[5] = new MBeanAttributeInfo( ALLOWSHRINKAGETAG, "java.util.Boolean", "Can this pool be shrunk?", true, true, false );
		mbAttributeInfo[6] = new MBeanAttributeInfo( WAITUNTILAVAILABLE, "java.util.Boolean", "If no connections available, wait until one is", true, true, false );
         
        return( mbAttributeInfo );
    }

// protected methods

    protected void setPoolName( String name )
    {
        poolName = name;
    }
    
    
    public void setProperties( Map props )
    {
        poolProperties = props;
    }

    
// attributes

    /**
     * Connection Pool name that this connection is part of.
     */
    private String poolName              = null;
    

     /** 
     * Map containing the connection parameters.
     * The connection parameters are any parameters in the property file that are
     * not covered by member attributes of this class.
     */
    private Map poolProperties   	= null;
    
                                                         
    private String connectionClassName	= null;
    private Integer initialCapacity		= null;
    private Integer maxCapacity			= null;
    private Integer capacityIncrement	= null;
    private Boolean allowShrinking		= null;
    private Boolean waitUntilAvailable	= null;
 
    /**
     * internal cache in use indicator
     */
    private Boolean usingInternalCaching = null;
    
// related connectionpool.properties keys    
    private final String POOLNAMETAG            = "poolName";
    private final String CONNECTIONCLASSNAMETAG = "connectionClassName";
    private final String INITIALCAPACITYTAG     = "initialCapacity";
    private final String MAXCAPACITYTAG         = "maxCapacity";
    private final String CAPACITYINCREMENTTAG   = "capacityIncrement";
    private final String ALLOWSHRINKAGETAG      = "allowShrinking";
    private final String WAITUNTILAVAILABLE		= "waitUntilAvailable";
}

/*
 * Change Log:
 * $Log: ConnectionPoolMBean.java,v $
 */
