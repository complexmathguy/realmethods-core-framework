/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.jmx;

import javax.management.MBeanServer;

import com.framework.common.exception.CreateJMXServerException;

/**
 * Implementor is expected to be the provider of the JMX MBean Server
 * used to register Framework related MBean components
 * <p>
 * @author    realMethods, Inc.
 */
public interface IFrameworkJMXServerFactory
{
	/**
	 * Return the MBeanServer in use
	 * 
	 * @return 		MBeanServer
	 * @exception	CreateJMXServerException	thrown if the implementation cannot create or obtain
	 * 											an MBeanServer
	 */
	public MBeanServer createMBeanServer()
	throws CreateJMXServerException;
		
}

/*
 * Change Log:
 * $Log: IFrameworkJMXServerFactory.java,v $
 */
