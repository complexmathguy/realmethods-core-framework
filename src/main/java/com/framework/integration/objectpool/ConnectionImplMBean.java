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
 * Connection Implementation DynamicMBean, in order
 * to accommodate the JMX specification towards object management.
 * <p>
 * @author    realMethods, Inc.
 */
abstract public class ConnectionImplMBean
    extends FrameworkDynamicMBean implements IConnectionImpl 
{
// constructor

    /**
     * default constructor - public and empty per JMX specification for a DynamicMBean
     */
    public ConnectionImplMBean()
    {
        super( false /* do not self-register yet */);
    }

// must implements from FrameworkDynamicMBean

    /**
     * Allows the value of the specified attribute of the Dynamic MBean to be obtained.
     * <p>
     * @param       attributeName		name of registered member variable
     * @return      Object
     * @exception   AttributeNotFoundException
     * @exception   MBeanException
     * @exception   ReflectionException 
     */
    public Object getAttribute( String attributeName ) 
	throws AttributeNotFoundException, MBeanException, ReflectionException 
	{
	    Object attribute = null;
	    
	    if ( attributeName.equals( POOL_NAME ) )
	    {
	        attribute = poolName;
	    }
	    else if ( attributeName.equals( CONNECTION_PROPERTIES ) )
	    {
	        attribute = properties;
	    }
	    else if ( attributeName.equals( IN_USE_FLAG ) )
	    {
	        attribute = inUse;
	    }
	    // is it a key in the connection properties
	    else if ( properties != null && properties.containsKey( attributeName ) )
	    {
	    	attribute = properties.get( attributeName );
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

	    if (name == null) 
	    {
	        throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name cannot be null"), 
						 "Cannot invoke the setter of " + getClassName() + " with null attribute name");
	    }
	    
	    // Check for a recognized attribute name and call the corresponding setter

        if ( name.equals( IN_USE_FLAG ) )
	    {
	        inUse = (Boolean)value;
	    }
		else if( assignToMapIfContained( properties, name, value )	)
		{
			return;			
		}
	    else
	    {
	        throw(new AttributeNotFoundException("Attribute " + name +
			    			 " not found in " + this.getClass().getName()));
		}
    }

// overloads from ConnectionImplMBean

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
	    if ( operationName == null) 
	    {
	        throw new RuntimeOperationsException(new IllegalArgumentException("Operation name cannot be null"), 
			        			 "Cannot invoke a null operation in " + getClassName());
	    }
	    
	    Object returnObject = null;
	    
	    try
	    {
	        if ( operationName.equals( "commit" ) )
	        {
	            commit();
	        }
	        else if ( operationName.equals( "rollback" ) )
	        {
	            rollback();
	        }	    
	        else if ( operationName.equals( "disconnect" ) )
	        {
	            disconnect();
	        }	    
		    else
		    // Check for a recognized operation name and call the corresponding operation
		    { 
		        // unrecognized operation name:
		        throw new ReflectionException(new NoSuchMethodException(operationName), 
						  "Cannot find the operation " + operationName + " in " + getClassName());
		    }
        }
        catch( Exception exc )
        {
            throw new MBeanException( exc );
        }
        
	    return( returnObject );
    }

    /**
     * Returns all the operations for this class as  an array of MBeanOperationInfos.
     * <p>
     * @return MBeanOperationInfo[]
     */    
    protected MBeanOperationInfo[] getMBeanOperations()
    {
        MBeanOperationInfo[] mbOperations = new MBeanOperationInfo[3];
        MBeanParameterInfo[] params     = null;        
        
	    mbOperations[0] = new MBeanOperationInfo( COMMIT,
						COMMIT + "(): Commit the current transaction associated with this connection.",
						params , 
						"void", 
						MBeanOperationInfo.ACTION);        

	    mbOperations[1] = new MBeanOperationInfo( ROLLBACK,
						ROLLBACK + "(): Rollback the current transaction associated with this connection.",
						params , 
						"void", 
						MBeanOperationInfo.ACTION);        

	    mbOperations[2] = new MBeanOperationInfo( DISCONNECT,
						DISCONNECT + "(): Disconnect this connection.",
						params , 
						"void", 
						MBeanOperationInfo.ACTION);        
						
        return( mbOperations );						
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
     * Returns the previously assign Map of properties
     * @return      Map
     */
    public Map getProperties()
    {
        return( properties );
    }

    /**
     * Retrieves the InUse flag.
     * @return A Boolean indicating whether the connection is being used.
     */
    public Boolean getInUse()
    {
        return inUse;   
    }

// action methods - according to Java Bean spec.
    

// overloaded helper methods

    /**
     * Returns the description of this MBean
     * @return      String      
     */
    protected String getMBeanDescription()
    {
        return( "Connection Implementation MBean" );
    }
    
    /**
     * Returns the name of this class instance.  This implementation is
     * sufficient for singletons, but should be overloaded for sub-classes
     * requiring multiple instances.
     * <p>
     * @return		instance name
     */
    public String getInstanceName() 
    {
        return("ConnectionImpl-" + getPoolName() );
    }
        
    /**
     * Returns all the attributes for this class as an array of MBeanAttributeInfos.
     * <p>
     * @return MBeanAttributeInfo[]
     */
    protected MBeanAttributeInfo[] getMBeanAttributes()
    {
        MBeanAttributeInfo[] mbAttributeInfo 	= new MBeanAttributeInfo[3];
        
        try
        {
            mbAttributeInfo[0] = new MBeanAttributeInfo( POOL_NAME, "java.lang.String", "UserIDTagValue: the HTML form tag indicating the user's id.", true, false, false );
            mbAttributeInfo[1] = new MBeanAttributeInfo( IN_USE_FLAG, "java.lang.Boolean", "InUseFlag: Flag used to indicate if the connection pool instance is now in use.", true, true, false );
            mbAttributeInfo[2] = new MBeanAttributeInfo( CONNECTION_PROPERTIES, "java.util.Properties", "Properties: The properties specific to this connection.", true, false, false );
            
            mbAttributeInfo = buildMBeanAttributeInfoArray( assignMapToMBeanAttributes( properties, true), mbAttributeInfo );
            
        }
        catch( Exception exc )
        {
            logErrorMessage( "ConnectionImplMBean:getMBeanAttributes() - " + exc );
        }
        
        return( mbAttributeInfo );
    }

// protected methods

	/**
	 * Assign the pool name.
	 * @param	name
	 */
    protected void setPoolName( String name )
    {
        poolName = name;
    }
    
    /**
     * Assign the properties.
     * @param 	props
     */
    public void setProperties( Map props )
    {
        properties = props;
    }
    
    /** 
     * Sets the InUse flag.
     * @param	inuseflag 
     */
    public void setInUse( Boolean inuseflag )
    {
        if ( inuseflag != null )
        {
            inUse = inuseflag;   
        }
    }
    
// attributes

    /**
     * Connection Pool name that this connection is part of.
     */
    private String poolName = null;

    /**
     * Map of key/value pairings for the connection.
     */
    private Map properties = null;

    /**
     * InUse flag.
     */
    private Boolean inUse = null;
 
    private final String POOL_NAME              = "PoolName";
    private final String CONNECTION_PROPERTIES  = "ConnectionProperties";
    private final String IN_USE_FLAG            = "InUseFlag";
}

/*
 * Change Log:
 * $Log: ConnectionImplMBean.java,v $
 */
