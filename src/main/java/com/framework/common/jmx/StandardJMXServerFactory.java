/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.jmx;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.exception.CreateJMXServerException;

import com.framework.common.namespace.FrameworkNameSpace;

/**
 * Default provider of an otherwise unaccessible MBeanServer
 * <p>
 * @author    realMethods, Inc.
 */
public class StandardJMXServerFactory 
	extends FrameworkBaseObject 
	implements IFrameworkJMXServerFactory
{
	/**
	 * Return an inplace created MBeanServer that has the name 'rM' assigned.
	 * 
	 * @return 		MBeanServer
	 * @exception	CreateJMXServerException	if the MBeanServerFactory fails to create 
	 * 											an MBeanServer by the name specified by FrameworkNameSpace.MBEAN_DOMAIN
	 */
	public MBeanServer createMBeanServer()
	throws CreateJMXServerException
	{
		if ( mBeanServer == null )
		{
			mBeanServer = MBeanServerFactory.createMBeanServer( FrameworkNameSpace.MBEAN_DOMAIN );
            logInfoMessage( "Using StandardJMXServerFactory..." );
		}
								
		return( mBeanServer );
	}
		
// attributes 
	
	/**
	 * singleton instance of the created MBeanServer
	 */
    public static MBeanServer mBeanServer           	= null;
	
}

/*
 * Change Log:
 * $Log: StandardJMXServerFactory.java,v $
 */
