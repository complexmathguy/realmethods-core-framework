/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.hook;

import com.framework.common.exception.FrameworkCheckedException;
import com.framework.common.exception.HookProcessException;

/** 
 * Called whenever a FrameworkCheckedException is created.  This
 * doesn't mean that the exception was necessary thrown however.  Therefore
 * it may be wise to only create a FrameworkCheckedException when in the
 * midst of throwing it. 
 * <p>
 * @author    realMethods, Inc.
 */
public interface ICheckedExceptionThrownHook extends IFrameworkHook
{
	/**
     * the purpose of the hook
     * 
     * @param       exception
     * @exception   HookProcessException     
     */
    public void process( FrameworkCheckedException exception )
    throws HookProcessException;
}

/*
 * Change Log:
 * $Log: ICheckedExceptionThrownHook.java,v $
 */
