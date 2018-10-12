/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.presentation.translator;

import javax.servlet.ServletRequest;

import com.framework.integration.cache.IUserSessionObjectManager;

//***********************************
// Imports
//***********************************

/**
 * Common interface Framework Translators. Translators are used on the presentation tier in order
 * to provide formatting semantics to the data provided to a view for rendering.
 * <p>
 * @author    realMethods, Inc.
 */
public interface IBaseTranslator
{
    /** 
     * This initialization method replaces the init( IUserSessionObjectManager ), which
     * remains for backward capability.  The provided objects represent the locations
     * by which an object should be acquired, first looking in the servletRequest, then
     * the USOM.
     * <p>
     * @param       servletRequest     
     * @param       usom			cache where data resides to translate 
     */
    public void init( ServletRequest servletRequest, IUserSessionObjectManager usom );
    
    /** 
     * This initialization method remains for backward capability.  Use an overloaded
     * version.
     * <p>
     * @param       usom
     * @deprecated 
     */        
    public void init( IUserSessionObjectManager usom );
}

/*
 * Change Log:
 * $Log: IBaseTranslator.java,v $
 */
