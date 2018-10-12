/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.esb;

import com.framework.common.FrameworkBaseObject;

/**
 * 
 * Base Framework LoginModule used authenticate and authorize a user.
 * <p>
 * Authentication requires a CallbackHandler to provide a userid and password.
 * <p>
 * @author    realMethods, Inc.
 */
abstract public class ESBManager 
extends FrameworkBaseObject
implements IESBManager
{

	public java.util.Map<String, String> getProperties()
	{
		return( properties);
	}
	
	public void setProperties( java.util.Map<String, String> props )
	{
		properties = props;
	}
    
    protected java.util.Map<String, String>properties = new java.util.HashMap<String, String>();
}