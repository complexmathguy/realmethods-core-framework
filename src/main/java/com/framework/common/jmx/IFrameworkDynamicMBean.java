/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.jmx;

import javax.management.DynamicMBean;
import javax.management.ObjectName;

/**
 * Common interface for all Framework JMX MBeans
 * 
 * <p>
 * @author    realMethods, Inc.
 */
public interface IFrameworkDynamicMBean 
	extends DynamicMBean, java.io.Serializable
{
    /**
     *  Handle MBean registration with MBeanServer
     */
    public void registerMBeanWithServer();

    /**
     *  Handle MBean unregistration with MBeanServer
     */
    public void unRegisterMBeanWithServer();

	/**
	 * Returns the MBean's ObjectName
	 * @return ObjectName
	 */
	public ObjectName getMBeanName();
}

/*
 * Change Log:
 * $Log: IFrameworkDynamicMBean.java,v $
 */
