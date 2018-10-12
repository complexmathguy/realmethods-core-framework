/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.hook;

import com.framework.common.exception.HookAccessException;

/** 
 * IFrameworkHookManager interface
 * 
 * <p>
 * @author    realMethods, Inc.
 */
public interface IFrameworkHookManager
{	
    /**
     * refreshes the Map of hooks by loading the classnames from the
     * framework property handler
     */
    public void refreshHooks();
    	
    /**
     * Returns the hook interface associated with the class name to create
     * <p>
     * @param       hookClassName
     * @return      IFrameworkHook
     * @exception   HookAccessException
     */
    public IFrameworkHook getHook( String hookClassName )
    throws HookAccessException;
}

/*
 * Change Log:
 * $Log: IFrameworkHookManager.java,v $
 */

