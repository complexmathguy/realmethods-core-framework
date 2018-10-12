/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.startup;

 

import com.framework.common.FrameworkBaseObject;

/**
 * Abstract startup base class, to be implemented by any class wishing to be 
 * created and invoked to start();
 * <p>
 * Provides a single static block, used to tell the StartupManager to start().
 * <p> 
 * This becomes useful in scenarios where the Framework is used in a non-servlet scenario.
 * <p>
 * @author    realMethods, Inc.
 * @see		  com.framework.common.startup.StartupManager
 */
abstract public class FrameworkStartup 
	extends FrameworkBaseObject
	implements IFrameworkStartup
{
	// static block
	{
		StartupManager.getInstance().start( null, null );
	}
}

/*
 * Change Log:
 * $Log:  $
 */
