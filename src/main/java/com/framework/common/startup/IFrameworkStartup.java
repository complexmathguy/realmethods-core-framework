/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.startup;

 

import com.framework.common.exception.FrameworkStartupException;

/**
 * Startup interface, to be implemented by any class wishing to be 
 * created and invoked to start() as well as invoked to stop() on Framework
 * shutdown.
 */
public interface IFrameworkStartup
{
	/**
	 * start indicator
	 * @throws FrameworkStartupException
	 */
	public void start()
	throws FrameworkStartupException;
	
	/**
	 * stop indicator
	 */
	public void stop();
}

/*
 * Change Log:
 * $Log:  $
 */
