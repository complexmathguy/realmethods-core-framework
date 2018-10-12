/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/

package com.framework.integration.persistent;

/**
 * Common interface for any persistent entity.
 * 
 * @author realMethods, Inc.
 */
public interface Persistent
{
	/**
	 * Called when saved
	 */	
	public void onSave();
	
	/**
	 * Called when loaded
	 */
	public void onLoad();
	
	/**
	 * Returns a saved T/F indicated
	 * @return	boolean
	 */
	public boolean isSaved();
}
