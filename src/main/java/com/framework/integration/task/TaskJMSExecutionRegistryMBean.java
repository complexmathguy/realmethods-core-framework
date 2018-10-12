/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.task;

import java.util.HashMap;

import javax.management.*;

import com.framework.common.jmx.FrameworkDynamicMBean;

/**
 * TaskJMSExecutionRegistry DynamicMBean, in order
 * to accommodate the JMX specification towards object management.
 * <p>
 * @author    realMethods, Inc.
 */
public class TaskJMSExecutionRegistryMBean extends FrameworkDynamicMBean
{
// constructor

    /**
     * default constructor - public and empty per JMX specification for a DynamicMBean
     */
    public TaskJMSExecutionRegistryMBean()
    {
        super();
    }

// must implements from FrameworkDynamicMBean

    /**
     * Allows the value of the specified attribute of the Dynamic MBean to be obtained.
     *
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

   	    if ( attributeName.equals( "TaskJMSExecutionHandlers" ) )
	    {
	    	attribute = taskJMSExecutionHandlers;
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
//	    Object value = attribute.getValue();

	    if (name == null) 
	    {
	        throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name cannot be null"), 
						 "Cannot invoke the setter of " + getClassName() + " with null attribute name");
	    }
	    
	    // Check for a recognized attribute name and call the corresponding setter
	    {
	        throw(new AttributeNotFoundException("Attribute " + name +
			    			 " not found in " + this.getClass().getName()));
		}
    }

    /**
     * Allows an operation to be invoked on the Dynamic MBean.
     * <p>
     * @param		operationName		registered method to execute
     * @param		params				parammeters to provide to the method
     * @param		signature			
     * @exception	MBeanException
     * @exception	ReflectionException
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
     * returns a HashMap of TaskJMSExecutionHandler
     *
     * @return      HashMap
     */
    public HashMap getTaskJMSExecutionHandlers()
    { return( taskJMSExecutionHandlers ); }
    

// FrameworkDynamicMBean overloads

    /**
     * Returns the name of this class instance.  This implementation is
     * sufficient for singletons, but should be overloaded for sub-classes
     * requiring multiple instances.
     * <p>
     * @return		instance name
     */
    public String getInstanceName() 
    {
        return( "TaskJMSExecutionRegistry" );
    }

    /**
     * Returns the description of this MBean
     * @return      String      
     */
    protected String getMBeanDescription()
    {
        return( "Task JMS Execution Registry Dynamic MBean" );
    }
    
    
    /**
     * Returns all the attributes for this class as an array of MBeanAttributeInfos.
     * <p>
     * @return MBeanAttributeInfo[]
     */
    protected MBeanAttributeInfo[] getMBeanAttributes()
    {	
        MBeanAttributeInfo[] mbAttributeInfo = new MBeanAttributeInfo[1];

   		mbAttributeInfo[0] = new MBeanAttributeInfo( "TaskJMSExecutionHandlers", "java.util.HashMap", "Map of Task JMS execution handlers", true, false, false );
		
        return( mbAttributeInfo );
    }
	
// attributes

    /**
     * maps a text message to a Task by class name
     */ 
    protected HashMap taskJMSExecutionHandlers                      = new HashMap( 4 );
	
}

/*
 * Change Log:
 * $Log: TaskJMSExecutionRegistryMBean.java,v $
 */
