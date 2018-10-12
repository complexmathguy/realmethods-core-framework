/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.task;

import javax.management.*;

import com.framework.common.jmx.FrameworkDynamicMBean;

/**
 * TaskJMSExecutionHandler DynamicMBean, in order
 * to accommodate the JMX specification towards object management.
 * <p>
 * @author    realMethods, Inc.
 */
public class TaskJMSExecutionHandlerMBean extends FrameworkDynamicMBean
{
// constructor

    /**
     * default constructor - public and empty per JMX specification for a DynamicMBean
     */
    public TaskJMSExecutionHandlerMBean()
    {
    	super( false /* don't JMX register yet */ );    	
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
	    
		if ( attributeName.equals( "TaskStatement" ) )
	    {
	    	attribute = taskStatement;
	    }
	    else if ( attributeName.equals( "ExecutionMsg" ) )
	    {
	    	attribute = executionMsg;
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
	    Object value = attribute.getValue();

	    if (name == null) 
	    {
	        throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name cannot be null"), 
						 "Cannot invoke the setter of " + getClassName() + " with null attribute name");
	    }
	    else if ( name.equals( "TaskStatement" ) )
	    {
	    	taskStatement = (String)value;
	    }
	    else if ( name.equals( "ExecutionMsg" ) )
	    {
	    	executionMsg = (String)value;
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
	 * Returns the taskStatement.
	 * @return		String
	 */
	public String getTaskStatement()
	{
		return ( taskStatement );
	}
	
	/**
	 * Returns the exeutionMsg.
	 * @return String
	 */
	public String getExecutionMsg()
	{
		return( executionMsg );
	}
	
// FrameworkDynamicMBean overloads

    /**
     * Returns the name of this class instance.  This implementation is
     * sufficient for singletons, but should be overloaded for sub-classes
     * requiring multiple instances.
     */
    public String getInstanceName() 
    {
        return( "TaskJMSExecutionHandler-" + executionMsg );
    }

    /**
     * Returns the description of this MBean
     * @return      String      
     */
    protected String getMBeanDescription()
    {
        return( "Task JMS Execution Handler Dynamic MBean" );
    }
    
    
    /**
     * Returns all the attributes for this class as an array of MBeanAttributeInfos.
     *
     * @return MBeanAttributeInfo[]
     */
    protected MBeanAttributeInfo[] getMBeanAttributes()
    {
        MBeanAttributeInfo[] mbAttributeInfo = new MBeanAttributeInfo[2];

        mbAttributeInfo[0] = new MBeanAttributeInfo( "TaskStatement", "java.lang.String", "Task statement", true, true, false );
        mbAttributeInfo[1] = new MBeanAttributeInfo( "ExecutionMsg", "java.lang.String", "Execution message", true, true, false );

        return( mbAttributeInfo );
    }
    
// attributes

    /**
     * bound Task statement
     */
    protected String taskStatement       = null;
    
    /**
     * bound execution message 
     */
    protected String executionMsg        = null;
    
	
}

/*
 * Change Log:
 * $Log: TaskJMSExecutionHandlerMBean.java,v $
 */
